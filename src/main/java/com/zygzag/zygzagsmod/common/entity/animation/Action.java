package com.zygzag.zygzagsmod.common.entity.animation;

import com.zygzag.zygzagsmod.common.Main;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.core.animation.RawAnimation;

import java.util.function.Supplier;

public class Action extends AbstractAction {
    public Action(RawAnimation raw, int lengthInTicks, boolean canBeSkipped) {
        super(raw, lengthInTicks, canBeSkipped);
    }

    public Action(RawAnimation raw, int lengthInTicks) {
        super(raw, lengthInTicks);
    }

    @Override
    public ResourceLocation id() {
        ResourceLocation loc = Main.actionRegistry().getKey(this);
        assert loc != null;
        return loc;
    }

    public boolean is(Supplier<Action> other) {
        ResourceLocation key = Main.actionRegistry().getKey(this);
        return key != null && key.equals(other.get().id());
    }

    @Override
    public boolean is(AbstractAction other) {
        return other instanceof Action anim && anim.id().equals(this.id());
    }

    @Override
    public String toString() {
        return "[normal action " + id() + "]";
    }
}
