package io.github.zygzaggaming.zygzagsmod.common.loot;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ResolvableProfile;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ExecutionerModifier extends LootModifier {
    public static MapCodec<ExecutionerModifier> CODEC = RecordCodecBuilder.mapCodec(
            (RecordCodecBuilder.Instance<ExecutionerModifier> inst) -> codecStart(inst)
                    .and(
                            inst.group(
                                    BuiltInRegistries.ITEM.byNameCodec()
                                            .fieldOf("item_to_drop")
                                            .forGetter((it) -> it.itemOut)
                            ).t1()
                    )
                    .apply(
                            inst,
                            ExecutionerModifier::new
                    )
    );

    Item itemOut;

    /**
     * Constructs a LootModifier.
     *
     * @param conditionsIn the ILootConditions that need to be matched before the loot is modified.
     */
    protected ExecutionerModifier(LootItemCondition[] conditionsIn, Item itemOut) {
        super(conditionsIn);
        this.itemOut = itemOut;
    }

    @NotNull
    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        if (!generatedLoot.contains(Items.WITHER_SKELETON_SKULL.getDefaultInstance())) {
            ItemStack item = itemOut.getDefaultInstance();
            Entity entity = context.getParam(LootContextParams.THIS_ENTITY);
            if (entity instanceof Player player && item.is(Items.PLAYER_HEAD)) item.set(DataComponents.PROFILE, new ResolvableProfile(player.getGameProfile()));
            generatedLoot.add(item);
        }
        return generatedLoot;
    }

    @Override
    public MapCodec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }
}