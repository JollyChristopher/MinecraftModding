package com.halemaster.party;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.relauncher.Side;

public class Party 
{
	protected CopyOnWriteArrayList<Integer> members = new CopyOnWriteArrayList<Integer>();
	
	public void join(Entity entity, Side side)
	{
		if (entity == null)
		{
			return;
		}
		
		Party previousParty = PartyAPIMod.getParty(entity, side, false);
		if (null != previousParty)
		{
			previousParty.leave(entity);
		}
		
		members.add(entity.getEntityId());
		PartyAPIMod.partyMap.put(entity.getEntityId(), this);
		if (entity instanceof EntityPlayer)
		{
			PartyAPIMod.requests.remove(((EntityPlayer) entity).getDisplayNameString());
		}
	}
	
	public void leave(Entity entity)
	{
		members.remove((Integer) entity.getEntityId());
		PartyAPIMod.partyMap.remove((Integer) entity.getEntityId());
	}
	
	public boolean contains(Entity entity)
	{
		return members.contains(entity.getEntityId());
	}
	
	public boolean contains(String playerName)
	{
		if (null == MinecraftServer.getServer().getConfigurationManager()
				.getPlayerByUsername(playerName))
		{
			return false;
		}
		
		return members.contains(MinecraftServer.getServer().getConfigurationManager()
				.getPlayerByUsername(playerName).getEntityId());
	}
	
	public EntityPlayer getLeader()
	{
		EntityPlayer leader = null;
		List<Entity> memberList = getMembers();
		
		for (Entity entity : memberList)
		{
			if (entity instanceof EntityPlayer)
			{
				leader = (EntityPlayer) entity;
				break;
			}
		}
		
		// No players, means there is no leader!
		if (null == leader)
		{
			for (Entity entity : memberList)
			{
				leave(entity);
			}
		}
		
		return leader;
	}
	
	public boolean isLeader(Entity member)
	{
		EntityPlayer leader = getLeader();
		if (null == leader)
		{
			return false;
		}
		
		if (!(member instanceof EntityPlayer))
		{
			return false;
		}
		
		return leader.getDisplayNameString().equals(((EntityPlayer) member)
				.getDisplayNameString());
	}
	
	public void promoteLeader(EntityPlayer player)
	{
		if (contains(player))
		{
			members.remove((Integer) player.getEntityId());
			members.add(0, player.getEntityId());
		}
	}
	
	public int getSize()
	{
		return members.size();
	}
	
	public Entity getMember(int member)
	{
		return Minecraft.getMinecraft().theWorld.getEntityByID(members.get(member));
	}
	
	public EntityPlayer getMember(String playerName)
	{
		EntityPlayer player = null;
		playerName = playerName.toLowerCase();
		
		for (Entity member : getMembers())
		{
			if (member instanceof EntityPlayer)
			{
				player = (EntityPlayer) member;
				if (player.getDisplayNameString().toLowerCase().equals(playerName))
				{
					break;
				}
				player = null;
			}
		}
		
		return player;
	}
	
	public List<Entity> getMembers()
	{
		List<Entity> memberList = new ArrayList<Entity>();
		
		for (Integer uuid : members)
		{
			Entity entity = Minecraft.getMinecraft().theWorld.getEntityByID(uuid);
			if (null != entity)
			{
				memberList.add(entity);
			}
			else
			{
				System.out.println(uuid + " produced null entity!");
			}
		}
		
		return memberList;
	}
	
	@Override
	public String toString()
	{
		String partyString = "";
		for (Entity entity : getMembers())
		{
			partyString += entity.getEntityId();
			partyString += ",";
		}
		if (partyString.length() > 0)
		{
			partyString = partyString.substring(0, partyString.length() - 1);
		}
		
		return partyString;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((members == null) ? 0 : members.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Party other = (Party) obj;
		if (members == null) {
			if (other.members != null)
				return false;
		} else if (!members.equals(other.members))
			return false;
		return true;
	}
	
	public void empty()
	{
		for (Entity member : getMembers())
		{
			leave(member);
		}
	}
	
	public void convert (Party other)
	{
		empty();
		for (Entity member : other.getMembers())
		{
			join(member, Side.SERVER);
		}
	}
}
