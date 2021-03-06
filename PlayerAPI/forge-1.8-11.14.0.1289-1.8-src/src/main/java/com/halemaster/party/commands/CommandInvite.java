package com.halemaster.party.commands;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.common.FMLCommonHandler;

import com.halemaster.party.Party;
import com.halemaster.party.PartyAPIMod;

public class CommandInvite implements ICommand
{
	private static final String NAME = "invite";
	private List aliases = new ArrayList();
	
	public CommandInvite()
	{
		aliases.add("inv");
	}

	@Override
	public String getName()
	{
		return NAME;
	}

	@Override
	public String getCommandUsage(ICommandSender icommandsender)
	{
		return "/" + NAME + " (playerName)";
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
		
		if (astring.length == 0)
		{
			List<Party> requests = PartyAPIMod.getRequests(((EntityPlayer)icommandsender.getCommandSenderEntity())
					.getDisplayNameString());
			
			icommandsender.addChatMessage(new ChatComponentText("Active Invites:"));
			if (requests != null)
			{
				for (Party request : requests)
				{
					if (request.getLeader() != null)
					{
						icommandsender.addChatMessage(new ChatComponentText(request.getLeader().getDisplayNameString()));
					}
				}
			}
			return;
		}
		
		if (!party.isLeader(icommandsender.getCommandSenderEntity()))
		{
			icommandsender.addChatMessage(new ChatComponentText("You need to be the leader to invite!").setChatStyle(
					new ChatStyle().setColor(EnumChatFormatting.RED)));
			return;
		}
		
		if (party.contains(astring[0]))
		{
			icommandsender.addChatMessage(new ChatComponentText(astring[0] + " is already in your party!").setChatStyle(
					new ChatStyle().setColor(EnumChatFormatting.RED)));
			return;
		}
		
		if(PartyAPIMod.addRequest(astring[0], party))
		{
			icommandsender.addChatMessage(new ChatComponentText(astring[0] + " has been invited to your group."));
		}
		else
		{
			icommandsender.addChatMessage(new ChatComponentText(astring[0] + " is not currently logged in!").setChatStyle(
					new ChatStyle().setColor(EnumChatFormatting.RED)));
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
