package com.zygzag.zygzagsmod.common.registry;

import com.zygzag.zygzagsmod.common.Main;
import com.zygzag.zygzagsmod.common.item.iridium.Socket;
import com.zygzag.zygzagsmod.common.item.iridium.armor.IridiumChestplateItem;
import com.zygzag.zygzagsmod.common.item.iridium.armor.PartialIridiumArmorItem;
import com.zygzag.zygzagsmod.common.item.iridium.tool.*;
import com.zygzag.zygzagsmod.common.item.iridium.tool.partial.PartialIridiumAxeItem;
import com.zygzag.zygzagsmod.common.item.iridium.tool.partial.PartialIridiumHoeItem;
import com.zygzag.zygzagsmod.common.item.iridium.tool.partial.PartialIridiumPickaxeItem;
import com.zygzag.zygzagsmod.common.item.iridium.tool.partial.PartialIridiumSwordItem;
import com.zygzag.zygzagsmod.common.tier.IridiumArmorMaterial;
import com.zygzag.zygzagsmod.common.tier.IridiumToolTier;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

import static com.zygzag.zygzagsmod.common.Main.MODID;

@SuppressWarnings("unused")
public class IridiumGearRegistry extends Registry<Item> {
    public static final IridiumGearRegistry INSTANCE = new IridiumGearRegistry(DeferredRegister.create(ForgeRegistries.ITEMS, MODID));
    public IridiumGearRegistry(DeferredRegister<Item> register) {
        super(register);
    }

    //region iridium pickaxe.json
    public static final RegistryObject<Item> IRIDIUM_PICKAXE = INSTANCE.register(
            "iridium_pickaxe",
            () -> new IridiumPickaxeItem(
                    IridiumToolTier.FULL,
                    1,
                    -2.8F,
                    new Item.Properties().tab(Main.TAB)
                            .fireResistant()
                            .stacksTo(1),
                    Socket.NONE
            )
    );

    public static final RegistryObject<Item> PARTIAL_IRIDIUM_PICKAXE_3_1 = INSTANCE.register(
            "partial_iridium_pickaxe_1",
            () -> new PartialIridiumPickaxeItem(
                    IridiumToolTier._3_1,
                    1,
                    -2.8F,
                    new Item.Properties().tab(Main.TAB)
                            .stacksTo(1),
                    3,
                    1
            )
    );
    public static final RegistryObject<Item> PARTIAL_IRIDIUM_PICKAXE_3_2 = INSTANCE.register(
            "partial_iridium_pickaxe_2",
            () -> new PartialIridiumPickaxeItem(
                    IridiumToolTier._3_2,
                    1,
                    -2.8F,
                    new Item.Properties().tab(Main.TAB)
                            .stacksTo(1),
                    3,
                    2
            )
    );

    public static final RegistryObject<Item> DIAMOND_SOCKETED_IRIDIUM_PICKAXE = INSTANCE.register(
            "diamond_socketed_iridium_pickaxe",
            () -> new IridiumPickaxeItem(
                    IridiumToolTier.DIAMOND_SOCKETED_PICK,
                    1,
                    -2.8F,
                    new Item.Properties().tab(Main.TAB)
                            .fireResistant()
                            .stacksTo(1).
                            craftRemainder(Items.DIAMOND),
                    Socket.DIAMOND
            )
    );
    public static final RegistryObject<Item> EMERALD_SOCKETED_IRIDIUM_PICKAXE = INSTANCE.register(
            "emerald_socketed_iridium_pickaxe",
            () -> new IridiumPickaxeItem(
                    IridiumToolTier.EMERALD_SOCKETED,
                    1,
                    -2.8F,
                    new Item.Properties().tab(Main.TAB)
                            .fireResistant()
                            .stacksTo(1).
                            craftRemainder(Items.EMERALD),
                    Socket.EMERALD
            )
    );
    public static final RegistryObject<Item> SKULL_SOCKETED_IRIDIUM_PICKAXE = INSTANCE.register(
            "skull_socketed_iridium_pickaxe",
            () -> new IridiumPickaxeItem(
                    IridiumToolTier.FULL,
                    1,
                    -2.8F,
                    new Item.Properties().tab(Main.TAB)
                            .fireResistant()
                            .stacksTo(1).
                            craftRemainder(Items.SKELETON_SKULL),
                    Socket.SKULL
            )
    );
    public static final RegistryObject<Item> WITHER_SKULL_SOCKETED_IRIDIUM_PICKAXE = INSTANCE.register(
            "wither_skull_socketed_iridium_pickaxe",
            () -> new IridiumPickaxeItem(
                    IridiumToolTier.WITHER_SOCKETED_PICK,
                    1,
                    -2.8F,
                    new Item.Properties().tab(Main.TAB)
                            .fireResistant()
                            .stacksTo(1).
                            craftRemainder(Items.WITHER_SKELETON_SKULL),
                    Socket.WITHER_SKULL
            )
    );
    public static final RegistryObject<Item> AMETHYST_SOCKETED_IRIDIUM_PICKAXE = INSTANCE.register(
            "amethyst_socketed_iridium_pickaxe",
            () -> new IridiumPickaxeItem(
                    IridiumToolTier.FULL,
                    1,
                    -2.8F,
                    new Item.Properties().tab(Main.TAB)
                            .fireResistant()
                            .stacksTo(1).
                            craftRemainder(Items.AMETHYST_SHARD),
                    Socket.AMETHYST
            )
    );
    //endregion

