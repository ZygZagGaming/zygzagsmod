package io.github.zygzaggaming.zygzagsmod.common.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import io.github.zygzaggaming.zygzagsmod.common.Config;
import io.github.zygzaggaming.zygzagsmod.common.Main;
import io.github.zygzaggaming.zygzagsmod.common.entity.animation.Action;
import io.github.zygzaggaming.zygzagsmod.common.entity.animation.TransitionAction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.capabilities.ICapabilityProvider;
import net.neoforged.neoforge.common.Tags;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static java.lang.Math.*;

@SuppressWarnings("unused")
public class GeneralUtil {
    public static final Codec<UUID> UUID_CODEC = Codec.LONG.listOf(2, 2).comapFlatMap(list -> list.size() == 2 ? DataResult.success(new UUID(list.get(0), list.get(1))) : DataResult.error(() -> "incorrect size for a UUID"), uuid -> List.of(uuid.getMostSignificantBits(), uuid.getLeastSignificantBits()));

    public static int getColor(BlockState state) {
        if (state.is(Tags.Blocks.ORES_COPPER)) return Config.copperOreColor;
        else if (state.is(Tags.Blocks.ORES_COAL)) return Config.coalOreColor;
        else if (state.is(Tags.Blocks.ORES_IRON)) return Config.ironOreColor;
        else if (state.is(Tags.Blocks.ORES_GOLD)) return Config.goldOreColor;
        else if (state.is(Tags.Blocks.ORES_NETHERITE_SCRAP)) return Config.netheriteOreColor;
        else if (state.is(Tags.Blocks.ORES_DIAMOND)) return Config.diamondOreColor;
        else if (state.is(Tags.Blocks.ORES_QUARTZ)) return Config.quartzOreColor;
        else if (state.is(Tags.Blocks.ORES_LAPIS)) return Config.lapisOreColor;
        else if (state.is(Tags.Blocks.ORES_REDSTONE)) return Config.redstoneOreColor;
        else if (state.is(Tags.Blocks.ORES_EMERALD)) return Config.emeraldOreColor;

        return Config.miscOreColor;
    }

    public static int pow(int base, int exponent) {
        if (exponent < 0) throw new UnsupportedOperationException("Cannot raise integer to negative power " + exponent);
        if (exponent == 0) return 1;
        else return base * pow(base, exponent - 1);
    }

    public static <O, C, T, P extends ICapabilityProvider<O, C, T>> void ifCapability(P provider, O capability, C context, Consumer<T> function) {
        T capabilityInstance = provider.getCapability(capability, context);
        if (capabilityInstance != null) function.accept(capabilityInstance);
    }

    @Nullable
    public static <O, C, T, P extends ICapabilityProvider<O, C, T>, V> V ifCapabilityMap(P provider, O capability, C context, Function<T, V> function) {
        T capabilityInstance = provider.getCapability(capability, context);
        return capabilityInstance == null ? null : function.apply(capabilityInstance);
    }

    public static <O, C, T, P extends ICapabilityProvider<O, C, T>> void ifCapability(P provider, O capability, Consumer<T> function) {
        T capabilityInstance = provider.getCapability(capability, null);
        if (capabilityInstance != null) function.accept(capabilityInstance);
    }

    @Nullable
    public static <O, C, T, P extends ICapabilityProvider<O, C, T>, V> V ifCapabilityMap(P provider, O capability, Function<T, V> function) {
        T capabilityInstance = provider.getCapability(capability, null);
        return capabilityInstance == null ? null : function.apply(capabilityInstance);
    }

    public static void particles(Level world, ParticleOptions particleType, BlockPos pos, int number, double dx, double dy, double dz) {
        var rng = world.getRandom();
        for (int k = 0; k < number; k++) {
            var xp = rng.nextDouble();
            var yp = rng.nextDouble();
            var zp = rng.nextDouble();
            world.addParticle(
                    particleType,
                    pos.getX() + xp,
                    pos.getY() + yp,
                    pos.getZ() + zp,
                    dx, dy, dz
            );
        }
    }

    public static <T> T randomElement(List<T> list, RandomSource rng) {
        return list.get(rng.nextInt(list.size()));
    }

    public static <T> T randomElement(List<T> list) {
        return list.get((int) Math.floor(Math.random() * list.size()));
    }

