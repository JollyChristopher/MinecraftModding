package com.halemaster.flexibilities.command;

import net.minecraft.command.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

public class CommandDamage extends CommandBase
{
    @Override
    public String getName()
    {
        return "damage";
    }

    @Override
    public int getRequiredPermissionLevel()
    {
        return 2;
    }

    @Override
    public String getUsage(ICommandSender sender)
    {
        return "commands.damage.usage";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length < 3)
        {
            throw new WrongUsageException("commands.damage.usage", new Object[0]);
        }
        else
        {
            List<Entity> entities = getEntityList(server, sender, args[0]);
            int damage = Integer.valueOf(args[1]);
            Entity sourceEntity = null;
            Entity indirectEntity = null;
            DamageSource source = null;
            if (args.length > 3) {
                sourceEntity = getEntity(server, sender, args[3]);
            }
            if (args.length > 4) {
                indirectEntity = getEntity(server, sender, args[4]);
            }
            if(args.length > 2) {
                source = getDamageSource(args[2], sourceEntity, indirectEntity);
            }

            for(Entity entity : entities) {
                if (entity instanceof EntityLivingBase) {
                    EntityLivingBase entityLiving = (EntityLivingBase) entity;
                    if (damage < 0) {
                        entityLiving.heal(-damage);
                    } else if(source != null){
                        entityLiving.attackEntityFrom(source, damage);
                    }
                }
            }
        }
    }

    public static Map<String, BiFunction<Entity, Entity, DamageSource>> sourceMap = new HashMap<>();

    static {
        sourceMap.put(DamageSource.IN_FIRE.getDamageType().toLowerCase(), (source, indirect) -> DamageSource.IN_FIRE);
        sourceMap.put(DamageSource.LIGHTNING_BOLT.getDamageType().toLowerCase(), (source, indirect) -> DamageSource.LIGHTNING_BOLT);
        sourceMap.put(DamageSource.ON_FIRE.getDamageType().toLowerCase(), (source, indirect) -> DamageSource.ON_FIRE);
        sourceMap.put(DamageSource.LAVA.getDamageType().toLowerCase(), (source, indirect) -> DamageSource.LAVA);
        sourceMap.put(DamageSource.HOT_FLOOR.getDamageType().toLowerCase(), (source, indirect) -> DamageSource.HOT_FLOOR);
        sourceMap.put(DamageSource.IN_WALL.getDamageType().toLowerCase(), (source, indirect) -> DamageSource.IN_WALL);
        sourceMap.put(DamageSource.CRAMMING.getDamageType().toLowerCase(), (source, indirect) -> DamageSource.CRAMMING);
        sourceMap.put(DamageSource.DROWN.getDamageType().toLowerCase(), (source, indirect) -> DamageSource.DROWN);
        sourceMap.put(DamageSource.STARVE.getDamageType().toLowerCase(), (source, indirect) -> DamageSource.STARVE);
        sourceMap.put(DamageSource.CACTUS.getDamageType().toLowerCase(), (source, indirect) -> DamageSource.CACTUS);
        sourceMap.put(DamageSource.FALL.getDamageType().toLowerCase(), (source, indirect) -> DamageSource.FALL);
        sourceMap.put(DamageSource.FLY_INTO_WALL.getDamageType().toLowerCase(), (source, indirect) -> DamageSource.FLY_INTO_WALL);
        sourceMap.put(DamageSource.OUT_OF_WORLD.getDamageType().toLowerCase(), (source, indirect) -> DamageSource.OUT_OF_WORLD);
        sourceMap.put(DamageSource.GENERIC.getDamageType().toLowerCase(), (source, indirect) -> DamageSource.GENERIC);
        sourceMap.put(DamageSource.MAGIC.getDamageType().toLowerCase(), (source, indirect) -> DamageSource.MAGIC);
        sourceMap.put(DamageSource.WITHER.getDamageType().toLowerCase(), (source, indirect) -> DamageSource.WITHER);
        sourceMap.put(DamageSource.ANVIL.getDamageType().toLowerCase(), (source, indirect) -> DamageSource.ANVIL);
        sourceMap.put(DamageSource.FALLING_BLOCK.getDamageType().toLowerCase(), (source, indirect) -> DamageSource.FALLING_BLOCK);
        sourceMap.put(DamageSource.DRAGON_BREATH.getDamageType().toLowerCase(), (source, indirect) -> DamageSource.DRAGON_BREATH);
        sourceMap.put(DamageSource.FIREWORKS.getDamageType().toLowerCase(), (source, indirect) -> DamageSource.FIREWORKS);
        sourceMap.put("mob", (source, indirect) -> DamageSource.causeMobDamage((EntityLivingBase) source));
        sourceMap.put("indirect", (source, indirect) -> DamageSource.causeIndirectDamage(source, (EntityLivingBase) indirect));
        sourceMap.put("player", (source, indirect) -> DamageSource.causePlayerDamage((EntityPlayer) source));
        sourceMap.put("arrow", (source, indirect) -> DamageSource.causeArrowDamage((EntityArrow) source, indirect));
        sourceMap.put("fireball", (source, indirect) -> DamageSource.causeFireballDamage((EntityFireball) source, indirect));
        sourceMap.put("thrown", DamageSource::causeThrownDamage);
        sourceMap.put("indirectmagic", DamageSource::causeIndirectMagicDamage);
        sourceMap.put("thorns", (source, indirect) -> DamageSource.causeThornsDamage(source));
        sourceMap.put("explosion", (source, indirect) -> DamageSource.causeExplosionDamage((EntityLivingBase) source));
    }

    public static DamageSource getDamageSource(String source, Entity sourceEntity, Entity indirectEntity) {
        return sourceMap.get(source.toLowerCase()).apply(sourceEntity, indirectEntity);
    }
}
