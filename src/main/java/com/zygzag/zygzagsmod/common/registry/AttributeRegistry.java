package com.zygzag.zygzagsmod.common.registry;

import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.checkerframework.checker.units.qual.A;

import static com.zygzag.zygzagsmod.common.Main.MODID;

public class AttributeRegistry extends Registry<Attribute> {
    public static final AttributeRegistry INSTANCE = new AttributeRegistry(DeferredRegister.create(ForgeRegistries.ATTRIBUTES, MODID));
    public AttributeRegistry(DeferredRegister<Attribute> register) {
        super(register);
    }

    public static final RegistryObject<Attribute> JUMP_POWER = INSTANCE.register("generic.jump_power", () -> new RangedAttribute("attribute.name.generic.jump_power", 1.0, 0.0, 1000000.0));
}
