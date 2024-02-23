package io.github.zygzaggaming.zygzagsmod.mixin;

import net.minecraft.world.item.BrushItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BrushItem.class)
public class BrushItemMixin extends Item {
    public BrushItemMixin(Properties p_41383_) {
        super(p_41383_);
    }

    @Override
    public int getEnchantmentValue(ItemStack stack) {
        return 1;
    }

    @Override
    public int getEnchantmentValue() {
        return 1;
    }
}
