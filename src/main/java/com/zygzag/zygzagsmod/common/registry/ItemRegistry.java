package com.zygzag.zygzagsmod.common.registry;

import com.zygzag.zygzagsmod.common.item.TransmutationCharge;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

import static com.zygzag.zygzagsmod.common.Main.MODID;

public class ItemRegistry extends Registry<Item> {
    public static final ItemRegistry INSTANCE = new ItemRegistry(DeferredRegister.create(Registries.ITEM, MODID));
    public static final RegistryObject<Item> IRIDIUM_PLATING = basicItem("iridium_plating");
    public static final RegistryObject<Item> TRANSMUTATION_CHARGE = INSTANCE.register(
            "transmutation_charge",
            () -> new TransmutationCharge(new Item.Properties())
    );
    public static final RegistryObject<Item> RAW_IRIDIUM = basicItem("raw_iridium");
    public static final RegistryObject<Item> IRIDIUM_HELMET_BLUEPRINT = basicItem("iridium_helmet_blueprint");
    public static final RegistryObject<Item> IRIDIUM_CHESTPLATE_BLUEPRINT = basicItem("iridium_chestplate_blueprint");
    public static final RegistryObject<Item> IRIDIUM_LEGGINGS_BLUEPRINT = basicItem("iridium_leggings_blueprint");
    public static final RegistryObject<Item> IRIDIUM_BOOTS_BLUEPRINT = basicItem("iridium_boots_blueprint");
    public static final RegistryObject<Item> IRIDIUM_SWORD_BLUEPRINT = basicItem("iridium_sword_blueprint");
    public static final RegistryObject<Item> IRIDIUM_PICKAXE_BLUEPRINT = basicItem("iridium_pickaxe_blueprint");
    public static final RegistryObject<Item> IRIDIUM_AXE_BLUEPRINT = basicItem("iridium_axe_blueprint");
    public static final RegistryObject<Item> IRIDIUM_SHOVEL_BLUEPRINT = basicItem("iridium_shovel_blueprint");
    public static final RegistryObject<Item> IRIDIUM_HOE_BLUEPRINT = basicItem("iridium_hoe_blueprint");
    public static final List<RegistryObject<Item>> IRIDIUM_GEAR_BLUEPRINTS = List.of(
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
    public static final RegistryObject<Item> DIAMOND_SOCKET_SCHEMATIC = basicItem("diamond_socket_schematic");
    public static final RegistryObject<Item> EMERALD_SOCKET_SCHEMATIC = basicItem("emerald_socket_schematic");
    public static final RegistryObject<Item> SKULL_SOCKET_SCHEMATIC = basicItem("skull_socket_schematic");
    public static final RegistryObject<Item> WITHER_SKULL_SOCKET_SCHEMATIC = basicItem("wither_skull_socket_schematic");
    public static final RegistryObject<Item> AMETHYST_SOCKET_SCHEMATIC = basicItem("amethyst_socket_schematic");
    public static final List<RegistryObject<Item>> SOCKET_SCHEMATICS = List.of(
            DIAMOND_SOCKET_SCHEMATIC,
            EMERALD_SOCKET_SCHEMATIC,
            SKULL_SOCKET_SCHEMATIC,
            WITHER_SKULL_SOCKET_SCHEMATIC,
            AMETHYST_SOCKET_SCHEMATIC
    );
    public static final RegistryObject<Item> IRIDIUM_GOLEM_SPAWN_EGG = INSTANCE.register("iridium_golem_spawn_egg", () -> new ForgeSpawnEggItem(EntityTypeRegistry.IRIDIUM_GOLEM, 0x78bbd6, 0xcfcfcf, new Item.Properties()));
    public static final RegistryObject<Item> BLAZE_SENTRY_SPAWN_EGG = INSTANCE.register("blaze_sentry_spawn_egg", () -> new ForgeSpawnEggItem(EntityTypeRegistry.BLAZE_SENTRY, 0xc79002, 0xc7c263, new Item.Properties()));
    public ItemRegistry(DeferredRegister<Item> register) {
        super(register);
    }

    private static RegistryObject<Item> basicItem(String id) {
        return INSTANCE.register(id, () -> new Item(new Item.Properties()));
    }

    public static RegistryObject<Item> registerBlockItem(RegistryObject<Block> block, Item.Properties properties) {
        return INSTANCE.register(block.getId().getPath(), () -> new BlockItem(block.get(), properties));
    }

    private static RegistryObject<Item> registerBlockItem(RegistryObject<Block> block) {
        return registerBlockItem(block, new Item.Properties());
    }
}
