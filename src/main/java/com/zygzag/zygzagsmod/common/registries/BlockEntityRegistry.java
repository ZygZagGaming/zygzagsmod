package com.zygzag.zygzagsmod.common.registries;

import com.zygzag.zygzagsmod.common.block.entity.SculkJawBlockEntity;
import com.zygzag.zygzagsmod.common.block.entity.TunedAmethystClusterBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.zygzag.zygzagsmod.common.Main.MODID;

public class BlockEntityRegistry extends Registry<BlockEntityType<?>> {
    public static final BlockEntityRegistry INSTANCE = new BlockEntityRegistry(DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, MODID));
    public BlockEntityRegistry(DeferredRegister<BlockEntityType<?>> register) {
        super(register);
    }

    //Probably deprecated since BIER exists now
}
