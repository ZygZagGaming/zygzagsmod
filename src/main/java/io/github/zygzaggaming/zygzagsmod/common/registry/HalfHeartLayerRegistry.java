package io.github.zygzaggaming.zygzagsmod.common.registry;

import io.github.zygzaggaming.hearty.api.HalfHeartContext;
import io.github.zygzaggaming.hearty.api.HalfHeartLayer;
import io.github.zygzaggaming.hearty.api.HeartyRegistries;
import io.github.zygzaggaming.zygzagsmod.common.registry.base.AkomiRegistry;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

import static io.github.zygzaggaming.zygzagsmod.common.Main.MODID;

public class HalfHeartLayerRegistry extends AkomiRegistry<HalfHeartLayer> {
    public static final HalfHeartLayerRegistry INSTANCE = new HalfHeartLayerRegistry(DeferredRegister.create(HeartyRegistries.HALF_HEART_LAYER_KEY, "half_heart_layer"));

    public static final Supplier<HalfHeartLayer> MAGMATIC = INSTANCE.register("magmatic", () -> new HalfHeartLayer(new ResourceLocation(MODID, "magmatic"), 3.5) {
        @Override
        public HalfHeartContext apply(HalfHeartContext ctx) {
            if (ctx.heartType().id.equals(new ResourceLocation("hearty:health")) && ctx.player().getData(AttachmentTypeRegistry.LIVING_ENTITY_OVERHEAT_ATTACHMENT.get()) > 0) {
                return ctx.withSprite(new ResourceLocation(MODID, "hud/hearts/magmatic/full"));
            }
            return ctx;
        }
    });

    public HalfHeartLayerRegistry(DeferredRegister<HalfHeartLayer> register) {
        super(register);
    }
}