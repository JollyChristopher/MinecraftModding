/**
 * Created by Halemaster on 1/10/2017.
 */

package com.halemaster.mapping.recipes;

import com.google.common.base.Throwables;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapDecoration;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.lang.reflect.Field;

public class MapMarkerRecipe implements IRecipe
{
    static final String LORE_PREFIX = "Icon: ";

    /**
     * Used to check if a recipe matches current crafting inventory
     */
    public boolean matches(InventoryCrafting inv, World worldIn)
    {
        ItemStack map = ItemStack.EMPTY;
        ItemStack wool = ItemStack.EMPTY;

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
                    if (!(itemstack1.getItem() instanceof ItemBlock &&
                            ((ItemBlock) itemstack1.getItem()).getBlock() == Blocks.WOOL))
                    {
                        return false;
                    }
                    if(!wool.isEmpty())
                    {
                        return false;
                    }
                    wool = itemstack1;
                }
            }
        }

        return !map.isEmpty() && !wool.isEmpty();
    }

    /**
     * Returns an Item that is the result of this recipe
     */
    public ItemStack getCraftingResult(InventoryCrafting inv)
    {
        ItemStack mapStack = ItemStack.EMPTY;
        ItemStack woolStack = ItemStack.EMPTY;
        int mapIndex = -1;
        int woolIndex = -1;

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
                    mapIndex = k;
                }
                else
                {
                    if (!(itemstack1.getItem() instanceof ItemBlock &&
                            ((ItemBlock) itemstack1.getItem()).getBlock() == Blocks.WOOL) ||
                            !woolStack.isEmpty())
                    {
                        return ItemStack.EMPTY;
                    }

                    woolStack = itemstack1.copy();
                    woolIndex = k;
                }
            }
        }

        if (mapStack.isEmpty() || woolStack.isEmpty())
        {
            return ItemStack.EMPTY;
        }
        else
        {
            int type = Math.min(woolStack.getMetadata(), MapDecoration.Type.values().length - 1);
            int woolX = woolIndex % (int) Math.sqrt(inv.getSizeInventory());
            int woolY = woolIndex / (int) Math.sqrt(inv.getSizeInventory());
            int mapX = mapIndex % (int) Math.sqrt(inv.getSizeInventory());
            int mapY = mapIndex / (int) Math.sqrt(inv.getSizeInventory());
            double rot = Math.toDegrees(Math.atan2(woolY - mapY, woolX - mapX)) - 90;
            EntityPlayer player = findPlayer(inv);
            int x = 0;
            int z = 0;
            if(player != null)
            {
                x = player.getPosition().getX();
                z = player.getPosition().getZ();
            }
            String id = getMapId(type, rot, x, z);
            NBTTagCompound tags = mapStack.getTagCompound();
            if(null == tags)
            {
                tags = new NBTTagCompound();
                mapStack.setTagCompound(tags);
            }
            NBTTagList decorations = tags.getTagList("Decorations", 10);
            NBTTagCompound marker = new NBTTagCompound();
            marker.setInteger("type", type);
            marker.setDouble("rot", rot);
            marker.setInteger("x", x);
            marker.setInteger("z", z);
            marker.setString("id", id);
            decorations.appendTag(marker);
            tags.setTag("Decorations", decorations);

            NBTTagList lore = mapStack.getOrCreateSubCompound("display").getTagList("Lore", 8);
            String loreString = LORE_PREFIX +
                    uppercaseFirstLetters(MapDecoration.Type.byIcon((byte) type).name().replace("_", " ")) +
                    "(" + x + ", " + z + ") rotated " + (((Double) rot).intValue() + 90);
            lore.appendTag(new NBTTagString(loreString));
            mapStack.getOrCreateSubCompound("display").setTag("Lore", lore);

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
        }

        return nonnulllist;
    }

    public static String getMapId(int type, double rot, int x, int z)
    {
        return "type:"+type+",rot:"+rot+"x:"+x+"z:"+z;
    }

    public static String uppercaseFirstLetters(String input)
    {
        String[] words = input.split(" ");
        String output = "";

        for(int i = 0; i < words.length; i++)
        {
            if(i != 0) output += " ";
            output += words[i].toUpperCase().charAt(0);
            if(words[i].length() > 1)
                output += words[i].toLowerCase().substring(1);
        }

        return output;
    }

    private static final Field eventHandlerField =
            ReflectionHelper.findField(InventoryCrafting.class, "eventHandler", "field_70465_c");
    private static final Field containerPlayerPlayerField =
            ReflectionHelper.findField(ContainerPlayer.class, "player", "field_82862_h");
    private static final Field slotCraftingPlayerField =
            ReflectionHelper.findField(SlotCrafting.class, "player", "field_75238_b");

    public static EntityPlayer findPlayer(InventoryCrafting inv)
    {
        try
        {
            Container container = (Container) eventHandlerField.get(inv);
            if (container instanceof ContainerPlayer)
            {
                return (EntityPlayer) containerPlayerPlayerField.get(container);
            }
            else
            {
                Slot firstSlot = container.getSlot(0);
                if(firstSlot instanceof SlotCrafting)
                {
                    return (EntityPlayer) slotCraftingPlayerField.get(firstSlot);
                }
            }
            return null;
        }
        catch (Exception e)
        {
            throw Throwables.propagate(e);
        }
    }
}
