package com.zygzag.zygzagsmod.common.tier;

import com.mojang.blaze3d.MethodsReturnNonnullByDefault;
import com.zygzag.zygzagsmod.common.Main;
import com.zygzag.zygzagsmod.common.registry.ItemRegistry;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;

@MethodsReturnNonnullByDefault
public enum IridiumToolTier implements Tier {
    FULL(2768, 15, 5, 5, 18),
    HALF(2364, 15, 4.5f, 5, 18),
    _3_1(2204, 15, 4.33f, 5, 18),
    _3_2(2560, 15, 4.66f, 5, 18),
    DIAMOND_SOCKETED(2894, 16, 5.33f, 6, 12),
    DIAMOND_SOCKETED_PICK(2894, 19, 5.33f, 6, 12),
    EMERALD_SOCKETED(2648, 17, 4f, 5, 21),
    WITHER_SOCKETED_PICK(2768, Integer.MAX_VALUE, 5, 5, 18);

    final int uses;
    final float speed;
    final float damage;
    final int level;
    final int enchantLevel;

    IridiumToolTier(int uses, float speed, float damage, int level, int enchantLevel) {
        this.uses = uses;
        this.speed = speed;
        this.damage = damage;
        this.level = level;
        this.enchantLevel = enchantLevel;
    }

    public static IridiumToolTier getToolTier(int platings, int outOf) {
        return outOf == 2 ? HALF : platings == 1 ? _3_1 : _3_2;
    }

    @Override
    public int getUses() {
        return this.uses;
    }

    @Override
    public float getSpeed() {
        return this.speed;
    }

    @Override
    public float getAttackDamageBonus() {
        return this.damage;
    }

    @Override
    public int getLevel() {
        return this.level;
    }

    @Override
    public int getEnchantmentValue() {
        return this.enchantLevel;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return Ingredient.of(ItemRegistry.IRIDIUM_PLATING.get());
    }

    @Override
    public TagKey<Block> getTag() {
        return Main.NEEDS_IRIDIUM_TOOL_TAG;
    }
}
