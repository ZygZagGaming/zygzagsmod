package com.zygzag.zygzagsmod.common.registry;

import com.zygzag.zygzagsmod.common.trigger.MultikillTrigger;
import com.zygzag.zygzagsmod.common.trigger.ThrowSnowballsAtMobTrigger;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.resources.ResourceLocation;

import static com.zygzag.zygzagsmod.common.Main.MODID;

public class CriterionTriggerRegistry {
    public static final MultikillTrigger MULTIKILL = CriteriaTriggers.register(
            new MultikillTrigger(
                    new ResourceLocation(MODID, "multikill")
            )
    );

    public static final ThrowSnowballsAtMobTrigger THROW_SNOWBALLS_AT_MOB = CriteriaTriggers.register(new ThrowSnowballsAtMobTrigger());

    public static void init() { }
}
