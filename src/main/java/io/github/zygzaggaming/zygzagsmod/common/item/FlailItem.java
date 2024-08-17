package io.github.zygzaggaming.zygzagsmod.common.item;

import io.github.zygzaggaming.zygzagsmod.common.entity.FlailProjectile;
import io.github.zygzaggaming.zygzagsmod.common.registry.AttributeRegistry;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomModelData;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.Level;

import javax.annotation.ParametersAreNonnullByDefault;

import static io.github.zygzaggaming.zygzagsmod.common.Main.MODID;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class FlailItem extends Item {
    public FlailItem(double attackDamage, double attackSpeed, double flailDamage, Properties properties) {
        super(
                properties.component(
                        DataComponents.ATTRIBUTE_MODIFIERS,
                        ItemAttributeModifiers.builder().add(
                                Attributes.ATTACK_DAMAGE,
                                new AttributeModifier(ResourceLocation.fromNamespaceAndPath(MODID, "weapon"), attackDamage, AttributeModifier.Operation.ADD_VALUE),
                                EquipmentSlotGroup.HAND
                        ).add(
                                Attributes.ATTACK_SPEED,
                                new AttributeModifier(ResourceLocation.fromNamespaceAndPath(MODID, "weapon"), attackSpeed, AttributeModifier.Operation.ADD_VALUE),
                                EquipmentSlotGroup.HAND
                        ).add(
                                AttributeRegistry.FLAIL_DAMAGE,
                                new AttributeModifier(ResourceLocation.fromNamespaceAndPath(MODID, "weapon"), flailDamage, AttributeModifier.Operation.ADD_VALUE),
                                EquipmentSlotGroup.HAND
                        ).build()
                )
        );
    }

    public void onSwing(Player player, ItemStack stack) {
        if ((!stack.has(DataComponents.CUSTOM_MODEL_DATA) || stack.get(DataComponents.CUSTOM_MODEL_DATA) == CustomModelData.DEFAULT) && player.getAttackStrengthScale(0.5f) >= 1f) {
            Level world = player.level();

            AttributeInstance instance = player.getAttribute(AttributeRegistry.FLAIL_DAMAGE);
            assert instance != null;
            float flatDmg = (float) instance.getValue();
            FlailProjectile projectile = new FlailProjectile(world, player, player.getBoundingBox().getCenter(), player.getLookAngle()/*.yRot(-Mth.HALF_PI / 8)*/.scale(3.0), flatDmg, 1f);
            world.addFreshEntity(projectile);

            stack.set(DataComponents.CUSTOM_MODEL_DATA, new CustomModelData(1));
        }
    }
}
