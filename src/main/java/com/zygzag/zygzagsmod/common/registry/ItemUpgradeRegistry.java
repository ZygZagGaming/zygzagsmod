package com.zygzag.zygzagsmod.common.registry;

import com.zygzag.zygzagsmod.common.UUIDs;
import com.zygzag.zygzagsmod.common.registry.base.CustomAkomiRegistry;
import com.zygzag.zygzagsmod.common.upgrade.ArmorAttributeUpgrade;
import com.zygzag.zygzagsmod.common.upgrade.AttributeUpgrade;
import com.zygzag.zygzagsmod.common.upgrade.ItemUpgrade;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Predicate;
import java.util.function.Supplier;

import static com.zygzag.zygzagsmod.common.Main.MODID;

public class ItemUpgradeRegistry extends CustomAkomiRegistry<ItemUpgrade> {
    public static final ItemUpgradeRegistry INSTANCE = new ItemUpgradeRegistry(DeferredRegister.create(new ResourceLocation(MODID, "item_upgrade"), MODID));
    public static final Registry<ItemUpgrade> BACKING_REGISTRY = INSTANCE.backingRegistry();
    public static final Predicate<EquipmentSlot> ARMOR_PREDICATE = (it) -> it != EquipmentSlot.MAINHAND && it != EquipmentSlot.OFFHAND;

    public static final Supplier<ItemUpgrade> DAMAGE = INSTANCE.register(
            "damage",
            () -> new AttributeUpgrade(
                    Attributes.ATTACK_DAMAGE,
                    EquipmentSlot.MAINHAND,
                    UUIDs.DAMAGE_UPGRADE_MODIFIER_UUID,
                    "Damage upgrade",
                    AttributeModifier.Operation.ADDITION,
                    (level) -> 4 - 4 * Math.pow(1.125, -level),
                    ChatFormatting.DARK_RED
            )
    );
    public static final Supplier<ItemUpgrade> ARMOR = INSTANCE.register(
            "armor",
            () -> new ArmorAttributeUpgrade(
                    Attributes.ARMOR,
                    UUIDs.ARMOR_UPGRADE_MODIFIER_UUID,
                    "Armor upgrade",
                    AttributeModifier.Operation.ADDITION,
                    (level) -> 3 - 3 * Math.pow(1.166, -level),
                    ChatFormatting.GOLD
            )
    );

    public ItemUpgradeRegistry(DeferredRegister<ItemUpgrade> register) {
        super(register);
    }
}
