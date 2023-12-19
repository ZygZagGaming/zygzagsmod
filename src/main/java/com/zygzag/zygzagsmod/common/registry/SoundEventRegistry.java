package com.zygzag.zygzagsmod.common.registry;

import com.zygzag.zygzagsmod.common.registry.base.AkomiRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.neoforged.neoforge.common.util.DeferredSoundType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

import static com.zygzag.zygzagsmod.common.Main.MODID;

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
    public static final Supplier<SoundEvent> END_SAND_DISSIPATE = INSTANCE.register(
            "end_sand_dissipate",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(MODID, "end_sand_dissipate"))
    );
    public static final Supplier<SoundEvent> END_SAND_REFORM = INSTANCE.register(
            "end_sand_reform",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(MODID, "end_sand_reform"))
    );
    public static final Supplier<SoundEvent> SCULK_JAW_CLOSE = INSTANCE.register(
            "sculk_jaw_close",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(MODID, "sculk_jaw_close"))
    );

    public SoundEventRegistry(DeferredRegister<SoundEvent> register) {
        super(register);
    }
}
