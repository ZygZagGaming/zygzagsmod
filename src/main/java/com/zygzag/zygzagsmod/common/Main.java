package com.zygzag.zygzagsmod.common;

import com.mojang.logging.LogUtils;
import com.zygzag.zygzagsmod.common.capability.PlayerSightCache;
import com.zygzag.zygzagsmod.common.entity.animation.Animation;
import com.zygzag.zygzagsmod.common.entity.animation.TransitionAnimation;
import com.zygzag.zygzagsmod.common.item.iridium.ISocketable;
import com.zygzag.zygzagsmod.common.registry.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.StructureTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BrushItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.Structure;
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
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import org.slf4j.Logger;

import java.util.Map;
import java.util.function.Supplier;
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
    public static final TagKey<Block> SCULK_VEIN_LIKE = TagKey.create(ForgeRegistries.Keys.BLOCKS, new ResourceLocation("zygzagsmod:sculk_vein_like"));
    public static final TagKey<EntityType<?>> BOSS_TAG = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation("zygzagsmod:bosses"));
    public static final TagKey<EntityType<?>> SCULK_JAW_IMMUNE = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation("zygzagsmod:sculk_jaw_immune"));

    public static final TagKey<Structure> CAIRN_LOCATED = TagKey.create(Registries.STRUCTURE, new ResourceLocation("zygzagsmod:cairn_located"));

    public static final Capability<PlayerSightCache> PLAYER_SIGHT_CACHE = CapabilityManager.get(new CapabilityToken<>() {
    });

    public static final Supplier<Map<BlockState, Integer>> EXTRANEOUS_SCULK_GROWTHS = () -> Map.of(
            Blocks.SCULK_SENSOR.defaultBlockState(), 10,
            Blocks.SCULK_SHRIEKER.defaultBlockState(), 1,
            BlockItemEntityRegistry.SCULK_JAW.getDefaultBlockState(), 2
    );

    public Main() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::clientSetup);

        Registry.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(this);

        modEventBus.addListener(this::addCreative);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);

        //System.out.println("Grass is " + (Blocks.GRASS.defaultBlockState().is(VEGETATION_TAG) ? "" : "not ") + "in the vegetation tag");
    }

    public static IForgeRegistry<Animation> animationRegistry() {
        return AnimationRegistry.FORGE_REGISTRY_SUPPLIER.get();
    }

    public static IForgeRegistry<TransitionAnimation> transitionAnimationRegistry() {
        return TransitionAnimationRegistry.FORGE_REGISTRY_SUPPLIER.get();
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
    }

    private void clientSetup(final FMLClientSetupEvent event) {
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.COMBAT) {
            event.acceptAll(Stream.of(
                    IridiumGearRegistry.IRIDIUM_HELMET,
                    IridiumGearRegistry.IRIDIUM_LEGGINGS,
                    IridiumGearRegistry.IRIDIUM_BOOTS
            ).map((it) -> it.get().getDefaultInstance()).collect(Collectors.toList()));
            event.acceptAll(IridiumGearRegistry.SWORDS.values().stream().map((it) -> it.get().getDefaultInstance()).collect(Collectors.toList()));
            event.acceptAll(IridiumGearRegistry.PICKAXES.values().stream().map((it) -> it.get().getDefaultInstance()).collect(Collectors.toList()));
            event.acceptAll(IridiumGearRegistry.AXES.values().stream().map((it) -> it.get().getDefaultInstance()).collect(Collectors.toList()));
            event.acceptAll(IridiumGearRegistry.SHOVELS.values().stream().map((it) -> it.get().getDefaultInstance()).collect(Collectors.toList()));
            event.acceptAll(IridiumGearRegistry.HOES.values().stream().map((it) -> it.get().getDefaultInstance()).collect(Collectors.toList()));
            event.acceptAll(IridiumGearRegistry.CHESTPLATES.values().stream().map((it) -> it.get().getDefaultInstance()).collect(Collectors.toList()));
            event.acceptAll(IridiumGearRegistry.SCEPTERS.values().stream().map((it) -> it.get().getDefaultInstance()).collect(Collectors.toList()));
        } else if (event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
            event.acceptAll(Stream.of(
                    ItemRegistry.IRIDIUM_PLATING,
                    ItemRegistry.RAW_IRIDIUM
            ).map((it) -> it.get().getDefaultInstance()).collect(Collectors.toList()));
            event.acceptAll(ItemRegistry.IRIDIUM_GEAR_BLUEPRINTS.stream().map((it) -> it.get().getDefaultInstance()).collect(Collectors.toList()));
            event.acceptAll(ItemRegistry.SOCKET_SCHEMATICS.stream().map((it) -> it.get().getDefaultInstance()).collect(Collectors.toList()));
        } else if (event.getTabKey() == CreativeModeTabs.NATURAL_BLOCKS) {
            event.acceptAll(Stream.of(
                    BlockWithItemRegistry.RAW_IRIDIUM_BLOCK.item(),
                    BlockItemEntityRegistry.SCULK_JAW.item()
            ).map((it) -> it.get().getDefaultInstance()).collect(Collectors.toList()));
        } else if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            event.acceptAll(Stream.of(
                    ItemRegistry.TRANSMUTATION_CHARGE
            ).map((it) -> it.get().getDefaultInstance()).collect(Collectors.toList()));
            event.acceptAll(IridiumGearRegistry.PICKAXES.values().stream().map((it) -> it.get().getDefaultInstance()).collect(Collectors.toList()));
            event.acceptAll(IridiumGearRegistry.AXES.values().stream().map((it) -> it.get().getDefaultInstance()).collect(Collectors.toList()));
            event.acceptAll(IridiumGearRegistry.SHOVELS.values().stream().map((it) -> it.get().getDefaultInstance()).collect(Collectors.toList()));
            event.acceptAll(IridiumGearRegistry.HOES.values().stream().map((it) -> it.get().getDefaultInstance()).collect(Collectors.toList()));
        } else if (event.getTabKey() == CreativeModeTabs.SPAWN_EGGS) {
            event.acceptAll(Stream.of(
                    ItemRegistry.IRIDIUM_GOLEM_SPAWN_EGG,
                    ItemRegistry.BLAZE_SENTRY_SPAWN_EGG
            ).map((it) -> it.get().getDefaultInstance()).collect(Collectors.toSet()));
        }
    }
}
