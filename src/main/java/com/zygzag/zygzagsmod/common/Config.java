package com.zygzag.zygzagsmod.common;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@Mod.EventBusSubscriber(modid = Main.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    private static final ForgeConfigSpec.IntValue AMETHYST_PICKAXE_COOLDOWN = BUILDER
            .comment("Cooldown for Amethyst-Socketed Iridium Pickaxes in ticks")
            .defineInRange("amethystPickaxeCooldown", 15 * 30, 0, Integer.MAX_VALUE);

    private static final ForgeConfigSpec.IntValue WITHER_SKULL_AXE_COOLDOWN = BUILDER
            .comment("Cooldown for Wither Skull-Socketed Iridium Axes in ticks")
            .defineInRange("witherSkullAxeCooldown", 15 * 30, 0, Integer.MAX_VALUE);

    private static final ForgeConfigSpec.DoubleValue AMETHYST_AXE_DAMAGE_BONUS = BUILDER
            .comment("Damage boost for Amethyst-Socketed Iridium Axes")
            .defineInRange("amethystAxeDamageBonus", 0.4, 0, Double.MAX_VALUE);

    private static final ForgeConfigSpec.IntValue EMERALD_SHOVEL_COOLDOWN = BUILDER
            .comment("Cooldown for Emerald-Socketed Iridium Shovels in ticks")
            .defineInRange("emeraldShovelCooldown", 5 * 20, 0, Integer.MAX_VALUE);

    private static final ForgeConfigSpec.IntValue SKULL_SHOVEL_COOLDOWN = BUILDER
            .comment("Cooldown for Skull-Socketed Iridium Shovels in ticks")
            .defineInRange("skullShovelCooldown", 15 * 30, 0, Integer.MAX_VALUE);

    private static final ForgeConfigSpec.IntValue WITHER_SKULL_SHOVEL_COOLDOWN = BUILDER
            .comment("Cooldown for Wither Skull-Socketed Iridium Shovels in ticks")
            .defineInRange("witherSkullShovelCooldown", 20 * 20, 0, Integer.MAX_VALUE);

    private static final ForgeConfigSpec.IntValue DIAMOND_SHOVEL_BLOCK_COUNT = BUILDER
            .comment("Maximum number of blocks that Diamond-Socketed Iridium Shovels can veinmine at once")
            .defineInRange("diamondShovelBlockCount", 64, 0, Integer.MAX_VALUE);

    private static final ForgeConfigSpec.DoubleValue DIAMOND_SHOVEL_DURABILITY_MULTIPLIER = BUILDER
            .comment("Multiplier to durability loss from Diamond-Socketed Iridium Shovels' veinmine ability")
            .defineInRange("diamondShovelDurabilityMultiplier", 2.0, 0, Double.MAX_VALUE);

    private static final ForgeConfigSpec.DoubleValue DIAMOND_SWORD_MAX_DAMAGE_BONUS = BUILDER
            .comment("Damage bonus for Diamond-Socketed Iridium Swords at Y=-64")
            .defineInRange("diamondSwordMaxDamageBuff", 5, Double.MIN_VALUE, Double.MAX_VALUE);

    private static final ForgeConfigSpec.DoubleValue DIAMOND_SWORD_MIN_DAMAGE_BONUS = BUILDER
            .comment("Damage bonus for Diamond-Socketed Iridium Swords at Y=320")
            .defineInRange("diamondSwordMinDamageBuff", -3, Double.MIN_VALUE, Double.MAX_VALUE);

    private static final ForgeConfigSpec.DoubleValue SKULL_SWORD_INSTAKILL_CHANCE = BUILDER
            .comment("Chance for Skull-Socketed Iridium Swords to instakill regular mobs")
            .defineInRange("skullSwordInstakillChance", 0.1, 0, 1);

    private static final ForgeConfigSpec.DoubleValue SKULL_SWORD_INSTAKILL_CHANCE_BOSSES = BUILDER
            .comment("Chance for Skull-Socketed Iridium Swords to instakill bosses")
            .defineInRange("skullSwordInstakillChanceBosses", 0.01, 0, 1);

    private static final ForgeConfigSpec.DoubleValue SKULL_SWORD_INSTAKILL_CHANCE_PLAYERS = BUILDER
            .comment("Chance for Skull-Socketed Iridium Swords to instakill players")
            .defineInRange("skullSwordInstakillChancePlayers", 0.01, 0, 1);

    private static final ForgeConfigSpec.IntValue WITHER_SKULL_SWORD_COOLDOWN = BUILDER
            .comment("Cooldown for Wither Skull-Socketed Iridium Swords in ticks")
            .defineInRange("witherSkullSwordCooldown", 1000, 0, Integer.MAX_VALUE);

    private static final ForgeConfigSpec.DoubleValue AMETHYST_SWORD_DAMAGE_BONUS = BUILDER
            .comment("Damage boost for Amethyst-Socketed Iridium Swords")
            .defineInRange("amethystSwordDamageBonus", 0.4, 0, Double.MAX_VALUE);

    private static final ForgeConfigSpec.DoubleValue EMERALD_SWORD_UNDEAD_DAMAGE_MULTIPLIER = BUILDER
            .comment("Damage multiplier for Emerald-Socketed Iridium Swords")
            .defineInRange("emeraldSwordUndeadDamageMultiplier", 2, 0, Double.MAX_VALUE);

    private static final ForgeConfigSpec.IntValue COPPER_ORE_COLOR = BUILDER
            .comment("Color for Copper Ores during Sight")
            .defineInRange("copperOreColor", 0xe77c56, 0, 0xffffff);
    private static final ForgeConfigSpec.IntValue COAL_ORE_COLOR = BUILDER
            .comment("Color for Coal Ores during Sight")
            .defineInRange("coalOreColor", 0x323232, 0, 0xffffff);
    private static final ForgeConfigSpec.IntValue IRON_ORE_COLOR = BUILDER
            .comment("Color for Iron Ores during Sight")
            .defineInRange("ironOreColor", 0xadacac, 0, 0xffffff);
    private static final ForgeConfigSpec.IntValue GOLD_ORE_COLOR = BUILDER
            .comment("Color for Gold Ores during Sight")
            .defineInRange("goldOreColor", 0xfdf55f, 0, 0xffffff);
    private static final ForgeConfigSpec.IntValue NETHERITE_ORE_COLOR = BUILDER
            .comment("Color for Netherite Ores during Sight")
            .defineInRange("netheriteOreColor", 0x654740, 0, 0xffffff);
    private static final ForgeConfigSpec.IntValue DIAMOND_ORE_COLOR = BUILDER
            .comment("Color for Diamond Ores during Sight")
            .defineInRange("diamondOreColor", 0x18a2b8, 0, 0xffffff);
    private static final ForgeConfigSpec.IntValue QUARTZ_ORE_COLOR = BUILDER
            .comment("Color for Quartz Ores during Sight")
            .defineInRange("quartzOreColor", 0xddd4c6, 0, 0xffffff);
    private static final ForgeConfigSpec.IntValue LAPIS_ORE_COLOR = BUILDER
            .comment("Color for Lapis Ores during Sight")
            .defineInRange("lapisOreColor", 0x345ec3, 0, 0xffffff);
    private static final ForgeConfigSpec.IntValue REDSTONE_ORE_COLOR = BUILDER
            .comment("Color for Redstone Ores during Sight")
            .defineInRange("redstoneOreColor", 0xaa0f01, 0, 0xffffff);
    private static final ForgeConfigSpec.IntValue EMERALD_ORE_COLOR = BUILDER
            .comment("Color for Emerald Ores during Sight")
            .defineInRange("emeraldOreColor", 0x72db58, 0, 0xffffff);

    private static final ForgeConfigSpec.IntValue MISC_ORE_COLOR = BUILDER
            .comment("Color for non-vanilla ores during Sight")
            .defineInRange("miscOreColor", 0x888888, 0, 0xffffff);

    public static final ForgeConfigSpec.IntValue SIGHT_EFFECT_COLOR = BUILDER
            .comment("Color for Sight effect")
            .defineInRange("sightEffectColor", 0x662382, 0, 0xffffff);

    public static final ForgeConfigSpec.IntValue WITHER_SKULL_AXE_RANGE = BUILDER
            .comment("Range for the ability of Wither Skull-Socketed Iridium Axes")
            .defineInRange("witherSkullAxeRange", 10, 0, Integer.MAX_VALUE);

    public static final ForgeConfigSpec.IntValue EMERALD_SCEPTER_COOLDOWN = BUILDER
            .comment("Cooldown for the ability of Emerald-Socketed Iridium Scepters")
            .defineInRange("emeraldScepterCooldown", 1000, 0, Integer.MAX_VALUE);

    public static final ForgeConfigSpec.IntValue SKULL_SCEPTER_COOLDOWN = BUILDER
            .comment("Cooldown for the ability of Skull-Socketed Iridium Scepters")
            .defineInRange("skullScepterCooldown", 400, 0, Integer.MAX_VALUE);

    public static final ForgeConfigSpec.IntValue WITHER_SKULL_SCEPTER_COOLDOWN = BUILDER
            .comment("Cooldown for the ability of Wither Skull-Socketed Iridium Scepters")
            .defineInRange("witherSkullScepterCooldown", 600, 0, Integer.MAX_VALUE);

    public static final ForgeConfigSpec.IntValue AMETHYST_SCEPTER_COOLDOWN = BUILDER
            .comment("Cooldown for the ability of Amethyst-Socketed Iridium Scepters")
            .defineInRange("amethystScepterCooldown", 400, 0, Integer.MAX_VALUE);

    static final ForgeConfigSpec SPEC = BUILDER.build();

    public static int amethystPickaxeCooldown,
            witherSkullAxeCooldown,
            emeraldShovelCooldown,
            skullShovelCooldown,
            witherSkullShovelCooldown,
            diamondShovelBlockCount,
            witherSkullSwordCooldown,
            copperOreColor,
            coalOreColor,
            ironOreColor,
            goldOreColor,
            netheriteOreColor,
            diamondOreColor,
            quartzOreColor,
            lapisOreColor,
            redstoneOreColor,
            emeraldOreColor,
            miscOreColor,
            sightEffectColor,
            witherSkullAxeRange,
            emeraldScepterCooldown,
            skullScepterCooldown,
            witherSkullScepterCooldown,
            amethystScepterCooldown;
    public static double diamondShovelDurabilityMultiplier,
            amethystAxeDamageBonus,
            amethystSwordDamageBonus,
            diamondSwordMaxDamageBonus,
            diamondSwordMinDamageBonus,
            skullSwordInstakillChance,
            skullSwordInstakillChanceBosses,
            skullSwordInstakillChancePlayers,
            emeraldSwordUndeadDamageMultiplier;


    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        amethystPickaxeCooldown = AMETHYST_PICKAXE_COOLDOWN.get();
        witherSkullAxeCooldown = WITHER_SKULL_AXE_COOLDOWN.get();
        emeraldShovelCooldown = EMERALD_SHOVEL_COOLDOWN.get();
        skullShovelCooldown = SKULL_SHOVEL_COOLDOWN.get();
        witherSkullShovelCooldown = WITHER_SKULL_SHOVEL_COOLDOWN.get();
        diamondShovelBlockCount  = DIAMOND_SHOVEL_BLOCK_COUNT.get();
        diamondShovelDurabilityMultiplier = DIAMOND_SHOVEL_DURABILITY_MULTIPLIER.get();
        witherSkullSwordCooldown = WITHER_SKULL_SWORD_COOLDOWN.get();
        copperOreColor = COPPER_ORE_COLOR.get();
        coalOreColor = COAL_ORE_COLOR.get();
        ironOreColor = IRON_ORE_COLOR.get();
        goldOreColor = GOLD_ORE_COLOR.get();
        netheriteOreColor = NETHERITE_ORE_COLOR.get();
        diamondOreColor = DIAMOND_ORE_COLOR.get();
        quartzOreColor = QUARTZ_ORE_COLOR.get();
        lapisOreColor = LAPIS_ORE_COLOR.get();
        redstoneOreColor = REDSTONE_ORE_COLOR.get();
        emeraldOreColor = EMERALD_ORE_COLOR.get();
        miscOreColor = MISC_ORE_COLOR.get();
        sightEffectColor = SIGHT_EFFECT_COLOR.get();
        witherSkullAxeRange = WITHER_SKULL_AXE_RANGE.get();
        amethystAxeDamageBonus = AMETHYST_AXE_DAMAGE_BONUS.get();
        diamondSwordMaxDamageBonus = DIAMOND_SWORD_MAX_DAMAGE_BONUS.get();
        diamondSwordMinDamageBonus = DIAMOND_SWORD_MIN_DAMAGE_BONUS.get();
        skullSwordInstakillChance = SKULL_SWORD_INSTAKILL_CHANCE.get();
        skullSwordInstakillChanceBosses = SKULL_SWORD_INSTAKILL_CHANCE_BOSSES.get();
        skullSwordInstakillChancePlayers = SKULL_SWORD_INSTAKILL_CHANCE_PLAYERS.get();
        amethystSwordDamageBonus = AMETHYST_SWORD_DAMAGE_BONUS.get();
        emeraldSwordUndeadDamageMultiplier = EMERALD_SWORD_UNDEAD_DAMAGE_MULTIPLIER.get();
        emeraldScepterCooldown = EMERALD_SCEPTER_COOLDOWN.get();
        skullScepterCooldown = SKULL_SCEPTER_COOLDOWN.get();
        witherSkullScepterCooldown = WITHER_SKULL_SCEPTER_COOLDOWN.get();
        amethystScepterCooldown = AMETHYST_SCEPTER_COOLDOWN.get();
    }
}
