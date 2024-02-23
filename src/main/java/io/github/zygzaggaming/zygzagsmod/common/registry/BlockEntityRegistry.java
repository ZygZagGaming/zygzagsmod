package io.github.zygzaggaming.zygzagsmod.common.registry;

import io.github.zygzaggaming.zygzagsmod.common.registry.base.AkomiRegistry;
import io.github.zygzaggaming.zygzagsmod.common.block.entity.StructurePlacerBlockEntity;
import io.github.zygzaggaming.zygzagsmod.common.block.entity.TogglableButtonBlockEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

import static io.github.zygzaggaming.zygzagsmod.common.Main.MODID;

public class BlockEntityRegistry extends AkomiRegistry<BlockEntityType<?>> {
    public static final BlockEntityRegistry INSTANCE = new BlockEntityRegistry(DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, MODID));

    public static final Supplier<BlockEntityType<?>> STRUCTURE_PLACER = INSTANCE.register("structure_placer", () ->
            BlockEntityType.Builder.of(StructurePlacerBlockEntity::new, BlockRegistry.STRUCTURE_PLACER.get()).build(null)
    );
    public static final Supplier<BlockEntityType<?>> TOGGLABLE_BUTTON = INSTANCE.register("togglable_button", () ->
            BlockEntityType.Builder.of(TogglableButtonBlockEntity::new, BlockWithItemRegistry.NETHER_BRICK_BUTTON.getBlock(), BlockWithItemRegistry.RED_NETHER_BRICK_BUTTON.getBlock()).build(null)
    );

    public BlockEntityRegistry(DeferredRegister<BlockEntityType<?>> register) {
        super(register);
    }
}
