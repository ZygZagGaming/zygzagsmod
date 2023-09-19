package com.zygzag.zygzagsmod.common.registry;

import com.zygzag.zygzagsmod.common.item.iridium.PartialGearType;
import com.zygzag.zygzagsmod.common.item.iridium.Socket;
import com.zygzag.zygzagsmod.common.item.iridium.SocketedGearType;
import com.zygzag.zygzagsmod.common.tier.IridiumArmorMaterial;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.zygzag.zygzagsmod.common.Main.MODID;

@SuppressWarnings("unused")
public class IridiumGearRegistry extends Registry<Item> {
    public static final IridiumGearRegistry INSTANCE = new IridiumGearRegistry(DeferredRegister.create(Registries.ITEM, MODID));
    public IridiumGearRegistry(DeferredRegister<Item> register) {
        super(register);
    }

    public static final RegistryObject<Item> IRIDIUM_HELMET = INSTANCE.register(
            "iridium_helmet",
            () -> new ArmorItem(
                    IridiumArmorMaterial.IRIDIUM,
                    ArmorItem.Type.HELMET,
                    new Item.Properties()
                            .fireResistant()
                            .stacksTo(1)
            )
    );
    public static final RegistryObject<Item> IRIDIUM_LEGGINGS = INSTANCE.register(
            "iridium_leggings",
            () -> new ArmorItem(
                    IridiumArmorMaterial.IRIDIUM,
                    ArmorItem.Type.LEGGINGS,
                    new Item.Properties()
                            .fireResistant()
                            .stacksTo(1)
            )
    );
    public static final RegistryObject<Item> IRIDIUM_BOOTS = INSTANCE.register(
            "iridium_boots",
            () -> new ArmorItem(
                    IridiumArmorMaterial.IRIDIUM,
                    ArmorItem.Type.BOOTS,
                    new Item.Properties()
                            .fireResistant()
                            .stacksTo(1)
            )
    );

    public static final Map<RegistryObject<Socket>, RegistryObject<Item>> SWORDS = makeAllSocketsForGearType(SocketedGearTypeRegistry.SWORD);
    public static final Set<RegistryObject<Item>> SOCKETED_SWORDS = SWORDS.entrySet().stream().filter((entry) -> entry.getKey() != SocketRegistry.NONE).map(Map.Entry::getValue).collect(Collectors.toSet());

    public static final Map<RegistryObject<Socket>, RegistryObject<Item>> PICKAXES = makeAllSocketsForGearType(SocketedGearTypeRegistry.PICKAXE);
    public static final Set<RegistryObject<Item>> SOCKETED_PICKAXES = PICKAXES.entrySet().stream().filter((entry) -> entry.getKey() != SocketRegistry.NONE).map(Map.Entry::getValue).collect(Collectors.toSet());

    public static final Map<RegistryObject<Socket>, RegistryObject<Item>> AXES = makeAllSocketsForGearType(SocketedGearTypeRegistry.AXE);
    public static final Set<RegistryObject<Item>> SOCKETED_AXES = AXES.entrySet().stream().filter((entry) -> entry.getKey() != SocketRegistry.NONE).map(Map.Entry::getValue).collect(Collectors.toSet());

    public static final Map<RegistryObject<Socket>, RegistryObject<Item>> SHOVELS = makeAllSocketsForGearType(SocketedGearTypeRegistry.SHOVEL);
    public static final Set<RegistryObject<Item>> SOCKETED_SHOVELS = SHOVELS.entrySet().stream().filter((entry) -> entry.getKey() != SocketRegistry.NONE).map(Map.Entry::getValue).collect(Collectors.toSet());

    public static final Map<RegistryObject<Socket>, RegistryObject<Item>> HOES = makeAllSocketsForGearType(SocketedGearTypeRegistry.HOE);
    public static final Set<RegistryObject<Item>> SOCKETED_HOES = HOES.entrySet().stream().filter((entry) -> entry.getKey() != SocketRegistry.NONE).map(Map.Entry::getValue).collect(Collectors.toSet());

