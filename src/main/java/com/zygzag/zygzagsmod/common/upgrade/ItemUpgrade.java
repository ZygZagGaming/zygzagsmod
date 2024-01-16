package com.zygzag.zygzagsmod.common.upgrade;

import com.google.common.collect.Multimap;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.zygzag.zygzagsmod.common.registry.ItemUpgradeRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

public interface ItemUpgrade {
    Codec<ItemUpgrade> CODEC = ResourceLocation.CODEC.comapFlatMap(
            loc -> {
                if (ItemUpgradeRegistry.BACKING_REGISTRY.containsKey(loc))
                    return DataResult.success(ItemUpgradeRegistry.BACKING_REGISTRY.get(loc));
                else return DataResult.error(() -> loc + " is not a valid item upgrade type");
            },
            ItemUpgradeRegistry.BACKING_REGISTRY::getKey
    );

    Multimap<Attribute, AttributeModifier> attributes(EquipmentSlot slot, int level, ItemStack stack);

    default ResourceLocation getId() {
        return ItemUpgradeRegistry.BACKING_REGISTRY.getKey(this);
    }

    @Nullable
    default Component additionalTooltipLine(int timesApplied) {
        return null;
    }
}
