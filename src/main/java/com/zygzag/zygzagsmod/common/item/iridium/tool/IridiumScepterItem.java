package com.zygzag.zygzagsmod.common.item.iridium.tool;

import com.zygzag.zygzagsmod.common.Config;
import com.zygzag.zygzagsmod.common.entity.HomingWitherSkull;
import com.zygzag.zygzagsmod.common.item.iridium.ISocketable;
import com.zygzag.zygzagsmod.common.item.iridium.Socket;
import com.zygzag.zygzagsmod.common.registries.EnchantmentRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Vec3i;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
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
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import javax.annotation.Nullable;
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
                aabb = new AABB(player.blockPosition().subtract(new Vec3i(20, 20, 20)), player.blockPosition().offset(new Vec3i(20, 20, 20)));
                List<ItemEntity> items = world.getEntities(EntityType.ITEM, aabb, (v) -> true);
                for (ItemEntity v : items) {
                    if (v.getItem().is(ItemTags.create(new ResourceLocation("zygzagsmod:diamond_scepter_consumable")))) {
                        int amount = v.getItem().getCount() / 8;
                        ExperienceOrb orb = new ExperienceOrb(world, v.getX(), v.getY(), v.getZ(), amount);
                        world.addFreshEntity(orb);
                        v.kill();
                        item.hurtAndBreak(1, player, (p) -> { });
                    }
                }
            }
            case EMERALD -> {
                aabb = new AABB(player.blockPosition().subtract(new Vec3i(6, 3, 6)), player.blockPosition().offset(new Vec3i(6, 3, 6)));
                List<Villager> villagers = world.getEntities(EntityType.VILLAGER, aabb, (v) -> true);
                for (Villager v : villagers) {
                    MerchantOffers offers = v.getOffers();
                    for (int i = 0; i < offers.toArray().length; i++) {
                        MerchantOffer offer = offers.get(i);
                        while (offer.isOutOfStock()) {
                            offer.increaseUses();
                            item.hurtAndBreak(1, player, (p) -> { });
                        }
                    }
                }
                ISocketable.setCooldown(player, this, item, world);
            }
            case WITHER_SKULL -> {
                if (!player.getCooldowns().isOnCooldown(this)) {
                    var power = player.getLookAngle().scale(1.5);
                    var skull = new HomingWitherSkull(world, player, power.x, power.y, power.z);
                    //skull.shootFromRotation(player, (float) player.getLookAngle().x, (float) player.getLookAngle().y, (float) player.getLookAngle().z, 3, 0);
                    world.addFreshEntity(skull);
                    ISocketable.setCooldown(player, this, item, world);
                }
            }
            case AMETHYST -> {
                if (!player.getCooldowns().isOnCooldown(this)) {
                    aabb = player.getBoundingBox().inflate(40.0);
                    List<LivingEntity> entities = world.getEntitiesOfClass(LivingEntity.class, aabb, (v) -> v != player);
                    for (LivingEntity e : entities) {
                        e.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 200));
                        e.addEffect(new MobEffectInstance(MobEffects.GLOWING, 200));
                        item.hurtAndBreak(1, player, (p) -> { });
                    }
                    ISocketable.setCooldown(player, this, item, world);
                }
            }
            case SKULL -> {
                world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.LINGERING_POTION_THROW, SoundSource.NEUTRAL, 0.5F, 0.4F / (player.getRandom().nextFloat() * 0.4F + 0.8F));
                if (!world.isClientSide) {
                    ThrownPotion thrownpotion = new ThrownPotion(world, player);
                    ItemStack stack = Items.LINGERING_POTION.getDefaultInstance();
                    PotionUtils.setPotion(stack, Potions.LONG_POISON);
                    thrownpotion.setItem(stack);
                    thrownpotion.shootFromRotation(player, player.getXRot(), player.getYRot(), -20.0F, 0.5F, 1.0F);
                    world.addFreshEntity(thrownpotion);
                }

                player.awardStat(Stats.ITEM_USED.get(this));
                if (!player.getAbilities().instabuild) {
                    item.hurtAndBreak(4, player, (e) -> { });
                }
                item.hurtAndBreak(1, player, (p) -> { });
                ISocketable.setCooldown(player, this, item, world);
            }
        }
        return InteractionResultHolder.consume(item);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> text, TooltipFlag flag) {
        Socket s = getSocket();
        Item i = s.i;
        MutableComponent m;
        if (s != Socket.NONE && world != null) {
            MutableComponent t = Component.translatable("socketed.zygzagsmod").withStyle(ChatFormatting.GRAY);
            t.append(Component.literal(": ").withStyle(ChatFormatting.GRAY));
            t.append(((MutableComponent) i.getName(i.getDefaultInstance())).withStyle(ChatFormatting.GOLD));
            text.add(t);

            Socket socket = getSocket();
            text.add(Component.literal(""));
            m = Minecraft.getInstance().options.keyUse.getKey().getDisplayName().copy().withStyle(ChatFormatting.GRAY);
            m.append(Component.literal( ": ").withStyle(ChatFormatting.GRAY));
            m.append(Component.translatable("use_ability.zygzagsmod.scepter." + socket.name().toLowerCase()).withStyle(ChatFormatting.GOLD));
            text.add(m);
            text.add(Component.translatable("description.use_ability.zygzagsmod.scepter." + socket.name().toLowerCase()));
            if (hasCooldown()) {
                MutableComponent comp = Component.translatable("zygzagsmod.cooldown").withStyle(ChatFormatting.GRAY);
                comp.append(Component.literal(": ").withStyle(ChatFormatting.GRAY));
                comp.append(Component.literal(getCooldown(stack, world) / 20f + " ").withStyle(ChatFormatting.GOLD));
                comp.append(Component.translatable("zygzagsmod.seconds").withStyle(ChatFormatting.GRAY));
                //text.add(Component.literal("\n"));
                text.add(comp);
            }
        }
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
    public int getCooldown(ItemStack stack, Level world) {
        int cooldownLevel = EnchantmentHelper.getTagEnchantmentLevel(EnchantmentRegistry.COOLDOWN_ENCHANTMENT.get(), stack);
        switch (socket) {
            case EMERALD -> {
                return Config.emeraldScepterCooldown / (cooldownLevel + 1);
            }
            case SKULL -> {
                return Config.skullScepterCooldown / (cooldownLevel + 1);
            }
            case WITHER_SKULL -> {
                return Config.witherSkullScepterCooldown / (cooldownLevel + 1);
            }
            case AMETHYST -> {
                return Config.amethystScepterCooldown / (cooldownLevel + 1);
            }
        }
        return 0;
    }
}