package com.halemaster.enchanting.basic.spell;

import java.util.Random;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import com.halemaster.enchanting.EnchantingMod;
import com.halemaster.enchanting.basic.BasicSpellbookMod;
import com.halemaster.enchanting.spell.Spell;

public class SpellAdrenaline extends Spell
{
	public static final String NAME = "adrenaline";
	public static final int enchID = 100;
	public static final int DURATION = 1;
	public static final String[] DESCRIPTION = new String[] { "enchantment.adrenaline.desc.1" };
	private static Random random = new Random();

	public SpellAdrenaline()
	{
		super(enchID, new ResourceLocation(BasicSpellbookMod.MODID, NAME), EnumRarity.COMMON);
		this.setName(NAME);
	}

	@Override
	public int getColor()
	{
		return Potion.saturation.getLiquidColor();
	}

	@Override
	public boolean onUse(ItemStack stack, World worldIn, EntityPlayer playerIn, int level)
	{
		boolean used = false;
		
		if(!worldIn.isRemote && playerIn.getFoodStats().needFood())
		{
			playerIn.addPotionEffect(new PotionEffect(Potion.saturation.id, DURATION, level - 1));
			used = true;
		}

		if (used)
		{
			worldIn.playSoundAtEntity(playerIn, "random.eat", 0.5F,
					0.4F / (random.nextFloat() * 0.4F + 0.8F));
		}

		return used;
	}

	@Override
	public int getMaxLevel()
	{
		return 5;
	}

	@Override
	public int getMinEnchantability(int enchantmentLevel)
	{
		return (enchantmentLevel - 1) * 7 + 1;
	}

	@Override
	public int getMaxEnchantability(int enchantmentLevel)
	{
		return enchantmentLevel * 10;
	}

	@Override
	public int getCastTime()
	{
		return 1;
	}

	@Override
	public String[] getRawDescription()
	{
		return DESCRIPTION;
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
