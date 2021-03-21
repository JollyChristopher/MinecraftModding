package com.halemaster.minesnmagic;

import com.halemaster.minesnmagic.magic.network.CastSpellEndMessage;
import com.halemaster.minesnmagic.magic.network.CastSpellMessage;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EventHandler {
    private boolean castHeld = false;

    @SideOnly(Side.CLIENT)
    @SubscribeEvent(priority= EventPriority.NORMAL, receiveCanceled=true)
    public void onEvent(InputEvent.KeyInputEvent event) {
        if (ClientProxy.castKey.isPressed()) {
            castHeld = true;
            MinesMagicMod.INSTANCE.sendToServer(new CastSpellMessage("base.arcane.bolt"));
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent(priority= EventPriority.NORMAL, receiveCanceled=true)
    public void onEvent(TickEvent event) {
        if(castHeld && !ClientProxy.castKey.isKeyDown()) {
            castHeld = false;
            MinesMagicMod.INSTANCE.sendToServer(new CastSpellEndMessage());
        }
    }

    @SubscribeEvent(priority= EventPriority.NORMAL, receiveCanceled=true)
    public void onEvent(AttachCapabilitiesEvent<Entity> event) {
        if(event.getObject() instanceof EntityLiving) {
            event.addCapability(new ResourceLocation("SpellBook"), MinesMagicMod.SPELL_BOOK_CAPABILITY);
        }
    }
}
