package com.zygzag.zygzagsmod.common.registry;


import com.zygzag.zygzagsmod.common.registry.base.AkomiRegistry;
import com.zygzag.zygzagsmod.common.structure.AkomiJigsawStructure;
import com.zygzag.zygzagsmod.common.structure.CairnArrangementStructure;
import com.zygzag.zygzagsmod.common.structure.IridiumEndIslandStructure;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

import static com.zygzag.zygzagsmod.common.Main.MODID;

public class StructureTypeRegistry extends AkomiRegistry<StructureType<?>> {
    public static final StructureTypeRegistry INSTANCE = new StructureTypeRegistry(DeferredRegister.create(BuiltInRegistries.STRUCTURE_TYPE, MODID));
    public static final Supplier<StructureType<IridiumEndIslandStructure>> IRIDIUM_END_ISLAND = INSTANCE.register(
            "iridium_end_island",
            () -> () -> IridiumEndIslandStructure.CODEC
    );

    public static final Supplier<StructureType<CairnArrangementStructure>> CAIRN_ARRANGEMENT = INSTANCE.register(
            "cairn_arrangement",
            () -> () -> CairnArrangementStructure.CODEC
    );

    public static final Supplier<StructureType<AkomiJigsawStructure>> AKOMI_JIGSAW = INSTANCE.register(
            "akomi_jigsaw",
            () -> () -> AkomiJigsawStructure.CODEC
    );

    public StructureTypeRegistry(DeferredRegister<StructureType<?>> register) {
        super(register);
    }
}
