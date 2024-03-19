package io.github.zygzaggaming.zygzagsmod.common.plugin.jei;

import com.mojang.blaze3d.MethodsReturnNonnullByDefault;
import io.github.zygzaggaming.zygzagsmod.common.Main;
import io.github.zygzaggaming.zygzagsmod.common.item.iridium.Socket;
import io.github.zygzaggaming.zygzagsmod.common.recipe.TransmutationRecipe;
import io.github.zygzaggaming.zygzagsmod.common.registry.IridiumGearRegistry;
import io.github.zygzaggaming.zygzagsmod.common.registry.ItemRegistry;
import io.github.zygzaggaming.zygzagsmod.common.registry.RecipeTypeRegistry;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

@SuppressWarnings("unused")
@JeiPlugin
@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class JEIPlugin implements IModPlugin {
    @Nullable
    public IRecipeCategory<TransmutationRecipe> transmutationCategory;

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(Main.MODID + ".plugin.jei");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(transmutationCategory = new TransmutationCategory(registration.getJeiHelpers()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        if (Minecraft.getInstance().level != null) {
            registration.addRecipes(TransmutationCategory.TRANSMUTATION_RECIPE_TYPE, Minecraft.getInstance().level.getRecipeManager().getAllRecipesFor(RecipeTypeRegistry.TRANSMUTATION.get()).stream().map(RecipeHolder::value).toList());
        }
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(ItemRegistry.TRANSMUTATION_CHARGE.get().getDefaultInstance(), TransmutationCategory.TRANSMUTATION_RECIPE_TYPE);
        registration.addRecipeCatalyst(IridiumGearRegistry.PICKAXES.get(Socket.SKULL).get().getDefaultInstance(), TransmutationCategory.TRANSMUTATION_RECIPE_TYPE);
    }
}