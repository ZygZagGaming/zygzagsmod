package com.zygzag.zygzagsmod.common.enchant;

import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import oshi.util.tuples.Pair;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public abstract class CustomEnchantment extends Enchantment {
    protected CustomEnchantment(Rarity rarity, EnchantmentCategory category, EquipmentSlot[] slots) {
        super(rarity, category, slots);
    }

    @Override
    public Component getFullname(int level) {
        MutableComponent component = Component.translatable(getDescriptionId()).withStyle(Style.EMPTY.withColor(getColor(level)));
        if (level != 1 || this.getMaxLevel() != 1) component.append(" ").append(Component.translatable("enchantment.level." + level));
        return component;
    }

    public int getColor(int level) {
        return isCurse() ? 0xff5555 : 0xaaaaaa;
    }

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

    private List<MobEffectInstance> effectsCache = null;
    public final List<MobEffectInstance> attackEffects() {
        if (effectsCache == null) {
            List<MobEffectInstance> effects = new LinkedList<>();
            setupAttackEffects(effects);
            effectsCache = effects;
        }
        return effectsCache;
    }

    public void setupAttackEffects(List<MobEffectInstance> effects) { }
}