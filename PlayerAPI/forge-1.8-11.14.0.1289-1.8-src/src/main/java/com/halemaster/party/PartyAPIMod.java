package com.halemaster.party;

import gui.GuiParty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

import com.halemaster.party.commands.CommandInvite;
import com.halemaster.party.commands.CommandJoin;
import com.halemaster.party.commands.CommandKick;
import com.halemaster.party.commands.CommandLeave;
import com.halemaster.party.commands.CommandList;
import com.halemaster.party.commands.CommandPartyChat;
import com.halemaster.party.commands.CommandPromote;

@Mod(modid = PartyAPIMod.MODID, version = PartyAPIMod.VERSION)
public class PartyAPIMod
{
	public static final String MODID = "partyapi";
	public static final String VERSION = "0.1";
	public static SimpleNetworkWrapper partyChannel;
	
	@Instance ( MODID )
    public static PartyAPIMod instance;
	
	@EventHandler
    public void preInit(FMLPreInitializationEvent event) 
	{
		partyChannel = NetworkRegistry.INSTANCE.newSimpleChannel("partyAPIchannel");
       	partyChannel.registerMessage(PartyPacket.Handler.class, PartyPacket.class, 0, Side.SERVER);
       	partyChannel.registerMessage(PartyInfoPacket.Handler.class, PartyInfoPacket.class, 1, Side.CLIENT);
    }

	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		//MinecraftForge.EVENT_BUS.register(new GuiBuffs(Minecraft.getMinecraft()));
		MinecraftForge.EVENT_BUS.register(new GuiParty(Minecraft.getMinecraft()));
	}

	@EventHandler
	public void serverLoad(FMLServerStartingEvent event)
	{
		event.registerServerCommand(new CommandList());
		event.registerServerCommand(new CommandInvite());
		event.registerServerCommand(new CommandJoin());
		event.registerServerCommand(new CommandLeave());
		event.registerServerCommand(new CommandKick());
		event.registerServerCommand(new CommandPromote());
		event.registerServerCommand(new CommandPartyChat());
	}

	protected static Map<Integer, Party> partyMap = new HashMap<Integer, Party>();
	protected static Map<String, List<Party>> requests = new HashMap<String, List<Party>>();

	public static Party getParty(Entity entity, Side side, boolean createParty)
	{
		if (side == Side.CLIENT)
		{
			partyChannel.sendToServer(new PartyPacket(entity.getEntityId()));
		}
		if (null != entity)
		{
			Party party = partyMap.get(entity.getEntityId());
			if (null == party && createParty)
			{
				party = new Party();
				party.join(entity, FMLCommonHandler.instance().getEffectiveSide());
			}
			return party;
		}
		return null;
	}
	
	public static Party getParty(Entity entity, Side side)
	{
		return getParty(entity, side, true);
	}

	public static Party getParty(String playerName, Side side)
	{		
		playerName = playerName.toLowerCase();
		return getParty(MinecraftServer.getServer().getConfigurationManager()
				.getPlayerByUsername((playerName)), side);
	}
	
	public static List<Party> getRequests(String playerName)
	{
		playerName = playerName.toLowerCase();
		List<Party> parties = requests.get(playerName);
		if (null == parties)
		{
			parties = new ArrayList<Party>();
			requests.put(playerName, parties);
		}
		return parties;
	}
	
	public static boolean addRequest(String playerName, Party party)
	{
		playerName = playerName.toLowerCase();
		EntityPlayer player = MinecraftServer.getServer().getConfigurationManager()
				.getPlayerByUsername((playerName));
		if (null == player)
		{
			return false;
		}
		List<Party> parties = getRequests(playerName);
		
		parties.add(party);
		player.addChatComponentMessage(new ChatComponentText("You have been invited to join " + 
				party.getLeader().getDisplayNameString() + "'s party. Type /join to join their party!"));
		return true;
	}

	public static EntityPlayer getNPCFollowing(Entity entity)
	{
		throw new UnsupportedOperationException("NPCs cannot follow players at this time!");
	}
}
