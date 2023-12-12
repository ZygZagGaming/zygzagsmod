package com.zygzag.zygzagsmod.common.item.iridium;

import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public interface IEffectAttackWeapon {
    default void addEffects(Multimap<MobEffect, MobEffectInstance> map) { }

    default Multimap<MobEffect, MobEffectInstance> effects() {
        Multimap<MobEffect, MobEffectInstance> effectMap = Multimaps.newListMultimap(new HashMap<>(), ArrayList::new);
        addEffects(effectMap);
        return effectMap;
    }
}
