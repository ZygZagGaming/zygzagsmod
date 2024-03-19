package io.github.zygzaggaming.zygzagsmod.common.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import io.github.zygzaggaming.zygzagsmod.common.Main;
import io.github.zygzaggaming.zygzagsmod.common.entity.FlailProjectile;
import io.github.zygzaggaming.zygzagsmod.common.registry.AttributeRegistry;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class FlailItem extends Item {
    private final Multimap<Attribute, AttributeModifier> defaultModifiers;

    public FlailItem(double attackDamage, double attackSpeed, double flailDamage, Properties properties) {
        super(properties);
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(
                Attributes.ATTACK_DAMAGE,
                new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", attackDamage, AttributeModifier.Operation.ADDITION)
        );
        builder.put(
                Attributes.ATTACK_SPEED,
                new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", attackSpeed, AttributeModifier.Operation.ADDITION)
        );
        builder.put(
                AttributeRegistry.FLAIL_DAMAGE.get(),
                new AttributeModifier(Main.BASE_FLAIL_DAMAGE_UUID, "Weapon modifier", flailDamage, AttributeModifier.Operation.ADDITION)
        );
        defaultModifiers = builder.build();
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        return slot == EquipmentSlot.MAINHAND ? defaultModifiers : super.getAttributeModifiers(slot, stack);
    }

    public void onSwing(Player player, ItemStack stack) {
        if ((stack.getOrCreateTag().contains("custom_model_data") || stack.getOrCreateTag().getInt("custom_model_data") == 0) && player.getAttackStrengthScale(0.5f) >= 1f) {
            Level world = player.level();

            AttributeInstance instance = player.getAttribute(AttributeRegistry.FLAIL_DAMAGE.get());
            assert instance != null;
            float flatDmg = (float) instance.getValue();
            FlailProjectile projectile = new FlailProjectile(world, player, player.getBoundingBox().getCenter(), player.getLookAngle()/*.yRot(-Mth.HALF_PI / 8)*/.scale(3.0), flatDmg, 1f);
            world.addFreshEntity(projectile);

            stack.getOrCreateTag().putInt("custom_model_data", 1);
        }
    }
}
