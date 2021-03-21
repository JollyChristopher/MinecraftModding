package com.halemaster.flexibilities.command;

import com.halemaster.flexibilities.party.PartyManager;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Halemaster on 7/23/2017.
 */
public class AcceptInviteCommand extends CommandBase {
    private static Map<Entity, EntityPlayer> invites = new HashMap<>();

    public static void addInvite(Entity entity, EntityPlayer inviter) {
        invites.put(entity, inviter);
    }

    public static void removeInvite(Entity entity) {
        invites.remove(entity);
    }

    @Override
    public String getName() {
        return "accept";
    }

    @Override
    public int getRequiredPermissionLevel()
    {
        return 0;
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender)
    {
        return true;
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "commands.accept.usage";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length < 1)
        {
            throw new WrongUsageException("commands.invite.usage", new Object[0]);
        }
        else if(sender instanceof Entity){
            Entity entity = (Entity) sender;
            EntityPlayer inviter = invites.get(entity);
            if(!PartyManager.isLeader(inviter)) {
                throw new WrongUsageException("commands.accept.notLeader", new Object[]{inviter.getName()});
            }

            PartyManager.joinParty(entity, inviter);
            removeInvite(entity);
        }
    }
}
