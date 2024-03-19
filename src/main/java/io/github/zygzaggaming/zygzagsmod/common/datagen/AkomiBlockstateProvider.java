package io.github.zygzaggaming.zygzagsmod.common.datagen;

import io.github.zygzaggaming.zygzagsmod.common.registry.BlockWithItemRegistry;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import static io.github.zygzaggaming.zygzagsmod.common.Main.MODID;

public class AkomiBlockstateProvider extends BlockStateProvider {
    private final ExistingFileHelper helper;
    public AkomiBlockstateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, MODID, exFileHelper);
        helper = exFileHelper;
    }

    @Override
    protected void registerStatesAndModels() {
//        var builder = getVariantBuilder(BlockItemEntityRegistry.MAGMATIC_NETHER_BRICKS.getBlock());
//        for (int i = 0; i < 104; i++) {
//            var partialState = builder.partialState().with(PULSE, i);
//            builder.setModels(partialState, partialState.modelForState().modelFile(models().cubeAll("magmatic_nether_bricks_" + (i % 4), new ResourceLocation(MODID, "block/magmatic_nether_brick/magmatic_nether_bricks_" + (i % 4)))).buildLast());
//        }
        makeSimpleBlockExistingModel(BlockWithItemRegistry.CORONAL_AMBER.getBlock(), "coronal_amber");
    }

    private void makeSimpleBlockExistingModel(Block block, String id) {
        var builder = getVariantBuilder(block);
        var partialState = builder.partialState();
        builder.setModels(partialState, partialState.modelForState().modelFile(new ModelFile.ExistingModelFile(new ResourceLocation(MODID, "block/" + id), helper)).buildLast());
    }
}
