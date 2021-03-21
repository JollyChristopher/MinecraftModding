/**
 * Created by Halemaster on 1/10/2017.
 */

package com.halemaster.mapping.event;

import net.minecraft.block.BlockBookshelf;
import net.minecraft.block.BlockCauldron;
import net.minecraft.block.BlockSponge;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.storage.MapData;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static com.halemaster.mapping.recipes.MapColorRecipe.MAP_BLANK_COLOR;

public class ClickMapEventHandler
{
    @SubscribeEvent
    public void rightClickBlock(PlayerInteractEvent.RightClickBlock event)
    {
        if(event.getEntityPlayer().getHeldItemMainhand().getItem() instanceof ItemMap)
        {
            if (event.getWorld().getBlockState(event.getPos()).getBlock() instanceof BlockCauldron &&
                    event.getWorld().getBlockState(event.getPos()).getValue(BlockCauldron.LEVEL) > 0)
            {
                if (ItemMap.getColor(event.getEntityPlayer().getHeldItemMainhand()) != MAP_BLANK_COLOR)
                {
                    event.getEntityPlayer().getHeldItemMainhand()
                            .getOrCreateSubCompound("display").setInteger("MapColor", MAP_BLANK_COLOR);
                    event.getWorld().playSound(null, event.getPos(), SoundEvents.ITEM_ARMOR_EQUIP_GENERIC,
                            SoundCategory.BLOCKS, 1.0F, 1.0F);
                    ((BlockCauldron) event.getWorld().getBlockState(event.getPos()).getBlock())
                            .setWaterLevel(event.getWorld(), event.getPos(),
                                    event.getWorld().getBlockState(event.getPos()),
                                    event.getWorld().getBlockState(event.getPos()).getValue(BlockCauldron.LEVEL) - 1);
                }
            }
            else if (event.getWorld().getBlockState(event.getPos()).getBlock() instanceof BlockSponge &&
                    event.getWorld().getBlockState(event.getPos()).getValue(BlockSponge.WET))
            {
                ItemStack itemStack = event.getEntityPlayer().getHeldItemMainhand();
                MapData mapData = ((ItemMap) itemStack.getItem()).getMapData(itemStack, event.getWorld());
                if (null != mapData)
                {
                    mapData.colors = new byte[16384];
                    mapData.setDirty(true);
                    event.getWorld().playSound(null, event.getPos(), SoundEvents.ITEM_ARMOR_EQUIP_GENERIC,
                            SoundCategory.BLOCKS, 1.0F, 1.0F);
                }
            }
            else if (event.getWorld().getBlockState(event.getPos()).getBlock() instanceof BlockBookshelf)
            {
                ItemStack itemStack = event.getEntityPlayer().getHeldItemMainhand();
                MapData mapData = ((ItemMap) itemStack.getItem()).getMapData(itemStack, event.getWorld());
                if (null != mapData && mapData.scale < 4) // needed because at scale 4 the game crashes
                {
                    ItemMap.renderBiomePreviewMap(event.getWorld(), event.getEntityPlayer().getHeldItemMainhand());
                    event.getWorld().playSound(null, event.getPos(), SoundEvents.ITEM_ARMOR_EQUIP_GENERIC,
                            SoundCategory.BLOCKS, 1.0F, 1.0F);
                }
            }
        }
    }
}
