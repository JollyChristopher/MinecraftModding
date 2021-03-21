/**
 * Created by Halemaster on 1/13/2017.
 */

package com.halemaster.mapping.loot;

import net.minecraft.entity.IMerchant;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Biomes;
import net.minecraft.init.Items;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.storage.MapData;
import net.minecraft.world.storage.MapDecoration;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public enum TreasureMap
{
    STRONGHOLD("Stronghold", 0xFF00FF, MapDecoration.Type.MONUMENT),
    VILLAGE("Village", 0x00FF00, MapDecoration.Type.MANSION),
    TEMPLE("Temple", 0xFF0000, MapDecoration.Type.TARGET_X),
    JUNGLE("Jungle", 0x006400, MapDecoration.Type.TARGET_X, Biomes.JUNGLE, Biomes.JUNGLE_EDGE,
            Biomes.JUNGLE_HILLS, Biomes.MUTATED_JUNGLE, Biomes.MUTATED_JUNGLE_EDGE),
    MESA("Mesa", 0xFFA500, MapDecoration.Type.TARGET_X, Biomes.MESA, Biomes.MESA_CLEAR_ROCK,
            Biomes.MESA_ROCK, Biomes.MUTATED_MESA, Biomes.MUTATED_MESA_CLEAR_ROCK, Biomes.MUTATED_MESA_ROCK),
    MUSHROOM_ISLAND("MushroomIsland", 0x2F4F4F, MapDecoration.Type.TARGET_X, Biomes.MUSHROOM_ISLAND,
            Biomes.MUSHROOM_ISLAND_SHORE),
    SWAMP("Swampland", 0x808000, MapDecoration.Type.TARGET_X, Biomes.SWAMPLAND, Biomes.MUTATED_SWAMPLAND),
    OCEAN_MONUMENT("Monument", 0, MapDecoration.Type.MONUMENT),
    WOODLAND_MANSION("Mansion", 0, MapDecoration.Type.MANSION);

    private String name;
    private List<Biome> biomes;
    private int color;
    private MapDecoration.Type destinationType;

    TreasureMap(String name, int color, MapDecoration.Type destinationType, Biome...biomes)
    {
        this.name = name;
        this.color = color;
        this.destinationType = destinationType;
        this.biomes = Arrays.asList(biomes);
    }

    public static TreasureMap getByName(String name)
    {
        for(TreasureMap type : values())
        {
            if(type.name.equalsIgnoreCase(name))
            {
                return type;
            }
        }

        return null;
    }

    public String getName()
    {
        return this.name;
    }

    public ItemStack generateMap(World world, BlockPos start, Random random)
    {
        if(!this.biomes.isEmpty())
        {
            return getTreasureMap(world, this.biomes, this.name, this.destinationType,
                    this.color, start, random);
        }
        else
        {
            return getTreasureMap(world, this.name, this.destinationType, this.color, start);
        }
    }

    public static ItemStack getTreasureMap(World world, BlockPos center, String destinationName,
                                           MapDecoration.Type destinationType, int color)
    {
        if (center != null)
        {
            ItemStack itemstack = ItemMap.setupNewMap(world, (double)center.getX(), (double)center.getZ(),
                    (byte)2, true, true);
            ItemMap.renderBiomePreviewMap(world, itemstack);
            MapData.addTargetDecoration(itemstack, center, "+", destinationType);
            itemstack.setTranslatableName("filled_map." + destinationName.toLowerCase(Locale.ROOT));
            NBTTagCompound nbttagcompound1 = itemstack.getOrCreateSubCompound("display");
            if(color > 0)
            {
                nbttagcompound1.setInteger("MapColor", color);
            }
            return itemstack;
        }
        return ItemStack.EMPTY;
    }

    public static ItemStack getTreasureMap(World world, List<Biome> biomes, String destinationName,
                                           MapDecoration.Type destinationType, int color, BlockPos start,
                                           Random random)
    {
        BlockPos blockPos = world.getBiomeProvider().findBiomePosition(start.getX(), start.getZ(), 3000, biomes,
                random);

        return getTreasureMap(world, blockPos, destinationName, destinationType, color);
    }

    public static ItemStack getTreasureMap(World world, String destination, MapDecoration.Type destinationType,
                                           int color, BlockPos start)
    {
        BlockPos blockpos = world.findNearestStructure(destination, start, true);

        return getTreasureMap(world, blockpos, destination, destinationType, color);
    }

    public static class TreasureMapSale implements EntityVillager.ITradeList
    {
        EntityVillager.PriceInfo emeraldValue;
        ItemStack stack;
        TreasureMap treasureMap;

        public TreasureMapSale(EntityVillager.PriceInfo emeraldPrice, TreasureMap map)
        {
            this(emeraldPrice, new ItemStack(Items.COMPASS), map);
        }

        public TreasureMapSale(EntityVillager.PriceInfo emeraldPrice, ItemStack otherItem, TreasureMap map)
        {
            this.emeraldValue = emeraldPrice;
            this.treasureMap = map;
            this.stack = otherItem;
        }

        public void addMerchantRecipe(IMerchant merchant, MerchantRecipeList recipeList, Random random)
        {
            int i = this.emeraldValue.getPrice(random);
            BlockPos start = merchant.getPos().add(random.nextInt(2000) - 1000,
                    random.nextInt(2000) - 1000, random.nextInt(2000) - 1000);
            ItemStack map = this.treasureMap.generateMap(merchant.getWorld(), start, random);

            if (!map.isEmpty())
            {
                recipeList.add(new MerchantRecipe(new ItemStack(Items.EMERALD, i), this.stack, map));
            }
        }
    }
}
