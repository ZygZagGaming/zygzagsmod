package com.zygzag.zygzagsmod.common.registries;

import com.zygzag.zygzagsmod.common.ModSoundEvents;
import com.zygzag.zygzagsmod.common.block.*;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.PushReaction;
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
    public static final RegistryObject<Block> RAW_IRIDIUM_BLOCK = INSTANCE.register(
            "raw_iridium_block",
            () -> new Block(
                    BlockBehaviour.Properties.of(Material.METAL)
                            .requiresCorrectToolForDrops()
                            .strength(7.0F, 9.0F)
            )
    );
    public static final RegistryObject<Block> END_STONE_SWITCH_BLOCK = INSTANCE.register(
            "end_stone_switch",
            () -> new EndStoneSwitchBlock(BlockBehaviour.Properties.copy(Blocks.END_STONE_BRICKS))
    );
    public static final RegistryObject<Block> END_SAND = INSTANCE.register(
            "end_sand",
            () -> new EndSandBlock(BlockBehaviour.Properties.copy(Blocks.SAND).sound(ModSoundEvents.END_SAND))
    );
    public static final RegistryObject<Block> IRIDIUM_END_SAND = INSTANCE.register(
            "iridium_end_sand",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.SAND))
    );
}
