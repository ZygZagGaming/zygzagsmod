package com.zygzag.zygzagsmod.common.upgrade;

import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.zygzag.zygzagsmod.common.recipe.UpgradeRecipe;
import com.zygzag.zygzagsmod.common.registry.ItemUpgradeRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import oshi.util.tuples.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ItemUpgrade {
    public static final Codec<ItemUpgrade> CODEC = ResourceLocation.CODEC.comapFlatMap(
            loc -> {
                if (ItemUpgradeRegistry.BACKING_REGISTRY.containsKey(loc))
                    return DataResult.success(ItemUpgradeRegistry.BACKING_REGISTRY.get(loc));
                else return DataResult.error(() -> loc + " is not a valid item upgrade type");
            },
            ItemUpgradeRegistry.BACKING_REGISTRY::getKey
    );

    private final Map<Pair<EquipmentSlot, Integer>, Multimap<Attribute, AttributeModifier>> attributeMapCache = new HashMap<>();
    public final Multimap<Attribute, AttributeModifier> attributes(EquipmentSlot slot, int level) {
        var index = new Pair<>(slot, level);
        if (!attributeMapCache.containsKey(index)) {
            Multimap<Attribute, AttributeModifier> attributeMap = Multimaps.newListMultimap(new HashMap<>(), ArrayList::new);
            setupAttributes(attributeMap, slot, level);
            attributeMapCache.put(index, attributeMap);
        }
        return attributeMapCache.get(index);
    }

    public void setupAttributes(Multimap<Attribute, AttributeModifier> attributeMap, EquipmentSlot slot, int level) { }

    public ResourceLocation getId() {
        return ItemUpgradeRegistry.BACKING_REGISTRY.getKey(this);
    }

    public ChatFormatting[] getFormatting() {
        return new ChatFormatting[0];
    }
}
