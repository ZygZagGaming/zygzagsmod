package com.zygzag.zygzagsmod.common.registry;

import com.zygzag.zygzagsmod.common.block.BlessedSoilBlock;
import com.zygzag.zygzagsmod.common.block.GlowingSoilBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.zygzag.zygzagsmod.common.Main.MODID;

public class BlockRegistry extends Registry<Block> {
    public static final BlockRegistry INSTANCE = new BlockRegistry(DeferredRegister.create(ForgeRegistries.BLOCKS, MODID));
    private BlockRegistry(DeferredRegister<Block> register) {
        super(register);
    }

    public static final RegistryObject<Block> BLESSED_SOIL = INSTANCE.register("blessed_soil", () -> new BlessedSoilBlock(BlockBehaviour.Properties.copy(Blocks.FARMLAND)));
    public static final RegistryObject<Block> GLOWING_SOIL = INSTANCE.register("glowing_soil", () -> new GlowingSoilBlock(BlockBehaviour.Properties.copy(Blocks.FARMLAND)));
}
