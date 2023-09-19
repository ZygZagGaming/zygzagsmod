package com.zygzag.zygzagsmod.common.item.iridium;

import net.minecraft.world.item.Item;

public interface PartialItemFactory {
    Item provideItem(Item.Properties properties, int platings);
}
