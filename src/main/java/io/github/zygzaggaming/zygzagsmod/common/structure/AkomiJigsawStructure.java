package io.github.zygzaggaming.zygzagsmod.common.structure;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.heightproviders.HeightProvider;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import net.minecraft.world.level.levelgen.structure.pools.DimensionPadding;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.pools.alias.PoolAliasBinding;
import net.minecraft.world.level.levelgen.structure.structures.JigsawStructure;
import net.minecraft.world.level.levelgen.structure.templatesystem.LiquidSettings;

import java.util.List;
import java.util.Optional;

public class AkomiJigsawStructure extends JigsawStructure {
    public static MapCodec<AkomiJigsawStructure> CODEC = RecordCodecBuilder.mapCodec(
                            instance -> instance.group(
                                            settingsCodec(instance),
                                            StructureTemplatePool.CODEC.fieldOf("start_pool").forGetter(p_227656_ -> p_227656_.startPool),
                                            ResourceLocation.CODEC.optionalFieldOf("start_jigsaw_name").forGetter(p_227654_ -> p_227654_.startJigsawName),
                                            Codec.intRange(0, 80).fieldOf("size").forGetter(p_227652_ -> p_227652_.maxDepth),
                                            HeightProvider.CODEC.fieldOf("start_height").forGetter(p_227649_ -> p_227649_.startHeight),
                                            Codec.BOOL.fieldOf("use_expansion_hack").forGetter(p_227646_ -> p_227646_.useExpansionHack),
                                            Heightmap.Types.CODEC.optionalFieldOf("project_start_to_heightmap").forGetter(p_227644_ -> p_227644_.projectStartToHeightmap),
                                            Codec.intRange(1, 1024).fieldOf("max_distance_from_center").forGetter(p_227642_ -> p_227642_.maxDistanceFromCenter),
                                            Codec.list(PoolAliasBinding.CODEC).optionalFieldOf("pool_aliases", List.of()).forGetter(p_307187_ -> p_307187_.poolAliases),
                                            DimensionPadding.CODEC
                                                    .optionalFieldOf("dimension_padding", DEFAULT_DIMENSION_PADDING)
                                                    .forGetter(p_348455_ -> p_348455_.dimensionPadding),
                                            LiquidSettings.CODEC.optionalFieldOf("liquid_settings", DEFAULT_LIQUID_SETTINGS).forGetter(p_352036_ -> p_352036_.liquidSettings)
                                    )
                                    .apply(instance, AkomiJigsawStructure::new)
                    );

    private static DataResult<AkomiJigsawStructure> verify(AkomiJigsawStructure structure) {
        int i = structure.terrainAdaptation() == TerrainAdjustment.NONE ? 0 : 12;
        return structure.maxDistanceFromCenter + i > 1024
                ? DataResult.error(() -> "Structure size including terrain adaptation must not exceed 1024")
                : DataResult.success(structure);
    }

    public AkomiJigsawStructure(StructureSettings settings, Holder<StructureTemplatePool> startingPool, Optional<ResourceLocation> id, int number, HeightProvider startingHeightProvider, boolean idk, Optional<Heightmap.Types> heightmap, int number2, List<PoolAliasBinding> poolAliases, DimensionPadding padding, LiquidSettings liquidSettings) {
        super(settings, startingPool, id, number, startingHeightProvider, idk, heightmap, number2, poolAliases, padding, liquidSettings);
    }
}