    //region iridium axe
    public static final RegistryObject<Item> IRIDIUM_AXE = INSTANCE.register(
            "iridium_axe",
            () -> new IridiumAxeItem(
                    IridiumToolTier.FULL,
                    5.0F,
                    -3.0F,
                    new Item.Properties().tab(Main.TAB)
                            .fireResistant()
                            .stacksTo(1),
                    Socket.NONE
            )
    );
    public static final RegistryObject<Item> PARTIAL_IRIDIUM_AXE_4_1 = INSTANCE.register(
            "partial_iridium_axe_1",
            () -> new PartialIridiumAxeItem(
                    IridiumToolTier._3_1,
                    5,
                    -3.0F,
                    new Item.Properties().tab(Main.TAB).stacksTo(1),
                    3,
                    1
            )
    );
    public static final RegistryObject<Item> PARTIAL_IRIDIUM_AXE_4_2 = INSTANCE.register(
            "partial_iridium_axe_2",
            () -> new PartialIridiumAxeItem(
                    IridiumToolTier._3_2,
                    5,
                    -3.0F,
                    new Item.Properties().tab(Main.TAB).stacksTo(1),
                    3,
                    2
            )
    );
    public static final RegistryObject<Item> DIAMOND_SOCKETED_IRIDIUM_AXE = INSTANCE.register(
            "diamond_socketed_iridium_axe",
            () -> new IridiumAxeItem(
                    IridiumToolTier.DIAMOND_SOCKETED,
                    5.0F,
                    -3.0F,
                    new Item.Properties().tab(Main.TAB)
                            .fireResistant()
                            .stacksTo(1)
                            .craftRemainder(Items.DIAMOND),
                    Socket.DIAMOND
            )
    );
    public static final RegistryObject<Item> EMERALD_SOCKETED_IRIDIUM_AXE = INSTANCE.register(
            "emerald_socketed_iridium_axe",
            () -> new IridiumAxeItem(
                    IridiumToolTier.EMERALD_SOCKETED,
                    5.0F,
                    -3.0F,
                    new Item.Properties().tab(Main.TAB)
                            .fireResistant()
                            .stacksTo(1)
                            .craftRemainder(Items.EMERALD),
                    Socket.EMERALD
            )
    );
    public static final RegistryObject<Item> SKULL_SOCKETED_IRIDIUM_AXE = INSTANCE.register(
            "skull_socketed_iridium_axe",
            () -> new IridiumAxeItem(
                    IridiumToolTier.FULL,
                    5.0F,
                    -3.0F,
                    new Item.Properties().tab(Main.TAB)
                            .fireResistant()
                            .stacksTo(1)
                            .craftRemainder(Items.SKELETON_SKULL),
                    Socket.SKULL
            )
    );
    public static final RegistryObject<Item> WITHER_SKULL_SOCKETED_IRIDIUM_AXE = INSTANCE.register(
            "wither_skull_socketed_iridium_axe",
            () -> new IridiumAxeItem(
                    IridiumToolTier.FULL,
                    5.0F,
                    -3.0F,
                    new Item.Properties().tab(Main.TAB)
                            .craftRemainder(Items.WITHER_SKELETON_SKULL)
                            .fireResistant()
                            .stacksTo(1),
                    Socket.WITHER_SKULL
            )
    );
    public static final RegistryObject<Item> AMETHYST_SOCKETED_IRIDIUM_AXE = INSTANCE.register(
            "amethyst_socketed_iridium_axe",
            () -> new IridiumAxeItem(
                    IridiumToolTier.FULL,
                    5.0F,
                    -3.0F,
                    new Item.Properties().tab(Main.TAB)
                            .craftRemainder(Items.AMETHYST_SHARD)
                            .fireResistant()
                            .stacksTo(1),
                    Socket.AMETHYST
            )
    );


