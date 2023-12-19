package com.zygzag.zygzagsmod.common.registry;

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

    public static final Supplier<Attribute> JUMP_POWER = INSTANCE.register("generic.jump_power", () -> new RangedAttribute("attribute.name.generic.jump_power", 1.0, 0.0, 1000000.0));
}