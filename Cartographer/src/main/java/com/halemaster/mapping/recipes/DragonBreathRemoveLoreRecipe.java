/**
 * Created by Halemaster on 1/12/2017.
 */

package com.halemaster.mapping.recipes;

import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

public class DragonBreathRemoveLoreRecipe implements IRecipe
{
    /**
     * Used to check if a recipe matches current crafting inventory
     */
    public boolean matches(InventoryCrafting inv, World worldIn)
    {
        ItemStack itemStack = ItemStack.EMPTY;
        ItemStack dragonBreath = ItemStack.EMPTY;

        for (int i = 0; i < inv.getSizeInventory(); ++i)
        {
            ItemStack itemstack1 = inv.getStackInSlot(i);

            if (!itemstack1.isEmpty())
            {
                if (itemstack1.getItem() == Items.DRAGON_BREATH)
                {
                    if(!dragonBreath.isEmpty())
                    {
                        return false;
                    }
                    dragonBreath = itemstack1;
                }
                else
                {
                    if(!itemStack.isEmpty())
                    {
                        return false;
                    }
                    itemStack = itemstack1;
                }
            }
        }

        return !itemStack.isEmpty() && !dragonBreath.isEmpty() && !itemStack.getOrCreateSubCompound("display")
                .getTag("Lore").hasNoTags();
    }

    /**
     * Returns an Item that is the result of this recipe
     */
    public ItemStack getCraftingResult(InventoryCrafting inv)
    {
        ItemStack itemStack = ItemStack.EMPTY;
        ItemStack dragonBreath = ItemStack.EMPTY;

        for (int k = 0; k < inv.getSizeInventory(); ++k)
        {
            ItemStack itemstack1 = inv.getStackInSlot(k);

            if (!itemstack1.isEmpty())
            {
                if (itemstack1.getItem() == Items.DRAGON_BREATH)
                {
                    if (!dragonBreath.isEmpty())
                    {
                        return ItemStack.EMPTY;
                    }

                    dragonBreath = itemstack1.copy();
                }
                else
                {
                    if(!itemStack.isEmpty())
                    {
                        return ItemStack.EMPTY;
                    }
                    itemStack = itemstack1.copy();
                }
            }
        }

        if (dragonBreath.isEmpty() || itemStack.isEmpty() || itemStack.getOrCreateSubCompound("display")
                .getTag("Lore").hasNoTags())
        {
            return ItemStack.EMPTY;
        }
        else
        {
            itemStack.getOrCreateSubCompound("display").setTag("Lore", new NBTTagList());

            return itemStack;
        }
    }

    /**
     * Returns the size of the recipe area
     */
    public int getRecipeSize()
    {
        return 10;
    }

    public ItemStack getRecipeOutput()
    {
        return ItemStack.EMPTY;
    }

    public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv)
    {
        NonNullList<ItemStack> nonnulllist = NonNullList.withSize(inv.getSizeInventory(), ItemStack.EMPTY);

        for (int i = 0; i < nonnulllist.size(); ++i)
        {
            ItemStack itemstack = inv.getStackInSlot(i).copy();
            if(itemstack.getItem() == Items.DRAGON_BREATH)
            {
                itemstack.setCount(1);
                nonnulllist.set(i, itemstack);
            }
        }

        return nonnulllist;
    }
}