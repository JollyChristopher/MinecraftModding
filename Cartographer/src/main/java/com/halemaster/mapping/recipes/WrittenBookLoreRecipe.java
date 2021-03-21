/**
 * Created by Halemaster on 1/12/2017.
 */

package com.halemaster.mapping.recipes;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemWrittenBook;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

public class WrittenBookLoreRecipe implements IRecipe
{
    /**
     * Used to check if a recipe matches current crafting inventory
     */
    public boolean matches(InventoryCrafting inv, World worldIn)
    {
        ItemStack itemStack = ItemStack.EMPTY;
        ItemStack writtenBook = ItemStack.EMPTY;

        for (int i = 0; i < inv.getSizeInventory(); ++i)
        {
            ItemStack itemstack1 = inv.getStackInSlot(i);

            if (!itemstack1.isEmpty())
            {
                if (itemstack1.getItem() instanceof ItemWrittenBook)
                {
                    if(!writtenBook.isEmpty())
                    {
                        return false;
                    }
                    writtenBook = itemstack1;
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

        return !itemStack.isEmpty() && !writtenBook.isEmpty();
    }

    /**
     * Returns an Item that is the result of this recipe
     */
    public ItemStack getCraftingResult(InventoryCrafting inv)
    {
        ItemStack itemStack = ItemStack.EMPTY;
        ItemStack writtenBook = ItemStack.EMPTY;

        for (int k = 0; k < inv.getSizeInventory(); ++k)
        {
            ItemStack itemstack1 = inv.getStackInSlot(k);

            if (!itemstack1.isEmpty())
            {
                if (itemstack1.getItem() instanceof ItemWrittenBook)
                {
                    if (!writtenBook.isEmpty())
                    {
                        return ItemStack.EMPTY;
                    }

                    writtenBook = itemstack1.copy();
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

        if (writtenBook.isEmpty() || itemStack.isEmpty())
        {
            return ItemStack.EMPTY;
        }
        else
        {
            NBTTagCompound tags = writtenBook.getTagCompound();
            if(null == tags)
            {
                tags = new NBTTagCompound();
                writtenBook.setTagCompound(tags);
            }
            NBTTagList pages = tags.getTagList("pages", 8);

            NBTTagList lore = itemStack.getOrCreateSubCompound("display").getTagList("Lore", 8);
            for(int i = 0; i < pages.tagCount(); i++)
            {
                String pageString = pages.getStringTagAt(i);
                pageString = pageString.substring(9,pageString.length()-2);
                String[] loreStrings = pageString.split("\\\\n");
                for (String loreString : loreStrings)
                {
                    lore.appendTag(new NBTTagString(loreString));
                }
            }
            itemStack.getOrCreateSubCompound("display").setTag("Lore", lore);

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