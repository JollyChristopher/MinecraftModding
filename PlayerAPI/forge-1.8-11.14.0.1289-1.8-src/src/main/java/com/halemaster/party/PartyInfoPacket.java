package com.halemaster.party;

import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class PartyInfoPacket implements IMessage 
{

	private Party party;
	private Integer sender;

	public PartyInfoPacket() 
	{
	}

	public PartyInfoPacket(Party party, Integer sender)
	{
		this.party = party;
		this.sender = sender;
	}

	@Override
	public void fromBytes(ByteBuf buf) 
	{
		String full = ByteBufUtils.readUTF8String(buf);
		String[] idStrings = full.split(",");
		List<Integer> ids = new ArrayList<Integer>();
		
		if (!StringUtils.isEmpty(idStrings[0]))
		{
			sender = Integer.valueOf(idStrings[0]);
		}
		for (int i = 1; i < idStrings.length; i++)
		{
			if (!StringUtils.isEmpty(idStrings[i]))
			{
				ids.add(Integer.valueOf(idStrings[i]));
			}
		}
		party = new Party();
		for (Integer id : ids)
		{
			party.members.add(id);
		}
	}

	@Override
	public void toBytes(ByteBuf buf) 
	{
		String partyString = "";
		if (null != party)
		{
			partyString = party.toString();
		}
		ByteBufUtils.writeUTF8String(buf, sender.toString() + "," + partyString);
	}

	public static class Handler implements IMessageHandler<PartyInfoPacket, IMessage> 
	{
		@Override
		public IMessage onMessage(PartyInfoPacket message, MessageContext ctx) 
		{
			Party current = PartyAPIMod.getParty(Minecraft.getMinecraft().theWorld.getEntityByID(message.sender), 
					Side.SERVER);
			if (!message.party.equals(current))
			{
				if (current != null)
				{
					current.convert(message.party);
				}
				else
				{
					Party party = new Party();
					party.convert(message.party);
				}
			}
			
			return null; // no response in this case
		}
	}
}
