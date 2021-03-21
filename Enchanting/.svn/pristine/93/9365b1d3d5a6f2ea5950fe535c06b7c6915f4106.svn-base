package com.halemaster.enchanting.basic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.util.StatCollector;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import com.halemaster.enchanting.EnchantingMod;
import com.halemaster.enchanting.basic.spell.SpellAdrenaline;
import com.halemaster.enchanting.basic.spell.SpellDarkvision;
import com.halemaster.enchanting.basic.spell.SpellDecay;
import com.halemaster.enchanting.basic.spell.SpellFireBall;
import com.halemaster.enchanting.basic.spell.SpellFireImmunity;
import com.halemaster.enchanting.basic.spell.SpellFlash;
import com.halemaster.enchanting.basic.spell.SpellHaste;
import com.halemaster.enchanting.basic.spell.SpellHealing;
import com.halemaster.enchanting.basic.spell.SpellHolyLight;
import com.halemaster.enchanting.basic.spell.SpellHopping;
import com.halemaster.enchanting.basic.spell.SpellIceBolt;
import com.halemaster.enchanting.basic.spell.SpellInvisibility;
import com.halemaster.enchanting.basic.spell.SpellInvulnerability;
import com.halemaster.enchanting.basic.spell.SpellMageShield;
import com.halemaster.enchanting.basic.spell.SpellNecroBlast;
import com.halemaster.enchanting.basic.spell.SpellPoisonBolt;
import com.halemaster.enchanting.basic.spell.SpellPower;
import com.halemaster.enchanting.basic.spell.SpellRegeneration;
import com.halemaster.enchanting.basic.spell.SpellSapStrength;
import com.halemaster.enchanting.basic.spell.SpellSickness;
import com.halemaster.enchanting.basic.spell.SpellSwiftness;
import com.halemaster.enchanting.basic.spell.SpellVigor;
import com.halemaster.enchanting.basic.spell.SpellWaterBreathing;
import com.halemaster.enchanting.spell.Spell;

@Mod(modid = BasicSpellbookMod.MODID, version = BasicSpellbookMod.VERSION)
public class BasicSpellbookMod
{
	public static final String MODID = "basic_spellbook";
	public static final String VERSION = "1.0";
	public static final String NAME = "basic.mod.name";
	public static final String DESCRIPTION = "basic.mod.description";
	public static final String URL = "https://www.assembla.com/spaces/mines-magic/wiki";
	public static final String CREDITS = "";
	public static final String LOGO = "basic.logo.png";
	public static final List<String> AUTHORS = Arrays.asList(new String[]{"Halemaster"});
	
	public static final List<Class<? extends Spell>> spellList;
	
	static
	{
		spellList = new ArrayList<>();
		spellList.add(SpellAdrenaline.class);
		spellList.add(SpellDarkvision.class);
		spellList.add(SpellDecay.class);
		spellList.add(SpellFireBall.class);
		spellList.add(SpellFireImmunity.class);
		spellList.add(SpellFlash.class);
		spellList.add(SpellHaste.class);
		spellList.add(SpellHealing.class);
		spellList.add(SpellHolyLight.class);
		spellList.add(SpellHopping.class);
		spellList.add(SpellIceBolt.class);
		spellList.add(SpellInvisibility.class);
		spellList.add(SpellInvulnerability.class);
		spellList.add(SpellMageShield.class);
		spellList.add(SpellNecroBlast.class);
		spellList.add(SpellPoisonBolt.class);
		spellList.add(SpellPower.class);
		spellList.add(SpellRegeneration.class);
		spellList.add(SpellSapStrength.class);
		spellList.add(SpellSickness.class);
		spellList.add(SpellSwiftness.class);
		spellList.add(SpellVigor.class);
		spellList.add(SpellWaterBreathing.class);
	}
	
	@Instance(value = MODID)
	public static BasicSpellbookMod instance;

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
		
		EnchantingMod.registerAllSpells(spellList);
	}

	@EventHandler
	public void init(FMLInitializationEvent event)
	{
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
	}
}
