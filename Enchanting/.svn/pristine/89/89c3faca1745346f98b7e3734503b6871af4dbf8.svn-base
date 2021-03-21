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

import java.util.Arrays;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

import org.junit.BeforeClass;
import org.junit.Test;

import com.halemaster.enchanting.EnchantingMod;
import com.halemaster.enchanting.basic.BasicSpellbookMod;
import com.halemaster.enchanting.basic.spell.SpellHopping;

public class SpellHoppingTest {
	private static SpellHopping spell;

	@BeforeClass
	public static void init() {
		spell = (SpellHopping) EnchantingMod.getSpell(SpellHopping.enchID);
	}

	@Test
	public void testGetColor() {
		assertEquals("Color: jump", Potion.jump.getLiquidColor(), spell.getColor());
	}

	@Test
	public void testMaxLevel() {
		assertEquals("Max level", 4, spell.getMaxLevel());
	}

	@Test
	public void testGetMinEnchantability() {
		assertEquals("Level 1", 1, spell.getMinEnchantability(1));
		assertEquals("Max Level", 22,
				spell.getMinEnchantability(spell.getMaxLevel()));
	}

	@Test
	public void testGetMaxEnchantability() {
		assertEquals("Level 1", 8, spell.getMaxEnchantability(1));
		assertEquals("Max Level", 32,
				spell.getMaxEnchantability(spell.getMaxLevel()));
	}

	@Test
	public void testGetCastTime() {
		assertEquals("Cast time", 16, spell.getCastTime());
	}

	@Test
	public void testGetRawDescription() {
		assertEquals("Length", 1, spell.getRawDescription().length);
		assertEquals("Description[0]", "enchantment.hopping.desc.1",
				spell.getRawDescription()[0]);
	}

	@Test
	public void testOnUse() throws Exception {
		ItemStack scroll = new ItemStack(EnchantingMod.scroll);
		World world = mock(World.class);
		EntityPlayer player = mock(EntityPlayer.class);
		when(world.getEntitiesWithinAABB((Class)any(), (AxisAlignedBB)any())).thenReturn(
				Arrays.asList(new EntityLivingBase[]{player}));
		
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
		Team team = mock(Team.class);
		when(world.getEntitiesWithinAABB((Class)any(), (AxisAlignedBB)any())).thenReturn(
				Arrays.asList(new EntityLivingBase[]{player}));
		
		assertFalse("Can't use on other entity", spell.auraHit(scroll, world, player, 1, notPlayer));
		verify(player, times(0)).addPotionEffect((PotionEffect) any());
		verify(notPlayer, times(0)).addPotionEffect((PotionEffect) any());
		
		assertTrue("Should use on self", spell.auraHit(scroll, world, player, 1, player));
		verify(player, times(1)).addPotionEffect((PotionEffect) any());
		verify(notPlayer, times(0)).addPotionEffect((PotionEffect) any());
		
		when(player.getTeam()).thenReturn(team);
		assertFalse("Can't use on entity that isn't on team", spell.auraHit(scroll, world, player, 1, 
				notPlayer));
		verify(player, times(1)).addPotionEffect((PotionEffect) any());
		verify(notPlayer, times(0)).addPotionEffect((PotionEffect) any());
		
		when(notPlayer.getTeam()).thenReturn(team);
		when(team.isSameTeam(team)).thenReturn(true);
		assertTrue("Should use on teammate", spell.auraHit(scroll, world, player, 1, notPlayer));
		verify(player, times(1)).addPotionEffect((PotionEffect) any());
		verify(notPlayer, times(1)).addPotionEffect((PotionEffect) any());
	}
	
	@Test
	public void testGetRenderName(){
		assertEquals("Render name", "hopping", spell.getRenderName());
	}
	
	@Test
	public void testGetModId(){
		assertEquals("Mod id", BasicSpellbookMod.MODID, spell.getModId());
	}
}
