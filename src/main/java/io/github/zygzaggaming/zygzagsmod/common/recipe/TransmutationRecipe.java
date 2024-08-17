package io.github.zygzaggaming.zygzagsmod.common.recipe;


import com.mojang.blaze3d.MethodsReturnNonnullByDefault;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.zygzaggaming.zygzagsmod.common.registry.RecipeSerializerRegistry;
import io.github.zygzaggaming.zygzagsmod.common.registry.RecipeTypeRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class TransmutationRecipe implements Recipe<SingleRecipeInput> {
    Ingredient inItem;
    Item outItem;
    double rate;

    public TransmutationRecipe(Ingredient inItem, Item outItem, double rate) {
        this.inItem = inItem;
        this.outItem = outItem;
        this.rate = rate;
    }

    @Override
    public boolean matches(SingleRecipeInput holder, Level world) {
        return inItem.test(holder.getItem(0));
    }

    @Override
    public ItemStack assemble(SingleRecipeInput holder, HolderLookup.Provider provider) {
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
    public ItemStack getResultItem(HolderLookup.Provider provider) {
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
        public static final StreamCodec<RegistryFriendlyByteBuf, TransmutationRecipe> STREAM_CODEC = StreamCodec.of(
                (buf, recipe) -> {
                    ResourceLocation id = BuiltInRegistries.ITEM.getKey(recipe.outItem);
                    buf.writeUtf(id.toString());
                    Ingredient.CONTENTS_STREAM_CODEC.encode(buf, recipe.inItem);
                    buf.writeDouble(recipe.rate);
                },
                (buf) -> {
                    Item item = BuiltInRegistries.ITEM.get(ResourceLocation.parse(buf.readUtf()));
                    return new TransmutationRecipe(
                            Ingredient.CONTENTS_STREAM_CODEC.decode(buf),
                            item,
                            buf.readDouble()
                    );
                }
        );
        @Override
        public MapCodec<TransmutationRecipe> codec() {
            return RecordCodecBuilder.mapCodec((RecordCodecBuilder.Instance<TransmutationRecipe> instance) ->
                    instance.group(
                            Ingredient.CODEC.fieldOf("inItem").forGetter(TransmutationRecipe::inItem),
                            BuiltInRegistries.ITEM.byNameCodec().fieldOf("outItem").forGetter(TransmutationRecipe::outItem),
                            Codec.DOUBLE.fieldOf("rate").forGetter(TransmutationRecipe::rate)
                    ).apply(instance, TransmutationRecipe::new)
            );
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, TransmutationRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }

}
