package com.zygzag.zygzagsmod.common.trigger;

import com.google.gson.JsonObject;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class MultikillTrigger extends SimpleCriterionTrigger<MultikillTrigger.Instance> {
    final ResourceLocation id;

    public MultikillTrigger(ResourceLocation id) {
        this.id = id;
    }

    @Override
    protected Instance createInstance(JsonObject json, ContextAwarePredicate predicate, DeserializationContext context) {
        return new Instance(id, predicate, EntityPredicate.fromJson(json, "all_entities", context), json.get("number").getAsInt());
    }

    public void trigger(ServerPlayer player, List<LivingEntity> killed, DamageSource source) {
        this.trigger(player, (instance) -> {
            return killed.stream().allMatch((it) -> instance.entityPredicate.matches(EntityPredicate.createContext(player, it))) && killed.size() >= instance.number;
        });
    }

    public ResourceLocation getId() {
        return this.id;
    }

    public static class Instance extends AbstractCriterionTriggerInstance {
        ContextAwarePredicate entityPredicate;
        int number;

        public Instance(ResourceLocation id, ContextAwarePredicate playerPredicate, ContextAwarePredicate entityPredicate, int number) {
            super(id, playerPredicate);
            this.entityPredicate = entityPredicate;
            this.number = number;
        }
    }
}
