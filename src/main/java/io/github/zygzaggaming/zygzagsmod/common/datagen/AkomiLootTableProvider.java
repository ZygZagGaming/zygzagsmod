package io.github.zygzaggaming.zygzagsmod.common.datagen;

import io.github.zygzaggaming.zygzagsmod.common.registry.BlockItemEntityRegistry;
import io.github.zygzaggaming.zygzagsmod.common.registry.BlockRegistry;
import io.github.zygzaggaming.zygzagsmod.common.registry.BlockWithItemRegistry;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.WritableRegistry;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.neoforge.registries.DeferredHolder;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import java.util.stream.Stream;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class AkomiLootTableProvider extends LootTableProvider {
    public AkomiLootTableProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> future) {
        super(output, Set.of(), List.of(new SubProviderEntry(AkomiBlockLoot::new, LootContextParamSets.BLOCK)), future);
    }

    @Override
    protected void validate(WritableRegistry<LootTable> registry, ValidationContext ctx, ProblemReporter.Collector collector) {
        registry.forEach((k) -> k.validate(ctx));
    }

    static class AkomiBlockLoot extends BlockLootSubProvider {
        public static final Supplier<Set<? extends Block>> EXCLUDED = () -> Set.of(
                BlockWithItemRegistry.IRIDIUM_END_SAND.getBlock()
        );

        public static final List<Supplier<? extends Block>> DROP_SELF = List.of(
                BlockWithItemRegistry.NETHER_QUARTZ_GLASS.block(),
                BlockWithItemRegistry.NETHER_QUARTZ_GLASS_SLAB.block(),
                BlockWithItemRegistry.NETHER_QUARTZ_GLASS_STAIRS.block(),
                BlockWithItemRegistry.CHISELED_RED_NETHER_BRICKS.block(),
                BlockWithItemRegistry.CRACKED_RED_NETHER_BRICKS.block(),
                BlockWithItemRegistry.GIRDED_RED_NETHER_BRICKS.block(),
                BlockWithItemRegistry.CRUSHED_RED_NETHER_BRICKS.block(),
                BlockWithItemRegistry.RED_NETHER_BRICK_BUTTON.block(),
                BlockWithItemRegistry.NETHER_BRICK_BUTTON.block(),
                BlockWithItemRegistry.END_SAND.block(),
                BlockWithItemRegistry.END_STONE_SWITCH.block(),
                BlockWithItemRegistry.RAW_IRIDIUM_BLOCK.block(),
                BlockWithItemRegistry.CORONAL_AMBER.block(),
                BlockItemEntityRegistry.RED_NETHER_BRICK_CACHE.block()
        );

        public static final Map<Holder<? extends Block>, Holder<? extends Item>> DROP_OTHER = Map.of(
                BlockRegistry.BLESSED_SOIL, Holder.direct(Items.DIRT),
                BlockRegistry.GLOWING_SOIL, Holder.direct(Items.DIRT),
                BlockItemEntityRegistry.SUSPICIOUS_END_SAND.block(), BlockWithItemRegistry.IRIDIUM_END_SAND.item()
        );

        public static final List<Holder<? extends Block>> DROP_NOTHING = List.of(
                BlockRegistry.STRUCTURE_PLACER
        );


        public static final List<Supplier<? extends Block>> DROP_SILK = List.of(
                BlockItemEntityRegistry.SCULK_JAW.block(),
                BlockItemEntityRegistry.MAGMATIC_NETHER_BRICKS.block()
        );

        public AkomiBlockLoot(HolderLookup.Provider provider) {
            super(Set.of(), FeatureFlags.REGISTRY.allFlags(), provider);
        }

        @Override
        protected void generate() {
            for (var blockSupplier : DROP_SELF) dropSelf(blockSupplier.get());
            for (var blockSupplier : DROP_OTHER.keySet()) dropOther(blockSupplier.value(), DROP_OTHER.get(blockSupplier).value());
            for (var blockSupplier : DROP_NOTHING) add(blockSupplier.value(), noDrop());
            for (var blockSupplier : DROP_SILK) dropWhenSilkTouch(blockSupplier.get());
        }

        @Override
        protected Iterable<Block> getKnownBlocks() {
            Stream<Block> a = BlockRegistry.INSTANCE.register.getEntries().stream().map(DeferredHolder::value);
            Stream<Block> b = BlockWithItemRegistry.INSTANCE.blockRegister.getEntries().stream().map(DeferredHolder::value);
            Stream<Block> c = BlockItemEntityRegistry.INSTANCE.blockRegister.getEntries().stream().map(DeferredHolder::value);
            return Stream.concat(a, Stream.concat(b, c)).filter((it) -> !EXCLUDED.get().contains(it))::iterator;
        }
    }
}
