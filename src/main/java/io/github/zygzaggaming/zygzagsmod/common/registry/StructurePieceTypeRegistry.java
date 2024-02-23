package io.github.zygzaggaming.zygzagsmod.common.registry;

import io.github.zygzaggaming.zygzagsmod.common.registry.base.AkomiRegistry;
import io.github.zygzaggaming.zygzagsmod.common.structure.CairnArrangementStructure;
import io.github.zygzaggaming.zygzagsmod.common.structure.IridiumEndIslandStructure;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

import static io.github.zygzaggaming.zygzagsmod.common.Main.MODID;

public class StructurePieceTypeRegistry extends AkomiRegistry<StructurePieceType> {
    public static final StructurePieceTypeRegistry INSTANCE = new StructurePieceTypeRegistry(DeferredRegister.create(BuiltInRegistries.STRUCTURE_PIECE, MODID));

    public StructurePieceTypeRegistry(DeferredRegister<StructurePieceType> register) {
        super(register);
    }

    public static final Supplier<StructurePieceType> IRIDIUM_END_ISLAND = INSTANCE.register(
            "iridium_end_island",
            () -> (StructurePieceType.ContextlessType) IridiumEndIslandStructure.IridiumEndIslandPiece::new
    );

    public static final Supplier<StructurePieceType> CAIRN_ARRANGEMENT = INSTANCE.register(
            "cairn_arrangement",
            () -> CairnArrangementStructure.CairnArrangementStructurePiece::new
    );
}
