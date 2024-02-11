package com.zygzag.zygzagsmod.common.registry;

import com.zygzag.zygzagsmod.common.registry.base.AkomiRegistry;
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
    public static final Supplier<PaintingVariant> HIVELY = INSTANCE.register(
            "hively",
            () -> new PaintingVariant(16, 32)
    );

    public PaintingVariantRegistry(DeferredRegister<PaintingVariant> register) {
        super(register);
    }
}
