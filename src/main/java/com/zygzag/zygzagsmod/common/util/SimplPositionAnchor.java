package com.zygzag.zygzagsmod.common.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import static com.zygzag.zygzagsmod.common.util.GeneralUtil.lerp;

public class SimplPositionAnchor implements PositionAnchor {
    public static Codec<SimplPositionAnchor> CODEC = RecordCodecBuilder.create(instance ->
        instance.group(
                Codec.DOUBLE.fieldOf("x").forGetter(SimplPositionAnchor::getX),
                Codec.DOUBLE.fieldOf("y").forGetter(SimplPositionAnchor::getY),
                Codec.DOUBLE.fieldOf("z").forGetter(SimplPositionAnchor::getZ),
                Codec.DOUBLE.fieldOf("xO").forGetter(SimplPositionAnchor::getXO),
                Codec.DOUBLE.fieldOf("yO").forGetter(SimplPositionAnchor::getYO),
                Codec.DOUBLE.fieldOf("zO").forGetter(SimplPositionAnchor::getZO)
        ).apply(instance, SimplPositionAnchor::new)
    );
    private double x, xO, xN;
    private double y, yO, yN;
    private double z, zO, zN;

    public SimplPositionAnchor() {}
    public SimplPositionAnchor(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;

        this.xO = x;
        this.yO = y;
        this.zO = z;
    }
    public SimplPositionAnchor(double x, double y, double z, double xO, double yO, double zO) {
        this.x = x;
        this.y = y;
        this.z = z;

        this.xO = xO;
        this.yO = yO;
        this.zO = zO;
    }

    public double getXO() {
        return xO;
    }
    public double getYO() {
        return yO;
    }
    public double getZO() {
        return zO;
    }

    @Override
    public void tick() {
        xO = x; x = xN;
        yO = y; y = yN;
        zO = z; z = zN;
    }

    @Override
    public void setX(double xN) {
        this.xN = xN;
    }

    @Override
    public double getX() {
        return x;
    }

    @Override
    public double getX(double partialTick) {
        return lerp(xO, x, partialTick);
    }

    @Override
    public void setY(double yN) {
        this.yN = yN;
    }

    @Override
    public double getY() {
        return y;
    }

    @Override
    public double getY(double partialTick) {
        return lerp(yO, y, partialTick);
    }

    @Override
    public void setZ(double zN) {
        this.zN = zN;
    }

    @Override
    public double getZ() {
        return z;
    }

    @Override
    public double getZ(double partialTick) {
        return lerp(zO, z, partialTick);
    }
}
