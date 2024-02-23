package io.github.zygzaggaming.zygzagsmod.common.entity.animation;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.core.animatable.GeoAnimatable;

import java.util.List;

public interface ActingEntity<T extends LivingEntity & ActingEntity<T>> extends GeoAnimatable {
    boolean isIdle();

    Actor<T> getActor();

    @Nullable Action getActionChange();

    List<Action> idleActions();

    EntityDataAccessor<Actor.State> actionStateAccessor();

    default boolean doesIdleActions() {
        return !idleActions().isEmpty();
    }
}
