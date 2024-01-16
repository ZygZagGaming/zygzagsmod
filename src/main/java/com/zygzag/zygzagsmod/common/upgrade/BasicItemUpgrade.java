package com.zygzag.zygzagsmod.common.upgrade;

import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import net.minecraft.ChatFormatting;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import oshi.util.tuples.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BasicItemUpgrade implements ItemUpgrade {

    private final Map<Pair<EquipmentSlot, Integer>, Multimap<Attribute, AttributeModifier>> attributeMapCache = new HashMap<>();
    @Override
    public final Multimap<Attribute, AttributeModifier> attributes(EquipmentSlot slot, int level, ItemStack stack) {
        var index = new Pair<>(slot, level);
        if (!attributeMapCache.containsKey(index)) {
            Multimap<Attribute, AttributeModifier> attributeMap = Multimaps.newListMultimap(new HashMap<>(), ArrayList::new);
            setupAttributes(attributeMap, slot, level);
            attributeMapCache.put(index, attributeMap);
        }
        return attributeMapCache.get(index);
    }

    public void setupAttributes(Multimap<Attribute, AttributeModifier> attributeMap, EquipmentSlot slot, int level) { }
}
