package io.github.zygzaggaming.zygzagsmod.common.item.iridium;

import com.mojang.blaze3d.FieldsAreNonnullByDefault;
import com.mojang.blaze3d.MethodsReturnNonnullByDefault;
import io.github.zygzaggaming.zygzagsmod.common.registry.ItemRegistry;
import io.github.zygzaggaming.zygzagsmod.common.registry.SocketedGearType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Predicate;
import java.util.function.Supplier;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
@FieldsAreNonnullByDefault
public enum Socket {
    NONE(() -> Items.AIR, 0xffffff),
    DIAMOND(() -> Items.DIAMOND, 0x4aedd9),
    EMERALD(() -> Items.EMERALD, 0x41f384),
    SKULL(() -> Items.SKELETON_SKULL, 0xd3d3d3),
    WITHER_SKULL(() -> Items.WITHER_SKELETON_SKULL, 0x515353),
    AMETHYST(() -> Items.AMETHYST_SHARD, 0xcfa0f3);

    public final Supplier<Item> itemSupplier;
    public final int color;
    public final Predicate<SocketedGearType> gearTypeFilter;

    Socket(Supplier<Item> itemSupplier, int color) {
        this.itemSupplier = itemSupplier;
        this.color = color;
        this.gearTypeFilter = (it) -> true;
    }

    Socket(Supplier<Item> itemSupplier, int color, Predicate<SocketedGearType> gearTypeFilter) {
        this.itemSupplier = itemSupplier;
        this.color = color;
        this.gearTypeFilter = gearTypeFilter;
    }

    public Item item() {
        return itemSupplier.get();
    }

    public Item schematic() {
        return ItemRegistry.SOCKET_SCHEMATICS.get(this).get();
    }

    public String lowerName() {
        return name().toLowerCase();
    }
}
