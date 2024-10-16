package io.github.zygzaggaming.zygzagsmod.common.item.iridium.tool;

import io.github.zygzaggaming.zygzagsmod.common.Config;
import io.github.zygzaggaming.zygzagsmod.common.entity.HomingWitherSkull;
import io.github.zygzaggaming.zygzagsmod.common.item.iridium.ISocketable;
import io.github.zygzaggaming.zygzagsmod.common.item.iridium.Socket;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class IridiumScepterItem extends Item implements ISocketable {
    Socket socket;

    public IridiumScepterItem(Properties properties, Socket socket) {
        super(properties.durability(2048));
        this.socket = socket;
    }

    @Override
    public Socket getSocket() {
        return socket;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack item = player.getItemInHand(hand);
        Socket socket = getSocket();
        AABB aabb;
        switch (socket) {
            case NONE -> {
                return InteractionResultHolder.fail(item);
            }
            case DIAMOND -> {
                int x = player.blockPosition().getX(), y = player.blockPosition().getY(), z = player.blockPosition().getZ();
                aabb = new AABB(x - 20, y - 20, z - 20, x + 20, y + 20, z + 20);
                List<ItemEntity> items = world.getEntities(EntityType.ITEM, aabb, (v) -> true);
                for (ItemEntity v : items) {
                    if (v.getItem().is(ItemTags.create(ResourceLocation.parse("zygzagsmod:diamond_scepter_consumable")))) {
                        int amount = v.getItem().getCount() / 8;
                        ExperienceOrb orb = new ExperienceOrb(world, v.getX(), v.getY(), v.getZ(), amount);
                        world.addFreshEntity(orb);
                        v.kill();
                        item.hurtAndBreak(1, player, EquipmentSlot.MAINHAND);
                    }
                }
            }
            case EMERALD -> {
                int x = player.blockPosition().getX(), y = player.blockPosition().getY(), z = player.blockPosition().getZ();
                aabb = new AABB(x - 6, y - 3, z - 6, x + 6, y + 3, z + 6);
                List<Villager> villagers = world.getEntities(EntityType.VILLAGER, aabb, (v) -> true);
                for (Villager v : villagers) {
                    MerchantOffers offers = v.getOffers();
                    for (int i = 0; i < offers.toArray().length; i++) {
                        MerchantOffer offer = offers.get(i);
                        while (offer.isOutOfStock()) {
                            offer.increaseUses();
                            item.hurtAndBreak(1, player, EquipmentSlot.MAINHAND);
                        }
                    }
                }
                addCooldown(player, item);
            }
            case WITHER_SKULL -> {
                if (!player.getCooldowns().isOnCooldown(this)) {
                    var power = player.getLookAngle().scale(1.5);
                    var skull = new HomingWitherSkull(world, player, 1.5);
                    //skull.shootFromRotation(player, (float) player.getLookAngle().x, (float) player.getLookAngle().y, (float) player.getLookAngle().z, 3, 0);
                    world.addFreshEntity(skull);
                    addCooldown(player, item);
                }
            }
            case AMETHYST -> {
                if (!player.getCooldowns().isOnCooldown(this)) {
                    aabb = player.getBoundingBox().inflate(40.0);
                    List<LivingEntity> entities = world.getEntitiesOfClass(LivingEntity.class, aabb, (v) -> v != player);
                    for (LivingEntity e : entities) {
                        e.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 200));
                        e.addEffect(new MobEffectInstance(MobEffects.GLOWING, 200));
                        item.hurtAndBreak(1, player, EquipmentSlot.MAINHAND);
                    }
                    addCooldown(player, item);
                }
            }
            case SKULL -> {
                world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.LINGERING_POTION_THROW, SoundSource.NEUTRAL, 0.5F, 0.4F / (player.getRandom().nextFloat() * 0.4F + 0.8F));
                if (!world.isClientSide) {
                    ThrownPotion thrownpotion = new ThrownPotion(world, player);
                    ItemStack potionItem = Items.LINGERING_POTION.getDefaultInstance();
                    potionItem.set(DataComponents.POTION_CONTENTS, new PotionContents(Potions.LONG_POISON));
                    thrownpotion.setItem(potionItem);
                    thrownpotion.shootFromRotation(player, player.getXRot(), player.getYRot(), -20.0F, 0.5F, 1.0F);
                    world.addFreshEntity(thrownpotion);
                }

                player.awardStat(Stats.ITEM_USED.get(this));
                if (!player.getAbilities().instabuild) {
                    item.hurtAndBreak(4, player, EquipmentSlot.MAINHAND);
                }
                item.hurtAndBreak(1, player, EquipmentSlot.MAINHAND);
                addCooldown(player, item);
            }
        }
        return InteractionResultHolder.consume(item);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext ctx, List<Component> text, TooltipFlag flag) {
        appendHoverText(stack, ctx, text, flag, "scepter");
    }

    @Override
    public boolean hasCooldown() {
        return socket != Socket.NONE && socket != Socket.DIAMOND;
    }

    @Override
    public boolean hasUseAbility() {
        return true;
    }

    @Override
    public int getBaseCooldown(ItemStack stack, Level world) {
        switch (socket) {
            case EMERALD -> {
                return Config.emeraldScepterCooldown;
            }
            case SKULL -> {
                return Config.skullScepterCooldown;
            }
            case WITHER_SKULL -> {
                return Config.witherSkullScepterCooldown;
            }
            case AMETHYST -> {
                return Config.amethystScepterCooldown;
            }
        }
        return 0;
    }
}