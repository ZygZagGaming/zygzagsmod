package com.zygzag.zygzagsmod.common.registry;

import com.zygzag.zygzagsmod.common.entity.animation.TransitionAnimation;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;
import software.bernie.geckolib.core.animation.RawAnimation;

import java.util.function.Supplier;

import static com.zygzag.zygzagsmod.common.Main.MODID;

public class TransitionAnimationRegistry extends Registry<TransitionAnimation> {
    public static final TransitionAnimationRegistry INSTANCE = new TransitionAnimationRegistry(DeferredRegister.create(new ResourceLocation(MODID, "transition_animation"), MODID));
    public static final Supplier<IForgeRegistry<TransitionAnimation>> FORGE_REGISTRY_SUPPLIER = INSTANCE.register.makeRegistry(RegistryBuilder::new);

    public TransitionAnimationRegistry(DeferredRegister<TransitionAnimation> register) {
        super(register);
    }

    @Override
    public void registerTo(IEventBus bus) {
        IridiumGolem.init(); // make the IridiumGolem class load, subclasses don't load unless they're used
        BlazeSentry.init();
        super.registerTo(bus);
    }

    public static class IridiumGolem {
        public static final RegistryObject<TransitionAnimation> IDLE_BASE_TO_WALK_BASE = INSTANCE.register("iridium_golem_idle_base_to_walk_base", () -> new TransitionAnimation(AnimationRegistry.IridiumGolem.IDLE_BASE.get(), AnimationRegistry.IridiumGolem.WALK_BASE.get(), RawAnimation.begin().thenPlay("animation.iridium_golem.start_walk"), 17, (ticks) -> 0f));
        public static final RegistryObject<TransitionAnimation> WALK_BASE_TO_IDLE_BASE = INSTANCE.register("iridium_golem_walk_base_to_idle_base", () -> new TransitionAnimation(AnimationRegistry.IridiumGolem.WALK_BASE.get(), AnimationRegistry.IridiumGolem.IDLE_BASE.get(), RawAnimation.begin().thenPlay("animation.iridium_golem.stop_walk"), 25, (ticks) -> 1f));
        public static final RegistryObject<TransitionAnimation> IDLE_BASE_TO_AGRO_BASE = INSTANCE.register("iridium_golem_idle_base_to_agro_base", () -> new TransitionAnimation(AnimationRegistry.IridiumGolem.IDLE_BASE.get(), AnimationRegistry.IridiumGolem.AGRO_BASE.get(), RawAnimation.begin().thenPlay("animation.iridium_golem.agro"), 28, (ticks) -> 1f));
        public static final RegistryObject<TransitionAnimation> AGRO_BASE_TO_IDLE_BASE = INSTANCE.register("iridium_golem_agro_base_to_idle_base", () -> new TransitionAnimation(AnimationRegistry.IridiumGolem.AGRO_BASE.get(), AnimationRegistry.IridiumGolem.IDLE_BASE.get(), RawAnimation.begin().thenPlay("animation.iridium_golem.deagro"), 42, (ticks) -> 1f));
        public static final RegistryObject<TransitionAnimation> IDLE_BASE_TO_RUN_BASE = INSTANCE.register("iridium_golem_idle_base_to_run_base", () -> new TransitionAnimation(AnimationRegistry.IridiumGolem.IDLE_BASE.get(), AnimationRegistry.IridiumGolem.RUN_BASE.get(), RawAnimation.begin().thenPlay("animation.iridium_golem.start_run_from_idle"), 13, (ticks) -> 0f));
        public static final RegistryObject<TransitionAnimation> RUN_BASE_TO_IDLE_BASE = INSTANCE.register("iridium_golem_run_base_to_idle_base", () -> new TransitionAnimation(AnimationRegistry.IridiumGolem.RUN_BASE.get(), AnimationRegistry.IridiumGolem.IDLE_BASE.get(), RawAnimation.begin().thenPlay("animation.iridium_golem.deagro"), 42, (ticks) -> 1f));
        public static final RegistryObject<TransitionAnimation> AGRO_BASE_TO_WALK_BASE = INSTANCE.register("iridium_golem_agro_base_to_walk_base", () -> new TransitionAnimation(AnimationRegistry.IridiumGolem.AGRO_BASE.get(), AnimationRegistry.IridiumGolem.WALK_BASE.get(), RawAnimation.begin().thenPlay("animation.iridium_golem.deagro").thenPlay("animation.iridium_golem.start_walk"), 59, (ticks) -> 0f));
        public static final RegistryObject<TransitionAnimation> RUN_BASE_TO_AGRO_BASE = INSTANCE.register("iridium_golem_run_base_to_agro_base", () -> new TransitionAnimation(AnimationRegistry.IridiumGolem.RUN_BASE.get(), AnimationRegistry.IridiumGolem.AGRO_BASE.get(), RawAnimation.begin().thenPlay("animation.iridium_golem.stop_run"), 26, (ticks) -> 1f));
        public static final RegistryObject<TransitionAnimation> AGRO_BASE_TO_RUN_BASE = INSTANCE.register("iridium_golem_agro_base_to_run_base", () -> new TransitionAnimation(AnimationRegistry.IridiumGolem.AGRO_BASE.get(), AnimationRegistry.IridiumGolem.RUN_BASE.get(), RawAnimation.begin().thenPlay("animation.iridium_golem.start_run"), 13, (ticks) -> 1f));
        public static final RegistryObject<TransitionAnimation> WALK_BASE_TO_AGRO_BASE = INSTANCE.register("iridium_golem_walk_base_to_agro_base", () -> new TransitionAnimation(AnimationRegistry.IridiumGolem.WALK_BASE.get(), AnimationRegistry.IridiumGolem.AGRO_BASE.get(), RawAnimation.begin().thenPlay("animation.iridium_golem.stop_walk").thenPlay("animation.iridium_golem.agro"), 53, (ticks) -> 1f));
        public static final RegistryObject<TransitionAnimation> WALK_BASE_TO_RUN_BASE = INSTANCE.register("iridium_golem_walk_base_to_run_base", () -> new TransitionAnimation(AnimationRegistry.IridiumGolem.WALK_BASE.get(), AnimationRegistry.IridiumGolem.RUN_BASE.get(), RawAnimation.begin().thenPlay("animation.iridium_golem.stop_walk").thenPlay("animation.iridium_golem.start_run_from_idle"), 38, (ticks) -> 1f));

        public static void init() {
        }
    }

    public static class BlazeSentry {
        public static final RegistryObject<TransitionAnimation> IDLE_BASE_TO_SHOOT_BASE = INSTANCE.register("blaze_sentry_idle_base_to_shoot_base", () -> new TransitionAnimation(AnimationRegistry.BlazeSentry.IDLE_BASE.get(), AnimationRegistry.BlazeSentry.SHOOT_BASE.get(), RawAnimation.begin().thenPlay("animation.blaze_sentry.start_shooting"), 60, (ticks) -> 1f));
        public static final RegistryObject<TransitionAnimation> SHOOT_BASE_TO_IDLE_BASE = INSTANCE.register("blaze_sentry_shoot_base_to_idle_base", () -> new TransitionAnimation(AnimationRegistry.BlazeSentry.SHOOT_BASE.get(), AnimationRegistry.BlazeSentry.IDLE_BASE.get(), RawAnimation.begin().thenPlay("animation.blaze_sentry.stop_shooting"), 60, (ticks) -> 1f));

        public static void init() {
        }
    }
}
