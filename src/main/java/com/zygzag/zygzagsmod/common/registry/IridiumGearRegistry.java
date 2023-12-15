package com.zygzag.zygzagsmod.common.registry;

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
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.zygzag.zygzagsmod.common.Main.MODID;

@SuppressWarnings("unused")
public class IridiumGearRegistry extends AkomiRegistry<Item> {
    public static final IridiumGearRegistry INSTANCE = new IridiumGearRegistry(DeferredRegister.create(Registries.ITEM, MODID));
    public static final Supplier<Item> IRIDIUM_HELMET = INSTANCE.register(
            "iridium_helmet",
            () -> new ArmorItem(
                    IridiumArmorMaterial.IRIDIUM,
                    ArmorItem.Type.HELMET,
                    new Item.Properties()
                            .fireResistant()
                            .stacksTo(1)
            )
    );
    public static final Supplier<Item> IRIDIUM_LEGGINGS = INSTANCE.register(
            "iridium_leggings",
            () -> new ArmorItem(
                    IridiumArmorMaterial.IRIDIUM,
                    ArmorItem.Type.LEGGINGS,
                    new Item.Properties()
                            .fireResistant()
                            .stacksTo(1)
            )
    );
    public static final Supplier<Item> IRIDIUM_BOOTS = INSTANCE.register(
            "iridium_boots",
            () -> new ArmorItem(
                    IridiumArmorMaterial.IRIDIUM,
                    ArmorItem.Type.BOOTS,
                    new Item.Properties()
                            .fireResistant()
                            .stacksTo(1)
            )
    );
    public static final Map<Socket, Supplier<Item>> SWORDS = makeAllSocketsForGearType(SocketedGearType.SWORD);
    public static final Set<Supplier<Item>> SOCKETED_SWORDS = SWORDS.entrySet().stream().filter((entry) -> entry.getKey() != Socket.NONE).map(Map.Entry::getValue).collect(Collectors.toSet());
    public static final Map<Socket, Supplier<Item>> PICKAXES = makeAllSocketsForGearType(SocketedGearType.PICKAXE);
    public static final Set<Supplier<Item>> SOCKETED_PICKAXES = PICKAXES.entrySet().stream().filter((entry) -> entry.getKey() != Socket.NONE).map(Map.Entry::getValue).collect(Collectors.toSet());
    public static final Map<Socket, Supplier<Item>> AXES = makeAllSocketsForGearType(SocketedGearType.AXE);
    public static final Set<Supplier<Item>> SOCKETED_AXES = AXES.entrySet().stream().filter((entry) -> entry.getKey() != Socket.NONE).map(Map.Entry::getValue).collect(Collectors.toSet());
    public static final Map<Socket, Supplier<Item>> SHOVELS = makeAllSocketsForGearType(SocketedGearType.SHOVEL);
    public static final Set<Supplier<Item>> SOCKETED_SHOVELS = SHOVELS.entrySet().stream().filter((entry) -> entry.getKey() != Socket.NONE).map(Map.Entry::getValue).collect(Collectors.toSet());
    public static final Map<Socket, Supplier<Item>> HOES = makeAllSocketsForGearType(SocketedGearType.HOE);
    public static final Set<Supplier<Item>> SOCKETED_HOES = HOES.entrySet().stream().filter((entry) -> entry.getKey() != Socket.NONE).map(Map.Entry::getValue).collect(Collectors.toSet());
    public static final Map<Socket, Supplier<Item>> CHESTPLATES = makeAllSocketsForGearType(SocketedGearType.CHESTPLATE);
    public static final Set<Supplier<Item>> SOCKETED_CHESTPLATES = CHESTPLATES.entrySet().stream().filter((entry) -> entry.getKey() != Socket.NONE).map(Map.Entry::getValue).collect(Collectors.toSet());
    public static final Map<Socket, Supplier<Item>> SCEPTERS = makeAllSocketsForGearType(SocketedGearType.SCEPTER);
    public static final Set<Supplier<Item>> SOCKETED_SCEPTERS = SCEPTERS.entrySet().stream().filter((entry) -> entry.getKey() != Socket.NONE).map(Map.Entry::getValue).collect(Collectors.toSet());
    public static List<Supplier<Item>> SOCKETED_ITEMS = Stream.of(
            SOCKETED_AXES,
            SOCKETED_SWORDS,
            SOCKETED_PICKAXES,
            SOCKETED_SHOVELS,
            SOCKETED_HOES,
            SOCKETED_CHESTPLATES,
            SOCKETED_SCEPTERS
    ).flatMap(Collection::stream).collect(Collectors.toList());
    public static final Map<Integer, Supplier<Item>> PARTIAL_SWORDS = makeAllPartialsForGearType(PartialGearType.SWORD);
    public static final Map<Integer, Supplier<Item>> PARTIAL_PICKAXES = makeAllPartialsForGearType(PartialGearType.PICKAXE);
    public static final Map<Integer, Supplier<Item>> PARTIAL_AXES = makeAllPartialsForGearType(PartialGearType.AXE);
    public static final Map<Integer, Supplier<Item>> PARTIAL_HOES = makeAllPartialsForGearType(PartialGearType.HOE);
    public static final Map<Integer, Supplier<Item>> PARTIAL_HELMETS = makeAllPartialsForGearType(PartialGearType.HELMET);
    public static final Map<Integer, Supplier<Item>> PARTIAL_CHESTPLATES = makeAllPartialsForGearType(PartialGearType.CHESTPLATE);
    public static final Map<Integer, Supplier<Item>> PARTIAL_LEGGINGS = makeAllPartialsForGearType(PartialGearType.LEGGINGS);
    public static final Map<Integer, Supplier<Item>> PARTIAL_BOOTS = makeAllPartialsForGearType(PartialGearType.BOOTS);