    public static final Map<RegistryObject<Socket>, RegistryObject<Item>> CHESTPLATES = makeAllSocketsForGearType(SocketedGearTypeRegistry.CHESTPLATE);
    public static final Set<RegistryObject<Item>> SOCKETED_CHESTPLATES = CHESTPLATES.entrySet().stream().filter((entry) -> entry.getKey() != SocketRegistry.NONE).map(Map.Entry::getValue).collect(Collectors.toSet());

    public static final Map<RegistryObject<Socket>, RegistryObject<Item>> SCEPTERS = makeAllSocketsForGearType(SocketedGearTypeRegistry.SCEPTER);
    public static final Set<RegistryObject<Item>> SOCKETED_SCEPTERS = SCEPTERS.entrySet().stream().filter((entry) -> entry.getKey() != SocketRegistry.NONE).map(Map.Entry::getValue).collect(Collectors.toSet());

    public static final Map<Integer, RegistryObject<Item>> PARTIAL_SWORDS = makeAllPartialsForGearType(PartialGearType.SWORD);
    public static final Map<Integer, RegistryObject<Item>> PARTIAL_PICKAXES = makeAllPartialsForGearType(PartialGearType.PICKAXE);
    public static final Map<Integer, RegistryObject<Item>> PARTIAL_AXES = makeAllPartialsForGearType(PartialGearType.AXE);
    public static final Map<Integer, RegistryObject<Item>> PARTIAL_HOES = makeAllPartialsForGearType(PartialGearType.HOE);
    public static final Map<Integer, RegistryObject<Item>> PARTIAL_HELMETS = makeAllPartialsForGearType(PartialGearType.HELMET);
    public static final Map<Integer, RegistryObject<Item>> PARTIAL_CHESTPLATES = makeAllPartialsForGearType(PartialGearType.CHESTPLATE);
    public static final Map<Integer, RegistryObject<Item>> PARTIAL_LEGGINGS = makeAllPartialsForGearType(PartialGearType.LEGGINGS);
    public static final Map<Integer, RegistryObject<Item>> PARTIAL_BOOTS = makeAllPartialsForGearType(PartialGearType.BOOTS);

    private static Map<RegistryObject<Socket>, RegistryObject<Item>> makeAllSocketsForGearType(RegistryObject<SocketedGearType> gearType) {
        Map<RegistryObject<Socket>, RegistryObject<Item>> map = new HashMap<>();
        for (RegistryObject<Socket> socket : SocketRegistry.INSTANCE.register.getEntries()) {
            map.put(socket, makeIridiumGear(socket, gearType));
        }
        return map;
    }

    private static RegistryObject<Item> makeIridiumGear(RegistryObject<Socket> socket, RegistryObject<SocketedGearType> gearType) {
        return INSTANCE.register(
                (socket == SocketRegistry.NONE ? "" : socket.getId().getPath() + "_socketed_") + "iridium_" + gearType.getId().getPath().toLowerCase(),
                () -> gearType.get().itemFactory.provideItem(
                        new Item.Properties()
                                .fireResistant()
                                .stacksTo(1),
                        socket
                )
        );
    }

    public static List<RegistryObject<Item>> SOCKETED_ITEMS = Stream.of(
            SOCKETED_AXES,
            SOCKETED_SWORDS,
            SOCKETED_PICKAXES,
            SOCKETED_SHOVELS,
            SOCKETED_HOES,
            SOCKETED_CHESTPLATES,
            SOCKETED_SCEPTERS
    ).flatMap(Collection::stream).collect(Collectors.toList());

    private static Map<Integer, RegistryObject<Item>> makeAllPartialsForGearType(PartialGearType gearType) {
        Map<Integer, RegistryObject<Item>> map = new HashMap<>();
        //Main.LOGGER.debug("making partials for gear type " + gearType);
        for (int platings = 1; platings < gearType.maxPlatings; platings++) {
            map.put(platings, makePartialIridiumGear(platings, gearType));
            //Main.LOGGER.debug("made partial " + platings + " for gear type " + gearType);
        }
        return map;
    }

    private static RegistryObject<Item> makePartialIridiumGear(int platings, PartialGearType gearType) {
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
}
