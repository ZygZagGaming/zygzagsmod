package io.github.zygzaggaming.zygzagsmod.common.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.function.Function;

import static java.lang.Math.acos;

public sealed interface WorldlyRotation permits WorldlyRotation.Anchored, WorldlyRotation.Concat, WorldlyRotation.Defer, WorldlyRotation.Lerped, WorldlyRotation.Static {
    Codec<WorldlyRotation> CODEC = Codec.recursive("worldly_rotation", c -> WorldlyRotationType.CODEC.dispatch(WorldlyRotation::type, (type) -> type.mapCodec(c)));

    enum WorldlyRotationType {
        STATIC(c -> Static.MAP_CODEC),

        LERPED(Lerped::mapCodec),
        ANCHORED(c -> Anchored.MAP_CODEC),
        TRACK(Track::mapCodec),
        CONCAT(Concat::mapCodec);
        public static final Codec<WorldlyRotationType> CODEC = GeneralUtil.enumCodec(WorldlyRotationType.class);
        final Function<Codec<WorldlyRotation>, MapCodec<? extends WorldlyRotation>> mapCodecProvider;
        WorldlyRotationType(Function<Codec<WorldlyRotation>, MapCodec<? extends WorldlyRotation>> mapCodecProvider) {
            this.mapCodecProvider = mapCodecProvider;
        }
        public MapCodec<? extends WorldlyRotation> mapCodec(Codec<WorldlyRotation> recursiveCodec) {
            return mapCodecProvider.apply(recursiveCodec);
        }
    }

    default Angle getXRot(Level world, double partialTick) {
        return apply(world, partialTick).getXRot();
    }
    default Angle getYRot(Level world, double partialTick) {
        return apply(world, partialTick).getYRot();
    }
    default Angle getZRot(Level world, double partialTick) {
        return apply(world, partialTick).getZRot();
    }

    StaticRotation apply(Level level, double partialTick);

    default void tick(Level world) {}

    WorldlyRotationType type();

    record Static(StaticRotation backing) implements WorldlyRotation {
        public static final MapCodec<Static> MAP_CODEC = StaticRotation.CODEC.xmap(Static::new, Static::backing).fieldOf("rotation");

        @Override
        public StaticRotation apply(Level world, double partialTick) {
            return backing;
        }

        @Override
        public WorldlyRotationType type() {
            return WorldlyRotationType.STATIC;
        }
    }

    record Lerped(WorldlyRotation start, WorldlyRotation end, double startTime, double ticks) implements WorldlyRotation {
        public static MapCodec<Lerped> mapCodec(Codec<WorldlyRotation> recursiveCodec) {
            return RecordCodecBuilder.mapCodec(inst ->
                    inst.group(
                            recursiveCodec.fieldOf("start").forGetter(Lerped::start),
                            recursiveCodec.fieldOf("end").forGetter(Lerped::end),
                            Codec.DOUBLE.fieldOf("start_time").forGetter(Lerped::startTime),
                            Codec.DOUBLE.fieldOf("ticks").forGetter(Lerped::ticks)
                    ).apply(inst, Lerped::new)
            );
        }

        @Override
        public StaticRotation apply(Level world, double partialTick) {
            return StaticRotation.slerp(start.apply(world, partialTick), end.apply(world, partialTick), (world.getGameTime() - startTime + partialTick) / ticks);
        }

        @Override
        public WorldlyRotationType type() {
            return WorldlyRotationType.LERPED;
        }
    }

    record Anchored(PositionAnchor start, PositionAnchor end, Angle roll) implements WorldlyRotation {
        public static final MapCodec<Anchored> MAP_CODEC = RecordCodecBuilder.mapCodec(inst ->
                inst.group(
                        PositionAnchor.CODEC.fieldOf("start").forGetter(Anchored::start),
                        PositionAnchor.CODEC.fieldOf("end").forGetter(Anchored::end),
                        Angle.CODEC.fieldOf("roll").forGetter(Anchored::roll)
                ).apply(inst, Anchored::new)
        );

        @Override
        public StaticRotation apply(Level world, double partialTick) {
            Vec3 del = end.apply(world).subtract(start.apply(world));
            return new StaticRotation.QuaternionBacked(Quaternion.between(new Vec3(0, 0, 1), del).times(Quaternion.axisAngle(new Vec3(0, 0, 1), roll)));
        }

