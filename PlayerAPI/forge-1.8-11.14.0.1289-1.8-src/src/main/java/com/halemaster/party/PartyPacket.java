package com.halemaster.party;

import io.netty.buffer.ByteBuf;

import java.util.UUID;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PartyPacket implements IMessage 
{

	private int member;

	public PartyPacket() 
	{
	}

	public PartyPacket(int member)
	{
		this.member = member;
	}

	@Override
	public void fromBytes(ByteBuf buf) 
	{
		member = Integer.valueOf(ByteBufUtils.readUTF8String(buf)); 
	}

	@Override
	public void toBytes(ByteBuf buf) 
	{
		ByteBufUtils.writeUTF8String(buf, String.valueOf(member));
	}

	public static class Handler implements IMessageHandler<PartyPacket, IMessage> 
	{

		@Override
		public IMessage onMessage(PartyPacket message, MessageContext ctx) 
		{
			Entity entity = Minecraft.getMinecraft().theWorld.getEntityByID(message.member); 
			Party party = PartyAPIMod.getParty(entity, ctx.side);
			PartyInfoPacket packet = new PartyInfoPacket(party, message.member);
			return packet;
		}
	}
}