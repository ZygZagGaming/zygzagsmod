package com.zygzag.zygzagsmod.common.util;

import com.zygzag.zygzagsmod.common.Config;
import com.zygzag.zygzagsmod.common.Main;
import com.zygzag.zygzagsmod.common.entity.animation.AbstractAnimation;
import com.zygzag.zygzagsmod.common.entity.animation.TransitionAnimation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
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
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Function;

public class GeneralUtil {
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

    public static @Nullable TransitionAnimation getTransitionAnimation(AbstractAnimation from, AbstractAnimation to) {
        return Main.transitionAnimationRegistry().stream().filter((it) -> it.from().is(from) && it.to().is(to)).findFirst().orElse(null);
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
        if (a < 0) return b - ((-a) % b);
        return a % b;
    }

    public static float mod(float a, float b) {
        if (a < 0) return b - ((-a) % b);
        return a % b;
    }

    public static double lerp(double a, double b, double t) {
        return a * (1 - t) + b * t;
    }

    public static float lerp(float a, float b, float t) {
        return a * (1 - t) + b * t;
    }

    public static int fireResistance(LivingEntity entity) {
        AtomicInteger value = new AtomicInteger(); // thanks intellij
        runIterationOnInventory((enchant, level) -> {
            if (enchant == Enchantments.FIRE_PROTECTION) value.addAndGet(level);
        }, entity.getArmorSlots());
        if (entity.hasEffect(MobEffects.FIRE_RESISTANCE)) value.addAndGet(6);
        return value.get();
    }

    public static void runIterationOnInventory(EnchantmentVisitor visitor, Iterable<ItemStack> inventory) {
        for (var item : inventory) runIterationOnItem(visitor, item);
    }

    public static void runIterationOnItem(EnchantmentVisitor visitor, ItemStack stack) {
        if (!stack.isEmpty()) for (Map.Entry<Enchantment, Integer> entry : stack.getAllEnchantments().entrySet()) {
            visitor.accept(entry.getKey(), entry.getValue());
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
        void accept(Enchantment enchant, int level);
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
}
