package com.zygzag.zygzagsmod.common.entity;

import software.bernie.geckolib.core.animation.RawAnimation;

public abstract class AbstractAnimation {
    private final RawAnimation raw;
    private final int lengthInTicks;
    private final boolean canBeSkipped;

    public AbstractAnimation(RawAnimation raw, int lengthInTicks, boolean canBeSkipped) {
        this.raw = raw;
        this.lengthInTicks = lengthInTicks;
        this.canBeSkipped = canBeSkipped;
    }

    public AbstractAnimation(RawAnimation raw, int lengthInTicks) {
        this(raw, lengthInTicks, true);
    }

    public RawAnimation raw() {
        return raw;
    }

    public int lengthInTicks() {
        return lengthInTicks;
    }

    public boolean canBeSkipped() {
        return canBeSkipped;
    }
}
