package com.halemaster.minesnmagic.magic.network;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.io.UnsupportedEncodingException;

public class CastSpellMessage implements IMessage {
    // A default constructor is always required
    public CastSpellMessage(){}

    private String spellCast;
    public CastSpellMessage(String spellCast) {
        this.spellCast = spellCast;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, spellCast);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        spellCast = ByteBufUtils.readUTF8String(buf);
    }

    public String getSpellCast() {
        return spellCast;
    }
}
