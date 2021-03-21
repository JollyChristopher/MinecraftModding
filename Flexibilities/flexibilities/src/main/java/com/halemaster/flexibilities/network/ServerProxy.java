package com.halemaster.flexibilities.network;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IThreadListener;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class ServerProxy extends CommonProxy {
    @Override
    public void preInit(FMLPreInitializationEvent e) {
        super.preInit(e);
    }

    @Override
    public EntityPlayer getPlayerFromContext(MessageContext ctx) {
        return ctx.side == Side.CLIENT ? null : ctx.getServerHandler().player;
    }

    @Override
    public IThreadListener getListenerFromContext(MessageContext ctx) {
        return ctx.side == Side.CLIENT ? null : (WorldServer) ctx.getServerHandler().player.world;
    }
}