    //endregion

    //region iridium shovel

    public static final RegistryObject<Item> IRIDIUM_SHOVEL = INSTANCE.register(
            "iridium_shovel",
            () -> new IridiumShovelItem(
                    IridiumToolTier.FULL,
                    1.5F,
                    -3.0F,
                    new Item.Properties().tab(Main.TAB)
                            .fireResistant()
                            .stacksTo(1),
                    Socket.NONE
            )
    );

    public static final RegistryObject<Item> DIAMOND_SOCKETED_IRIDIUM_SHOVEL = INSTANCE.register(
            "diamond_socketed_iridium_shovel",
            () -> new IridiumShovelItem(
                    IridiumToolTier.DIAMOND_SOCKETED,
                    1.5F,
                    -3.0F,
                    new Item.Properties().tab(Main.TAB)
                            .fireResistant()
                            .stacksTo(1)
                            .craftRemainder(Items.DIAMOND),
                    Socket.DIAMOND
            )
    );
    public static final RegistryObject<Item> EMERALD_SOCKETED_IRIDIUM_SHOVEL = INSTANCE.register(
            "emerald_socketed_iridium_shovel",
            () -> new IridiumShovelItem(
                    IridiumToolTier.EMERALD_SOCKETED,
                    1.5F,
                    -3.0F,
                    new Item.Properties().tab(Main.TAB)
                            .fireResistant()
                            .stacksTo(1)
                            .craftRemainder(Items.EMERALD),
                    Socket.EMERALD
            )
    );
    public static final RegistryObject<Item> SKULL_SOCKETED_IRIDIUM_SHOVEL = INSTANCE.register(
            "skull_socketed_iridium_shovel",
            () -> new IridiumShovelItem(
                    IridiumToolTier.FULL,
                    1.5F,
                    -3.0F,
                    new Item.Properties().tab(Main.TAB)
                            .fireResistant()
                            .stacksTo(1)
                            .craftRemainder(Items.SKELETON_SKULL),
                    Socket.SKULL
            )
    );
    public static final RegistryObject<Item> WITHER_SKULL_SOCKETED_IRIDIUM_SHOVEL = INSTANCE.register(
            "wither_skull_socketed_iridium_shovel",
            () -> new IridiumShovelItem(
                    IridiumToolTier.FULL,
                    1.5F,
                    -3.0F,
                    new Item.Properties().tab(Main.TAB)
                            .fireResistant()
                            .stacksTo(1)
                            .craftRemainder(Items.WITHER_SKELETON_SKULL),
                    Socket.WITHER_SKULL
            )
    );
    public static final RegistryObject<Item> AMETHYST_SOCKETED_IRIDIUM_SHOVEL = INSTANCE.register(
            "amethyst_socketed_iridium_shovel",
            () -> new IridiumShovelItem(
                    IridiumToolTier.FULL,
                    1.5F,
                    -3.0F,
                    new Item.Properties().tab(Main.TAB)
                            .fireResistant()
                            .stacksTo(1)
                            .craftRemainder(Items.AMETHYST_SHARD),
                    Socket.AMETHYST
            )
    );

    //endregion

    //region iridium sword

