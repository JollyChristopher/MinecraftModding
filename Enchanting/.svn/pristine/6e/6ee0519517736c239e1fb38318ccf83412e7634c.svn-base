package com.halemaster.enchanting.basic.spell;

import java.util.Random;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

import com.halemaster.enchanting.EnchantingMod;
import com.halemaster.enchanting.basic.BasicSpellbookMod;
import com.halemaster.enchanting.spell.EntitySpellProjectile;
import com.halemaster.enchanting.spell.SpellProjectile;

public class SpellFireBall extends SpellProjectile
{
	public static final String NAME = "fireball";
	public static final int enchID = 103;
	public static final int DURATION = 5;
	public static final String[] DESCRIPTION = new String[] { "enchantment.fireball.desc.1"};
	private static Random random = new Random();
	private static Item fireBall_spell = null;

	public SpellFireBall()
	{
		super(enchID, new ResourceLocation(BasicSpellbookMod.MODID, NAME), EnumRarity.COMMON);
		this.setName(NAME);
		if(fireBall_spell == null)
		{
			fireBall_spell = new Item().setUnlocalizedName(NAME).setMaxStackSize(1);
			GameRegistry.registerItem(fireBall_spell, NAME);
		}
		
		setItem(fireBall_spell);
	}

	@Override
	public int getColor()
	{
		return 0xFFA500;
	}

	@Override
	public int getMaxLevel()
	{
		return 4;
	}

	@Override
	public int getMinEnchantability(int enchantmentLevel)
	{
		return (enchantmentLevel - 1) * 7 + 1;
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
	public void onImpact(World worldIn, EntitySpellProjectile spell, EntityLivingBase thrower, 
			MovingObjectPosition movingObject, int level)
	{
		if (!worldIn.isRemote)
		{
			if(movingObject.entityHit instanceof EntityLivingBase)
			{
				EntityLivingBase hit = (EntityLivingBase) movingObject.entityHit;
				if (thrower.getTeam() == null || !(thrower.getTeam().isSameTeam(hit.getTeam()) &&
						!thrower.getTeam().getAllowFriendlyFire()))
				{
					hit.attackEntityFrom(new EntityDamageSource(NAME, spell).setFireDamage(), level * 2);
					hit.setFire(DURATION);
				}
			}
		}
	}

	@Override
	public void fire(World worldIn, EntityPlayer playerIn, int level)
	{
		worldIn.playSoundAtEntity(playerIn, "random.bow", 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));
	}

	@Override
	public void onUpdate(World world, EntitySpellProjectile spell, int level)
	{
		spell.setFire(1);
		world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, spell.posX, spell.posY + 0.5D, spell.posZ, 0.0D, 0.0D,
				0.0D, new int[0]);
	}

	@Override
	public Item getRender() {
		return fireBall_spell;
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
