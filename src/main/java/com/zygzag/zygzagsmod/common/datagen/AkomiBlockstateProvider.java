package com.zygzag.zygzagsmod.common.datagen;

import com.zygzag.zygzagsmod.common.registry.BlockItemEntityRegistry;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

import static com.zygzag.zygzagsmod.common.Main.MODID;
import static com.zygzag.zygzagsmod.common.block.MagmaticNetherBrickBlock.PULSE;

public class AkomiBlockstateProvider extends BlockStateProvider {
    public AkomiBlockstateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        var builder = getVariantBuilder(BlockItemEntityRegistry.MAGMATIC_NETHER_BRICKS.getBlock());
        for (int i = 0; i < 104; i++) {
            var partialState = builder.partialState().with(PULSE, i);
            builder.setModels(partialState, partialState.modelForState().modelFile(models().cubeAll("magmatic_nether_bricks_" + (i % 4), new ResourceLocation(MODID, "block/magmatic_nether_brick/magmatic_nether_bricks_" + (i % 4)))).buildLast());
        }
    }
}