    public IridiumGearRegistry(DeferredRegister<Item> register) {
        super(register);
    }

    private static Map<Socket, Supplier<Item>> makeAllSocketsForGearType(SocketedGearType gearType) {
        Map<Socket, Supplier<Item>> map = new HashMap<>();
        for (Socket socket : Socket.values()) {
            if (socket.gearTypeFilter.test(gearType)) map.put(socket, makeIridiumGear(socket, gearType));
        }
        return map;
    }

    private static Supplier<Item> makeIridiumGear(Socket socket, SocketedGearType gearType) {
        return INSTANCE.register(
                (socket == Socket.NONE ? "" : socket.name().toLowerCase() + "_socketed_") + "iridium_" + gearType.name().toLowerCase(),
                () -> gearType.itemFactory.provideItem(
                        new Item.Properties()
                                .fireResistant()
                                .stacksTo(1)
                                .craftRemainder(socket.itemSupplier.get()),
                        socket
                )
        );
    }

    private static Map<Integer, Supplier<Item>> makeAllPartialsForGearType(PartialGearType gearType) {
        Map<Integer, Supplier<Item>> map = new HashMap<>();
        //Main.LOGGER.debug("making partials for gear type " + gearType);
        for (int platings = 1; platings < gearType.maxPlatings; platings++) {
            map.put(platings, makePartialIridiumGear(platings, gearType));
            //Main.LOGGER.debug("made partial " + platings + " for gear type " + gearType);
        }
        return map;
    }

    private static Supplier<Item> makePartialIridiumGear(int platings, PartialGearType gearType) {
        return INSTANCE.register(
                "partial_iridium_" + gearType.name().toLowerCase() + "_" + platings,
                () -> gearType.itemFactory.provideItem(
                        new Item.Properties()
                                .fireResistant()
                                .stacksTo(1),
                        platings
                )
        );
    }

