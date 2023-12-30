package com.zygzag.zygzagsmod.common.util;

import static com.zygzag.zygzagsmod.common.util.GeneralUtil.degreesToRadians;
import static com.zygzag.zygzagsmod.common.util.GeneralUtil.radiansToDegrees;

public interface EntityRotation {
    void tick();

    void setBodyXRot(float value);

    float getBodyXRot(float partialTick);

    float getBodyXRot();

    void setBodyYRot(float value);

    float getBodyYRot(float partialTick);

    float getBodyYRot();

    void setHeadXRot(float value);

    float getHeadXRot(float partialTick);

    float getHeadXRot();

    void setHeadYRot(float value);

    float getHeadYRot(float partialTick);

    float getHeadYRot();

    default void setBodyXRotDegrees(float value) {
        setBodyXRot(degreesToRadians(value));
    }

    default float getBodyXRotDegrees(float partialTick) {
        return radiansToDegrees(getBodyXRot(partialTick));
    }

    default float getBodyXRotDegrees() {
        return radiansToDegrees(getBodyXRot());
    }

    default void setBodyYRotDegrees(float value) {
        setBodyYRot(degreesToRadians(value));
    }

    default float getBodyYRotDegrees(float partialTick) {
        return radiansToDegrees(getBodyYRot(partialTick));
    }

    default float getBodyYRotDegrees() {
        return radiansToDegrees(getBodyYRot());
    }

    default void setHeadXRotDegrees(float value) {
        setHeadXRot(degreesToRadians(value));
    }

    default float getHeadXRotDegrees(float partialTick) {
        return radiansToDegrees(getHeadXRot(partialTick));
    }

    default float getHeadXRotDegrees() {
        return radiansToDegrees(getHeadXRot());
    }

    default void setHeadYRotDegrees(float value) {
        setHeadYRot(degreesToRadians(value));
    }

    default float getHeadYRotDegrees(float partialTick) {
        return radiansToDegrees(getHeadYRot(partialTick));
    }

    default float getHeadYRotDegrees() {
        return radiansToDegrees(getHeadYRot());
    }
}
