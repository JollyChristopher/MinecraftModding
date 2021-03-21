package com.halemaster.enchanting.spell.item;

import java.util.Random;

import com.halemaster.enchanting.EnchantingMod;
import com.halemaster.enchanting.spell.Spell;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.world.World;

public class Staff extends SpellItem
{
	public static final String NAME = "staff";
	private static Random random = new Random();
	
	private int enchantability;
	
	public Staff(String type, int durability, int enchantability)
	{
		setMaxStackSize(1);
		setCreativeTab(EnchantingMod.tabSpell);
		setUnlocalizedName(NAME + "." + type);
		setMaxDamage(durability);
		this.enchantability = enchantability;
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
}

