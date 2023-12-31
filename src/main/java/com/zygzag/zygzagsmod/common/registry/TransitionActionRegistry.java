package com.zygzag.zygzagsmod.common.registry;

import com.zygzag.zygzagsmod.common.entity.animation.TransitionAction;
import com.zygzag.zygzagsmod.common.registry.base.CustomAkomiRegistry;
import net.minecraft.core.Registry;
import net.neoforged.bus.api.IEventBus;
import software.bernie.geckolib.core.animation.RawAnimation;

import java.util.function.Supplier;

public class TransitionActionRegistry extends CustomAkomiRegistry<TransitionAction> {
    public static final TransitionActionRegistry INSTANCE = new TransitionActionRegistry("transition_action");

    public TransitionActionRegistry(String registryPath) {
        super(registryPath);
    }

    @Override
    public void registerTo(IEventBus bus) {
        IridiumGolem.init(); // make the IridiumGolem class load, subclasses don't load unless they're used
        BlazeSentry.init();
        super.registerTo(bus);
    }

    public static class IridiumGolem {
        public static final Supplier<TransitionAction> IDLE_BASE_TO_WALK_BASE = INSTANCE.register("iridium_golem_idle_base_to_walk_base", () -> new TransitionAction(ActionRegistry.IridiumGolem.IDLE_BASE.get(), ActionRegistry.IridiumGolem.WALK_BASE.get(), RawAnimation.begin().thenPlay("animation.iridium_golem.start_walk"), 17, (ticks) -> 0f));
        public static final Supplier<TransitionAction> WALK_BASE_TO_IDLE_BASE = INSTANCE.register("iridium_golem_walk_base_to_idle_base", () -> new TransitionAction(ActionRegistry.IridiumGolem.WALK_BASE.get(), ActionRegistry.IridiumGolem.IDLE_BASE.get(), RawAnimation.begin().thenPlay("animation.iridium_golem.stop_walk"), 15, (ticks) -> 1f));
        public static final Supplier<TransitionAction> IDLE_BASE_TO_AGRO_BASE = INSTANCE.register("iridium_golem_idle_base_to_agro_base", () -> new TransitionAction(ActionRegistry.IridiumGolem.IDLE_BASE.get(), ActionRegistry.IridiumGolem.AGRO_BASE.get(), RawAnimation.begin().thenPlay("animation.iridium_golem.agro"), 28, (ticks) -> 1f));
        public static final Supplier<TransitionAction> AGRO_BASE_TO_IDLE_BASE = INSTANCE.register("iridium_golem_agro_base_to_idle_base", () -> new TransitionAction(ActionRegistry.IridiumGolem.AGRO_BASE.get(), ActionRegistry.IridiumGolem.IDLE_BASE.get(), RawAnimation.begin().thenPlay("animation.iridium_golem.deagro"), 42, (ticks) -> 1f));
        public static final Supplier<TransitionAction> IDLE_BASE_TO_RUN_BASE = INSTANCE.register("iridium_golem_idle_base_to_run_base", () -> new TransitionAction(ActionRegistry.IridiumGolem.IDLE_BASE.get(), ActionRegistry.IridiumGolem.RUN_BASE.get(), RawAnimation.begin().thenPlay("animation.iridium_golem.start_run_from_idle"), 42, (ticks) -> 0f));
        public static final Supplier<TransitionAction> RUN_BASE_TO_IDLE_BASE = INSTANCE.register("iridium_golem_run_base_to_idle_base", () -> new TransitionAction(ActionRegistry.IridiumGolem.RUN_BASE.get(), ActionRegistry.IridiumGolem.IDLE_BASE.get(), RawAnimation.begin().thenPlay("animation.iridium_golem.deagro"), 42, (ticks) -> 1f));
        public static final Supplier<TransitionAction> AGRO_BASE_TO_WALK_BASE = INSTANCE.register("iridium_golem_agro_base_to_walk_base", () -> new TransitionAction(ActionRegistry.IridiumGolem.AGRO_BASE.get(), ActionRegistry.IridiumGolem.WALK_BASE.get(), RawAnimation.begin().thenPlay("animation.iridium_golem.deagro").thenPlay("animation.iridium_golem.start_walk"), 59, (ticks) -> 0f));
        public static final Supplier<TransitionAction> RUN_BASE_TO_AGRO_BASE = INSTANCE.register("iridium_golem_run_base_to_agro_base", () -> new TransitionAction(ActionRegistry.IridiumGolem.RUN_BASE.get(), ActionRegistry.IridiumGolem.AGRO_BASE.get(), RawAnimation.begin().thenPlay("animation.iridium_golem.stop_run"), 26, (ticks) -> 1f));
        public static final Supplier<TransitionAction> AGRO_BASE_TO_RUN_BASE = INSTANCE.register("iridium_golem_agro_base_to_run_base", () -> new TransitionAction(ActionRegistry.IridiumGolem.AGRO_BASE.get(), ActionRegistry.IridiumGolem.RUN_BASE.get(), RawAnimation.begin().thenPlay("animation.iridium_golem.start_run"), 13, (ticks) -> 1f));
        public static final Supplier<TransitionAction> WALK_BASE_TO_AGRO_BASE = INSTANCE.register("iridium_golem_walk_base_to_agro_base", () -> new TransitionAction(ActionRegistry.IridiumGolem.WALK_BASE.get(), ActionRegistry.IridiumGolem.AGRO_BASE.get(), RawAnimation.begin().thenPlay("animation.iridium_golem.stop_walk").thenPlay("animation.iridium_golem.agro"), 43, (ticks) -> 1f));
        public static final Supplier<TransitionAction> WALK_BASE_TO_RUN_BASE = INSTANCE.register("iridium_golem_walk_base_to_run_base", () -> new TransitionAction(ActionRegistry.IridiumGolem.WALK_BASE.get(), ActionRegistry.IridiumGolem.RUN_BASE.get(), RawAnimation.begin().thenPlay("animation.iridium_golem.stop_walk").thenPlay("animation.iridium_golem.start_run_from_idle"), 57, (ticks) -> 1f));

