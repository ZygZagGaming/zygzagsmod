package com.zygzag.zygzagsmod.common.registry;

import com.zygzag.zygzagsmod.common.registry.base.AkomiRegistry;
import com.zygzag.zygzagsmod.common.structure.CairnArrangementStructure;
import com.zygzag.zygzagsmod.common.structure.IridiumEndIslandStructure;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

import static com.zygzag.zygzagsmod.common.Main.MODID;

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
            () -> (StructurePieceType.ContextlessType) CairnArrangementStructure.CairnArrangementStructurePiece::new
    );
}
