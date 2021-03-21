package com.halemaster.minesnmagic.magic.network;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class CastSpellEndMessage implements IMessage {
    // A default constructor is always required
    public CastSpellEndMessage(){}

    @Override
    public void toBytes(ByteBuf buf) {

    }

    @Override
    public void fromBytes(ByteBuf buf) {

    }
}