package com.zygzag.zygzagsmod.common.block.entity;

import com.zygzag.zygzagsmod.common.registry.BlockEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import org.jetbrains.annotations.Nullable;

public class StructurePlacerBlockEntity extends BlockEntity {
    public @Nullable Structure structure;

    public StructurePlacerBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.STRUCTURE_PLACER.get(), pos, state);
    }

    public void tick(Level level, BlockPos pos, BlockState state) {
        if (level.isLoaded(pos)) {
            if (level instanceof ServerLevel world && structure != null) {
                ChunkGenerator chunkGen = world.getChunkSource().getGenerator();
                StructureStart structureStart = structure.generate(world.registryAccess(), chunkGen, chunkGen.getBiomeSource(), world.getChunkSource().randomState(), world.getStructureManager(), world.getSeed(), new ChunkPos(pos), 0, world, (a) -> true);
                if (structureStart.isValid()) {
                    BoundingBox bounds = structureStart.getBoundingBox();
                    ChunkPos min = new ChunkPos(SectionPos.blockToSectionCoord(bounds.minX()), SectionPos.blockToSectionCoord(bounds.minZ()));
                    ChunkPos max = new ChunkPos(SectionPos.blockToSectionCoord(bounds.maxX()), SectionPos.blockToSectionCoord(bounds.maxZ()));
                    if (ChunkPos.rangeClosed(min, max).filter((it) -> !world.isLoaded(it.getWorldPosition())).findAny().isEmpty()) { // if whole structure bounds are loaded
                        ChunkPos.rangeClosed(min, max).forEach((it) -> {
                            structureStart.placeInChunk(world, world.structureManager(), chunkGen, world.getRandom(), new BoundingBox(it.getMinBlockX(), world.getMinBuildHeight(), it.getMinBlockZ(), it.getMaxBlockX(), world.getMaxBuildHeight(), it.getMaxBlockZ()), it);
                        });
                        if (world.getBlockState(pos).is(state.getBlock()))
                            world.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
                    }
                }
            } else level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
        }
    }
}
