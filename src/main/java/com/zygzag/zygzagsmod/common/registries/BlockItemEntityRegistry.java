package com.zygzag.zygzagsmod.common.registries;

import com.zygzag.zygzagsmod.common.block.SculkJawBlock;
import com.zygzag.zygzagsmod.common.block.TunedAmethystClusterBlock;
import com.zygzag.zygzagsmod.common.block.entity.SculkJawBlockEntity;
import com.zygzag.zygzagsmod.common.block.entity.TunedAmethystClusterBlockEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
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
            new Item.Properties(),
            SculkJawBlockEntity::new
    );

    public static final BlockItemEntityRegistryObject<TunedAmethystClusterBlock, BlockItem, TunedAmethystClusterBlockEntity> TUNED_AMETHYST_CLUSTER = INSTANCE.register(
            "tuned_amethyst_cluster",
            () -> new TunedAmethystClusterBlock(BlockBehaviour.Properties.of(Material.AMETHYST)),
            new Item.Properties(),
            TunedAmethystClusterBlockEntity::new
    );

    public <B extends Block, E extends BlockEntity> BlockItemEntityRegistryObject<B, BlockItem, E> register(String id, Supplier<B> blockSupplier, Item.Properties properties, BlockEntityType.BlockEntitySupplier<E> beSupplier) {
        var blockRegObj = blockRegister.register(id, blockSupplier);
        return new BlockItemEntityRegistryObject<>(
                new ResourceLocation(MODID, id),
                blockRegObj,
                itemRegister.register(id, () -> new BlockItem(blockRegObj.get(), properties)),
                beRegister.register(id, () -> BlockEntityType.Builder.of(beSupplier, blockSupplier.get()).build(null))
        );
    }

    public BlockItemEntityRegistry() {
        Registry.REGISTRATION_QUEUE.add((bus) -> {
            blockRegister.register(bus);
            itemRegister.register(bus);
            beRegister.register(bus);
        });
    }
}
