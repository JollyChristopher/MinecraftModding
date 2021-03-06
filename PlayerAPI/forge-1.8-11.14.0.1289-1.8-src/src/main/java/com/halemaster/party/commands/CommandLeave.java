package com.halemaster.party.commands;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.common.FMLCommonHandler;

import com.halemaster.party.Party;
import com.halemaster.party.PartyAPIMod;

public class CommandLeave implements ICommand
{
	private static final String NAME = "leave";
	private List aliases = new ArrayList();

	@Override
	public String getName()
	{
		return NAME;
	}

	@Override
	public String getCommandUsage(ICommandSender icommandsender)
	{
		return "/" + NAME;
	}

	@Override
	public List getAliases()
	{
		return this.aliases;
	}

	@Override
	public void execute(ICommandSender icommandsender, String[] astring)
	{
		Party party = PartyAPIMod.getParty(icommandsender.getCommandSenderEntity(), 
				FMLCommonHandler.instance().getEffectiveSide());
		EntityPlayer self = (EntityPlayer) icommandsender.getCommandSenderEntity();
		boolean leader = party.getLeader() == self;
		String newLeader = null;
		party.leave(icommandsender.getCommandSenderEntity());
		if (leader && party.getLeader() != null)
		{
			newLeader = party.getLeader().getDisplayNameString() + " is now the leader of the group.";
		}
		
		
		for (Entity member : party.getMembers())
		{
			if (member instanceof EntityPlayer)
			{
				EntityPlayerMP player = 
						MinecraftServer.getServer().getConfigurationManager().getPlayerByUsername(member.getName());
				player.addChatMessage(new ChatComponentText(self.getDisplayNameString() + 
						" has left the party."));
				if (leader)
				{
					player.addChatMessage(new ChatComponentText(newLeader));
				}
			}
		}
		if (party.getSize() != 0)
		{
			icommandsender.addChatMessage(new ChatComponentText("You left the party!"));
		}
	}

	@Override
	public boolean canCommandSenderUse(ICommandSender icommandsender)
	{
		return icommandsender.getCommandSenderEntity() instanceof EntityPlayer;
	}

	@Override
	public List addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos)
	{
		return null;
	}

	@Override
	public boolean isUsernameIndex(String[] astring, int i)
	{
		return false;
	}

	@Override
	public int compareTo(Object o)
	{
		if (!(o instanceof String))
		{
			return -1;
		}
		return NAME.compareTo((String)o);
	}
}

