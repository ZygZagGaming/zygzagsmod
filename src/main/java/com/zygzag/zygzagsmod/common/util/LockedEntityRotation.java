package com.zygzag.zygzagsmod.common.util;

import net.minecraft.world.phys.Vec3;

public class LockedEntityRotation extends SimplEntityRotation {
    public static final AnglesFromAnchors DEFAULT_ROTATIONS = (difference) -> {
        double horizDifference = difference.horizontalDistance();
        float yRot = (float) Math.atan2(difference.x(), difference.z());
        float xRot = (float) Math.atan2(difference.y(), horizDifference);
        return new float[]{xRot, 0, yRot, yRot};
    };

    public PositionAnchor origin, target;
    public AnglesFromAnchors transform;
    public LockedEntityRotation(PositionAnchor origin, PositionAnchor target, AnglesFromAnchors transform) {
        this.origin = origin;
        this.target = target;
        this.transform = transform;
    }
    public LockedEntityRotation(PositionAnchor origin, PositionAnchor target) {
        this(origin, target, DEFAULT_ROTATIONS);
    }

    public LockedEntityRotation(PositionAnchor target, AnglesFromAnchors transform) {
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

        setHeadXRot(rotations[0]);
        setBodyXRot(rotations[1]);
        setHeadYRot(rotations[2]);
        setBodyYRot(rotations[3]);

        super.tick();
    }

    public interface AnglesFromAnchors {
        float[] rotationsFromDifference(Vec3 difference);
    }
}
