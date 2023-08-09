package com.zygzag.zygzagsmod.common.tier;

import com.mojang.blaze3d.MethodsReturnNonnullByDefault;
import com.zygzag.zygzagsmod.common.registries.ItemRegistry;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.function.Supplier;

@SuppressWarnings("unused")
@MethodsReturnNonnullByDefault
public enum IridiumArmorMaterial implements ArmorMaterial {
    IRIDIUM("iridium", 42, new int[]{4, 7, 9, 4}, 17, SoundEvents.ARMOR_EQUIP_NETHERITE, 3.0F, 0.2F, () -> {
        return Ingredient.of(ItemRegistry.IRIDIUM_PLATING.get());
    }),
    _4_1("iridium_1", 38, new int[]{3, 6, 8, 3}, 15, SoundEvents.ARMOR_EQUIP_NETHERITE, 3.0F, 0.2F, () -> {
        return Ingredient.of(ItemRegistry.IRIDIUM_PLATING.get());
    }),
    _4_2("iridium_2", 40, new int[]{4, 7, 9, 4}, 16, SoundEvents.ARMOR_EQUIP_NETHERITE, 3.0F, 0.2F, () -> {
        return Ingredient.of(ItemRegistry.IRIDIUM_PLATING.get());
    }),
    _4_3("iridium_3", 41, new int[]{4, 7, 9, 4}, 17, SoundEvents.ARMOR_EQUIP_NETHERITE, 3.0F, 0.2F, () -> {
        return Ingredient.of(ItemRegistry.IRIDIUM_PLATING.get());
    }),
    _5_1("iridium_1", 38, new int[]{3, 6, 8, 3}, 15, SoundEvents.ARMOR_EQUIP_NETHERITE, 3.0F, 0.2F, () -> {
        return Ingredient.of(ItemRegistry.IRIDIUM_PLATING.get());
    }),
    _5_2("iridium_2", 39, new int[]{3, 6, 8, 3}, 16, SoundEvents.ARMOR_EQUIP_NETHERITE, 3.0F, 0.2F, () -> {
        return Ingredient.of(ItemRegistry.IRIDIUM_PLATING.get());
    }),
    _5_3("iridium_3", 40, new int[]{4, 7, 9, 4}, 16, SoundEvents.ARMOR_EQUIP_NETHERITE, 3.0F, 0.2F, () -> {
        return Ingredient.of(ItemRegistry.IRIDIUM_PLATING.get());
    }),
    _5_4("iridium_4", 41, new int[]{4, 7, 9, 4}, 17, SoundEvents.ARMOR_EQUIP_NETHERITE, 3.0F, 0.2F, () -> {
        return Ingredient.of(ItemRegistry.IRIDIUM_PLATING.get());
    }),
    _7_1("iridium_1", 38, new int[]{3, 6, 8, 3}, 15, SoundEvents.ARMOR_EQUIP_NETHERITE, 3.0F, 0.2F, () -> {
        return Ingredient.of(ItemRegistry.IRIDIUM_PLATING.get());
    }),
    _7_2("iridium_2", 39, new int[]{3, 6, 8, 3}, 15, SoundEvents.ARMOR_EQUIP_NETHERITE, 3.0F, 0.2F, () -> {
        return Ingredient.of(ItemRegistry.IRIDIUM_PLATING.get());
    }),
    _7_3("iridium_3", 39, new int[]{3, 6, 8, 3}, 16, SoundEvents.ARMOR_EQUIP_NETHERITE, 3.0F, 0.2F, () -> {
        return Ingredient.of(ItemRegistry.IRIDIUM_PLATING.get());
    }),
    _7_4("iridium_4", 40, new int[]{4, 7, 9, 4}, 16, SoundEvents.ARMOR_EQUIP_NETHERITE, 3.0F, 0.2F, () -> {
        return Ingredient.of(ItemRegistry.IRIDIUM_PLATING.get());
    }),
    _7_5("iridium_5", 40, new int[]{4, 7, 9, 4}, 17, SoundEvents.ARMOR_EQUIP_NETHERITE, 3.0F, 0.2F, () -> {
        return Ingredient.of(ItemRegistry.IRIDIUM_PLATING.get());
    }),
    _7_6("iridium_6", 41, new int[]{4, 7, 9, 4}, 17, SoundEvents.ARMOR_EQUIP_NETHERITE, 3.0F, 0.2F, () -> {
        return Ingredient.of(ItemRegistry.IRIDIUM_PLATING.get());
    }),
    _8_1("iridium_1", 38, new int[]{3, 6, 8, 3}, 15, SoundEvents.ARMOR_EQUIP_NETHERITE, 3.0F, 0.2F, () -> {
        return Ingredient.of(ItemRegistry.IRIDIUM_PLATING.get());
    }),
    _8_2("iridium_2", 38, new int[]{3, 6, 8, 3}, 15, SoundEvents.ARMOR_EQUIP_NETHERITE, 3.0F, 0.2F, () -> {
        return Ingredient.of(ItemRegistry.IRIDIUM_PLATING.get());
    }),
    _8_3("iridium_3", 39, new int[]{3, 6, 8, 3}, 15, SoundEvents.ARMOR_EQUIP_NETHERITE, 3.0F, 0.2F, () -> {
        return Ingredient.of(ItemRegistry.IRIDIUM_PLATING.get());
    }),
    _8_4("iridium_4", 39, new int[]{3, 6, 8, 3}, 16, SoundEvents.ARMOR_EQUIP_NETHERITE, 3.0F, 0.2F, () -> {
        return Ingredient.of(ItemRegistry.IRIDIUM_PLATING.get());
    }),
    _8_5("iridium_5", 40, new int[]{4, 7, 9, 4}, 16, SoundEvents.ARMOR_EQUIP_NETHERITE, 3.0F, 0.2F, () -> {
        return Ingredient.of(ItemRegistry.IRIDIUM_PLATING.get());
    }),
    _8_6("iridium_6", 41, new int[]{4, 7, 9, 4}, 17, SoundEvents.ARMOR_EQUIP_NETHERITE, 3.0F, 0.2F, () -> {
        return Ingredient.of(ItemRegistry.IRIDIUM_PLATING.get());
    }),
    _8_7("iridium_7", 41, new int[]{4, 7, 9, 4}, 17, SoundEvents.ARMOR_EQUIP_NETHERITE, 3.0F, 0.2F, () -> {
        return Ingredient.of(ItemRegistry.IRIDIUM_PLATING.get());
    }),
    DIAMOND_SOCKETED("diamond_iridium", 45, new int[]{8, 8, 8, 8}, 12, SoundEvents.ARMOR_EQUIP_NETHERITE, 3.5f, 0.4f, () -> {
        return Ingredient.of(ItemRegistry.IRIDIUM_PLATING.get());
    }),
    EMERALD_SOCKETED("emerald_iridium", 38, new int[]{7, 7, 7, 7}, 19, SoundEvents.ARMOR_EQUIP_NETHERITE, 3.0f, 0.6f, () -> {
        return Ingredient.of(ItemRegistry.IRIDIUM_PLATING.get());
    });

