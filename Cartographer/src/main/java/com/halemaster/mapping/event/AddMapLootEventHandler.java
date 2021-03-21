/**
 * Created by Halemaster on 1/13/2017.
 */

package com.halemaster.mapping.event;

import com.halemaster.mapping.loot.ExplorationMapLootFunction;
import com.halemaster.mapping.loot.TreasureMap;
import net.minecraft.init.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.*;
import net.minecraft.world.storage.loot.conditions.KilledByPlayer;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.conditions.RandomChance;
import net.minecraft.world.storage.loot.conditions.RandomChanceWithLooting;
import net.minecraft.world.storage.loot.functions.LootFunction;
import net.minecraft.world.storage.loot.functions.LootFunctionManager;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.*;

public class AddMapLootEventHandler
{
    private static final Map<ResourceLocation, List<TreasureMap>> lootLists;
    private static final Map<ResourceLocation, Float> entities;
    private static final Map<ResourceLocation, Float> chests;

    static
    {
        lootLists = new HashMap<ResourceLocation, List<TreasureMap>>();
        entities = new HashMap<ResourceLocation, Float>();
        chests = new HashMap<ResourceLocation, Float>();

        // undead
        List<TreasureMap> undeadMaps = Arrays.asList(TreasureMap.TEMPLE,
                TreasureMap.VILLAGE);
        entities.put(LootTableList.ENTITIES_SKELETON, 0.015f);
        lootLists.put(LootTableList.ENTITIES_SKELETON, undeadMaps);
        entities.put(LootTableList.ENTITIES_STRAY, 0.025f);
        lootLists.put(LootTableList.ENTITIES_STRAY, undeadMaps);
        entities.put(LootTableList.ENTITIES_ZOMBIE, 0.015f);
        lootLists.put(LootTableList.ENTITIES_ZOMBIE, undeadMaps);
        entities.put(LootTableList.ENTITIES_ZOMBIE_VILLAGER, 0.045f);
        lootLists.put(LootTableList.ENTITIES_ZOMBIE_VILLAGER, undeadMaps);
        entities.put(LootTableList.ENTITIES_HUSK, 0.025f);
        lootLists.put(LootTableList.ENTITIES_HUSK, undeadMaps);
        entities.put(LootTableList.ENTITIES_ENDERMAN, 0.025f);
        lootLists.put(LootTableList.ENTITIES_ENDERMAN, undeadMaps);

        // witch
        List<TreasureMap> witchMaps = Arrays.asList(TreasureMap.TEMPLE,
                TreasureMap.VILLAGE, TreasureMap.WOODLAND_MANSION,
                TreasureMap.SWAMP);
        entities.put(LootTableList.ENTITIES_WITCH, 0.035f);
        lootLists.put(LootTableList.ENTITIES_WITCH, witchMaps);

        // mansion
        List<TreasureMap> mansionMaps = Arrays.asList(TreasureMap.TEMPLE,
                TreasureMap.VILLAGE, TreasureMap.OCEAN_MONUMENT);
        entities.put(LootTableList.ENTITIES_EVOCATION_ILLAGER, 0.055f);
        lootLists.put(LootTableList.ENTITIES_EVOCATION_ILLAGER, mansionMaps);
        entities.put(LootTableList.ENTITIES_VINDICATION_ILLAGER, 0.105f);
        lootLists.put(LootTableList.ENTITIES_VINDICATION_ILLAGER, mansionMaps);

        // spawn chest
        List<TreasureMap> spawnChestMaps = Collections.singletonList
                (TreasureMap.VILLAGE);
        chests.put(LootTableList.CHESTS_SPAWN_BONUS_CHEST, 1f);
        lootLists.put(LootTableList.CHESTS_SPAWN_BONUS_CHEST, spawnChestMaps);

        // blacksmith chest
        List<TreasureMap> smithChestMaps = Arrays.asList(TreasureMap.VILLAGE,
                TreasureMap.TEMPLE);
        chests.put(LootTableList.CHESTS_VILLAGE_BLACKSMITH, 0.25f);
        lootLists.put(LootTableList.CHESTS_VILLAGE_BLACKSMITH, smithChestMaps);

        // other chests
        List<TreasureMap> chestMaps = Arrays.asList(TreasureMap.values());
        chests.put(LootTableList.GAMEPLAY_FISHING_TREASURE, 0.15f);
        lootLists.put(LootTableList.GAMEPLAY_FISHING_TREASURE, chestMaps);
        chests.put(LootTableList.CHESTS_SIMPLE_DUNGEON, 0.35f);
        lootLists.put(LootTableList.CHESTS_SIMPLE_DUNGEON, chestMaps);
        chests.put(LootTableList.CHESTS_ABANDONED_MINESHAFT, 0.35f);
        lootLists.put(LootTableList.CHESTS_ABANDONED_MINESHAFT, chestMaps);
        chests.put(LootTableList.CHESTS_STRONGHOLD_LIBRARY, 0.65f);
        lootLists.put(LootTableList.CHESTS_STRONGHOLD_LIBRARY, chestMaps);
        chests.put(LootTableList.CHESTS_STRONGHOLD_CROSSING, 0.35f);
        lootLists.put(LootTableList.CHESTS_STRONGHOLD_CROSSING, chestMaps);
        chests.put(LootTableList.CHESTS_STRONGHOLD_CORRIDOR, 0.35f);
        lootLists.put(LootTableList.CHESTS_STRONGHOLD_CORRIDOR, chestMaps);
        chests.put(LootTableList.CHESTS_DESERT_PYRAMID, 0.25f);
        lootLists.put(LootTableList.CHESTS_DESERT_PYRAMID, chestMaps);
        chests.put(LootTableList.CHESTS_JUNGLE_TEMPLE, 0.35f);
        lootLists.put(LootTableList.CHESTS_JUNGLE_TEMPLE, chestMaps);
        chests.put(LootTableList.CHESTS_IGLOO_CHEST, 0.25f);
        lootLists.put(LootTableList.CHESTS_IGLOO_CHEST, chestMaps);
        chests.put(LootTableList.CHESTS_WOODLAND_MANSION, 0.45f);
        lootLists.put(LootTableList.CHESTS_WOODLAND_MANSION, chestMaps);
    }

