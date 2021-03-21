/**
 * Created by Halemaster on 1/13/2017.
 */

package com.halemaster.mapping.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;

import java.util.Random;

public class ExplorationMapLootFunction extends LootFunction
{
    private final String destinationType;

    public ExplorationMapLootFunction(LootCondition[] p_i46623_1_, String destinationType)
    {
        super(p_i46623_1_);
        this.destinationType = destinationType;
    }

    @Override
    public ItemStack apply(ItemStack stack, Random random, LootContext context)
    {
        TreasureMap type = TreasureMap.getByName(this.destinationType);
        if(type != null)
        {
            ItemStack map = type.generateMap(context.getWorld(),
                    new BlockPos(random.nextInt(10000) - 5000, 64,
                            random.nextInt(10000) - 5000), random);
            if (!map.isEmpty())
            {
                return map;
            }
        }
        return ItemStack.EMPTY;
    }

    public static class Serializer extends
            net.minecraft.world.storage.loot.functions.LootFunction.Serializer<ExplorationMapLootFunction>
    {
        public Serializer()
        {
            super(new ResourceLocation("explore_map"), ExplorationMapLootFunction.class);
        }

        public void serialize(JsonObject p_serialize_1, ExplorationMapLootFunction p_serialize_2,
                              JsonSerializationContext p_serialize_3)
        {
            p_serialize_1.add("destinationType", p_serialize_3.serialize(p_serialize_2.destinationType));
        }

        public ExplorationMapLootFunction deserialize(JsonObject p_deserialize_1,
                                                      JsonDeserializationContext p_deserialize_2,
                                                      LootCondition[] p_deserialize_3)
        {
            return new ExplorationMapLootFunction(p_deserialize_3,
                    JsonUtils.deserializeClass(p_deserialize_1, "destinationType",
                            p_deserialize_2, String.class));
        }
    }
}
