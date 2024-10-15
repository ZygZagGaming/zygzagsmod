package io.github.zygzaggaming.zygzagsmod.common.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.phys.Vec3;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

import static io.github.zygzaggaming.zygzagsmod.common.util.GeneralUtil.quaternionToXYZTaitBryanAngles;
import static io.github.zygzaggaming.zygzagsmod.common.util.GeneralUtil.sqr;
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

        public Angle[] getXYZTaitBryanAngles() {
            return quaternionToXYZTaitBryanAngles(backing);
        }

        @Override
        public Angle getYRot() { // yRot
            return getXYZTaitBryanAngles()[1];
        }

        @Override
        public Angle getZRot() { // zRot
            return getXYZTaitBryanAngles()[2];
        }

        @Override
        public Angle getXRot() { // xRot
            return getXYZTaitBryanAngles()[0];
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

//    public static void main(String[] args) {
//        // TESTING
////        Angle zRot = Angle.random(), yRot = Angle.random(Angle.RIGHT.inverse(), Angle.RIGHT), xRot = Angle.random();
//        Angle zRot = Angle.radians(-3.1277), yRot = Angle.radians(-0.68417), xRot = Angle.radians(-2.54621);
//        Quaternion quaternion = Quaternion.zyx(xRot, yRot, zRot);
//        System.out.println("zRot: " + zRot + ", yRot: " + yRot + ", xRot: " + xRot);
//        System.out.println(quaternion);
//
//        // algorithm used was found at https://www.ncbi.nlm.nih.gov/pmc/articles/PMC9648712/
//        // e = z, e' = y, e'' = x, eps = -1
//        // a = q.a, b = q.d, c = q.c, d = -q.b
//        // a' = a - c, b' = b + d, c' = c + a, d' = d - b
//        // theta2 = acos(2 * (a'^2 + b'^2) - 1) - pi/2
//        Angle theta2 = Angle.acos(sqr(quaternion.a() - quaternion.c()) + sqr(quaternion.d() - quaternion.b()) - 1).minus(Angle.RIGHT);
//        System.out.println(theta2);
//
//        // theta1 = atan2(b', a') - atan2(d', c')
//        Angle theta1 = Angle.atan2(quaternion.d() - quaternion.b(), quaternion.a() - quaternion.c()).minus(Angle.atan2( -quaternion.b() - quaternion.d(), quaternion.c() + quaternion.a()));
//        System.out.println(theta1);
//
//        // theta3 = eps * (atan2(b', a') + atan2(d', c'))
//        Angle theta3 = Angle.atan2(quaternion.d() - quaternion.b(), quaternion.a() - quaternion.c()).plus(Angle.atan2(-quaternion.b() - quaternion.d(), quaternion.c() + quaternion.a())).inverse();
//        System.out.println(theta3);
//    }
}
