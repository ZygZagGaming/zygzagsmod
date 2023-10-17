package com.zygzag.zygzagsmod.common.registry;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static com.zygzag.zygzagsmod.common.Main.MODID;

public class BlockEntityRegistry extends Registry<BlockEntityType<?>> {
    public static final BlockEntityRegistry INSTANCE = new BlockEntityRegistry(DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, MODID));

    public BlockEntityRegistry(DeferredRegister<BlockEntityType<?>> register) {
        super(register);
    }
}
