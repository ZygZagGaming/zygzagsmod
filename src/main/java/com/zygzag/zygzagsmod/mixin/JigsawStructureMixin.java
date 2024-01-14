package com.zygzag.zygzagsmod.mixin;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.heightproviders.HeightProvider;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.pools.alias.PoolAliasBinding;
import net.minecraft.world.level.levelgen.structure.structures.JigsawStructure;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

import static net.minecraft.world.level.levelgen.structure.Structure.settingsCodec;

@Mixin(JigsawStructure.class)
public class JigsawStructureMixin {
    @Shadow
    public static Codec<JigsawStructure> CODEC;
    static {
        CODEC = ExtraCodecs.validate(
                        RecordCodecBuilder.mapCodec(
                                instance -> instance.group(
                                                settingsCodec(instance),
                                                StructureTemplatePool.CODEC.fieldOf("start_pool").forGetter(jigsaw -> jigsaw.startPool),
                                                ResourceLocation.CODEC.optionalFieldOf("start_jigsaw_name").forGetter(jigsaw -> jigsaw.startJigsawName),
                                                Codec.intRange(0, 20).fieldOf("size").forGetter(jigsaw -> jigsaw.maxDepth),
                                                HeightProvider.CODEC.fieldOf("start_height").forGetter(jigsaw -> jigsaw.startHeight),
                                                Codec.BOOL.fieldOf("use_expansion_hack").forGetter(jigsaw -> jigsaw.useExpansionHack),
                                                Heightmap.Types.CODEC.optionalFieldOf("project_start_to_heightmap").forGetter(jigsaw -> jigsaw.projectStartToHeightmap),
                                                Codec.intRange(1, 150).fieldOf("max_distance_from_center").forGetter(jigsaw -> jigsaw.maxDistanceFromCenter),
                                                Codec.list(PoolAliasBinding.CODEC).optionalFieldOf("pool_aliases", List.of()).forGetter(jigsaw -> jigsaw.poolAliases)
                                        )
                                        .apply(instance, JigsawStructure::new)
                        ),
                        JigsawStructureMixin::verifyRange
                )
                .codec();
    }
    private static DataResult<JigsawStructure> verifyRange(JigsawStructure jigsaw) {
        int i = switch (jigsaw.terrainAdaptation()) {
            case NONE -> 0;
            case BURY, BEARD_THIN, BEARD_BOX -> 12;
        };
        return jigsaw.maxDistanceFromCenter + i > 150
                ? DataResult.error(() -> "Structure size including terrain adaptation must not exceed 150")
                : DataResult.success(jigsaw);
    }
}
