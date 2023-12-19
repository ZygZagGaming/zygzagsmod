package com.zygzag.zygzagsmod.common.registry;

import com.zygzag.zygzagsmod.common.entity.animation.Animation;
import com.zygzag.zygzagsmod.common.registry.base.CustomAkomiRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.IEventBus;
import software.bernie.geckolib.core.animation.RawAnimation;

import java.util.function.Supplier;

public class AnimationRegistry extends CustomAkomiRegistry<Animation> {
    public static final AnimationRegistry INSTANCE = new AnimationRegistry("animation");
    public static final ResourceKey<? extends Registry<Animation>> ANIMATION_REGISTRY_KEY = INSTANCE.registryKey();

    public AnimationRegistry(String registryPath) {
        super(registryPath);
    }

    @Override
    public void registerTo(IEventBus bus) {
        IridiumGolem.init(); // make the IridiumGolem class load, subclasses don't load unless they're used
        BlazeSentry.init();
        super.registerTo(bus);
    }

    public static class IridiumGolem {
        public static final Supplier<Animation> IDLE_BASE = INSTANCE.register("iridium_golem_idle_base", () -> new Animation(RawAnimation.begin().thenPlay("animation.iridium_golem.idle_loop"), 160));
        public static final Supplier<Animation> WALK_BASE = INSTANCE.register("iridium_golem_walk_base", () -> new Animation(RawAnimation.begin().thenPlay("animation.iridium_golem.walk_cycle"), 31));
        public static final Supplier<Animation> RUN_BASE = INSTANCE.register("iridium_golem_run_base", () -> new Animation(RawAnimation.begin().thenPlay("animation.iridium_golem.run_cycle"), 16));
        public static final Supplier<Animation> AGRO_BASE = INSTANCE.register("iridium_golem_agro_base", () -> new Animation(RawAnimation.begin().thenPlay("animation.iridium_golem.agro_idle"), 40));
        public static final Supplier<Animation> ATTACK_SMASH = INSTANCE.register("iridium_golem_attack_smash", () -> new Animation(RawAnimation.begin().thenPlay("animation.iridium_golem.attack2"), 48));
        public static final Supplier<Animation> IDLE_1 = INSTANCE.register("iridium_golem_idle_1", () -> new Animation(RawAnimation.begin().thenPlay("animation.iridium_golem.idle1"), 54, false));
        public static final Supplier<Animation> IDLE_2 = INSTANCE.register("iridium_golem_idle_2", () -> new Animation(RawAnimation.begin().thenPlay("animation.iridium_golem.idle2"), 99, false));
        public static final Supplier<Animation> IDLE_3 = INSTANCE.register("iridium_golem_idle_3", () -> new Animation(RawAnimation.begin().thenPlay("animation.iridium_golem.idle3"), 114, false));

        public static void init() {
        }
    }

    public static class BlazeSentry {
        public static final Supplier<Animation> IDLE_BASE = INSTANCE.register("blaze_sentry_idle_base", () -> new Animation(RawAnimation.begin().thenPlay("animation.blaze_sentry.idle_rotation"), 60));
        public static final Supplier<Animation> SHOOT_BASE = INSTANCE.register("blaze_sentry_shoot_base", () -> new Animation(RawAnimation.begin().thenPlay("animation.blaze_sentry.shooting_loop"), 60));

        public static void init() {
        }
    }

    public static Registry<Animation> registry() {
        return INSTANCE.backingRegistry();
    }
}
