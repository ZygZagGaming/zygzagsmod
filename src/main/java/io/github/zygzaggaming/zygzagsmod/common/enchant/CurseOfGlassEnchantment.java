package io.github.zygzaggaming.zygzagsmod.common.enchant;

import com.google.common.collect.Multimap;
import io.github.zygzaggaming.zygzagsmod.common.Main;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class CurseOfGlassEnchantment extends CustomEnchantment {
    public CurseOfGlassEnchantment(Rarity rarity) {
        super(rarity, EnchantmentCategory.ARMOR_CHEST, new EquipmentSlot[]{EquipmentSlot.CHEST});
    }

    @Override
    public int getMinCost(int level) {
        return 7;
    }

    @Override
    public int getMaxCost(int level) {
        return 10;
    }

    @Override
    public void setupAttributes(Multimap<Attribute, AttributeModifier> attributeMap, EquipmentSlot slot, int level) {
        if (slot == EquipmentSlot.CHEST) {
            attributeMap.put(Attributes.MAX_HEALTH, new AttributeModifier(Main.CURSE_OF_GLASS_ENCHANTMENT_HEALTH_MODIFIER_UUID, "Curse of Glass enchantment modifier", -8, AttributeModifier.Operation.ADDITION));
            attributeMap.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(Main.CURSE_OF_GLASS_ENCHANTMENT_DAMAGE_MODIFIER_UUID, "Curse of Glass enchantment modifier", 2, AttributeModifier.Operation.MULTIPLY_TOTAL));
        }
    }

    @Override
    public boolean isCurse() {
        return true;
    }
}
