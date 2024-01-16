package com.zygzag.zygzagsmod.common.enchant;

import com.google.common.collect.Multimap;
import com.zygzag.zygzagsmod.common.UUIDs;
import com.zygzag.zygzagsmod.common.registry.AttributeRegistry;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class SpringsEnchantment extends CustomEnchantment {
    public static final float[] JUMP_HEIGHT_MULTIPLIERS = { 0.1f, 0.2f, 0.3f, 0.5f, 0.75f };
    public SpringsEnchantment(Rarity rarity) {
        super(rarity, EnchantmentCategory.ARMOR_LEGS, new EquipmentSlot[]{EquipmentSlot.LEGS});
    }

    @Override
    public int getMinCost(int level) {
        return 15 + 3 * level;
    }

    @Override
    public int getMaxCost(int level) {
        return 15 + 5 * level;
    }

    @Override
    public int getMaxLevel() {
        return 5;
    }

    @Override
    public void setupAttributes(Multimap<Attribute, AttributeModifier> attributeMap, EquipmentSlot slot, int level) {
        if (slot == EquipmentSlot.LEGS) {
            attributeMap.put(AttributeRegistry.JUMP_POWER.get(), new AttributeModifier(UUIDs.SPRINGS_ENCHANTMENT_MODIFIER_UUID, "Springs enchantment modifier", JUMP_HEIGHT_MULTIPLIERS[level - 1], AttributeModifier.Operation.MULTIPLY_BASE));
        }
    }
}
