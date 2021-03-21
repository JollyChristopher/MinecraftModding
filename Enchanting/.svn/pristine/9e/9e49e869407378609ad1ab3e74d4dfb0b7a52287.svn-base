package com.halemaster.enchanting.spell;

import java.util.Iterator;
import java.util.List;

import com.halemaster.enchanting.EnchantingMod;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3i;

public class RenderSpellProjectile extends Render
{
	public static final Item DEFAULT = EnchantingMod.default_spell;

    private final RenderItem renderItem;

    public RenderSpellProjectile(RenderManager renderManager, RenderItem renderItem)
    {
        super(renderManager);
        this.renderItem = renderItem;
    }

    public void doRender(Entity entity, double x, double y, double z, float p_76986_8_, float partialTicks)
    {
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)x, (float)y, (float)z);
        GlStateManager.enableRescaleNormal();
        GlStateManager.scale(0.5F, 0.5F, 0.5F);
        GlStateManager.rotate(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
        this.bindTexture(TextureMap.locationBlocksTexture);
        this.renderItem.renderItemModel(this.getItemStack(entity));
        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();
        super.doRender(entity, x, y, z, p_76986_8_, partialTicks);
    }

    public ItemStack getItemStack(Entity entity)
    {
    	if (entity instanceof EntitySpellProjectile)
    	{
    		SpellProjectile spell = ((EntitySpellProjectile) entity).getSpell();
    		if (null != spell)
    		{
    			if (null != spell.getItem())
    			{
    				return new ItemStack(spell.getItem(), 1, 0);
    			}
    		}
    	}
        return new ItemStack(DEFAULT, 1, 0);
    }

    protected ResourceLocation getEntityTexture(Entity entity)
    {
        return TextureMap.locationBlocksTexture;
    }
}
