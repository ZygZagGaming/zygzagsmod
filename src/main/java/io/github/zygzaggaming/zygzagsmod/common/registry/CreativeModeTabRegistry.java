package io.github.zygzaggaming.zygzagsmod.common.registry;

import io.github.zygzaggaming.zygzagsmod.common.item.iridium.Socket;
import io.github.zygzaggaming.zygzagsmod.common.registry.base.AkomiRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;
import java.util.function.Supplier;

import static io.github.zygzaggaming.zygzagsmod.common.Main.MODID;

@SuppressWarnings("unused")
public class CreativeModeTabRegistry extends AkomiRegistry<CreativeModeTab> {
    public static final CreativeModeTabRegistry INSTANCE = new CreativeModeTabRegistry(DeferredRegister.create(BuiltInRegistries.CREATIVE_MODE_TAB, MODID));
    public static final List<Supplier<? extends Item>> CREATIVE_TAB_ORDER = List.of(
            // First, iridium & end stuffs
            BlockWithItemRegistry.END_STONE_SWITCH.item(),
            BlockWithItemRegistry.END_SAND.item(),
            BlockItemEntityRegistry.SUSPICIOUS_END_SAND.item(),
            BlockWithItemRegistry.IRIDIUM_END_SAND.item(),
            ItemRegistry.RAW_IRIDIUM,
            BlockWithItemRegistry.RAW_IRIDIUM_BLOCK.item(),
            ItemRegistry.IRIDIUM_PLATING,
            ItemRegistry.IRIDIUM_GEAR_BLUEPRINTS.get(IridiumGearType.HELMET),
            ItemRegistry.IRIDIUM_GEAR_BLUEPRINTS.get(IridiumGearType.CHESTPLATE),
            ItemRegistry.IRIDIUM_GEAR_BLUEPRINTS.get(IridiumGearType.LEGGINGS),
            ItemRegistry.IRIDIUM_GEAR_BLUEPRINTS.get(IridiumGearType.BOOTS),
            ItemRegistry.IRIDIUM_GEAR_BLUEPRINTS.get(IridiumGearType.SWORD),
            ItemRegistry.IRIDIUM_GEAR_BLUEPRINTS.get(IridiumGearType.PICKAXE),
            ItemRegistry.IRIDIUM_GEAR_BLUEPRINTS.get(IridiumGearType.AXE),
            ItemRegistry.IRIDIUM_GEAR_BLUEPRINTS.get(IridiumGearType.SHOVEL),
            ItemRegistry.IRIDIUM_GEAR_BLUEPRINTS.get(IridiumGearType.HOE),
            ItemRegistry.SOCKET_SCHEMATICS.get(Socket.DIAMOND),
            ItemRegistry.SOCKET_SCHEMATICS.get(Socket.EMERALD),
            ItemRegistry.SOCKET_SCHEMATICS.get(Socket.SKULL),
            ItemRegistry.SOCKET_SCHEMATICS.get(Socket.WITHER_SKULL),
            ItemRegistry.SOCKET_SCHEMATICS.get(Socket.AMETHYST),

            // Then, iridium gear (partial platings and sockets come last)
            IridiumGearRegistry.IRIDIUM_HELMET,
            IridiumGearRegistry.CHESTPLATES.get(Socket.NONE),
            IridiumGearRegistry.IRIDIUM_LEGGINGS,
            IridiumGearRegistry.IRIDIUM_BOOTS,
            IridiumGearRegistry.SWORDS.get(Socket.NONE),
            IridiumGearRegistry.PICKAXES.get(Socket.NONE),
            IridiumGearRegistry.AXES.get(Socket.NONE),
            IridiumGearRegistry.SHOVELS.get(Socket.NONE),
            IridiumGearRegistry.HOES.get(Socket.NONE),
            IridiumGearRegistry.SCEPTERS.get(Socket.NONE),

            // Nether brick stuffs
            BlockWithItemRegistry.CRACKED_RED_NETHER_BRICKS.item(),
            BlockWithItemRegistry.CHISELED_RED_NETHER_BRICKS.item(),
            BlockWithItemRegistry.GIRDED_RED_NETHER_BRICKS.item(),
            BlockItemEntityRegistry.MAGMATIC_NETHER_BRICKS.item(),
            BlockWithItemRegistry.CRUSHED_RED_NETHER_BRICKS.item(),
            BlockItemEntityRegistry.RED_NETHER_BRICK_CACHE.item(),
            BlockWithItemRegistry.NETHER_BRICK_BUTTON.item(),
            BlockWithItemRegistry.RED_NETHER_BRICK_BUTTON.item(),
            BlockWithItemRegistry.NETHER_QUARTZ_GLASS.item(),
            BlockWithItemRegistry.NETHER_QUARTZ_GLASS_SLAB.item(),
            BlockWithItemRegistry.NETHER_QUARTZ_GLASS_STAIRS.item(),

            // Misc
            BlockItemEntityRegistry.SCULK_JAW.item(),
            ItemRegistry.IRIDIUM_GOLEM_SPAWN_EGG,
            ItemRegistry.BLAZE_SENTRY_SPAWN_EGG
    );
    public static final Supplier<CreativeModeTab> ZYGZAGSMOD = INSTANCE.register("zygzagsmod_tab", () -> CreativeModeTab.builder()
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> ItemRegistry.IRIDIUM_PLATING.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                for (var item : CREATIVE_TAB_ORDER) output.accept(item.get());
            })
            .title(Component.translatable("zygzagsmod.tab"))
            .build());
    public static final Supplier<CreativeModeTab> IRIDIUM_TOOLS_AND_ARMOR = INSTANCE.register("zygzagsmod_iridium_tools_and_armor", () -> CreativeModeTab.builder()
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> IridiumGearRegistry.SWORDS.get(Socket.NONE).get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                for (var gearType : SocketedGearType.values()) {
                    var map = IridiumGearRegistry.ALL_SOCKETABLES.get(gearType);
                    if (gearType == SocketedGearType.CHESTPLATE) output.accept(IridiumGearRegistry.IRIDIUM_HELMET.get());
                    for (Socket socket : Socket.values()) output.accept(map.get(socket).get());
                    if (gearType == SocketedGearType.CHESTPLATE) {
                        output.accept(IridiumGearRegistry.IRIDIUM_LEGGINGS.get());
                        output.accept(IridiumGearRegistry.IRIDIUM_BOOTS.get());
                    }
                }
            })
            .title(Component.translatable("zygzagsmod.iridium_tools_and_armor"))
            .build());

    public CreativeModeTabRegistry(DeferredRegister<CreativeModeTab> register) {
        super(register);
    }
}
