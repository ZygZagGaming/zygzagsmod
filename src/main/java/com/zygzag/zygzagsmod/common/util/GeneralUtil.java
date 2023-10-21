package com.zygzag.zygzagsmod.common.util;

import com.zygzag.zygzagsmod.common.Config;
import com.zygzag.zygzagsmod.common.Main;
import com.zygzag.zygzagsmod.common.entity.animation.AbstractAnimation;
import com.zygzag.zygzagsmod.common.entity.animation.TransitionAnimation;
import net.minecraft.core.BlockPos;
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
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Optional;
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

    public static <T extends ICapabilityProvider, C> void ifCapability(T t, Capability<C> capability, Consumer<C> function) {
        Optional<C> op = t.getCapability(capability).resolve();
        op.ifPresent(function);
    }

    @Nullable
    public static <T extends ICapabilityProvider, C, O> O ifCapabilityMap(T t, Capability<C> capability, Function<C, O> function) {
        Optional<C> op = t.getCapability(capability).resolve();
        return op.map(function).orElse(null);
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
        TransitionAnimation anim = Main.transitionAnimationRegistry().getValues().stream().filter((it) -> it.from().is(from) && it.to().is(to)).findFirst().orElse(null);
        //  if (anim == null) System.out.println("no transition exists from " + from + " to " + to);
        return anim;
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
        double da = (a1 - a0) % max;
        return (2 * da) % max - da;
    }

    public static double angleLerp(double a0, double a1, double t) {
        return a0 + shortAngleDist(a0, a1) * t;
    }

    public static float shortAngleDist(float a0, float a1) {
        float max = (float) Math.PI * 2;
        float da = (a1 - a0) % max;
        return 2 * da % max - da;
    }

    public static float angleLerp(float a0, float a1, float t) {
        return a0 + shortAngleDist(a0, a1) * t;
    }

    public static double mod(double a, double b) {
        if (a < 0) return b + (a % b);
        return a % b;
    }

    public static float mod(float a, float b) {
        if (a < 0) return b - ((-a) % b);
        return a % b;
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
}