    public AddMapLootEventHandler()
    {
        LootFunctionManager.registerFunction(new ExplorationMapLootFunction.Serializer());
    }

    @SubscribeEvent
    public void lootLoad(LootTableLoadEvent event)
    {
        LootCondition[] lootConditions = null;
        if(entities.containsKey(event.getName()))
        {
            // mob loot conditions
            lootConditions = new LootCondition[2];
            lootConditions[0] = new KilledByPlayer(false);
            lootConditions[1] = new RandomChanceWithLooting(entities.get(event.getName()), 0.01F);
        }
        else if(chests.containsKey(event.getName()))
        {
            // chest loot conditions
            lootConditions = new LootCondition[1];
            lootConditions[0] = new RandomChance(chests.get(event.getName()));
        }

        if(null != lootConditions)
        {
            List<TreasureMap> lootList = lootLists.get(event.getName());
            if(null != lootList)
            {
                LootEntry[] entries = new LootEntry[lootList.size()];
                for(int i = 0; i < lootList.size(); i++)
                {
                    ExplorationMapLootFunction mapFunction = new ExplorationMapLootFunction(lootConditions,
                            lootList.get(i).getName());
                    entries[i] = new LootEntryItem(Items.FILLED_MAP, 1, 1,
                            new LootFunction[]{mapFunction}, lootConditions, lootList.get(i).getName());
                }

                if(event.getName().equals(LootTableList.GAMEPLAY_FISHING_TREASURE))
                {
                    for(LootEntry entry : entries)
                    {
                        event.getTable().getPool("main").addEntry(entry);
                    }
                }
                else
                {
                    event.getTable().addPool(new LootPool(entries, lootConditions, new RandomValueRange
                            (1f, 2f), new RandomValueRange(1F, 1F), "maps"));
                }
            }
        }
    }
}
