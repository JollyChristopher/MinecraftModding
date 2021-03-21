package com.halemaster.enchanting.testing.tests.basic.spell;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyFloat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.util.Arrays;

import net.minecraft.entity.DataWatcher;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

import org.junit.BeforeClass;
import org.junit.Test;

import com.halemaster.enchanting.EnchantingMod;
import com.halemaster.enchanting.basic.BasicSpellbookMod;
import com.halemaster.enchanting.basic.spell.SpellHealing;

public class SpellHealingTest {
	private static SpellHealing spell;

	@BeforeClass
	public static void init() {
		spell = (SpellHealing) EnchantingMod.getSpell(SpellHealing.enchID);
	}

	@Test
	public void testGetColor() {
		assertEquals("Color: white", 0xFFFFFF, spell.getColor());
	}

	@Test
	public void testMaxLevel() {
		assertEquals("Max level", 4, spell.getMaxLevel());
	}

	@Test
	public void testGetMinEnchantability() {
		assertEquals("Level 1", 1, spell.getMinEnchantability(1));
		assertEquals("Max Level", 25,
				spell.getMinEnchantability(spell.getMaxLevel()));
	}

	@Test
	public void testGetMaxEnchantability() {
		assertEquals("Level 1", 9, spell.getMaxEnchantability(1));
		assertEquals("Max Level", 36,
				spell.getMaxEnchantability(spell.getMaxLevel()));
	}

	@Test
	public void testGetCastTime() {
		assertEquals("Cast time", 32, spell.getCastTime());
	}

	@Test
	public void testGetRawDescription() {
		assertEquals("Length", 1, spell.getRawDescription().length);
		assertEquals("Description[0]", "enchantment.healing.desc.1",
				spell.getRawDescription()[0]);
	}

	@Test
	public void testOnUse() throws Exception {
		ItemStack scroll = new ItemStack(EnchantingMod.scroll);
		World world = mock(World.class);
		EntityPlayer player = mock(EntityPlayer.class);
		DataWatcher dataWatcher = mock(DataWatcher.class);
		IAttributeInstance attrInstance = mock(IAttributeInstance.class);
		when(world.getEntitiesWithinAABB((Class)any(), (AxisAlignedBB)any())).thenReturn(
				Arrays.asList(new EntityLivingBase[]{player}));
		when(player.getEntityAttribute(SharedMonsterAttributes.maxHealth)).thenReturn(attrInstance);
		when(attrInstance.getAttributeValue()).thenReturn(20.0);
		setDataWatcher(player, dataWatcher);
		
		spell.onUse(scroll, world, player, 1);
		verify(world, times(1)).playSoundAtEntity((Entity)any(), anyString(), anyFloat(), anyFloat());
		
		when(dataWatcher.getWatchableObjectFloat(6)).thenReturn(20.0f);
		spell.onUse(scroll, world, player, 1);
		verify(world, times(1)).playSoundAtEntity((Entity)any(), anyString(), anyFloat(), anyFloat());
	}
	
	@Test
	public void testGetRadius() {
		assertEquals("Level 1", 0.5, spell.getRadius(1), 0.1);
		assertEquals("Max Level", 15.0, spell.getRadius(spell.getMaxLevel()), 0.1);
	}
	
	@Test
	public void testAuraHit() throws Exception {
		ItemStack scroll = new ItemStack(EnchantingMod.scroll);
		World world = mock(World.class);
		EntityPlayer player = mock(EntityPlayer.class);
		EntityLivingBase notPlayer = mock(EntityLivingBase.class);
		DataWatcher dataWatcher = mock(DataWatcher.class);
		IAttributeInstance attrInstance = mock(IAttributeInstance.class);
		Team team = mock(Team.class);
		when(world.getEntitiesWithinAABB((Class)any(), (AxisAlignedBB)any())).thenReturn(
				Arrays.asList(new EntityLivingBase[]{player}));
		when(player.getEntityAttribute(SharedMonsterAttributes.maxHealth)).thenReturn(attrInstance);
		setDataWatcher(player, dataWatcher);
		when(attrInstance.getAttributeValue()).thenReturn(20.0);
		when(notPlayer.getEntityAttribute(SharedMonsterAttributes.maxHealth)).thenReturn(attrInstance);
		setDataWatcher(notPlayer, dataWatcher);
		when(dataWatcher.getWatchableObjectFloat(6)).thenReturn(20.0f);
		
		assertFalse("Should be full health", spell.auraHit(scroll, world, player, 1, notPlayer));
		verify(player, times(0)).heal(anyFloat());
		verify(notPlayer, times(0)).heal(anyFloat());
		
		when(dataWatcher.getWatchableObjectFloat(6)).thenReturn(10.0f);
		assertFalse("Can't heal other entity", spell.auraHit(scroll, world, player, 1, notPlayer));
		verify(player, times(0)).heal(anyFloat());
		verify(notPlayer, times(0)).heal(anyFloat());
		
		assertTrue("Should heal self", spell.auraHit(scroll, world, player, 1, player));
		verify(player, times(1)).heal(anyFloat());
		verify(notPlayer, times(0)).heal(anyFloat());
		
		when(player.getTeam()).thenReturn(team);
		assertFalse("Can't heal entity that isn't on team", spell.auraHit(scroll, world, player, 1, notPlayer));
		verify(player, times(1)).heal(anyFloat());
		verify(notPlayer, times(0)).heal(anyFloat());
		
		when(notPlayer.getTeam()).thenReturn(team);
		when(team.isSameTeam(team)).thenReturn(true);
		assertTrue("Should heal teammate", spell.auraHit(scroll, world, player, 1, notPlayer));
		verify(player, times(1)).heal(anyFloat());
		verify(notPlayer, times(1)).heal(anyFloat());
		
		assertTrue("Should only heal self by 2", spell.auraHit(scroll, world, player, 1, player));
		verify(player, times(0)).heal(4);
		verify(player, times(2)).heal(2);
		verify(notPlayer, times(1)).heal(anyFloat());
	}
	
	@Test
	public void testGetRenderName(){
		assertEquals("Render name", "healing", spell.getRenderName());
	}
	
	@Test
	public void testGetModId(){
		assertEquals("Mod id", BasicSpellbookMod.MODID, spell.getModId());
	}
	
	public static void setDataWatcher(EntityLivingBase living, DataWatcher watcher) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException
	{
		Field field = Entity.class.getDeclaredField("dataWatcher");
		field.setAccessible(true);
		field.set(living, watcher);
	}
}
