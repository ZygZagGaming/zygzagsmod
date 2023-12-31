package com.zygzag.zygzagsmod.common.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import static com.zygzag.zygzagsmod.common.util.GeneralUtil.*;
import static java.lang.Math.cos;

@SuppressWarnings("unused")
public class SimplEntityRotation implements EntityRotation {
    public static Codec<SimplEntityRotation> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.FLOAT.fieldOf("bodyXRot").forGetter((it) -> it.bodyXRot),
                    Codec.FLOAT.fieldOf("bodyYRot").forGetter((it) -> it.bodyYRot),
                    Codec.FLOAT.fieldOf("oldBodyXRot").forGetter((it) -> it.oldBodyXRot),
                    Codec.FLOAT.fieldOf("oldBodyYRot").forGetter((it) -> it.oldBodyYRot),
                    Codec.FLOAT.fieldOf("headXRot").forGetter((it) -> it.headXRot),
                    Codec.FLOAT.fieldOf("headYRot").forGetter((it) -> it.headYRot),
                    Codec.FLOAT.fieldOf("oldHeadXRot").forGetter((it) -> it.oldHeadXRot),
                    Codec.FLOAT.fieldOf("oldHeadYRot").forGetter((it) -> it.oldHeadYRot)
            ).apply(instance, SimplEntityRotation::new)
    );
    // IMPORTANT: THIS IS IN RADIANS!!
    public float bodyXRot, oldBodyXRot, newBodyXRot;
    public float bodyYRot, oldBodyYRot, newBodyYRot;
    public float headXRot, oldHeadXRot, newHeadXRot;
    public float headYRot, oldHeadYRot, newHeadYRot;
    public float[] maxRotationPerTick = {(float) (2 * Math.PI), (float) (2 * Math.PI)};

    public SimplEntityRotation() { }

    public SimplEntityRotation(float bodyXRot, float bodyYRot, float oldBodyXRot, float oldBodyYRot, float headXRot, float headYRot, float oldHeadXRot, float oldHeadYRot) {
        this.bodyXRot = this.newBodyXRot = bodyXRot;
        this.bodyYRot = this.newBodyYRot = bodyYRot;
        this.oldBodyXRot = oldBodyXRot;
        this.oldBodyYRot = oldBodyYRot;
        this.headXRot = this.newHeadXRot = headXRot;
        this.headYRot = this.newHeadYRot = headYRot;
        this.oldHeadXRot = oldHeadXRot;
        this.oldHeadYRot = oldHeadYRot;
    }

    public SimplEntityRotation(SimplEntityRotation past, boolean resetRotations) {
        bodyXRot = past.bodyXRot;
        bodyYRot = past.bodyYRot;
        oldBodyXRot = past.oldBodyXRot;
        oldBodyYRot = past.oldBodyYRot;
        headXRot = past.headXRot;
        headYRot = past.headYRot;
        oldHeadXRot = past.oldHeadXRot;
        oldHeadYRot = past.oldHeadYRot;
        maxRotationPerTick = past.maxRotationPerTick;
        if (!resetRotations) {
            newBodyXRot = past.newBodyXRot;
            newBodyYRot = past.newBodyYRot;
            newHeadXRot = past.newHeadXRot;
            newHeadYRot = past.newHeadYRot;
        }
    }

    public SimplEntityRotation(SimplEntityRotation past) {
        this(past, true);
    }

    @Override
    public void tick() {
        oldHeadXRot = headXRot;
        oldBodyXRot = bodyXRot;
        oldHeadYRot = headYRot;
        oldBodyYRot = bodyYRot;
        float[] newHeadRot = moveAngleAmountTowards(headXRot, headYRot, newHeadXRot, newHeadYRot, maxRotationPerTick[0]);
        float[] newBodyRot = moveAngleAmountTowards(bodyXRot, bodyYRot, newBodyXRot, newBodyYRot, maxRotationPerTick[1]);
        headXRot = newHeadRot[0]; headYRot = newHeadRot[1];
        bodyXRot = newBodyRot[0]; bodyYRot = newBodyRot[1];
    }

    @Override
    public void setBodyXRot(float value) {
        newBodyXRot = value;
    }

    @Override
    public float getBodyXRot(float partialTick) {
        return angleLerp(oldBodyXRot, bodyXRot, partialTick);
    }

    @Override
    public float getBodyXRot() {
        return bodyXRot;
    }

    @Override
    public void setBodyYRot(float value) {
        newBodyYRot = value;
    }

    @Override
    public float getBodyYRot(float partialTick) {
        return angleLerp(oldBodyYRot, bodyYRot, partialTick);
    }

    @Override
    public float getBodyYRot() {
        return bodyYRot;
    }

    @Override
    public void setHeadXRot(float value) {
        newHeadXRot = value;
    }

    @Override
    public float getHeadXRot(float partialTick) {
        return angleLerp(oldHeadXRot, headXRot, partialTick);
    }

    @Override
    public float getHeadXRot() {
        return headXRot;
    }

    @Override
    public void setHeadYRot(float value) {
        newHeadYRot = value;
    }

    @Override
    public float getHeadYRot(float partialTick) {
        return angleLerp(oldHeadYRot, headYRot, partialTick);
    }

    @Override
    public float getHeadYRot() {
        return headYRot;
    }
}
