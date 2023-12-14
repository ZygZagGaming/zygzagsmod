package com.zygzag.zygzagsmod.common.registry;

import com.zygzag.zygzagsmod.common.Main;
import com.zygzag.zygzagsmod.common.block.*;
import com.zygzag.zygzagsmod.common.block.entity.GlassGroupBlock;
import com.zygzag.zygzagsmod.common.registry.object.BlockWithItemRegistryObject;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

import static com.zygzag.zygzagsmod.common.Main.MODID;

public class BlockWithItemRegistry {
    public static final BlockWithItemRegistry INSTANCE = new BlockWithItemRegistry();
    public static final BlockWithItemRegistryObject<Block, BlockItem> RAW_IRIDIUM_BLOCK = INSTANCE.register(
            "raw_iridium_block",
            () -> new Block(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.RAW_IRON)
                            .instrument(NoteBlockInstrument.BASEDRUM)
                            .requiresCorrectToolForDrops()
                            .strength(7.0F, 9.0F)
            )
    );
    public static final BlockWithItemRegistryObject<EndStoneSwitchBlock, BlockItem> END_STONE_SWITCH = INSTANCE.register(
            "end_stone_switch",
            () -> new EndStoneSwitchBlock(BlockBehaviour.Properties.copy(Blocks.END_STONE_BRICKS))
    );
    public static final BlockWithItemRegistryObject<EndSandBlock, BlockItem> END_SAND = INSTANCE.register(
            "end_sand",
            () -> new EndSandBlock(BlockBehaviour.Properties.copy(Blocks.SAND).sound(SoundEventRegistry.END_SAND))
    );
    public static final BlockWithItemRegistryObject<Block, BlockItem> IRIDIUM_END_SAND = INSTANCE.register(
            "iridium_end_sand",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.SAND))
    );
    public static final BlockWithItemRegistryObject<Block, BlockItem> CHISELED_RED_NETHER_BRICKS = INSTANCE.register(
            "chiseled_red_nether_bricks",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.RED_NETHER_BRICKS))
    );
    public static final BlockWithItemRegistryObject<Block, BlockItem> CRACKED_RED_NETHER_BRICKS = INSTANCE.register(
            "cracked_red_nether_bricks",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.RED_NETHER_BRICKS))
    );
    public static final BlockWithItemRegistryObject<Block, BlockItem> NETHER_QUARTZ_GLASS = INSTANCE.register(
            "nether_quartz_glass",
            () -> new GlassBlock(BlockBehaviour.Properties.copy(Blocks.QUARTZ_BLOCK).noOcclusion().isValidSpawn(BlockWithItemRegistry::never).isRedstoneConductor(BlockWithItemRegistry::never).isSuffocating(BlockWithItemRegistry::never).isViewBlocking(BlockWithItemRegistry::never))
    );
    public static final BlockWithItemRegistryObject<Block, BlockItem> NETHER_QUARTZ_GLASS_STAIRS = INSTANCE.register(
            "nether_quartz_glass_stairs",
            () -> new GlassStairBlock(NETHER_QUARTZ_GLASS::getDefaultBlockState, BlockBehaviour.Properties.copy(Blocks.QUARTZ_BLOCK).noOcclusion().isValidSpawn(BlockWithItemRegistry::never).isRedstoneConductor(BlockWithItemRegistry::never).isSuffocating(BlockWithItemRegistry::never).isViewBlocking(BlockWithItemRegistry::never))
    );
    public static final BlockWithItemRegistryObject<Block, BlockItem> NETHER_QUARTZ_GLASS_SLAB = INSTANCE.register(
            "nether_quartz_glass_slab",
            () -> new GlassSlabBlock(BlockBehaviour.Properties.copy(Blocks.QUARTZ_BLOCK).noOcclusion().isValidSpawn(BlockWithItemRegistry::never).isRedstoneConductor(BlockWithItemRegistry::never).isSuffocating(BlockWithItemRegistry::never).isViewBlocking(BlockWithItemRegistry::never))
    );
    public DeferredRegister<Block> blockRegister = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    public DeferredRegister<Item> itemRegister = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);

    public <B extends Block> BlockWithItemRegistryObject<B, BlockItem> register(String id, Supplier<B> blockSupplier, Item.Properties properties) {
        //Main.LOGGER.debug("registered block with id " + id);
        var blockRegObj = blockRegister.register(id, blockSupplier);
        return new BlockWithItemRegistryObject<>(
                new ResourceLocation(MODID, id),
                blockRegObj,
                itemRegister.register(id, () -> new BlockItem(blockRegObj.get(), properties))
        );
    }

    public <B extends Block> BlockWithItemRegistryObject<B, BlockItem> register(String id, Supplier<B> blockSupplier) {
        return register(id, blockSupplier, new Item.Properties());
    }

    public void registerTo(IEventBus bus) {
        blockRegister.register(bus);
        itemRegister.register(bus);
    }

    private static Boolean never(BlockState state, BlockGetter world, BlockPos pos, EntityType<?> type) {
        return false;
    }

    private static boolean never(BlockState state, BlockGetter world, BlockPos pos) {
        return false;
    }
}