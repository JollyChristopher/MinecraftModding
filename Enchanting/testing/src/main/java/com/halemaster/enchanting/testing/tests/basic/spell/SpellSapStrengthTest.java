package com.halemaster.enchanting.testing.tests.basic.spell;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyDouble;
import static org.mockito.Matchers.anyFloat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

import org.junit.BeforeClass;
import org.junit.Test;

import com.halemaster.enchanting.EnchantingMod;
import com.halemaster.enchanting.basic.spell.SpellSapStrength;
import com.halemaster.enchanting.spell.EntitySpellProjectile;

public class SpellSapStrengthTest {
	private static SpellSapStrength spell;

	@BeforeClass
	public static void init() {
		spell = (SpellSapStrength) EnchantingMod.getSpell(SpellSapStrength.enchID);
	}

	@Test
	public void testGetColor() {
		assertEquals("Color: weakness", Potion.weakness.getLiquidColor(), spell.getColor());
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
		assertEquals("Description[0]", "enchantment.sapstrength.desc.1",
				spell.getRawDescription()[0]);
	}

	@Test
	public void testOnImpact() {
		Team team = mock(Team.class);
		MovingObjectPosition position = mock(MovingObjectPosition.class);
		EntityLivingBase thrower = mock(EntityLivingBase.class);
		Entity entity = mock(Entity.class);
		World world = mock(World.class);
		EntitySpellProjectile projectile = new EntitySpellProjectile(null);

		// run null as entityHit
		spell.onImpact(world, projectile, thrower, position, 1);
		verify(thrower, times(0)).addPotionEffect((PotionEffect) any());

		// run entity as entityHit
		position.entityHit = entity;
		spell.onImpact(world, projectile, thrower, position, 1);
		verify(thrower, times(0)).addPotionEffect((PotionEffect) any());

		// run above and entityLiving and disallow friendly fire
		position.entityHit = thrower;
		when(thrower.getTeam()).thenReturn(team);
		when(team.isSameTeam(team)).thenReturn(true);
		when(team.getAllowFriendlyFire()).thenReturn(false);
		spell.onImpact(world, projectile, thrower, position, 1);
		verify(thrower, times(0)).addPotionEffect((PotionEffect) any());

		// run above and entityLiving and allow friendly fire
		when(team.getAllowFriendlyFire()).thenReturn(true);
		spell.onImpact(world, projectile, thrower, position, 1);
		verify(thrower,times(1)).addPotionEffect((PotionEffect) any());

		// run thrower with team
		when(team.isSameTeam(team)).thenReturn(false);
		spell.onImpact(world, projectile, thrower, position, 1);
		verify(thrower,times(2)).addPotionEffect((PotionEffect) any());

		// run entityLiving as hit
		when(thrower.getTeam()).thenReturn(null);
		spell.onImpact(world, projectile, thrower, position, 1);
		verify(thrower,times(3)).addPotionEffect((PotionEffect) any());
	}

	@Test
	public void testFire() {
		EntityPlayer player = mock(EntityPlayer.class);
		World world = mock(World.class);
		spell.fire(world, player, 0);
		verify(world).playSoundAtEntity((Entity) any(), anyString(),
				anyFloat(), anyFloat());
	}

	@Test
	public void testOnUpdate() {
		World world = mock(World.class);
		EntitySpellProjectile projectile = new EntitySpellProjectile(null);
		spell.onUpdate(world, projectile, 0);
		verify(world).spawnParticle((EnumParticleTypes) any(), anyDouble(), anyDouble(),
				anyDouble(), anyDouble(), anyDouble(), anyDouble());
	}
}
