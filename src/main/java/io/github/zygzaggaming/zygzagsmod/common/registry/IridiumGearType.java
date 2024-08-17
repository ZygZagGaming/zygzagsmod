package io.github.zygzaggaming.zygzagsmod.common.registry;

import io.github.zygzaggaming.zygzagsmod.common.item.iridium.armor.PartialIridiumArmorItem;
import io.github.zygzaggaming.zygzagsmod.common.item.iridium.tool.partial.PartialIridiumAxeItem;
import io.github.zygzaggaming.zygzagsmod.common.item.iridium.tool.partial.PartialIridiumHoeItem;
import io.github.zygzaggaming.zygzagsmod.common.item.iridium.tool.partial.PartialIridiumPickaxeItem;
import io.github.zygzaggaming.zygzagsmod.common.item.iridium.tool.partial.PartialIridiumSwordItem;
import io.github.zygzaggaming.zygzagsmod.common.tier.IridiumToolTier;
import net.minecraft.world.item.ArmorItem;

public enum IridiumGearType {
    HELMET(5, (properties, platings) -> new PartialIridiumArmorItem(
            ArmorMaterialRegistry.getArmorForPartial(platings, 5),
            ArmorItem.Type.HELMET,
            properties,
            5,
            platings
    )),
    CHESTPLATE(8, (properties, platings) -> new PartialIridiumArmorItem(
            ArmorMaterialRegistry.getArmorForPartial(platings, 8),
            ArmorItem.Type.CHESTPLATE,
            properties,
            8,
            platings
    )),
    LEGGINGS(7, (properties, platings) -> new PartialIridiumArmorItem(
            ArmorMaterialRegistry.getArmorForPartial(platings, 7),
            ArmorItem.Type.LEGGINGS,
            properties,
            7,
            platings
    )),
    BOOTS(4, (properties, platings) -> new PartialIridiumArmorItem(
            ArmorMaterialRegistry.getArmorForPartial(platings, 4),
            ArmorItem.Type.BOOTS,
            properties,
            4,
            platings
    )),
    SWORD(2, (properties, platings) -> new PartialIridiumSwordItem(
            IridiumToolTier.getToolTier(platings, 2),
            3,
            -2.4F,
            properties,
            2,
            platings // theres only one but whatever
    )),
    PICKAXE(3, (properties, platings) -> new PartialIridiumPickaxeItem(
            IridiumToolTier.getToolTier(platings, 3),
            1,
            -2.8F,
            properties,
            3,
            platings
    )),
    AXE(3, (properties, platings) -> new PartialIridiumAxeItem(
            IridiumToolTier.getToolTier(platings, 3),
            5,
            -3.0F,
            properties,
            3,
            platings
    )),
    HOE(2, (properties, platings) -> new PartialIridiumHoeItem(
            IridiumToolTier.getToolTier(platings, 2),
            -1,
            0.0F,
            properties,
            2,
            platings
    )),
    SHOVEL(1, (properties, platings) -> null);

    public final int maxPlatings;
    public final IridiumGearRegistry.PartialItemFactory itemFactory;

    IridiumGearType(int maxPlatings, IridiumGearRegistry.PartialItemFactory factory) {
        this.maxPlatings = maxPlatings;
        itemFactory = factory;
    }

    public String lowerName() {
        return name().toLowerCase();
    }
}
