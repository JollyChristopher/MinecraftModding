package com.halemaster.flexibilities;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.halemaster.flexibilities.command.AcceptInviteCommand;
import com.halemaster.flexibilities.command.CommandDamage;
import com.halemaster.flexibilities.command.InvitePartyCommand;
import com.halemaster.flexibilities.command.PromoteCommand;
import com.halemaster.flexibilities.item.FlexibilitiesRenderItem;
import com.halemaster.flexibilities.item.Scroll;
import com.halemaster.flexibilities.network.CommonProxy;
import com.halemaster.flexibilities.party.PartyManager;
import com.halemaster.flexibilities.spell.Spell;
import com.halemaster.flexibilities.spell.SpellCooldown;
import com.halemaster.flexibilities.network.SpellCooldownMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.block.model.ModelManager;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static com.halemaster.flexibilities.network.CommonProxy.NETWORK;

@Mod(modid = FlexibilitiesMod.MODID, version = FlexibilitiesMod.VERSION)
public class FlexibilitiesMod {
    public static final String MODID = "flexibilities";
    public static final String VERSION = "0.5";

    public static Scroll scroll = new Scroll();
    public static Map<String, Spell> localSpells;
    public static Map<String, Spell> worldSpells;


    @SidedProxy(clientSide="com.halemaster.flexibilities.network.ClientProxy", serverSide="com.halemaster.flexibilities.network.ServerProxy")
    public static CommonProxy proxy;

    @CapabilityInject(SpellCooldown.class)
    public static final Capability<SpellCooldown> SPELL_COOLDOWN_CAPABILITY = null;

    public static Spell getSpellFromId(String id) {
        return localSpells.get(id);
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        proxy.preInit(event);
    }
    
    @EventHandler
    public void init(FMLInitializationEvent event) {
        if (event.getSide().isClient()) {
            registerRenderers();
        }

        localSpells = Arrays.stream(loadLocalSpells()).collect(Collectors.toMap(Spell::getId, spell -> spell));
    }

    @EventHandler
    public void serverStart(FMLServerStartingEvent event) {
        event.registerServerCommand(new CommandDamage());
        event.registerServerCommand(new InvitePartyCommand());
        event.registerServerCommand(new AcceptInviteCommand());
        event.registerServerCommand(new PromoteCommand());
    }

    private Spell[] loadLocalSpells() {
        Gson gson = new GsonBuilder().create();
        try(InputStreamReader reader = new InputStreamReader((getClass().getClassLoader()
                .getResourceAsStream("assets/flexibilities/conf/spells.json")))) {
            return gson.fromJson(reader, Spell[].class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //todo: load from config first

        return null;
    }

    @SideOnly(Side.CLIENT)
    public static void registerItem(Item item, int metadata, String itemName) {
        ItemModelMesher mesher = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();
        mesher.register(item, metadata, new ModelResourceLocation(itemName, "inventory"));
    }

    public void registerRenderers() {
        registerItem(scroll, 0, MODID + ":" + Scroll.NAME);

        Minecraft.getMinecraft().getItemColors().registerItemColorHandler(Scroll::getColor, scroll);

        try {
            Field renderItem = Minecraft.class.getDeclaredField("renderItem");
            renderItem.setAccessible(true);
            Field modelManager = Minecraft.class.getDeclaredField("modelManager");
            modelManager.setAccessible(true);
            FlexibilitiesRenderItem flexibilitiesRenderItem = new FlexibilitiesRenderItem(Minecraft.getMinecraft()
                    .getRenderItem(), Minecraft.getMinecraft().renderEngine,
                    (ModelManager) modelManager.get(Minecraft.getMinecraft()),
                    Minecraft.getMinecraft().getItemColors());

            renderItem.set(Minecraft.getMinecraft(), flexibilitiesRenderItem);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

//        RenderingRegistry.registerEntityRenderingHandler(EntitySpellProjectile.class, new RenderSpellProjectile(
//                Minecraft.getMinecraft().getRenderManager(), Minecraft.getMinecraft().getRenderItem()));
    }

    @Mod.EventBusSubscriber(modid = FlexibilitiesMod.MODID)
    public static class RegistrationHandler {
        @SubscribeEvent
        public static void registerItems(final RegistryEvent.Register<Item> event) {
            final IForgeRegistry<Item> registry = event.getRegistry();
            registry.register(scroll);
        }

        @SubscribeEvent
        public static void playerLoggedIn(final PlayerEvent.PlayerLoggedInEvent event) {
            PartyManager.createParty(event.player);
        }

        @SubscribeEvent
        public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
            if(event.side == Side.SERVER && event.player.world.getTotalWorldTime() % 20 == 0) {
                EntityPlayer player = event.player;
                SpellCooldown capabilities = player.getCapability(SPELL_COOLDOWN_CAPABILITY, null);
                NETWORK.sendTo(new SpellCooldownMessage(capabilities), (EntityPlayerMP) player);
            }
        }

        @SubscribeEvent
        public static void onEntityConstruct(AttachCapabilitiesEvent evt)
        {
            evt.addCapability(new ResourceLocation(MODID, "SpellCooldown"),
                    new ICapabilitySerializable<NBTTagCompound>()
            {
                SpellCooldown inst = SPELL_COOLDOWN_CAPABILITY.getDefaultInstance();
                @Override
                public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
                    return capability == SPELL_COOLDOWN_CAPABILITY;
                }

                @Override
                public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
                    return capability == SPELL_COOLDOWN_CAPABILITY ? SPELL_COOLDOWN_CAPABILITY.<T>cast(inst) : null;
                }

                @Override
                public NBTTagCompound serializeNBT() {
                    return (NBTTagCompound) SPELL_COOLDOWN_CAPABILITY.getStorage()
                            .writeNBT(SPELL_COOLDOWN_CAPABILITY, inst, null);
                }

                @Override
                public void deserializeNBT(NBTTagCompound nbt) {
                    SPELL_COOLDOWN_CAPABILITY.getStorage().readNBT(SPELL_COOLDOWN_CAPABILITY, inst, null, nbt);
                }
            });
        }
    }

    public static class Storage implements Capability.IStorage<SpellCooldown>
    {
        @Override
        public NBTBase writeNBT(Capability<SpellCooldown> capability, SpellCooldown instance, EnumFacing side)
        {
            NBTTagCompound cooldownList = new NBTTagCompound();
            for(String spellId : instance.getCooldownSpells()) {
                cooldownList.setLong(spellId, instance.getCooldownStart(spellId));
            }
            return cooldownList;
        }

        @Override
        public void readNBT(Capability<SpellCooldown> capability, SpellCooldown instance, EnumFacing side, NBTBase nbt)
        {
            NBTTagCompound cooldownList = (NBTTagCompound) nbt;
            for(String spellId : cooldownList.getKeySet()) {
                instance.setCooldownStart(spellId, cooldownList.getInteger(spellId));
            }
        }
    }

    public static class DefaultImpl implements SpellCooldown
    {
        Map<String, Long> cooldowns = new HashMap<>();

        @Override
        public long getCooldownStart(String spellId) {
            return cooldowns.getOrDefault(spellId, -1L);
        }

        @Override
        public void setCooldownStart(String spellId, long cooldownStart) {
            cooldowns.put(spellId, cooldownStart);
        }

        @Override
        public void removeCooldownStart(String spellId) {
            cooldowns.remove(spellId);
        }

        @Override
        public String[] getCooldownSpells() {
            return cooldowns.keySet().toArray(new String[cooldowns.size()]);
        }
    }
}
