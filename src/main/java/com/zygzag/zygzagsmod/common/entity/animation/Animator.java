package com.zygzag.zygzagsmod.common.entity.animation;

import com.zygzag.zygzagsmod.common.Main;
import com.zygzag.zygzagsmod.common.util.GeneralUtil;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
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
    private final Queue<AbstractAnimation> queuedAnims = new LinkedList<>();
    private final AnimationController<T> mainAnimController;
    private Animation lastNonTransitionAnimation;
    private AbstractAnimation lastAnimation;
    private Animation animation;
    private @Nullable TransitionAnimation transitionAnimation;
    private int timeUntilIdleAnimation = 5 * 20;
    private int ticksRemainingInAnimation = 0;

    public Animator(T parent) {
        this.parent = parent;
        mainAnimController = new AnimationController<>(parent, "main", 0, (animState) -> animState.getAnimatable().getAnimator().animate(animState));
    }

    public Level level() {
        return parent.level();
    }

    public @Nullable Animation getAnimation() {
        return animation;
    }

    public void setAnimation(Animation animation) {
        if (!level().isClientSide()) {
            //System.out.println("animation changed from " + getAnimation() + " to " + animation);
            this.animation = animation;
        }
    }

    public @Nullable TransitionAnimation getTransitionAnimation() {
        return transitionAnimation;
    }

    public void setTransitionAnimation(@Nullable TransitionAnimation transitionAnimation) {
        this.transitionAnimation = transitionAnimation;
    }

    public int getTicksRemainingInAnimation() {
        return ticksRemainingInAnimation;
    }

    public void setTicksRemainingInAnimation(int ticksRemainingInAnimation) {
        this.ticksRemainingInAnimation = ticksRemainingInAnimation;
    }

    public void tick() {
        if (parent.isIdle()) timeUntilIdleAnimation--;
        if (!level().isClientSide()) {
            var animation = parent.getAnimationChange();
            if (animation != null) setAnimation(animation);
            if (getTicksRemainingInAnimation() > 0)
                setTicksRemainingInAnimation(getTicksRemainingInAnimation() - 1);

            Animation currentAnimation = getAnimation();

            // Check if transition should be played
            if (currentAnimation != null && !currentAnimation.is(lastNonTransitionAnimation)) {
                //System.out.println("animation change between " + lastNonTransitionAnimation + " and " + currentAnimation);
                TransitionAnimation transitionAnim = GeneralUtil.getTransitionAnimation(lastNonTransitionAnimation, currentAnimation);
                if (transitionAnim != null) queueAnimation(transitionAnim);
                lastNonTransitionAnimation = currentAnimation;
            }

            // Choose Animation to play if the current one should be over
            if (getTicksRemainingInAnimation() <= 0 || lastAnimation == null || lastAnimation.canBeSkipped()) {
                if (lastAnimation instanceof TransitionAnimation) setTransitionAnimation(null);
                AbstractAnimation animToChangeTo;
                if (queuedAnims.isEmpty()) {
                    //Does not continue with another Idle animation until the animation is complete
                    if (parent.doesIdleAnimations() && parent.isIdle() && timeUntilIdleAnimation <= 0) {
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

        if (level().isClientSide()) {
            setState(parent.getEntityData().get(parent.animatorStateAccessor()));
        } else {
            parent.getEntityData().set(parent.animatorStateAccessor(), getState());
        }
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

    public State getState() {
        return new State(
                animation,
                transitionAnimation,
                timeUntilIdleAnimation,
                ticksRemainingInAnimation,
                queuedAnims,
                lastAnimation,
                lastNonTransitionAnimation
        );
    }

    public void setState(State state) {
        animation = state.animation();
        transitionAnimation = state.transitionAnimation();
        timeUntilIdleAnimation = state.timeUntilIdleAnimation();
        ticksRemainingInAnimation = state.ticksRemainingInAnimation();
        queuedAnims.clear();
        queuedAnims.addAll(state.queuedAnims());
        lastAnimation = state.lastAnimation();
        lastNonTransitionAnimation = state.lastNonTransitionAnimation;
    }

    public boolean isTransitioning() {
        return getTransitionAnimation() != null;
    }

    public float progressThroughAnimation() {
        return (float) ticksRemainingInAnimation / (transitionAnimation == null ? animation : transitionAnimation).lengthInTicks();
    }

    public record State(
            Animation animation,
            @Nullable TransitionAnimation transitionAnimation,
            int timeUntilIdleAnimation,
            int ticksRemainingInAnimation,
            Queue<AbstractAnimation> queuedAnims,
            AbstractAnimation lastAnimation,
            Animation lastNonTransitionAnimation
    ) {
        public static State fromNetwork(FriendlyByteBuf buf) {
            ResourceLocation animationLoc = new ResourceLocation(buf.readUtf());
            Animation animation = Main.animationRegistry().get(animationLoc);
            if (animation == null) throw new IllegalArgumentException("Invalid animation id recieved: " + animationLoc);
            String transString = buf.readUtf();
            TransitionAnimation transitionAnimation = transString.equals("null") ? null : Main.transitionAnimationRegistry().get(new ResourceLocation(transString));
            int timeUntilIdleAnimation = buf.readInt();
            int ticksRemainingInAnimation = buf.readInt();
            int numQueuedAnims = buf.readInt();
            Queue<AbstractAnimation> queuedAnims = new LinkedList<>();
            for (int i = 0; i < numQueuedAnims; i++) {
                boolean isTransition = buf.readBoolean();
                ResourceLocation loc = new ResourceLocation(buf.readUtf());
                AbstractAnimation anim = isTransition ? Main.transitionAnimationRegistry().get(loc) : Main.animationRegistry().get(loc);
                queuedAnims.add(anim);
            }
            boolean lastTransition = buf.readBoolean();
            ResourceLocation lastLoc = new ResourceLocation(buf.readUtf());
            AbstractAnimation lastAnimation = lastTransition ? Main.transitionAnimationRegistry().get(lastLoc) : Main.animationRegistry().get(lastLoc);
            if (lastAnimation == null) throw new IllegalArgumentException("Invalid animation id recieved: " + lastLoc);
            ResourceLocation lastNonTransitionLoc = new ResourceLocation(buf.readUtf());
            Animation lastNonTransitionAnimation = Main.animationRegistry().get(lastNonTransitionLoc);
            if (lastNonTransitionAnimation == null)
                throw new IllegalArgumentException("Invalid animation id recieved: " + lastNonTransitionLoc);
            return new State(animation, transitionAnimation, timeUntilIdleAnimation, ticksRemainingInAnimation, queuedAnims, lastAnimation, lastNonTransitionAnimation);
        }

        public void toNetwork(FriendlyByteBuf buf) {
            buf.writeUtf(animation.id().toString());
            buf.writeUtf(transitionAnimation == null ? "null" : transitionAnimation.id().toString());
            buf.writeInt(timeUntilIdleAnimation);
            buf.writeInt(ticksRemainingInAnimation);
            buf.writeInt(queuedAnims.size());
            for (AbstractAnimation queued : queuedAnims) {
                buf.writeBoolean(queued instanceof TransitionAnimation);
                buf.writeUtf(queued.id().toString());
            }
            buf.writeBoolean(lastAnimation instanceof TransitionAnimation);
            buf.writeUtf(lastAnimation.id().toString());
            buf.writeUtf(lastNonTransitionAnimation.id().toString());
        }
    }
}
