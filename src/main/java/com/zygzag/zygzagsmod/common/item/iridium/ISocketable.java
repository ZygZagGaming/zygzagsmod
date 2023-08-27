package com.zygzag.zygzagsmod.common.item.iridium;

import com.zygzag.zygzagsmod.common.item.iridium.tool.*;
import com.zygzag.zygzagsmod.common.registry.EnchantmentRegistry;
import com.zygzag.zygzagsmod.common.registry.IridiumGearRegistry;
import com.zygzag.zygzagsmod.common.registry.ItemRegistry;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;

public interface ISocketable {
    Socket getSocket();

    boolean hasCooldown();

    boolean hasUseAbility();

    default int getCooldown(ItemStack stack, Level world) {
        return 0;
    }

    static void addCooldown(Player player, ItemStack stack, int amount) {
        if (!player.getAbilities().instabuild) {
            int level = EnchantmentHelper.getTagEnchantmentLevel(EnchantmentRegistry.COOLDOWN_ENCHANTMENT.get(), stack);
            player.getCooldowns().addCooldown(stack.getItem(), amount / (level + 1));
        }
    }

    static <I extends Item & ISocketable> void setCooldown(Player player, I socketableItem, ItemStack stack, Level world) {
        addCooldown(player, stack, socketableItem.getCooldown(stack, world));
    }

    default Item getSocketlessForm() {
        if (this instanceof IridiumAxeItem) return IridiumGearRegistry.IRIDIUM_AXE.get();
        //else if (this instanceof IridiumChestplateItem) return IridiumGearRegistry.IRIDIUM_CHESTPLATE.get();
        else if (this instanceof IridiumHoeItem) return IridiumGearRegistry.IRIDIUM_HOE.get();
        else if (this instanceof IridiumPickaxeItem) return IridiumGearRegistry.IRIDIUM_PICKAXE.get();
        //else if (this instanceof IridiumScepterItem) return IridiumGearRegistry.IRIDIUM_SCEPTER.get();
        else if (this instanceof IridiumShovelItem) return IridiumGearRegistry.IRIDIUM_SHOVEL.get();
        else if (this instanceof IridiumSwordItem) return IridiumGearRegistry.IRIDIUM_SWORD.get();
        // else if (this instanceof IridiumRingItem) return Registry.IRIDIUM_RING.get(); // foreshadowing
        return ItemRegistry.IRIDIUM_PLATING.get(); // should never happen
    }
}