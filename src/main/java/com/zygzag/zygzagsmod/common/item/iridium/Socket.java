package com.zygzag.zygzagsmod.common.item.iridium;

import com.mojang.blaze3d.FieldsAreNonnullByDefault;
import com.mojang.blaze3d.MethodsReturnNonnullByDefault;
import com.zygzag.zygzagsmod.common.registry.IridiumGearRegistry;
import com.zygzag.zygzagsmod.common.registry.ItemRegistry;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Predicate;
import java.util.function.Supplier;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
@FieldsAreNonnullByDefault
public enum Socket {
    NONE(() -> Items.AIR, () -> Items.AIR, 0xffffff),
    DIAMOND(() -> Items.DIAMOND, ItemRegistry.DIAMOND_SOCKET_SCHEMATIC, 0x4aedd9),
    EMERALD(() -> Items.EMERALD, ItemRegistry.EMERALD_SOCKET_SCHEMATIC, 0x41f384),
    SKULL(() -> Items.SKELETON_SKULL, ItemRegistry.SKULL_SOCKET_SCHEMATIC, 0xd3d3d3),
    WITHER_SKULL(() -> Items.WITHER_SKELETON_SKULL, ItemRegistry.WITHER_SKULL_SOCKET_SCHEMATIC, 0x515353),
    AMETHYST(() -> Items.AMETHYST_SHARD, ItemRegistry.AMETHYST_SOCKET_SCHEMATIC, 0xcfa0f3);

    public final Supplier<Item> itemSupplier;
    public final Supplier<Item> schematicSupplier;
    public final int color;
    public final Predicate<IridiumGearRegistry.SocketedGearType> gearTypeFilter;

    Socket(Supplier<Item> itemSupplier, Supplier<Item> schematicSupplier, int color) {
        this.itemSupplier = itemSupplier;
        this.schematicSupplier = schematicSupplier;
        this.color = color;
        this.gearTypeFilter = (it) -> true;
    }

    Socket(Supplier<Item> itemSupplier, Supplier<Item> schematicSupplier, int color, Predicate<IridiumGearRegistry.SocketedGearType> gearTypeFilter) {
        this.itemSupplier = itemSupplier;
        this.schematicSupplier = schematicSupplier;
        this.color = color;
        this.gearTypeFilter = gearTypeFilter;
    }

    public Item item() {
        return itemSupplier.get();
    }

    public Item schematic() {
        return schematicSupplier.get();
    }
}
