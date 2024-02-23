package io.github.zygzaggaming.zygzagsmod.common.registry;

import io.github.zygzaggaming.zygzagsmod.common.enchant.*;
import io.github.zygzaggaming.zygzagsmod.common.registry.base.AkomiRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.enchantment.Enchantment;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

import static io.github.zygzaggaming.zygzagsmod.common.Main.MODID;

public class EnchantmentRegistry extends AkomiRegistry<Enchantment> {
    public static final EnchantmentRegistry INSTANCE = new EnchantmentRegistry(DeferredRegister.create(Registries.ENCHANTMENT, MODID));
    public static final Supplier<Enchantment> COOLDOWN_ENCHANTMENT = INSTANCE.register("cooldown", () -> new CooldownEnchantment(Enchantment.Rarity.RARE));
    public static final Supplier<Enchantment> BRUSH_EFFICIENCY_ENCHANTMENT = INSTANCE.register("brush_efficiency", () -> new BrushEfficiencyEnchantment(Enchantment.Rarity.COMMON));
    public static final Supplier<Enchantment> STEADY_ENCHANTMENT = INSTANCE.register("steady", () -> new SteadyEnchantment(Enchantment.Rarity.UNCOMMON));
    public static final Supplier<Enchantment> SPRINGS_ENCHANTMENT = INSTANCE.register("springs", () -> new SpringsEnchantment(Enchantment.Rarity.VERY_RARE));
    public static final Supplier<Enchantment> CURSE_OF_GLASS_ENCHANTMENT = INSTANCE.register("curse_of_glass", () -> new CurseOfGlassEnchantment(Enchantment.Rarity.RARE));
    //public static final Supplier<Enchantment> HALLOWED_ENCHANTMENT = INSTANCE.register("hallowed", () -> new HallowedEnchantment(Enchantment.Rarity.UNCOMMON));
    public static final Supplier<Enchantment> CRITICAL_ENCHANTMENT = INSTANCE.register("critical", () -> new CriticalEnchantment(Enchantment.Rarity.RARE));
    public static final Supplier<Enchantment> COTV_ENCHANTMENT = INSTANCE.register("curse_of_the_valkyrie", () -> new CurseOfTheValkyrieEnchantment(Enchantment.Rarity.RARE));
    public static final Supplier<Enchantment> SPRINT_ENCHANTMENT = INSTANCE.register("sprint", () -> new SprintEnchantment(Enchantment.Rarity.UNCOMMON));
    public static final Supplier<Enchantment> SHATTER_ENCHANTMENT = INSTANCE.register("shatter", () -> new ShatterEnchantment(Enchantment.Rarity.RARE));
    public EnchantmentRegistry(DeferredRegister<Enchantment> register) {
        super(register);
    }
}
