package com.zygzag.zygzagsmod.common.enchant;

import com.google.common.collect.Multimap;
import com.zygzag.zygzagsmod.common.Main;
import com.zygzag.zygzagsmod.common.registry.AttributeRegistry;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class CurseOfTheValkyrieEnchantment extends CustomEnchantment {
    public static final float[] CRITICAL_BOOSTS = { 0.2f, 0.6f };
    public static final float[] ATTACK_SPEED_NERFS = { -0.25f, -0.45f };
    public CurseOfTheValkyrieEnchantment(Rarity rarity) {
        super(rarity, Main.AXE, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
    }

    @Override
    public int getMinCost(int level) {
        return 15 + 5 * level;
    }

    @Override
    public int getMaxCost(int level) {
        return 30;
    }

    @Override
    public int getMaxLevel() {
        return 2;
    }

    @Override
    public boolean isCurse() {
        return true;
    }

    @Override
    public void setupAttributes(Multimap<Attribute, AttributeModifier> attributeMap, EquipmentSlot slot, int level) {
        if (slot == EquipmentSlot.MAINHAND) {
            attributeMap.put(
                    AttributeRegistry.CRIT_DAMAGE.get(),
                    new AttributeModifier(
                            Main.COTV_ENCHANTMENT_CRIT_DAMAGE_MODIFIER_UUID,
                            "CotV enchantment modifier",
                            CRITICAL_BOOSTS[level - 1],
                            AttributeModifier.Operation.MULTIPLY_BASE
                    )
            );
            attributeMap.put(
                    Attributes.ATTACK_SPEED,
                    new AttributeModifier(
                            Main.COTV_ENCHANTMENT_ATTACK_SPEED_MODIFIER_UUID,
                            "CotV enchantment modifier",
                            ATTACK_SPEED_NERFS[level - 1],
                            AttributeModifier.Operation.MULTIPLY_BASE
                    )
            );
        }
    }
}
