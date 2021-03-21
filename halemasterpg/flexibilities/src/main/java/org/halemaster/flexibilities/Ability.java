package org.halemaster.flexibilities;

import ninja.leaping.configurate.ConfigurationNode;

import java.util.*;

public class Ability
{
    public static final String ABILITIES = "abilities";
    public static final String PROJECTILE = "projectile";
    public static final String PROJECTILE_ITEM = "item";
    public static final String AURA = "aura";
    public static final String AURA_RANGE = "range";
    public static final String AURA_PARTICLES = "particles";
    public static final String CONE = "cone";
    public static final String CONE_RANGE = "range";
    public static final String CONE_PARTICLES = "particles";
    public static final String EFFECT = "effect";
    public static final String EFFECT_LEVEL = "level";
    public static final String EFFECT_TIME = "time";
    public static final String COST = "cost";
    public static final String COST_HP = "hp";
    public static final String COST_HUNGER = "hunger";
    public static final String COST_ITEMS = "items";
    public static final String COST_ITEM_AMOUNT = "amount";
    public static final String MODEL = "model";
    public static final String MODEL_LORE = "lore";
    public static final String MODEL_ITEM = "item";
    public static final String TEAM = "team";
    private static Map<String, Ability> abilityCache = new HashMap<>();

    public static Optional<Ability> loadAbility(ConfigurationNode abilityNode, String name)
    {
        if(abilityCache.containsKey(name))
        {
            return Optional.of(abilityCache.get(name));
        }

        if(abilityNode.getNode(ABILITIES).getChildrenMap().containsKey(name))
        {
            Ability ability = new Ability();
            ConfigurationNode specificAbility = abilityNode.getNode(ABILITIES, name);
            ability.setName(name);
            if(specificAbility.getChildrenMap().containsKey(PROJECTILE))
            {
                ability.setProjectileItemId(specificAbility.getNode(PROJECTILE, PROJECTILE_ITEM).getString());
            }
            if(specificAbility.getChildrenMap().containsKey(AURA))
            {
                ability.setAuraParticleId(specificAbility.getNode(AURA, AURA_PARTICLES).getString());
                ability.setAuraRange(specificAbility.getNode(AURA, AURA_RANGE).getInt());
            }
            if(specificAbility.getChildrenMap().containsKey(CONE))
            {
                ability.setConeParticleId(specificAbility.getNode(CONE, CONE_PARTICLES).getString());
                ability.setConeRange(specificAbility.getNode(CONE, CONE_RANGE).getInt());
            }
            if(specificAbility.getChildrenMap().containsKey(EFFECT))
            {
                List<Effect> effects = new ArrayList<>();
                specificAbility.getNode(EFFECT).getChildrenMap().entrySet().stream().forEach(entry -> {
                    Effect effect = new Effect();
                    effect.setEffectId((Integer) entry.getKey());
                    effect.setLevel(entry.getValue().getNode(EFFECT_LEVEL).getInt());
                    effect.setTime(entry.getValue().getNode(EFFECT_TIME).getInt());
                    effects.add(effect);
                });
                ability.setEffects(effects.toArray(new Effect[effects.size()]));
            }
            if(specificAbility.getNode(COST).getChildrenMap().containsKey(COST_HP))
            {
                ability.setHpCost(specificAbility.getNode(COST, COST_HP).getInt());
            }
            if(specificAbility.getNode(COST).getChildrenMap().containsKey(COST_HUNGER))
            {
                ability.setHungerCost(specificAbility.getNode(COST, COST_HUNGER).getInt());
            }
            if(specificAbility.getNode(COST).getChildrenMap().containsKey(COST_ITEMS))
            {
                List<ItemCost> itemCosts = new ArrayList<>();
                specificAbility.getNode(COST, COST_ITEMS).getChildrenMap().entrySet().stream().forEach(entry -> {
                    ItemCost itemCost = new ItemCost();
                    itemCost.setItemId((String) entry.getKey());
                    itemCost.setAmount(entry.getValue().getNode(COST_ITEM_AMOUNT).getInt());
                    itemCosts.add(itemCost);
                });
                ability.setItemCosts(itemCosts.toArray(new ItemCost[itemCosts.size()]));
            }
            if(specificAbility.getChildrenMap().containsKey(TEAM))
            {
                ability.setTeam(specificAbility.getNode(TEAM).getString());
            }
            if(specificAbility.getNode(MODEL).getChildrenMap().containsKey(MODEL_LORE))
            {
                ability.setLore(specificAbility.getNode(MODEL, MODEL_LORE).getString());
            }
            if(specificAbility.getNode(MODEL).getChildrenMap().containsKey(MODEL_ITEM))
            {
                ability.setItemId(specificAbility.getNode(MODEL, MODEL_ITEM).getString());
            }
            abilityCache.put(name, ability);
            return Optional.of(ability);
        }
        else
        {
            return Optional.empty();
        }
    }

    private String name;
    private String projectileItemId;
    private Integer auraRange;
    private String auraParticleId;
    private Integer coneRange;
    private String coneParticleId;
    private Effect[] effects;
    private Integer hpCost;
    private Integer hungerCost;
    private ItemCost[] itemCosts;
    private String lore;
    private String itemId;
    private String team;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Optional<String> getProjectileItemId()
    {
        return Optional.of(projectileItemId);
    }

    public void setProjectileItemId(String projectileItemId)
    {
        this.projectileItemId = projectileItemId;
    }

    public Optional<Integer> getAuraRange()
    {
        return Optional.of(auraRange);
    }

    public void setAuraRange(int auraRange)
    {
        this.auraRange = auraRange;
    }

    public Optional<String> getAuraParticleId()
    {
        return Optional.of(auraParticleId);
    }

    public void setAuraParticleId(String auraParticleId)
    {
        this.auraParticleId = auraParticleId;
    }

    public Optional<Integer> getConeRange()
    {
        return Optional.of(coneRange);
    }

    public void setConeRange(int coneRange)
    {
        this.coneRange = coneRange;
    }

    public Optional<String> getConeParticleId()
    {
        return Optional.of(coneParticleId);
    }

    public void setConeParticleId(String coneParticleId)
    {
        this.coneParticleId = coneParticleId;
    }

    public Optional<Effect[]> getEffects()
    {
        return Optional.of(effects);
    }

    public void setEffects(Effect[] effects)
    {
        this.effects = effects;
    }

    public Optional<Integer> getHpCost()
    {
        return Optional.of(hpCost);
    }

    public void setHpCost(int hpCost)
    {
        this.hpCost = hpCost;
    }

    public Optional<Integer> getHungerCost()
    {
        return Optional.of(hungerCost);
    }

    public void setHungerCost(int hungerCost)
    {
        this.hungerCost = hungerCost;
    }

    public Optional<ItemCost[]> getItemCosts()
    {
        return Optional.of(itemCosts);
    }

    public void setItemCosts(ItemCost[] itemCosts)
    {
        this.itemCosts = itemCosts;
    }

    public Optional<String> getLore()
    {
        return Optional.of(lore);
    }

    public void setLore(String lore)
    {
        this.lore = lore;
    }

    public Optional<String> getItemId()
    {
        return Optional.of(itemId);
    }

    public void setItemId(String itemId)
    {
        this.itemId = itemId;
    }

    public Optional<String> getTeam()
    {
        return Optional.of(team);
    }

    public void setTeam(String team)
    {
        this.team = team;
    }
}
