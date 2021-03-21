package com.halemaster.minesnmagic.magic;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

import java.util.concurrent.Callable;

public class SpellBook {
    private String currentCast = null;
    private Long startTime = null;

    public boolean startCasting(String spell) {
        if(currentCast == null) {
            return false;
        }
        currentCast = spell;
        startTime = System.nanoTime();
        return true;
    }

    public boolean stopCasting() {
        if(currentCast != null) {
            return false;
        }
        currentCast = null;
        startTime = null;
        return true;
    }

    public String getCurrentCast() {
        return currentCast;
    }

    public Long getStartTime() {
        return startTime;
    }

    public static class Storage implements Capability.IStorage<SpellBook> {

        @Override
        public NBTBase writeNBT(Capability<SpellBook> capability, SpellBook instance, EnumFacing side) {
            // nothing to return just yet
            return null;
        }

        @Override
        public void readNBT(Capability<SpellBook> capability, SpellBook instance, EnumFacing side, NBTBase nbt) {
            // load from the NBT tag
        }
    }

    public static class Factory implements Callable<SpellBook> {

        @Override
        public SpellBook call() throws Exception {
            return new SpellBook();
        }
    }
}
