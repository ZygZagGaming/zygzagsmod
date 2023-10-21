package com.zygzag.zygzagsmod.common.structure;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.zygzag.zygzagsmod.common.Main;
import com.zygzag.zygzagsmod.common.block.entity.StructurePlacerBlockEntity;
import com.zygzag.zygzagsmod.common.registry.BlockRegistry;
import com.zygzag.zygzagsmod.common.registry.StructurePieceTypeRegistry;
import com.zygzag.zygzagsmod.common.registry.StructureTypeRegistry;
import com.zygzag.zygzagsmod.common.util.GeneralUtil;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.chunk.ChunkGeneratorStructureState;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.*;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.placement.ConcentricRingsStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;

import static com.zygzag.zygzagsmod.common.Main.MODID;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CairnArrangementStructure extends Structure {
    public static final Codec<CairnArrangementStructure> CODEC = simpleCodec(CairnArrangementStructure::new);

    public CairnArrangementStructure(StructureSettings settings) {
        super(settings);
    }

    @Override
    protected Optional<GenerationStub> findGenerationPoint(GenerationContext ctx) {
        int x = ctx.chunkPos().getBlockX(11);
        int z = ctx.chunkPos().getBlockZ(11);
        int y = ctx.chunkGenerator().getBaseHeight(
                x,
                z,
                Heightmap.Types.WORLD_SURFACE_WG,
                ctx.heightAccessor(),
                ctx.randomState()
        );
        return onTopOfChunkCenter(ctx, Heightmap.Types.WORLD_SURFACE_WG, (builder) -> {
            builder.addPiece(
                    new CairnArrangementStructurePiece(
                            new BoundingBox(
                                    x,
                                    y,
                                    z,
                                    x,
                                    y,
                                    z
                            ),
                            ctx.random().nextBoolean() ? CairnType.SINGLE : CairnType.DOUBLE
                    )
            );
        });
    }

    @Override
    public StructureType<?> type() {
        return StructureTypeRegistry.CAIRN_ARRANGEMENT.get();
    }

    public enum CairnType {
        SINGLE,
        DOUBLE;
    }

    public static class CairnArrangementStructurePiece extends StructurePiece {
        private final CairnType type;

        protected CairnArrangementStructurePiece(BoundingBox bounds, CairnType type) {
            super(StructurePieceTypeRegistry.CAIRN_ARRANGEMENT.get(), 0, bounds);
            this.type = type;
        }

        public CairnArrangementStructurePiece(CompoundTag tag) {
            super(StructurePieceTypeRegistry.CAIRN_ARRANGEMENT.get(), tag);
            var t = tag.get("cairn_type");
            if (t != null) this.type = CairnType.valueOf(t.getAsString().toUpperCase());
            else this.type = CairnType.SINGLE;
        }

        @Override
        protected void addAdditionalSaveData(StructurePieceSerializationContext ctx, CompoundTag tag) {
            tag.putString("cairn_type", type.name().toLowerCase());
        }

        @Override
        public void postProcess(WorldGenLevel world, StructureManager manager, ChunkGenerator chunkGen, RandomSource rng, BoundingBox bounds, ChunkPos chunk, BlockPos origin) {
            if (world instanceof ServerLevel serverLevel) place(serverLevel, manager, chunkGen, rng, bounds, chunk, origin);
            else {
                world.setBlock(origin, BlockRegistry.STRUCTURE_PLACER.get().defaultBlockState(), 3);
                if (world.getBlockEntity(origin) instanceof StructurePlacerBlockEntity be) // should always be
                    be.structure = world.registryAccess().registryOrThrow(Registries.STRUCTURE).get(new ResourceLocation(MODID, "cairn_arrangement"));
            }
        }

        public void place(ServerLevel world, StructureManager manager, ChunkGenerator chunkGen, RandomSource rng, BoundingBox bounds, ChunkPos chunk, BlockPos origin) {
            Map<BlockPos, BlockState> structure = new HashMap<>();
            Registry<Structure> registry = world.registryAccess().registryOrThrow(Registries.STRUCTURE);
            Optional<HolderSet.Named<Structure>> cairnBasicTag = registry.getTag(Main.CAIRN_BASIC);

            structure.put(origin, Blocks.AIR.defaultBlockState());

            BlockPos actualOrigin = origin.offset(rng.nextIntBetweenInclusive(0, 15), 0, rng.nextIntBetweenInclusive(0, 15));

            switch (type) {
                case SINGLE -> {
                    placeBasicCairn(structure, actualOrigin, rng, world);
                }
                case DOUBLE -> {
                    doubleCairn(structure, actualOrigin, rng, world, cairnBasicTag.orElse(null));
                }
            }

            for (BlockPos position : structure.keySet()) {
                BlockState state = structure.get(position);
                FluidState fluid = world.getFluidState(position);
                if (fluid.is(Fluids.WATER) && state.hasProperty(BlockStateProperties.WATERLOGGED)) state = state.setValue(BlockStateProperties.WATERLOGGED, true);
                world.setBlock(position, state, 3);
            }
        }

        public static void placeShortCairn(Map<BlockPos, BlockState> structure, BlockPos origin, RandomSource rng, ServerLevel world) {
            cairnPillar(structure, origin, rng, world, 0, 0, 1, 1, 0, 1);
        }

        public static void placeBasicCairn(Map<BlockPos, BlockState> structure, BlockPos origin, RandomSource rng, ServerLevel world) {
            cairnPillar(structure, origin, rng, world, 1, 3, 1, 2, 1, 2);
        }

        public static void placeTallCairn(Map<BlockPos, BlockState> structure, BlockPos origin, RandomSource rng, ServerLevel world) {
            cairnPillar(structure, origin, rng, world, 1, 3, 1, 2, 1, 2);
        }

        public static void doubleCairn(Map<BlockPos, BlockState> structure, BlockPos origin, RandomSource rng, ServerLevel world, @Nullable HolderSet<Structure> cairnLocatedTag) {
            placeBasicCairn(structure, origin, rng, world);

            if (cairnLocatedTag == null) return;
            Pair<BlockPos, Holder<Structure>> pair = findNearestMapStructure(world, cairnLocatedTag, origin, 100, false);
            if (pair != null) {
                int x = pair.getFirst().getX() - origin.getX();
                int z = pair.getFirst().getZ() - origin.getZ();
                int intendedDistance = rng.nextIntBetweenInclusive(8, 24);
                double fullDistance = Math.sqrt(x * x + z * z);
                int transformedX = (int) Math.round(x * intendedDistance / fullDistance);
                int transformedZ = (int) Math.round(z * intendedDistance / fullDistance);
                placeTallCairn(structure, origin.offset(transformedX, 0, transformedZ), rng, world);
            }
        }

        public static boolean isReplaceable(BlockState state) {
            return state.is(Main.REPLACEABLE_BY_CAIRN);
        }

        public static void cairnPillar(Map<BlockPos, BlockState> structure, BlockPos origin, RandomSource rng, ServerLevel world, int minBlocks, int maxBlocks, int minWalls, int maxWalls, int minMoss, int maxMoss) {
            cairnPillar(structure, origin, world, rng.nextIntBetweenInclusive(minBlocks, maxBlocks), rng.nextIntBetweenInclusive(minWalls, maxWalls), rng.nextIntBetweenInclusive(minMoss, maxMoss));
        }

        public static void cairnPillar(Map<BlockPos, BlockState> structure, BlockPos origin, ServerLevel world, int blockHeight, int wallHeight, int mossHeight) {
            int maxSearchDistanceVertical = 80;
            int yOffset = -3;
            while (isReplaceable(world.getBlockState(origin.offset(0, yOffset, 0))) && yOffset > -maxSearchDistanceVertical) yOffset--;
            while (!isReplaceable(world.getBlockState(origin.offset(0, yOffset, 0))) && yOffset < maxSearchDistanceVertical) yOffset++;
            if (!isReplaceable(world.getBlockState(origin.offset(0, yOffset, 0)))) return;
            for (int y = 0; y < blockHeight; y++) structure.put(origin.offset(0, yOffset + y, 0), Blocks.COBBLESTONE.defaultBlockState());
            for (int y = blockHeight; y < blockHeight + wallHeight; y++) structure.put(origin.offset(0, yOffset + y, 0), Blocks.COBBLESTONE_WALL.defaultBlockState());
            for (int y = 0; y < mossHeight; y++) GeneralUtil.map(structure, origin.offset(0, yOffset + y, 0), GeneralUtil::mossify);
        }
    }

    @Nullable
    public static Pair<BlockPos, Holder<Structure>> findNearestMapStructure(ServerLevel world, HolderSet<Structure> structures, BlockPos pos, int range, boolean flag7) {
        ChunkGeneratorStructureState chunkGenStructureState = world.getChunkSource().getGeneratorState();
        Map<StructurePlacement, Set<Holder<Structure>>> placementMap = new Object2ObjectArrayMap<>();

        for (Holder<Structure> holder : structures) {
            for (StructurePlacement placement : chunkGenStructureState.getPlacementsForStructure(holder)) {
                placementMap.computeIfAbsent(placement, (a) -> new ObjectArraySet<>()).add(holder);
            }
        }

        //System.out.println(placementMap);
        if (placementMap.isEmpty()) {
            return null;
        } else {
            Pair<BlockPos, Holder<Structure>> structurePosAndHolder = null;
            double shortestDistance = Double.MAX_VALUE;
            StructureManager manager = world.structureManager();
            List<Map.Entry<StructurePlacement, Set<Holder<Structure>>>> entries = new ArrayList<>(placementMap.size());

            for (Map.Entry<StructurePlacement, Set<Holder<Structure>>> entry : placementMap.entrySet()) {
                StructurePlacement placement = entry.getKey();
                if (placement instanceof ConcentricRingsStructurePlacement cRSP) {
                    Pair<BlockPos, Holder<Structure>> nearestGenerated = getNearestGeneratedStructure(entry.getValue(), world, manager, pos, flag7, cRSP);
                    if (nearestGenerated != null) {
                        BlockPos position = nearestGenerated.getFirst();
                        double distance = pos.distSqr(position);
                        if (distance < shortestDistance) {
                            shortestDistance = distance;
                            structurePosAndHolder = nearestGenerated;
                        }
                    }
                } else if (placement instanceof RandomSpreadStructurePlacement) {
                    entries.add(entry);
                }
            }

            //System.out.println(structurePosAndHolder);

            if (!entries.isEmpty()) {
                int sectionX = SectionPos.blockToSectionCoord(pos.getX());
                int sectionZ = SectionPos.blockToSectionCoord(pos.getZ());

                for (int lookRange = 0; lookRange <= range; ++lookRange) {
                    boolean found = false;

                    for (Map.Entry<StructurePlacement, Set<Holder<Structure>>> entry : entries) {
                        RandomSpreadStructurePlacement placement = (RandomSpreadStructurePlacement) entry.getKey();
                        //System.out.println("getting nearest generated structure for entry " + entry);
                        Pair<BlockPos, Holder<Structure>> otherStructure = getNearestGeneratedStructure(entry.getValue(), world, manager, sectionX, sectionZ, lookRange, flag7, chunkGenStructureState.getLevelSeed(), placement);
                        //System.out.println("done");
                        if (otherStructure != null) {
                            found = true;
                            double distance = pos.distSqr(otherStructure.getFirst());
                            if (distance < shortestDistance) {
                                shortestDistance = distance;
                                structurePosAndHolder = otherStructure;
                            }
                        }
                    }

                    if (found) {
                        return structurePosAndHolder;
                    }
                }
            }

            return structurePosAndHolder;
        }
    }


    @Nullable
    private static Pair<BlockPos, Holder<Structure>> getNearestGeneratedStructure(Set<Holder<Structure>> structures, LevelReader world, StructureManager manager, int originX, int originZ, int distance, boolean flag, long seed, RandomSpreadStructurePlacement placement) {
        int spacing = placement.spacing();

        for (int x = -distance; x <= distance; x += Math.max(1, distance + distance)) {
            for (int z = -distance; z <= distance; z += Math.max(1, distance + distance)) {
                //System.out.println("xz loop, x = " + x + ", z = " + z + ", distance = " + distance + ", spacing = " + spacing);
                int structureX = originX + spacing * x;
                int structureZ = originZ + spacing * z;
                ChunkPos chunkpos = placement.getPotentialStructureChunk(seed, structureX, structureZ);
                Pair<BlockPos, Holder<Structure>> pair = getStructureGeneratingAt(structures, world, manager, flag, placement, chunkpos);
                if (pair != null) {
                    return pair;
                }
            }
        }

        return null;
    }

    @Nullable
    private static Pair<BlockPos, Holder<Structure>> getNearestGeneratedStructure(Set<Holder<Structure>> structures, ServerLevel world, StructureManager manager, BlockPos pos, boolean flag, ConcentricRingsStructurePlacement placement) {
        List<ChunkPos> ringPositions = world.getChunkSource().getGeneratorState().getRingPositionsFor(placement);
        if (ringPositions == null) {
            throw new IllegalStateException("Somehow tried to find structures for a placement that doesn't exist");
        } else {
            Pair<BlockPos, Holder<Structure>> closestStructure = null;
            double shortestDistance = Double.MAX_VALUE;
            BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();

            for (ChunkPos chunkPos : ringPositions) {
                mutablePos.set(SectionPos.sectionToBlockCoord(chunkPos.x, 8), 32, SectionPos.sectionToBlockCoord(chunkPos.z, 8));
                double distance = mutablePos.distSqr(pos);
                if (closestStructure == null || distance < shortestDistance) {
                    Pair<BlockPos, Holder<Structure>> closer = getStructureGeneratingAt(structures, world, manager, flag, placement, chunkPos);
                    if (closer != null) {
                        closestStructure = closer;
                        shortestDistance = distance;
                    }
                }
            }

            return closestStructure;
        }
    }

    @Nullable
    private static Pair<BlockPos, Holder<Structure>> getStructureGeneratingAt(Set<Holder<Structure>> structures, LevelReader world, StructureManager manager, boolean flag, StructurePlacement placement, ChunkPos pos) {
        for (Holder<Structure> structureHolder : structures) {
            StructureCheckResult structureCheckResult = manager.checkStructurePresence(pos, structureHolder.value(), flag);
            if (structureCheckResult != StructureCheckResult.START_NOT_PRESENT) {
                if (!flag && structureCheckResult == StructureCheckResult.START_PRESENT) {
                    return Pair.of(placement.getLocatePos(pos), structureHolder);
                }

                ChunkAccess access = world.getChunk(pos.x, pos.z, ChunkStatus.STRUCTURE_STARTS);
                StructureStart structureStart = manager.getStartForStructure(SectionPos.bottomOf(access), structureHolder.value(), access);
                if (structureStart != null && structureStart.isValid() && (!flag || tryAddReference(manager, structureStart))) {
                    return Pair.of(placement.getLocatePos(structureStart.getChunkPos()), structureHolder);
                }
            }
        }

        return null;
    }

    private static boolean tryAddReference(StructureManager manager, StructureStart structureStart) {
        if (structureStart.canBeReferenced()) {
            manager.addReference(structureStart);
            return true;
        } else {
            return false;
        }
    }
}