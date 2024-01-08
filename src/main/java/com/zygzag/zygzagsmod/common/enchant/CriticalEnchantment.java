package com.zygzag.zygzagsmod.common.enchant;

import com.google.common.collect.Multimap;
import com.zygzag.zygzagsmod.common.Main;
import com.zygzag.zygzagsmod.common.registry.AttributeRegistry;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.Enchantments;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CriticalEnchantment extends CustomEnchantment {
    public static final float[] CRITICAL_BOOSTS = { 0.15f, 0.25f, 0.4f };
    public CriticalEnchantment(Rarity rarity) {
        super(rarity, EnchantmentCategory.WEAPON, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
    }

    @Override
    public int getMinCost(int level) {
        return 21 + 3 * level;
    }

    @Override
    public int getMaxCost(int level) {
        return 27 + level;
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public void setupAttributes(Multimap<Attribute, AttributeModifier> attributeMap, EquipmentSlot slot, int level) {
        if (slot == EquipmentSlot.MAINHAND) attributeMap.put(
            AttributeRegistry.CRIT_DAMAGE.get(),
            new AttributeModifier(
                    Main.CRITICAL_ENCHANTMENT_CRIT_DAMAGE_MODIFIER_UUID,
                    "Critical enchantment modifier",
                    CRITICAL_BOOSTS[level - 1],
                    AttributeModifier.Operation.ADDITION
            )
        );
    }

    @Override
    protected boolean checkCompatibility(Enchantment other) {
        return other != Enchantments.SHARPNESS;
    }
}
