package io.github.zygzaggaming.zygzagsmod.common.registry;

import io.github.zygzaggaming.zygzagsmod.common.item.iridium.Socket;
import io.github.zygzaggaming.zygzagsmod.common.item.iridium.armor.IridiumChestplateItem;
import io.github.zygzaggaming.zygzagsmod.common.item.iridium.tool.*;
import io.github.zygzaggaming.zygzagsmod.common.tier.IridiumArmorMaterial;
import io.github.zygzaggaming.zygzagsmod.common.tier.IridiumToolTier;

public enum SocketedGearType {
    SWORD((properties, socket) -> new IridiumSwordItem(
            socket == Socket.DIAMOND ? IridiumToolTier.DIAMOND_SOCKETED :
                    socket == Socket.EMERALD ? IridiumToolTier.EMERALD_SOCKETED :
                            IridiumToolTier.FULL,
            3,
            -2.4f,
            properties,
            socket
    )),
    PICKAXE((properties, socket) -> new IridiumPickaxeItem(
            socket == Socket.DIAMOND ? IridiumToolTier.DIAMOND_SOCKETED_PICK :
                    socket == Socket.EMERALD ? IridiumToolTier.EMERALD_SOCKETED :
                            socket == Socket.WITHER_SKULL ? IridiumToolTier.WITHER_SOCKETED_PICK :
                                    IridiumToolTier.FULL,
            1,
            -2.8f,
            properties,
            socket
    )),
    AXE((properties, socket) -> new IridiumAxeItem(
            socket == Socket.DIAMOND ? IridiumToolTier.DIAMOND_SOCKETED :
                    socket == Socket.EMERALD ? IridiumToolTier.EMERALD_SOCKETED :
                            IridiumToolTier.FULL,
            socket == Socket.DIAMOND ? 4.5f : 5,
            -3,
            properties,
            socket
    )),
    SHOVEL((properties, socket) -> new IridiumShovelItem(
            socket == Socket.DIAMOND ? IridiumToolTier.DIAMOND_SOCKETED :
                    socket == Socket.EMERALD ? IridiumToolTier.EMERALD_SOCKETED :
                            IridiumToolTier.FULL,
            1.5f,
            -3,
            properties,
            socket
    )),
    HOE((properties, socket) -> new IridiumHoeItem(
            socket == Socket.DIAMOND ? IridiumToolTier.DIAMOND_SOCKETED :
                    socket == Socket.EMERALD ? IridiumToolTier.EMERALD_SOCKETED :
                            IridiumToolTier.FULL,
            socket == Socket.WITHER_SKULL ? 3 : -1,
            socket == Socket.WITHER_SKULL ? -2.4f : 0,
            properties,
            socket
    )),
    CHESTPLATE((properties, socket) -> new IridiumChestplateItem(
            socket == Socket.DIAMOND ? IridiumArmorMaterial.DIAMOND_SOCKETED :
                    socket == Socket.EMERALD ? IridiumArmorMaterial.EMERALD_SOCKETED :
                            IridiumArmorMaterial.IRIDIUM,
            properties,
            socket
    )),
    SCEPTER(IridiumScepterItem::new);

    public final IridiumGearRegistry.SocketedItemFactory itemFactory;

    SocketedGearType(IridiumGearRegistry.SocketedItemFactory factory) {
        itemFactory = factory;
    }
}
