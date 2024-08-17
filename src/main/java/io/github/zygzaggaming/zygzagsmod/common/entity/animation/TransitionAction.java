package io.github.zygzaggaming.zygzagsmod.common.entity.animation;

import io.github.zygzaggaming.zygzagsmod.common.Main;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.animation.RawAnimation;

import java.util.function.Function;
import java.util.function.Supplier;

public class TransitionAction extends AbstractAction {
    private final AbstractAction from, to;
    private final Function<Integer, Float> speedModifierSupplier;

    public TransitionAction(AbstractAction from, AbstractAction to, RawAnimation raw, int lengthInTicks, Function<Integer, Float> speedModifierSupplier) {
        super(raw, lengthInTicks, false);
        this.from = from;
        this.to = to;
        this.speedModifierSupplier = speedModifierSupplier;
    }

    public TransitionAction(AbstractAction from, AbstractAction to, RawAnimation raw, int lengthInTicks, Function<Integer, Float> speedModifierSupplier, boolean canBeSkipped) {
        super(raw, lengthInTicks, canBeSkipped);
        this.from = from;
        this.to = to;
        this.speedModifierSupplier = speedModifierSupplier;
    }

    public float speedModifier(int ticksUntilAnimEnds) {
        return speedModifierSupplier.apply(ticksUntilAnimEnds);
    }

    public AbstractAction from() {
        return from;
    }

    public AbstractAction to() {
        return to;
    }

    public boolean is(Supplier<TransitionAction> other) {
        ResourceLocation key = Main.transitionActionRegistry().getKey(this);
        return key != null && key.equals(other.get().id());
    }

    @Override
    public boolean is(AbstractAction other) {
        return other instanceof TransitionAction transition && transition.id().equals(this.id());
    }

    @Override
    public ResourceLocation id() {
        ResourceLocation loc = Main.transitionActionRegistry().getKey(this);
        assert loc != null;
        return loc;
    }

    @Override
    public String toString() {
        return "[transition action " + id() + "]";
    }
}
