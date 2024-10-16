package io.github.zygzaggaming.zygzagsmod.common.registry;

import com.mojang.serialization.Codec;
import io.github.zygzaggaming.zygzagsmod.common.entity.FlailProjectile;
import io.github.zygzaggaming.zygzagsmod.common.registry.base.AkomiRegistry;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.Optional;
import java.util.function.Supplier;

import static io.github.zygzaggaming.zygzagsmod.common.Main.MODID;

public class AttachmentTypeRegistry extends AkomiRegistry<AttachmentType<?>> {
    public static final AttachmentTypeRegistry INSTANCE = new AttachmentTypeRegistry(DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, MODID));

    public static final Supplier<AttachmentType<Double>> ITEM_ENTITY_BULK_SMELTING = INSTANCE.register(
            "item_entity_bulk_smelting",
            () -> AttachmentType.builder(() -> 0.0).serialize(Codec.DOUBLE).build()
    );

    public static final Supplier<AttachmentType<Integer>> LIVING_ENTITY_OVERHEAT = INSTANCE.register(
            "living_entity_overheat",
            () -> AttachmentType.builder(() -> 0).serialize(Codec.INT).build()
    );

    public static final Supplier<AttachmentType<Optional<FlailProjectile>>> PLAYER_FLAIL = INSTANCE.register(
            "player_flail",
            () -> AttachmentType.<Optional<FlailProjectile>>builder(Optional::empty).build()
    );

    public AttachmentTypeRegistry(DeferredRegister<AttachmentType<?>> register) {
        super(register);
    }
}
