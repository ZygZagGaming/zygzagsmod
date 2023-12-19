package com.zygzag.zygzagsmod.client.renderer;

import com.zygzag.zygzagsmod.client.gecko.geomodel.RedNetherBrickCacheGeoModel;
import com.zygzag.zygzagsmod.common.block.entity.CacheBlockEntity;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class CacheRenderer extends GeoBlockRenderer<CacheBlockEntity> {

    public CacheRenderer(BlockEntityRendererProvider.Context context) {
        super(new RedNetherBrickCacheGeoModel());
    }
}