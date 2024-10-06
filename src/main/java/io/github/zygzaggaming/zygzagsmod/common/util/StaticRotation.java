package io.github.zygzaggaming.zygzagsmod.common.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.phys.Vec3;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

import static java.lang.Math.*;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public sealed interface StaticRotation permits StaticRotation.XYZAngles, StaticRotation.QuaternionBacked {
    public static final StaticRotation ZERO = new QuaternionBacked(Quaternion.ZERO);
    static StaticRotation slerp(StaticRotation start, StaticRotation end, double t) {
        if (t <= 0) return start;
        if (t >= 1) return end;
        return new QuaternionBacked(Quaternion.slerp(start.asQuaternion(), end.asQuaternion(), t));
    }

    enum StaticRotationType {
        QUATERNION_BACKED(QuaternionBacked.MAP_CODEC),
        XYZ_ANGLES(XYZAngles.MAP_CODEC);
        final MapCodec<? extends StaticRotation> mapCodec;
        StaticRotationType(MapCodec<? extends StaticRotation> mapCodec) {
            this.mapCodec = mapCodec;
        }
        MapCodec<? extends StaticRotation> mapCodec() {
            return mapCodec;
        }
    }
    Codec<StaticRotation> CODEC = GeneralUtil.enumCodec(StaticRotationType.class).dispatch(StaticRotation::staticType, StaticRotationType::mapCodec);

    Angle getXRot();
    Angle getYRot();
    Angle getZRot();

    Quaternion asQuaternion();

    Vec3 appliedTo(Vec3 other);

    StaticRotationType staticType();

    record QuaternionBacked(Quaternion backing) implements StaticRotation {
        static MapCodec<QuaternionBacked> MAP_CODEC = Quaternion.CODEC.xmap(QuaternionBacked::new, QuaternionBacked::backing).fieldOf("value");

        @Override
        public Angle getYRot() { // yRot
            double a = backing.a(), b = backing.b(), c = backing.c(), d = backing.d();
            double ap = a - c, bp = b + d, cp = c + a, dp = d - b;

            return Angle.radians(acos(2 * (ap * ap + bp * bp) / (ap * ap + bp * bp + cp * cp + dp * dp) - 1)).minus(Angle.RIGHT);
        }

        @Override
        public Angle getZRot() { // zRot
            double a = backing.a(), b = backing.b(), c = backing.c(), d = backing.d();
            double ap = a - c, bp = b + d, cp = c + a, dp = d - b;

            Angle angle2p = Angle.radians(acos(2 * (ap * ap + bp * bp) / (ap * ap + bp * bp + cp * cp + dp * dp) - 1));
            Angle thetaPlus = Angle.radians(atan2(bp, ap)), thetaMinus = Angle.radians(atan2(dp, cp));

            if (abs(angle2p.inRadians()) <= 1e-6 || abs(angle2p.minus(Angle.HALF_RIGHT).inRadians()) <= 1e-6) return thetaPlus.scaled(2);
            return thetaPlus.plus(thetaMinus);
        }

        @Override
        public Angle getXRot() { // xRot
            double a = backing.a(), b = backing.b(), c = backing.c(), d = backing.d();
            double ap = a - c, bp = b + d, cp = c + a, dp = d - b;

            Angle angle2p = Angle.radians(acos(2 * (ap * ap + bp * bp) / (ap * ap + bp * bp + cp * cp + dp * dp) - 1));
            if (abs(angle2p.inRadians()) <= 1e-6 || abs(angle2p.minus(Angle.HALF_RIGHT).inRadians()) <= 1e-6) return Angle.ZERO;
            Angle thetaPlus = Angle.radians(atan2(bp, ap)), thetaMinus = Angle.radians(atan2(dp, cp));
            return thetaPlus.minus(thetaMinus);
        }

        @Override
        public Quaternion asQuaternion() {
            return backing;
        }

        @Override
        public Vec3 appliedTo(Vec3 other) {
            return backing.conjugate(other);
        }

        @Override
        public StaticRotationType staticType() {
            return StaticRotationType.QUATERNION_BACKED;
        }
    }

    record XYZAngles(Angle xRot, Angle yRot, Angle zRot) implements StaticRotation {
        static MapCodec<XYZAngles> MAP_CODEC = Angle.CODEC.listOf(3, 3).xmap(
                list -> new XYZAngles(list.get(0), list.get(1), list.get(2)),
                angles -> List.of(angles.xRot, angles.yRot, angles.zRot)
        ).fieldOf("angles");

        @Override
        public Angle getXRot() {
            return xRot;
        }

        @Override
        public Angle getYRot() {
            return yRot;
        }

        @Override
        public Angle getZRot() {
            return zRot;
        }

        @Override
        public Quaternion asQuaternion() {
            return Quaternion.zyx(xRot, yRot, zRot);
        }

        @Override
        public Vec3 appliedTo(Vec3 other) {
            return asQuaternion().conjugate(other);
        }

        @Override
        public StaticRotationType staticType() {
            return StaticRotationType.XYZ_ANGLES;
        }
    }

    public static void main(String[] args) {
        // TESTING
        Angle xRot = Angle.revolutions(-3 / 32.0).clampRevolutions(-0.25, 0.25), yRot = Angle.revolutions(14 / 32.0).normalized(), zRot = Angle.revolutions(-5 / 32.0).normalizedRevolutions(-0.5, 0.5);
        Quaternion quaternion = Quaternion.yxz(xRot, yRot, zRot);
        //ZXY
        // e'' = z, e' = y, e = x, eps = 1
        double a = quaternion.a(), b = quaternion.b(), c = quaternion.c(), d = quaternion.d();
        double ap = a - c, bp = b + d, cp = c + a, dp = d - b;

        Angle angle2p = Angle.radians(acos(2 * (ap * ap + bp * bp) / (ap * ap + bp * bp + cp * cp + dp * dp) - 1));
        Angle thetaPlus = Angle.radians(atan2(bp, ap)), thetaMinus = Angle.radians(atan2(dp, cp));

        Angle angle1 = thetaPlus.minus(thetaMinus), angle3 = thetaPlus.plus(thetaMinus);
        Angle angle2 = angle2p.minus(Angle.RIGHT)/*, angle3 = angle3p.inverse()*/;

        System.out.println(angle1 + ", " + angle2 + ", " + angle3);
        System.out.println(zRot + ", " + xRot + ", " + yRot);

        System.out.println(quaternion);
        System.out.println(Quaternion.zyx(angle1, angle2, angle3));
    }
}
