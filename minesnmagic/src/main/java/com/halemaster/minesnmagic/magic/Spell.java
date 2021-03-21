package com.halemaster.minesnmagic.magic;

import com.halemaster.minesnmagic.magic.wrappers.EntityProxy;

public interface Spell {
    public String id();
    public String name(EntityProxy caster, Library library);
}
