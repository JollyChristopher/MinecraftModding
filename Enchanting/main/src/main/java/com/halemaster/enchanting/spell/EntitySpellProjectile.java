package com.halemaster.enchanting.spell;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntitySpellProjectile extends EntityThrowable
{
	public static final int SPELL_ID = 31;
	public static final int LEVEL_ID = 30;
	public static final int NOT_SET = -1;
	
	public EntitySpellProjectile(World worldIn)
	{
		super(worldIn);
	}
	
    public EntitySpellProjectile(World worldIn, SpellProjectile spell, int level)
    {
        super(worldIn);
        setSpell(spell);
        setLevel(level);
    }

    public EntitySpellProjectile(World worldIn, EntityLivingBase shooter, SpellProjectile spell, int level)
    {
        super(worldIn, shooter);
        setSpell(spell);
        setLevel(level);
    }

    public EntitySpellProjectile(World worldIn, double x, double y, double z, SpellProjectile spell, int level)
    {
        super(worldIn, x, y, z);
        setSpell(spell);
        setLevel(level);
    }

    protected void entityInit()
    {
    	this.dataWatcher.addObject(SPELL_ID, NOT_SET);
    	this.dataWatcher.addObject(LEVEL_ID, NOT_SET);
    }
    
    public void setLevel(int level)
    {
    	this.dataWatcher.updateObject(LEVEL_ID, level);
    }
    
    public int getLevel()
    {
    	return this.dataWatcher.getWatchableObjectInt(LEVEL_ID);
    }
    
    public void setSpell(SpellProjectile spell)
    {
    	this.dataWatcher.updateObject(SPELL_ID, spell.effectId);
    }
    
    public SpellProjectile getSpell()
    {
    	int id = this.dataWatcher.getWatchableObjectInt(SPELL_ID);
    	if (NOT_SET != id)
    	{
    		Enchantment enchant = Enchantment.getEnchantmentById(id);
    		if (enchant instanceof SpellProjectile)
    		{
    			return (SpellProjectile) enchant;
    		}
    	}
    	return null;
    }

    protected void onImpact(MovingObjectPosition movingObject)
    {
        if (!this.worldObj.isRemote)
        {
        	if (getSpell() != null)
        	{
        		getSpell().onImpact(worldObj, this, getThrower(), movingObject, getLevel());
        	}

            this.setDead();
        }
    }
    
    @Override
    public void onUpdate()
    {
    	super.onUpdate();
    	if (getSpell() != null)
    	{
    		getSpell().onUpdate(worldObj, this, getLevel());
    	}
    }
}
