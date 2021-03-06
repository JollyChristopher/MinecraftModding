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

import org.apache.commons.lang3.StringUtils;

import com.halemaster.party.Party;
import com.halemaster.party.PartyAPIMod;

public class CommandPartyChat implements ICommand
{
	private static final String NAME = "party";
	private List aliases = new ArrayList();
	
	public CommandPartyChat()
	{
		aliases.add("p");
	}

	@Override
	public String getName()
	{
		return NAME;
	}

	@Override
	public String getCommandUsage(ICommandSender icommandsender)
	{
		return "/" + NAME + " <chatText>";
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
	    String fullText = convertText(astring);
	    
	    if (!StringUtils.isEmpty(fullText))
	    {			
			for (Entity member : party.getMembers())
			{
				if (member instanceof EntityPlayer)
				{
					EntityPlayerMP player = 
							MinecraftServer.getServer().getConfigurationManager().getPlayerByUsername(member.getName());
					player.addChatMessage(new ChatComponentText(((EntityPlayer) icommandsender
							.getCommandSenderEntity()).getDisplayNameString() + ": " + fullText).setChatStyle(
									new ChatStyle().setColor(EnumChatFormatting.BLUE)));
				}
			}
	    }
	}
	
	private String convertText(String[] text)
	{
		String converted = "";
		
		for (String subtext : text)
		{
			converted += subtext;
			converted += " ";
		}
		converted = converted.substring(0, converted.length() - 1);
		
		return converted;
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
