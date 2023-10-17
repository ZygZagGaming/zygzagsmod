package com.zygzag.zygzagsmod.common.registry;

import com.zygzag.zygzagsmod.common.structure.IridiumEndIslandStructure;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import static com.zygzag.zygzagsmod.common.Main.MODID;

public class StructurePieceTypeRegistry extends Registry<StructurePieceType> {
    public static final StructurePieceTypeRegistry INSTANCE = new StructurePieceTypeRegistry(DeferredRegister.create(Registries.STRUCTURE_PIECE, MODID));

    public StructurePieceTypeRegistry(DeferredRegister<StructurePieceType> register) {
        super(register);
    }

    public static final RegistryObject<StructurePieceType> IRIDIUM_END_ISLAND = INSTANCE.register(
            "iridium_end_island",
            () -> (StructurePieceType.ContextlessType) IridiumEndIslandStructure.IridiumEndIslandPiece::new
    );
}
