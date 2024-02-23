package io.github.zygzaggaming.zygzagsmod.common.datagen;

import io.github.zygzaggaming.zygzagsmod.common.Main;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class AkomiBlockstateProvider extends BlockStateProvider {
    public AkomiBlockstateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, Main.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
//        var builder = getVariantBuilder(BlockItemEntityRegistry.MAGMATIC_NETHER_BRICKS.getBlock());
//        for (int i = 0; i < 104; i++) {
//            var partialState = builder.partialState().with(PULSE, i);
//            builder.setModels(partialState, partialState.modelForState().modelFile(models().cubeAll("magmatic_nether_bricks_" + (i % 4), new ResourceLocation(MODID, "block/magmatic_nether_brick/magmatic_nether_bricks_" + (i % 4)))).buildLast());
//        }
    }
}
