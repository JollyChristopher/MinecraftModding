/**
 * Created by Halemaster on 10/18/2016.
 */

package com.halemaster.minesnmagic.items;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public abstract class ItemSpell extends Item
{
    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List tooltip, boolean advanced)
    {
        // todo: put info into tooltip
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack stack)
    {
        // todo: return item has spell
        return stack.isItemEnchanted();
    }

    @Override
    public EnumRarity getRarity(ItemStack stack)
    {
        // todo: figure out rarity based upon spell set
        return EnumRarity.COMMON;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand)
    {
        //todo: figure out what to do here
        return new ActionResult(EnumActionResult.PASS, itemStackIn);
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft)
    {
        // todo: determines what happens when spell is done casting
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity)
    {
        // todo: determine what happens when left clicking entities
        return false;
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer playerIn, EntityLivingBase target, EnumHand hand)
    {
        // todo: determine what happens when using on entities with spell
        return false;
    }

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase entityLiving)
    {
        // todo: determine what happens when spells destroy blocks
        return false;
    }

    @Override
    public boolean canHarvestBlock(IBlockState blockIn)
    {
        // todo: determine what happens when spells break blocks
        return false;
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
    {
        // todo: determine what happens when spell is in inventory
    }

    @Override
    public void onCreated(ItemStack stack, World worldIn, EntityPlayer playerIn)
    {
        // todo: determine what happens when spell is created
    }

    @Override
    public String getHighlightTip( ItemStack item, String displayName )
    {
        // todo: determine spell name and append here
        return displayName;
    }

    @Override
    public void onUsingTick(ItemStack stack, EntityLivingBase player, int count)
    {
        // todo: determine what happens while using the spell
    }

    @Override
    public int getEntityLifespan(ItemStack itemStack, World world)
    {
        // todo: determine if spell has custom lifespan
        return 6000;
    }

    @Override
    public boolean hasCustomEntity(ItemStack stack)
    {
        // todo: determine if spell has custom entity
        return false;
    }

    @Override
    public Entity createEntity(World world, Entity location, ItemStack itemstack)
    {
        // todo: create custom entity if spell calls for it
        return null;
    }

    @Override
    public boolean onEntityItemUpdate(net.minecraft.entity.item.EntityItem entityItem)
    {
        // todo: do custom entity updates here for spells
        return false;
    }

    @Override
    public boolean isItemTool(ItemStack stack)
    {
        return stack.getItem() instanceof ItemSpell;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack)
    {
        return EnumAction.BOW;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack)
    {
        return 9999;
    }
}
