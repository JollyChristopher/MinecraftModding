package com.halemaster.enchanting;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import scala.actors.threadpool.Arrays;

import com.halemaster.enchanting.spell.EntitySpellProjectile;
import com.halemaster.enchanting.spell.RenderSpellProjectile;
import com.halemaster.enchanting.spell.Spell;
import com.halemaster.enchanting.spell.item.Scroll;
import com.halemaster.enchanting.spell.item.Staff;
import com.halemaster.enchanting.spell.item.Wand;
import com.halemaster.enchanting.spell.item.enchants.EnchantmentBattle;
import com.halemaster.enchanting.spell.item.enchants.EnchantmentQuick;

@Mod(modid = EnchantingMod.MODID, version = EnchantingMod.VERSION)
public class EnchantingMod
{
	public static final String MODID = "enchanting";
	public static final String VERSION = "1.0";
	public static final String NAME = "enchanting.mod.name";
	public static final String DESCRIPTION = "enchanting.mod.description";
	public static final String URL = "https://www.assembla.com/spaces/mines-magic/wiki";
	public static final String CREDITS = "Alex for the name";
	public static final String LOGO = "enchanting.logo.png";
	public static final List<String> AUTHORS = Arrays.asList(new String[]{"Halemaster"});
	public static final int ENCHANTS_START = 99;

	@Instance(value = MODID)
	public static EnchantingMod instance;

	public static Scroll scroll;
	public static Wand wooden_wand;
	public static Wand emerald_wand;
	public static Wand star_wand;
	public static Staff wooden_staff;
	public static Staff emerald_staff;
	public static Staff star_staff;

	public static ArmorCloth clothHelmet;
	public static ArmorCloth clothChest;
	public static ArmorCloth clothLegs;
	public static ArmorCloth clothBoots;
	
	public static List<Spell> spellList;
	public static List<Item> spellRenders;

	// projectile items for rendering
	public static Item default_spell;

	public static Set<Enchantment> boostSpellEnchants = new HashSet<Enchantment>();
	public static EnchantmentQuick enchantmentQuick;
	public static EnchantmentBattle enchantmentBattle;

	public static CreativeTabs tabSpell = new CreativeTabs("tabSpell")
	{
		@Override
		@SideOnly(Side.CLIENT)
		public Item getTabIconItem()
		{
			return scroll;
		}

		@Override
		public boolean hasSearchBar()
		{
			return false;
		}

		@Override
		@SideOnly(Side.CLIENT)
		public void displayAllReleventItems(List itemList)
		{
			super.displayAllReleventItems(itemList);
			// Adds all spell enchantments
			this.addEnchantmentBooksToList(itemList, new EnumEnchantmentType[] { Spell.SPELL_TYPE });
		}
	};

	@SideOnly(Side.CLIENT)
	public static void registerItem(Item item, int metadata, String itemName)
	{
		ItemModelMesher mesher = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();
		mesher.register(item, metadata, new ModelResourceLocation(itemName, "inventory"));
	}

	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		event.getModMetadata().version = VERSION;
		event.getModMetadata().modId = MODID;
		event.getModMetadata().authorList = AUTHORS;
		event.getModMetadata().credits = CREDITS;
		event.getModMetadata().description = StatCollector.translateToLocal(DESCRIPTION);
		event.getModMetadata().logoFile = LOGO;
		event.getModMetadata().name = StatCollector.translateToLocal(NAME);
		event.getModMetadata().url = URL;

