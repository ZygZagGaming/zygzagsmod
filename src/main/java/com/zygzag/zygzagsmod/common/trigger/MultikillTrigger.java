package com.zygzag.zygzagsmod.common.trigger;

import com.google.gson.JsonObject;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.storage.loot.LootContext;
import org.spongepowered.asm.mixin.Mixin;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.function.IntPredicate;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class MultikillTrigger extends SimpleCriterionTrigger<MultikillTrigger.Instance> {
    @Override
    protected Instance createInstance(JsonObject json, ContextAwarePredicate predicate, DeserializationContext context) {
        return new Instance(id, predicate, EntityPredicate.fromJson(json, "all_entities", context), json.get("number").getAsInt());
    }

    public void trigger(ServerPlayer player, List<LivingEntity> killed, DamageSource source) {
        this.trigger(player, (instance) -> {
            return killed.stream().allMatch((it) -> instance.entityPredicate.matches(EntityPredicate.createContext(player, it))) && killed.size() >= instance.number;
        });
    }

    final ResourceLocation id;

    public MultikillTrigger(ResourceLocation id) {
        this.id = id;
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
