package com.halemaster.minesnmagic.magic.network;

import com.halemaster.minesnmagic.MinesMagicMod;
import com.halemaster.minesnmagic.magic.Spell;
import com.halemaster.minesnmagic.magic.SpellBook;
import com.halemaster.minesnmagic.magic.SpellBookCapability;
import com.halemaster.minesnmagic.magic.wrappers.EntityProxy;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class CastSpellMessageHandler implements IMessageHandler<CastSpellMessage, IMessage> {
    @Override public IMessage onMessage(CastSpellMessage message, MessageContext ctx) {
        EntityPlayerMP serverPlayer = ctx.getServerHandler().player;
        String spellId = message.getSpellCast();

        serverPlayer.getServerWorld().addScheduledTask(() -> {
            Spell spell = MinesMagicMod.getLibrary().getSpell(spellId);
            SpellBook spellBook = serverPlayer.getCapability(SpellBookCapability.capabilitySpellbook, null);
            if(null == spellBook || !spellBook.startCasting(spellId)) {
                MinesMagicMod.logger.warn("Somehow casting a spell a second time");
            } else {
                // todo: call can cast and start cast etc before the start casting call
                System.out.println("Started cast");
            }
        });
        return null;
    }
}