    public static boolean isExposedToSunlight(BlockPos pos, BlockGetter world) {
        int max = world.getMaxBuildHeight();
        for (int y = 0; y < max - pos.getY(); y++) {
            if (!world.getBlockState(pos.offset(0, y, 0)).isAir()) return false;
        }
        return true;
    }

    public static <T> T randomElement(List<T> list, List<Integer> weights, RandomSource rng) {
        int totalWeight = 0;
        for (int k : weights) totalWeight += k;
        int randomWeight = rng.nextInt(totalWeight);
        for (int k = 0; k < Math.min(list.size(), weights.size()); k++) {
            T element = list.get(k);
            int weight = weights.get(k);
            randomWeight -= weight;
            if (randomWeight <= 0) return element;
        }
        return list.get(list.size() - 1);
    }

    @SafeVarargs
    public static <T> T randomElement(T... elements) {
        return elements[(int) (Math.random() * elements.length)];
    }

    @SafeVarargs
    public static <T> T randomElement(RandomSource rng, T... elements) {
        return elements[rng.nextInt(elements.length)];
    }

    public static @Nullable TransitionAction getTransitionAnimation(Action from, Action to) {
        return Main.transitionActionRegistry().stream().filter((it) -> it.from().is(from) && it.to().is(to)).findFirst().orElse(null);
    }

    public static String stringCS(Level level) {
        return level.isClientSide() ? "client" : "server";
    }

    public static float degreesToRadians(float degrees) {
        return (float) (degrees * Math.PI / 180);
    }

    public static float radiansToDegrees(float radians) {
        return (float) (radians * 180 / Math.PI);
    }

    public static double degreesToRadians(double degrees) {
        return degrees * Math.PI / 180;
    }

    public static double radiansToDegrees(double radians) {
        return radians * 180 / Math.PI;
    }

    public static float rotlerp(float a, float b, float changeBound) {
        float f = Mth.wrapDegrees(b - a);
        if (f > changeBound) {
            f = changeBound;
        }

        if (f < -changeBound) {
            f = -changeBound;
        }

        return a + f;
    }

    public static double shortAngleDist(double a0, double a1) {
        double max = Math.PI * 2;
        double da = mod(a1 - a0, max);
        return mod(2 * da, max) - da;
    }

    public static double angleLerp(double a0, double a1, double t) {
        return a0 + shortAngleDist(a0, a1) * t;
    }

    public static float shortAngleDist(float a0, float a1) {
        float max = (float) Math.PI * 2;
        float da = mod(a1 - a0, max);
        return mod(2 * da, max) - da;
    }

    public static float angleLerp(float a0, float a1, float t) {
        return a0 + shortAngleDist(a0, a1) * t;
    }

    public static double shortAngleDistDeg(double a0, double a1) {
        double max = 360.0;
        double da = mod(a1 - a0, max);
        return mod(2 * da, max) - da;
    }

    public static double angleLerpDeg(double a0, double a1, double t) {
        return a0 + shortAngleDistDeg(a0, a1) * t;
    }

    public static float shortAngleDistDeg(float a0, float a1) {
        float max = 360f;
        float da = mod(a1 - a0, max);
        return mod(2 * da, max) - da;
    }

    public static float angleLerpDeg(float a0, float a1, float t) {
        return a0 + shortAngleDistDeg(a0, a1) * t;
    }

    public static double mod(double a, double b) {
        return (a % b + b) % b;
    }

    public static float mod(float a, float b) {
        return (a % b + b) % b;
    }

    public static float formatAngle(float theta) {
        return mod(theta, (float) (2 * Math.PI));
    }

    public static float formatAngleCentered(float theta) {
        return mod(theta + (float) Math.PI, (float) (2 * Math.PI)) - (float) Math.PI;
    }

    public static double lerp(double a, double b, double t) {
        return a * (1 - t) + b * t;
    }

    public static float lerp(float a, float b, float t) {
        return a * (1 - t) + b * t;
    }

    public static int fireResistance(LivingEntity entity) {
        AtomicInteger value = new AtomicInteger(); // thanks intellij
        entity.level().registryAccess().lookup(Registries.ENCHANTMENT).ifPresent(lookup -> runIterationOnInventory((enchant, level) -> {
            if (enchant.getKey() == Enchantments.FIRE_PROTECTION) value.addAndGet(level);
        }, entity.getArmorSlots(), lookup));
        if (entity.hasEffect(MobEffects.FIRE_RESISTANCE)) value.addAndGet(6);
        return value.get();
    }

