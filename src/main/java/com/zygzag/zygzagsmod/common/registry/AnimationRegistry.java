package com.zygzag.zygzagsmod.common.registry;

import com.zygzag.zygzagsmod.common.entity.animation.Animation;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;
import software.bernie.geckolib.core.animation.RawAnimation;

import java.util.function.Supplier;

import static com.zygzag.zygzagsmod.common.Main.MODID;

public class AnimationRegistry extends Registry<Animation> {
    public static final AnimationRegistry INSTANCE = new AnimationRegistry(DeferredRegister.create(new ResourceLocation(MODID, "animation"), MODID));
    public static final Supplier<IForgeRegistry<Animation>> FORGE_REGISTRY_SUPPLIER = INSTANCE.register.makeRegistry(RegistryBuilder::new);

    public AnimationRegistry(DeferredRegister<Animation> register) {
        super(register);
    }

    @Override
    public void registerTo(IEventBus bus) {
        IridiumGolem.init(); // make the IridiumGolem class load, subclasses don't load unless they're used
        BlazeSentry.init();
        super.registerTo(bus);
    }

    public static class IridiumGolem {
        public static final RegistryObject<Animation> IDLE_BASE = INSTANCE.register("iridium_golem_idle_base", () -> new Animation(RawAnimation.begin().thenPlay("animation.iridium_golem.idle_loop"), 160));
        public static final RegistryObject<Animation> WALK_BASE = INSTANCE.register("iridium_golem_walk_base", () -> new Animation(RawAnimation.begin().thenPlay("animation.iridium_golem.walk_cycle"), 33));
        public static final RegistryObject<Animation> RUN_BASE = INSTANCE.register("iridium_golem_run_base", () -> new Animation(RawAnimation.begin().thenPlay("animation.iridium_golem.run_cycle"), 20));
        public static final RegistryObject<Animation> AGRO_BASE = INSTANCE.register("iridium_golem_agro_base", () -> new Animation(RawAnimation.begin().thenPlay("animation.iridium_golem.agro_idle"), 16));
        public static final RegistryObject<Animation> ATTACK_SMASH = INSTANCE.register("iridium_golem_attack_smash", () -> new Animation(RawAnimation.begin().thenPlay("animation.iridium_golem.attack2"), 48));
        public static final RegistryObject<Animation> IDLE_1 = INSTANCE.register("iridium_golem_idle_1", () -> new Animation(RawAnimation.begin().thenPlay("animation.iridium_golem.idle1"), 0, false));
        public static final RegistryObject<Animation> IDLE_2 = INSTANCE.register("iridium_golem_idle_2", () -> new Animation(RawAnimation.begin().thenPlay("animation.iridium_golem.idle2"), 0, false));
        public static final RegistryObject<Animation> IDLE_3 = INSTANCE.register("iridium_golem_idle_3", () -> new Animation(RawAnimation.begin().thenPlay("animation.iridium_golem.idle3"), 0, false));

        public static void init() {
        }
    }

    public static class BlazeSentry {
        public static final RegistryObject<Animation> IDLE_BASE = INSTANCE.register("blaze_sentry_idle_base", () -> new Animation(RawAnimation.begin().thenPlay("animation.blaze_sentry.idle_rotation"), 60));
        public static final RegistryObject<Animation> SHOOT_BASE = INSTANCE.register("blaze_sentry_shoot_base", () -> new Animation(RawAnimation.begin().thenPlay("animation.blaze_sentry.shooting_loop"), 60));

        public static void init() {
        }
    }
}
