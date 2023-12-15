package com.zygzag.zygzagsmod.common.registry;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

import static com.zygzag.zygzagsmod.common.Main.MODID;

public class PaintingVariantRegistry extends AkomiRegistry<PaintingVariant> {
    public static final PaintingVariantRegistry INSTANCE = new PaintingVariantRegistry(DeferredRegister.create(BuiltInRegistries.PAINTING_VARIANT, MODID));

    public static final Supplier<PaintingVariant> GOLEM = INSTANCE.register(
            "golem",
            () -> new PaintingVariant(64, 64)
    );

    public PaintingVariantRegistry(DeferredRegister<PaintingVariant> register) {
        super(register);
    }
}