    public static final RegistryObject<Item> IRIDIUM_SWORD = INSTANCE.register(
            "iridium_sword",
            () -> new IridiumSwordItem(
                    IridiumToolTier.FULL,
                    3,
                    -2.4F,
                    new Item.Properties().tab(Main.TAB)
                            .fireResistant()
                            .stacksTo(1),
                    Socket.NONE
            )
    );
    public static final RegistryObject<Item> PARTIAL_IRIDIUM_SWORD_2_1 = INSTANCE.register(
            "partial_iridium_sword_1",
            () -> new PartialIridiumSwordItem(
                    IridiumToolTier.HALF,
                    3,
                    -2.4F,
                    new Item.Properties().tab(Main.TAB)
                            .stacksTo(1),
                    2,
                    1
            )
    );
    public static final RegistryObject<Item> DIAMOND_SOCKETED_IRIDIUM_SWORD = INSTANCE.register(
            "diamond_socketed_iridium_sword",
            () -> new IridiumSwordItem(
                    IridiumToolTier.DIAMOND_SOCKETED,
                    3,
                    -2.4F,
                    new Item.Properties().tab(Main.TAB)
                            .fireResistant()
                            .stacksTo(1)
                            .craftRemainder(Items.DIAMOND),
                    Socket.DIAMOND
            )
    );
    public static final RegistryObject<Item> EMERALD_SOCKETED_IRIDIUM_SWORD = INSTANCE.register(
            "emerald_socketed_iridium_sword",
            () -> new IridiumSwordItem(
                    IridiumToolTier.EMERALD_SOCKETED,
                    3,
                    -2.4F,
                    new Item.Properties().tab(Main.TAB)
                            .fireResistant()
                            .stacksTo(1)
                            .craftRemainder(Items.EMERALD),
                    Socket.EMERALD
            )
    );
    public static final RegistryObject<Item> SKULL_SOCKETED_IRIDIUM_SWORD = INSTANCE.register(
            "skull_socketed_iridium_sword",
            () -> new IridiumSwordItem(
                    IridiumToolTier.FULL,
                    3,
                    -2.4F,
                    new Item.Properties().tab(Main.TAB)
                            .fireResistant()
                            .stacksTo(1)
                            .craftRemainder(Items.SKELETON_SKULL),
                    Socket.SKULL
            )
    );
    public static final RegistryObject<Item> WITHER_SKULL_SOCKETED_IRIDIUM_SWORD = INSTANCE.register(
            "wither_skull_socketed_iridium_sword",
            () -> new IridiumSwordItem(
                    IridiumToolTier.FULL,
                    3,
                    -2.4F,
                    new Item.Properties().tab(Main.TAB)
                            .fireResistant()
                            .stacksTo(1)
                            .craftRemainder(Items.WITHER_SKELETON_SKULL),
                    Socket.WITHER_SKULL
            )
    );
    public static final RegistryObject<Item> AMETHYST_SOCKETED_IRIDIUM_SWORD = INSTANCE.register(
            "amethyst_socketed_iridium_sword",
            () -> new IridiumSwordItem(
                    IridiumToolTier.FULL,
                    3,
                    -2.4F,
                    new Item.Properties().tab(Main.TAB)
                            .fireResistant()
                            .stacksTo(1)
                            .craftRemainder(Items.AMETHYST_SHARD),
                    Socket.AMETHYST
            )
    );

    //endregion

    //region iridium hoe

