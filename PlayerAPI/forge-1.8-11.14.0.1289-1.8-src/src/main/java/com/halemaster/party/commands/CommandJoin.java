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

public class CommandJoin implements ICommand
{
	private static final String NAME = "join";
	private List aliases = new ArrayList();

	@Override
	public String getName()
	{
		return NAME;
	}

	@Override
	public String getCommandUsage(ICommandSender icommandsender)
	{
		return "/" + NAME + " (inviterName)";
	}

	@Override
	public List getAliases()
	{
		return this.aliases;
	}

	@Override
	public void execute(ICommandSender icommandsender, String[] astring)
	{
		EntityPlayer self = (EntityPlayer) icommandsender.getCommandSenderEntity();
		
		if(astring.length == 1)
		{
			join(self, astring[0]);
			return;
		}
		
		join(self);
	}
	
	private void join(EntityPlayer self)
	{
		List<Party> requests = PartyAPIMod.getRequests(self.getDisplayNameString());
		if (requests.size() > 0)
		{
			if (requests.get(requests.size() - 1).getLeader() != null)
			{
				join(self, requests.get(requests.size() - 1).getLeader().getDisplayNameString().toLowerCase());
			}
			else
			{
				requests.remove(requests.size() - 1);
				join(self);
			}
		}
		else
		{
			self.addChatMessage(new ChatComponentText("You have no pending requests!").setChatStyle(
					new ChatStyle().setColor(EnumChatFormatting.RED)));
		}
	}
	
	private void join(EntityPlayer self, String playerName)
	{
		List<Party> requests = PartyAPIMod.getRequests(self.getDisplayNameString());
		for(Party request : requests)
		{
			if(request.getLeader().getDisplayNameString().toLowerCase().equals(playerName))
			{
				for (Entity member : request.getMembers())
				{
					if (member instanceof EntityPlayer)
					{
						EntityPlayerMP player = 
								MinecraftServer.getServer().getConfigurationManager()
								.getPlayerByUsername(member.getName());
						player.addChatMessage(new ChatComponentText(self.getDisplayNameString() + 
								" has joined the party."));
					}
				}
				request.join(self, FMLCommonHandler.instance().getEffectiveSide());
				self.addChatMessage(new ChatComponentText("You have joined " + playerName + "'s party!"));
				return;
			}
		}
		
		self.addChatMessage(new ChatComponentText(playerName + " hasn't invited you to their group!").setChatStyle(
				new ChatStyle().setColor(EnumChatFormatting.RED)));
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
