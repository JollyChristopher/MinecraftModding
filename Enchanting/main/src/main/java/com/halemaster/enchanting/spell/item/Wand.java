package com.halemaster.enchanting.spell.item;

import java.util.Random;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.world.World;

import com.halemaster.enchanting.EnchantingMod;

public class Wand extends SpellItem
{
	public static final String NAME = "wand";
	public static final int COST = 3;
	private static Random random = new Random();
	
	private int enchantability;
	
	public Wand(String type, int durability, int enchantability)
	{
		setMaxStackSize(1);
		setCreativeTab(EnchantingMod.tabSpell);
		setUnlocalizedName(NAME + "." + type);
		setMaxDamage(durability);
		this.enchantability = enchantability;
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack stack, World worldIn, EntityPlayer playerIn)
    {
        if (!stack.isItemEnchanted())
        {
        	if (playerIn.capabilities.isCreativeMode)
        	{
        		EnchantmentHelper.addRandomEnchantment(random, stack, enchantability);
        	}
        	else if (playerIn.experienceLevel >= COST)
        	{
        		playerIn.removeExperienceLevel(COST);
        		EnchantmentHelper.addRandomEnchantment(random, stack, enchantability);
        	}
			
			return stack;
        }

        return super.onItemRightClick(stack, worldIn, playerIn);
    }

	@Override
	public ItemStack onSpellSuccess(ItemStack stack, World worldIn, EntityPlayer playerIn)
	{
		if (!playerIn.capabilities.isCreativeMode)
        {
            stack.attemptDamageItem(1 + Math.max(playerIn.getTotalArmorValue() - 
            		getLevel(stack, EnchantingMod.enchantmentBattle) * 2, 0), random);
            if(stack.getItemDamage() >= getMaxDamage())
            {
            	--stack.stackSize;
            }
        }

        playerIn.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);
        
        if (stack.stackSize <= 0)
        {
            return null;
        }

        return stack;
	}

	@Override
	public int getItemEnchantability()
	{
		return enchantability;
	}
	
	@Override
	public int getMaxItemUseDuration(ItemStack stack)
    {
		return (int) Math.ceil(super.getMaxItemUseDuration(stack) * 3.0 / 4.0);
    }
}

