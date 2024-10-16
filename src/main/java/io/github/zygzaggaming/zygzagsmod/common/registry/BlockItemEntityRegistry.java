package io.github.zygzaggaming.zygzagsmod.common.registry;

import io.github.zygzaggaming.zygzagsmod.common.block.CacheBlock;
import io.github.zygzaggaming.zygzagsmod.common.block.MagmaticNetherBrickBlock;
import io.github.zygzaggaming.zygzagsmod.common.block.SculkJawBlock;
import io.github.zygzaggaming.zygzagsmod.common.block.SuspiciousEndSandBlock;
import io.github.zygzaggaming.zygzagsmod.common.block.entity.CacheBlockEntity;
import io.github.zygzaggaming.zygzagsmod.common.block.entity.CustomBrushableBlockEntity;
import io.github.zygzaggaming.zygzagsmod.common.block.entity.MagmaticNetherBrickBlockEntity;
import io.github.zygzaggaming.zygzagsmod.common.block.entity.SculkJawBlockEntity;
import io.github.zygzaggaming.zygzagsmod.common.registry.object.BlockItemEntitySupplier;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

import static io.github.zygzaggaming.zygzagsmod.common.Main.MODID;

public class BlockItemEntityRegistry {
    public static final BlockItemEntityRegistry INSTANCE = new BlockItemEntityRegistry();
    public DeferredRegister<Block> blockRegister = DeferredRegister.create(BuiltInRegistries.BLOCK, MODID);
    public DeferredRegister<Item> itemRegister = DeferredRegister.create(BuiltInRegistries.ITEM, MODID);
    public DeferredRegister<BlockEntityType<?>> beRegister = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, MODID);
    
    public static final BlockItemEntitySupplier<SculkJawBlock, BlockItem, SculkJawBlockEntity> SCULK_JAW = INSTANCE.register(
            "sculk_jaw",
            () -> new SculkJawBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SCULK_VEIN)),
            SculkJawBlockEntity::new
    );

    public static final BlockItemEntitySupplier<SuspiciousEndSandBlock, BlockItem, CustomBrushableBlockEntity> SUSPICIOUS_END_SAND = INSTANCE.register(
            "suspicious_end_sand",
            () -> new SuspiciousEndSandBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SAND).pushReaction(PushReaction.DESTROY), SoundEvents.BRUSH_SAND, SoundEvents.BRUSH_SAND_COMPLETED),
            CustomBrushableBlockEntity::new
    );

    public static final BlockItemEntitySupplier<MagmaticNetherBrickBlock, BlockItem, MagmaticNetherBrickBlockEntity> MAGMATIC_NETHER_BRICKS = INSTANCE.register(
            "magmatic_nether_bricks",
            () -> new MagmaticNetherBrickBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.RED_NETHER_BRICKS).strength(60f, 6f)),
            MagmaticNetherBrickBlockEntity::new
    );

    public static final BlockItemEntitySupplier<CacheBlock, BlockItem, CacheBlockEntity> RED_NETHER_BRICK_CACHE = INSTANCE.register(
            "red_nether_brick_cache",
            () -> new CacheBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.RED_NETHER_BRICKS).strength(3f, 6f)),
            CacheBlockEntity::new
    );

    public <B extends Block, E extends BlockEntity> BlockItemEntitySupplier<B, BlockItem, E> register(String id, Supplier<B> blockSupplier, BlockEntityType.BlockEntitySupplier<E> beSupplier) {
        return register(id, blockSupplier, new Item.Properties(), beSupplier);
    }

    public <B extends Block, E extends BlockEntity> BlockItemEntitySupplier<B, BlockItem, E> register(String id, Supplier<B> blockSupplier, Item.Properties properties, BlockEntityType.BlockEntitySupplier<E> beSupplier) {
        var blockRegObj = blockRegister.register(id, blockSupplier);
        return new BlockItemEntitySupplier<>(
                ResourceLocation.fromNamespaceAndPath(MODID,  id),
                blockRegObj,
                itemRegister.register(id, () -> new BlockItem(blockRegObj.get(), properties)),
                beRegister.register(id, () -> BlockEntityType.Builder.of(beSupplier, blockRegObj.get()).build(null))
        );
    }

    public void registerTo(IEventBus bus) {
        blockRegister.register(bus);
        itemRegister.register(bus);
        beRegister.register(bus);
        //Main.LOGGER.debug("registered registry " + this);
    }
}