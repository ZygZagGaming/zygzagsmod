package com.zygzag.zygzagsmod.common.entity;

import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.core.animatable.GeoAnimatable;

import java.util.List;

public interface AnimatedEntity<T extends LivingEntity & AnimatedEntity<T>> extends GeoAnimatable {
    boolean isIdle();
    Animator<T> getAnimator();
    @Nullable Animation getAnimationChange();
    List<Animation> idleAnimations();
//    default boolean isMoving() {
//        Animator<T> animator = getAnimator();
//        if (animator.getTransitionAnimation() != null && animator.getTransitionAnimation().speedModifier(animator.getTicksRemainingInAnimation()) == 0) return true;
//        if (this instanceof PathfinderMob pather) {
//            return (pather.getNavigation().getPath() == null) ||
//        }
//    }
}