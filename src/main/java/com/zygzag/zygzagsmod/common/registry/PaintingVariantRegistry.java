package com.zygzag.zygzagsmod.common.registry;

import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.zygzag.zygzagsmod.common.Main.MODID;

public class PaintingVariantRegistry extends Registry<PaintingVariant> {
    public static final PaintingVariantRegistry INSTANCE = new PaintingVariantRegistry(DeferredRegister.create(ForgeRegistries.PAINTING_VARIANTS, MODID));

    public static final RegistryObject<PaintingVariant> GOLEM = INSTANCE.register(
            "golem",
            () -> new PaintingVariant(64, 64)
    );

    public PaintingVariantRegistry(DeferredRegister<PaintingVariant> register) {
        super(register);
    }
}