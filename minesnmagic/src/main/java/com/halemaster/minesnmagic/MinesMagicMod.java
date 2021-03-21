package com.halemaster.minesnmagic;

import com.halemaster.minesnmagic.magic.Library;
import com.halemaster.minesnmagic.magic.SpellBook;
import com.halemaster.minesnmagic.magic.SpellBookCapability;
import com.halemaster.minesnmagic.magic.network.CastSpellEndMessage;
import com.halemaster.minesnmagic.magic.network.CastSpellEndMessageHandler;
import com.halemaster.minesnmagic.magic.network.CastSpellMessage;
import com.halemaster.minesnmagic.magic.network.CastSpellMessageHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.Logger;

import java.io.File;

@Mod(modid = MinesMagicMod.MODID, name = MinesMagicMod.NAME, version = MinesMagicMod.VERSION)
public class MinesMagicMod
{
    public static final String MODID = "minesnmagic";
    public static final String NAME = "Mines and Magic";
    public static final String VERSION = "1.0";
    public static Logger logger;

    @SidedProxy(
            clientSide="com.halemaster.minesnmagic.ClientProxy",
            serverSide="com.halemaster.minesnmagic.ServerProxy"
    )
    public static IProxy proxy;

    public static com.halemaster.minesnmagic.EventHandler handler;
    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(MODID);
    private static int packet = 0;

    public static final SpellBookCapability SPELL_BOOK_CAPABILITY = new SpellBookCapability();

    public static final Library SPELL_LIBRARY = new Library();
    public static Library SPELL_LIBRARY_REMOTE;

    public static Library getLibrary() {
        return SPELL_LIBRARY; // todo: use remote once you've logged into a server, which will send you their library.
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();
        File spellFolder = new File(event.getModConfigurationDirectory().getAbsolutePath() + "/minesnmagic/spells");
        if(!spellFolder.exists()) {
            if(!spellFolder.mkdirs()) {
                MinesMagicMod.logger.warn("Unable to create spell directory in config!");
            }
        }
        SPELL_LIBRARY.loadSpells(spellFolder);

        INSTANCE.registerMessage(CastSpellMessageHandler.class, CastSpellMessage.class, packet++, Side.SERVER);
        INSTANCE.registerMessage(CastSpellEndMessageHandler.class, CastSpellEndMessage.class, packet++, Side.SERVER);

        // todo: need to clone the spell book on death
        CapabilityManager.INSTANCE.register(SpellBook.class, new SpellBook.Storage(), new SpellBook.Factory());
        proxy.preInit(event);
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        handler = new com.halemaster.minesnmagic.EventHandler();
        proxy.init(event);
        MinecraftForge.EVENT_BUS.register(handler);
    }
}
