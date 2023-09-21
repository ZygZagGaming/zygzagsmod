package com.zygzag.zygzagsmod.common.item.iridium;

import com.mojang.blaze3d.FieldsAreNonnullByDefault;
import com.mojang.blaze3d.MethodsReturnNonnullByDefault;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
@FieldsAreNonnullByDefault
public enum Socket {
    NONE(Items.AIR, 0xffffff),
    DIAMOND(Items.DIAMOND, 0x4aedd9),
    EMERALD(Items.EMERALD, 0x41f384),
    SKULL(Items.SKELETON_SKULL, 0xd3d3d3),
    WITHER_SKULL(Items.WITHER_SKELETON_SKULL, 0x515353),
    AMETHYST(Items.AMETHYST_SHARD, 0xcfa0f3);

    public final Item i;
    public final int color;

    Socket(Item i, int color) {
        this.i = i;
        this.color = color;
    }

    
}
