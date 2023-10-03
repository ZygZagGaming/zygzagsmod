package com.zygzag.zygzagsmod.common.entity;

import com.zygzag.zygzagsmod.common.Main;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.RegistryObject;
import software.bernie.geckolib.core.animation.RawAnimation;

import java.util.function.Function;

public class TransitionAnimation extends AbstractAnimation {
    private final AbstractAnimation from, to;
    private final Function<Integer, Float> speedModifierSupplier;

    public TransitionAnimation(AbstractAnimation from, AbstractAnimation to, RawAnimation raw, int lengthInTicks, Function<Integer, Float> speedModifierSupplier) {
        super(raw, lengthInTicks, false);
        this.from = from;
        this.to = to;
        this.speedModifierSupplier = speedModifierSupplier;
    }

    public float speedModifier(int ticksUntilAnimEnds) {
        return speedModifierSupplier.apply(ticksUntilAnimEnds);
    }

    public AbstractAnimation from() {
        return from;
    }

    public AbstractAnimation to() {
        return to;
    }

    public boolean is(RegistryObject<TransitionAnimation> other) {
        ResourceLocation key = Main.transitionAnimationRegistry().getKey(this);
        return key != null && key.equals(other.getId());
    }
}
