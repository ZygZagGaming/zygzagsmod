package io.github.zygzaggaming.zygzagsmod.common.util;

import com.mojang.serialization.Codec;

import static io.github.zygzaggaming.zygzagsmod.common.util.GeneralUtil.mod;
import static java.lang.Math.*;

public class Angle {
    public static final Codec<Angle> CODEC = Codec.DOUBLE.xmap(Angle::radians, Angle::inRadians);
    public static final Angle ZERO = new Angle(0), RIGHT = new Angle(PI * 0.5), HALF_RIGHT = RIGHT.scaled(0.5), STRAIGHT = revolutions(0.5);
    private static final double RAD_REV = 0.5 / PI, RAD_DEG = 180.0 * RAD_REV,
            DEG_REV = 1 / 360.0, DEG_RAD = PI * DEG_REV;
    public static Angle radians(double value) {
        return new Angle(value);
    }
    public static Angle degrees(double value) {
        return new Angle(value * DEG_RAD);
    }
    public static Angle revolutions(double value) {
        return new Angle(value * 2 * PI);
    }

    private final double radians;
    private Angle(double radians) {
        this.radians = radians;
    }

    public static Angle arccos(double dot) {
        return radians(acos(dot));
    }
    public double inRadians() {
        return radians;
    }
    public double inDegrees() {
        return radians * RAD_DEG;
    }
    public double inRevolutions() {
        return radians * RAD_REV;
    }

    public double sine() {
        return sin(radians);
    }
    public double cosine() {
        return cos(radians);
    }

    public Angle normalized() {
        return revolutions(mod(inRevolutions(), 1));
    }

    public Angle normalized(Angle lower, Angle upper) {
        return radians(mod(radians - lower.radians, upper.radians - lower.radians) + lower.radians);
    }

    public Angle normalizedRevolutions(double lower, double upper) {
        return normalized(revolutions(lower), revolutions(upper));
    }

    public Angle scaled(double value) {
        return revolutions(inRevolutions() * value);
    }

    public Angle inverse() {
        return scaled(-1);
    }

    @Override
    public String toString() {
        return "(" + radians + ") rad";
    }

    public Angle clamp(Angle lower, Angle upper) {
        if (radians < lower.radians) return lower;
        if (radians > upper.radians) return upper;
        return this;
    }

    public Angle clampRevolutions(double lower, double upper) {
        return clamp(revolutions(lower), revolutions(upper));
    }

    public Angle plus(Angle other) {
        return radians(radians + other.radians);
    }

    public Angle minus(Angle other) {
        return radians(radians - other.radians);
    }
}
