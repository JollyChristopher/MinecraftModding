/**
 * Created by Halemaster on 1/10/2017.
 */

package com.halemaster.mapping;

import com.halemaster.mapping.event.AddMapLootEventHandler;
import com.halemaster.mapping.event.ClickMapEventHandler;
import com.halemaster.mapping.loot.TreasureMap;
import com.halemaster.mapping.recipes.*;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.VillagerRegistry;

@Mod(modid = CartographerMod.MODID, version = CartographerMod.VERSION)
public class CartographerMod
{
    static final String MODID = "cartographer";
    static final String VERSION = "1.0";

    @Mod.EventHandler
    public void preinit(FMLPreInitializationEvent event)
    {
        addRecipes();
        addTrades();
        addEventHandlers();
    }

    private void addRecipes()
    {
        CraftingManager.getInstance().addRecipe(new MapColorRecipe());
        CraftingManager.getInstance().addRecipe(new MapMarkerRecipe());
        CraftingManager.getInstance().addRecipe(new MapRemoveMarkerRecipe());
        CraftingManager.getInstance().addRecipe(new MapCombineMarkerRecipe());
        CraftingManager.getInstance().addRecipe(new WrittenBookLoreRecipe());
        CraftingManager.getInstance().addRecipe(new DragonBreathRemoveLoreRecipe());
    }

    private void addEventHandlers()
    {
        MinecraftForge.EVENT_BUS.register(new ClickMapEventHandler());
        MinecraftForge.EVENT_BUS.register(new AddMapLootEventHandler());
    }

    private void addTrades()
    {
        VillagerRegistry.VillagerProfession librarian = VillagerRegistry.instance().getRegistry()
                .getValue(new ResourceLocation("minecraft","librarian"));
        if(librarian != null)
        {
            VillagerRegistry.VillagerCareer cartographer = librarian.getCareer(1);
            TreasureMap.TreasureMapSale strongholdMap = new TreasureMap.TreasureMapSale(new EntityVillager.PriceInfo(
                    40, 60), new ItemStack(Items.ENDER_EYE, 5), TreasureMap.STRONGHOLD);
            TreasureMap.TreasureMapSale villageMap = new TreasureMap.TreasureMapSale(new EntityVillager.PriceInfo(
                    6, 12), TreasureMap.VILLAGE);
            TreasureMap.TreasureMapSale templeMap = new TreasureMap.TreasureMapSale(new EntityVillager.PriceInfo(
                    12, 18), TreasureMap.TEMPLE);
            TreasureMap.TreasureMapSale jungleMap = new TreasureMap.TreasureMapSale(new EntityVillager.PriceInfo(
                    4, 10), TreasureMap.JUNGLE);
            TreasureMap.TreasureMapSale mesaMap = new TreasureMap.TreasureMapSale(new EntityVillager.PriceInfo(
                    9, 14), TreasureMap.MESA);
            TreasureMap.TreasureMapSale mushroomIslandMap = new TreasureMap.TreasureMapSale(new EntityVillager.PriceInfo(
                    12, 18), TreasureMap.MUSHROOM_ISLAND);
            TreasureMap.TreasureMapSale swampMap = new TreasureMap.TreasureMapSale(new EntityVillager.PriceInfo(
                    4, 10), TreasureMap.SWAMP);
            cartographer.addTrade(3, villageMap, jungleMap, swampMap);
            cartographer.addTrade(4, mesaMap, templeMap);
            cartographer.addTrade(5, mushroomIslandMap, strongholdMap);
        }
    }
}