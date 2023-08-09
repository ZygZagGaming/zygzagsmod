package com.zygzag.zygzagsmod.common.registries;

import com.zygzag.zygzagsmod.common.recipe.TransmutationRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import static com.zygzag.zygzagsmod.common.Main.MODID;

public class RecipeTypeRegistry extends Registry<RecipeType<?>> {
    public static final RecipeTypeRegistry INSTANCE = new RecipeTypeRegistry(DeferredRegister.create(Registries.RECIPE_TYPE, MODID));

    public static final RegistryObject<RecipeType<TransmutationRecipe>> TRANSMUTATION = registerRecipeType("transmutation");
    //public static final RegistryObject<RecipeType<EmpowermentRecipe>> EMPOWERMENT = registerRecipeType("empowerment");

    public RecipeTypeRegistry(DeferredRegister<RecipeType<?>> register) {
        super(register);
    }

    public static <T extends Recipe<?>> RegistryObject<RecipeType<T>> registerRecipeType(String id) {
        return INSTANCE.register(id, () -> new RecipeType<>() {
            @Override
            public String toString() {
                return id;
            }
        });
    }
}