    public enum SocketedGearType {
        SWORD((properties, socket) -> new IridiumSwordItem(
                socket == Socket.DIAMOND ? IridiumToolTier.DIAMOND_SOCKETED :
                        socket == Socket.EMERALD ? IridiumToolTier.EMERALD_SOCKETED :
                                IridiumToolTier.FULL,
                3,
                -2.4f,
                properties,
                socket
        )),
        PICKAXE((properties, socket) -> new IridiumPickaxeItem(
                socket == Socket.DIAMOND ? IridiumToolTier.DIAMOND_SOCKETED_PICK :
                        socket == Socket.EMERALD ? IridiumToolTier.EMERALD_SOCKETED :
                                socket == Socket.WITHER_SKULL ? IridiumToolTier.WITHER_SOCKETED_PICK :
                                        IridiumToolTier.FULL,
                1,
                -2.8f,
                properties,
                socket
        )),
        AXE((properties, socket) -> new IridiumAxeItem(
                socket == Socket.DIAMOND ? IridiumToolTier.DIAMOND_SOCKETED :
                        socket == Socket.EMERALD ? IridiumToolTier.EMERALD_SOCKETED :
                                IridiumToolTier.FULL,
                socket == Socket.DIAMOND ? 4.5f : 5,
                -3,
                properties,
                socket
        )),
        SHOVEL((properties, socket) -> new IridiumShovelItem(
                socket == Socket.DIAMOND ? IridiumToolTier.DIAMOND_SOCKETED :
                        socket == Socket.EMERALD ? IridiumToolTier.EMERALD_SOCKETED :
                                IridiumToolTier.FULL,
                1.5f,
                -3,
                properties,
                socket
        )),
        HOE((properties, socket) -> new IridiumHoeItem(
                socket == Socket.DIAMOND ? IridiumToolTier.DIAMOND_SOCKETED :
                        socket == Socket.EMERALD ? IridiumToolTier.EMERALD_SOCKETED :
                                IridiumToolTier.FULL,
                socket == Socket.WITHER_SKULL ? 3 : -1,
                socket == Socket.WITHER_SKULL ? -2.4f : 0,
                properties,
                socket
        )),
        CHESTPLATE((properties, socket) -> new IridiumChestplateItem(
                socket == Socket.DIAMOND ? IridiumArmorMaterial.DIAMOND_SOCKETED :
                        socket == Socket.EMERALD ? IridiumArmorMaterial.EMERALD_SOCKETED :
                                IridiumArmorMaterial.IRIDIUM,
                properties,
                socket
        )),
        SCEPTER(IridiumScepterItem::new);

        public final SocketedItemFactory itemFactory;

        SocketedGearType(SocketedItemFactory factory) {
            itemFactory = factory;
        }
    }

    enum PartialGearType {
        HELMET(5, (properties, platings) -> new PartialIridiumArmorItem(
                IridiumArmorMaterial.getArmorForPartial(platings, 5),
                ArmorItem.Type.HELMET,
                properties,
                5,
                platings
        )),
        CHESTPLATE(8, (properties, platings) -> new PartialIridiumArmorItem(
                IridiumArmorMaterial.getArmorForPartial(platings, 8),
                ArmorItem.Type.CHESTPLATE,
                properties,
                8,
                platings
        )),
        LEGGINGS(7, (properties, platings) -> new PartialIridiumArmorItem(
                IridiumArmorMaterial.getArmorForPartial(platings, 7),
                ArmorItem.Type.LEGGINGS,
                properties,
                7,
                platings
        )),
        BOOTS(4, (properties, platings) -> new PartialIridiumArmorItem(
                IridiumArmorMaterial.getArmorForPartial(platings, 4),
                ArmorItem.Type.BOOTS,
                properties,
                4,
                platings
        )),
        SWORD(2, (properties, platings) -> new PartialIridiumSwordItem(
                IridiumToolTier.getToolTier(platings, 2),
                3,
                -2.4F,
                properties,
                2,
                platings // theres only one but whatever
        )),
        PICKAXE(3, (properties, platings) -> new PartialIridiumPickaxeItem(
                IridiumToolTier.getToolTier(platings, 3),
                1,
                -2.8F,
                properties,
                3,
                platings
        )),
        AXE(3, (properties, platings) -> new PartialIridiumAxeItem(
                IridiumToolTier.getToolTier(platings, 3),
                5,
                -3.0F,
                properties,
                3,
                platings
        )),
        HOE(2, (properties, platings) -> new PartialIridiumHoeItem(
                IridiumToolTier.getToolTier(platings, 2),
                -1,
                0.0F,
                properties,
                2,
                platings
        ));

        public final int maxPlatings;
        public final PartialItemFactory itemFactory;

        PartialGearType(int maxPlatings, PartialItemFactory factory) {
            this.maxPlatings = maxPlatings;
            itemFactory = factory;
        }
    }

    public interface SocketedItemFactory {
        Item provideItem(Item.Properties properties, Socket socket);
    }

    public interface PartialItemFactory {
        Item provideItem(Item.Properties properties, int platings);
    }
}
