package io.github.zygzaggaming.zygzagsmod.common.registry;

import io.github.zygzaggaming.zygzagsmod.common.registry.base.AkomiRegistry;
import io.github.zygzaggaming.zygzagsmod.common.item.TransmutationCharge;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;
import java.util.function.Supplier;

import static io.github.zygzaggaming.zygzagsmod.common.Main.MODID;

public class ItemRegistry extends AkomiRegistry<Item> {
    public static final ItemRegistry INSTANCE = new ItemRegistry(DeferredRegister.create(Registries.ITEM, MODID));
    public static final Supplier<Item> IRIDIUM_PLATING = basicItem("iridium_plating");
    public static final Supplier<Item> TRANSMUTATION_CHARGE = INSTANCE.register(
            "transmutation_charge",
            () -> new TransmutationCharge(new Item.Properties())
    );
    public static final Supplier<Item> RAW_IRIDIUM = basicItem("raw_iridium");
    public static final Supplier<Item> IRIDIUM_HELMET_BLUEPRINT = basicItem("iridium_helmet_blueprint");
    public static final Supplier<Item> IRIDIUM_CHESTPLATE_BLUEPRINT = basicItem("iridium_chestplate_blueprint");
    public static final Supplier<Item> IRIDIUM_LEGGINGS_BLUEPRINT = basicItem("iridium_leggings_blueprint");
    public static final Supplier<Item> IRIDIUM_BOOTS_BLUEPRINT = basicItem("iridium_boots_blueprint");
    public static final Supplier<Item> IRIDIUM_SWORD_BLUEPRINT = basicItem("iridium_sword_blueprint");
    public static final Supplier<Item> IRIDIUM_PICKAXE_BLUEPRINT = basicItem("iridium_pickaxe_blueprint");
    public static final Supplier<Item> IRIDIUM_AXE_BLUEPRINT = basicItem("iridium_axe_blueprint");
    public static final Supplier<Item> IRIDIUM_SHOVEL_BLUEPRINT = basicItem("iridium_shovel_blueprint");
    public static final Supplier<Item> IRIDIUM_HOE_BLUEPRINT = basicItem("iridium_hoe_blueprint");
    public static final List<Supplier<Item>> IRIDIUM_GEAR_BLUEPRINTS = List.of(
            IRIDIUM_HELMET_BLUEPRINT,
            IRIDIUM_CHESTPLATE_BLUEPRINT,
            IRIDIUM_LEGGINGS_BLUEPRINT,
            IRIDIUM_BOOTS_BLUEPRINT,
            IRIDIUM_SWORD_BLUEPRINT,
            IRIDIUM_PICKAXE_BLUEPRINT,
            IRIDIUM_AXE_BLUEPRINT,
            IRIDIUM_SHOVEL_BLUEPRINT,
            IRIDIUM_HOE_BLUEPRINT
    );
    public static final Supplier<Item> DIAMOND_SOCKET_SCHEMATIC = basicItem("diamond_socket_schematic");
    public static final Supplier<Item> EMERALD_SOCKET_SCHEMATIC = basicItem("emerald_socket_schematic");
    public static final Supplier<Item> SKULL_SOCKET_SCHEMATIC = basicItem("skull_socket_schematic");
    public static final Supplier<Item> WITHER_SKULL_SOCKET_SCHEMATIC = basicItem("wither_skull_socket_schematic");
    public static final Supplier<Item> AMETHYST_SOCKET_SCHEMATIC = basicItem("amethyst_socket_schematic");
    public static final List<Supplier<Item>> SOCKET_SCHEMATICS = List.of(
            DIAMOND_SOCKET_SCHEMATIC,
            EMERALD_SOCKET_SCHEMATIC,
            SKULL_SOCKET_SCHEMATIC,
            WITHER_SKULL_SOCKET_SCHEMATIC,
            AMETHYST_SOCKET_SCHEMATIC
    );
    public static final Supplier<Item> IRIDIUM_GOLEM_SPAWN_EGG = INSTANCE.register("iridium_golem_spawn_egg", () -> new DeferredSpawnEggItem(EntityTypeRegistry.IRIDIUM_GOLEM, 0x78bbd6, 0xcfcfcf, new Item.Properties()));
    public static final Supplier<Item> BLAZE_SENTRY_SPAWN_EGG = INSTANCE.register("blaze_sentry_spawn_egg", () -> new DeferredSpawnEggItem(EntityTypeRegistry.BLAZE_SENTRY, 0xc79002, 0xc7c263, new Item.Properties()));
    public ItemRegistry(DeferredRegister<Item> register) {
        super(register);
    }

    private static Supplier<Item> basicItem(String id) {
        return INSTANCE.register(id, () -> new Item(new Item.Properties()));
    }
}
