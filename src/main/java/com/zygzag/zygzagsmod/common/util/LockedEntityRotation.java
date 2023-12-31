package com.zygzag.zygzagsmod.common.util;

import net.minecraft.world.phys.Vec3;

import static com.zygzag.zygzagsmod.common.util.GeneralUtil.moveAngleAmountTowards;

public class LockedEntityRotation extends SimplEntityRotation {
    public static final RotationsFromDifference DEFAULT_ROTATIONS = (difference) -> {
        double horizDifference = difference.horizontalDistance();
        float yRot = (float) Math.atan2(difference.x(), difference.z());
        float xRot = (float) Math.atan2(difference.y(), horizDifference);
        return new float[]{xRot, 0, yRot, yRot};
    };

    public PositionAnchor origin, target;
    public RotationsFromDifference transform;
    public LockedEntityRotation(PositionAnchor origin, PositionAnchor target, RotationsFromDifference transform) {
        this.origin = origin;
        this.target = target;
        this.transform = transform;
    }
    public LockedEntityRotation(PositionAnchor origin, PositionAnchor target, RotationsFromDifference transform, SimplEntityRotation past) {
        super(past);
        this.origin = origin;
        this.target = target;
        this.transform = transform;
    }
    public LockedEntityRotation(PositionAnchor origin, PositionAnchor target) {
        this(origin, target, DEFAULT_ROTATIONS);
    }

    public LockedEntityRotation(PositionAnchor target, RotationsFromDifference transform) {
        this(PositionAnchor.ZERO, target, transform);
    }

    public LockedEntityRotation(PositionAnchor target) {
        this(PositionAnchor.ZERO, target);
    }

    @Override
    public void tick() {
        origin.tick();
        target.tick();

        float[] rotations = transform.rotationsFromDifference(target.get().subtract(origin.get()));

        newHeadXRot = rotations[0];
        newBodyXRot = rotations[1];
        newHeadYRot = rotations[2];
        newBodyYRot = rotations[3];

        super.tick();
    }

    public interface RotationsFromDifference {
        float[] rotationsFromDifference(Vec3 difference);
    }
}
