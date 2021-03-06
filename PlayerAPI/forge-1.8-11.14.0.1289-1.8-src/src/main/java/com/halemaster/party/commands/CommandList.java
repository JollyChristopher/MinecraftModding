package com.halemaster.party.commands;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.fml.common.FMLCommonHandler;

import com.halemaster.party.Party;
import com.halemaster.party.PartyAPIMod;

public class CommandList implements ICommand
{
	private static final String NAME = "list";
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
		
		String leader = "Leader: ";
		String memberName;
		
		for (Entity member : party.getMembers())
		{
			if (member instanceof EntityPlayer)
			{
				memberName = leader;
				leader = "";
			}
			else
			{
				memberName = "";
			}
			memberName += getMemberName(member);
			icommandsender.addChatMessage(new ChatComponentText(memberName));
		}
	}
	
	private String getMemberName(Entity member)
	{
		if (member instanceof EntityPlayer)
		{
			return ((EntityPlayer) member).getDisplayNameString();
		}
		else
		{
			return member.getClass().getName().replaceFirst("Entity", "");
		}
	}

	@Override
	public boolean canCommandSenderUse(ICommandSender icommandsender)
	{
		return true;
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