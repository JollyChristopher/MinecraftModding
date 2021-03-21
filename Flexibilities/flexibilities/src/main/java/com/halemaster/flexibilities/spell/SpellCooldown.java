package com.halemaster.flexibilities.spell;

/**
 * Created by Halemaster on 7/15/2017.
 */
public interface SpellCooldown {
    long getCooldownStart(String spellId);
    void setCooldownStart(String spellId, long cooldownStart);
    void removeCooldownStart(String spellId);
    String[] getCooldownSpells();
}
