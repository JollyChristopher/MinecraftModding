package com.halemaster.enchanting.spell.item;

import com.halemaster.enchanting.EnchantingMod;
import com.halemaster.enchanting.spell.Spell;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.world.World;

public class Scroll extends SpellItem
{
	public static final String NAME = "scroll";
	
	public Scroll()
	{
		setMaxStackSize(64);
		setCreativeTab(EnchantingMod.tabSpell);
		setUnlocalizedName(NAME);
		setMaxDamage(1);
		setNoRepair();
	}

	@Override
	public ItemStack onSpellSuccess(ItemStack stack, World worldIn, EntityPlayer playerIn)
	{
		if (!playerIn.capabilities.isCreativeMode)
        {
            --stack.stackSize;
        }

        playerIn.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);
        
        if (stack.stackSize <= 0)
        {
            return null;
        }

        return stack;
	}
	
	@Override
	public int getMaxItemUseDuration(ItemStack stack)
    {		
        return 1;
    }

	@Override
	public int getItemEnchantability()
	{
		return 10;
	}
	
	@Override
	public boolean isBookEnchantable(ItemStack stack, ItemStack book)
	{
		return false;
	}
}
