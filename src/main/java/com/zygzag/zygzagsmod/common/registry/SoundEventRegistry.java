package com.zygzag.zygzagsmod.common.registry;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraftforge.common.util.ForgeSoundType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.zygzag.zygzagsmod.common.Main.MODID;

public class SoundEventRegistry extends Registry<SoundEvent> {
    public static final SoundEventRegistry INSTANCE = new SoundEventRegistry(DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, MODID));

    public static final RegistryObject<SoundEvent> END_SAND_BREAK = INSTANCE.register(
            "end_sand_break",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(MODID, "end_sand_break"))
    );
    public static final RegistryObject<SoundEvent> END_SAND_DISSIPATE = INSTANCE.register(
            "end_sand_dissipate",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(MODID, "end_sand_dissipate"))
    );
    public static final RegistryObject<SoundEvent> END_SAND_REFORM = INSTANCE.register(
            "end_sand_reform",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(MODID, "end_sand_reform"))
    );

    public static final ForgeSoundType END_SAND = new ForgeSoundType(
            1.0F,
            1.0F,
            END_SAND_BREAK,
            () -> SoundEvents.SAND_STEP,
            () -> SoundEvents.SAND_PLACE,
            () -> SoundEvents.SAND_HIT,
            () -> SoundEvents.SAND_FALL
    );

    public SoundEventRegistry(DeferredRegister<SoundEvent> register) {
        super(register);
    }
}