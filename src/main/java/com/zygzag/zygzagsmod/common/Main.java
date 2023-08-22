package com.zygzag.zygzagsmod.common;

import com.mojang.logging.LogUtils;
import com.zygzag.zygzagsmod.common.capability.PlayerSightCache;
import com.zygzag.zygzagsmod.common.item.iridium.ISocketable;
import com.zygzag.zygzagsmod.common.registries.ItemRegistry;
import com.zygzag.zygzagsmod.common.registries.SoundEventRegistry;
import com.zygzag.zygzagsmod.common.registries.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import org.slf4j.Logger;

@Mod(Main.MODID)
public class Main {

    public static final EnchantmentCategory COOLDOWN_CATEGORY = EnchantmentCategory.create("cooldown", (item) -> item instanceof ISocketable socketable && socketable.hasCooldown());
    public static final String MODID = "zygzagsmod";
    @SuppressWarnings("unused")
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final TagKey<Block> NEEDS_IRIDIUM_TOOL_TAG = BlockTags.create(new ResourceLocation("zygzagsmod:needs_iridium_tool"));
    public static final TagKey<Block> VEGETATION_TAG = BlockTags.create(new ResourceLocation("zygzagsmod:vegetation"));
    public static final TagKey<EntityType<?>> VILLAGER_HATED = TagKey.create(ForgeRegistries.Keys.ENTITY_TYPES, new ResourceLocation("zygzagsmod:villager_hated"));
    public static final TagKey<Block> VEINMINEABLE = TagKey.create(ForgeRegistries.Keys.BLOCKS, new ResourceLocation("zygzagsmod:veinmineable"));
    public static final TagKey<EntityType<?>> BOSS_TAG = TagKey.create(ForgeRegistries.Keys.ENTITY_TYPES, new ResourceLocation("zygzagsmod:bosses"));

    public static final Capability<PlayerSightCache> PLAYER_SIGHT_CACHE = CapabilityManager.get(new CapabilityToken<>(){});

    public static final CreativeModeTab TAB = new CreativeModeTab("zygzagsmod") {
        @Override
        public ItemStack makeIcon() {
            return ItemRegistry.IRIDIUM_PLATING.get().getDefaultInstance();
        }
    };

    public Main() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::clientSetup);

        Registry.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(this);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);

        //System.out.println("Grass is " + (Blocks.GRASS.defaultBlockState().is(VEGETATION_TAG) ? "" : "not ") + "in the vegetation tag");
    }

    private void commonSetup(final FMLCommonSetupEvent event) { }

    private void clientSetup(final FMLClientSetupEvent event) { }
}
