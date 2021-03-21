package com.halemaster.flexibilities.network;

import com.halemaster.flexibilities.FlexibilitiesMod;
import com.halemaster.flexibilities.spell.SpellCooldown;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;

import java.nio.charset.Charset;

public class SpellCooldownMessage implements IMessage {
    public static final char VALUE_SEPARATOR = ':';
    public static final char KEY_SEPARATOR = ',';

    private SpellCooldown value;

    public SpellCooldownMessage() {
        this.value = null;
    }

    public SpellCooldownMessage(SpellCooldown value) {
        this.value = value;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        if(FMLCommonHandler.instance().getSide() == Side.CLIENT && null != Minecraft.getMinecraft().player) {
            this.value = Minecraft.getMinecraft().player.getCapability(FlexibilitiesMod.SPELL_COOLDOWN_CAPABILITY,
                    null);
        }
        if(this.value != null) {
            String fullMessage = buf.toString(Charset.defaultCharset());
            if(!"".equals(fullMessage)) {
                String[] keyValues = fullMessage.split(String.valueOf(KEY_SEPARATOR));
                for (String keyValue : keyValues) {
                    String[] values = keyValue.split(String.valueOf(VALUE_SEPARATOR));
                    this.value.setCooldownStart(values[0], Long.valueOf(values[1]));
                }
            }
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        if(this.value != null) {
            for(String spellId : value.getCooldownSpells()) {
                buf.writeBytes((spellId + VALUE_SEPARATOR + String.valueOf(value.getCooldownStart(spellId)) +
                        KEY_SEPARATOR).getBytes());
            }
        }
    }

    public void setValue(SpellCooldown value) {
        this.value = value;
    }

    public SpellCooldown getValue() {
        return this.value;
    }
}