        public static void init() {
        }
    }

    public static class BlazeSentry {
        public static final Supplier<TransitionAction> AGRO_BASE_TO_SHOOT_BASE = INSTANCE.register("blaze_sentry_agro_base_to_shoot_base", () -> new TransitionAction(ActionRegistry.BlazeSentry.AGRO_BASE.get(), ActionRegistry.BlazeSentry.SHOOT_BASE.get(), RawAnimation.begin().thenPlay("animation.blaze_sentry.start_shooting"), 60, (ticks) -> 1f));
        public static final Supplier<TransitionAction> SHOOT_BASE_TO_AGRO_BASE = INSTANCE.register("blaze_sentry_shoot_base_to_agro_base", () -> new TransitionAction(ActionRegistry.BlazeSentry.SHOOT_BASE.get(), ActionRegistry.BlazeSentry.AGRO_BASE.get(), RawAnimation.begin().thenPlay("animation.blaze_sentry.stop_shooting"), 60, (ticks) -> 1f));
        public static final Supplier<TransitionAction> AGRO_BASE_TO_SHOOT_BIG_BASE = INSTANCE.register("blaze_sentry_agro_base_to_shoot_big_base", () -> new TransitionAction(ActionRegistry.BlazeSentry.AGRO_BASE.get(), ActionRegistry.BlazeSentry.SHOOT_BIG_BASE.get(), RawAnimation.begin().thenPlay("animation.blaze_sentry.start_shooting"), 60, (ticks) -> 1f));
        public static final Supplier<TransitionAction> SHOOT_BIG_BASE_TO_AGRO_BASE = INSTANCE.register("blaze_sentry_shoot_big_base_to_agro_base", () -> new TransitionAction(ActionRegistry.BlazeSentry.SHOOT_BIG_BASE.get(), ActionRegistry.BlazeSentry.AGRO_BASE.get(), RawAnimation.begin().thenPlay("animation.blaze_sentry.stop_shooting"), 60, (ticks) -> 1f));
        public static final Supplier<TransitionAction> IDLE_BASE_TO_AGRO_BASE = INSTANCE.register("blaze_sentry_idle_base_to_agro_base", () -> new TransitionAction(ActionRegistry.BlazeSentry.IDLE_BASE.get(), ActionRegistry.BlazeSentry.AGRO_BASE.get(), RawAnimation.begin().thenPlay("animation.blaze_sentry.idle_to_agro"), 60, (ticks) -> 1f));
        public static final Supplier<TransitionAction> AGRO_BASE_TO_IDLE_BASE = INSTANCE.register("blaze_sentry_agro_base_to_idle_base", () -> new TransitionAction(ActionRegistry.BlazeSentry.AGRO_BASE.get(), ActionRegistry.BlazeSentry.IDLE_BASE.get(), RawAnimation.begin().thenPlay("animation.blaze_sentry.agro_to_idle"), 60, (ticks) -> 1f));
        public static final Supplier<TransitionAction> SHOOT_BASE_TO_IDLE_BASE = INSTANCE.register("blaze_sentry_shoot_base_to_idle_base", () -> new TransitionAction(ActionRegistry.BlazeSentry.SHOOT_BASE.get(), ActionRegistry.BlazeSentry.IDLE_BASE.get(), RawAnimation.begin().thenPlay("animation.blaze_sentry.shooting_to_idle"), 60, (ticks) -> 1f));

        public static void init() {
        }
    }

    public static Registry<TransitionAction> registry() {
        return INSTANCE.backingRegistry();
    }
}
