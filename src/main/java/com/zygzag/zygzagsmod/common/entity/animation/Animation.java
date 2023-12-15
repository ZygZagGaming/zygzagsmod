package com.zygzag.zygzagsmod.common.entity.animation;

import com.zygzag.zygzagsmod.common.Main;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.core.animation.RawAnimation;

import java.util.function.Supplier;

public class Animation extends AbstractAnimation {
    public Animation(RawAnimation raw, int lengthInTicks, boolean canBeSkipped) {
        super(raw, lengthInTicks, canBeSkipped);
    }

    public Animation(RawAnimation raw, int lengthInTicks) {
        super(raw, lengthInTicks);
    }

    @Override
    public ResourceLocation id() {
        ResourceLocation loc = Main.animationRegistry().getKey(this);
        assert loc != null;
        return loc;
    }

    public boolean is(Supplier<Animation> other) {
        ResourceLocation key = Main.animationRegistry().getKey(this);
        return key != null && key.equals(other.get().id());
    }

    @Override
    public boolean is(AbstractAnimation other) {
        return other instanceof Animation anim && anim.id().equals(this.id());
    }

    @Override
    public String toString() {
        return "[normal animation " + id() + "]";
    }
}
