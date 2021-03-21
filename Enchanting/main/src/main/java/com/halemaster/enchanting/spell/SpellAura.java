package com.halemaster.enchanting.spell;

import java.util.List;
import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public abstract class SpellAura extends Spell
{
	private static Random random = new Random();
	
	public SpellAura(String enchID, ResourceLocation enchName, EnumRarity rarity)
	{
		super(enchID, enchName, rarity);
	}
	
	public abstract double getRadius(int level);
	public abstract boolean auraHit(ItemStack stack, World worldIn, EntityPlayer playerIn, int level, EntityLivingBase entity);

	@Override
	public boolean onUse(ItemStack stack, World worldIn, EntityPlayer playerIn, int level)
	{
		boolean used = false;
		
		if (!worldIn.isRemote)
		{
			List<EntityLivingBase> radius = worldIn.getEntitiesWithinAABB(EntityLivingBase.class, 
					new AxisAlignedBB(playerIn.posX - getRadius(level), playerIn.posY - getRadius(level), 
							playerIn.posZ - getRadius(level), playerIn.posX + getRadius(level), playerIn.posY + 
							getRadius(level), playerIn.posZ + getRadius(level)));
			for (EntityLivingBase entity : radius)
			{
				used |= auraHit(stack, worldIn, playerIn, level, entity);
			}
		}
		else
		{
			used = true;
		}
		
		return used;
	}
	
	public void createAuraParticles(World world, EnumParticleTypes particle, Entity entity, int level, int color, 
			int lower, int upper)
	{
		createAuraParticles(world, particle, entity, level, color, getRadius(level), lower, upper);
	}
	
	public static void createAuraParticles(World world, EnumParticleTypes particle, Entity entity, int level, int color,
			double radius, int lower, int upper)
	{
        lower *= Math.pow(radius, 1.5);
        upper *= Math.pow(radius, 2);
        int amount = getRange(lower, upper);
        
        for (int i = 0; i < amount; i++)
        {
        	sprinkle(world, particle, entity, color, radius);
        }
	}
	
	private static int getRange(int lower, int upper)
	{
		return random.nextInt(upper - lower + 1) + lower;
	}
	
	private static void sprinkle(World world, EnumParticleTypes particle, Entity entity, int color, double radius)
	{
		double R = (double)(color >> 16 & 255) / 255.0D;
        double G = (double)(color >> 8 & 255) / 255.0D;
        double B = (double)(color >> 0 & 255) / 255.0D; 
        
        double x = findPos(entity.posX, radius);
        double z = findPos(entity.posZ, radius);
        
        world.spawnParticle(particle, x, entity.posY + 0.5D, z, R, G, B, new int[0]);
	}
	
	private static double findPos(double value, double radius)
	{
		double lower = value - radius;
		double upper = value + radius;
		
		return random.nextDouble() * (upper - lower) + lower;
	}
}