        @Override
        public WorldlyRotationType type() {
            return WorldlyRotationType.ANCHORED;
        }
    }

    public static void main(String[] args) {
        Vec3 root = new Vec3(0, 0, -1);
        Vec3 r2 = new Vec3(0, 0.1, -1);
        Vec3 x = new Vec3(0.1, 0, -1).normalize(), y = new Vec3(-0.1, 0, -1).normalize();
        Quaternion a = Quaternion.between(root, x), b = Quaternion.between(root, y);
        System.out.print("[");
        for (double t = 0; t <= 1; t += 0.0625) {
            Quaternion c = Quaternion.slerp(a, b, t);
            Vec3 v = c.conjugate(r2);
            System.out.printf("(%.4f, %.4f, %.4f)", v.x(), v.y(), v.z());
            if (t != 1) System.out.print(", ");
        }
        System.out.println("]");
    }

    sealed interface Defer extends WorldlyRotation permits Track {
        WorldlyRotation getCurrentDelegate();
        @Override
        default StaticRotation apply(Level world, double partialTick) {
            return getCurrentDelegate().apply(world, partialTick);
        }
    }

    final class Track implements Defer {
        public static MapCodec<Track> mapCodec(Codec<WorldlyRotation> recursiveCodec) {
            return RecordCodecBuilder.mapCodec(inst ->
                    inst.group(
                            StaticRotation.CODEC.fieldOf("state").forGetter(it -> it.latestState),
                            recursiveCodec.fieldOf("target").forGetter(it -> it.target),
                            Angle.CODEC.fieldOf("max_rotation_per_tick").forGetter(it -> it.maxRotationPerTick),
                            Codec.INT.fieldOf("ticks_per_update").forGetter(it -> it.ticksPerUpdate)
                    ).apply(inst, Track::new)
            );
        }

        public final WorldlyRotation target;
        public StaticRotation latestState, oldState;
        private WorldlyRotation delegate;
        public final Angle maxRotationPerTick;
        public final int ticksPerUpdate;

        public Track(StaticRotation start, WorldlyRotation target, Angle maxRotationPerTick, int ticksPerUpdate) {
            this.target = target;
            latestState = oldState = start;
            delegate = new Static(start);
            this.maxRotationPerTick = maxRotationPerTick;
            this.ticksPerUpdate = ticksPerUpdate;
        }

        @Override
        public void tick(Level world) {
            long ct = world.getGameTime();
            if (ct % ticksPerUpdate == 0) {
                oldState = latestState;
                StaticRotation targetRotation = target.apply(world, 0);
                double radians = 2 * acos(targetRotation.asQuaternion().div(latestState.asQuaternion()).a());
                if (radians < 1e-4) {
                    latestState = targetRotation;
                    delegate = new Static(targetRotation);
                } else {
                    double t = (maxRotationPerTick).inRadians() * ticksPerUpdate / radians;
                    if (t < 1) System.out.println("Lerping btwn " + oldState + " and " + targetRotation);
                    latestState = StaticRotation.slerp(oldState, targetRotation, t);
                    delegate = new Lerped(new Static(oldState), new Static(latestState), world.getGameTime(), ticksPerUpdate);
                }
            }
        }

        @Override
        public WorldlyRotationType type() {
            return WorldlyRotationType.TRACK;
        }

        @Override
        public WorldlyRotation getCurrentDelegate() {
            return delegate;
        }
    }

    record Concat(List<WorldlyRotation> rotations) implements WorldlyRotation {
        public static MapCodec<Concat> mapCodec(Codec<WorldlyRotation> recursiveCodec) {
            return recursiveCodec.listOf().xmap(Concat::new, Concat::rotations).fieldOf("rotations");
        }

        @Override
        public StaticRotation apply(Level world, double partialTick) {
            Quaternion accumulator = Quaternion.ONE;
            for (var rot : rotations) accumulator = (rot.apply(world, partialTick).asQuaternion()).times(accumulator);
            return new StaticRotation.QuaternionBacked(accumulator);
        }

        @Override
        public WorldlyRotationType type() {
            return WorldlyRotationType.CONCAT;
        }
    }
}
