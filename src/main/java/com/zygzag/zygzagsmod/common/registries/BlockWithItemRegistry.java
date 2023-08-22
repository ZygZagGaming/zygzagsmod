package com.zygzag.zygzagsmod.common.registries;

import com.zygzag.zygzagsmod.common.block.EndSandBlock;
import com.zygzag.zygzagsmod.common.block.EndStoneSwitchBlock;
import com.zygzag.zygzagsmod.common.block.SculkJawBlock;
import com.zygzag.zygzagsmod.common.block.TunedAmethystClusterBlock;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

import static com.zygzag.zygzagsmod.common.Main.MODID;

public class BlockWithItemRegistry {
    public static final BlockWithItemRegistry INSTANCE = new BlockWithItemRegistry();
    public DeferredRegister<Block> blockRegister = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    public DeferredRegister<Item> itemRegister = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);

    public static final BlockWithItemRegistryObject<Block, BlockItem> RAW_IRIDIUM_BLOCK = INSTANCE.register(
            "raw_iridium_block",
            () -> new Block(
                    BlockBehaviour.Properties.of(Material.METAL)
                            .requiresCorrectToolForDrops()
                            .strength(7.0F, 9.0F)
            ),
            new Item.Properties()
    );

    public static final BlockWithItemRegistryObject<EndStoneSwitchBlock, BlockItem> END_STONE_SWITCH = INSTANCE.register(
            "end_stone_switch",
            () -> new EndStoneSwitchBlock(BlockBehaviour.Properties.copy(Blocks.END_STONE_BRICKS)),
            new Item.Properties()
    );

    public static final BlockWithItemRegistryObject<EndSandBlock, BlockItem> END_SAND = INSTANCE.register(
            "end_sand",
            () -> new EndSandBlock(BlockBehaviour.Properties.copy(Blocks.SAND).sound(SoundEventRegistry.END_SAND)),
            new Item.Properties()
    );

    public static final BlockWithItemRegistryObject<Block, BlockItem> IRIDIUM_END_SAND = INSTANCE.register(
            "iridium_end_sand",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.SAND)),
            new Item.Properties()
    );

    public <B extends Block> BlockWithItemRegistryObject<B, BlockItem> register(String id, Supplier<B> blockSupplier, Item.Properties properties) {
        var blockRegObj = blockRegister.register(id, blockSupplier);
        return new BlockWithItemRegistryObject<>(
                new ResourceLocation(MODID, id),
                blockRegObj,
                itemRegister.register(id, () -> new BlockItem(blockRegObj.get(), properties))
        );
    }

    public BlockWithItemRegistry() {
        Registry.REGISTRATION_QUEUE.add((bus) -> {
            blockRegister.register(bus);
            itemRegister.register(bus);
        });
    }
}
