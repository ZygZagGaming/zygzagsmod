package com.zygzag.zygzagsmod.common.registry;

import com.zygzag.zygzagsmod.common.recipe.TransmutationRecipe;
import com.zygzag.zygzagsmod.common.recipe.UpgradeRecipe;
import com.zygzag.zygzagsmod.common.registry.base.AkomiRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

import static com.zygzag.zygzagsmod.common.Main.MODID;

public class RecipeSerializerRegistry extends AkomiRegistry<RecipeSerializer<?>> {
    public static final RecipeSerializerRegistry INSTANCE = new RecipeSerializerRegistry(DeferredRegister.create(BuiltInRegistries.RECIPE_SERIALIZER, MODID));
    //public static final Supplier<SimpleRecipeSerializer<ShulkerBowlRecipe>> SHULKER_BOWL_CRAFTING = INSTANCE.register("crafting_special_shulker_bowl", () -> new SimpleRecipeSerializer<>(ShulkerBowlRecipe::new));
    public static final Supplier<RecipeSerializer<TransmutationRecipe>> TRANSMUTATION_SERIALIZER = INSTANCE.register("transmutation", TransmutationRecipe.TransmutationSerializer::new);
    //public static final Supplier<RecipeSerializer<EmpowermentRecipe>> EMPOWERMENT_SERIALIZER = INSTANCE.register("empowerment", EmpowermentRecipe.EmpowermentSerializer::new);
    //public static final Supplier<SimpleRecipeSerializer<SocketRemoveRecipe>> SOCKET_REMOVE_CRAFTING = INSTANCE.register("crafting_special_socket_remove", () -> new SimpleRecipeSerializer<>(SocketRemoveRecipe::new));
    //public static final Supplier<SimpleRecipeSerializer<EnderBookCopyRecipe>> ENDER_BOOK_COPY_CRAFTING = INSTANCE.register("crafting_special_ender_book_copy", () -> new SimpleRecipeSerializer<>(EnderBookCopyRecipe::new));
    public static final Supplier<RecipeSerializer<UpgradeRecipe>> UPGRADE_SERIALIZER = INSTANCE.register("upgrade", UpgradeRecipe.Serializer::new);

    private RecipeSerializerRegistry(DeferredRegister<RecipeSerializer<?>> register) {
        super(register);
    }
}
