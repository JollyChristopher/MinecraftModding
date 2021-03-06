package com.halemaster.enchanting.spell;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.EnumHelper;

import com.halemaster.enchanting.spell.item.SpellItem;

public abstract class Spell extends Enchantment
{
	public static final EnumEnchantmentType SPELL_TYPE = EnumHelper.addEnchantmentType("spell");
	public static final int COMMON = 10;
	public static final int UNCOMMON = 7;
	public static final int RARE = 5;
	public static final int EPIC = 2;
	public static final EnumRarity LEGENDARY = EnumHelper.addRarity("legendary", EnumChatFormatting.GOLD, "Legendary");
	public static final int LEGENDARY_WEIGHT = 1;
	public static final Map<EnumRarity, Integer> rarities;
	static
	{
		rarities = new HashMap<EnumRarity, Integer>();
		rarities.put(EnumRarity.COMMON, COMMON);
		rarities.put(EnumRarity.UNCOMMON, UNCOMMON);
		rarities.put(EnumRarity.RARE, RARE);
		rarities.put(EnumRarity.EPIC, EPIC);
		rarities.put(LEGENDARY, LEGENDARY_WEIGHT);
	}
	
	private EnumRarity rarity;
	
	// 0-62 are off limits.
	private static final int LOWER_LIMIT = 63;
	private static int hash(String id)
	{
		int hash = 0;
		
		for(byte ch : id.getBytes())
		{
			hash += ch;
		}
		
		hash %= 256 - LOWER_LIMIT;
		hash += LOWER_LIMIT;
		
		return hash;
	}
	
	public Spell(String enchID, ResourceLocation enchName, EnumRarity rarity)
	{
		super(hash(enchID), enchName, rarities.get(rarity), SPELL_TYPE);
		Enchantment.addToBookList(this);
		this.rarity = rarity;
	}
	
	public abstract Item getRender();
	public abstract String getRenderName();
	public abstract String getModId();
	public abstract int getColor();
	public abstract boolean onUse(ItemStack stack, World worldIn, EntityPlayer playerIn, int level);
	@Override
	public abstract int getMinEnchantability(int enchantmentLevel);
	@Override
    public abstract int getMaxEnchantability(int enchantmentLevel);
	public abstract int getCastTime();
	public abstract String[] getRawDescription();
	
	public String[] getDescription()
	{
		String[] names = getRawDescription();
		for (int i = 0; i < names.length; i++)
		{
			names[i] = StatCollector.translateToLocal(names[i]);
		}
		return names;
	}
	
	public boolean canApplyTogether(Enchantment ench)
    {
        return !(ench instanceof Spell);
    }
	
	public boolean canApply(ItemStack stack)
    {
        return stack.getItem() instanceof SpellItem;
    }
	
	public boolean canApplyAtEnchantingTable(ItemStack stack)
    {
        return canApply(stack);
    }
	
	public EnumRarity getRarity()
	{
		return rarity;
	}
}
