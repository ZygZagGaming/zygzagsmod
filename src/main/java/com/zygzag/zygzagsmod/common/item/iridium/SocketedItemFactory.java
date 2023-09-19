package com.zygzag.zygzagsmod.common.item.iridium;

import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;

public interface SocketedItemFactory {
    Item provideItem(Item.Properties properties, RegistryObject<Socket> socket);
}
