package com.halemaster.minesnmagic.magic.wrappers;

import net.minecraft.util.math.Vec3d;

public class Vector {
    private Vec3d proxy;

    public Vector(Vec3d proxy) {
        this.proxy = proxy;
    }

    public double getX() {
        return proxy.x;
    }

    public double getY() {
        return proxy.y;
    }

    public double getZ() {
        return proxy.z;
    }

    // todo: do other methods here
}
