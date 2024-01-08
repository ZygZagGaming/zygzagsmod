package com.zygzag.zygzagsmod.common.enchant;

import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public abstract class CustomEnchantment extends Enchantment {
    protected CustomEnchantment(Rarity rarity, EnchantmentCategory category, EquipmentSlot[] slots) {
        super(rarity, category, slots);
    }

    @Override
    public Component getFullname(int level) {
        MutableComponent mutablecomponent = Component.translatable(getDescriptionId());
        mutablecomponent.withStyle(Style.EMPTY.withColor(getColor(level)));

        if (level != 1 || this.getMaxLevel() != 1) {
            mutablecomponent.append(" ").append(Component.translatable("enchantment.level." + level));
        }

        return mutablecomponent;
    }

    public int getColor(int level) {
        if (this.isCurse()) {
            return 0xff5555;
        } else {
            return 0xaaaaaa;
        }
    }

    private Multimap<Attribute, AttributeModifier> attributeMapCache = null;
    public final Multimap<Attribute, AttributeModifier> attributes(EquipmentSlot slot, int level) {
        if (attributeMapCache == null) {
            Multimap<Attribute, AttributeModifier> attributeMap = Multimaps.newListMultimap(new HashMap<>(), ArrayList::new);
            setupAttributes(attributeMap, slot, level);
            attributeMapCache = attributeMap;
        }
        return attributeMapCache;
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