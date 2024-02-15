package com.zygzag.zygzagsmod.common.enchant;

import com.google.common.collect.Multimap;
import com.zygzag.zygzagsmod.common.Main;
import com.zygzag.zygzagsmod.common.registry.AttributeRegistry;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ShatterEnchantment extends CustomEnchantment {
    public ShatterEnchantment(Rarity rarity) {
        super(rarity, Main.SWORD_OR_AXE, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
    }

    @Override
    public int getMinCost(int level) {
        return 20;
    }

    @Override
    public int getMaxCost(int level) {
        return 30;
    }

    @Override
    public void setupAttributes(Multimap<Attribute, AttributeModifier> attributeMap, EquipmentSlot slot, int level) {
        if (slot == EquipmentSlot.MAINHAND) attributeMap.put(AttributeRegistry.ARMOR_DURABILITY_REDUCTION.get(), new AttributeModifier(Main.SHATTER_ENCHANTMENT_ARMOR_DURABILITY_REDUCTION_MODIFIER_UUID, "Shatter enchantment modifier", 2, AttributeModifier.Operation.MULTIPLY_TOTAL));
    }
}
