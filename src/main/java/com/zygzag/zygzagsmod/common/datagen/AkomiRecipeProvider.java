package com.zygzag.zygzagsmod.common.datagen;

import com.zygzag.zygzagsmod.common.item.iridium.Socket;
import com.zygzag.zygzagsmod.common.registry.IridiumGearRegistry;
import com.zygzag.zygzagsmod.common.registry.ItemRegistry;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.SmithingTransformRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Map;
import java.util.function.Consumer;

import static com.zygzag.zygzagsmod.common.Main.MODID;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class AkomiRecipeProvider extends RecipeProvider {
    private static final IridiumPlatingRecipeType[] PLATING_RECIPE_TYPES = {
            new IridiumPlatingRecipeType("axe", ItemRegistry.IRIDIUM_AXE_BLUEPRINT.get(), IridiumGearRegistry.AXES.get(Socket.NONE).get(), Items.NETHERITE_AXE, IridiumGearRegistry.PARTIAL_AXES, 3, RecipeCategory.TOOLS),
            new IridiumPlatingRecipeType("sword", ItemRegistry.IRIDIUM_SWORD_BLUEPRINT.get(), IridiumGearRegistry.SWORDS.get(Socket.NONE).get(), Items.NETHERITE_SWORD, IridiumGearRegistry.PARTIAL_SWORDS, 2, RecipeCategory.COMBAT),
            new IridiumPlatingRecipeType("pickaxe", ItemRegistry.IRIDIUM_PICKAXE_BLUEPRINT.get(), IridiumGearRegistry.PICKAXES.get(Socket.NONE).get(), Items.NETHERITE_PICKAXE, IridiumGearRegistry.PARTIAL_PICKAXES, 3, RecipeCategory.TOOLS),
            new IridiumPlatingRecipeType("shovel", ItemRegistry.IRIDIUM_SHOVEL_BLUEPRINT.get(), IridiumGearRegistry.SHOVELS.get(Socket.NONE).get(), Items.NETHERITE_SHOVEL, Map.of(), 1, RecipeCategory.TOOLS),
            new IridiumPlatingRecipeType("hoe", ItemRegistry.IRIDIUM_HOE_BLUEPRINT.get(), IridiumGearRegistry.HOES.get(Socket.NONE).get(), Items.NETHERITE_HOE, IridiumGearRegistry.PARTIAL_HOES, 2, RecipeCategory.TOOLS),
            new IridiumPlatingRecipeType("helmet", ItemRegistry.IRIDIUM_HELMET_BLUEPRINT.get(), IridiumGearRegistry.IRIDIUM_HELMET.get(), Items.NETHERITE_HELMET, IridiumGearRegistry.PARTIAL_HELMETS, 5, RecipeCategory.COMBAT),
            new IridiumPlatingRecipeType("chestplate", ItemRegistry.IRIDIUM_CHESTPLATE_BLUEPRINT.get(), IridiumGearRegistry.CHESTPLATES.get(Socket.NONE).get(), Items.NETHERITE_CHESTPLATE, IridiumGearRegistry.PARTIAL_CHESTPLATES, 8, RecipeCategory.COMBAT),
            new IridiumPlatingRecipeType("leggings", ItemRegistry.IRIDIUM_LEGGINGS_BLUEPRINT.get(), IridiumGearRegistry.IRIDIUM_LEGGINGS.get(), Items.NETHERITE_LEGGINGS, IridiumGearRegistry.PARTIAL_LEGGINGS, 7, RecipeCategory.COMBAT),
            new IridiumPlatingRecipeType("boots", ItemRegistry.IRIDIUM_BOOTS_BLUEPRINT.get(), IridiumGearRegistry.IRIDIUM_BOOTS.get(), Items.NETHERITE_BOOTS, IridiumGearRegistry.PARTIAL_BOOTS, 4, RecipeCategory.COMBAT),
    };

    private static final IridiumSocketingRecipeType[] SOCKETING_RECIPE_TYPES = {
            new IridiumSocketingRecipeType("sword", IridiumGearRegistry.SocketedGearType.SWORD, IridiumGearRegistry.SWORDS, RecipeCategory.COMBAT),
            new IridiumSocketingRecipeType("pickaxe", IridiumGearRegistry.SocketedGearType.PICKAXE, IridiumGearRegistry.PICKAXES, RecipeCategory.TOOLS),
            new IridiumSocketingRecipeType("axe", IridiumGearRegistry.SocketedGearType.AXE, IridiumGearRegistry.AXES, RecipeCategory.TOOLS),
            new IridiumSocketingRecipeType("shovel", IridiumGearRegistry.SocketedGearType.SHOVEL, IridiumGearRegistry.SHOVELS, RecipeCategory.TOOLS),
            new IridiumSocketingRecipeType("hoe", IridiumGearRegistry.SocketedGearType.HOE, IridiumGearRegistry.HOES, RecipeCategory.TOOLS),
            new IridiumSocketingRecipeType("chestplate", IridiumGearRegistry.SocketedGearType.CHESTPLATE, IridiumGearRegistry.CHESTPLATES, RecipeCategory.COMBAT),
            new IridiumSocketingRecipeType("scepter", IridiumGearRegistry.SocketedGearType.SCEPTER, IridiumGearRegistry.SCEPTERS, RecipeCategory.TOOLS),
    };

    public AkomiRecipeProvider(PackOutput output) {
        super(output);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> consumer) {
        makeIridiumPlatingRecipes(consumer);
        makeIridiumSocketingRecipes(consumer);
    }

    private void makeIridiumPlatingRecipes(Consumer<FinishedRecipe> consumer) {
        for (IridiumPlatingRecipeType recipeType : PLATING_RECIPE_TYPES) {
            makeIridiumPlatingRecipesForType(recipeType, consumer);
        }
    }

    private void makeIridiumPlatingRecipesForType(IridiumPlatingRecipeType recipeType, Consumer<FinishedRecipe> consumer) {
        for (int platings = 0; platings < recipeType.itemPlatings(); platings++) {
            SmithingTransformRecipeBuilder.smithing(
                    Ingredient.of(recipeType.blueprint()),
                    Ingredient.of(platings == 0 ? recipeType.baseItem() : recipeType.partials.get(platings).get()),
                    Ingredient.of(ItemRegistry.IRIDIUM_PLATING.get()),
                    recipeType.category,
                    platings == recipeType.itemPlatings() - 1 ? recipeType.finalItem() : recipeType.partials.get(platings + 1).get()
            ).unlocks(
                    "has_iridium_plating",
                    has(ItemRegistry.IRIDIUM_PLATING.get())
            ).save(consumer, new ResourceLocation(MODID, "iridium_" + recipeType.name() + "_plating_" + platings));
        }
    }

    private void makeIridiumSocketingRecipes(Consumer<FinishedRecipe> consumer) {
        for (IridiumSocketingRecipeType recipeType : SOCKETING_RECIPE_TYPES) {
            makeIridiumSocketingRecipesForType(recipeType, consumer);
        }
    }

    private void makeIridiumSocketingRecipesForType(IridiumSocketingRecipeType recipeType, Consumer<FinishedRecipe> consumer) {
        for (Socket socket : Socket.values())
            if (socket != Socket.NONE && socket.gearTypeFilter.test(recipeType.type)) {
                SmithingTransformRecipeBuilder.smithing(
                        Ingredient.of(socket.schematicSupplier.get()),
                        Ingredient.of(recipeType.finalItems().get(Socket.NONE).get()),
                        Ingredient.of(socket.itemSupplier.get()),
                        recipeType.category(),
                        recipeType.finalItems().get(socket).get()
                ).unlocks(
                        "has_iridium_plating",
                        has(ItemRegistry.IRIDIUM_PLATING.get())
                ).save(consumer, new ResourceLocation(MODID, socket.name().toLowerCase() + "_socketed_iridium_" + recipeType.name()));
            }
    }

    private record IridiumPlatingRecipeType(String name, Item blueprint, Item finalItem, Item baseItem,
                                            Map<Integer, RegistryObject<Item>> partials, int itemPlatings,
                                            RecipeCategory category) {
    }

    private record IridiumSocketingRecipeType(String name, IridiumGearRegistry.SocketedGearType type,
                                              Map<Socket, RegistryObject<Item>> finalItems, RecipeCategory category) {
    }
}
