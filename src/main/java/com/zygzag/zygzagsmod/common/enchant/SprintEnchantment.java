package com.zygzag.zygzagsmod.common.enchant;

import com.google.common.collect.Multimap;
import com.zygzag.zygzagsmod.common.Main;
import com.zygzag.zygzagsmod.common.registry.AttributeRegistry;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class SprintEnchantment extends CustomEnchantment {
    public static final float[] SPRINT_SPEED_MULTIPLIERS = { 0.05f, 0.1f, 0.2f, 0.4f, 0.5f };
    public static final float[] SPRINT_HUNGER_CONSUMPTION_MULTIPLIERS = { -0.02f, -0.05f, -0.1f, -0.2f, -0.25f };
    public static final float[] SPRINT_JUMP_HUNGER_CONSUMPTION_MULTIPLIERS = { -0.06f, -0.1f, -0.2f, -0.4f, -0.5f };
    public SprintEnchantment(Rarity rarity) {
        super(rarity, EnchantmentCategory.ARMOR_FEET, new EquipmentSlot[]{EquipmentSlot.FEET});
    }

    @Override
    public int getMinCost(int level) {
        return 10 + 10 * level;
    }

    @Override
    public int getMaxCost(int level) {
        return 15 * level;
    }

    @Override
    public int getMaxLevel() {
        return 5;
    }

    @Override
    public void setupAttributes(Multimap<Attribute, AttributeModifier> attributeMap, EquipmentSlot slot, int level) {
        if (slot == EquipmentSlot.FEET) {
            attributeMap.put(
                    AttributeRegistry.SPRINT_SPEED.get(),
                    new AttributeModifier(
                            Main.SPRINT_ENCHANTMENT_SPRINT_SPEED_MODIFIER_UUID,
                            "Sprint enchantment modifier",
                            SPRINT_SPEED_MULTIPLIERS[level - 1],
                            AttributeModifier.Operation.MULTIPLY_BASE
                    )
            );
            attributeMap.put(
                    AttributeRegistry.SPRINT_HUNGER_CONSUMPTION.get(),
                    new AttributeModifier(
                            Main.SPRINT_ENCHANTMENT_SPRINT_HUNGER_CONSUMPTION_MODIFIER_UUID,
                            "Sprint enchantment modifier",
                            SPRINT_HUNGER_CONSUMPTION_MULTIPLIERS[level - 1],
                            AttributeModifier.Operation.MULTIPLY_BASE
                    )
            );
            attributeMap.put(
                    AttributeRegistry.SPRINT_JUMP_HUNGER_CONSUMPTION.get(),
                    new AttributeModifier(
                            Main.SPRINT_ENCHANTMENT_SPRINT_JUMP_HUNGER_CONSUMPTION_MODIFIER_UUID,
                            "Sprint enchantment modifier",
                            SPRINT_JUMP_HUNGER_CONSUMPTION_MULTIPLIERS[level - 1],
                            AttributeModifier.Operation.MULTIPLY_BASE
                    )
            );
        }
    }

    @Override
    public boolean isTradeable() {
        return false;
    }
}
