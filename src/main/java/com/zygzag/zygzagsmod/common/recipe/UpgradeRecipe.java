package com.zygzag.zygzagsmod.common.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.zygzag.zygzagsmod.common.registry.AttachmentTypeRegistry;
import com.zygzag.zygzagsmod.common.registry.ItemUpgradeRegistry;
import com.zygzag.zygzagsmod.common.registry.RecipeSerializerRegistry;
import com.zygzag.zygzagsmod.common.upgrade.ItemUpgrade;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SmithingRecipe;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class UpgradeRecipe implements SmithingRecipe {
    private final Ingredient template, addition;
    private final @Nullable Ingredient possibleBase;
    private final ItemUpgrade upgrade;
    public UpgradeRecipe(Ingredient template, Ingredient addition, ItemUpgrade upgrade) {
        this.template = template;
        this.addition = addition;
        this.upgrade = upgrade;
        this.possibleBase = null;
    }
    public UpgradeRecipe(Ingredient template, Ingredient addition, ItemUpgrade upgrade, @Nullable Ingredient possibleBase) {
        this.template = template;
        this.addition = addition;
        this.upgrade = upgrade;
        this.possibleBase = possibleBase;
    }

    public Ingredient getTemplate() { return template; }
    public Ingredient getAddition() { return addition; }
    public ItemUpgrade getUpgrade() { return upgrade; }
    public boolean testBase(ItemStack item) { return possibleBase == null || possibleBase.test(item); }

    @Override
    public boolean isTemplateIngredient(ItemStack stack) {
        return template.test(stack);
    }

    @Override
    public boolean isBaseIngredient(ItemStack stack) {
        return testBase(stack);
    }

    @Override
    public boolean isAdditionIngredient(ItemStack stack) {
        return addition.test(stack);
    }

    @Override
    public boolean matches(Container container, Level world) {
        return isTemplateIngredient(container.getItem(0)) && isBaseIngredient(container.getItem(1)) && isAdditionIngredient(container.getItem(2));
    }

    @Override
    public ItemStack assemble(Container container, RegistryAccess access) {
        ItemStack stack = container.getItem(1).copy();
        stack.getData(AttachmentTypeRegistry.ITEM_UPGRADE_ATTACHMENT.get()).incrementApplied(upgrade);
        return stack;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess access) {
        return Items.NETHERITE_SWORD.getDefaultInstance();
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeSerializerRegistry.UPGRADE_SERIALIZER.get();
    }

    public static class Serializer implements RecipeSerializer<UpgradeRecipe> {
        public static final Codec<UpgradeRecipe> CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                        Ingredient.CODEC_NONEMPTY.fieldOf("template").forGetter(UpgradeRecipe::getTemplate),
                        Ingredient.CODEC_NONEMPTY.fieldOf("addition").forGetter(UpgradeRecipe::getAddition),
                        ItemUpgrade.CODEC.fieldOf("upgrade").forGetter(UpgradeRecipe::getUpgrade),
                        Ingredient.CODEC_NONEMPTY.optionalFieldOf("possible_base").forGetter((it) -> Optional.ofNullable(it.possibleBase))
                ).apply(instance, (a, b, c, d) -> new UpgradeRecipe(a, b, c, d.orElse(null)))
        );

        @Override
        public Codec<UpgradeRecipe> codec() {
            return CODEC;
        }

        @Override
        public UpgradeRecipe fromNetwork(FriendlyByteBuf buf) {
            Ingredient template = Ingredient.fromNetwork(buf);
            Ingredient addition = Ingredient.fromNetwork(buf);
            ResourceLocation upgradeLoc = buf.readResourceLocation();
            if (!ItemUpgradeRegistry.BACKING_REGISTRY.containsKey(upgradeLoc)) throw new IllegalArgumentException(upgradeLoc + " is not an existing item upgrade");
            ItemUpgrade upgrade = ItemUpgradeRegistry.BACKING_REGISTRY.get(upgradeLoc);
            assert upgrade != null;
            Ingredient possibleBase = buf.readBoolean() ? Ingredient.fromNetwork(buf) : null;
            return new UpgradeRecipe(template, addition, upgrade, possibleBase);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, UpgradeRecipe recipe) {
            recipe.getTemplate().toNetwork(buf);
            recipe.getAddition().toNetwork(buf);
            ResourceLocation location = ItemUpgradeRegistry.BACKING_REGISTRY.getKey(recipe.getUpgrade());
            if (location == null) throw new IllegalArgumentException("Why tf is there an unregistered upgrade instance?? Get that shit outta here");
            buf.writeResourceLocation(location);
            Ingredient k = recipe.possibleBase;
            boolean flag = k != null;
            buf.writeBoolean(flag);
            if (flag) k.toNetwork(buf);
        }
    }
}
