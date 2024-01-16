package com.zygzag.zygzagsmod.common.registry;

import com.google.common.collect.Multimap;
import com.zygzag.zygzagsmod.common.Main;
import com.zygzag.zygzagsmod.common.registry.base.CustomAkomiRegistry;
import com.zygzag.zygzagsmod.common.upgrade.ItemUpgrade;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

import static com.zygzag.zygzagsmod.common.Main.MODID;

public class ItemUpgradeRegistry extends CustomAkomiRegistry<ItemUpgrade> {
    public static final ItemUpgradeRegistry INSTANCE = new ItemUpgradeRegistry(DeferredRegister.create(new ResourceLocation(MODID, "item_upgrade"), MODID));
    public static final Registry<ItemUpgrade> BACKING_REGISTRY = INSTANCE.backingRegistry();

    public static final Supplier<ItemUpgrade> DAMAGE = INSTANCE.register(
            "damage",
            () -> new ItemUpgrade() {
                @Override
                public void setupAttributes(Multimap<Attribute, AttributeModifier> attributeMap, EquipmentSlot slot, int level) {
                    if (slot == EquipmentSlot.MAINHAND) attributeMap.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(Main.DAMAGE_UPGRADE_MODIFIER_UUID, "Damage upgrade", 4 - 4 * Math.pow(1.125, -level), AttributeModifier.Operation.ADDITION));
                }

                @Override
                public ChatFormatting[] getFormatting() {
                    return new ChatFormatting[]{ ChatFormatting.DARK_RED };
                }
            }
    );

    public ItemUpgradeRegistry(DeferredRegister<ItemUpgrade> register) {
        super(register);
    }
}
