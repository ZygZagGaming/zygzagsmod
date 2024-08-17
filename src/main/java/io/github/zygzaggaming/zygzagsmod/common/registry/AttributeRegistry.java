package io.github.zygzaggaming.zygzagsmod.common.registry;

import io.github.zygzaggaming.zygzagsmod.common.registry.base.AkomiRegistry;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.neoforged.neoforge.registries.DeferredRegister;

import static io.github.zygzaggaming.zygzagsmod.common.Main.MODID;

public class AttributeRegistry extends AkomiRegistry<Attribute> {
    public static final AttributeRegistry INSTANCE = new AttributeRegistry(DeferredRegister.create(BuiltInRegistries.ATTRIBUTE, MODID));
    public AttributeRegistry(DeferredRegister<Attribute> register) {
        super(register);
    }

    public static final Holder<Attribute> CRIT_DAMAGE = INSTANCE.register("player.crit_damage", () -> new RangedAttribute("attribute.name.generic.crit_damage", 1.5, 1.0, 1000000.0));
    public static final Holder<Attribute> ARMOR_DURABILITY_REDUCTION = INSTANCE.register("generic.armor_durability_reduction", () -> new RangedAttribute("attribute.name.generic.armor_durability_reduction", 1.0, 0.0, 1000000.0));

    public static final Holder<Attribute> SPRINT_SPEED = INSTANCE.register("player.sprint_speed", () -> new RangedAttribute("attribute.name.generic.sprint_speed", 1.0, 0.0, 1000000.0).setSyncable(true));
    public static final Holder<Attribute> SPRINT_HUNGER_CONSUMPTION = INSTANCE.register("player.sprint_hunger_consumption", () -> new RangedAttribute("attribute.name.generic.sprint_hunger_consumption", 1.0, 0.0, 1000000.0));
    public static final Holder<Attribute> SPRINT_JUMP_HUNGER_CONSUMPTION = INSTANCE.register("player.sprint_jump_hunger_consumption", () -> new RangedAttribute("attribute.name.generic.sprint_jump_hunger_consumption", 1.0, 0.0, 1000000.0));
    public static final Holder<Attribute> FLAIL_DAMAGE = INSTANCE.register("player.flail_damage", () -> new RangedAttribute("attribute.name.generic.flail_damage", 0.0, 0.0, 1000000.0));
    public static final Holder<Attribute> ABILITY_COOLDOWN_LENGTH = INSTANCE.register("player.ability_cooldown_length", () -> new RangedAttribute("attribute.name.generic.ability_cooldown_length", 1.0, 0.0, 1000000.0));
}
