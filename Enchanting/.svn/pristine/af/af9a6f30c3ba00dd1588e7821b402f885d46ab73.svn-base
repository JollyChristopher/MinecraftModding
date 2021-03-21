package com.halemaster.enchanting;

import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.EnumHelper;

public class ArmorCloth extends ItemArmor
{
	public static final String NAME = "silk";
	public static final String TEXTURE = "silkTexture";
	public static ArmorMaterial SILK = EnumHelper.addArmorMaterial(NAME, TEXTURE, 5, new int[] { 0, 0, 0, 0 }, 20);

	public ArmorCloth(int armorType)
	{
		super(SILK, 0, armorType);
		setUnlocalizedName(NAME + getNameFromPart(armorType));
		setCreativeTab(EnchantingMod.tabSpell);
		setMaxStackSize(1);
	}

	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type)
	{
		return EnchantingMod.MODID + ":textures/armor/" + TEXTURE + "_" + (this.armorType == 2 ? "2" : "1")
				+ (null != type ? "_" + type : "") + ".png";
	}

	public static String getNameFromPart(int armorType)
	{
		switch (armorType)
		{
		case 0:
			return "helmet";
		case 1:
			return "chest";
		case 2:
			return "legs";
		case 3:
			return "boots";
		}
		return "";
	}

	@Override
	public boolean hasColor(ItemStack stack)
	{
		return (!stack.hasTagCompound() ? false : (!stack.getTagCompound().hasKey("display", 10) ? false : stack
				.getTagCompound().getCompoundTag("display").hasKey("color", 3)));
	}

	@Override
	public int getColor(ItemStack stack)
	{
		NBTTagCompound nbttagcompound = stack.getTagCompound();

		if (nbttagcompound != null)
		{
			NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("display");

			if (nbttagcompound1 != null && nbttagcompound1.hasKey("color", 3))
			{
				return nbttagcompound1.getInteger("color");
			}
		}

		return 0xffffff;
	}

	@Override
	public void removeColor(ItemStack stack)
	{
		NBTTagCompound nbttagcompound = stack.getTagCompound();

		if (nbttagcompound != null)
		{
			NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("display");

			if (nbttagcompound1.hasKey("color"))
			{
				nbttagcompound1.removeTag("color");
			}
		}
	}

	@Override
	public void setColor(ItemStack stack, int color)
	{
		NBTTagCompound nbttagcompound = stack.getTagCompound();

		if (nbttagcompound == null)
		{
			nbttagcompound = new NBTTagCompound();
			stack.setTagCompound(nbttagcompound);
		}

		NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("display");

		if (!nbttagcompound.hasKey("display", 10))
		{
			nbttagcompound.setTag("display", nbttagcompound1);
		}

		nbttagcompound1.setInteger("color", color);
	}

	@Override
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair)
	{
		return repair.getItem() == Item.getItemFromBlock(Blocks.wool);
	}
}
