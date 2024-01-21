package com.zygzag.zygzagsmod.common.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.world.phys.Vec3;

import static com.zygzag.zygzagsmod.common.util.GeneralUtil.degreesToRadians;
import static com.zygzag.zygzagsmod.common.util.GeneralUtil.radiansToDegrees;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

public interface Rotation {
    Codec<Rotation> LOSSY_CODEC = SimpleRotation.CODEC.flatComapMap(
            (it) -> it,
            (it) -> it instanceof SimpleRotation cast ? DataResult.success(cast) : DataResult.error(() -> "Unrecognized rotation type")
    );
    default void tick() { }
    void setXRot(float value);

    default float getXRot(float partialTick) {
        return getXRot();
    }

    float getXRot();

    void setYRot(float value);

    default float getYRot(float partialTick) {
        return getYRot();
    }

    float getYRot();

    default float[] get() {
        return new float[]{getXRot(), getYRot()};
    }
    default void set(float[] values) {
        setXRot(values[0]);
        setYRot(values[1]);
    }
    default void set(float xRot, float yRot) {
        setXRot(xRot);
        setYRot(yRot);
    }

    default void setXRotDegrees(float value) {
        setXRot(degreesToRadians(value));
    }

    default float getXRotDegrees(float partialTick) {
        return radiansToDegrees(getXRot(partialTick));
    }

    default float getXRotDegrees() {
        return radiansToDegrees(getXRot());
    }

    default void setYRotDegrees(float value) {
        setYRot(degreesToRadians(value));
    }

    default float getYRotDegrees(float partialTick) {
        return radiansToDegrees(getYRot(partialTick));
    }

    default float getYRotDegrees() {
        return radiansToDegrees(getYRot());
    }
    default Vec3 directionVector() {
        return new Vec3(sin(getYRot()) * cos(getXRot()), sin(getXRot()), cos(getYRot()) * cos(getXRot()));
    }
}