    public static void runIterationOnInventory(EnchantmentVisitor visitor, Iterable<ItemStack> inventory, HolderLookup.RegistryLookup<Enchantment> lookup) {
        for (var item : inventory) runIterationOnItem(visitor, item, lookup);
    }

    public static void runIterationOnItem(EnchantmentVisitor visitor, ItemStack stack, HolderLookup.RegistryLookup<Enchantment> lookup) {
        if (!stack.isEmpty()) for (var entry : stack.getAllEnchantments(lookup).entrySet()) {
            visitor.accept(entry.getKey(), entry.getIntValue());
        }
    }

    public static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }

    public static float clamp(float value, float min, float max) {
        return Math.max(min, Math.min(max, value));
    }

    @FunctionalInterface
    public interface EnchantmentVisitor {
        void accept(Holder<Enchantment> enchant, int level);
    }

    public static <K, V> void map(Map<K, V> map, K key, Function<V, V> function) {
        if (map.containsKey(key)) {
            map.put(key, function.apply(map.get(key)));
        }
    }

    public static BlockState mossify(BlockState original) {
        if (original.is(Blocks.COBBLESTONE)) return Blocks.MOSSY_COBBLESTONE.withPropertiesOf(original);
        if (original.is(Blocks.COBBLESTONE_WALL)) return Blocks.MOSSY_COBBLESTONE_WALL.withPropertiesOf(original);
        if (original.is(Blocks.COBBLESTONE_SLAB)) return Blocks.MOSSY_COBBLESTONE_SLAB.withPropertiesOf(original);
        if (original.is(Blocks.COBBLESTONE_STAIRS)) return Blocks.MOSSY_COBBLESTONE_STAIRS.withPropertiesOf(original);

        if (original.is(Blocks.STONE_BRICKS)) return Blocks.MOSSY_STONE_BRICKS.withPropertiesOf(original);
        if (original.is(Blocks.STONE_BRICK_WALL)) return Blocks.MOSSY_STONE_BRICK_WALL.withPropertiesOf(original);
        if (original.is(Blocks.STONE_BRICK_SLAB)) return Blocks.MOSSY_STONE_BRICK_SLAB.withPropertiesOf(original);
        if (original.is(Blocks.STONE_BRICK_STAIRS)) return Blocks.MOSSY_STONE_BRICK_STAIRS.withPropertiesOf(original);

        return original;
    }

    public static boolean rangeIntersectsOrTouches(double minA, double maxA, double minB, double maxB) {
        return minA <= maxB && minB <= maxA;
    }

    public static boolean rangeIntersects(double minA, double maxA, double minB, double maxB) {
        return minA < maxB && minB < maxA;
    }

    public static boolean intersects(VoxelShape a, VoxelShape b) {
        if (a.isEmpty() || b.isEmpty() || !a.bounds().intersects(b.bounds())) return false;
        final boolean[] returnValue = {false};
        a.forAllBoxes((minAX, minAY, minAZ, maxAX, maxAY, maxAZ) -> {
            if (!returnValue[0]) b.forAllBoxes((minBX, minBY, minBZ, maxBX, maxBY, maxBZ) -> {
                if (!returnValue[0] && rangeIntersects(minAX, maxAX, minBX, maxBX) && rangeIntersects(minAY, maxAY, minBY, maxBY) && rangeIntersects(minAZ, maxAZ, minBZ, maxBZ)) returnValue[0] = true;
            });
        });
        return returnValue[0];
    }

    public static boolean intersects(VoxelShape a, AABB b) {
        if (a.isEmpty() || !a.bounds().intersects(b)) return false;
        final boolean[] returnValue = {false};
        a.forAllBoxes((minAX, minAY, minAZ, maxAX, maxAY, maxAZ) -> {
            if (!returnValue[0] && rangeIntersects(minAX, maxAX, b.minX, b.maxX) && rangeIntersects(minAY, maxAY, b.minY, b.maxY) && rangeIntersects(minAZ, maxAZ, b.minZ, b.maxZ)) returnValue[0] = true;
        });
        return returnValue[0];
    }

    public static AABB rotated(AABB aabb, Direction.Axis axisOfRotation, int quarterTurns) {
        if (quarterTurns % 4 == 0) return aabb;
        if (quarterTurns >= 4) return rotated(aabb, axisOfRotation, quarterTurns % 4);
        if (quarterTurns < 0) return rotated(aabb, axisOfRotation, 4 - (-quarterTurns) % 4);
        return switch (axisOfRotation) {
            case X -> rotated(new AABB(aabb.minX, aabb.minZ, -aabb.maxY, aabb.maxX, aabb.maxZ, -aabb.minY), axisOfRotation, quarterTurns - 1);
            case Y -> rotated(new AABB(aabb.minZ, aabb.minY, -aabb.maxX, aabb.maxZ, aabb.maxY, -aabb.minX), axisOfRotation, quarterTurns - 1);
            case Z -> rotated(new AABB(aabb.minY, -aabb.maxX, aabb.minZ, aabb.maxY, -aabb.minX, aabb.maxZ), axisOfRotation, quarterTurns - 1);
        };
    }

    public static AABB rotated(AABB aabb, Direction.Axis axisOfRotation, int quarterTurns, Vec3 originOfRotation) {
        return rotated(aabb.move(originOfRotation.scale(-1)), axisOfRotation, quarterTurns % 4).move(originOfRotation);
    }

    public static double angleDistance(double a, double b) {
        return Math.PI - Math.abs(Math.abs(mod(a, 2 * Math.PI) - mod(b, 2 * Math.PI)) - Math.PI);
    }

    public static float angleDistance(float a, float b) {
        return (float) Math.PI - Math.abs(Math.abs(mod(a, (float) (2 * Math.PI)) - mod(b, (float) (2 * Math.PI))) - (float) Math.PI);
    }

    public static double moveAngleAmountTowards(double a, double b, double c) {
        return angleLerp(a, b, Math.min(1, c / angleDistance(a, b)));
    }

    public static float moveAngleAmountTowards(float a, float b, float c) {
        return angleLerp(a, b, Math.min(1, c / angleDistance(a, b)));
    }

    public static double[] lerpSpherical(double phi0, double theta0, double phi1, double theta1, double t) {
        // Convert spherical to rectangular on unit sphere
        double x0 = cos(theta0) * cos(phi0), y0 = sin(theta0) * cos(phi0), z0 = sin(phi0);
        double x1 = cos(theta1) * cos(phi1), y1 = sin(theta1) * cos(phi1), z1 = sin(phi1);

        // Take the cross product to get a normal vector to the geodesic
        double x2 = y0 * z1 - z0 * y1, y2 = x0 * z1 - z0 * x1, z2 = x0 * y1 - y0 * x1;

        // Normalize
        double factor = 1 / magnitude(x2, y2, z2);
        x2 *= factor; y2 *= factor; z2 *= factor;

        // Take the cross product and normalize again to get an orthonormal basis
        double x3 = y0 * z2 - z0 * y2, y3 = x0 * z2 - z0 * x2, z3 = x0 * y2 - y0 * x2;
        factor = 1 / magnitude(x3, y3,z3);
        x3 *= factor; y3 *= factor; z3 *= factor;

        // The geodesic can now be parametrized as r(t) = (x0, y0, z0)cos(t) + (x3, y3, z3)sin(t), r(0) = (x0, y0, z0)
        // To find the value of t for which r(t) = (x1, y1, z1), we take the dot product of (x1, y1, z1) with (x0, y0, z0) and (x3, y3, z3)
        double dot01 = x0 * x1 + y0 * y1 + z0 * z1;
        double dot13 = x1 * x3 + y1 * y3 + z1 * z3;

        // Now, the atan2 of dot01 and dot03 is the value of t for which r(t) = (x1, y1, z1)
        double t1 = atan2(dot13, dot01);

        // We transform the input value of t so that it can be used in the aforementioned function
        double finalT = angleLerp(0, t1, t);

        // We use that value of T to find a rectangular representation of the sought-after vector
        double cos = cos(finalT), sin = sin(finalT), x4 = x0 * cos + x3 * sin, y4 = y0 * cos + y3 * sin, z4 = z0 * cos + z3 * sin;

        // Finally, we reduce the rectangular form to spherical
        double outPhi = acos(z4);
        double outTheta = atan2(y4, x4);
        return new double[]{outPhi, outTheta};
    }

    public static double magnitude(double x, double y, double z) {
        return sqrt(x * x + y * y + z * z);
    }

    public static double angleDifferenceSpherical(double phi1, double theta1, double phi2, double theta2) {
        return acos(cos(theta1 - theta2) * cos(phi1) * cos(phi2) + sin(phi1) * sin(phi2));
    }

    public static double[] moveAngleAmountTowards(double phi1, double theta1, double phi2, double theta2, double c) {
        return lerpSpherical(phi1, theta1, phi2, theta2, Math.min(1, c / angleDifferenceSpherical(phi1, theta1, phi2, theta2)));
    }

    public static float[] lerpSpherical(float phi0, float theta0, float phi1, float theta1, float t) {
        // Convert spherical to rectangular on unit sphere
        float[] v0 = sphericalToRectangular(1, phi0, theta0);
        float[] v1 = sphericalToRectangular(1, phi1, theta1);

        // Take the cross product to get a normal vector to the geodesic
        float[] v2 = cross(v0, v1);

        // Normalize
        v2 = normalized(v2);

        // Take the cross product and normalize again to get an orthonormal basis v0, v2, v3
        float[] v3 = normalized(cross(v0, v2));

        // The geodesic can now be parametrized as r(t) = v0cos(t) + v3sin(t), r(0) = v0
        // To find the value of t for which r(t) = v1, we take the dot product of v1 with v0 and v3
        float dot01 = dot(v0, v1);
        float dot13 = dot(v1, v3);

        // Now, the atan2 of dot01 and dot03 is the value of t for which r(t) = v1
        float t1 = (float) atan2(dot13, dot01);

        // We transform the input value of t so that it can be used in the aforementioned function
        float finalT = angleLerp(0, t1, t);

        // We use that value of T to find a rectangular representation of the sought-after vector
        float[] v4 = add(scale((float) cos(finalT), v0), scale((float) sin(finalT), v3));

        // Finally, we reduce the rectangular form to spherical
        float[] out = rectangularToSpherical(v4);
        return new float[]{out[1], out[2]};
    }

    public static float[] sphericalToRectangular(float[] rpt) {
        float rho = rpt[0], phi = rpt[1], theta = rpt[2];
        return new float[]{(float) (rho * cos(theta) * cos(phi)), (float) (rho * sin(theta) * cos(phi)), (float) (rho * sin(phi))};
    }

    public static float[] rectangularToSpherical(float[] xyz) {
        float x = xyz[0], y = xyz[1], z = xyz[2], rho = magnitude(xyz);
        return new float[]{rho, (float) asin(z / rho), (float) atan2(y, x)};
    }

    public static float[] rectangularToSphericalAngles(float[] xyz) {
        float x = xyz[0], y = xyz[1], z = xyz[2], rho = magnitude(xyz);
        return new float[]{(float) asin(z / rho), (float) atan2(y, x)};
    }

    public static float[] rectangularToXYRot(Vec3 xyz) {
        float x = (float) xyz.x, y = (float) xyz.y, z = (float) xyz.z, r = (float) Math.sqrt(x * x + z * z);
        if (Math.abs(atan2(y, r)) > Math.PI * 0.5) System.out.println("PROBLEM: xRot " + atan2(y, r) + " is not in in [-pi/2,pi/2] for input " + xyz);
        return new float[]{(float) atan2(y, r), (float) atan2(x, z)};
    }

    public static double[] rectangularToXYRotD(Vec3 xyz) {
        double x = xyz.x, y = xyz.y, z = xyz.z, r = Math.sqrt(x * x + z * z);
        if (Math.abs(atan2(y, r)) > Math.PI * 0.5) System.out.println("PROBLEM: xRot " + atan2(y, r) + " is not in in [-pi/2,pi/2] for input " + xyz);
        return new double[]{atan2(y, r), atan2(x, z)};
    }

    public static Vec3 xYRotToRectangular(float[] xy) {
        return new Vec3(sin(xy[0]), 0, cos(xy[0])).scale(cos(xy[1])).add(0, sin(xy[1]), 0);
    }

    public static Vec3 xYRotToRectangular(double[] xy) {
        return new Vec3(sin(xy[0]), 0, cos(xy[0])).scale(cos(xy[1])).add(0, sin(xy[1]), 0);
    }

    public static float[] sphericalToRectangular(float rho, float phi, float theta) {
        return new float[]{(float) (rho * cos(theta) * cos(phi)), (float) (rho * sin(theta) * cos(phi)), (float) (rho * sin(phi))};
    }

    public static double[] sphericalToRectangular(double rho, double phi, double theta) {
        return new double[]{rho * cos(theta) * cos(phi), rho * sin(theta) * cos(phi), rho * sin(phi)};
    }

    public static float[] rectangularToSpherical(float x, float y, float z) {
        float rho = magnitude(x, y, z);
        return new float[]{rho, (float) asin(z / rho), (float) atan2(y, x)};
    }

    public static double[] rectangularToSpherical(double x, double y, double z) {
        double rho = magnitude(x, y, z);
        return new double[]{rho, asin(z / rho), atan2(y, x)};
    }

    public static float magnitude(float x, float y, float z) {
        return (float) sqrt(x * x + y * y + z * z);
    }

    public static float angleDifferenceSpherical(float phi1, float theta1, float phi2, float theta2) {
        return (float) acos(cos(theta1 - theta2) * cos(phi1) * cos(phi2) + sin(phi1) * sin(phi2));
    }

    public static float[] moveAngleAmountTowards(float phi1, float theta1, float phi2, float theta2, float c) {
        float diff = angleDifferenceSpherical(phi1, theta1, phi2, theta2);
        return diff <= 1e-4 ? new float[]{phi1, theta1} : lerpSpherical(phi1, theta1, phi2, theta2, Math.min(1, c / diff));
    }

    public static float[] moveAngleAmountTowards(Rotation rot0, Rotation rot1, float c) {
        float diff = rot0.angleDifference(rot1);
        return diff <= 1e-4 ? new float[]{rot0.getXRot(), rot0.getYRot()} : lerpRotations(rot0, rot1, Math.min(1, c / diff));
    }

    public static float magnitude(float[] values) {
        float total = 0;
        for (float component : values) total += component * component;
        return (float) sqrt(total);
    }

    public static float[] scale(float scalar, float[] values) {
        float[] fin = new float[values.length];
        for (int i = 0; i < values.length; i++) fin[i] = scalar * values[i];
        return fin;
    }

    public static float[] add(float[] a, float[] b) {
        float[] c = new float[min(a.length, b.length)];
        for (int i = 0; i < c.length; i++) c[i] = a[i] + b[i];
        return c;
    }

    public static float[] subtract(float[] a, float[] b) {
        float[] c = new float[min(a.length, b.length)];
        for (int i = 0; i < c.length; i++) c[i] = a[i] - b[i];
        return c;
    }

    public static float dot(float[] a, float[] b) {
        float total = 0;
        for (int i = 0; i < min(a.length, b.length); i++) total += a[i] * b[i];
        return total;
    }

    public static float[] cross(float[] a, float[] b) {
        return new float[]{a[1] * b[2] - a[2] * b[1], a[2] * b[0] - a[0] * b[2], a[0] * b[1] - a[1] * b[0]};
    }

    public static float[] normalized(float[] values) {
        return scale(1 / magnitude(values), values);
    }

    public static void main(String[] args) { // testing
        float pi = (float) Math.PI;
        int numCases = 250;
        for (int whichCase = 0; whichCase < numCases; whichCase++) {
            float xRot1, yRot1, xRot2, yRot2;
            Rotation rot1, rot2;
            { // random rotations 1
                xRot1 = pi * (float) Math.random() - 0.5f * pi;
                yRot1 = 2 * pi * (float) Math.random() - pi;
                rot1 = new SimpleRotation(xRot1, yRot1);
            }
            { // random rotations 2
                xRot2 = pi * (float) Math.random() - 0.5f * pi;
                yRot2 = 2 * pi * (float) Math.random() - pi;
                rot2 = new SimpleRotation(xRot2, yRot2);
            }
            { // testing conversion
                float[] xyRot = rectangularToXYRot(rot1.directionVector());
                float diff = (xRot1 - xyRot[0]) * (xRot1 - xyRot[0]) + (yRot1 - xyRot[1]) * (yRot1 - xyRot[1]);
                if (diff > 1e-3) System.out.printf("Problem: xy-rot (%.2f, %.2f) converted to and from rectangular yields different xy-rot (%.2f, %.2f)%n", xRot1, yRot1, xyRot[0], xyRot[1]);
            }
            { // testing moving rot
                float angleDifference = (float) Math.acos(rot1.directionVector().dot(rot2.directionVector()));
                // 4 cases, 0, 1/2 difference, exactly difference, 3/2 difference
                float[] cases = { 0, 0.5f, 1, 1.5f };
                for (float multiplier : cases) {
                    float angleDifferenceToMove = angleDifference * multiplier;
                    Rotation result = new SimpleRotation(moveAngleAmountTowards(rot1, rot2, angleDifferenceToMove));
                    float actualDifference = result.angleDifference(rot1);
                    float remainingDifference = result.angleDifference(rot2);
                    if (distinct(min(angleDifferenceToMove, angleDifference), actualDifference, 1e-3f)) System.out.printf("Problem: %s moved %.4f to %s is %s, %.4f away from original%n", rot1, angleDifferenceToMove, rot2, result, actualDifference);
                    if (distinct(max(angleDifference - angleDifferenceToMove, 0), remainingDifference, 1e-3f)) System.out.printf("Problem: %s moved %.4f to %s is %s, %.4f away from destination%n", rot1, angleDifferenceToMove, rot2, result, remainingDifference);
                }
            }
        }
    }

    public static boolean distinct(float f1, float f2, float error) {
        return abs(f1 - f2) > error;
    }

