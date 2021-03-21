/**
 * Created by Halemaster on 1/10/2017.
 */

package com.halemaster.mapping.recipes;

import com.google.common.collect.Lists;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

import java.util.List;

public class MapCombineMarkerRecipe implements IRecipe
{
    /**
     * Used to check if a recipe matches current crafting inventory
     */
    public boolean matches(InventoryCrafting inv, World worldIn)
    {
        List<ItemStack> list = Lists.newArrayList();

        for (int i = 0; i < inv.getSizeInventory(); ++i)
        {
            ItemStack itemstack1 = inv.getStackInSlot(i);

            if (!itemstack1.isEmpty())
            {
                if (itemstack1.getItem() instanceof ItemMap)
                {
                    list.add(itemstack1);
                }
                else
                {
                    return false;
                }
            }
        }

        if(list.size() < 2)
        {
            return false;
        }

        for(int i = 1; i < list.size(); i++)
        {
            if(list.get(i).getMetadata() != list.get(i - 1).getMetadata())
            {
                return false;
            }
        }

        return true;
    }

    /**
     * Returns an Item that is the result of this recipe
     */
    public ItemStack getCraftingResult(InventoryCrafting inv)
    {
        List<ItemStack> list = Lists.newArrayList();

        for (int k = 0; k < inv.getSizeInventory(); ++k)
        {
            ItemStack itemstack1 = inv.getStackInSlot(k);

            if (!itemstack1.isEmpty())
            {
                if (itemstack1.getItem() instanceof ItemMap)
                {
                    list.add(itemstack1.copy());
                }
                else
                {
                    return ItemStack.EMPTY;
                }
            }
        }

        if (list.size() < 2)
        {
            return ItemStack.EMPTY;
        }
        else
        {
            ItemStack itemStack = list.get(0);
            itemStack.setCount(1);
            NBTTagCompound tags = itemStack.getTagCompound();
            if(null == tags)
            {
                tags = new NBTTagCompound();
                itemStack.setTagCompound(tags);
            }
            NBTTagList decorations = tags.getTagList("Decorations", 10);
            NBTTagList lore = itemStack.getOrCreateSubCompound("display").getTagList("Lore", 8);

            for(int i = 1; i < list.size(); i++)
            {
                if(list.get(i).getMetadata() != list.get(i - 1).getMetadata())
                {
                    return ItemStack.EMPTY;
                }

                NBTTagCompound otherTags = list.get(i).getTagCompound();
                if(null == otherTags)
                {
                    otherTags = new NBTTagCompound();
                    itemStack.setTagCompound(otherTags);
                }
                NBTTagList otherDecorations = otherTags.getTagList("Decorations", 10);
                NBTTagList otherLore = list.get(i).getOrCreateSubCompound("display").getTagList("Lore", 8);
                for(int j = 0; j < otherDecorations.tagCount(); j++)
                {
                    decorations.appendTag(otherDecorations.get(j));
                }
                for(int j = 0; j < otherLore.tagCount(); j++)
                {
                    lore.appendTag(otherLore.get(j));
                }
                itemStack.setCount(itemStack.getCount() + 1);
            }

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
            ItemStack itemstack = inv.getStackInSlot(i);
            nonnulllist.set(i, net.minecraftforge.common.ForgeHooks.getContainerItem(itemstack));
        }

        return nonnulllist;
    }
}