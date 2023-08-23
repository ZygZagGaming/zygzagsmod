package com.zygzag.zygzagsmod.common.recipe;


import com.google.gson.JsonObject;
import com.mojang.blaze3d.MethodsReturnNonnullByDefault;
import com.zygzag.zygzagsmod.common.registry.RecipeSerializerRegistry;
import com.zygzag.zygzagsmod.common.registry.RecipeTypeRegistry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class TransmutationRecipe implements Recipe<SimpleContainer> {
    Ingredient inItem;
    Item outItem;
    double rate;
    ResourceLocation id;
    public TransmutationRecipe(Ingredient inItem, Item outItem, double rate, ResourceLocation id) {
        this.inItem = inItem;
        this.outItem = outItem;
        this.rate = rate;
        this.id = id;
    }

    @Override
    public boolean matches(SimpleContainer holder, Level world) {
        return inItem.test(holder.getItem(0));
    }

    @Override
    public ItemStack assemble(SimpleContainer holder, RegistryAccess access) {
        int count = holder.getItem(0).getCount();
        double random = (count * rate) % 1;
        int newCount = (int) (count * rate) + (Math.random() <= random ? 1 : 0);
        return new ItemStack(outItem, newCount);
    }

    @Override
    public boolean canCraftInDimensions(int x, int y) {
        return x * y >= 1;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess access) {
        return outItem.getDefaultInstance();
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeSerializerRegistry.TRANSMUTATION_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return RecipeTypeRegistry.TRANSMUTATION.get();
    }

    public Ingredient getInItem() {
        return inItem;
    }

    public Item getOutItem() {
        return outItem;
    }

    public double getRate() {
        return rate;
    }

    public static class TransmutationSerializer implements RecipeSerializer<TransmutationRecipe> {

        @Override
        public TransmutationRecipe fromJson(ResourceLocation id, JsonObject obj) {
            Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(obj.get("output").getAsString()));
            if (item == null) item = Items.AIR;
            return new TransmutationRecipe(
                    Ingredient.fromJson(obj.get("input")),
                    item,
                    obj.get("rate").getAsDouble(),
                    id
            );
        }

        @Nullable
        @Override
        public TransmutationRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(buf.readUtf()));
            if (item == null) item = Items.AIR;
            return new TransmutationRecipe(
                    Ingredient.fromNetwork(buf),
                    item,
                    buf.readDouble(),
                    id
            );
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, TransmutationRecipe recipe) {
            ResourceLocation id = ForgeRegistries.ITEMS.getKey(recipe.outItem);
            if (id == null) id = new ResourceLocation("minecraft:air");
            buf.writeUtf(id.toString());
            recipe.inItem.toNetwork(buf);
            buf.writeDouble(recipe.rate);
        }
    }

}
