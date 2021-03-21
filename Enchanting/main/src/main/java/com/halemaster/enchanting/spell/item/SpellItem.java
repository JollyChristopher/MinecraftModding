package com.halemaster.enchanting.spell.item;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.halemaster.enchanting.EnchantingMod;
import com.halemaster.enchanting.spell.Spell;

public abstract class SpellItem extends Item
{
	private static Random random = new Random();
	
	@Override
	@SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List tooltip, boolean advanced) 
	{
		Spell spell = getSpell(stack);
		if (spell != null)
		{
			tooltip.addAll(Arrays.asList(spell.getDescription()));
		}
	}
	
	@Override
	public EnumRarity getRarity(ItemStack stack)
	{
		if(stack.isItemEnchanted())
		{
			Spell spell = getSpell(stack);
			if (spell != null)
			{
				return spell.getRarity();
			}
		}
		return EnumRarity.COMMON;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack stack, int renderPass)
    {
        if(renderPass > 0)
    	{
        	return 16777215;	
    	}
        else
        {
        	Spell spell = getSpell(stack);
        	if (spell == null)
        	{
        		return 16777215;
        	}
        	
        	return spell.getColor();
        }
    }
	
	@Override
	public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn)
    {
        playerIn.setItemInUse(itemStackIn, this.getMaxItemUseDuration(itemStackIn));
        return itemStackIn;
    }
	
	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityPlayer playerIn)
    {
        if (stack.isItemEnchanted())
        {
        	Spell spell = getSpell(stack);
        	int level = getLevel(stack);
			if (null != spell && spell.onUse(stack, worldIn, playerIn, level))
			{
				return onSpellSuccess(stack, worldIn, playerIn);
			}
			else if (null == spell)
			{
				ItemStack copy = new ItemStack(stack.getItem(), stack.stackSize, stack.getItemDamage());
				EnchantmentHelper.addRandomEnchantment(random, copy, 5 + 7 * (getNonSpellLevel(stack) - 1));
				return copy;
			}
        }

        return stack;
    }
	
	@Override
	public int getMaxItemUseDuration(ItemStack stack)
    {
		Spell spell = getSpell(stack);
		
		if (null != spell)
		{
			int castTime = spell.getCastTime();
			int quick = getLevel(stack, EnchantingMod.enchantmentQuick);
			castTime -= (double)castTime * (1.0/10.0) * (double)quick;
			
			return Math.max(castTime, 1);
		}
		
        return 1;
    }

	@Override
    public EnumAction getItemUseAction(ItemStack stack)
    {
        return EnumAction.BOW;
    }
    
    protected Spell getSpell(ItemStack stack)
    {
    	this.getItemEnchantability();
    	Map enchantments = EnchantmentHelper.getEnchantments(stack);
    	for (Object enchantid : enchantments.keySet())
    	{
    		Enchantment enchant = Enchantment.getEnchantmentById((Integer) enchantid);
    		if (enchant instanceof Spell)
    		{
    			return (Spell) enchant;
    		}
    	}
    	
    	return null;
    }
    
    protected int getLevel(ItemStack stack)
    {
    	this.getItemEnchantability();
    	Map enchantments = EnchantmentHelper.getEnchantments(stack);
    	for (Object enchantid : enchantments.keySet())
    	{
    		Enchantment enchant = Enchantment.getEnchantmentById((Integer) enchantid);
    		if (enchant instanceof Spell)
    		{
    			return (Integer) enchantments.get(enchantid);
    		}
    	}
    	
    	return 0;
    }
    
    protected int getNonSpellLevel(ItemStack stack)
    {
    	this.getItemEnchantability();
    	Map enchantments = EnchantmentHelper.getEnchantments(stack);
    	int level = 0;
    	
    	for (Object enchantid : enchantments.keySet())
    	{
    		Enchantment enchant = Enchantment.getEnchantmentById((Integer) enchantid);
    		if (!(enchant instanceof Spell))
    		{
    			level += (Integer) enchantments.get(enchantid);
    		}
    	}
    	
    	return level;
    }
    
    protected int getLevel(ItemStack stack, Enchantment enchantment)
    {
    	this.getItemEnchantability();
    	Map enchantments = EnchantmentHelper.getEnchantments(stack);
    	for (Object enchantid : enchantments.keySet())
    	{
    		Enchantment enchant = Enchantment.getEnchantmentById((Integer) enchantid);
    		if (enchant.equals(enchantment))
    		{
    			return (Integer) enchantments.get(enchantid);
    		}
    	}
    	
    	return 0;
    }
    
    @Override
    public boolean isItemTool(ItemStack stack)
    {
    	return stack.getItem() instanceof SpellItem;
    }
	
    public String getItemStackDisplayName(ItemStack stack)
    {
    	String name = getUnlocalizedNameInefficiently(stack);
    	String spellName = "";
    	Spell spell = getSpell(stack);
    	
    	if (null != spell)
    	{
    		name += ".enchanted";
    		spellName = spell.getTranslatedName(getLevel(stack));
    	}
    	
    	name += ".name";
    	
        return ("" + StatCollector.translateToLocalFormatted(name, spellName)).trim();
    }
	
	public abstract ItemStack onSpellSuccess(ItemStack stack, World worldIn, EntityPlayer playerIn);	
	@Override
	public abstract int getItemEnchantability();
}
