package io.github.zygzaggaming.zygzagsmod.common.registry;

import io.github.zygzaggaming.zygzagsmod.common.item.iridium.Socket;
import io.github.zygzaggaming.zygzagsmod.common.item.iridium.armor.IridiumChestplateItem;
import io.github.zygzaggaming.zygzagsmod.common.item.iridium.tool.*;
import io.github.zygzaggaming.zygzagsmod.common.tier.IridiumToolTier;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.SwordItem;

public enum SocketedGearType {
    SWORD((properties, socket) -> new IridiumSwordItem(
            getTierForSocket(socket),
            properties.attributes(SwordItem.createAttributes(IridiumToolTier.FULL, 3, -2.4f)),
            socket
    )),
    PICKAXE((properties, socket) -> new IridiumPickaxeItem(
            socket == Socket.WITHER_SKULL ? IridiumToolTier.WITHER_SOCKETED_PICK : getTierForSocket(socket),
            properties.attributes(PickaxeItem.createAttributes(IridiumToolTier.FULL, 1, -2.8f)),
            socket
    )),
    AXE((properties, socket) -> new IridiumAxeItem(
            getTierForSocket(socket),
            properties.attributes(AxeItem.createAttributes(IridiumToolTier.FULL, socket == Socket.DIAMOND ? 4.5f : 5, -3.1f)),
            socket
    )),
    SHOVEL((properties, socket) -> new IridiumShovelItem(
            getTierForSocket(socket),
            properties.attributes(IridiumShovelItem.createAttributes(IridiumToolTier.FULL, 1.5f, -3f)),
            socket
    )),
    HOE((properties, socket) -> new IridiumHoeItem(
            getTierForSocket(socket),
            properties.attributes(HoeItem.createAttributes(IridiumToolTier.FULL, socket == Socket.WITHER_SKULL ? 3 : -4f, socket == Socket.WITHER_SKULL ? -2.4f : 0)),
            socket
    )),
    CHESTPLATE((properties, socket) -> new IridiumChestplateItem(
            socket == Socket.DIAMOND ? ArmorMaterialRegistry.DIAMOND_SOCKETED :
                    socket == Socket.EMERALD ? ArmorMaterialRegistry.EMERALD_SOCKETED :
                            ArmorMaterialRegistry.IRIDIUM,
            properties,
            socket
    )),
    SCEPTER(IridiumScepterItem::new);

    public final IridiumGearRegistry.SocketedItemFactory itemFactory;

    SocketedGearType(IridiumGearRegistry.SocketedItemFactory factory) {
        itemFactory = factory;
    }

    public static IridiumToolTier getTierForSocket(Socket socket) {
        return socket == Socket.DIAMOND ? IridiumToolTier.DIAMOND_SOCKETED : socket == Socket.EMERALD ? IridiumToolTier.EMERALD_SOCKETED : IridiumToolTier.FULL;
    }
}
