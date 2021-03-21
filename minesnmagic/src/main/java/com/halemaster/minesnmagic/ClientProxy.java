package com.halemaster.minesnmagic;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.lwjgl.input.Keyboard;

public class ClientProxy implements IProxy {
    public static KeyBinding castKey;
    public static KeyBinding switchKey;
    public static KeyBinding bookKey;

    @Override
    public void preInit(FMLPreInitializationEvent event) {

    }

    @Override
    public void init(FMLInitializationEvent event) {
        castKey = new KeyBinding("key.spell_cast.desc", Keyboard.KEY_V, "key.minesnmagic.category");
        switchKey = new KeyBinding("key.spell_switch.desc", Keyboard.KEY_LMENU, "key.minesnmagic.category");
        bookKey = new KeyBinding("key.spell_book.desc", Keyboard.KEY_P, "key.minesnmagic.category");

        ClientRegistry.registerKeyBinding(castKey);
        ClientRegistry.registerKeyBinding(switchKey);
        ClientRegistry.registerKeyBinding(bookKey);
    }
}
