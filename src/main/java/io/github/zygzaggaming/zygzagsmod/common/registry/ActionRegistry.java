package io.github.zygzaggaming.zygzagsmod.common.registry;

import io.github.zygzaggaming.zygzagsmod.common.entity.animation.Action;
import io.github.zygzaggaming.zygzagsmod.common.registry.base.CustomAkomiRegistry;
import net.minecraft.core.Registry;
import net.neoforged.bus.api.IEventBus;
import software.bernie.geckolib.animation.RawAnimation;

import java.util.function.Supplier;

public class ActionRegistry extends CustomAkomiRegistry<Action> {
    public static final ActionRegistry INSTANCE = new ActionRegistry("action");

    public ActionRegistry(String registryPath) {
        super(registryPath);
    }

    @Override
    public void registerTo(IEventBus bus) {
        IridiumGolem.init(); // make the IridiumGolem class load, subclasses don't load unless they're used
        BlazeSentry.init();
        SmallRod.init();
        super.registerTo(bus);
    }

    public static class IridiumGolem {
        public static final Supplier<Action> IDLE_BASE = INSTANCE.register("iridium_golem_idle_base", () -> new Action(RawAnimation.begin().thenPlay("animation.iridium_golem.idle_loop"), 160));
        public static final Supplier<Action> WALK_BASE = INSTANCE.register("iridium_golem_walk_base", () -> new Action(RawAnimation.begin().thenPlay("animation.iridium_golem.walk_cycle"), 31));
        public static final Supplier<Action> RUN_BASE = INSTANCE.register("iridium_golem_run_base", () -> new Action(RawAnimation.begin().thenPlay("animation.iridium_golem.run_cycle"), 16));
        public static final Supplier<Action> AGRO_BASE = INSTANCE.register("iridium_golem_agro_base", () -> new Action(RawAnimation.begin().thenPlay("animation.iridium_golem.agro_idle"), 40));
        public static final Supplier<Action> ATTACK_SMASH = INSTANCE.register("iridium_golem_attack_smash", () -> new Action(RawAnimation.begin().thenPlay("animation.iridium_golem.attack2"), 48));
        public static final Supplier<Action> IDLE_1 = INSTANCE.register("iridium_golem_idle_1", () -> new Action(RawAnimation.begin().thenPlay("animation.iridium_golem.idle1"), 54, false));
        public static final Supplier<Action> IDLE_2 = INSTANCE.register("iridium_golem_idle_2", () -> new Action(RawAnimation.begin().thenPlay("animation.iridium_golem.idle2"), 99, false));
        public static final Supplier<Action> IDLE_3 = INSTANCE.register("iridium_golem_idle_3", () -> new Action(RawAnimation.begin().thenPlay("animation.iridium_golem.idle3"), 114, false));

        public static void init() {
        }
    }

    public static class BlazeSentry {
        public static final Supplier<Action> IDLE_BASE = INSTANCE.register("blaze_sentry_idle_base", () -> new Action(RawAnimation.begin().thenPlay("animation.blaze_sentry.idle_loop"), 60, true));
        public static final Supplier<Action> SHOOT_BASE = INSTANCE.register("blaze_sentry_shoot_base", () -> new Action(RawAnimation.begin().thenPlay("animation.blaze_sentry.shooting_loop"), 60, false));
        public static final Supplier<Action> AGRO_BASE = INSTANCE.register("blaze_sentry_agro_base", () -> new Action(RawAnimation.begin().thenPlay("animation.blaze_sentry.agro_loop_2"), 40, false));
        public static final Supplier<Action> SHOOT_BIG_BASE = INSTANCE.register("blaze_sentry_shoot_big_base", () -> new Action(RawAnimation.begin().thenPlay("animation.blaze_sentry.shoot_big_fireball"), 53, false));
        public static final Supplier<Action> FLAMETHROW_BASE = INSTANCE.register("blaze_sentry_flamethrow_base", () -> new Action(RawAnimation.begin().thenPlay("animation.blaze_sentry.flamethrow"), 30, false));

        public static void init() {
        }
    }

    public static class SmallRod {
        public static final Supplier<Action> SPIN_BASE_0 = INSTANCE.register("small_rod_spin_base_0", () -> new Action(RawAnimation.begin().thenPlay("animation.small_rod.spin_0"), 40, true));
        public static final Supplier<Action> SPIN_BASE_1 = INSTANCE.register("small_rod_spin_base_1", () -> new Action(RawAnimation.begin().thenPlay("animation.small_rod.spin_1"), 40, true));
        public static final Supplier<Action> IDLE_BASE = INSTANCE.register("small_rod_idle", () -> new Action(RawAnimation.begin().thenPlay("animation.small_rod.idle"), 20, true));

        public static void init() {
        }
    }

    public static Registry<Action> registry() {
        return INSTANCE.backingRegistry();
    }
}
