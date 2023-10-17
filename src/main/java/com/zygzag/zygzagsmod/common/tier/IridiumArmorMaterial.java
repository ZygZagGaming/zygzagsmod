package com.zygzag.zygzagsmod.common.tier;

import com.mojang.blaze3d.MethodsReturnNonnullByDefault;
import com.zygzag.zygzagsmod.common.registry.ItemRegistry;
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
public class IridiumArmorMaterial implements ArmorMaterial {
    public static final IridiumArmorMaterial IRIDIUM = new IridiumArmorMaterial("iridium", 42, new int[]{4, 9, 7, 4}, 17);
    public static final IridiumArmorMaterial _4_1 = new IridiumArmorMaterial("iridium_1", 38, new int[]{3, 8, 6, 3}, 15);
    public static final IridiumArmorMaterial _4_2 = new IridiumArmorMaterial("iridium_2", 40, new int[]{4, 9, 7, 4}, 16);
    public static final IridiumArmorMaterial _4_3 = new IridiumArmorMaterial("iridium_3", 41, new int[]{4, 9, 7, 4}, 17);
    public static final IridiumArmorMaterial _5_1 = new IridiumArmorMaterial("iridium_1", 38, new int[]{3, 8, 6, 3}, 15);
    public static final IridiumArmorMaterial _5_2 = new IridiumArmorMaterial("iridium_2", 39, new int[]{3, 8, 6, 3}, 16);
    public static final IridiumArmorMaterial _5_3 = new IridiumArmorMaterial("iridium_3", 40, new int[]{4, 9, 7, 4}, 16);
    public static final IridiumArmorMaterial _5_4 = new IridiumArmorMaterial("iridium_4", 41, new int[]{4, 9, 7, 4}, 17);
    public static final IridiumArmorMaterial _7_1 = new IridiumArmorMaterial("iridium_1", 38, new int[]{3, 8, 6, 3}, 15);
    public static final IridiumArmorMaterial _7_2 = new IridiumArmorMaterial("iridium_2", 39, new int[]{3, 8, 6, 3}, 15);
    public static final IridiumArmorMaterial _7_3 = new IridiumArmorMaterial("iridium_3", 39, new int[]{3, 8, 6, 3}, 16);
    public static final IridiumArmorMaterial _7_4 = new IridiumArmorMaterial("iridium_4", 40, new int[]{4, 9, 7, 4}, 16);
    public static final IridiumArmorMaterial _7_5 = new IridiumArmorMaterial("iridium_5", 40, new int[]{4, 9, 7, 4}, 17);
    public static final IridiumArmorMaterial _7_6 = new IridiumArmorMaterial("iridium_6", 41, new int[]{4, 9, 7, 4}, 17);
    public static final IridiumArmorMaterial _8_1 = new IridiumArmorMaterial("iridium_1", 38, new int[]{3, 8, 6, 3}, 15);
    public static final IridiumArmorMaterial _8_2 = new IridiumArmorMaterial("iridium_2", 38, new int[]{3, 8, 6, 3}, 15);
    public static final IridiumArmorMaterial _8_3 = new IridiumArmorMaterial("iridium_3", 39, new int[]{3, 8, 6, 3}, 15);
    public static final IridiumArmorMaterial _8_4 = new IridiumArmorMaterial("iridium_4", 39, new int[]{3, 8, 6, 3}, 16);
    public static final IridiumArmorMaterial _8_5 = new IridiumArmorMaterial("iridium_5", 40, new int[]{4, 9, 7, 4}, 16);
    public static final IridiumArmorMaterial _8_6 = new IridiumArmorMaterial("iridium_6", 41, new int[]{4, 9, 7, 4}, 17);
    public static final IridiumArmorMaterial _8_7 = new IridiumArmorMaterial("iridium_7", 41, new int[]{4, 9, 7, 4}, 17);
    public static final IridiumArmorMaterial DIAMOND_SOCKETED = new IridiumArmorMaterial("diamond_iridium", 45, new int[]{9, 9, 9, 9}, 12, SoundEvents.ARMOR_EQUIP_NETHERITE, 3.5f, 0.4f, () -> {
        return Ingredient.of(ItemRegistry.IRIDIUM_PLATING.get());
    });
    public static final IridiumArmorMaterial EMERALD_SOCKETED = new IridiumArmorMaterial("emerald_iridium", 38, new int[]{6, 6, 6, 6}, 19, SoundEvents.ARMOR_EQUIP_NETHERITE, 3.0f, 0.0f, () -> {
        return Ingredient.of(ItemRegistry.IRIDIUM_PLATING.get());
    });

    private static final int[] HEALTH_PER_SLOT = new int[]{11, 16, 15, 13};
    private final String name;
    private final int durabilityMultiplier;
    private final int[] slotProtections;
    private final int enchantmentValue;
    private final SoundEvent sound;
    private final float toughness;
    private final float knockbackResistance;
    private final LazyLoadedValue<Ingredient> repairIngredient;

    IridiumArmorMaterial(String name, int durability, int[] slotProtections, int enchantValue) {
        this(name, durability, slotProtections, enchantValue, SoundEvents.ARMOR_EQUIP_NETHERITE, 3f, 0.2f, () -> Ingredient.of(ItemRegistry.IRIDIUM_PLATING.get()));
    }

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

    public static IridiumArmorMaterial getArmorForPartial(int plating, int outOf) {
        if (outOf == 4) {
            return plating == 1 ? _4_1 : plating == 2 ? _4_2 : _4_3;
        } else if (outOf == 5) {
            return plating == 1 ? _5_1 : plating == 2 ? _5_2 : plating == 3 ? _5_3 : _5_4;
        } else if (outOf == 7) {
            return plating == 1 ? _7_1 : plating == 2 ? _7_2 : plating == 3 ? _7_3 : plating == 4 ? _7_4 : plating == 5 ? _7_5 : _7_6;
        } else {
            return plating == 1 ? _8_1 : plating == 2 ? _8_2 : plating == 3 ? _8_3 : plating == 4 ? _8_4 : plating == 5 ? _8_5 : plating == 6 ? _8_6 : _8_7;
        }
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
