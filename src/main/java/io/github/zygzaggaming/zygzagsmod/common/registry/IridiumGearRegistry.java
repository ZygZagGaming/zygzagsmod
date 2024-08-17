package io.github.zygzaggaming.zygzagsmod.common.registry;

import io.github.zygzaggaming.zygzagsmod.common.item.iridium.Socket;
import io.github.zygzaggaming.zygzagsmod.common.registry.base.AkomiRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.github.zygzaggaming.zygzagsmod.common.Main.MODID;

@SuppressWarnings("unused")
public class IridiumGearRegistry extends AkomiRegistry<Item> {
    public static final IridiumGearRegistry INSTANCE = new IridiumGearRegistry(DeferredRegister.create(Registries.ITEM, MODID));
    public static final Supplier<Item> IRIDIUM_HELMET = INSTANCE.register(
            "iridium_helmet",
            () -> new ArmorItem(
                    ArmorMaterialRegistry.IRIDIUM,
                    ArmorItem.Type.HELMET,
                    new Item.Properties()
                            .fireResistant()
                            .stacksTo(1)
            )
    );
    public static final Supplier<Item> IRIDIUM_LEGGINGS = INSTANCE.register(
            "iridium_leggings",
            () -> new ArmorItem(
                    ArmorMaterialRegistry.IRIDIUM,
                    ArmorItem.Type.LEGGINGS,
                    new Item.Properties()
                            .fireResistant()
                            .stacksTo(1)
            )
    );
    public static final Supplier<Item> IRIDIUM_BOOTS = INSTANCE.register(
            "iridium_boots",
            () -> new ArmorItem(
                    ArmorMaterialRegistry.IRIDIUM,
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
    public static Map<SocketedGearType, Map<Socket, Supplier<Item>>> ALL_SOCKETABLES = Map.of(
            SocketedGearType.SWORD, SWORDS,
            SocketedGearType.PICKAXE, PICKAXES,
            SocketedGearType.AXE, AXES,
            SocketedGearType.SHOVEL, SHOVELS,
            SocketedGearType.HOE, HOES,
            SocketedGearType.SCEPTER, SCEPTERS,
            SocketedGearType.CHESTPLATE, CHESTPLATES
    );
    public static final Map<Integer, Supplier<Item>> PARTIAL_SWORDS = makeAllPartialsForGearType(IridiumGearType.SWORD);
    public static final Map<Integer, Supplier<Item>> PARTIAL_PICKAXES = makeAllPartialsForGearType(IridiumGearType.PICKAXE);
    public static final Map<Integer, Supplier<Item>> PARTIAL_AXES = makeAllPartialsForGearType(IridiumGearType.AXE);
    public static final Map<Integer, Supplier<Item>> PARTIAL_HOES = makeAllPartialsForGearType(IridiumGearType.HOE);
    public static final Map<Integer, Supplier<Item>> PARTIAL_HELMETS = makeAllPartialsForGearType(IridiumGearType.HELMET);
    public static final Map<Integer, Supplier<Item>> PARTIAL_CHESTPLATES = makeAllPartialsForGearType(IridiumGearType.CHESTPLATE);
    public static final Map<Integer, Supplier<Item>> PARTIAL_LEGGINGS = makeAllPartialsForGearType(IridiumGearType.LEGGINGS);
    public static final Map<Integer, Supplier<Item>> PARTIAL_BOOTS = makeAllPartialsForGearType(IridiumGearType.BOOTS);

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

    private static Map<Integer, Supplier<Item>> makeAllPartialsForGearType(IridiumGearType gearType) {
        Map<Integer, Supplier<Item>> map = new HashMap<>();
        //Main.LOGGER.debug("making partials for gear type " + gearType);
        for (int platings = 1; platings < gearType.maxPlatings; platings++) {
            map.put(platings, makePartialIridiumGear(platings, gearType));
            //Main.LOGGER.debug("made partial " + platings + " for gear type " + gearType);
        }
        return map;
    }

    private static Supplier<Item> makePartialIridiumGear(int platings, IridiumGearType gearType) {
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

    public interface SocketedItemFactory {
        Item provideItem(Item.Properties properties, Socket socket);
    }

    public interface PartialItemFactory {
        Item provideItem(Item.Properties properties, int platings);
    }
}
