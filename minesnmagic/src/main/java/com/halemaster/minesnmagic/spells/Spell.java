/**
 * Created by Halemaster on 10/22/2016.
 */

package com.halemaster.minesnmagic.spells;

import java.util.*;
import java.util.stream.Stream;

public class Spell
{
    private List<SpellModifier> spellModifiers = new ArrayList<>();
    private Map<SpellCastType, List<Effect>> spellEffects = new HashMap<>();

    public boolean addModifier(SpellModifier modifier)
    {
        if(getSpellModifiers().anyMatch(mod -> mod.uniqueWith(modifier)))
        {
            return false;
        }
        spellModifiers.add(modifier);
        return true;
    }

    public boolean removeModifier(SpellModifier modifier)
    {
        Optional<SpellModifier> removeModifier = getSpellModifiers()
                .filter(mod -> mod.getClass().isInstance(modifier)).findAny();
        return removeModifier.isPresent() && spellModifiers.remove(removeModifier.get());
    }

    public Stream<SpellModifier> getSpellModifiers()
    {
        return spellModifiers.stream();
    }

    public boolean addEffect(Effect effect, SpellCastType castType)
    {
        List<Effect> effects = spellEffects.get(castType);
        if(null == effects)
        {
            effects = new ArrayList<>();
            spellEffects.put(castType, effects);
        }
        if(effects.stream().anyMatch(effect1 -> effect1.matches(effect)))
        {
            return false;
        }
        effects.add(effect);
        return true;
    }

    public boolean removeEffect(Effect effect, SpellCastType castType)
    {
        List<Effect> effects = spellEffects.get(castType);
        if(null == effects)
        {
            effects = new ArrayList<>();
            spellEffects.put(castType, effects);
        }
        Optional<Effect> removeEffect = effects.stream()
                .filter(effect1 -> effect1.matches(effect)).findAny();
        return removeEffect.isPresent() && effects.remove(removeEffect.get());
    }

    public Set<SpellCastType> getCastTypes()
    {
        return spellEffects.keySet();
    }

    public Stream<Effect> getEffects(SpellCastType castType)
    {
        List<Effect> effects = spellEffects.get(castType);
        if(null != effects)
        {
            return effects.stream();
        }
        return Stream.empty();
    }

    public class Effect
    {
        private List<SpellModifier> effectModifiers = new ArrayList<>();
        private List<SpellEffect> effects = new ArrayList<>();

        public boolean addModifier(SpellModifier modifier)
        {
            // todo: add modifier as unique
            return false;
        }

        public boolean removeModifier(SpellModifier modifier)
        {
            // todo: remove modifier of that type
            return false;
        }

        public Stream<SpellModifier> getModifiers()
        {
            return effectModifiers.stream();
        }

        public Stream<SpellModifier> getAllModifiers()
        {
            return Stream.concat(getModifiers(), getSpellModifiers());
        }

        public boolean addEffect(SpellEffect effect)
        {
            // todo: add effect
            return false;
        }

        public boolean removeEffect(SpellEffect effect)
        {
            // todo: find and remove effect
            return false;
        }

        public Stream<SpellEffect> getEffects()
        {
            return effects.stream();
        }

        public boolean matches(Effect other)
        {
            // todo: write this
            return false;
        }
    }
}
