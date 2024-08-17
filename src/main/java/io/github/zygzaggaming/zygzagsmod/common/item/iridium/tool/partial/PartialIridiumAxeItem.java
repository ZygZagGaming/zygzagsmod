package io.github.zygzaggaming.zygzagsmod.common.item.iridium.tool.partial;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
public class PartialIridiumAxeItem extends AxeItem {
    int numberOfPlates;
    int maxNumOfPlates;

    //TODO: fix dmg and speed
    public PartialIridiumAxeItem(Tier tier, Properties properties, int maxNumOfPlates, int numberOfPlates) {
        super(tier, properties);
        this.numberOfPlates = numberOfPlates;
        this.maxNumOfPlates = maxNumOfPlates;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext ctx, List<Component> text, TooltipFlag flag) {
        super.appendHoverText(stack, ctx, text, flag);
        text.add(Component.literal(""));
        MutableComponent plated = Component.literal("Plated: ").withStyle(ChatFormatting.GRAY);
        plated.append(Component.literal(numberOfPlates + " / " + maxNumOfPlates).withStyle(ChatFormatting.GOLD));
        text.add(plated);
    }
}