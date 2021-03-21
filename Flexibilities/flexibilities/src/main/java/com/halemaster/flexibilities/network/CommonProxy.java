package com.halemaster.flexibilities.network;

import com.halemaster.flexibilities.FlexibilitiesMod;
import com.halemaster.flexibilities.spell.SpellCooldown;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

import static com.halemaster.flexibilities.FlexibilitiesMod.MODID;

public class CommonProxy {
    public static SimpleNetworkWrapper NETWORK;

    public void preInit(FMLPreInitializationEvent e) {
        CapabilityManager.INSTANCE.register(SpellCooldown.class, new FlexibilitiesMod.Storage(), FlexibilitiesMod.DefaultImpl.class);
        NETWORK = NetworkRegistry.INSTANCE.newSimpleChannel(MODID);
        NETWORK.registerMessage(SpellCooldownMessageHandler.class, SpellCooldownMessage.class, 1, Side.CLIENT);
    }

    public EntityPlayer getPlayerFromContext(MessageContext ctx) {
        return null;
    }

    public IThreadListener getListenerFromContext(MessageContext ctx) {
        return null;
    }
}