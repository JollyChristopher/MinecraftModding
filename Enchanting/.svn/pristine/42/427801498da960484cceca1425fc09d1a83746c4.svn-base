package com.halemaster.enchanting.spell.item.enchants;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentDurability;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.EnumHelper;

import com.halemaster.enchanting.EnchantingMod;
import com.halemaster.enchanting.spell.Spell;
import com.halemaster.enchanting.spell.item.SpellItem;

public class EnchantmentQuick extends Enchantment
{	
	public static final String NAME = "quick";
	
	public EnchantmentQuick(int enchID)
	{
		super(enchID, new ResourceLocation("enchanting", NAME), 10, Spell.SPELL_TYPE);
		this.setName(NAME);
		Enchantment.addToBookList(this);
		EnchantingMod.boostSpellEnchants.add(this);
	}
	
	@Override
	public int getMaxLevel()
	{
		return 4;
	}
	
	@Override
	public int getMinEnchantability(int enchantmentLevel)
	{
		return (enchantmentLevel - 1) * 9 + 1;
	}
	
	@Override
    public int getMaxEnchantability(int enchantmentLevel)
	{
		return enchantmentLevel * 13;
	}
	
	public boolean canApplyTogether(Enchantment ench)
    {
		if(ench != null && EnchantingMod.boostSpellEnchants.contains(ench))
		{
			return false;
		}
        return true;
    }
	
	public boolean canApply(ItemStack stack)
    {
        return stack.getItem() instanceof SpellItem;
    }
	
	public boolean canApplyAtEnchantingTable(ItemStack stack)
    {
        return canApply(stack);
    }
}
