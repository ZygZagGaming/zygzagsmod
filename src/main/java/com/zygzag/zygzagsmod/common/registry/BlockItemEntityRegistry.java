package com.zygzag.zygzagsmod.common.registry;

import com.zygzag.zygzagsmod.common.Main;
import com.zygzag.zygzagsmod.common.block.SculkJawBlock;
import com.zygzag.zygzagsmod.common.block.SuspiciousEndSandBlock;
import com.zygzag.zygzagsmod.common.block.entity.SculkJawBlockEntity;
import com.zygzag.zygzagsmod.common.block.entity.CustomBrushableBlockEntity;
import com.zygzag.zygzagsmod.common.registry.object.BlockItemEntityRegistryObject;
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
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

import static com.zygzag.zygzagsmod.common.Main.MODID;

public class BlockItemEntityRegistry {
    public static final BlockItemEntityRegistry INSTANCE = new BlockItemEntityRegistry();
    public DeferredRegister<Block> blockRegister = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    public DeferredRegister<Item> itemRegister = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    public DeferredRegister<BlockEntityType<?>> beRegister = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, MODID);

    public static final BlockItemEntityRegistryObject<SculkJawBlock, BlockItem, SculkJawBlockEntity> SCULK_JAW = INSTANCE.register(
            "sculk_jaw",
            () -> new SculkJawBlock(BlockBehaviour.Properties.copy(Blocks.SCULK_VEIN)),
            SculkJawBlockEntity::new
    );

//    public static final BlockItemEntityRegistryObject<TunedAmethystClusterBlock, BlockItem, TunedAmethystClusterBlockEntity> TUNED_AMETHYST_CLUSTER = INSTANCE.register(
//            "tuned_amethyst_cluster",
//            () -> new TunedAmethystClusterBlock(BlockBehaviour.Properties.of(Material.AMETHYST)),
//            TunedAmethystClusterBlockEntity::new
//    );

    public static final BlockItemEntityRegistryObject<SuspiciousEndSandBlock, BlockItem, CustomBrushableBlockEntity> SUSPICIOUS_END_SAND = INSTANCE.register(
            "suspicious_end_sand",
            () -> new SuspiciousEndSandBlock(BlockBehaviour.Properties.copy(Blocks.SAND).pushReaction(PushReaction.DESTROY), SoundEvents.BRUSH_SAND, SoundEvents.BRUSH_SAND_COMPLETED),
            CustomBrushableBlockEntity::new
    );

    public <B extends Block, E extends BlockEntity> BlockItemEntityRegistryObject<B, BlockItem, E> register(String id, Supplier<B> blockSupplier, Item.Properties properties, BlockEntityType.BlockEntitySupplier<E> beSupplier) {
        Main.LOGGER.debug("registered block with id " + id);
        var blockRegObj = blockRegister.register(id, blockSupplier);
        return new BlockItemEntityRegistryObject<>(
                new ResourceLocation(MODID, id),
                blockRegObj,
                itemRegister.register(id, () -> new BlockItem(blockRegObj.get(), properties)),
                beRegister.register(id, () -> BlockEntityType.Builder.of(beSupplier, blockRegObj.get()).build(null))
        );
    }

    public <B extends Block, E extends BlockEntity> BlockItemEntityRegistryObject<B, BlockItem, E> register(String id, Supplier<B> blockSupplier, BlockEntityType.BlockEntitySupplier<E> beSupplier) {
        return register(id, blockSupplier, new Item.Properties(), beSupplier);
    }

    public void registerTo(IEventBus bus) {
        blockRegister.register(bus);
        itemRegister.register(bus);
        beRegister.register(bus);
        Main.LOGGER.debug("registered registry " + this);
    }
}