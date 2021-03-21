package com.halemaster.flexibilities.item;

import com.halemaster.flexibilities.FlexibilitiesMod;
import com.halemaster.flexibilities.spell.Spell;
import com.halemaster.flexibilities.spell.SpellCooldown;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

/**
 * Created by Halemaster on 7/11/2017.
 */
public class Scroll extends Item {
    public static final String NAME = "scroll";
    public static final int MAX_CAST = 72000;

    public Scroll() {
        setMaxStackSize(64);
        setUnlocalizedName(NAME);
        setMaxDamage(1);
        setNoRepair();
        setRegistryName(FlexibilitiesMod.MODID, NAME);
    }

    public static Optional<Spell> getSpell(ItemStack stack) {
        if(stack == null || stack.getTagCompound() == null) {
            return Optional.empty();
        }
        String id = stack.getTagCompound().getString("spell.id");
        if(id.isEmpty()) {
            return Optional.empty();
        }

        return Optional.ofNullable(FlexibilitiesMod.getSpellFromId(id));
    }

    public static int getColor(ItemStack stack, int renderPass) {
        if(renderPass > 0) {
            return 16777215;
        }
        else {
            return getSpell(stack).map(spell -> spell.getColorExpr(true)).orElse(16777215);
        }
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        return MAX_CAST;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        getSpell(stack).ifPresent(spell -> tooltip.addAll(spell.getDescription().get(Minecraft.getMinecraft()
                .getLanguageManager().getCurrentLanguage().getLanguageCode())));
    }

    @Override
    public EnumRarity getRarity(ItemStack stack) {
        return getSpell(stack).map(spell -> EnumRarity.RARE).orElse(EnumRarity.COMMON);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        ItemStack stack = playerIn.getHeldItem(handIn);
        Optional<Spell> spell = getSpell(stack);
        if(spell.isPresent()) {
            // use up scroll, and set cooldown
            final SpellCooldown cdCap = playerIn.getCapability(FlexibilitiesMod.SPELL_COOLDOWN_CAPABILITY, null);
            long cooldownStart = cdCap.getCooldownStart(spell.get().getId());
            boolean offCooldown = true;
            if(cooldownStart >= 0) {
                if(worldIn.getTotalWorldTime() - cooldownStart >=
                        spell.get().getCooldownExpr(worldIn, playerIn, true)) {
                    cdCap.removeCooldownStart(spell.get().getId());
                }
                else {
                    offCooldown = false;
                }
            }
            if(offCooldown) {
                playerIn.setActiveHand(handIn);
                return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
            }
        }
        return ActionResult.newResult(EnumActionResult.FAIL, stack);
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft) {
        Optional<Spell> spell = getSpell(stack);
        if(spell.isPresent() && entityLiving.getServer() != null) {
            if(MAX_CAST - timeLeft >= spell.get().getCastTimeExpr(worldIn, entityLiving, true)) {
                final SpellCooldown cdCap = entityLiving.getCapability(FlexibilitiesMod.SPELL_COOLDOWN_CAPABILITY,
                        null);
                cdCap.setCooldownStart(spell.get().getId(), worldIn.getTotalWorldTime());

                for (String command : spell.get().getCommandsExprs(worldIn, entityLiving,true,
                        MAX_CAST - timeLeft)) {
                    if(!command.isEmpty()) {
                        entityLiving.getServer().getCommandManager().executeCommand(entityLiving.getServer(), command);
                    }
                }
                // todo: use up item
            }
        }
    }

    @Override
    public void onUsingTick(ItemStack stack, EntityLivingBase player, int count) {
        Optional<Spell> spell = getSpell(stack);
        if(spell.isPresent()) {
            if(player.getServer() != null) {
                boolean isReady = MAX_CAST - count >= spell.get().getCastTimeExpr(player.world, player, true);
                for (String command : spell.get().getDuringCastingCommands(player.world, player,
                        true, isReady, count)) {
                    if(!command.isEmpty()) {
                        player.getServer().getCommandManager().executeCommand(player.getServer(), command);
                    }
                }

                if(MAX_CAST - count == spell.get().getCastTimeExpr(player.world, player, true)) {
                    for (String command : spell.get().getOnReadyCommands(player.world, player, true)) {
                        if(!command.isEmpty()) {
                            player.getServer().getCommandManager().executeCommand(player.getServer(), command);
                        }
                    }
                }
            }
        }
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.BOW;
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        return I18n.translateToLocalFormatted(this.getUnlocalizedNameInefficiently(stack) + ".name",
                getSpell(stack).map(spell -> spell.getName().get(Minecraft.getMinecraft().getLanguageManager()
                        .getCurrentLanguage().getLanguageCode()))
                        .orElse(I18n.translateToLocal("scroll.nothing"))).trim();
    }
}