    public static final RegistryObject<Item> IRIDIUM_HOE = INSTANCE.register(
            "iridium_hoe",
            () -> new IridiumHoeItem(
                    IridiumToolTier.FULL,
                    -1
                    , 0.0F,
                    new Item.Properties().tab(Main.TAB)
                            .fireResistant()
                            .stacksTo(1)
                    , Socket.NONE));
    public
    static final RegistryObject
            <
                    Item> PARTIAL_IRIDIUM_HOE_2_1 = INSTANCE.register(
            "partial_iridium_hoe_1",
            () -> new PartialIridiumHoeItem(
                    IridiumToolTier.HALF,
                    -1
                    , 0.0F,
                    new Item.Properties().tab(Main.TAB)
                            .fireResistant()
                            .stacksTo(1)
                    , 2, 1));
    public
    static final RegistryObject
            <
                    Item> DIAMOND_SOCKETED_IRIDIUM_HOE = INSTANCE.register(
            "diamond_socketed_iridium_hoe",
            () -> new IridiumHoeItem(
                    IridiumToolTier.DIAMOND_SOCKETED,
                    -1
                    , 0.0F,
                    new Item.Properties().tab(Main.TAB)
                            .fireResistant()
                            .stacksTo(1)
                            .craftRemainder(Items.DIAMOND),
                    Socket.DIAMOND
            )
    );
    public static final RegistryObject<Item> EMERALD_SOCKETED_IRIDIUM_HOE = INSTANCE.register(
            "emerald_socketed_iridium_hoe",
            () -> new IridiumHoeItem(
                    IridiumToolTier.EMERALD_SOCKETED,
                    -1
                    , 0.0F,
                    new Item.Properties().tab(Main.TAB)
                            .fireResistant()
                            .stacksTo(1)
                            .craftRemainder(Items.EMERALD),
                    Socket.EMERALD
            )
    );
    public static final RegistryObject<Item> SKULL_SOCKETED_IRIDIUM_HOE = INSTANCE.register(
            "skull_socketed_iridium_hoe",
            () -> new IridiumHoeItem(
                    IridiumToolTier.FULL,
                    -1
                    , 0.0F,
                    new Item.Properties().tab(Main.TAB)
                            .fireResistant()
                            .stacksTo(1)
                            .craftRemainder(Items.SKELETON_SKULL),
                    Socket.SKULL
            )
    );
    public static final RegistryObject<Item> WITHER_SKULL_SOCKETED_IRIDIUM_HOE = INSTANCE.register(
            "wither_skull_socketed_iridium_hoe",
            () -> new IridiumHoeItem(
                    IridiumToolTier.FULL,
                    3,
                    -2.4F,
                    new Item.Properties().tab(Main.TAB)
                            .fireResistant()
                            .stacksTo(1)
                            .craftRemainder(Items.WITHER_SKELETON_SKULL),
                    Socket.WITHER_SKULL
            )
    );
    public static final RegistryObject<Item> AMETHYST_SOCKETED_IRIDIUM_HOE = INSTANCE.register(
            "amethyst_socketed_iridium_hoe",
            () -> new IridiumHoeItem(
                    IridiumToolTier.FULL,
                    -1
                    , 0.0F,
                    new Item.Properties().tab(Main.TAB)
                            .fireResistant()
                            .stacksTo(1)
                            .craftRemainder(Items.AMETHYST_SHARD),
                    Socket.AMETHYST
            )
    );

    //endregion

    //region iridium armor

    public static final RegistryObject<Item> IRIDIUM_HELMET = INSTANCE.register(
            "iridium_helmet",
            () -> new ArmorItem(
                    IridiumArmorMaterial.IRIDIUM,
                    EquipmentSlot.HEAD,
                    new Item.Properties().tab(Main.TAB)
                            .fireResistant()
                            .stacksTo(1)
            )
    );
    public static final RegistryObject<Item> IRIDIUM_CHESTPLATE = INSTANCE.register(
            "iridium_chestplate",
            () -> new IridiumChestplateItem(
                    IridiumArmorMaterial.IRIDIUM,
                    new Item.Properties().tab(Main.TAB)
                            .fireResistant()
                            .stacksTo(1),
                    Socket.NONE
            )
    );
    public static final RegistryObject<Item> IRIDIUM_LEGGINGS = INSTANCE.register(
            "iridium_leggings",
            () -> new ArmorItem(
                    IridiumArmorMaterial.IRIDIUM,
                    EquipmentSlot.LEGS,
                    new Item.Properties().tab(Main.TAB)
                            .fireResistant()
                            .stacksTo(1)
            )
    );
    public static final RegistryObject<Item> IRIDIUM_BOOTS = INSTANCE.register(
            "iridium_boots",
            () -> new ArmorItem(
                    IridiumArmorMaterial.IRIDIUM,
                    EquipmentSlot.FEET,
                    new Item.Properties().tab(Main.TAB)
                            .fireResistant()
                            .stacksTo(1)
            )
    );

