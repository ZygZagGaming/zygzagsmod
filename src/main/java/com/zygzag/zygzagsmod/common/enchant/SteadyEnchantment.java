package com.zygzag.zygzagsmod.common.enchant;

import com.google.common.collect.Multimap;
import com.zygzag.zygzagsmod.common.Main;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class SteadyEnchantment extends CustomEnchantment {
    public SteadyEnchantment(Enchantment.Rarity rarity) {
        super(rarity, EnchantmentCategory.ARMOR_FEET, new EquipmentSlot[]{EquipmentSlot.FEET});
    }

    @Override
    public int getMinCost(int level) {
        return 5 + level;
    }

    @Override
    public int getMaxCost(int level) {
        return 10 + 4 * level;
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public void setupAttributes(Multimap<Attribute, AttributeModifier> attributeMap, EquipmentSlot slot, int level) {
        if (slot == EquipmentSlot.FEET) {
            var kbRes = level == 1 ? 0.05 : level == 2 ? 0.1 : 0.2;
            attributeMap.put(Attributes.KNOCKBACK_RESISTANCE, new AttributeModifier(Main.STEADY_ENCHANTMENT_MODIFIER_UUID, "Steady enchantment modifier", kbRes, AttributeModifier.Operation.ADDITION));
        }
    }
}
