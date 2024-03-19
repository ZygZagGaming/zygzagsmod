package io.github.zygzaggaming.zygzagsmod.client.renderer;

import io.github.zygzaggaming.zygzagsmod.client.gecko.geomodel.RedNetherBrickCacheGeoModel;
import io.github.zygzaggaming.zygzagsmod.common.block.entity.CacheBlockEntity;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class CacheRenderer extends GeoBlockRenderer<CacheBlockEntity> {

    public CacheRenderer(BlockEntityRendererProvider.Context context) {
        super(new RedNetherBrickCacheGeoModel());
    }
}