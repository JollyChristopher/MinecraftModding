package com.halemaster.minesnmagic.magic;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Library {
    private Map<String, Spell> spells = new HashMap<>();

    private <T> T createInterface(String path, Class<T> clazz) throws FileNotFoundException, ScriptException {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("JavaScript");

        if (!(engine instanceof Invocable)) {
            System.out.println("Interface implementation in script is not supported.");
            return null;
        }
        Invocable inv = (Invocable) engine;
        engine.eval(new FileReader(path));
        T inter = inv.getInterface(clazz);
        if (inter == null) {
            System.err.println("Interface not implemented"); // todo: do usage
            return null;
        }
        return inter;
    }

    public void loadSpells(File path) {
        File[] spellFiles = path.listFiles();
        if(null != spellFiles) {
            for(File spellFile : spellFiles) {
                try {
                    registerSpell(spellFile.getAbsolutePath());
                } catch (ScriptException | FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void registerSpell(String path) throws ScriptException, FileNotFoundException {
        Spell spell = createInterface(path, Spell.class);
        System.out.println("Spell Loaded: " + spell.id());
        spells.put(spell.id(), spell);
    }

    public Spell getSpell(String id) {
        return spells.get(id);
    }
}
