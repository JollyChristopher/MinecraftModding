/**
 * Created by Halemaster on 10/18/2016.
 */

package com.halemaster.minesnmagic;

import net.minecraft.init.Blocks;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

@Mod(modid = MinesnMagicMod.MODID, version = MinesnMagicMod.VERSION)
public class MinesnMagicMod
{
    public static final String MODID = "minesnmagic";
    public static final String VERSION = "1.10.2-0.0.1.0";

    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
        // some example code
        System.out.println("DIRT BLOCK >> "+ Blocks.DIRT.getUnlocalizedName());
    }
}
