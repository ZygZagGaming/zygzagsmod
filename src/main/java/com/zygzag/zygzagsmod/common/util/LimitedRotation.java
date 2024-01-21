package com.zygzag.zygzagsmod.common.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

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
    public void setXRot(float value) {
        if (!xSetThisTick) {
            xSetThisTick = true;
            oldXRot = xRot;
        }
        float[] newRot = GeneralUtil.moveAngleAmountTowards(oldXRot, oldYRot, value, yRot, maxRotationPerTick);
        xRot = newRot[0]; yRot = newRot[1];
    }

    @Override
    public void setYRot(float value) {
        if (!ySetThisTick) {
            ySetThisTick = true;
            oldYRot = yRot;
        }
        float[] newRot = GeneralUtil.moveAngleAmountTowards(oldXRot, oldYRot, xRot, value, maxRotationPerTick);
        xRot = newRot[0]; yRot = newRot[1];
    }
}
