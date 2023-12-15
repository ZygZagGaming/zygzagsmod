package com.zygzag.zygzagsmod.common.loot;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class AutosmeltModifier extends LootModifier {
    public static Codec<AutosmeltModifier> CODEC = RecordCodecBuilder.create(inst -> codecStart(inst).apply(inst, AutosmeltModifier::new));

    /**
     * Constructs a LootModifier.
     *
     * @param conditionsIn the ILootConditions that need to be matched before the loot is modified.
     */
    protected AutosmeltModifier(LootItemCondition[] conditionsIn) {
        super(conditionsIn);
    }

    @Override
    protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        Level world = context.getLevel();
        for (int i = 0; i < generatedLoot.size(); i++) {
            ItemStack stack = generatedLoot.get(i);
            SimpleContainer cont = new SimpleContainer(stack);
            Optional<SmeltingRecipe> opt = world.getRecipeManager().getRecipeFor(RecipeType.SMELTING, cont, world).map(RecipeHolder::value);
            if (opt.isPresent()) {
                ItemStack outStack = opt.get().assemble(cont, context.getLevel().registryAccess());
                outStack.setCount(stack.getCount());
                generatedLoot.set(i, outStack);
            }
        }
        return generatedLoot;
    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }
}