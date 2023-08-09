package com.zygzag.zygzagsmod.common.registries;

import com.zygzag.zygzagsmod.common.blockentity.CustomBrushableBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.BrushableBlockEntity;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.zygzag.zygzagsmod.common.Main.MODID;

public class BlockEntityRegistry extends Registry<BlockEntityType<?>> {
    public static final BlockEntityRegistry INSTANCE = new BlockEntityRegistry(DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, MODID));
    public BlockEntityRegistry(DeferredRegister<BlockEntityType<?>> register) {
        super(register);
    }

    public static final RegistryObject<BlockEntityType<CustomBrushableBlockEntity>> SUSPICIOUS_END_SAND = INSTANCE.register(
            "suspicious_end_sand_be",
            () -> BlockEntityType.Builder.of(CustomBrushableBlockEntity::new, BlockRegistry.SUSPICIOUS_END_SAND.get()).build(null)
    );
}
