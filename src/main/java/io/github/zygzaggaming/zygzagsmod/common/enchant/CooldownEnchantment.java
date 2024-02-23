package io.github.zygzaggaming.zygzagsmod.common.enchant;

import io.github.zygzaggaming.zygzagsmod.common.Main;
import net.minecraft.world.entity.EquipmentSlot;

public class CooldownEnchantment extends CustomEnchantment {
    public CooldownEnchantment(Rarity rarity) {
        super(rarity, Main.COOLDOWN_CATEGORY, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
    }

    @Override
    public int getMinCost(int level) {
        return 1 + 15 * level;
    }

    @Override
    public int getMaxCost(int level) {
        return 1 + 20 * level;
    }

    @Override
    public int getMaxLevel() {
        return 4;
    }
}
