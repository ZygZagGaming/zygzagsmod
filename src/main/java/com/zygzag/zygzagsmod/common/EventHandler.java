package com.zygzag.zygzagsmod.common;

import com.zygzag.zygzagsmod.common.blockentity.CustomBrushableBlockEntity;
import com.zygzag.zygzagsmod.common.capability.PlayerSightCache;
import com.zygzag.zygzagsmod.common.capability.PlayerSightCacheImpl;
import com.zygzag.zygzagsmod.common.effect.SightEffect;
import com.zygzag.zygzagsmod.common.item.iridium.Socket;
import com.zygzag.zygzagsmod.common.item.iridium.armor.IridiumChestplateItem;
import com.zygzag.zygzagsmod.common.item.iridium.tool.IridiumAxeItem;
import com.zygzag.zygzagsmod.common.item.iridium.tool.IridiumHoeItem;
import com.zygzag.zygzagsmod.common.registries.BlockRegistry;
import com.zygzag.zygzagsmod.common.registries.EnchantmentRegistry;
import com.zygzag.zygzagsmod.common.registries.IridiumGearRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.gossip.GossipType;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BrushItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BrushableBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BrushableBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

import static com.zygzag.zygzagsmod.common.GeneralUtil.ifCapability;
import static com.zygzag.zygzagsmod.common.Main.MODID;

