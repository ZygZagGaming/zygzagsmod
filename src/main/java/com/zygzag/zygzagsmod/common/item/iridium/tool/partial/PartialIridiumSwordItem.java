package com.zygzag.zygzagsmod.common.item.iridium.tool.partial;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
public class PartialIridiumSwordItem extends SwordItem {
    int numberOfPlates;
    int maxNumOfPlates;
    public PartialIridiumSwordItem(Tier tier, int damage, float speed, Properties properties, int maxNumOfPlates, int numberOfPlates) {
        super(tier, damage, speed, properties);
        this.numberOfPlates = numberOfPlates;
        this.maxNumOfPlates = maxNumOfPlates;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> text, TooltipFlag flag) {
        super.appendHoverText(stack, world, text, flag);
        text.add(Component.literal(""));
        MutableComponent plated = Component.literal("Plated: ").withStyle(ChatFormatting.GRAY);
        plated.append(Component.literal(numberOfPlates + " / " + maxNumOfPlates).withStyle(ChatFormatting.GOLD));
        text.add(plated);
    }
}
