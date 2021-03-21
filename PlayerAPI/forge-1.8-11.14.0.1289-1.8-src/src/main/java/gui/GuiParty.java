package gui;

import java.awt.Color;
import java.util.Collection;
import java.util.Iterator;
import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import org.lwjgl.opengl.GL11;

import com.halemaster.party.Party;
import com.halemaster.party.PartyAPIMod;

public class GuiParty extends GuiScreen
{
    public static final int GUI_ID = 20;
    private Minecraft mc;
    
    private static final int HEIGHT = 32;
    private static final int BORDER_SIZE = 4;

	public GuiParty(Minecraft mc)
	{
		super();
		this.mc = mc;
	}
	
	@SubscribeEvent(priority = EventPriority.NORMAL)
	public void onRenderExperienceBar(RenderGameOverlayEvent event)
	{
		if (event.isCancelable() || event.type != ElementType.EXPERIENCE)
		{
			return;
		}
		
		int yPos = 0;
		int xPos = 0;
		Party party = PartyAPIMod.getParty(mc.thePlayer, FMLCommonHandler.instance().getEffectiveSide());
		
		if (null == party)
		{
			party = new Party();
			party.join(mc.thePlayer, FMLCommonHandler.instance().getEffectiveSide());
		}
		
		for (Entity member : party.getMembers())
		{
			if (member instanceof EntityPlayer)
			{
				//MinecraftServer server = FMLClientHandler.instance().getServer();
				//member = server.getConfigurationManager().getPlayerByUsername(member.getName());
			}
			
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glDisable(GL11.GL_LIGHTING);
			this.mc.renderEngine.bindTexture(new ResourceLocation("textures/gui/container/inventory.png"));
			this.drawTexturedModalRect(xPos, yPos, 0, 166, 120, HEIGHT);
			drawMember(member, xPos, yPos);
			
			yPos += HEIGHT;
		}
	}
	
	private void drawMember(Entity member, int xPos, int yPos) 
	{
		if (member == null) 
		{
			return;
		}

		String name = member.getName();
		
		drawMemberHealth(member, xPos, yPos);
		//drawMemberEffects(member, xPos, yPos);
		this.drawString(mc.fontRendererObj, name, xPos + BORDER_SIZE, yPos + BORDER_SIZE, 
				Color.WHITE.getRGB());
	}
	
	private void drawMemberHealth(Entity member, int xPos, int yPos)
	{
		if (!(member instanceof EntityLivingBase))
		{
			return;
		}
		
		this.mc.getTextureManager().bindTexture(icons);
		
		EntityLivingBase memberLiving = (EntityLivingBase) member;
		ScaledResolution res = new ScaledResolution(mc, mc.displayHeight, mc.displayWidth);
		IAttributeInstance iattributeinstance = memberLiving.getEntityAttribute(SharedMonsterAttributes.maxHealth);
		Random rand = new Random();
        int health = MathHelper.ceiling_float_int(memberLiving.getHealth());
		float maxHealth = (float)iattributeinstance.getAttributeValue();
		float absorption = memberLiving.getAbsorptionAmount();
		int heartType;
		int currentBar;
        int xHeart;
        int yHeart;
        int maxHPBars = MathHelper.ceiling_float_int((maxHealth + absorption) / 2.0F / 10.0F);
        int healthBars = 2;
        float secondaryAbsorption = absorption;
		
        for (int HPRender = MathHelper.ceiling_float_int((maxHealth + absorption) / 2.0F) - 1; HPRender >= 0; --HPRender)
        {
        	heartType = 16;

            currentBar = MathHelper.ceiling_float_int((float)(HPRender + 1) / 10.0F) - 1;
            xHeart = xPos + BORDER_SIZE + HPRender % 10 * 8;
            yHeart = yPos + HEIGHT - 10 - BORDER_SIZE - currentBar * healthBars;

            byte hardcore = 0;

            if (memberLiving.worldObj.getWorldInfo().isHardcoreModeEnabled())
            {
                hardcore = 5;
            }

            this.drawTexturedModalRect(xHeart, yHeart, 16 + 0 * 9, 9 * hardcore, 9, 9);

            if (secondaryAbsorption > 0.0F)
            {
                if (secondaryAbsorption == absorption && absorption % 2.0F == 1.0F)
                {
                    this.drawTexturedModalRect(xHeart, yHeart, heartType + 153, 9 * hardcore, 9, 9);
                }
                else
                {
                    this.drawTexturedModalRect(xHeart, yHeart, heartType + 144, 9 * hardcore, 9, 9);
                }

                secondaryAbsorption -= 2.0F;
            }
            else
            {
                if (HPRender * 2 + 1 < health)
                {
                    this.drawTexturedModalRect(xHeart, yHeart, heartType + 36, 9 * hardcore, 9, 9);
                }

                if (HPRender * 2 + 1 == health)
                {
                    this.drawTexturedModalRect(xHeart, yHeart, heartType + 45, 9 * hardcore, 9, 9);
                }
            }
        }
	}
	
	private static final int BUFF_ICON_SIZE = 18;
	private static final int BUFF_ICON_BASE_U_OFFSET = 0;
	private static final int BUFF_ICON_BASE_V_OFFSET = 198;
	private static final int BUFF_ICONS_PER_ROW = 8;
	private static final int ICON_PER_ROW = 3;
	private static final int ROWS = 2;
	
	private void drawMemberEffects(Entity member, int xPos, int yPos)
	{
		if (!(member instanceof EntityLivingBase))
		{
			return;
		}
		
		EntityLivingBase memberLiving = (EntityLivingBase) member;
		
		xPos += BORDER_SIZE * 2 + 80;
		yPos += BORDER_SIZE;
		int index = 0;
		Collection collection = memberLiving.getActivePotionEffects();
		if (!collection.isEmpty())
		{
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glScalef(0.5f, 0.5f, 0.5f);
			GL11.glDisable(GL11.GL_LIGHTING);
			this.mc.renderEngine.bindTexture(new ResourceLocation("textures/gui/container/inventory.png"));

			for (Iterator iterator = collection.iterator(); iterator.hasNext(); )
			{
				PotionEffect potioneffect = (PotionEffect) iterator.next();
				Potion potion = Potion.potionTypes[potioneffect.getPotionID()];

				if (potion.hasStatusIcon())
				{
					int iconIndex = potion.getStatusIconIndex();
					this.drawTexturedModalRect((xPos + (index % ICON_PER_ROW * BUFF_ICON_SIZE / 2))*2, (yPos + 
							(index / ICON_PER_ROW * BUFF_ICON_SIZE / 2))*2, BUFF_ICON_BASE_U_OFFSET + 
							iconIndex % BUFF_ICONS_PER_ROW * BUFF_ICON_SIZE,
							BUFF_ICON_BASE_V_OFFSET + iconIndex / BUFF_ICONS_PER_ROW * BUFF_ICON_SIZE, BUFF_ICON_SIZE,
							BUFF_ICON_SIZE);
					index++;
					if (index > ICON_PER_ROW*ROWS - 1)
					{
						break;
					}
				}
			}
			GL11.glScalef(2f, 2f, 2f);
		}
	}
}
