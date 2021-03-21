package com.halemaster.enchanting.basic.spell;

import java.util.Random;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import com.halemaster.enchanting.EnchantingMod;
import com.halemaster.enchanting.basic.BasicSpellbookMod;
import com.halemaster.enchanting.spell.SpellAura;

public class SpellHealing extends SpellAura
{
	public static final String NAME = "healing";
	public static final int enchID = 107;
	public static final String[] DESCRIPTION = new String[] { "enchantment.healing.desc.1" };
	private static Random random = new Random();

	public SpellHealing()
	{
		super(enchID, new ResourceLocation(BasicSpellbookMod.MODID, NAME), EnumRarity.COMMON);
		this.setName(NAME);
	}

	@Override
	public int getColor()
	{
		return 0xffffff;
	}

	@Override
	public boolean onUse(ItemStack stack, World worldIn, EntityPlayer playerIn, int level)
	{
		boolean used = super.onUse(stack, worldIn, playerIn, level);
		
		if (used)
		{
			worldIn.playSoundAtEntity(playerIn, "mob.enderdragon.wings", 0.5F,
					0.4F / (random.nextFloat() * 0.4F + 0.8F));
			this.createAuraParticles(worldIn, EnumParticleTypes.HEART, playerIn, level, 0, 1, 1);
		}

		return used;
	}

	@Override
	public int getMaxLevel()
	{
		return 4;
	}

	@Override
	public int getMinEnchantability(int enchantmentLevel)
	{
		return (enchantmentLevel - 1) * 8 + 1;
	}

	@Override
	public int getMaxEnchantability(int enchantmentLevel)
	{
		return enchantmentLevel * 9;
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
		if (canHeal(entity))
		{
			if (playerIn.equals(entity))
			{
				entity.heal(level*2);
				return true;
			}
			else if (playerIn.getTeam() != null && playerIn.getTeam().isSameTeam(entity.getTeam()))
			{
				entity.heal(level*4);
				return true;
			}
		}
		return false;
	}
	
	public boolean canHeal (EntityLivingBase entity)
	{
		IAttributeInstance iattributeinstance = entity.getEntityAttribute(SharedMonsterAttributes.maxHealth);
        int health = MathHelper.ceiling_float_int(entity.getHealth());
		float maxHealth = (float)iattributeinstance.getAttributeValue();
		
		return maxHealth > health;
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
