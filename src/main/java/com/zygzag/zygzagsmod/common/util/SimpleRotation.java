package com.zygzag.zygzagsmod.common.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

@SuppressWarnings("unused")
public class SimpleRotation implements Rotation {
    public static Codec<SimpleRotation> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.FLOAT.fieldOf("xRot").forGetter((it) -> it.xRot),
                    Codec.FLOAT.fieldOf("yRot").forGetter((it) -> it.yRot)
            ).apply(instance, SimpleRotation::new)
    );
    // IMPORTANT: THIS IS IN RADIANS!!
    public float xRot, yRot;

    public SimpleRotation() { }

    public SimpleRotation(float xRot, float yRot) {
        this.xRot = xRot;
        this.yRot = yRot;
    }

    public SimpleRotation(SimpleRotation past, boolean resetRotations) {
        xRot = past.xRot;
        yRot = past.yRot;
    }

    public SimpleRotation(SimpleRotation past) {
        this(past, true);
    }

    @Override
    public void setXRot(float value) {
        xRot = value;
    }

    @Override
    public float getXRot() {
        return xRot;
    }

    @Override
    public void setYRot(float value) {
        yRot = value;
    }

    @Override
    public float getYRot() {
        return yRot;
    }
}
