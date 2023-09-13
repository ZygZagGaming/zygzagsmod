package com.zygzag.zygzagsmod.common.feature;

import com.mojang.serialization.Codec;
import com.zygzag.zygzagsmod.common.block.EndStoneSwitchBlock;
import com.zygzag.zygzagsmod.common.registry.BlockItemEntityRegistry;
import com.zygzag.zygzagsmod.common.registry.BlockWithItemRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.zygzag.zygzagsmod.common.util.GeneralUtil.randomElement;

public class IridiumEndIslandFeature extends Feature<NoneFeatureConfiguration> {
    public IridiumEndIslandFeature(Codec<NoneFeatureConfiguration> pCodec) {
        super(pCodec);
    }

    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel world = context.level();
        RandomSource rng = context.random();
        BlockPos pos = context.origin();
        int size = rng.nextInt(6, 8);
        Map<BlockPos, BlockState> structure = new HashMap<>();

        var pillarHeight = 6;
        for (int k = 0; k < pillarHeight; k++) {
            structure.put(pos.offset(0, k, 0), Blocks.END_STONE_BRICKS.defaultBlockState());
        }
        for (int x = (int) (-size * 1.414); x < size * 1.414; x++) for (int z = (int) (-size * 1.414); z < size * 1.414; z++) {
            if (x * x + z * z <= size * size * 1.3) {
                structure.put(pos.offset(x, pillarHeight, z), Blocks.END_STONE_BRICKS.defaultBlockState());
            } else if (x * x + z * z <= size * size * 1.7) {
                var random = rng.nextDouble();
                var state = random < .5 ? Blocks.END_STONE_BRICKS.defaultBlockState() :
                        random < .85 ? Blocks.AIR.defaultBlockState() :
                                Blocks.END_STONE.defaultBlockState();
                structure.put(pos.offset(x, pillarHeight, z), state);
            }
        }
        for (int x = -size; x < size; x++) for (int z = -size; z < size; z++) {
            if (x * x + z * z <= size * size * 0.3) {
                structure.put(pos.offset(x, pillarHeight - 1, z), Blocks.END_STONE_BRICKS.defaultBlockState());
            } else if (x * x + z * z <= size * size * 0.85) {
                var random = rng.nextDouble();
                var state = random < .5 ? Blocks.END_STONE_BRICKS.defaultBlockState() :
                        random < .85 ? Blocks.AIR.defaultBlockState() :
                                Blocks.END_STONE.defaultBlockState();
                structure.put(pos.offset(x, pillarHeight - 1, z), state);
            }
        }
        float radius = (float) size - 1.5f;

        int layer;
        for (layer = 0; radius > 0.5F; ++layer) {
            for (int x = Mth.floor(-radius); x <= Mth.ceil(radius); ++x) {
                for (int z = Mth.floor(-radius); z <= Mth.ceil(radius); ++z) {
                    if ((float) (x * x + z * z) <= (radius + 1f) * (radius + 1f)) {
                        var state = BlockWithItemRegistry.END_SAND.getDefaultBlockState();
                        var random = rng.nextDouble();
                        if (random < 0.03) state = BlockItemEntityRegistry.SUSPICIOUS_END_SAND.getDefaultBlockState();
                        if (random > 0.95) state = BlockWithItemRegistry.IRIDIUM_END_SAND.getDefaultBlockState();
                        structure.put(pos.offset(x, layer + pillarHeight + 1, z), state);
                    }
                }
            }

            radius -= (float) (rng.nextDouble() * layer + 1) * 0.33;
        }
        var pillarPos = 2;
        var placedSwitch = false;
        var horizontalDirections = new Direction[]{
                Direction.NORTH,
                Direction.SOUTH,
                Direction.EAST,
                Direction.WEST
        };
        var pillarsHeight = 9;
        for (double x = -0.5; x <= 0.5; x++) for (double z = -0.5; z <= 0.5; z++) {
            for (int height = 1; height < pillarsHeight; height++) {
                var p = pos.offset((int) (x * 2 * pillarPos), height + pillarHeight, (int) (z * 2 * pillarPos));
                var k = structure.put(p, Blocks.END_STONE_BRICKS.defaultBlockState());
                if (k == null && !placedSwitch) {
                    structure.put(p,
                            BlockWithItemRegistry.END_STONE_SWITCH.getDefaultBlockState()
                                    .setValue(EndStoneSwitchBlock.FACING, Direction.WEST)
                    );
                    structure.put(p.relative(Direction.EAST),
                            BlockWithItemRegistry.END_SAND.getDefaultBlockState()
                    );
                    structure.put(p.relative(Direction.EAST).offset(0, -1, 0),
                            BlockWithItemRegistry.END_SAND.getDefaultBlockState()
                    );
                    structure.put(p.relative(Direction.SOUTH),
                            BlockWithItemRegistry.END_SAND.getDefaultBlockState()
                    );
                    structure.put(p.relative(Direction.SOUTH).offset(0, -1, 0),
                            BlockWithItemRegistry.END_SAND.getDefaultBlockState()
                    );
                    placedSwitch = true;
                }
            }
        }
        List<Direction> directions = List.of(Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST);
        for (Direction direction : directions) {
            int branchLength = rng.nextInt(6) + 2;
            BlockPos currentPos = pos.offset(0, pillarHeight + 1, 0);
            var directionsToGo = List.of(
                    direction,
                    direction,
                    Direction.UP,
                    Direction.UP,
                    direction.getClockWise(Direction.Axis.Y),
                    direction.getCounterClockWise(Direction.Axis.Y)
            );
            for (int block = 0; block < branchLength; block++) {
                structure.put(currentPos, BlockWithItemRegistry.RAW_IRIDIUM_BLOCK.getDefaultBlockState());
                Direction directionToGo = randomElement(directionsToGo, rng);
                setBlock(world, currentPos.relative(randomElement(directionsToGo, rng)), BlockWithItemRegistry.IRIDIUM_END_SAND.getDefaultBlockState());
                currentPos = currentPos.relative(directionToGo);
            }
        }


        for (BlockPos position : structure.keySet()) {
            BlockState state = structure.get(position);
            setBlock(world, position, state);
            if (state.is(BlockItemEntityRegistry.SUSPICIOUS_END_SAND.getBlock())) {
                world.getBlockEntity(position, BlockItemEntityRegistry.SUSPICIOUS_END_SAND.getBlockEntityType()).ifPresent((it) ->
                        it.setLootTable(new ResourceLocation("zygzagsmod:archaeology/suspicious_end_sand"), rng.nextLong())
                );
            }
        }

        return true;
    }
}
