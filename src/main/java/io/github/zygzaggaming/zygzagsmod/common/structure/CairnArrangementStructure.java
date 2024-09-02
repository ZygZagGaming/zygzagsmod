package io.github.zygzaggaming.zygzagsmod.common.structure;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.MapCodec;
import io.github.zygzaggaming.zygzagsmod.common.Main;
import io.github.zygzaggaming.zygzagsmod.common.registry.StructurePieceTypeRegistry;
import io.github.zygzaggaming.zygzagsmod.common.registry.StructureTypeRegistry;
import io.github.zygzaggaming.zygzagsmod.common.util.GeneralUtil;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntArrayTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.material.Fluids;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Optional;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CairnArrangementStructure extends Structure {
    public static final MapCodec<CairnArrangementStructure> CODEC = simpleCodec(CairnArrangementStructure::new);

    public CairnArrangementStructure(StructureSettings settings) {
        super(settings);
    }

    @Override
    protected Optional<GenerationStub> findGenerationPoint(GenerationContext ctx) {
        var rng = ctx.random();
        int x = ctx.chunkPos().getBlockX(rng.nextIntBetweenInclusive(0, 15));
        int z = ctx.chunkPos().getBlockZ(rng.nextIntBetweenInclusive(0, 15));
        int y = ctx.chunkGenerator().getBaseHeight(
                x,
                z,
                Heightmap.Types.OCEAN_FLOOR_WG,
                ctx.heightAccessor(),
                ctx.randomState()
        );
        return onTopOfChunkCenter(ctx, Heightmap.Types.OCEAN_FLOOR_WG, (builder) -> {
            CairnType type = GeneralUtil.randomElement(rng, CairnType.values());
            switch (type) {
                case SINGLE -> builder.addPiece(mediumCairn(new BlockPos(x, y, z), rng));
                case SINGLE_SHORT -> builder.addPiece(shortCairn(new BlockPos(x, y, z), rng));
                case DOUBLE -> {
                    builder.addPiece(shortCairn(new BlockPos(x, y, z), rng));
                    int offsetX = rng.nextIntBetweenInclusive(2, 6);
                    int offsetZ = rng.nextIntBetweenInclusive(-6, 6);
                    if (rng.nextBoolean()) offsetX *= -1;
                    if (rng.nextBoolean()) {
                        int temp = offsetX;
                        offsetX = offsetZ;
                        offsetZ = temp;
                    }
                    builder.addPiece(tallCairn(new BlockPos(x + offsetX, y, z + offsetZ), rng));
                    //builder.addPiece(tallCompassCairn(new BlockPos(x, y, z), rng)); FIXME: make compass cairns work
                }
                case TRIPLE -> {
                    builder.addPiece(tallCairn(new BlockPos(x, y, z), rng));
                    int offsetX = rng.nextIntBetweenInclusive(2, 6);
                    int offsetZ = rng.nextIntBetweenInclusive(-6, 6);
                    if (rng.nextBoolean()) {
                        int temp = offsetX;
                        offsetX = offsetZ;
                        offsetZ = temp;
                    }
                    builder.addPiece(shortCairn(new BlockPos(x + offsetX, y, z + offsetZ), rng));
                    builder.addPiece(shortCairn(new BlockPos(x - offsetX, y, z - offsetZ), rng));
                }
            }
        });
    }

    @Override
    public StructureType<?> type() {
        return StructureTypeRegistry.CAIRN_ARRANGEMENT.get();
    }

    public enum CairnType {
        SINGLE,
        SINGLE_SHORT,
        DOUBLE,
        TRIPLE,
    }

    public static Cairn shortCairn(BlockPos origin, RandomSource rng) {
        return new Cairn(origin, rng.nextIntBetweenInclusive(1, 2), rng.nextIntBetweenInclusive(1, 2), rng.nextIntBetweenInclusive(0, 2));
    }

    public static Cairn mediumCairn(BlockPos origin, RandomSource rng) {
        return new Cairn(origin, rng.nextIntBetweenInclusive(2, 4), rng.nextIntBetweenInclusive(2, 3), rng.nextIntBetweenInclusive(2, 7));
    }

    public static Cairn tallCairn(BlockPos origin, RandomSource rng) {
        return new Cairn(origin, rng.nextIntBetweenInclusive(7, 11), rng.nextIntBetweenInclusive(3, 5), rng.nextIntBetweenInclusive(7, 9));
    }

    public static CompassCairn tallCompassCairn(BlockPos origin, RandomSource rng) {
        return new CompassCairn(origin, Main.CAIRN_BASIC, rng.nextDouble() * 2 + 2, rng.nextIntBetweenInclusive(7, 11), rng.nextIntBetweenInclusive(3, 5), rng.nextIntBetweenInclusive(7, 9));
    }

    public static class Cairn extends StructurePiece {
        public final BlockPos origin;
        public final int blocks, walls, moss;

        public Cairn(BlockPos origin, int blocks, int walls, int moss) {
            super(StructurePieceTypeRegistry.CAIRN.get(), 1, BoundingBox.fromCorners(origin, origin.above(walls)));
            this.origin = origin;
            this.blocks = blocks;
            this.walls = walls;
            this.moss = moss;

            setOrientation(null);
        }

        public Cairn(CompoundTag tag) {
            super(StructurePieceTypeRegistry.CAIRN.get(), tag);
            int[] originXYZ = tag.getIntArray("origin");
            this.origin = new BlockPos(originXYZ[0], originXYZ[1], originXYZ[2]);
            this.blocks = tag.getInt("blocks");
            this.walls = tag.getInt("walls");
            this.moss = tag.getInt("moss");

            setOrientation(null);
        }

        @Override
        protected void addAdditionalSaveData(StructurePieceSerializationContext ctx, CompoundTag tag) {
            tag.put("origin", new IntArrayTag(List.of(origin.getX(), origin.getY(), origin.getZ())));
            tag.putInt("blocks", blocks);
            tag.putInt("walls", walls);
            tag.putInt("moss", moss);
        }

        @Override
        public void postProcess(WorldGenLevel world, StructureManager manager, ChunkGenerator chunkGen, RandomSource rng, BoundingBox bounds, ChunkPos chunkPos, BlockPos blockPos) {
            BlockPos newOrigin = world.getHeightmapPos(Heightmap.Types.OCEAN_FLOOR_WG, origin);
            int y;
            for (y = 0; y < blocks; y++) placeBlock(world, y < moss ? Blocks.MOSSY_COBBLESTONE.defaultBlockState() : Blocks.COBBLESTONE.defaultBlockState(), newOrigin.getX(), newOrigin.getY() + y, newOrigin.getZ(), bounds);
            for (; y < blocks + walls; y++) {
                boolean waterlogged = world.getFluidState(new BlockPos(newOrigin.getX(), newOrigin.getY() + y, newOrigin.getZ())).is(Fluids.WATER);
                placeBlock(world, (y < moss ? Blocks.MOSSY_COBBLESTONE_WALL : Blocks.COBBLESTONE_WALL).defaultBlockState().setValue(BlockStateProperties.WATERLOGGED, waterlogged), newOrigin.getX(), newOrigin.getY() + y, newOrigin.getZ(), bounds);
            }
        }
    }

    public static class CompassCairn extends StructurePiece {
        public final BlockPos origin;
        public final TagKey<Structure> locatedTag;
        public final double distance;
        public final int blocks, walls, moss;

        public CompassCairn(BlockPos origin, TagKey<Structure> locatedTag, double distance, int blocks, int walls, int moss) {
            super(StructurePieceTypeRegistry.COMPASS_CAIRN.get(), 1, BoundingBox.fromCorners(origin.offset(-(int) distance - 1, 0, -(int) distance - 1), origin.offset((int) distance + 1, walls, (int) distance + 1)));
            this.origin = origin;
            this.locatedTag = locatedTag;
            this.distance = distance;
            this.blocks = blocks;
            this.walls = walls;
            this.moss = moss;

            setOrientation(null);
        }

        public CompassCairn(CompoundTag tag) {
            super(StructurePieceTypeRegistry.COMPASS_CAIRN.get(), tag);
            int[] originXYZ = tag.getIntArray("origin");
            this.origin = new BlockPos(originXYZ[0], originXYZ[1], originXYZ[2]);
            this.locatedTag = TagKey.create(Registries.STRUCTURE, ResourceLocation.parse(tag.getString("located_tag")));
            this.distance = tag.getDouble("distance");
            this.blocks = tag.getInt("blocks");
            this.walls = tag.getInt("walls");
            this.moss = tag.getInt("moss");

            setOrientation(null);
        }

        @Override
        protected void addAdditionalSaveData(StructurePieceSerializationContext ctx, CompoundTag tag) {
            tag.put("origin", new IntArrayTag(List.of(origin.getX(), origin.getY(), origin.getZ())));
            tag.putString("located_tag", locatedTag.location().toString());
            tag.putDouble("distance", distance);
            tag.putInt("blocks", blocks);
            tag.putInt("walls", walls);
            tag.putInt("moss", moss);
        }

        @Override
        public void postProcess(WorldGenLevel world, StructureManager manager, ChunkGenerator chunkGen, RandomSource rng, BoundingBox bounds, ChunkPos chunkPos, BlockPos blockPos) {
//            if (world.getChunkSource() instanceof ServerChunkCache cache) cache.mainThreadProcessor.
            world.registryAccess().registryOrThrow(Registries.STRUCTURE).getTag(Main.CAIRN_BASIC).ifPresent(tagSet -> {
                Pair<BlockPos, Holder<Structure>> pair = chunkGen.findNearestMapStructure(world.getLevel(), tagSet, origin, 5, false);
//                if (pair != null) {
////                    int x = pair.getFirst().getX() - origin.getX();
////                    int z = pair.getFirst().getZ() - origin.getZ();
////                    double fullDistance = Math.sqrt(x * x + z * z);
////                    int transformedX = (int) Math.round(x * distance / fullDistance);
////                    int transformedZ = (int) Math.round(z * distance / fullDistance);
////                    BlockPos newOrigin = world.getHeightmapPos(Heightmap.Types.OCEAN_FLOOR_WG, origin).offset(transformedX, 0, transformedZ);
////
////                    int y;
////                    for (y = 0; y < blocks; y++) placeBlock(world, y < mossHeight ? Blocks.MOSSY_COBBLESTONE.defaultBlockState() : Blocks.COBBLESTONE.defaultBlockState(), newOrigin.getX(), newOrigin.getY() + y, newOrigin.getZ(), bounds);
////                    for (; y < blocks + walls; y++) placeBlock(world, y < mossHeight ? Blocks.MOSSY_COBBLESTONE_WALL.defaultBlockState() : Blocks.COBBLESTONE_WALL.defaultBlockState(), newOrigin.getX(), newOrigin.getY() + y, newOrigin.getZ(), bounds);
//
//                }
            });
        }
    }
}