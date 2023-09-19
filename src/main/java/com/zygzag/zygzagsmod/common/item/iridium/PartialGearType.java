package com.zygzag.zygzagsmod.common.item.iridium;

import com.zygzag.zygzagsmod.common.item.iridium.armor.PartialIridiumArmorItem;
import com.zygzag.zygzagsmod.common.item.iridium.tool.partial.PartialIridiumAxeItem;
import com.zygzag.zygzagsmod.common.item.iridium.tool.partial.PartialIridiumHoeItem;
import com.zygzag.zygzagsmod.common.item.iridium.tool.partial.PartialIridiumPickaxeItem;
import com.zygzag.zygzagsmod.common.item.iridium.tool.partial.PartialIridiumSwordItem;
import com.zygzag.zygzagsmod.common.tier.IridiumArmorMaterial;
import com.zygzag.zygzagsmod.common.tier.IridiumToolTier;
import net.minecraft.world.item.ArmorItem;

public class PartialGearType {
    public static final PartialGearType HELMET = new PartialGearType(5, (properties, platings) -> new PartialIridiumArmorItem(
            IridiumArmorMaterial.getArmorForPartial(platings, 5),
            ArmorItem.Type.HELMET,
            properties,
            5,
            platings
    ));
    public static final PartialGearType CHESTPLATE = new PartialGearType(8, (properties, platings) -> new PartialIridiumArmorItem(
            IridiumArmorMaterial.getArmorForPartial(platings, 8),
            ArmorItem.Type.CHESTPLATE,
            properties,
            8,
            platings
    ));
    public static final PartialGearType LEGGINGS = new PartialGearType(7, (properties, platings) -> new PartialIridiumArmorItem(
            IridiumArmorMaterial.getArmorForPartial(platings, 7),
            ArmorItem.Type.LEGGINGS,
            properties,
            7,
            platings
    ));
    public static final PartialGearType BOOTS = new PartialGearType(4, (properties, platings) -> new PartialIridiumArmorItem(
            IridiumArmorMaterial.getArmorForPartial(platings, 4),
            ArmorItem.Type.BOOTS,
            properties,
            4,
            platings
    ));
    public static final PartialGearType SWORD = new PartialGearType(2, (properties, platings) -> new PartialIridiumSwordItem(
            IridiumToolTier.getToolTier(platings, 2),
            3,
            -2.4F,
            properties,
            2,
            platings // there's only one but whatever
    ));
    public static final PartialGearType PICKAXE = new PartialGearType(3, (properties, platings) -> new PartialIridiumPickaxeItem(
            IridiumToolTier.getToolTier(platings, 3),
            1,
            -2.8F,
            properties,
            3,
            platings
    ));
    public static final PartialGearType AXE = new PartialGearType(3, (properties, platings) -> new PartialIridiumAxeItem(
            IridiumToolTier.getToolTier(platings, 3),
            5,
            -3.0F,
            properties,
            3,
            platings
    ));
    public static final PartialGearType HOE = new PartialGearType(2, (properties, platings) -> new PartialIridiumHoeItem(
            IridiumToolTier.getToolTier(platings, 2),
            -1,
            0.0F,
            properties,
            2,
            platings
    ));

    public final int maxPlatings;
    public final PartialItemFactory itemFactory;

    public PartialGearType(int maxPlatings, PartialItemFactory factory) {
        this.maxPlatings = maxPlatings;
        itemFactory = factory;
    }
}
