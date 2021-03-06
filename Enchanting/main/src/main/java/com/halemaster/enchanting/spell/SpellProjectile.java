package com.halemaster.enchanting.spell;

import java.util.Random;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public abstract class SpellProjectile extends Spell
{
	protected static Random random = new Random();
	private Item item;
	
	public SpellProjectile(String enchID, ResourceLocation enchName, EnumRarity rarity)
	{
		this(enchID, enchName, rarity, null);
	}
	
	public SpellProjectile(String enchID, ResourceLocation enchName, EnumRarity rarity, Item item)
	{
		super(enchID, enchName, rarity);
		this.item = item;
	}
	
	public void setItem(Item item)
	{
		this.item = item;
	}
	
	public Item getItem()
	{
		return item;
	}

	@Override
	public boolean onUse(ItemStack stack, World worldIn, EntityPlayer playerIn, int level)
	{
		fire(worldIn, playerIn, level);
		
		if (!worldIn.isRemote)
        {
            worldIn.spawnEntityInWorld(new EntitySpellProjectile(worldIn, playerIn, this, level));
            return true;
        }
		return false;
	}
	
	public abstract void onImpact(World worldIn, EntitySpellProjectile spell, EntityLivingBase thrower, 
			MovingObjectPosition movingObject, int level);
	public abstract void fire(World worldIn, EntityPlayer playerIn, int level);
	public abstract void onUpdate(World world, EntitySpellProjectile spell, int level);
}
