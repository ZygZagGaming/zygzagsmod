package com.zygzag.zygzagsmod.common.trigger;

import com.google.gson.JsonObject;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.loot.LootContext;

import javax.annotation.ParametersAreNonnullByDefault;

import static com.zygzag.zygzagsmod.common.Main.MODID;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ThrowSnowballsAtMobTrigger extends SimpleCriterionTrigger<ThrowSnowballsAtMobTrigger.TriggerInstance> {
    static final ResourceLocation ID = new ResourceLocation(MODID, "throw_snowballs_at_mob");

    public ResourceLocation getId() {
        return ID;
    }

    public void trigger(ServerPlayer player, int numberOfSnowballs, Entity target) {
        trigger(player, (instance) -> instance.matches(numberOfSnowballs, EntityPredicate.createContext(player, target)));
    }

    @Override
    protected TriggerInstance createInstance(JsonObject json, ContextAwarePredicate player, DeserializationContext ctx) {
        int numberOfSnowballs = json.get("number_of_snowballs").getAsInt();
        ContextAwarePredicate target = EntityPredicate.fromJson(json, "target", ctx);

        return new TriggerInstance(player, numberOfSnowballs, target);
    }

    public static class TriggerInstance extends AbstractCriterionTriggerInstance {
        private final int numberOfSnowballs;
        private final ContextAwarePredicate target;

        public TriggerInstance(ContextAwarePredicate player, int numberOfSnowballs, ContextAwarePredicate target) {
            super(ID, player);
            this.numberOfSnowballs = numberOfSnowballs;
            this.target = target;
        }

        public JsonObject serializeToJson(SerializationContext ctx) {
            JsonObject jsonobject = super.serializeToJson(ctx);
            jsonobject.addProperty("number_of_snowballs", numberOfSnowballs);
            jsonobject.add("target", target.toJson(ctx));
            return jsonobject;
        }

        public boolean matches(int numberOfSnowballs, LootContext target) {
            return numberOfSnowballs > this.numberOfSnowballs && this.target.matches(target);
        }
    }
}
