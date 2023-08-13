package com.zygzag.zygzagsmod.common.registries;

import com.zygzag.zygzagsmod.common.enchant.CooldownEnchantment;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.zygzag.zygzagsmod.common.Main.MODID;

public class EnchantmentRegistry extends Registry<Enchantment> {
    public static final EnchantmentRegistry INSTANCE = new EnchantmentRegistry(DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, MODID));
    public EnchantmentRegistry(DeferredRegister<Enchantment> register) {
        super(register);
    }

    public static final RegistryObject<Enchantment> COOLDOWN_ENCHANTMENT = INSTANCE.register("cooldown", () -> new CooldownEnchantment(Enchantment.Rarity.RARE));
}