//    public static float[][] rotationMatrix(Direction.Axis axis, float theta) {
//        float cos = (float) cos(theta), sin = (float) sin(theta);
//        return switch (axis) {
//            case X -> new float[][]{{1, 0, 0}, {0, cos, -sin}, {0, sin, cos}};
//            case Y -> new float[][]{{cos, 0, sin}, {0, 1, 0}, {-sin, 0, cos}};
//            case Z -> new float[][]{{cos, -sin, 0}, {sin, cos, 0}, {0, 0, 1}};
//        };
//    }
//
//    public static boolean pointInCuboid(
//            float[] center,
//            float[] scale,
//            float[] rotations,
//            float[] point
//    ) {
//        float x = point[0] - center[0], y = point[1] - center[1], z = point[2] - center[2];
//        float r = (float) sqrt(x * x + z * z);
//        float theta = (float) atan2(z, x) - rotations[1];
//        x = r * (float) cos(theta);
//        z = r * (float) sin(theta);
//        r = (float) sqrt(y * y + z * z);
//        theta = (float) atan2(z, y) - rotations[0];
//        y = r * (float) cos(theta);
//        z = r * (float) sin(theta);
//        return abs(x) < scale[0] * 0.5f && abs(y) < scale[1] * 0.5f && abs(z) < scale[2] * 0.5f;
//    }
//
//    public static boolean collideCuboids(
//            float[] center1,
//            float[] scale1,
//            float[] rotations1,
//            float[] center2,
//            float[] scale2,
//            float[] rotations2
//    ) {
//        // bounding radii
//        float r1 = magnitude(scale1) * 0.5f, r2 = magnitude(scale2) * 0.5f;
//        if (magnitude(subtract(center1, center2)) > r1 + r2) return false;
//        // if either center is contained in the other
//        if (pointInCuboid(center1, scale1, rotations1, center2) || pointInCuboid(center2, scale2, rotations2, center1)) return true;
//        float[][] planeNormals = {
//                {1, 0, 0},
//                {0, 1, 0},
//                {0, 0, 1}
//        };
//        for (float[] normal : planeNormals) {
//
//        }
//    }

    public static float[] lerpRotations(Rotation rot0, Rotation rot1, float t) {
        // Convert spherical to rectangular on unit sphere
        float[] v0 = rot0.direction();
        float[] v1 = rot1.direction();

        double angle = Math.acos(dot(v0, v1));
        if (Math.abs(angle) < 1e-2 || Double.isNaN(angle)) return new float[]{rot0.getXRot(), rot0.getYRot()}; // if they're really close we dont have to do anything

        // Take the cross product to get a normal vector to the geodesic
        float[] v2 = cross(v0, v1);
        //System.out.println(Arrays.toString(v0) + " cross " + Arrays.toString(v1) + " = " + Arrays.toString(v2));

        // Normalize
        v2 = normalized(v2);

        // Take the cross product and normalize again to get an orthonormal basis v0, v2, v3
        float[] v3 = normalized(cross(v0, v2));

        // The geodesic can now be parametrized as r(t) = v0cos(t) + v3sin(t), r(0) = v0
        // To find the value of t for which r(t) = v1, we take the dot product of v1 with v0 and v3
        float dot01 = dot(v0, v1);
        float dot13 = dot(v1, v3);

        // Now, the atan2 of dot01 and dot03 is the value of t for which r(t) = v1
        float t1 = (float) atan2(dot13, dot01);

        // We transform the input value of t so that it can be used in the aforementioned function
        float finalT = angleLerp(0, t1, t);

        // We use that value of T to find a rectangular representation of the sought-after vector
        float[] v4 = add(scale((float) cos(finalT), v0), scale((float) sin(finalT), v3));

        // Finally, we reduce the rectangular form to spherical
        var k = rectangularToXYRot(new Vec3(v4[0], v4[1], v4[2]));
        if (Float.isNaN(k[0]) || Float.isNaN(k[1])) throw new RuntimeException("Lerping between rotations (%f, %f, %f) and (%f, %f, %f) with t=%.4f yields problematic rot [%.4f, %.4f]".formatted(v0[0], v0[1], v0[2], v1[0], v1[1], v1[2], t, k[0], k[1]));
        return k;
    }

    public static <A, B> Codec<B> mapDefinedCodec(Codec<A> aCodec, Map<A, B> map) {
        Map<B, A> reverseMap = map.entrySet().stream().collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));
        return aCodec.flatXmap(
                a -> Optional.ofNullable(map.get(a)).map(DataResult::success).orElse(DataResult.error(() -> "Not a possible choice for field")),
                b -> Optional.ofNullable(reverseMap.get(b)).map(DataResult::success).orElse(DataResult.error(() -> "Not a possible choice for field"))
        );
    }

    public static <E extends Enum<E>> Codec<E> enumCodec(Class<E> clazz) {
        return Codec.STRING.comapFlatMap(name -> optionalToDataResult(catchOptional(() -> Enum.valueOf(clazz, name))), Enum::name);
    }

    public static <T> Optional<T> catchOptional(Supplier<T> supplier) {
        try {
            return Optional.of(supplier.get());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public static <T> DataResult<T> optionalToDataResult(Optional<T> optional) {
        return optional.map(DataResult::success).orElse(DataResult.error(() -> ""));
    }

    public static <T> DataResult<T> optionalToDataResult(Optional<T> optional, Supplier<String> msg) {
        return optional.map(DataResult::success).orElse(DataResult.error(msg));
    }

    public static double sqr(double d) {
        return d * d;
    }

    public static Angle[] quaternionToXYZTaitBryanAngles(Quaternion quaternion) {
        // algorithm used was found at https://www.ncbi.nlm.nih.gov/pmc/articles/PMC9648712/

        // e = z, e' = y, e'' = x, eps = -1
        // a = q.a, b = q.d, c = q.c, d = -q.b
        double a = quaternion.a(), b = quaternion.d(), c = quaternion.c(), d = -quaternion.b();

        // a' = a - c, b' = b + d, c' = c + a, d' = d - b
        double aP = a - c, bP = b + d, cP = c + a, dP = d - b;

        // theta2 = acos(a'^2 + b'^2 - 1) - pi/2
        Angle theta2 = Angle.acos(sqr(aP) + sqr(bP) - 1).minus(Angle.RIGHT);
        if (abs((theta2.inRadians() - PI) % (2 * PI) + PI) < 1e-5) return new Angle[]{Angle.ZERO, theta2, Angle.ZERO};

        // theta1 = atan2(b', a') - atan2(d', c')
        Angle theta1 = Angle.atan2(bP, aP).minus(Angle.atan2(dP, cP));

        // theta3 = eps * (atan2(b', a') + atan2(d', c'))
        Angle theta3 = Angle.atan2(bP, aP).plus(Angle.atan2(dP, cP)).inverse();

        return new Angle[]{theta1, theta2, theta3};
    }
}