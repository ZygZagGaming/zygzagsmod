package io.github.zygzaggaming.zygzagsmod.common;

import com.mojang.logging.LogUtils;
import io.github.zygzaggaming.zygzagsmod.common.capability.PlayerSightCache;
import io.github.zygzaggaming.zygzagsmod.common.entity.animation.Action;
import io.github.zygzaggaming.zygzagsmod.common.entity.animation.TransitionAction;
import io.github.zygzaggaming.zygzagsmod.common.item.iridium.ISocketable;
import io.github.zygzaggaming.zygzagsmod.common.registry.*;
import io.github.zygzaggaming.zygzagsmod.common.registry.base.AkomiRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.BrushItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import org.slf4j.Logger;

import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Mod(Main.MODID)
public class Main {

    public static final EnchantmentCategory COOLDOWN_CATEGORY = EnchantmentCategory.create("cooldown", (item) -> item instanceof ISocketable socketable && socketable.hasCooldown());
    public static final EnchantmentCategory BRUSH_CATEGORY = EnchantmentCategory.create("brush", (item) -> item instanceof BrushItem);
    public static final EnchantmentCategory SWORD_OR_AXE = EnchantmentCategory.create("sword_or_axe", (item) -> item instanceof SwordItem || item instanceof AxeItem);
    public static final EnchantmentCategory AXE = EnchantmentCategory.create("axe", (item) -> item instanceof AxeItem);
    public static final String MODID = "zygzagsmod";
    @SuppressWarnings("unused")
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final TagKey<Block> NEEDS_IRIDIUM_TOOL_TAG = BlockTags.create(new ResourceLocation("zygzagsmod:needs_iridium_tool"));
    public static final TagKey<Block> VEGETATION_TAG = BlockTags.create(new ResourceLocation("zygzagsmod:vegetation"));
    //public static final TagKey<EntityType<?>> VILLAGER_HATED = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation("zygzagsmod:villager_hated"));
    public static final TagKey<Block> VEINMINEABLE = TagKey.create(Registries.BLOCK, new ResourceLocation("zygzagsmod:veinmineable"));
    //public static final TagKey<Block> NETHER_QUARTZ_GLASS = TagKey.create(Registries.BLOCK, new ResourceLocation("zygzagsmod:nether_quartz_glass"));
    public static final TagKey<Block> SCULK_VEIN_LIKE = TagKey.create(BuiltInRegistries.BLOCK.key(), new ResourceLocation("zygzagsmod:sculk_vein_like"));
    public static final TagKey<Block> REPLACEABLE_BY_CAIRN = TagKey.create(BuiltInRegistries.BLOCK.key(), new ResourceLocation("zygzagsmod:replaceable_by_cairn"));
    public static final TagKey<Block> WORLD_CONTAINERS = TagKey.create(BuiltInRegistries.BLOCK.key(), new ResourceLocation("zygzagsmod:world_containers"));
    public static final TagKey<EntityType<?>> BOSS_TAG = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation("zygzagsmod:bosses"));
    public static final TagKey<EntityType<?>> SCULK_JAW_IMMUNE = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation("zygzagsmod:sculk_jaw_immune"));

    public static final TagKey<Structure> CAIRN_BASIC = TagKey.create(Registries.STRUCTURE, new ResourceLocation("zygzagsmod:cairn/basic"));

    public static final UUID SPRINGS_ENCHANTMENT_MODIFIER_UUID = UUID.fromString("488b16bb-d01c-49bb-adf2-714c230d035f");
    public static final UUID STEADY_ENCHANTMENT_MODIFIER_UUID = UUID.fromString("4c0789ca-3380-416b-8d3e-723b24d272c2");
    public static final UUID CURSE_OF_GLASS_ENCHANTMENT_HEALTH_MODIFIER_UUID = UUID.fromString("82d7ee30-93bc-448b-b16f-77097f9625ec");
    public static final UUID CURSE_OF_GLASS_ENCHANTMENT_DAMAGE_MODIFIER_UUID = UUID.fromString("4df8ccef-9fee-432a-b3a7-f0178c6f6bfe");
    public static final UUID CRITICAL_ENCHANTMENT_CRIT_DAMAGE_MODIFIER_UUID = UUID.fromString("218fd817-a62a-4911-b1a0-7d5789d6f923");
    public static final UUID COTV_ENCHANTMENT_CRIT_DAMAGE_MODIFIER_UUID = UUID.fromString("f7ec1ea1-ab1a-4679-9a22-d6ea95f0e1b2");
    public static final UUID COTV_ENCHANTMENT_ATTACK_SPEED_MODIFIER_UUID = UUID.fromString("997231b1-3775-43f8-a4e9-5c35307fc50e");
    public static final UUID SPRINT_SPEED_ATTRIBUTE_MOVEMENT_SPEED_MODIFIER_UUID = UUID.fromString("5c8f65f0-903e-4af4-ba04-7ab20dea2a44");
    public static final UUID SPRINT_ENCHANTMENT_SPRINT_SPEED_MODIFIER_UUID = UUID.fromString("92cfc16e-65b4-4c09-90d3-1e226562fe1c");
    public static final UUID SPRINT_ENCHANTMENT_SPRINT_HUNGER_CONSUMPTION_MODIFIER_UUID = UUID.fromString("33e11794-56e2-4e54-b83b-af12df29549c");
    public static final UUID SPRINT_ENCHANTMENT_SPRINT_JUMP_HUNGER_CONSUMPTION_MODIFIER_UUID = UUID.fromString("9f93b995-da40-4674-be20-58ef794e2827");
    public static final UUID SHATTER_ENCHANTMENT_ARMOR_DURABILITY_REDUCTION_MODIFIER_UUID = UUID.fromString("dcfd17cb-8a70-48e1-a2e8-f23ffe3cab4a");
    public static final UUID BASE_FLAIL_DAMAGE_UUID = UUID.fromString("d43f7bc2-9936-4646-ae0e-58377eca3313");
    public static PlayerSightCache CURRENT_PLAYER_CACHE = null;

    public static final BlockSetType NETHER_BRICK_BLOCK_SET_TYPE = BlockSetType.register(
            new BlockSetType(
                    "nether_bricks",
                    true,
                    true,
                    false,
                    BlockSetType.PressurePlateSensitivity.MOBS,
                    SoundType.STONE,
                    SoundEvents.IRON_DOOR_CLOSE,
                    SoundEvents.IRON_DOOR_OPEN,
                    SoundEvents.IRON_TRAPDOOR_CLOSE,
                    SoundEvents.IRON_TRAPDOOR_OPEN,
                    SoundEvents.STONE_PRESSURE_PLATE_CLICK_OFF,
                    SoundEvents.STONE_PRESSURE_PLATE_CLICK_ON,
                    SoundEvents.STONE_BUTTON_CLICK_OFF,
                    SoundEvents.STONE_BUTTON_CLICK_ON
            )
    );
    public static final BlockSetType RED_NETHER_BRICK_BLOCK_SET_TYPE = BlockSetType.register(
            new BlockSetType(
                    "red_nether_bricks",
                    true,
                    true,
                    false,
                    BlockSetType.PressurePlateSensitivity.MOBS,
                    SoundType.STONE,
                    SoundEvents.IRON_DOOR_CLOSE,
                    SoundEvents.IRON_DOOR_OPEN,
                    SoundEvents.IRON_TRAPDOOR_CLOSE,
                    SoundEvents.IRON_TRAPDOOR_OPEN,
                    SoundEvents.STONE_PRESSURE_PLATE_CLICK_OFF,
                    SoundEvents.STONE_PRESSURE_PLATE_CLICK_ON,
                    SoundEvents.STONE_BUTTON_CLICK_OFF,
                    SoundEvents.STONE_BUTTON_CLICK_ON
            )
    );

    public static final Supplier<Map<BlockState, Integer>> EXTRANEOUS_SCULK_GROWTHS = () -> Map.of(
            Blocks.SCULK_SENSOR.defaultBlockState(), 10,
            Blocks.SCULK_SHRIEKER.defaultBlockState(), 1,
            BlockItemEntityRegistry.SCULK_JAW.getDefaultBlockState(), 2
    );

    public Main(IEventBus modEventBus) {
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::clientSetup);
        BuiltInRegistries.BLOCK.addAlias(new ResourceLocation(MODID, "red_nether_brick_pillar"), new ResourceLocation(MODID, "girded_red_nether_bricks"));
        BuiltInRegistries.ITEM.addAlias(new ResourceLocation(MODID, "red_nether_brick_pillar"), new ResourceLocation(MODID, "girded_red_nether_bricks"));

        AkomiRegistry.register(modEventBus);

        //NeoForge.EVENT_BUS.register(this);

        modEventBus.addListener(this::addCreative);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    public static Registry<Action> actionRegistry() {
        return ActionRegistry.registry();
    }

    public static Registry<TransitionAction> transitionActionRegistry() {
        return TransitionActionRegistry.registry();
    }

    private void commonSetup(final FMLCommonSetupEvent event) { }

    private void clientSetup(final FMLClientSetupEvent event) {
        CURRENT_PLAYER_CACHE = new PlayerSightCache();
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
            event.acceptAll(ItemRegistry.IRIDIUM_GEAR_BLUEPRINTS.values().stream().map((it) -> it.get().getDefaultInstance()).collect(Collectors.toList()));
            event.acceptAll(ItemRegistry.SOCKET_SCHEMATICS.values().stream().map((it) -> it.get().getDefaultInstance()).collect(Collectors.toList()));
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
