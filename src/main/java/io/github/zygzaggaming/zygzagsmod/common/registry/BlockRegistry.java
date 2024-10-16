package io.github.zygzaggaming.zygzagsmod.common.registry;

import io.github.zygzaggaming.zygzagsmod.common.block.BlessedSoilBlock;
import io.github.zygzaggaming.zygzagsmod.common.block.GlowingSoilBlock;
import io.github.zygzaggaming.zygzagsmod.common.block.StructurePlacerBlock;
import io.github.zygzaggaming.zygzagsmod.common.registry.base.AkomiRegistry;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

import static io.github.zygzaggaming.zygzagsmod.common.Main.MODID;

public class BlockRegistry extends AkomiRegistry<Block> {
    public static final BlockRegistry INSTANCE = new BlockRegistry(DeferredRegister.create(BuiltInRegistries.BLOCK, MODID));
    public static final Holder<Block> BLESSED_SOIL = INSTANCE.register("blessed_soil", () -> new BlessedSoilBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.FARMLAND)));
    public static final Holder<Block> GLOWING_SOIL = INSTANCE.register("glowing_soil", () -> new GlowingSoilBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.FARMLAND)));
    public static final Holder<Block> STRUCTURE_PLACER = INSTANCE.register("structure_placer", () -> new StructurePlacerBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE)));
    private BlockRegistry(DeferredRegister<Block> register) {
        super(register);
    }

    @Override
    public <P extends Block> DeferredHolder<Block, P> register(String id, Supplier<P> supplier) {
        return super.register(id, supplier);
    }
}
