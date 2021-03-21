package com.halemaster.flexibilities.spell;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.apache.commons.jexl3.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Halemaster on 7/14/2017.
 */
public class Spell {
    private static JexlEngine jexl = new JexlBuilder().create();

    private String id;
    private Map<String, String> name;
    private Model model;
    private Map<String, List<String>> description;
    private String color;
    private JexlExpression colorExpr;
    private String cooldown;
    private JexlExpression cooldownExpr;
    private String castTime;
    private JexlExpression castTimeExpr;
    private String[] duringCasting;
    private JexlExpression[] duringCastingExpr;
    private String[] onReady;
    private JexlExpression[] onReadyExpr;
    private Cost cost;
    private String[] commands;
    private JexlExpression[] commandsExpr;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<String, String> getName() {
        return name;
    }

    public void setName(Map<String, String> name) {
        this.name = name;
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public Map<String, List<String>> getDescription() {
        return description;
    }

    public void setDescription(Map<String, List<String>> description) {
        this.description = description;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getColorExpr(boolean isScroll) {
        JexlContext jc = new MapContext();
        jc.set("spell", this);
        jc.set("world", Minecraft.getMinecraft().world);
        jc.set("isScroll", isScroll);
        jc.set("Math", new MathHelper());

        if(colorExpr == null) {
            this.colorExpr = jexl.createExpression(color);
        }

        return ((Number) colorExpr.evaluate(jc)).intValue();
    }

    public String getCooldown() {
        return cooldown;
    }

    public void setCooldown(String cooldown) {
        this.cooldown = cooldown;
    }

    public int getCooldownExpr(World world, EntityLivingBase caster, boolean isScroll) {
        JexlContext jc = new MapContext();
        jc.set("spell", this);
        jc.set("world", world);
        jc.set("caster", caster);
        jc.set("isScroll", isScroll);
        jc.set("Math", new MathHelper());

        if(cooldownExpr == null) {
            this.cooldownExpr = jexl.createExpression(cooldown);
        }

        return ((Number) cooldownExpr.evaluate(jc)).intValue();
    }

    public String getCastTime() {
        return castTime;
    }

    public void setCastTime(String castTime) {
        this.castTime = castTime;
    }

    public int getCastTimeExpr(World world, EntityLivingBase caster, boolean isScroll) {
        JexlContext jc = new MapContext();
        jc.set("spell", this);
        jc.set("world", world);
        jc.set("caster", caster);
        jc.set("isScroll", isScroll);
        jc.set("Math", new MathHelper());

        if(castTimeExpr == null) {
            this.castTimeExpr = jexl.createExpression(castTime);
        }

        return ((Number) castTimeExpr.evaluate(jc)).intValue();
    }

    public String[] getDuringCasting() {
        return duringCasting;
    }

    public void setDuringCasting(String[] duringCasting) {
        this.duringCasting = duringCasting;
    }

    public String[] getDuringCastingCommands(World world, EntityLivingBase caster, boolean isScroll, boolean isReady,
                                             int tick) {
        JexlContext jc = new MapContext();
        jc.set("spell", this);
        jc.set("world", world);
        jc.set("caster", caster);
        jc.set("isScroll", isScroll);
        jc.set("isReady", isReady);
        jc.set("tick", tick);
        jc.set("Math", new MathHelper());

        if(duringCastingExpr == null) {
            this.duringCastingExpr = new JexlExpression[duringCasting.length];
            for(int i = 0; i < duringCastingExpr.length; i++) {
                duringCastingExpr[i] = jexl.createExpression(duringCasting[i]);
            }
        }

        return Arrays.stream(duringCastingExpr).map(expr -> (String) expr.evaluate(jc))
                .collect(Collectors.toList()).toArray(new String[duringCasting.length]);
    }

    public String[] getOnReady() {
        return onReady;
    }

    public void setOnReady(String[] onReady) {
        this.onReady = onReady;
    }

    public String[] getOnReadyCommands(World world, EntityLivingBase caster, boolean isScroll) {
        JexlContext jc = new MapContext();
        jc.set("spell", this);
        jc.set("world", world);
        jc.set("caster", caster);
        jc.set("isScroll", isScroll);
        jc.set("Math", new MathHelper());

        if(onReadyExpr == null) {
            this.onReadyExpr = new JexlExpression[onReady.length];
            for(int i = 0; i < onReadyExpr.length; i++) {
                onReadyExpr[i] = jexl.createExpression(onReady[i]);
            }
        }

        return Arrays.stream(onReadyExpr).map(expr -> (String) expr.evaluate(jc))
                .collect(Collectors.toList()).toArray(new String[onReady.length]);
    }

    public Cost getCost() {
        return cost;
    }

    public void setCost(Cost cost) {
        this.cost = cost;
    }

    public String[] getCommands() {
        return commands;
    }

    public void setCommands(String[] commands) {
        this.commands = commands;
    }

    public String[] getCommandsExprs(World world, EntityLivingBase caster, boolean isScroll, int castTime) {
        JexlContext jc = new MapContext();
        jc.set("spell", this);
        jc.set("world", world);
        jc.set("caster", caster);
        jc.set("isScroll", isScroll);
        jc.set("castTime", castTime);
        jc.set("Math", new MathHelper());

        if(commandsExpr == null) {
            this.commandsExpr = new JexlExpression[commands.length];
            for(int i = 0; i < commandsExpr.length; i++) {
                commandsExpr[i] = jexl.createExpression(commands[i]);
            }
        }

        return Arrays.stream(commandsExpr).map(expr -> (String) expr.evaluate(jc))
                .collect(Collectors.toList()).toArray(new String[commands.length]);
    }
}
