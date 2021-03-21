package com.halemaster.flexibilities.party;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.scoreboard.Team;

import java.util.List;

/**
 * Created by Halemaster on 7/23/2017.
 */
public class PartyManager {
    public static void createParty(EntityPlayer player) {
        if(player.getServer() != null) {
            player.getServer().getCommandManager().executeCommand(player.getServer(),
                    "scoreboard teams add "+getShortenedName(player)+" "+getShortenedPartyName(player));
            player.getServer().getCommandManager().executeCommand(player.getServer(),
                    "execute "+player.getName()+" ~ ~ ~ scoreboard teams join "+
                            getShortenedName(player)+" @s");
        }
    }

    public static String getShortenedName(Entity entity) {
        return entity.getName().substring(0, Math.min(16, entity.getName().length()));
    }

    public static String getPartyName(Entity entity) {
        return "Party_"+entity.getName();
    }

    public static String getShortenedPartyName(Entity entity) {
        return getPartyName(entity).substring(0, Math.min(32, getPartyName(entity).length()));
    }

    public static String getParty(Entity entity) {
        return entity.getTeam().getName();
    }

    public static boolean isLeader(Entity entity) {
        return getParty(entity).equals(getPartyName(entity));
    }

    public static void joinParty(Entity joiner, EntityPlayer leaderToJoin) {
        if(joiner.getServer() != null) {
            List<Entity> teamEntities = joiner.getEntityWorld().getEntities(Entity.class,
                    (entity) -> entity.getTeam().isSameTeam(joiner.getTeam()));
            teamEntities.remove(joiner);
            if (teamEntities.size() > 0 && joiner.getTeam().getName().equals(getShortenedName(joiner))) {
                EntityPlayer newLeader = null;
                for (Entity entity : teamEntities) {
                    if (entity instanceof EntityPlayer) {
                        newLeader = (EntityPlayer) entity;
                        break;
                    }
                }
                if (null != newLeader) {
                    createParty(newLeader);
                    teamEntities.remove(newLeader);
                    for (Entity entity : teamEntities) {
                        joinParty(entity, newLeader);
                    }
                }
            }

            joiner.getServer().getCommandManager().executeCommand(joiner.getServer(),
                    "execute "+leaderToJoin.getName()+" ~ ~ ~ scoreboard teams join "+
                            getShortenedName(joiner)+" @s");
        }
    }

    public static void promoteToLeader(EntityPlayer newLeader) {
        if(newLeader.getServer() != null) {
            List<Entity> teamEntities = newLeader.getEntityWorld().getEntities(Entity.class,
                    (entity) -> entity.getTeam().isSameTeam(newLeader.getTeam()));
            teamEntities.remove(newLeader);
            if (teamEntities.size() > 0 && !newLeader.getTeam().getName().equals(getShortenedName(newLeader))) {
                createParty(newLeader);
                for (Entity entity : teamEntities) {
                    joinParty(entity, newLeader);
                }
            }
        }
    }
}
