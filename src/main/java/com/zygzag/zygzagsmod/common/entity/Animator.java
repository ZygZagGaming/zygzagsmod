package com.zygzag.zygzagsmod.common.entity;

import com.zygzag.zygzagsmod.common.util.GeneralUtil;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.object.PlayState;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.LinkedList;
import java.util.Queue;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class Animator<T extends LivingEntity & AnimatedEntity<T>> {
    private final T parent;
    private Animation lastNonTransitionAnimation;
    private AbstractAnimation lastAnimation;
    private final Queue<AbstractAnimation> queuedAnims = new LinkedList<>();
    private Animation animation;
    private @Nullable TransitionAnimation transitionAnimation;
    private int timeUntilIdleAnimation = 5 * 20;
    private int ticksRemainingInAnimation = 0;

    private final AnimationController<T> mainAnimController;

    public Animator(T parent) {
        this.parent = parent;
        mainAnimController = new AnimationController<>(parent, "main", 0, (animState) -> animState.getAnimatable().getAnimator().animate(animState));
    }

    public Level level() {
        return parent.level();
    }

    public Animation getAnimation() {
        return animation;
    }

    public @Nullable TransitionAnimation getTransitionAnimation() {
        return transitionAnimation;
    }

    public int getTicksRemainingInAnimation() {
        return ticksRemainingInAnimation;
    }

    public void setAnimation(Animation animation) {
        if (!level().isClientSide()) {
            this.animation = animation;
        }
    }

    public void setTransitionAnimation(@Nullable TransitionAnimation transitionAnimation) {
        this.transitionAnimation = transitionAnimation;
    }

    public void setTicksRemainingInAnimation(int ticksRemainingInAnimation) {
        this.ticksRemainingInAnimation = ticksRemainingInAnimation;
    }

    public void tick() {
        if (parent.isIdle()) timeUntilIdleAnimation--;
        var animation = parent.getAnimationChange();
        if (!level().isClientSide()) {
            if (animation != null) setAnimation(animation);
            if (getTicksRemainingInAnimation() > 0)
                setTicksRemainingInAnimation(getTicksRemainingInAnimation() - 1);

            Animation currentAnimation = getAnimation();

            // Check if transition should be played
            if (currentAnimation != lastNonTransitionAnimation) {
                TransitionAnimation transitionAnim = GeneralUtil.getTransitionAnimation(lastNonTransitionAnimation, currentAnimation);
                if (transitionAnim != null) queueAnimation(transitionAnim);
                lastNonTransitionAnimation = currentAnimation;
            }

            // Choose Animation to play if the current one should be over
            if (getTicksRemainingInAnimation() <= 0 || lastAnimation == null || lastAnimation.canBeSkipped()) {
                AbstractAnimation animToChangeTo;
                if (queuedAnims.isEmpty()) {
                    //Does not continue with another Idle animation until the animation is complete
                    if (parent.isIdle() && timeUntilIdleAnimation <= 0) {
                        timeUntilIdleAnimation = (int) (20 * (10 + 15 * level().getRandom().nextDouble())); // 10-25s
                        animToChangeTo = GeneralUtil.randomElement(parent.idleAnimations(), level().getRandom());
                    } else {
                        animToChangeTo = animation;
                    }
                } else {
                    animToChangeTo = queuedAnims.poll();
                }

                if (animToChangeTo != null) {
                    playAnimation(animToChangeTo);
                    setTicksRemainingInAnimation(animToChangeTo.lengthInTicks());
                }
            }
        }

        if (parent instanceof PathfinderMob pather) {
            pather.getNavigation().setSpeedModifier(getTicksRemainingInAnimation() > 0 && lastAnimation instanceof TransitionAnimation trans ? trans.speedModifier(getTicksRemainingInAnimation()) : 1);
        }

        lastAnimation = getTransitionAnimation() != null ? getTransitionAnimation() : getAnimation();
    }

    public void queueAnimation(AbstractAnimation anim) {
        queuedAnims.add(anim);
    }

    public void playAnimation(AbstractAnimation animation) {
        if (animation instanceof Animation normalAnim) {
            setAnimation(normalAnim);
            setTransitionAnimation(null);
        } else if (animation instanceof TransitionAnimation transition) setTransitionAnimation(transition);
    }

    public PlayState animate(AnimationState<T> animState) {
        AnimationController<T> animController = animState.getController();

        TransitionAnimation transition = getTransitionAnimation();
        Animation animation = getAnimation();
        animController.setAnimation(transition != null ? transition.raw() : (animation != null ? animation.raw() : null));

        return PlayState.CONTINUE;
    }

    public void register(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(mainAnimController);
    }
}