		registerItems();
		registerEnchants();
		registerEntities();
		boostSpellEnchants.add(Enchantment.unbreaking);
	}

	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		if (event.getSide().isClient())
		{
			renderAllSpells();
			registerRenderers();
		}
		registerRecipes();
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
	}
	
	public static void registerAllSpells(Collection<Class<? extends Spell>> classes)
	{
		spellList = new ArrayList<>();
		// find all spells 
		for(Class<? extends Spell> spellClass : classes)
		{
			// try to construct spell
			try
			{
				Spell spell = spellClass.newInstance();

				// add to list of spells
				spellList.add(spell);
			}
			catch(InstantiationException | IllegalAccessException e)
			{
				// not a bad thing, just a Spell that has arguments.
				System.out.println("Failed to load Spell of type " + spellClass.getName());
			}
		}
	}
	
	private void renderAllSpells()
	{
		registerItem(default_spell, 0, MODID + ":" + "default");
		spellRenders = new ArrayList<>();
		for(Spell spell : spellList)
		{
			Item render = spell.getRender();
			if(render != null)
			{
				spellRenders.add(render);
				registerItem(render, 0, spell.getModId() + ":" + spell.getRenderName());
			}
		}
	}

	public void registerRenderers()
	{
		registerItem(scroll, 0, MODID + ":" + Scroll.NAME);
		registerItem(wooden_wand, 0, MODID + ":" + Wand.NAME + ".wood");
		registerItem(emerald_wand, 0, MODID + ":" + Wand.NAME + ".emerald");
		registerItem(star_wand, 0, MODID + ":" + Wand.NAME + ".star");
		registerItem(wooden_staff, 0, MODID + ":" + Staff.NAME + ".wood");
		registerItem(emerald_staff, 0, MODID + ":" + Staff.NAME + ".emerald");
		registerItem(star_staff, 0, MODID + ":" + Staff.NAME + ".star");
		registerItem(clothHelmet, 0, MODID + ":" + ArmorCloth.NAME + ArmorCloth.getNameFromPart(0));
		registerItem(clothChest, 0, MODID + ":" + ArmorCloth.NAME + ArmorCloth.getNameFromPart(1));
		registerItem(clothLegs, 0, MODID + ":" + ArmorCloth.NAME + ArmorCloth.getNameFromPart(2));
		registerItem(clothBoots, 0, MODID + ":" + ArmorCloth.NAME + ArmorCloth.getNameFromPart(3));

		RenderingRegistry.registerEntityRenderingHandler(EntitySpellProjectile.class, new RenderSpellProjectile(
				Minecraft.getMinecraft().getRenderManager(), Minecraft.getMinecraft().getRenderItem()));
	}

	public void registerEnchants()
	{
		int enchId = ENCHANTS_START;
		enchantmentQuick = new EnchantmentQuick(enchId--);
		enchantmentBattle = new EnchantmentBattle(enchId--);
	}

	public void registerEntities()
	{
		EntityRegistry.registerModEntity(EntitySpellProjectile.class, "Spell Projectile", 12000, this, 16, 1, true);
	}

	public void registerItems()
	{
		scroll = new Scroll();
		GameRegistry.registerItem(scroll, Scroll.NAME);
		wooden_wand = new Wand("wood", 20, 4);
		GameRegistry.registerItem(wooden_wand, Wand.NAME + ".wood");
		emerald_wand = new Wand("emerald", 40, 8);
		GameRegistry.registerItem(emerald_wand, Wand.NAME + ".emerald");
		star_wand = new Wand("star", 80, 16);
		GameRegistry.registerItem(star_wand, Wand.NAME + ".star");
		wooden_staff = new Staff("wood", 40, 5);
		GameRegistry.registerItem(wooden_staff, Staff.NAME + ".wood");
		emerald_staff = new Staff("emerald", 80, 9);
		GameRegistry.registerItem(emerald_staff, Staff.NAME + ".emerald");
		star_staff = new Staff("star", 160, 17);
		GameRegistry.registerItem(star_staff, Staff.NAME + ".star");

		GameRegistry.registerItem(clothHelmet = new ArmorCloth(0), ArmorCloth.NAME + ArmorCloth.getNameFromPart(0));
		GameRegistry.registerItem(clothChest = new ArmorCloth(1), ArmorCloth.NAME + ArmorCloth.getNameFromPart(1)); 
		GameRegistry.registerItem(clothLegs = new ArmorCloth(2), ArmorCloth.NAME + ArmorCloth.getNameFromPart(2));
		GameRegistry.registerItem(clothBoots = new ArmorCloth(3), ArmorCloth.NAME + ArmorCloth.getNameFromPart(3)); 

		// projectile items
		default_spell = new Item().setUnlocalizedName("default").setMaxStackSize(1);
		GameRegistry.registerItem(default_spell, "default");
	}

	public void registerRecipes()
	{
		GameRegistry.addShapelessRecipe(new ItemStack(scroll), new ItemStack(Items.paper), new ItemStack(Items.string));
		GameRegistry.addRecipe(new ItemStack(wooden_wand), "x ", " x", 'x', new ItemStack(Items.stick));
		GameRegistry.addRecipe(new ItemStack(emerald_wand), "y ", " x", 'x', new ItemStack(Items.blaze_rod), 'y',
				new ItemStack(Items.emerald));
		GameRegistry.addRecipe(new ItemStack(star_wand), "y ", " x", 'x', new ItemStack(Items.diamond), 'y',
				new ItemStack(Items.nether_star));
		GameRegistry.addRecipe(new ItemStack(wooden_staff), "  x", " xy", "x  ", 'x', new ItemStack(Items.stick), 'y',
				new ItemStack(Blocks.planks));
		GameRegistry.addRecipe(new ItemStack(emerald_staff), "  y", " xx", "x  ", 'x', new ItemStack(Items.blaze_rod),
				'y', new ItemStack(Items.emerald));
		GameRegistry.addRecipe(new ItemStack(star_staff), "  y", " xx", "x  ", 'x', new ItemStack(Items.diamond), 'y',
				new ItemStack(Items.nether_star));
		GameRegistry.addRecipe(new ItemStack(clothHelmet), "xxx", "x x", 'x', Blocks.wool);
		GameRegistry.addRecipe(new ItemStack(clothChest), "x x", "xxx", "xxx", 'x', Blocks.wool);
		GameRegistry.addRecipe(new ItemStack(clothLegs), "xxx", "x x", "x x", 'x', Blocks.wool);
		GameRegistry.addRecipe(new ItemStack(clothBoots), "x x", "x x", 'x', Blocks.wool);
		GameRegistry.addRecipe(new RecipesDyeCloth());
	}
	
	public static Spell getSpell(int enchID)
	{
		Spell spell = null;
		
		for(Spell possible : spellList)
		{
			if(possible.effectId == enchID)
			{
				spell = possible;
			}
		}
		
		return spell;
	}
}
