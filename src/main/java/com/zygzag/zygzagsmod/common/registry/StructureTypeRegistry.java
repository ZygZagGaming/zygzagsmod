package com.zygzag.zygzagsmod.common.registry;


import com.zygzag.zygzagsmod.common.structure.IridiumEndIslandStructure;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import static com.zygzag.zygzagsmod.common.Main.MODID;

public class StructureTypeRegistry extends Registry<StructureType<?>> {
    public static final StructureTypeRegistry INSTANCE = new StructureTypeRegistry(DeferredRegister.create(Registries.STRUCTURE_TYPE, MODID));
    public StructureTypeRegistry(DeferredRegister<StructureType<?>> register) {
        super(register);
    }

    public static final RegistryObject<StructureType<IridiumEndIslandStructure>> IRIDIUM_END_ISLAND = INSTANCE.register(
            "iridium_end_island",
            () -> () -> IridiumEndIslandStructure.CODEC
    );
}
