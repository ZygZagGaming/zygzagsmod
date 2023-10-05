package com.zygzag.zygzagsmod.common.util;

import com.zygzag.zygzagsmod.common.Config;
import com.zygzag.zygzagsmod.common.Main;
import com.zygzag.zygzagsmod.common.entity.animation.AbstractAnimation;
import com.zygzag.zygzagsmod.common.entity.animation.TransitionAnimation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
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
}
