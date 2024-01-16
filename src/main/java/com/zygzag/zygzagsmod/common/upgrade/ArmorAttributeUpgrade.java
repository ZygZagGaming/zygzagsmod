package com.zygzag.zygzagsmod.common.upgrade;

import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import oshi.util.tuples.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Predicate;

import static com.zygzag.zygzagsmod.common.util.GeneralUtil.tooltipToString;

public class ArmorAttributeUpgrade implements ItemUpgrade {
    private final Attribute attribute;
    private final UUID uuid;
    private final String name;
    private final AttributeModifier.Operation operation;
    private final Function<Integer, Double> scalingFunction;
    private final ChatFormatting[] formattings;
    public ArmorAttributeUpgrade(Attribute attribute, UUID uuid, String name, AttributeModifier.Operation operation, Function<Integer, Double> scalingFunction, ChatFormatting... formattings) {
        this.attribute = attribute;
        this.uuid = uuid;
        this.name = name;
        this.operation = operation;
        this.scalingFunction = scalingFunction;
        this.formattings = formattings;
    }

    private AttributeModifier modifier(int timesApplied) {
        return new AttributeModifier(uuid, name, scalingFunction.apply(timesApplied), operation);
    }

    @Override
    public final Multimap<Attribute, AttributeModifier> attributes(EquipmentSlot slot, int level, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> attributeMap = Multimaps.newListMultimap(new HashMap<>(), ArrayList::new);
        if (stack.getItem() instanceof ArmorItem armorItem && slot == armorItem.getEquipmentSlot()) attributeMap.put(attribute, modifier(level));
        return attributeMap;
    }

    @Nullable
    @Override
    public Component additionalTooltipLine(int timesApplied) {
        var id = getId();
        return Component.translatable("augment.%s.%s".formatted(id.getNamespace(), id.getPath())).append(" x%s > %s ".formatted(timesApplied, tooltipToString(modifier(timesApplied)))).append(Component.translatable(attribute.getDescriptionId()));
    }
}
