package com.zygzag.zygzagsmod.common;

import com.mojang.logging.LogUtils;
import com.zygzag.zygzagsmod.client.CustomBrushableBlockRenderer;
import com.zygzag.zygzagsmod.common.capability.PlayerSightCache;
import com.zygzag.zygzagsmod.common.item.iridium.ISocketable;
import com.zygzag.zygzagsmod.common.registries.BlockEntityRegistry;
import com.zygzag.zygzagsmod.common.registries.IridiumGearRegistry;
import com.zygzag.zygzagsmod.common.registries.ItemRegistry;
import com.zygzag.zygzagsmod.common.registries.Registry;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BrushItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@Mod(Main.MODID)
public class Main {

    public static final EnchantmentCategory COOLDOWN_CATEGORY = EnchantmentCategory.create("cooldown", (item) -> item instanceof ISocketable socketable && socketable.hasCooldown());
    public static final EnchantmentCategory BRUSH_CATEGORY = EnchantmentCategory.create("brush", (item) -> item instanceof BrushItem);
    public static final String MODID = "zygzagsmod";
    @SuppressWarnings("unused")
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final TagKey<Block> NEEDS_IRIDIUM_TOOL_TAG = BlockTags.create(new ResourceLocation("zygzagsmod:needs_iridium_tool"));
    public static final TagKey<Block> VEGETATION_TAG = BlockTags.create(new ResourceLocation("zygzagsmod:vegetation"));
    public static final TagKey<EntityType<?>> VILLAGER_HATED = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation("zygzagsmod:villager_hated"));
    public static final TagKey<Block> VEINMINEABLE = TagKey.create(Registries.BLOCK, new ResourceLocation("zygzagsmod:veinmineable"));
    public static final TagKey<EntityType<?>> BOSS_TAG = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation("zygzagsmod:bosses"));

    public static final Capability<PlayerSightCache> PLAYER_SIGHT_CACHE = CapabilityManager.get(new CapabilityToken<>(){});

    public Main() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::clientSetup);

        Registry.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(this);

        modEventBus.addListener(this::addCreative);
        for (var soundEvent : ModSoundEvents.SOUND_EVENTS) modEventBus.register(soundEvent);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);

        //System.out.println("Grass is " + (Blocks.GRASS.defaultBlockState().is(VEGETATION_TAG) ? "" : "not ") + "in the vegetation tag");
    }

    private void commonSetup(final FMLCommonSetupEvent event) { }

    private void clientSetup(final FMLClientSetupEvent event) { }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.COMBAT) {
            event.acceptAll(Stream.of(
                    IridiumGearRegistry.IRIDIUM_SWORD,
                    IridiumGearRegistry.IRIDIUM_AXE,
                    IridiumGearRegistry.IRIDIUM_SCEPTER,
                    IridiumGearRegistry.IRIDIUM_HELMET,
                    IridiumGearRegistry.IRIDIUM_CHESTPLATE,
                    IridiumGearRegistry.IRIDIUM_LEGGINGS,
                    IridiumGearRegistry.IRIDIUM_BOOTS,
                    IridiumGearRegistry.DIAMOND_SOCKETED_IRIDIUM_SWORD,
                    IridiumGearRegistry.EMERALD_SOCKETED_IRIDIUM_SWORD,
                    IridiumGearRegistry.SKULL_SOCKETED_IRIDIUM_SWORD,
                    IridiumGearRegistry.WITHER_SKULL_SOCKETED_IRIDIUM_SWORD,
                    IridiumGearRegistry.AMETHYST_SOCKETED_IRIDIUM_SWORD,
                    IridiumGearRegistry.DIAMOND_SOCKETED_IRIDIUM_AXE,
                    IridiumGearRegistry.EMERALD_SOCKETED_IRIDIUM_AXE,
                    IridiumGearRegistry.SKULL_SOCKETED_IRIDIUM_AXE,
                    IridiumGearRegistry.WITHER_SKULL_SOCKETED_IRIDIUM_AXE,
                    IridiumGearRegistry.AMETHYST_SOCKETED_IRIDIUM_AXE,
                    IridiumGearRegistry.DIAMOND_SOCKETED_IRIDIUM_SCEPTER,
                    IridiumGearRegistry.EMERALD_SOCKETED_IRIDIUM_SCEPTER,
                    IridiumGearRegistry.SKULL_SOCKETED_IRIDIUM_SCEPTER,
                    IridiumGearRegistry.WITHER_SKULL_SOCKETED_IRIDIUM_SCEPTER,
                    IridiumGearRegistry.AMETHYST_SOCKETED_IRIDIUM_SCEPTER,
                    IridiumGearRegistry.DIAMOND_SOCKETED_IRIDIUM_CHESTPLATE,
                    IridiumGearRegistry.EMERALD_SOCKETED_IRIDIUM_CHESTPLATE,
                    IridiumGearRegistry.SKULL_SOCKETED_IRIDIUM_CHESTPLATE,
                    IridiumGearRegistry.WITHER_SKULL_SOCKETED_IRIDIUM_CHESTPLATE,
                    IridiumGearRegistry.AMETHYST_SOCKETED_IRIDIUM_CHESTPLATE
            ).map((it) -> it.get().getDefaultInstance()).collect(Collectors.toList()));
        } else if (event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
            event.acceptAll(Stream.of(
                    ItemRegistry.IRIDIUM_PLATING,
                    ItemRegistry.RAW_IRIDIUM
            ).map((it) -> it.get().getDefaultInstance()).collect(Collectors.toList()));
            event.acceptAll(ItemRegistry.IRIDIUM_GEAR_BLUEPRINTS.stream().map((it) -> it.get().getDefaultInstance()).collect(Collectors.toList()));
            event.acceptAll(ItemRegistry.SOCKET_SCHEMATICS.stream().map((it) -> it.get().getDefaultInstance()).collect(Collectors.toList()));
        } else if (event.getTabKey() == CreativeModeTabs.NATURAL_BLOCKS) {
            event.acceptAll(Stream.of(
                    ItemRegistry.RAW_IRIDIUM_BLOCK_ITEM
            ).map((it) -> it.get().getDefaultInstance()).collect(Collectors.toList()));
        } else if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            event.acceptAll(Stream.of(
                    IridiumGearRegistry.IRIDIUM_PICKAXE,
                    IridiumGearRegistry.IRIDIUM_AXE,
                    IridiumGearRegistry.IRIDIUM_SHOVEL,
                    IridiumGearRegistry.IRIDIUM_HOE,
                    IridiumGearRegistry.IRIDIUM_SCEPTER,
                    IridiumGearRegistry.DIAMOND_SOCKETED_IRIDIUM_PICKAXE,
                    IridiumGearRegistry.EMERALD_SOCKETED_IRIDIUM_PICKAXE,
                    IridiumGearRegistry.SKULL_SOCKETED_IRIDIUM_PICKAXE,
                    IridiumGearRegistry.WITHER_SKULL_SOCKETED_IRIDIUM_PICKAXE,
                    IridiumGearRegistry.AMETHYST_SOCKETED_IRIDIUM_PICKAXE,
                    IridiumGearRegistry.DIAMOND_SOCKETED_IRIDIUM_AXE,
                    IridiumGearRegistry.EMERALD_SOCKETED_IRIDIUM_AXE,
                    IridiumGearRegistry.SKULL_SOCKETED_IRIDIUM_AXE,
                    IridiumGearRegistry.WITHER_SKULL_SOCKETED_IRIDIUM_AXE,
                    IridiumGearRegistry.AMETHYST_SOCKETED_IRIDIUM_AXE,
                    IridiumGearRegistry.DIAMOND_SOCKETED_IRIDIUM_SHOVEL,
                    IridiumGearRegistry.EMERALD_SOCKETED_IRIDIUM_SHOVEL,
                    IridiumGearRegistry.SKULL_SOCKETED_IRIDIUM_SHOVEL,
                    IridiumGearRegistry.WITHER_SKULL_SOCKETED_IRIDIUM_SHOVEL,
                    IridiumGearRegistry.AMETHYST_SOCKETED_IRIDIUM_SHOVEL,
                    IridiumGearRegistry.DIAMOND_SOCKETED_IRIDIUM_HOE,
                    IridiumGearRegistry.EMERALD_SOCKETED_IRIDIUM_HOE,
                    IridiumGearRegistry.SKULL_SOCKETED_IRIDIUM_HOE,
                    IridiumGearRegistry.WITHER_SKULL_SOCKETED_IRIDIUM_HOE,
                    IridiumGearRegistry.AMETHYST_SOCKETED_IRIDIUM_HOE,
                    IridiumGearRegistry.DIAMOND_SOCKETED_IRIDIUM_SCEPTER,
                    IridiumGearRegistry.EMERALD_SOCKETED_IRIDIUM_SCEPTER,
                    IridiumGearRegistry.SKULL_SOCKETED_IRIDIUM_SCEPTER,
                    IridiumGearRegistry.WITHER_SKULL_SOCKETED_IRIDIUM_SCEPTER,
                    IridiumGearRegistry.AMETHYST_SOCKETED_IRIDIUM_SCEPTER,
                    ItemRegistry.TRANSMUTATION_CHARGE
            ).map((it) -> it.get().getDefaultInstance()).collect(Collectors.toList()));
        }
    }
}
