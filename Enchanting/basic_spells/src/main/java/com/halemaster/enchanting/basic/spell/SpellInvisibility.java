package com.halemaster.enchanting.basic.spell;

import java.util.Random;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import com.halemaster.enchanting.EnchantingMod;
import com.halemaster.enchanting.basic.BasicSpellbookMod;
import com.halemaster.enchanting.spell.SpellAura;

public class SpellInvisibility extends SpellAura
{
	public static final String NAME = "invisibility";
	public static final int enchID = 111;
	public static final int DURATION = 20 * 15;
	public static final String[] DESCRIPTION = new String[] { "enchantment.invisibility.desc.1" };
	private static Random random = new Random();

	public SpellInvisibility()
	{
		super(enchID, new ResourceLocation(BasicSpellbookMod.MODID, NAME), EnumRarity.RARE);
		this.setName(NAME);
	}

	@Override
	public int getColor()
	{
		return Potion.invisibility.getLiquidColor();
	}

	@Override
	public boolean onUse(ItemStack stack, World worldIn, EntityPlayer playerIn, int level)
	{
		boolean used = super.onUse(stack, worldIn, playerIn, level);

		if (used)
		{
			worldIn.playSoundAtEntity(playerIn, "mob.enderdragon.wings", 0.5F,
					0.4F / (random.nextFloat() * 0.4F + 0.8F));
			this.createAuraParticles(worldIn, EnumParticleTypes.SPELL_MOB, playerIn, level, getColor(),  1, 2);
		}

		return used;
	}

	@Override
	public int getMaxLevel()
	{
		return 3;
	}

	@Override
	public int getMinEnchantability(int enchantmentLevel)
	{
		return (enchantmentLevel) * 8;
	}

	@Override
	public int getMaxEnchantability(int enchantmentLevel)
	{
		return enchantmentLevel * 14;
	}

	@Override
	public int getCastTime()
	{
		return 32;
	}

	@Override
	public String[] getRawDescription()
	{
		return DESCRIPTION;
	}

	@Override
	public double getRadius(int level)
	{
		if (level == 1)
		{
			return 0.5;
		}
		return 5*(level - 1);
	}

	@Override
	public boolean auraHit(ItemStack stack, World worldIn, EntityPlayer playerIn, int level, EntityLivingBase entity)
	{
		if (playerIn.getTeam() != null && playerIn.getTeam().isSameTeam(entity.getTeam()))
		{
			entity.addPotionEffect(new PotionEffect(Potion.invisibility.id, DURATION * level, 0));
			return true;
		}
		else if (playerIn.equals(entity))
		{
			entity.addPotionEffect(new PotionEffect(Potion.invisibility.id, DURATION * level, 0));
			return true;
		}
		return false;
	}

	@Override
	public Item getRender() {
		return null;
	}

	@Override
	public String getRenderName() {
		return NAME;
	}

	@Override
	public String getModId() {
		return BasicSpellbookMod.MODID;
	}
}
