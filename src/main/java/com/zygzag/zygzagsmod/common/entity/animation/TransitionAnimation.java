package com.zygzag.zygzagsmod.common.entity.animation;

import com.zygzag.zygzagsmod.common.Main;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.core.animation.RawAnimation;

import java.util.function.Function;
import java.util.function.Supplier;

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

    public boolean is(Supplier<TransitionAnimation> other) {
        ResourceLocation key = Main.transitionAnimationRegistry().getKey(this);
        return key != null && key.equals(other.get().id());
    }

    @Override
    public boolean is(AbstractAnimation other) {
        return other instanceof TransitionAnimation transition && transition.id().equals(this.id());
    }

    @Override
    public ResourceLocation id() {
        ResourceLocation loc = Main.transitionAnimationRegistry().getKey(this);
        assert loc != null;
        return loc;
    }

    @Override
    public String toString() {
        return "[transition animation " + id() + "]";
    }
}
