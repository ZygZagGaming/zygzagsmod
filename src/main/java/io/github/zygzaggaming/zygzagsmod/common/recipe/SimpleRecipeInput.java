package io.github.zygzaggaming.zygzagsmod.common.recipe;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;

import java.util.Arrays;

public class SimpleRecipeInput implements RecipeInput {
    private final NonNullList<ItemStack> items = NonNullList.create();

    public SimpleRecipeInput(ItemStack... items) {
        this.items.addAll(Arrays.asList(items));
    }

    @Override
    public ItemStack getItem(int index) {
        return items.get(index);
    }

    @Override
    public int size() {
        return items.size();
    }
}
