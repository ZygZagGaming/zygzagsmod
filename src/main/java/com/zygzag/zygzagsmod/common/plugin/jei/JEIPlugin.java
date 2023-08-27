package com.zygzag.zygzagsmod.common.plugin.jei;

import com.mojang.blaze3d.MethodsReturnNonnullByDefault;
import com.zygzag.zygzagsmod.common.recipe.TransmutationRecipe;
import com.zygzag.zygzagsmod.common.registry.IridiumGearRegistry;
import com.zygzag.zygzagsmod.common.registry.ItemRegistry;
import com.zygzag.zygzagsmod.common.registry.RecipeTypeRegistry;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

import static com.zygzag.zygzagsmod.common.Main.MODID;

@SuppressWarnings("unused")
@JeiPlugin
@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class JEIPlugin implements IModPlugin {
    @Nullable
    public IRecipeCategory<TransmutationRecipe> transmutationCategory;

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(MODID + ".plugin.jei");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(transmutationCategory = new TransmutationCategory(registration.getJeiHelpers()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        if (Minecraft.getInstance().level != null) {
            registration.addRecipes(TransmutationCategory.TRANSMUTATION_RECIPE_TYPE, Minecraft.getInstance().level.getRecipeManager().getAllRecipesFor(RecipeTypeRegistry.TRANSMUTATION.get()));
        }
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(ItemRegistry.TRANSMUTATION_CHARGE.get().getDefaultInstance(), TransmutationCategory.TRANSMUTATION_RECIPE_TYPE);
        registration.addRecipeCatalyst(IridiumGearRegistry.SKULL_SOCKETED_IRIDIUM_PICKAXE.get().getDefaultInstance(), TransmutationCategory.TRANSMUTATION_RECIPE_TYPE);
    }
}