    //region socketed chestplates
    public static final RegistryObject<Item> DIAMOND_SOCKETED_IRIDIUM_CHESTPLATE = INSTANCE.register(
            "diamond_socketed_iridium_chestplate",
            () -> new IridiumChestplateItem(
                    IridiumArmorMaterial.DIAMOND_SOCKETED,
                    new Item.Properties().tab(Main.TAB)
                            .fireResistant()
                            .stacksTo(1)
                            .craftRemainder(Items.DIAMOND),
                    Socket.DIAMOND
            )
    );
    public static final RegistryObject<Item> EMERALD_SOCKETED_IRIDIUM_CHESTPLATE = INSTANCE.register(
            "emerald_socketed_iridium_chestplate",
            () -> new IridiumChestplateItem(
                    IridiumArmorMaterial.EMERALD_SOCKETED,
                    new Item.Properties().tab(Main.TAB)
                            .fireResistant()
                            .stacksTo(1)
                            .craftRemainder(Items.EMERALD),
                    Socket.EMERALD
            )
    );
    public static final RegistryObject<Item> SKULL_SOCKETED_IRIDIUM_CHESTPLATE = INSTANCE.register(
            "skull_socketed_iridium_chestplate",
            () -> new IridiumChestplateItem(
                    IridiumArmorMaterial.IRIDIUM,
                    new Item.Properties().tab(Main.TAB)
                            .fireResistant()
                            .stacksTo(1)
                            .craftRemainder(Items.SKELETON_SKULL),
                    Socket.SKULL
            )
    );
    public static final RegistryObject<Item> WITHER_SKULL_SOCKETED_IRIDIUM_CHESTPLATE = INSTANCE.register(
            "wither_skull_socketed_iridium_chestplate",
            () -> new IridiumChestplateItem(
                    IridiumArmorMaterial.IRIDIUM,
                    new Item.Properties().tab(Main.TAB)
                            .fireResistant()
                            .stacksTo(1)
                            .craftRemainder(Items.WITHER_SKELETON_SKULL),
                    Socket.WITHER_SKULL
            )
    );
    public static final RegistryObject<Item> AMETHYST_SOCKETED_IRIDIUM_CHESTPLATE = INSTANCE.register(
            "amethyst_socketed_iridium_chestplate",
            () -> new IridiumChestplateItem(
                    IridiumArmorMaterial.IRIDIUM,
                    new Item.Properties().tab(Main.TAB)
                            .fireResistant()
                            .stacksTo(1)
                            .craftRemainder(Items.AMETHYST_SHARD),
                    Socket.AMETHYST
            )
    );
    //endregion

    //region partial iridium armors

