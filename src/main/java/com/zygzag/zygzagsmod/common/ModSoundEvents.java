package com.zygzag.zygzagsmod.common;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraftforge.common.util.ForgeSoundType;

import static com.zygzag.zygzagsmod.common.Main.MODID;

public class ModSoundEvents {
    public static final SoundEvent END_SAND_BREAK = new SoundEvent(new ResourceLocation(MODID, "end_sand_break"));
    public static final SoundEvent END_SAND_DISSIPATE = new SoundEvent(new ResourceLocation(MODID, "end_sand_dissipate"));
    public static final SoundEvent END_SAND_REFORM = new SoundEvent(new ResourceLocation(MODID, "end_sand_reform"));

    public static final SoundEvent[] SOUND_EVENTS = {
            END_SAND_BREAK,
            END_SAND_DISSIPATE,
            END_SAND_REFORM
    };

    public static final ForgeSoundType END_SAND = new ForgeSoundType(
            1.0F,
            1.0F,
            () -> END_SAND_BREAK,
            () -> SoundEvents.SAND_STEP,
            () -> SoundEvents.SAND_PLACE,
            () -> SoundEvents.SAND_HIT,
            () -> SoundEvents.SAND_FALL
    );
}
