package com.halemaster.flexibilities.command;

import com.halemaster.flexibilities.party.PartyManager;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

/**
 * Created by Halemaster on 7/23/2017.
 */
public class InvitePartyCommand extends CommandBase {
    @Override
    public String getName() {
        return "invite";
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
        return "commands.invite.usage";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length < 1)
        {
            throw new WrongUsageException("commands.invite.usage", new Object[0]);
        }
        else if(sender instanceof EntityPlayer){
            EntityPlayer entity = (EntityPlayer) sender;
            if(!PartyManager.isLeader(entity)) {
                throw new WrongUsageException("commands.invite.notLeader", new Object[0]);
            }

            Entity inviteEntity = getEntity(server, sender, args[0]);

            AcceptInviteCommand.addInvite(inviteEntity, entity);
        }
    }
}
