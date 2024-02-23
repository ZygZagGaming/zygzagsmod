package io.github.zygzaggaming.zygzagsmod.common.datagen;

import io.github.zygzaggaming.zygzagsmod.common.Main;
import io.github.zygzaggaming.zygzagsmod.common.item.iridium.Socket;
import io.github.zygzaggaming.zygzagsmod.common.registry.BlockItemEntityRegistry;
import io.github.zygzaggaming.zygzagsmod.common.registry.BlockWithItemRegistry;
import io.github.zygzaggaming.zygzagsmod.common.registry.IridiumGearRegistry;
import io.github.zygzaggaming.zygzagsmod.common.registry.ItemRegistry;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Map;
import java.util.function.Supplier;

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
    protected void buildRecipes(RecipeOutput output) {
        makeIridiumPlatingRecipes(output);
        makeIridiumSocketingRecipes(output);

        makeSimpleStonecuttingRecipe(Items.RED_NETHER_BRICKS, BlockWithItemRegistry.CHISELED_RED_NETHER_BRICKS.getItem(), RecipeCategory.BUILDING_BLOCKS, "stonecutting", output);
        makeSimpleStonecuttingRecipe(Items.RED_NETHER_BRICKS, BlockWithItemRegistry.CRACKED_RED_NETHER_BRICKS.getItem(), RecipeCategory.BUILDING_BLOCKS, "stonecutting", output);
        makeSimpleStonecuttingRecipe(Items.RED_NETHER_BRICKS, BlockWithItemRegistry.GIRDED_RED_NETHER_BRICKS.getItem(), RecipeCategory.BUILDING_BLOCKS, "stonecutting", output);
        makeSimpleStonecuttingRecipe(Items.RED_NETHER_BRICKS, BlockWithItemRegistry.CRUSHED_RED_NETHER_BRICKS.getItem(), RecipeCategory.BUILDING_BLOCKS, "stonecutting", output);

        makeSimpleStonecuttingRecipe(BlockWithItemRegistry.NETHER_QUARTZ_GLASS.getItem(), BlockWithItemRegistry.NETHER_QUARTZ_GLASS_STAIRS.getItem(), RecipeCategory.BUILDING_BLOCKS, "stonecutting", output);
        makeSimpleStonecuttingRecipe(BlockWithItemRegistry.NETHER_QUARTZ_GLASS.getItem(), BlockWithItemRegistry.NETHER_QUARTZ_GLASS_SLAB.getItem(), RecipeCategory.BUILDING_BLOCKS, "stonecutting", 2, output);

        SimpleCookingRecipeBuilder.smelting(Ingredient.of(Items.QUARTZ_BLOCK), RecipeCategory.BUILDING_BLOCKS, BlockWithItemRegistry.NETHER_QUARTZ_GLASS_SLAB.getItem(), 0.35f, 200).unlockedBy("has_quartz_block", has(Items.QUARTZ_BLOCK)).save(output);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.REDSTONE, BlockWithItemRegistry.RED_NETHER_BRICK_BUTTON.getItem()).unlockedBy("has_red_nether_bricks", has(Items.RED_NETHER_BRICKS)).requires(Items.RED_NETHER_BRICKS).save(output, new ResourceLocation(Main.MODID, "red_nether_brick_button"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.REDSTONE, BlockWithItemRegistry.NETHER_BRICK_BUTTON.getItem()).unlockedBy("has_nether_bricks", has(Items.NETHER_BRICK)).requires(Items.NETHER_BRICK).save(output, new ResourceLocation(Main.MODID, "nether_brick_button"));

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BlockItemEntityRegistry.MAGMATIC_NETHER_BRICKS.getItem()).unlockedBy("has_magmatic_nether_bricks", has(BlockItemEntityRegistry.MAGMATIC_NETHER_BRICKS.getItem())).pattern("MRM").pattern("RBR").pattern("MRM").define('M', Items.MAGMA_CREAM).define('R', Items.RED_NETHER_BRICKS).define('B', Items.BLAZE_ROD).save(output, new ResourceLocation(Main.MODID, "magmatic_nether_bricks"));
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, BlockItemEntityRegistry.RED_NETHER_BRICK_CACHE.getItem(), 2).unlockedBy("has_red_nether_bricks", has(Items.RED_NETHER_BRICKS)).pattern("RRR").pattern("R R").pattern("RRR").define('R', Items.RED_NETHER_BRICKS).save(output, new ResourceLocation(Main.MODID, "red_nether_brick_cache"));
    }

    private void makeSimpleStonecuttingRecipe(Item start, Item result, RecipeCategory category, String path, RecipeOutput output) {
        makeSimpleStonecuttingRecipe(start, result, category, path, 1, output);
    }

    private void makeSimpleStonecuttingRecipe(Item start, Item result, RecipeCategory category, String path, int count, RecipeOutput output) {
        var startId = BuiltInRegistries.ITEM.getKey(start);
        var resultId = BuiltInRegistries.ITEM.getKey(result);
        SingleItemRecipeBuilder.stonecutting(Ingredient.of(start), category, result, count).unlockedBy("has_" + startId, has(start)).save(output, new ResourceLocation(Main.MODID, path + "/" + resultId.getPath()));
    }

    private void makeIridiumPlatingRecipes(RecipeOutput output) {
        for (IridiumPlatingRecipeType recipeType : PLATING_RECIPE_TYPES) {
            makeIridiumPlatingRecipesForType(recipeType, output);
        }
    }

    private void makeIridiumPlatingRecipesForType(IridiumPlatingRecipeType recipeType, RecipeOutput output) {
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
            ).save(output, new ResourceLocation(Main.MODID, "iridium_" + recipeType.name() + "_plating_" + platings));
        }
    }

    private void makeIridiumSocketingRecipes(RecipeOutput output) {
        for (IridiumSocketingRecipeType recipeType : SOCKETING_RECIPE_TYPES) {
            makeIridiumSocketingRecipesForType(recipeType, output);
        }
    }

    private void makeIridiumSocketingRecipesForType(IridiumSocketingRecipeType recipeType, RecipeOutput output) {
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
                ).save(output, new ResourceLocation(Main.MODID, socket.name().toLowerCase() + "_socketed_iridium_" + recipeType.name()));
            }
    }

    private record IridiumPlatingRecipeType(String name, Item blueprint, Item finalItem, Item baseItem,
                                            Map<Integer, Supplier<Item>> partials, int itemPlatings,
                                            RecipeCategory category) {
    }

    private record IridiumSocketingRecipeType(String name, IridiumGearRegistry.SocketedGearType type,
                                              Map<Socket, Supplier<Item>> finalItems, RecipeCategory category) {
    }
}
