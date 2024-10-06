package io.github.zygzaggaming.zygzagsmod.common.util;

import com.mojang.serialization.Codec;
import net.minecraft.Util;
import net.minecraft.world.phys.Vec3;

import java.util.List;

import static java.lang.Math.*;

public record Quaternion(double a, double b, double c, double d) {
    public static Codec<Quaternion> CODEC = Codec.DOUBLE
            .listOf()
            .comapFlatMap(
                    list -> Util.fixedSize(list, 4).map(it -> new Quaternion(it.get(0), it.get(1), it.get(2), it.get(3))),
                    quaternion -> List.of(quaternion.a(), quaternion.b(), quaternion.c(), quaternion.d())
            );

    public static Quaternion ZERO = new Quaternion(0, 0, 0, 0),
            ONE = new Quaternion(1, 0, 0, 0),
            I = new Quaternion(0, 1, 0, 0),
            J = new Quaternion(0, 0, 1, 0),
            K = new Quaternion(0, 0, 0, 1);

    public static Quaternion between(Vec3 a, Vec3 b) {
        a = a.normalize(); b = b.normalize();
        Vec3 cross = a.cross(b);
        if (cross.lengthSqr() <= 1e-4) {
            return Quaternion.axisAngle(new Vec3(0, 1, 0).cross(a).cross(a), Angle.STRAIGHT);
        }
        return new Quaternion(a.dot(b), cross).normalized();
    }

    public static Quaternion axisAngle(Vec3 axis, Angle angle) {
        angle = angle.scaled(0.5);
        return new Quaternion(angle.cosine(), axis.normalize().scale(angle.sine())).normalized();
    }

    public static Quaternion yxz(Angle xRot, Angle yRot, Angle zRot) {
        return Quaternion.axisAngle(new Vec3(0, 1, 0), yRot).times(Quaternion.axisAngle(new Vec3(1, 0, 0), xRot)).times(Quaternion.axisAngle(new Vec3(0, 0, 1), zRot));
    }

    public static Quaternion zyx(Angle xRot, Angle yRot, Angle zRot) {
        return Quaternion.axisAngle(new Vec3(1, 0, 0), xRot).times(Quaternion.axisAngle(new Vec3(0, 1, 0), yRot)).times(Quaternion.axisAngle(new Vec3(0, 0, 1), zRot));
    }

    public static Quaternion slerp(Quaternion q0, Quaternion q1, double t) {
        if (t <= 0) return q0;
        if (t >= 1) return q1;
        if (signum(q0.a()) != signum(q1.a())) q0 = q0.times(-1);
        return q0.times((q0.inverse().times(q1)).pow(t));
    }

    public Quaternion(double real, Vec3 imag) {
        this(real, imag.x(), imag.y(), imag.z());
    }

    public Quaternion plus(Quaternion other) {
        return new Quaternion(a + other.a, b + other.b, c + other.c, d + other.d);
    }

    public Quaternion times(Quaternion other) {
        return new Quaternion(
                a * other.a - b * other.b - c * other.c - d * other.d,
                a * other.b + b * other.a + c * other.d - d * other.c,
                a * other.c - b * other.d + c * other.a + d * other.b,
                a * other.d + b * other.c - c * other.b + d * other.a
        );
    }

    public Quaternion times(double other) {
        return new Quaternion(a * other, b * other, c * other, d * other);
    }

    public Quaternion div(Quaternion other) {
        return times(other.inverse());
    }
    public Quaternion div(double other) {
        return new Quaternion(a / other, b / other, c / other, d / other);
    }

    public Quaternion conjugate() {
        return new Quaternion(a, -b, -c, -d);
    }

    public double normSqr() {
        return a * a + b * b + c * c + d * d;
    }

    public double norm() {
        return sqrt(normSqr());
    }

    public Quaternion inverse() {
        return conjugate().div(normSqr());
    }

    public Quaternion conjugationBy(Quaternion other) {
        return other.times(this.times(other.inverse()));
    }

    public Vec3 vectorPart() {
        return new Vec3(b, c, d);
    }

    public Vec3 conjugate(Vec3 other) {
        return new Quaternion(0, other.x, other.y, other.z).conjugationBy(this).vectorPart();
    }

    public Quaternion normalized() {
        return div(norm());
    }

    public Vec3 versorLogarithm() {
        Quaternion normalized = normalized();
        double mult = acos(normalized.a);
        return normalized.vectorPart().scale(mult / sin(mult));
    }

    public Quaternion pow(double other) {
        Vec3 vlog = versorLogarithm();
        double omega = vlog.length();
        return new Quaternion(cos(other * omega), vlog.normalize().scale(sin(other * omega)));
    }

    @Override
    public String toString() {
        return "(" + a + ") + (" + b + ")i + (" + c + ")j + (" + d + ")k";
    }
}