package com.halemaster.enchanting.basic.spell;

import java.util.Random;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
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

public class SpellPoisonBolt extends SpellProjectile
{
	public static final String NAME = "poisonbolt";
	public static final int enchID = 115;
	public static final int DURATION = 20 * 10;
	public static final String[] DESCRIPTION = new String[] { "enchantment.poisonbolt.desc.1"};
	private static Random random = new Random();
	private static Item poisonBolt_spell = null;

	public SpellPoisonBolt()
	{
		super(enchID, new ResourceLocation(BasicSpellbookMod.MODID, NAME), EnumRarity.UNCOMMON);
		this.setName(NAME);
		if(poisonBolt_spell == null)
		{
			poisonBolt_spell = new Item().setUnlocalizedName(NAME).setMaxStackSize(1);
			GameRegistry.registerItem(poisonBolt_spell, NAME);
		}
		
		setItem(poisonBolt_spell);
	}

	@Override
	public int getColor()
	{
		return Potion.poison.getLiquidColor();
	}

	@Override
	public int getMaxLevel()
	{
		return 3;
	}

	@Override
	public int getMinEnchantability(int enchantmentLevel)
	{
		return (enchantmentLevel) * 7;
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
					hit.addPotionEffect(new PotionEffect(Potion.poison.id, DURATION, level - 1));
					hit.attackEntityFrom(new EntityDamageSource(NAME, spell).setMagicDamage(), level);
				}
			}
		}
	}

	@Override
	public void fire(World worldIn, EntityPlayer playerIn, int level)
	{
		worldIn.playSoundAtEntity(playerIn, "mob.endermen.portal", 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));
	}

	@Override
	public void onUpdate(World world, EntitySpellProjectile spell, int level)
	{
		int color = getColor();
		double R = (double)(color >> 16 & 255) / 255.0D;
        double G = (double)(color >> 8 & 255) / 255.0D;
        double B = (double)(color >> 0 & 255) / 255.0D; 
		world.spawnParticle(EnumParticleTypes.SPELL_MOB, spell.posX, spell.posY + 0.5D, spell.posZ, R, G, B, new int[0]);
	}

	@Override
	public Item getRender() {
		return poisonBolt_spell;
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
