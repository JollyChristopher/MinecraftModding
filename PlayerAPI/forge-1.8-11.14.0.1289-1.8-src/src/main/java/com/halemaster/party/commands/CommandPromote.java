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

public class CommandPromote implements ICommand
{
	private static final String NAME = "promote";
	private List aliases = new ArrayList();
	
	public CommandPromote()
	{
		aliases.add("pro");
	}

	@Override
	public String getName()
	{
		return NAME;
	}

	@Override
	public String getCommandUsage(ICommandSender icommandsender)
	{
		return "/" + NAME + " <playerName>";
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
		
		if (astring.length != 1)
		{
			icommandsender.addChatMessage(new ChatComponentText("You must specify a player to promote!").setChatStyle(
					new ChatStyle().setColor(EnumChatFormatting.RED)));
			return;
		}
		
		if (!party.isLeader(icommandsender.getCommandSenderEntity()))
		{
			icommandsender.addChatMessage(new ChatComponentText("You must be the leader to promote a player!").setChatStyle(
					new ChatStyle().setColor(EnumChatFormatting.RED)));
			return;
		}
		
		EntityPlayer promote = party.getMember(astring[0]);
		
		if (null == promote)
		{
			icommandsender.addChatMessage(new ChatComponentText("That player isn't in your party!").setChatStyle(
					new ChatStyle().setColor(EnumChatFormatting.RED)));
			return;
		}
		
		if (promote.getDisplayNameString().toLowerCase().equals(((EntityPlayer) icommandsender.getCommandSenderEntity())
				.getDisplayNameString().toLowerCase()))
		{
			icommandsender.addChatMessage(new ChatComponentText("You cannot promote yourself!").setChatStyle(
					new ChatStyle().setColor(EnumChatFormatting.RED)));
			return;
		}
		
		party.promoteLeader(promote);
		
		for (Entity member : party.getMembers())
		{
			if (member instanceof EntityPlayer)
			{
				EntityPlayerMP player = 
						MinecraftServer.getServer().getConfigurationManager().getPlayerByUsername(member.getName());
				player.addChatMessage(new ChatComponentText(promote.getDisplayNameString() + 
						" is now the leader of the group."));
			}
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