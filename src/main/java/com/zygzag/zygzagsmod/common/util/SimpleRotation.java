package com.zygzag.zygzagsmod.common.util;

@SuppressWarnings("unused")
public class SimpleRotation implements Rotation {
//    public static Codec<SimpleRotation> CODEC = RecordCodecBuilder.create(instance ->
//            instance.group(
//                    Codec.FLOAT.fieldOf("xRot").forGetter((it) -> it.xRot),
//                    Codec.FLOAT.fieldOf("yRot").forGetter((it) -> it.yRot)
//            ).apply(instance, SimpleRotation::new)
//    );
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

    public SimpleRotation(float[] values) {
        this(values[0], values[1]);
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
    public String toString() {
        return "(x-rot %.4f, y-rot %.4f)".formatted(getXRot(), getYRot());
    }
}
