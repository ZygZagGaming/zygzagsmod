package com.zygzag.zygzagsmod.common.effect;

import com.zygzag.zygzagsmod.common.GeneralUtil;
import com.zygzag.zygzagsmod.common.Main;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Function;
import java.util.function.Predicate;

import static com.zygzag.zygzagsmod.common.GeneralUtil.ifCapability;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class SightEffect extends MobEffect {
    public Predicate<BlockState> predicate;
    public Function<BlockState, Integer> selector;
    public SightEffect(MobEffectCategory category, int color, Predicate<BlockState> blockPredicate, Function<BlockState, Integer> colorSelector) {
        super(category, color);
        this.predicate = blockPredicate;
        this.selector = colorSelector;
    }

    public boolean test(BlockState state) {
        return predicate.test(state);
    }

    public int color(BlockState state) {
        return selector.apply(state);
    }

    public int range(int amplifier) {
        return 20 * (1 + amplifier);
    }
}
