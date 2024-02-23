package io.github.zygzaggaming.zygzagsmod.common.registry;

import io.github.zygzaggaming.zygzagsmod.common.registry.base.AkomiRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.neoforged.neoforge.common.util.DeferredSoundType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

import static io.github.zygzaggaming.zygzagsmod.common.Main.MODID;

public class SoundEventRegistry extends AkomiRegistry<SoundEvent> {
    public static final SoundEventRegistry INSTANCE = new SoundEventRegistry(DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, MODID));

    public static final Supplier<SoundEvent> END_SAND_BREAK = INSTANCE.register(
            "end_sand_break",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(MODID, "end_sand_break"))
    );
    public static final DeferredSoundType END_SAND = new DeferredSoundType(
            1.0F,
            1.0F,
            END_SAND_BREAK,
            () -> SoundEvents.SAND_STEP,
            () -> SoundEvents.SAND_PLACE,
            () -> SoundEvents.SAND_HIT,
            () -> SoundEvents.SAND_FALL
    );
    public static final Supplier<SoundEvent> END_SAND_DISSIPATE = makeSimpleSoundEvent("end_sand_dissipate");
    public static final Supplier<SoundEvent> END_SAND_REFORM = makeSimpleSoundEvent("end_sand_reform");
    public static final Supplier<SoundEvent> SCULK_JAW_CLOSE = makeSimpleSoundEvent("sculk_jaw_close");
    public static final Supplier<SoundEvent> CACHE_OPEN = makeSimpleSoundEvent("cache_open");
    public static final Supplier<SoundEvent> CACHE_CLOSE = makeSimpleSoundEvent("cache_close");

    public SoundEventRegistry(DeferredRegister<SoundEvent> register) {
        super(register);
    }

    public static Supplier<SoundEvent> makeSimpleSoundEvent(String id) {
        return makeSimpleSoundEvent(id, new ResourceLocation(MODID, id));
    }
    public static Supplier<SoundEvent> makeSimpleSoundEvent(String id, ResourceLocation soundLocation) {
        return INSTANCE.register(
                id,
                () -> SoundEvent.createVariableRangeEvent(soundLocation)
        );
    }
}