    public static final RegistryObject<Item> PARTIAL_IRIDIUM_HELMET_5_1 = INSTANCE.register(
            "partial_iridium_helmet_1",
            () -> new PartialIridiumArmorItem(
                    IridiumArmorMaterial._5_1,
                    EquipmentSlot.HEAD,
                    new Item.Properties().tab(Main.TAB)
                            .fireResistant(),
                    5,
                    1
            )
    );
    public static final RegistryObject<Item> PARTIAL_IRIDIUM_HELMET_5_2 = INSTANCE.register(
            "partial_iridium_helmet_2",
            () -> new PartialIridiumArmorItem(
                    IridiumArmorMaterial._5_2,
                    EquipmentSlot.HEAD,
                    new Item.Properties().tab(Main.TAB)
                            .fireResistant(),
                    5,
                    2
            )
    );
    public static final RegistryObject<Item> PARTIAL_IRIDIUM_HELMET_5_3 = INSTANCE.register(
            "partial_iridium_helmet_3",
            () -> new PartialIridiumArmorItem(
                    IridiumArmorMaterial._5_3,
                    EquipmentSlot.HEAD,
                    new Item.Properties().tab(Main.TAB)
                            .fireResistant(),
                    5,
                    3
            )
    );
    public static final RegistryObject<Item> PARTIAL_IRIDIUM_HELMET_5_4 = INSTANCE.register(
            "partial_iridium_helmet_4",
            () -> new PartialIridiumArmorItem(
                    IridiumArmorMaterial._5_4,
                    EquipmentSlot.HEAD,
                    new Item.Properties().tab(Main.TAB)
                            .fireResistant(),
                    5,
                    4
            )
    );
    public static final RegistryObject<Item> PARTIAL_IRIDIUM_CHESTPLATE_8_1 = INSTANCE.register(
            "partial_iridium_chestplate_1",
            () -> new PartialIridiumArmorItem(
                    IridiumArmorMaterial._8_1,
                    EquipmentSlot.CHEST,
                    new Item.Properties().tab(Main.TAB)
                            .fireResistant(),
                    8,
                    1
            )
    );
    public static final RegistryObject<Item> PARTIAL_IRIDIUM_CHESTPLATE_8_2 = INSTANCE.register(
            "partial_iridium_chestplate_2",
            () -> new PartialIridiumArmorItem(
                    IridiumArmorMaterial._8_2,
                    EquipmentSlot.CHEST,
                    new Item.Properties().tab(Main.TAB)
                            .fireResistant(),
                    8,
                    2
            )
    );
    public static final RegistryObject<Item> PARTIAL_IRIDIUM_CHESTPLATE_8_3 = INSTANCE.register(
            "partial_iridium_chestplate_3",
            () -> new PartialIridiumArmorItem(
                    IridiumArmorMaterial._8_3,
                    EquipmentSlot.CHEST,
                    new Item.Properties().tab(Main.TAB)
                            .fireResistant(),
                    8,
                    3
            )
    );
    public static final RegistryObject<Item> PARTIAL_IRIDIUM_CHESTPLATE_8_4 = INSTANCE.register(
            "partial_iridium_chestplate_4",
            () -> new PartialIridiumArmorItem(
                    IridiumArmorMaterial._8_4,
                    EquipmentSlot.CHEST,
                    new Item.Properties().tab(Main.TAB)
                            .fireResistant(),
                    8,
                    4
            )
    );
    public static final RegistryObject<Item> PARTIAL_IRIDIUM_CHESTPLATE_8_5 = INSTANCE.register(
            "partial_iridium_chestplate_5",
            () -> new PartialIridiumArmorItem(
                    IridiumArmorMaterial._8_5,
                    EquipmentSlot.CHEST,
                    new Item.Properties().tab(Main.TAB)
                            .fireResistant(),
                    8,
                    5
            )
    );
    public static final RegistryObject<Item> PARTIAL_IRIDIUM_CHESTPLATE_8_6 = INSTANCE.register(
            "partial_iridium_chestplate_6",
            () -> new PartialIridiumArmorItem(
                    IridiumArmorMaterial._8_6,
                    EquipmentSlot.CHEST,
                    new Item.Properties().tab(Main.TAB)
                            .fireResistant(),
                    8,
                    6
            )
    );
    public static final RegistryObject<Item> PARTIAL_IRIDIUM_CHESTPLATE_8_7 = INSTANCE.register(
            "partial_iridium_chestplate_7",
            () -> new PartialIridiumArmorItem(
                    IridiumArmorMaterial._8_7,
                    EquipmentSlot.CHEST,
                    new Item.Properties().tab(Main.TAB)
                            .fireResistant(),
                    8,
                    7
            )
    );
    public static final RegistryObject<Item> PARTIAL_IRIDIUM_LEGGINGS_7_1 = INSTANCE.register(
            "partial_iridium_leggings_1",
            () -> new PartialIridiumArmorItem(
                    IridiumArmorMaterial._7_1,
                    EquipmentSlot.LEGS,
                    new Item.Properties().tab(Main.TAB)
                            .fireResistant(),
                    7,
                    1
            )
    );
    public static final RegistryObject<Item> PARTIAL_IRIDIUM_LEGGINGS_7_2 = INSTANCE.register(
            "partial_iridium_leggings_2",
            () -> new PartialIridiumArmorItem(
                    IridiumArmorMaterial._7_2,
                    EquipmentSlot.LEGS,
                    new Item.Properties().tab(Main.TAB)
                            .fireResistant(),
                    7,
                    2
            )
    );
    public static final RegistryObject<Item> PARTIAL_IRIDIUM_LEGGINGS_7_3 = INSTANCE.register(
            "partial_iridium_leggings_3",
            () -> new PartialIridiumArmorItem(
                    IridiumArmorMaterial._7_3,
                    EquipmentSlot.LEGS,
                    new Item.Properties().tab(Main.TAB)
                            .fireResistant(),
                    7,
                    3
            )
    );
    public static final RegistryObject<Item> PARTIAL_IRIDIUM_LEGGINGS_7_4 = INSTANCE.register(
            "partial_iridium_leggings_4",
            () -> new PartialIridiumArmorItem(
                    IridiumArmorMaterial._7_4,
                    EquipmentSlot.LEGS,
                    new Item.Properties().tab(Main.TAB)
                            .fireResistant(),
                    7,
                    4
            )
    );
    public static final RegistryObject<Item> PARTIAL_IRIDIUM_LEGGINGS_7_5 = INSTANCE.register(
            "partial_iridium_leggings_5",
            () -> new PartialIridiumArmorItem(
                    IridiumArmorMaterial._7_5,
                    EquipmentSlot.LEGS,
                    new Item.Properties().tab(Main.TAB)
                            .fireResistant(),
                    7,
                    5
            )
    );
    public static final RegistryObject<Item> PARTIAL_IRIDIUM_LEGGINGS_7_6 = INSTANCE.register(
            "partial_iridium_leggings_6",
            () -> new PartialIridiumArmorItem(
                    IridiumArmorMaterial._7_6,
                    EquipmentSlot.LEGS,
                    new Item.Properties().tab(Main.TAB)
                            .fireResistant(),
                    7,
                    6
            )
    );
    public static final RegistryObject<Item> PARTIAL_IRIDIUM_BOOTS_4_1 = INSTANCE.register(
            "partial_iridium_boots_1",
            () -> new PartialIridiumArmorItem(
                    IridiumArmorMaterial._4_1,
                    EquipmentSlot.FEET,
                    new Item.Properties().tab(Main.TAB)
                            .fireResistant(),
                    4,
                    1
            )
    );
    public static final RegistryObject<Item> PARTIAL_IRIDIUM_BOOTS_4_2 = INSTANCE.register(
            "partial_iridium_boots_2",
            () -> new PartialIridiumArmorItem(
                    IridiumArmorMaterial._4_2,
                    EquipmentSlot.FEET,
                    new Item.Properties().tab(Main.TAB)
                            .fireResistant(),
                    4,
                    2
            )
    );
    public static final RegistryObject<Item> PARTIAL_IRIDIUM_BOOTS_4_3 = INSTANCE.register(
            "partial_iridium_boots_3",
            () -> new PartialIridiumArmorItem(
                    IridiumArmorMaterial._4_3,
                    EquipmentSlot.FEET,
                    new Item.Properties().tab(Main.TAB)
                            .fireResistant(),
                    4,
                    3
            )
    );

