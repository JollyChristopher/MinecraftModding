package com.halemaster.enchanting.testing.tests.spell;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import org.junit.BeforeClass;
import org.junit.Test;

import scala.actors.threadpool.Arrays;

import com.halemaster.enchanting.EnchantingMod;
import com.halemaster.enchanting.spell.Spell;
import com.halemaster.enchanting.testing.TestingMod;

public class SpellTest 
{
	private static SpellBasic spellBasic;
	
	@BeforeClass
	public static void init()
	{
		spellBasic = new SpellBasic();
	}
	
	@Test
	public void testInBookList()
	{
		assertTrue("Basic Spell should appear in enchantmentBookList",
				Arrays.asList((Enchantment.enchantmentsBookList)).contains(spellBasic));
	}
	
	@Test
	public void testDescription()
	{
		String[] desc = spellBasic.getDescription();
		// for some reason, in gradle command line this is not localized but from eclipse it is.
		// this will need to be addressed in the future.
		assertEquals("First should be localized","Basic Mock Spell Description",desc[0]);
		assertEquals("Second should not be localized","spell.basic.test.desc.2",desc[1]);
	}
	
	@Test
	public void testCanApplyTogether()
	{
		assertTrue("Can Apply with battle enchantment", 
				spellBasic.canApplyTogether(EnchantingMod.enchantmentBattle));
		assertFalse("Cannot Apply with self", spellBasic.canApplyTogether(spellBasic));
	}
	
	@Test
	public void testCanApply()
	{
		ItemStack spellStack = new ItemStack(EnchantingMod.emerald_staff);
		ItemStack nonSpellStack = new ItemStack(EnchantingMod.clothBoots);
		assertTrue("Can apply to Staff",spellBasic.canApply(spellStack));
		assertFalse("Cannot apply to cloth boots",spellBasic.canApply(nonSpellStack));
		assertEquals("canApplyAtEnchantingTable should be same as canApply for spellStack", 
				spellBasic.canApply(spellStack), spellBasic.canApplyAtEnchantingTable(spellStack));
		assertEquals("canApplyAtEnchantingTable should be same as canApply for nonSpellStack", 
				spellBasic.canApply(nonSpellStack), spellBasic.canApplyAtEnchantingTable(nonSpellStack));
	}
	
	@Test
	public void testGetRarity()
	{
		assertEquals("Rarity should be COMMON",EnumRarity.COMMON,spellBasic.getRarity());
	}
	
	public static class SpellBasic extends Spell
	{
		public SpellBasic() {
			super(63, new ResourceLocation("testing","spell_basic"), EnumRarity.COMMON);
		}

		@Override
		public int getCastTime() {
			return 0;
		}

		@Override
		public int getColor() {
			return 0;
		}

		@Override
		public int getMaxEnchantability(int arg0) {
			return 0;
		}

		@Override
		public int getMinEnchantability(int arg0) {
			return 0;
		}

		@Override
		public String getModId() {
			return TestingMod.MODID;
		}

		@Override
		public String[] getRawDescription() {
			return new String[]{"spell.basic.test.desc.1","spell.basic.test.desc.2"};
		}

		@Override
		public Item getRender() {
			return null;
		}

		@Override
		public String getRenderName() {
			return "spell.basic.test.name";
		}

		@Override
		public boolean onUse(ItemStack arg0, World arg1, EntityPlayer arg2,
				int arg3) {
			return true;
		}
	}
}
