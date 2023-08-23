package com.zygzag.zygzagsmod.common.registry;

import com.zygzag.zygzagsmod.common.recipe.TransmutationRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import static com.zygzag.zygzagsmod.common.Main.MODID;

public class RecipeSerializerRegistry extends Registry<RecipeSerializer<?>> {
    public static final RecipeSerializerRegistry INSTANCE = new RecipeSerializerRegistry(DeferredRegister.create(Registries.RECIPE_SERIALIZER, MODID));
    //public static final RegistryObject<SimpleRecipeSerializer<ShulkerBowlRecipe>> SHULKER_BOWL_CRAFTING = INSTANCE.register("crafting_special_shulker_bowl", () -> new SimpleRecipeSerializer<>(ShulkerBowlRecipe::new));
    public static final RegistryObject<RecipeSerializer<TransmutationRecipe>> TRANSMUTATION_SERIALIZER = INSTANCE.register("transmutation", TransmutationRecipe.TransmutationSerializer::new);
    //public static final RegistryObject<RecipeSerializer<EmpowermentRecipe>> EMPOWERMENT_SERIALIZER = INSTANCE.register("empowerment", EmpowermentRecipe.EmpowermentSerializer::new);
    //public static final RegistryObject<SimpleRecipeSerializer<SocketRemoveRecipe>> SOCKET_REMOVE_CRAFTING = INSTANCE.register("crafting_special_socket_remove", () -> new SimpleRecipeSerializer<>(SocketRemoveRecipe::new));
    //public static final RegistryObject<SimpleRecipeSerializer<EnderBookCopyRecipe>> ENDER_BOOK_COPY_CRAFTING = INSTANCE.register("crafting_special_ender_book_copy", () -> new SimpleRecipeSerializer<>(EnderBookCopyRecipe::new));

    private RecipeSerializerRegistry(DeferredRegister<RecipeSerializer<?>> register) {
        super(register);
    }
}
