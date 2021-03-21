package com.halemaster.flexibilities.item;

import com.halemaster.flexibilities.FlexibilitiesMod;
import com.halemaster.flexibilities.spell.Spell;
import com.halemaster.flexibilities.spell.SpellCooldown;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Optional;

import static com.halemaster.flexibilities.item.Scroll.getSpell;

/**
 * Created by Halemaster on 7/15/2017.
 */
public class FlexibilitiesRenderItem extends RenderItem {
    private RenderItem superRenderer;

    public FlexibilitiesRenderItem(RenderItem superRenderer, TextureManager textureManager, ModelManager modelManager,
                                   ItemColors itemColors) {
        super(textureManager, modelManager, itemColors);
        this.superRenderer = superRenderer;
    }

    @Override
    public void renderItemOverlays(FontRenderer fr, ItemStack stack, int xPosition, int yPosition)
    {
        this.renderItemOverlayIntoGUI(fr, stack, xPosition, yPosition, (String)null);
    }

    @Override
    public void renderItemOverlayIntoGUI(FontRenderer fr, ItemStack stack, int xPosition, int yPosition,
                                         @Nullable String text) {
        superRenderer.renderItemOverlayIntoGUI(fr, stack, xPosition, yPosition, text);
        Optional<Spell> spell = getSpell(stack);
        if(!stack.isEmpty() && spell.isPresent()) {
                // use up scroll, and set cooldown
            EntityPlayer entityplayersp = Minecraft.getMinecraft().player;
            if(entityplayersp != null) {
                final SpellCooldown cdCap = entityplayersp.getCapability(FlexibilitiesMod.SPELL_COOLDOWN_CAPABILITY,
                        null);
                if (null != cdCap) {
                    long cooldownStart = cdCap.getCooldownStart(spell.get().getId());
                    if (cooldownStart >= 0) {
                        float f3 = (cooldownStart + spell.get().getCooldownExpr(entityplayersp.world, entityplayersp,
                                true) - entityplayersp.world.getTotalWorldTime()) /
                                ((Integer) spell.get().getCooldownExpr(entityplayersp.world, entityplayersp,
                                        true)).floatValue();

                        f3 = f3 > 1.0f ? 1.0f : f3;
                        if (f3 > 0.0F) {
                            GlStateManager.disableLighting();
                            GlStateManager.disableDepth();
                            GlStateManager.disableTexture2D();
                            Tessellator tessellator1 = Tessellator.getInstance();
                            BufferBuilder bufferbuilder1 = tessellator1.getBuffer();
                            draw(bufferbuilder1, xPosition, yPosition + MathHelper.floor(16.0F * (1.0F - f3)),
                                    16, MathHelper.ceil(16.0F * f3), 255, 255, 255, 127);
                            GlStateManager.enableTexture2D();
                            GlStateManager.enableLighting();
                            GlStateManager.enableDepth();
                        }
                    }
                }
            }
        }
    }

    private void draw(BufferBuilder renderer, int x, int y, int width, int height, int red, int green, int blue, int alpha)
    {
        renderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        renderer.pos((double)(x + 0), (double)(y + 0), 0.0D).color(red, green, blue, alpha).endVertex();
        renderer.pos((double)(x + 0), (double)(y + height), 0.0D).color(red, green, blue, alpha).endVertex();
        renderer.pos((double)(x + width), (double)(y + height), 0.0D).color(red, green, blue, alpha).endVertex();
        renderer.pos((double)(x + width), (double)(y + 0), 0.0D).color(red, green, blue, alpha).endVertex();
        Tessellator.getInstance().draw();
    }

    @Override
    public ItemModelMesher getItemModelMesher()
    {
        return superRenderer.getItemModelMesher();
    }

    @Override
    public void renderItem(ItemStack stack, IBakedModel model)
    {
        superRenderer.renderItem(stack, model);
    }

    @Override
    public boolean shouldRenderItemIn3D(ItemStack stack)
    {
        return superRenderer.shouldRenderItemIn3D(stack);
    }

    @Override
    public void renderItem(ItemStack stack, ItemCameraTransforms.TransformType cameraTransformType)
    {
        superRenderer.renderItem(stack, cameraTransformType);
    }

    @Override
    public IBakedModel getItemModelWithOverrides(ItemStack stack, @Nullable World worldIn,
                                                 @Nullable EntityLivingBase entitylivingbaseIn)
    {
        return superRenderer.getItemModelWithOverrides(stack, worldIn, entitylivingbaseIn);
    }

    @Override
    public void renderItem(ItemStack stack, EntityLivingBase entitylivingbaseIn,
                           ItemCameraTransforms.TransformType transform, boolean leftHanded)
    {
        superRenderer.renderItem(stack, entitylivingbaseIn, transform, leftHanded);
    }

    @Override
    public void renderItemIntoGUI(ItemStack stack, int x, int y)
    {
        superRenderer.renderItemIntoGUI(stack, x, y);
    }

    @Override
    public void renderItemAndEffectIntoGUI(ItemStack stack, int xPosition, int yPosition)
    {
        superRenderer.renderItemAndEffectIntoGUI(stack, xPosition, yPosition);
    }

    @Override
    public void renderItemAndEffectIntoGUI(@Nullable EntityLivingBase p_184391_1_, final ItemStack p_184391_2_,
                                           int p_184391_3_, int p_184391_4_)
    {
        superRenderer.renderItemAndEffectIntoGUI(p_184391_1_, p_184391_2_, p_184391_3_, p_184391_4_);
    }

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager)
    {
        superRenderer.onResourceManagerReload(resourceManager);
    }
}
