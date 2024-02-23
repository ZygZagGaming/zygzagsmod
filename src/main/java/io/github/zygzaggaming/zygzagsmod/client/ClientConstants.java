package io.github.zygzaggaming.zygzagsmod.client;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;

import java.util.OptionalDouble;

public class ClientConstants {
    public static RenderType.CompositeRenderType SIGHT_RENDER_TYPE = RenderType.create(
            "sight",
            DefaultVertexFormat.POSITION_COLOR_NORMAL,
            VertexFormat.Mode.LINES,
            256,
            RenderType.CompositeState
                    .builder()
                    .setShaderState(RenderType.RENDERTYPE_LINES_SHADER)
                    .setLineState(new RenderStateShard.LineStateShard(OptionalDouble.empty()))
                    .setLayeringState(RenderType.VIEW_OFFSET_Z_LAYERING)
                    .setTransparencyState(RenderType.TRANSLUCENT_TRANSPARENCY)
                    .setOutputState(RenderType.OUTLINE_TARGET)
                    .setWriteMaskState(RenderType.COLOR_DEPTH_WRITE)
                    .setDepthTestState(RenderType.NO_DEPTH_TEST)
                    .setCullState(RenderType.NO_CULL)
                    .createCompositeState(true)
    );
}
