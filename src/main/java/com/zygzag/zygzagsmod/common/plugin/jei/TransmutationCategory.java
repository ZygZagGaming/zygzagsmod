package com.zygzag.zygzagsmod.common.plugin.jei;

import com.zygzag.zygzagsmod.common.recipe.TransmutationRecipe;
import com.zygzag.zygzagsmod.common.registry.ItemRegistry;
import mezz.jei.api.constants.ModIds;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;

import javax.annotation.ParametersAreNonnullByDefault;

@SuppressWarnings({"unused", "FieldCanBeLocal"})
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class TransmutationCategory implements IRecipeCategory<TransmutationRecipe> {
    public static final String TEXTURE_GUI_PATH = "textures/jei/gui/";
    public static final String TEXTURE_GUI_VANILLA = TEXTURE_GUI_PATH + "gui_vanilla.png";
    public static final ResourceLocation RECIPE_GUI_VANILLA = new ResourceLocation(ModIds.JEI_ID, TEXTURE_GUI_VANILLA);
    public static final RecipeType<TransmutationRecipe> TRANSMUTATION_RECIPE_TYPE = RecipeType.create("zygzagsmod", "transmutation", TransmutationRecipe.class);
    private final IJeiHelpers helpers;
    private final IGuiHelper guiHelper;
    private final IDrawable background;
    private final IDrawable icon;

    public TransmutationCategory(IJeiHelpers helpers) {
        this.helpers = helpers;
        guiHelper = helpers.getGuiHelper();
        background = guiHelper.drawableBuilder(RECIPE_GUI_VANILLA, 0, 220, 82, 34)
                .addPadding(0, 10, 0, 0)
                .build();
        icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, ItemRegistry.TRANSMUTATION_CHARGE.get().getDefaultInstance());
    }

    @Override
    public RecipeType<TransmutationRecipe> getRecipeType() {
        return TRANSMUTATION_RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("zygzagsmod.recipe.transmutation");
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder recipeLayout, TransmutationRecipe recipe, IFocusGroup ingredients) {
        recipeLayout.addSlot(RecipeIngredientRole.INPUT, 1, 9).addIngredients(recipe.getInItem());
        recipeLayout.addSlot(RecipeIngredientRole.OUTPUT, 61, 9).addIngredients(Ingredient.of(recipe.getOutItem()));
    }

    @Override
    public void draw(TransmutationRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics graphics, double mouseX, double mouseY) {
        drawRateCost(Minecraft.getInstance(), graphics, "Rate: " + recipe.getRate() + "x");
    }

    private void drawRateCost(Minecraft minecraft, GuiGraphics graphics, String text) {
        int shadowColor = 0xFF000000 | (0xFF111111 & 0xFCFCFC) >> 2;
        int width = minecraft.font.width(text);
        int x = background.getWidth() - 2 - width;
        int y = 37;

        minecraft.font.drawInBatch(text, x, y, 0xFF111111, false, graphics.pose().last().pose(), graphics.bufferSource(), Font.DisplayMode.NORMAL, 0, 15728880);
    }
}