    //endregion

    //endregion

    //region iridium scepter

    public static final RegistryObject<Item> IRIDIUM_SCEPTER = INSTANCE.register(
            "iridium_scepter",
            () -> new IridiumScepterItem(
                    new Item.Properties().tab(Main.TAB)
                            .fireResistant()
                            .stacksTo(1)
                    , Socket.NONE
            )
    );

    public static final RegistryObject
            <Item> DIAMOND_SOCKETED_IRIDIUM_SCEPTER = INSTANCE.register(
            "diamond_socketed_iridium_scepter",
            () -> new IridiumScepterItem(
                    new Item.Properties().tab(Main.TAB)
                            .fireResistant()
                            .stacksTo(1)
                            .craftRemainder(Items.DIAMOND),
                    Socket.DIAMOND
            )
    );
    public static final RegistryObject<Item> EMERALD_SOCKETED_IRIDIUM_SCEPTER = INSTANCE.register(
            "emerald_socketed_iridium_scepter",
            () -> new IridiumScepterItem(
                    new Item.Properties().tab(Main.TAB)
                            .fireResistant()
                            .stacksTo(1)
                            .craftRemainder(Items.EMERALD),
                    Socket.EMERALD
            )
    );
    public static final RegistryObject<Item> SKULL_SOCKETED_IRIDIUM_SCEPTER = INSTANCE.register(
            "skull_socketed_iridium_scepter",
            () -> new IridiumScepterItem(
                    new Item.Properties().tab(Main.TAB)
                            .fireResistant()
                            .stacksTo(1)
                            .craftRemainder(Items.SKELETON_SKULL),
                    Socket.SKULL
            )
    );
    public static final RegistryObject<Item> WITHER_SKULL_SOCKETED_IRIDIUM_SCEPTER = INSTANCE.register(
            "wither_skull_socketed_iridium_scepter",
            () -> new IridiumScepterItem(
                    new Item.Properties().tab(Main.TAB)
                            .fireResistant()
                            .stacksTo(1)
                            .craftRemainder(Items.WITHER_SKELETON_SKULL),
                    Socket.WITHER_SKULL
            )
    );
    public static final RegistryObject<Item> AMETHYST_SOCKETED_IRIDIUM_SCEPTER = INSTANCE.register(
            "amethyst_socketed_iridium_scepter",
            () -> new IridiumScepterItem(
                    new Item.Properties().tab(Main.TAB)
                            .fireResistant()
                            .stacksTo(1)
                            .craftRemainder(Items.AMETHYST_SHARD),
                    Socket.AMETHYST
            )
    );

    //endregion
}
