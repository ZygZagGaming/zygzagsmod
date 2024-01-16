package com.zygzag.zygzagsmod.common.upgrade;

import com.google.common.collect.Multimap;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.function.Function;
import java.util.function.Predicate;

import static com.zygzag.zygzagsmod.common.util.GeneralUtil.tooltipToString;

public class AttributeUpgrade extends BasicItemUpgrade {
    private final Attribute attribute;
    private final Predicate<EquipmentSlot> slotPredicate;
    private final UUID uuid;
    private final String name;
    private final AttributeModifier.Operation operation;
    private final Function<Integer, Double> scalingFunction;
    private final ChatFormatting[] formattings;
    public AttributeUpgrade(Attribute attribute, Predicate<EquipmentSlot> slotPredicate, UUID uuid, String name, AttributeModifier.Operation operation, Function<Integer, Double> scalingFunction, ChatFormatting... formattings) {
        this.attribute = attribute;
        this.slotPredicate = slotPredicate;
        this.uuid = uuid;
        this.name = name;
        this.operation = operation;
        this.scalingFunction = scalingFunction;
        this.formattings = formattings;
    }
    public AttributeUpgrade(Attribute attribute, EquipmentSlot slot, UUID uuid, String name, AttributeModifier.Operation operation, Function<Integer, Double> scalingFunction, ChatFormatting... formattings) {
        this(attribute, (it) -> it == slot, uuid, name, operation, scalingFunction, formattings);
    }

    private AttributeModifier modifier(int timesApplied) {
        return new AttributeModifier(uuid, name, scalingFunction.apply(timesApplied), operation);
    }

    @Override
    public void setupAttributes(Multimap<Attribute, AttributeModifier> attributeMap, EquipmentSlot slot, int level) {
        if (slotPredicate.test(slot)) attributeMap.put(attribute, modifier(level));
    }

    @Nullable
    @Override
    public Component additionalTooltipLine(int timesApplied) {
        var id = getId();
        return Component.translatable("augment.%s.%s".formatted(id.getNamespace(), id.getPath()))
                .append(" x%s ".formatted(timesApplied))
                .withStyle(formattings)
                .append(Component.literal("> ").withStyle(ChatFormatting.GRAY))
                .append(Component.literal("%s ".formatted(tooltipToString(modifier(timesApplied)))).withStyle(ChatFormatting.BLUE))
                .append(Component.translatable(attribute.getDescriptionId()).withStyle(ChatFormatting.BLUE));
    }
}
