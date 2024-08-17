package io.github.zygzaggaming.zygzagsmod.common.entity.animation;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.animation.RawAnimation;

public abstract class AbstractAction {
    private final RawAnimation raw;
    private final int lengthInTicks;
    private final boolean canBeSkipped;

    public AbstractAction(RawAnimation raw, int lengthInTicks, boolean canBeSkipped) {
        this.raw = raw;
        this.lengthInTicks = lengthInTicks;
        this.canBeSkipped = canBeSkipped;
    }

    public AbstractAction(RawAnimation raw, int lengthInTicks) {
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

    public boolean is(AbstractAction other) {
        return this == other;
    }

    public ResourceLocation id() {
        return null;
    }
}
