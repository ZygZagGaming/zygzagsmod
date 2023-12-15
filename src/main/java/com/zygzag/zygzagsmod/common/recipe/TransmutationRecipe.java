package com.zygzag.zygzagsmod.common.recipe;


import com.mojang.blaze3d.MethodsReturnNonnullByDefault;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.zygzag.zygzagsmod.common.registry.RecipeSerializerRegistry;
import com.zygzag.zygzagsmod.common.registry.RecipeTypeRegistry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class TransmutationRecipe implements Recipe<SimpleContainer> {
    Ingredient inItem;
    Item outItem;
    double rate;

    public TransmutationRecipe(Ingredient inItem, Item outItem, double rate) {
        this.inItem = inItem;
        this.outItem = outItem;
        this.rate = rate;
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
    public RecipeSerializer<?> getSerializer() {
        return RecipeSerializerRegistry.TRANSMUTATION_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return RecipeTypeRegistry.TRANSMUTATION.get();
    }

    public Ingredient inItem() {
        return inItem;
    }

    public Item outItem() {
        return outItem;
    }

    public double rate() {
        return rate;
    }

    public static class TransmutationSerializer implements RecipeSerializer<TransmutationRecipe> {
        @Nullable
        @Override
        public TransmutationRecipe fromNetwork(FriendlyByteBuf buf) {
            Item item = BuiltInRegistries.ITEM.get(new ResourceLocation(buf.readUtf()));
            return new TransmutationRecipe(
                    Ingredient.fromNetwork(buf),
                    item,
                    buf.readDouble()
            );
        }

        @Override
        public Codec<TransmutationRecipe> codec() {
            return RecordCodecBuilder.create(instance ->
                    instance.group(
                            Ingredient.VANILLA_CODEC.fieldOf("inItem").forGetter(TransmutationRecipe::inItem),
                            BuiltInRegistries.ITEM.byNameCodec().fieldOf("inItem").forGetter(TransmutationRecipe::outItem),
                            Codec.DOUBLE.fieldOf("inItem").forGetter(TransmutationRecipe::rate)
                    ).apply(instance, TransmutationRecipe::new)
            );
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, TransmutationRecipe recipe) {
            ResourceLocation id = BuiltInRegistries.ITEM.getKey(recipe.outItem);
            buf.writeUtf(id.toString());
            recipe.inItem.toNetwork(buf);
            buf.writeDouble(recipe.rate);
        }
    }

}
