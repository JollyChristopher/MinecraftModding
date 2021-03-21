/**
 * Created by Halemaster on 1/10/2017.
 */

package com.halemaster.mapping.recipes;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapData;

import static com.halemaster.mapping.recipes.MapMarkerRecipe.LORE_PREFIX;
import static com.halemaster.mapping.recipes.MapMarkerRecipe.findPlayer;

public class MapRemoveMarkerRecipe implements IRecipe
{
    /**
     * Used to check if a recipe matches current crafting inventory
     */
    public boolean matches(InventoryCrafting inv, World worldIn)
    {
        ItemStack map = ItemStack.EMPTY;
        ItemStack waterBucket = ItemStack.EMPTY;

        for (int i = 0; i < inv.getSizeInventory(); ++i)
        {
            ItemStack itemstack1 = inv.getStackInSlot(i);

            if (!itemstack1.isEmpty())
            {
                if (itemstack1.getItem() instanceof ItemMap)
                {
                    if(!map.isEmpty())
                    {
                        return false;
                    }
                    map = itemstack1;
                }
                else
                {
                    if (itemstack1.getItem() != Items.WATER_BUCKET)
                    {
                        return false;
                    }
                    if(!waterBucket.isEmpty())
                    {
                        return false;
                    }
                    waterBucket = itemstack1;
                }
            }
        }

        return !map.isEmpty() && !waterBucket.isEmpty();
    }

    /**
     * Returns an Item that is the result of this recipe
     */
    public ItemStack getCraftingResult(InventoryCrafting inv)
    {
        ItemStack mapStack = ItemStack.EMPTY;
        ItemStack waterBucketStack = ItemStack.EMPTY;

        for (int k = 0; k < inv.getSizeInventory(); ++k)
        {
            ItemStack itemstack1 = inv.getStackInSlot(k);

            if (!itemstack1.isEmpty())
            {
                if (itemstack1.getItem() instanceof ItemMap)
                {
                    if (!mapStack.isEmpty())
                    {
                        return ItemStack.EMPTY;
                    }

                    mapStack = itemstack1.copy();
                    mapStack.setCount(1);
                }
                else
                {
                    if (itemstack1.getItem() != Items.WATER_BUCKET)
                    {
                        return ItemStack.EMPTY;
                    }

                    if(!waterBucketStack.isEmpty())
                    {
                        return ItemStack.EMPTY;
                    }
                    waterBucketStack = itemstack1.copy();
                }
            }
        }

        if (mapStack.isEmpty() || waterBucketStack.isEmpty())
        {
            return ItemStack.EMPTY;
        }
        else
        {
            NBTTagCompound tags = mapStack.getTagCompound();
            if(null == tags)
            {
                tags = new NBTTagCompound();
                mapStack.setTagCompound(tags);
            }
            tags.setTag("Decorations", new NBTTagList());
            NBTTagList lore = mapStack.getOrCreateSubCompound("display").getTagList("Lore", 8);
            for(int i = 0; i < lore.tagCount(); i++)
            {
                String loreString = lore.getStringTagAt(i);
                if(loreString.startsWith(LORE_PREFIX))
                {
                    lore.removeTag(i);
                    i--;
                }
            }


            EntityPlayer player = findPlayer(inv);
            if(null != player)
            {
                MapData mapData = ((ItemMap) mapStack.getItem()).getMapData(mapStack, player.getEntityWorld());
                if(null != mapData) mapData.mapDecorations.clear();
            }

            return mapStack;
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
            if(itemstack.getItem() == Items.WATER_BUCKET)
            {
                nonnulllist.set(i, new ItemStack(Items.BUCKET, itemstack.getCount()));
            }
        }

        return nonnulllist;
    }
}
