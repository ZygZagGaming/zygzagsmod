package io.github.zygzaggaming.zygzagsmod.common.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import static io.github.zygzaggaming.zygzagsmod.common.util.GeneralUtil.lerpRotations;

public class LerpedRotation extends SimpleRotation {
    public static Codec<LerpedRotation> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.FLOAT.fieldOf("xRot").forGetter((it) -> it.xRot),
                    Codec.FLOAT.fieldOf("yRot").forGetter((it) -> it.yRot),
                    Codec.FLOAT.fieldOf("oldXRot").forGetter((it) -> it.oldXRot),
                    Codec.FLOAT.fieldOf("oldYRot").forGetter((it) -> it.oldYRot)
            ).apply(instance, LerpedRotation::new)
    );
    // IMPORTANT: THIS IS IN RADIANS!!
    public float oldXRot, oldYRot;
    public boolean xSetThisTick, ySetThisTick;

    public LerpedRotation() { }

    public LerpedRotation(float xRot, float yRot) {
        super(xRot, yRot);
    }

    public LerpedRotation(float xRot, float yRot, float oldXRot, float oldYRot) {
        super(xRot, yRot);
        this.oldXRot = oldXRot;
        this.oldYRot = oldYRot;
    }

    public LerpedRotation(LerpedRotation past) {
        super(past);
        oldXRot = past.oldXRot;
        oldYRot = past.oldYRot;
    }

    @Override
    public void tick() {
        xSetThisTick = ySetThisTick = false;
    }

    @Override
    public void setXRot(float value) {
        if (!xSetThisTick) {
            xSetThisTick = true;
            oldXRot = xRot;
        }
        xRot = value;
    }

    @Override
    public float getXRot(float partialTick) {
        return lerpRotations(getOldRotation(), this, partialTick)[0];
    }

    @Override
    public void setYRot(float value) {
        if (!ySetThisTick) {
            ySetThisTick = true;
            oldYRot = yRot;
        }
        yRot = value;
    }

    @Override
    public float getYRot(float partialTick) {
        return lerpRotations(getOldRotation(), this, partialTick)[1];
    }

    public Rotation getOldRotation() {
        return new SimpleRotation(oldXRot, oldYRot);
    }

    public void setOldXRot(float value) {
        this.oldXRot = value;
    }
    public void setOldYRot(float value) {
        this.oldYRot = value;
    }
}
