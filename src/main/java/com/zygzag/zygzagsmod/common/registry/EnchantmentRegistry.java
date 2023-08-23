package com.zygzag.zygzagsmod.common.registry;

import com.zygzag.zygzagsmod.common.enchant.BrushEfficiencyEnchantment;
import com.zygzag.zygzagsmod.common.enchant.CooldownEnchantment;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import static com.zygzag.zygzagsmod.common.Main.MODID;

public class EnchantmentRegistry extends Registry<Enchantment> {
    public static final EnchantmentRegistry INSTANCE = new EnchantmentRegistry(DeferredRegister.create(Registries.ENCHANTMENT, MODID));
    public EnchantmentRegistry(DeferredRegister<Enchantment> register) {
        super(register);
    }

    public static final RegistryObject<Enchantment> COOLDOWN_ENCHANTMENT = INSTANCE.register("cooldown", () -> new CooldownEnchantment(Enchantment.Rarity.RARE));
    public static final RegistryObject<Enchantment> BRUSH_EFFICIENCY_ENCHANTMENT = INSTANCE.register("brush_efficiency", () -> new BrushEfficiencyEnchantment(Enchantment.Rarity.COMMON));
}
