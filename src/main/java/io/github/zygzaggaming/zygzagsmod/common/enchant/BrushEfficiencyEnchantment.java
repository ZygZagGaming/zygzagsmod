package io.github.zygzaggaming.zygzagsmod.common.enchant;

import io.github.zygzaggaming.zygzagsmod.common.Main;
import net.minecraft.world.entity.EquipmentSlot;

public class BrushEfficiencyEnchantment extends CustomEnchantment {
    public BrushEfficiencyEnchantment(Rarity rarity) {
        super(rarity, Main.BRUSH_CATEGORY, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
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
        return 5;
    }
}
