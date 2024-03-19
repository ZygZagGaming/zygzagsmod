package io.github.zygzaggaming.zygzagsmod.common.registry;

import io.github.zygzaggaming.zygzagsmod.common.item.FlailItem;
import io.github.zygzaggaming.zygzagsmod.common.item.TransmutationCharge;
import io.github.zygzaggaming.zygzagsmod.common.item.iridium.Socket;
import io.github.zygzaggaming.zygzagsmod.common.registry.base.AkomiRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import oshi.util.tuples.Pair;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static io.github.zygzaggaming.zygzagsmod.common.Main.MODID;

public class ItemRegistry extends AkomiRegistry<Item> {
    public static final ItemRegistry INSTANCE = new ItemRegistry(DeferredRegister.create(Registries.ITEM, MODID));
    public static final Supplier<Item> IRIDIUM_PLATING = basicItem("iridium_plating");
    public static final Supplier<Item> TRANSMUTATION_CHARGE = INSTANCE.register(
            "transmutation_charge",
            () -> new TransmutationCharge(new Item.Properties())
    );
    public static final Supplier<Item> FLAIL = INSTANCE.register(
            "flail",
            () -> new FlailItem(7.0, -2.5, 4.0, new Item.Properties())
    );
    public static final Supplier<Item> RAW_IRIDIUM = basicItem("raw_iridium");
    public static final Map<IridiumGearType, Supplier<Item>> IRIDIUM_GEAR_BLUEPRINTS = Arrays.stream(IridiumGearType.values())
            .map(s -> new Pair<>(s, basicItem("iridium_" + s.lowerName() + "_blueprint")))
            .collect(Collectors.toMap(Pair::getA, Pair::getB));

    public static final Map<Socket, Supplier<Item>> SOCKET_SCHEMATICS = Arrays.stream(Socket.values())
            .map(s -> new Pair<>(s, basicItem(s.lowerName() + "_socket_schematic")))
            .collect(Collectors.toMap(Pair::getA, Pair::getB));
    public static final Supplier<Item> IRIDIUM_GOLEM_SPAWN_EGG = INSTANCE.register("iridium_golem_spawn_egg", () -> new DeferredSpawnEggItem(EntityTypeRegistry.IRIDIUM_GOLEM, 0x78bbd6, 0xcfcfcf, new Item.Properties()));
    public static final Supplier<Item> BLAZE_SENTRY_SPAWN_EGG = INSTANCE.register("blaze_sentry_spawn_egg", () -> new DeferredSpawnEggItem(EntityTypeRegistry.BLAZE_SENTRY, 0xc79002, 0xc7c263, new Item.Properties()));
    public ItemRegistry(DeferredRegister<Item> register) {
        super(register);
    }

    private static Supplier<Item> basicItem(String id) {
        return INSTANCE.register(id, () -> new Item(new Item.Properties()));
    }
}
