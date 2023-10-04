package com.zygzag.zygzagsmod.common.entity;

import com.zygzag.zygzagsmod.common.Main;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.RegistryObject;
import software.bernie.geckolib.core.animation.RawAnimation;

public class Animation extends AbstractAnimation {
    public Animation(RawAnimation raw, int lengthInTicks, boolean canBeSkipped) {
        super(raw, lengthInTicks, canBeSkipped);
    }

    public Animation(RawAnimation raw, int lengthInTicks) {
        super(raw, lengthInTicks);
    }

    public ResourceLocation id() {
        ResourceLocation loc = Main.animationRegistry().getKey(this);
        assert loc != null;
        return loc;
    }

    public boolean is(RegistryObject<Animation> other) {
        ResourceLocation key = Main.animationRegistry().getKey(this);
        return key != null && key.equals(other.getId());
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
