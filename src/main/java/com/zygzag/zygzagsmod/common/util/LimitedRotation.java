package com.zygzag.zygzagsmod.common.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.Arrays;

public class LimitedRotation extends LerpedRotation {
    public static Codec<LimitedRotation> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.FLOAT.fieldOf("xRot").forGetter((it) -> it.xRot),
                    Codec.FLOAT.fieldOf("yRot").forGetter((it) -> it.yRot),
                    Codec.FLOAT.fieldOf("oldXRot").forGetter((it) -> it.oldXRot),
                    Codec.FLOAT.fieldOf("oldYRot").forGetter((it) -> it.oldYRot),
                    Codec.FLOAT.fieldOf("maxRotationPerTick").forGetter((it) -> it.maxRotationPerTick)
            ).apply(instance, LimitedRotation::new)
    );

    public float maxRotationPerTick;

    public LimitedRotation(float xRot, float yRot, float oldXRot, float oldYRot, float maxRotationPerTick) {
        super(xRot, yRot);
        this.oldXRot = oldXRot;
        this.oldYRot = oldYRot;
        this.maxRotationPerTick = maxRotationPerTick;
    }

    public LimitedRotation(LimitedRotation past) {
        this(past, past.maxRotationPerTick);
    }

    public LimitedRotation(LerpedRotation past, float maxRotationPerTick) {
        super(past);
        oldXRot = past.oldXRot;
        oldYRot = past.oldYRot;
        this.maxRotationPerTick = maxRotationPerTick;
    }

    @Override
    public void set(float xRot, float yRot) {
        if (!xSetThisTick) {
            xSetThisTick = true;
            oldXRot = this.xRot;
        }
        if (!ySetThisTick) {
            ySetThisTick = true;
            oldYRot = this.yRot;
        }
        this.xRot = xRot; this.yRot = yRot;
        float[] newRot = GeneralUtil.moveAngleAmountTowards(getOldRotation(), this, maxRotationPerTick);
        if ((Float.isNaN(newRot[0]) || Float.isNaN(newRot[1]))) {
            if (!(Float.isNaN(oldXRot) || Float.isNaN(oldYRot)))
                new Exception("Bruh; problematic rot (%.4f, %.4f), being set to (%.4f, %.4f) from (%.4f, %.4f)".formatted(newRot[0], newRot[1], xRot, yRot, oldXRot, oldYRot)).printStackTrace();
            else if (Float.isNaN(oldXRot)) this.oldXRot = xRot;
            else this.oldYRot = yRot;
        } else {
            this.xRot = newRot[0];
            this.yRot = newRot[1];
        }
    }

    @Override
    public void setXRot(float value) {
        if (!xSetThisTick) {
            xSetThisTick = true;
            oldXRot = xRot;
        }
        xRot = value;
        float[] newRot = GeneralUtil.moveAngleAmountTowards(getOldRotation(), this, maxRotationPerTick);
        xRot = newRot[0]; yRot = newRot[1];
    }

    @Override
    public void setYRot(float value) {
        if (!ySetThisTick) {
            ySetThisTick = true;
            oldYRot = yRot;
        }
        yRot = value;
        float[] newRot = GeneralUtil.moveAngleAmountTowards(getOldRotation(), this, maxRotationPerTick);
        xRot = newRot[0]; yRot = newRot[1];
    }
}
