package io.github.zygzaggaming.zygzagsmod.common.enchant;

import io.github.zygzaggaming.zygzagsmod.common.Main;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;

import java.util.List;

public class HallowedEnchantment extends CustomEnchantment {
    public HallowedEnchantment(Rarity rarity) {
        super(rarity, Main.SWORD_OR_AXE, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
    }

    @Override
    public int getMinCost(int level) {
        return 1;
    }

    @Override
    public int getMaxCost(int level) {
        return 2;
    }

    @Override
    public void setupAttackEffects(List<MobEffectInstance> effects) {
        effects.add(new MobEffectInstance(MobEffects.GLOWING, 10 * 20));
    }
}
