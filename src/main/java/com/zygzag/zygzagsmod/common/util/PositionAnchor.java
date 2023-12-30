package com.zygzag.zygzagsmod.common.util;

import net.minecraft.world.phys.Vec3;

public interface PositionAnchor {
    public static PositionAnchor ZERO = new Constant(Vec3.ZERO);
    void tick();

    void setX(double xN);

    double getX();

    double getX(double partialTick);

    void setY(double yN);

    double getY();

    double getY(double partialTick);

    void setZ(double zN);

    double getZ();

    double getZ(double partialTick);

    default void set(Vec3 value) {
        setX(value.x());
        setY(value.y());
        setZ(value.z());
    }
    default Vec3 get() {
        return new Vec3(getX(), getY(), getZ());
    }
    default Vec3 get(double partialTick) {
        return new Vec3(getX(partialTick), getY(partialTick), getZ(partialTick));
    }

    record Constant(Vec3 position) implements PositionAnchor {
        @Override
        public void tick() { }

        @Override
        public void setX(double xN) { }

        @Override
        public double getX() {
            return position.x();
        }

        @Override
        public double getX(double partialTick) {
            return position.x();
        }

        @Override
        public void setY(double yN) { }

        @Override
        public double getY() {
            return position.y();
        }

        @Override
        public double getY(double partialTick) {
            return position.y();
        }

        @Override
        public void setZ(double zN) { }

        @Override
        public double getZ() {
            return position.z();
        }

        @Override
        public double getZ(double partialTick) {
            return position.z();
        }
    }
}
