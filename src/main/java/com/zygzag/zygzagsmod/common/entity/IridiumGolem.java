package com.zygzag.zygzagsmod.common.entity;

import com.zygzag.zygzagsmod.common.GeneralUtil;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;
import java.util.UUID;

public class IridiumGolem extends AbstractGolem implements NeutralMob, GeoAnimatable {
    protected static final RawAnimation START_WALKING = RawAnimation.begin().thenPlay("animation.iridium_golem.start_walk").thenLoop("animation.iridium_golem.walk_cycle");
    protected static final RawAnimation WALK_ANIM = RawAnimation.begin().thenLoop("animation.iridium_golem.walk_cycle");
    protected static final RawAnimation WALK_START = RawAnimation.begin().thenPlay("animation.iridium_golem.start_walk");
    protected static final RawAnimation WALK_STOP = RawAnimation.begin().thenPlay("animation.iridium_golem.stop_walk");
    protected static final RawAnimation IDLE1 = RawAnimation.begin().thenPlay("animation.iridium_golem.idle1");
    protected static final RawAnimation IDLE2 = RawAnimation.begin().thenPlay("animation.iridium_golem.idle2");

    private final AnimatableInstanceCache instanceCache = GeckoLibUtil.createInstanceCache(this);

    public IridiumGolem(EntityType<? extends AbstractGolem> type, Level world) {
        super(type, world);
    }

    @Override
    public int getRemainingPersistentAngerTime() {
        return 0;
    }

    @Override
    public void setRemainingPersistentAngerTime(int p_21673_) {

    }

    protected void registerGoals() {
        this.goalSelector.addGoal(1, new RandomStrollGoal(this, 1));
    }

    @Nullable
    @Override
    public UUID getPersistentAngerTarget() {
        return null;
    }

    @Override
    public void setPersistentAngerTarget(@Nullable UUID p_21672_) {

    }

    @Override
    public void startPersistentAngerTimer() {

    }

    @Override
    public void tick() {
        //walkAnimController.setAnimation(WALK_ANIM);
        super.tick();
        //sinceMotionChange++;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 100.0D).add(Attributes.MOVEMENT_SPEED, 0.08).add(Attributes.KNOCKBACK_RESISTANCE, 1.0D).add(Attributes.ATTACK_DAMAGE, 15.0D);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(animationController);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return instanceCache;
    }

    @Override
    public double getTick(Object o) {
        return tickCount;
    }

    private final AnimationController<IridiumGolem> animationController = new AnimationController<>(this, "animations", 5, (animState) -> {
        AnimationController<IridiumGolem> animController = animState.getController();
        if (animState.isMoving()) {
            animController.setAnimation(START_WALKING);
            return PlayState.CONTINUE;
        } else {
            var currentAnimation = animController.getCurrentRawAnimation();
            if (currentAnimation == START_WALKING) {
                animController.setAnimation(WALK_STOP);
            } else {
                if (currentAnimation == null) {
                    if (Math.random() < 0.01) animController.setAnimation(IDLE1);
                    else if (Math.random() < 0.01) animController.setAnimation(IDLE2);
                }
            }
            if (animController.hasAnimationFinished()) return PlayState.STOP;
            else return PlayState.CONTINUE;
        }
    }).setParticleKeyframeHandler((event) -> {
        var data = event.getKeyframeData();
        var controller = event.getController();
        var effect = data.getEffect();
        var locator = data.getLocator();

    });
}
