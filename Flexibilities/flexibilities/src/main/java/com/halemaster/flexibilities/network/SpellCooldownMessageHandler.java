package com.halemaster.flexibilities.network;

import com.halemaster.flexibilities.FlexibilitiesMod;
import com.halemaster.flexibilities.spell.SpellCooldown;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SpellCooldownMessageHandler implements IMessageHandler<SpellCooldownMessage, IMessage> {

    @Override
    public IMessage onMessage(SpellCooldownMessage message, MessageContext ctx) {
        IThreadListener thread = FlexibilitiesMod.proxy.getListenerFromContext(ctx);
        final EntityPlayer player = FlexibilitiesMod.proxy.getPlayerFromContext(ctx);
        final SpellCooldown value = message.getValue();
        thread.addScheduledTask(() -> {
            if(player != null) {
                SpellCooldown capabilities = player.getCapability(FlexibilitiesMod.SPELL_COOLDOWN_CAPABILITY,
                        null);
                if(capabilities != null && value != null) {
                    for(String spellId : value.getCooldownSpells()) {
                        capabilities.setCooldownStart(spellId, value.getCooldownStart(spellId));
                    }
                }
            }
        });
        return null;
    }

}