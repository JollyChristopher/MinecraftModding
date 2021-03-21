package com.halemaster.minesnmagic.magic.wrappers;

import net.minecraft.entity.EntityLivingBase;

public class EntityProxy {
    private EntityLivingBase proxyEntity;

    public EntityProxy(EntityLivingBase proxyEntity) {
        this.proxyEntity = proxyEntity;
    }

    public Vector getPositionVector() {
        return new Vector(proxyEntity.getPositionVector());
    }

    // todo: need to implement all of the methods
}
