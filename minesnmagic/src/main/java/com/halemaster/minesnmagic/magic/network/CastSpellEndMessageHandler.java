package com.halemaster.minesnmagic.magic.network;

import com.halemaster.minesnmagic.MinesMagicMod;
import com.halemaster.minesnmagic.magic.SpellBook;
import com.halemaster.minesnmagic.magic.SpellBookCapability;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class CastSpellEndMessageHandler implements IMessageHandler<CastSpellEndMessage, IMessage> {
    @Override public IMessage onMessage(CastSpellEndMessage message, MessageContext ctx) {
        EntityPlayerMP serverPlayer = ctx.getServerHandler().player;

        serverPlayer.getServerWorld().addScheduledTask(() -> {
            SpellBook spellBook = serverPlayer.getCapability(SpellBookCapability.capabilitySpellbook, null);
            if(null == spellBook || !spellBook.stopCasting()) {
                MinesMagicMod.logger.warn("Not casting a spell");
            } else {
                // todo: call cast on the spell. Will have needed the id beforehand
                System.out.println("Stopped cast");
            }
        });
        return null;
    }
}