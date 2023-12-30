package com.zygzag.zygzagsmod.common.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import static com.zygzag.zygzagsmod.common.util.GeneralUtil.*;

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
    protected float bodyXRot, oldBodyXRot, newBodyXRot;
    protected float bodyYRot, oldBodyYRot, newBodyYRot;
    protected float headXRot, oldHeadXRot, newHeadXRot;
    protected float headYRot, oldHeadYRot, newHeadYRot;

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

    @Override
    public void tick() {
        oldBodyXRot = bodyXRot; bodyXRot = newBodyXRot;
        oldBodyYRot = bodyYRot; bodyYRot = newBodyYRot;
        oldHeadXRot = headXRot; headXRot = newHeadXRot;
        oldHeadYRot = headYRot; headYRot = newHeadYRot;
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
