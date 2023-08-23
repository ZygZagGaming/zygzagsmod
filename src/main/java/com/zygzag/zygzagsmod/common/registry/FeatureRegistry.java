package com.zygzag.zygzagsmod.common.registry;

import com.zygzag.zygzagsmod.common.feature.IridiumEndIslandFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.zygzag.zygzagsmod.common.Main.MODID;

public class FeatureRegistry extends Registry<Feature<?>> {
    public static final FeatureRegistry INSTANCE = new FeatureRegistry(DeferredRegister.create(ForgeRegistries.FEATURES, MODID));
    public FeatureRegistry(DeferredRegister<Feature<?>> register) {
        super(register);
    }

    public static final RegistryObject<IridiumEndIslandFeature> IRIDIUM_END_ISLAND = INSTANCE.register(
            "iridium_end_island",
            () -> new IridiumEndIslandFeature(NoneFeatureConfiguration.CODEC)
    );
}
