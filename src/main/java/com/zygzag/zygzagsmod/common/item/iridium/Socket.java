package com.zygzag.zygzagsmod.common.item.iridium;

import com.mojang.blaze3d.FieldsAreNonnullByDefault;
import com.mojang.blaze3d.MethodsReturnNonnullByDefault;
import net.minecraft.world.item.Item;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
@FieldsAreNonnullByDefault
public class Socket {

    public final Item item;
    public final int color;
    public final String name;

    public Socket(Item item, int color, String name) {
        this.item = item;
        this.color = color;
        this.name = name;
    }
}