@Mod.EventBusSubscriber(modid = MODID)
@SuppressWarnings("unused")
public class EventHandler {
    @SubscribeEvent
    public static void attachCapabilitiesToPlayer(final AttachCapabilitiesEvent<Entity> event) {
        var entity = event.getObject();
        if (entity instanceof Player player && entity.level().isClientSide) {
            PlayerSightCache backend = new PlayerSightCacheImpl(player);
            LazyOptional<PlayerSightCache> optional = LazyOptional.of(() -> backend);
            ICapabilityProvider provider = new ICapabilityProvider() {
                @Override
                public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
                    if (cap == Main.PLAYER_SIGHT_CACHE) {
                        return optional.cast();
                    }
                    return LazyOptional.empty();
                }
            };

            event.addCapability(new ResourceLocation(MODID, "player_sight_cache"), provider);
        }
    }

    @SubscribeEvent
    public static void onEffect(final MobEffectEvent event) {
        var inst = event.getEffectInstance();
        if (inst == null) return;
        var living = event.getEntity();
        var effect = inst.getEffect();
        if (effect instanceof SightEffect sightEffect && living instanceof Player) ifCapability(
                living,
                Main.PLAYER_SIGHT_CACHE,
                (playerSightCache) -> playerSightCache.update(sightEffect, inst.getAmplifier())
        );
    }

    @SubscribeEvent
    public static void onHurt(final LivingDamageEvent evt) {
        LivingEntity entity = evt.getEntity();
        Level world = entity.level();
        long time = world.dayTime();
        DamageSource source = evt.getSource();
        float amt = evt.getAmount();
        ItemStack chestplateStack = entity.getItemBySlot(EquipmentSlot.CHEST);
        if (chestplateStack.getItem() == IridiumGearRegistry.DIAMOND_SOCKETED_IRIDIUM_CHESTPLATE.get()) {
            AABB box = entity.getBoundingBox().inflate(16.0);
            Object[] blocks = world.getBlockStates(box).toArray();
            HashMap<Block, Integer> map = new HashMap<>();
            int extra = 0;
            for (Object obj : blocks) {
                if (obj instanceof BlockState state && state.is(Tags.Blocks.ORES)) {
                    Block block = state.getBlock();
                    if (map.containsKey(block)) {
                        int n = map.get(block);
                        if (n == 32) {
                            map.put(block, 1);
                            extra++;
                        } else map.put(block, map.get(block) + 1);
                    }
                    else map.put(block, 1);
                }
            }
            extra += map.size();
            evt.setAmount(Math.max(evt.getAmount() - ((float) extra * 0.25f), 0.05f / (extra + 1)));
        }

        if (source.getEntity() != null) {
            Entity attacker = source.getEntity();

            if (attacker instanceof LivingEntity living) {
                ItemStack mainhandStack = living.getMainHandItem();
                Item mainhandItem = mainhandStack.getItem();
                Item chestItem = living.getItemBySlot(EquipmentSlot.CHEST).getItem();

                if (chestplateStack.getItem() == IridiumGearRegistry.WITHER_SKULL_SOCKETED_IRIDIUM_CHESTPLATE.get()) {
                    int th = EnchantmentHelper.getTagEnchantmentLevel(Enchantments.THORNS, chestplateStack);
                    MobEffectInstance effect = new MobEffectInstance(MobEffects.WITHER, 60 * (3 + th), th / 2);
                    living.addEffect(effect);
                }

                if (mainhandItem == IridiumGearRegistry.DIAMOND_SOCKETED_IRIDIUM_SWORD.get()) {
                    int height = attacker.getBlockY();
                    double a = Config.diamondSwordMaxDamageBonus, b = Config.diamondSwordMinDamageBonus,
                            m = (b - a) / 384.0;
                    float damageBonus = (float) (m * (height + 64) + a);
                    evt.setAmount(amt + damageBonus);
                } else if (mainhandItem == IridiumGearRegistry.SKULL_SOCKETED_IRIDIUM_SWORD.get()) {
                    double chance = Config.skullSwordInstakillChance;
                    if (entity.getType().is(Main.BOSS_TAG)) chance = Config.skullSwordInstakillChanceBosses;
                    else if (entity.getType() == EntityType.PLAYER) chance = Config.skullSwordInstakillChancePlayers;
                    if (world.getRandom().nextDouble() <= chance) {
                        evt.setAmount(Float.MAX_VALUE);
                    }
                    //System.out.println("chance " + chance);
                } else if (mainhandItem == IridiumGearRegistry.SKULL_SOCKETED_IRIDIUM_HOE.get()) {
                    if (entity.getMobType() == MobType.UNDEAD) {
                        if (entity.getType().is(Main.BOSS_TAG)) evt.setAmount(25f);
                        else evt.setAmount(Float.MAX_VALUE);
                    }
                } else if (mainhandItem == IridiumGearRegistry.AMETHYST_SOCKETED_IRIDIUM_AXE.get()) {
                    if (time < 11834 || time > 22300) evt.setAmount(evt.getAmount() * (float) Config.amethystAxeDamageBonus);
                } else if (mainhandItem == IridiumGearRegistry.AMETHYST_SOCKETED_IRIDIUM_SWORD.get()) {
                    if (time >= 11834 && time <= 22300) evt.setAmount(evt.getAmount() * (float) Config.amethystSwordDamageBonus);
                }

                if (chestItem == IridiumGearRegistry.SKULL_SOCKETED_IRIDIUM_CHESTPLATE.get()) {
                    float heal = amt / 4;
                    living.heal(heal);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onEntityKill(final LivingDeathEvent event) {
        var living = event.getEntity();
        var source = event.getSource();
        var killer = source.getEntity();
        var world = living.level();
        if (killer instanceof Player player) {
            var mainhand = player.getMainHandItem();
            if (mainhand.getItem() instanceof IridiumAxeItem iridaxe
                    && iridaxe.getSocket() == Socket.EMERALD
                    && living.getType().is(Main.VILLAGER_HATED)
            ) {
                var nearby = world.getNearbyEntities(Villager.class, TargetingConditions.DEFAULT, living, living.getBoundingBox().inflate(20.0));
                for (var villager : nearby) {
                    villager.getGossips().add(player.getUUID(), GossipType.MAJOR_POSITIVE, 10);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onXpDrop(final LivingExperienceDropEvent event) {
        var player = event.getAttackingPlayer();
        if (player == null) return;
        var world = player.level();
        var mainhand = player.getMainHandItem();
        var item = mainhand.getItem();
        if (player instanceof ServerPlayer sPlayer && item == IridiumGearRegistry.EMERALD_SOCKETED_IRIDIUM_SWORD.get()) {
            var trades = sPlayer.getStats().getValue(Stats.CUSTOM, Stats.TRADED_WITH_VILLAGER);
            var multiplier = 1 + (Math.log(trades) / Math.log(2.0));
            event.setDroppedExperience((int) (event.getDroppedExperience() * multiplier));
        }
    }

    @SubscribeEvent
    public static void toolModification(final BlockEvent.BlockToolModificationEvent event) {
        var player = event.getPlayer();
        var stack = event.getHeldItemStack();
        var item = stack.getItem();
        var world = event.getLevel();
        var blockpos = event.getPos();
        var rng = world.getRandom();
        if (item instanceof IridiumHoeItem hoe && event.getFinalState().getBlock() == Blocks.DIRT) {
            var socket = hoe.getSocket();
            switch (socket) {
                case DIAMOND -> {
                    int rand = rng.nextInt(1000);
                    if (rand >= 69 && rand <= 73) {
                        ItemStack i;
                        if (rand == 69) {
                            i = Items.DIAMOND.getDefaultInstance();
                        } else if (rand <= 71) {
                            i = Items.EMERALD.getDefaultInstance();
                        } else {
                            i = Items.AMETHYST_SHARD.getDefaultInstance();
                            i.setCount(rng.nextInt(8));
                        }
                        if (world instanceof Level level) {
                            ItemEntity e;
                            if (player != null) e = new ItemEntity(level, player.getX(), player.getY(), player.getZ(), i);
                            else e = new ItemEntity(level, blockpos.getX(), blockpos.getY(), blockpos.getZ(), i);
                            world.addFreshEntity(e);
                        }
                    }
                }
                case EMERALD -> {
                    event.setFinalState(BlockRegistry.BLESSED_SOIL.get().defaultBlockState());
                }
                case AMETHYST -> {
                    event.setFinalState(BlockRegistry.GLOWING_SOIL.get().defaultBlockState());
                }
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        ItemStack chestStack = player.getItemBySlot(EquipmentSlot.CHEST);
        Item chestItem = chestStack.getItem();
        Level world = player.level();
        if (chestItem instanceof IridiumChestplateItem plate) {
            Socket socket = plate.getSocket();
            if (socket == Socket.EMERALD) {
                MobEffectInstance effect = player.getEffect(MobEffects.HERO_OF_THE_VILLAGE);
                if (effect == null || effect.getDuration() <= 5) {
                    player.addEffect(new MobEffectInstance(MobEffects.HERO_OF_THE_VILLAGE, 5, 0, true, false));
                }
            } else if (socket == Socket.AMETHYST) {
                MobEffectInstance effect = player.getEffect(MobEffects.NIGHT_VISION);
                var duration = 10 * 20 + 1;
                if (effect == null || effect.getDuration() <= duration) {
                    player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, duration, 0, true, false));
                }
            }
        }
    }

    @SubscribeEvent
    public static void brush(final LivingEntityUseItemEvent.Tick event) {
        var living = event.getEntity();
        var stack = living.getMainHandItem();
        var world = living.level();
        var remainingUseDuration = living.getUseItemRemainingTicks();
        if (stack.getItem() instanceof BrushItem brush) {
            if (remainingUseDuration >= 0 && living instanceof Player player) {
                HitResult hitresult = brush.calculateHitResult(living);
                if (hitresult instanceof BlockHitResult blockhitresult) {
                    if (hitresult.getType() == HitResult.Type.BLOCK) {
                        int i = brush.getUseDuration(stack) - remainingUseDuration + 1;
                        boolean flag = i % 10 == 5;
                        if (flag) {
                            BlockPos blockpos = blockhitresult.getBlockPos();
                            BlockState blockstate = world.getBlockState(blockpos);
                            HumanoidArm humanoidarm = living.getUsedItemHand() == InteractionHand.MAIN_HAND ? player.getMainArm() : player.getMainArm().getOpposite();
                            brush.spawnDustParticles(world, blockhitresult, blockstate, living.getViewVector(0.0F), humanoidarm);
                            Block $$18 = blockstate.getBlock();
                            SoundEvent soundevent;
                            if ($$18 instanceof BrushableBlock brushableblock) {
                                soundevent = brushableblock.getBrushSound();
                            } else {
                                soundevent = SoundEvents.BRUSH_GENERIC;
                            }

                            world.playSound(player, blockpos, soundevent, SoundSource.BLOCKS);
                            if (!world.isClientSide()) {
                                BlockEntity blockentity = world.getBlockEntity(blockpos);
                                if (blockentity instanceof CustomBrushableBlockEntity brushableblockentity) {
                                    boolean flag1 = brushableblockentity.brush(world.getGameTime(), player, blockhitresult.getDirection());
                                    if (flag1) {
                                        EquipmentSlot equipmentslot = stack.equals(player.getItemBySlot(EquipmentSlot.OFFHAND)) ? EquipmentSlot.OFFHAND : EquipmentSlot.MAINHAND;
                                        stack.hurtAndBreak(1, living, (p_279044_) -> {
                                            p_279044_.broadcastBreakEvent(equipmentslot);
                                        });
                                    }
                                }
                                else if (blockentity instanceof BrushableBlockEntity brushableblockentity) {
                                    boolean flag1 = brush(brushableblockentity, world, world.getGameTime(), player, blockhitresult.getDirection());
                                    if (flag1) {
                                        EquipmentSlot equipmentslot = stack.equals(player.getItemBySlot(EquipmentSlot.OFFHAND)) ? EquipmentSlot.OFFHAND : EquipmentSlot.MAINHAND;
                                        stack.hurtAndBreak(1, living, (p_279044_) -> {
                                            p_279044_.broadcastBreakEvent(equipmentslot);
                                        });
                                    }
                                }
                            }
                        }

                        return;
                    }
                }

                living.releaseUsingItem();
            } else {
                living.releaseUsingItem();
            }
        }
    }

    public static boolean brush(BrushableBlockEntity be, Level level, long pStartTick, Player pPlayer, Direction pHitDirection) {
        if (be.hitDirection == null) {
            be.hitDirection = pHitDirection;
        }

        be.brushCountResetsAtTick = pStartTick + 40L;
        if (pStartTick >= be.coolDownEndsAtTick && level instanceof ServerLevel) {
            be.coolDownEndsAtTick = pStartTick + 10L;
            be.unpackLootTable(pPlayer);
            int i = be.getCompletionState();
            var numTicks = 10 - 2 * EnchantmentHelper.getTagEnchantmentLevel(EnchantmentRegistry.BRUSH_EFFICIENCY_ENCHANTMENT.get(), pPlayer.getMainHandItem());
            if (++be.brushCount >= numTicks) {
                be.brushingCompleted(pPlayer);
                return true;
            } else {
                level.scheduleTick(be.getBlockPos(), be.getBlockState().getBlock(), 40);
                int j = be.getCompletionState();
                if (i != j) {
                    BlockState blockstate = be.getBlockState();
                    BlockState blockstate1 = blockstate.setValue(BlockStateProperties.DUSTED, j);
                    level.setBlock(be.getBlockPos(), blockstate1, 3);
                }

                return false;
            }
        } else {
            return false;
        }
    }
}
