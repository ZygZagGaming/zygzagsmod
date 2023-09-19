package com.zygzag.zygzagsmod.common.registry;

import com.zygzag.zygzagsmod.common.item.iridium.PartialGearType;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.DeferredRegister;

import static com.zygzag.zygzagsmod.common.Main.MODID;

public class PartialGearTypeRegistry extends Registry<PartialGearType> {
    public static final PartialGearTypeRegistry INSTANCE = new PartialGearTypeRegistry(DeferredRegister.create(new ResourceLocation(MODID, "partial_gear_type"), MODID));

    public PartialGearTypeRegistry(DeferredRegister<PartialGearType> register) {
        super(register);
    }
}