    private static final int[] HEALTH_PER_SLOT = new int[]{13, 15, 16, 11};
    private final String name;
    private final int durabilityMultiplier;
    private final int[] slotProtections;
    private final int enchantmentValue;
    private final SoundEvent sound;
    private final float toughness;
    private final float knockbackResistance;
    private final LazyLoadedValue<Ingredient> repairIngredient;

    IridiumArmorMaterial(String name, int durability, int[] slotProtections, int enchantValue, SoundEvent sound, float toughness, float kbRes, Supplier<Ingredient> repair) {
        this.name = name;
        this.durabilityMultiplier = durability;
        this.slotProtections = slotProtections;
        this.enchantmentValue = enchantValue;
        this.sound = sound;
        this.toughness = toughness;
        this.knockbackResistance = kbRes;
        this.repairIngredient = new LazyLoadedValue<>(repair);
    }

    public int getDurabilityForType(ArmorItem.Type type) {
        return HEALTH_PER_SLOT[type.ordinal()] * this.durabilityMultiplier;
    }

    public int getDefenseForType(ArmorItem.Type type) {
        return this.slotProtections[type.ordinal()];
    }

    public int getEnchantmentValue() {
        return this.enchantmentValue;
    }

    public SoundEvent getEquipSound() {
        return this.sound;
    }

    public Ingredient getRepairIngredient() {
        return this.repairIngredient.get();
    }

    @OnlyIn(Dist.CLIENT)
    public String getName() {
        return this.name;
    }

    public float getToughness() {
        return this.toughness;
    }

    public float getKnockbackResistance() {
        return this.knockbackResistance;
    }
}
