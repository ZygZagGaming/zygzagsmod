package io.github.zygzaggaming.zygzagsmod.common.registry;

import io.github.zygzaggaming.zygzagsmod.common.recipe.TransmutationRecipe;
import io.github.zygzaggaming.zygzagsmod.common.registry.base.AkomiRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

import static io.github.zygzaggaming.zygzagsmod.common.Main.MODID;

public class RecipeTypeRegistry extends AkomiRegistry<RecipeType<?>> {
    public static final RecipeTypeRegistry INSTANCE = new RecipeTypeRegistry(DeferredRegister.create(BuiltInRegistries.RECIPE_TYPE, MODID));

    public static final Supplier<RecipeType<TransmutationRecipe>> TRANSMUTATION = registerRecipeType("transmutation");
    //public static final Supplier<RecipeType<EmpowermentRecipe>> EMPOWERMENT = registerRecipeType("empowerment");

    public RecipeTypeRegistry(DeferredRegister<RecipeType<?>> register) {
        super(register);
    }

    public static <T extends Recipe<?>> Supplier<RecipeType<T>> registerRecipeType(String id) {
        return INSTANCE.register(id, () -> new RecipeType<>() {
            @Override
            public String toString() {
                return id;
            }
        });
    }
}
