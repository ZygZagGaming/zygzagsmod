package com.zygzag.zygzagsmod.common.registry;

import com.zygzag.zygzagsmod.common.registry.base.AkomiRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

import static com.zygzag.zygzagsmod.common.Main.MODID;

public class AttributeRegistry extends AkomiRegistry<Attribute> {
    public static final AttributeRegistry INSTANCE = new AttributeRegistry(DeferredRegister.create(BuiltInRegistries.ATTRIBUTE, MODID));
    public AttributeRegistry(DeferredRegister<Attribute> register) {
        super(register);
    }

    public static final Supplier<Attribute> JUMP_POWER = INSTANCE.register("generic.jump_power", () -> new RangedAttribute("attribute.name.generic.jump_power", 1.0, 0.0, 1000000.0).setSyncable(true));
    public static final Supplier<Attribute> CRIT_DAMAGE = INSTANCE.register("generic.crit_damage", () -> new RangedAttribute("attribute.name.generic.crit_damage", 1.5, 1.0, 1000000.0));
}
