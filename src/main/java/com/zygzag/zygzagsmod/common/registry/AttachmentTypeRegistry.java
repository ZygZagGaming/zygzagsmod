package com.zygzag.zygzagsmod.common.registry;

import com.mojang.serialization.Codec;
import com.zygzag.zygzagsmod.common.registry.base.AkomiRegistry;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

import static com.zygzag.zygzagsmod.common.Main.MODID;

public class AttachmentTypeRegistry extends AkomiRegistry<AttachmentType<?>> {
    public static final AttachmentTypeRegistry INSTANCE = new AttachmentTypeRegistry(DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, MODID));

    public static final Supplier<AttachmentType<Double>> ITEM_ENTITY_BULK_SMELTING_ATTACHMENT = INSTANCE.register(
            "item_entity_bulk_smelting",
            () -> AttachmentType.builder(() -> 0.0).serialize(Codec.DOUBLE).build()
    );

    public AttachmentTypeRegistry(DeferredRegister<AttachmentType<?>> register) {
        super(register);
    }
}
