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

        ShurikenAssembly.init();
        SentryAAssembly.init();
        HelixAAssembly.init();
        HelixBAssembly.init();
        SpawnerAAssembly.init();
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
        public static final Supplier<Action> RANDOM_SPIN_BASE = (Math.random() <= 0.5) ?
                INSTANCE.register("small_rod_spin_random_base_0", () -> new Action(RawAnimation.begin().thenPlay("animation.small_rod.spin_0"), 40, true)) :
                INSTANCE.register("small_rod_spin_random_base_1", () -> new Action(RawAnimation.begin().thenPlay("animation.small_rod.spin_1"), 40, true));
        public static final Supplier<Action> SPIN_BASE_0 = INSTANCE.register("small_rod_spin_base_0", () -> new Action(RawAnimation.begin().thenPlay("animation.small_rod.spin_0"), 40, true));
        public static final Supplier<Action> SPIN_BASE_1 = INSTANCE.register("small_rod_spin_base_1", () -> new Action(RawAnimation.begin().thenPlay("animation.small_rod.spin_1"), 40, true));
        public static final Supplier<Action> IDLE_BASE = INSTANCE.register("small_rod_idle", () -> new Action(RawAnimation.begin().thenPlay("animation.small_rod.idle"), 20, true));

        public static void init() {
        }
    }

    public static class ShurikenAssembly {
        //public static final Supplier<Action> RANDOM_SPIN_BASE = (Math.random() <= 0.5) ?
        public static final Supplier<Action> SPIN_UP = INSTANCE.register("shuriken_assembly_spin_base", () -> new Action(RawAnimation.begin().thenPlay("animation.four_rod.Shuriken_A_0"), 20, false));

        public static void init() {
        }
    }

    public static class SentryAAssembly {
        public static final Supplier<Action> SPIN_BASE = INSTANCE.register("sentry_a_spin_base", () -> new Action(RawAnimation.begin().thenPlay("animation.sentry_a.spin"), 20, false));
        public static final Supplier<Action> ASSEMBLY = INSTANCE.register("sentry_a_assembly", () -> new Action(RawAnimation.begin().thenPlay("animation.sentry_a.assembly"), 20, true));
        public static final Supplier<Action> IDLE_BASE = INSTANCE.register("sentry_a_assembly_idle", () -> new Action(RawAnimation.begin().thenPlay("animation.sentry_a.idle"), 10, true));

        public static void init() {
        }
    }

    public static class HelixAAssembly {
        public static final Supplier<Action> SPIN_BASE = INSTANCE.register("helix_a_spin_base", () -> new Action(RawAnimation.begin().thenPlay("animation.Helix_A_spin"), 20, false));
        public static final Supplier<Action> ASSEMBLY = INSTANCE.register("helix_a_assembly", () -> new Action(RawAnimation.begin().thenPlay("animation.Helix_A_assembly"), 20, true));

        public static void init() {
        }
    }

    public static class HelixBAssembly {
        public static final Supplier<Action> SPIN_BASE = INSTANCE.register("helix_b_spin_base", () -> new Action(RawAnimation.begin().thenPlay("animation.Helix_B_spin"), 20, false));
        public static final Supplier<Action> ASSEMBLY = INSTANCE.register("helix_b_assembly", () -> new Action(RawAnimation.begin().thenPlay("animation.Helix_B_assembly"), 20, false));

        public static void init() {
        }
    }

    public static class SpawnerAAssembly {
        public static final Supplier<Action> OPENED_BASE = INSTANCE.register("spawner_a_opened_base", () -> new Action(RawAnimation.begin().thenPlay("animation.spawner_a_opened"), 80, false));
        public static final Supplier<Action> CLOSED_BASE = INSTANCE.register("spawner_a_closed_base", () -> new Action(RawAnimation.begin().thenPlay("animation.spawner_a_closed"), 10, false));
        public static final Supplier<Action> SPAWNING_BASE = INSTANCE.register("spawner_a_spawning_base", () -> new Action(RawAnimation.begin().thenPlay("animation.spawner_a_spawning"), 10, false));
        public static final Supplier<Action> ASSEMBLY = INSTANCE.register("spawner_a_assembly", () -> new Action(RawAnimation.begin().thenPlay("animation.spawner_a_assembly"), 40, false));

        public static void init() {
        }
    }

    public static Registry<Action> registry() {
        return INSTANCE.backingRegistry();
    }
}
