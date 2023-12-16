package com.zygzag.zygzagsmod.common;

import com.zygzag.zygzagsmod.common.block.entity.CustomBrushableBlockEntity;
import com.zygzag.zygzagsmod.common.effect.SightEffect;
import com.zygzag.zygzagsmod.common.enchant.CustomEnchantment;
import com.zygzag.zygzagsmod.common.enchant.SpringsEnchantment;
import com.zygzag.zygzagsmod.common.entity.HomingWitherSkull;
import com.zygzag.zygzagsmod.common.item.iridium.IEffectAttackWeapon;
import com.zygzag.zygzagsmod.common.item.iridium.ISocketable;
import com.zygzag.zygzagsmod.common.item.iridium.Socket;
import com.zygzag.zygzagsmod.common.item.iridium.armor.IridiumChestplateItem;
import com.zygzag.zygzagsmod.common.item.iridium.tool.IridiumAxeItem;
import com.zygzag.zygzagsmod.common.item.iridium.tool.IridiumHoeItem;
import com.zygzag.zygzagsmod.common.item.iridium.tool.IridiumSwordItem;
import com.zygzag.zygzagsmod.common.registry.AttributeRegistry;
import com.zygzag.zygzagsmod.common.registry.BlockRegistry;
import com.zygzag.zygzagsmod.common.registry.EnchantmentRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.Difficulty;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.item.ItemEntity;
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
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.event.ItemAttributeModifierEvent;
import net.neoforged.neoforge.event.TickEvent;
import net.neoforged.neoforge.event.entity.living.*;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

import java.util.HashMap;

import static com.zygzag.zygzagsmod.common.Main.MODID;
import static com.zygzag.zygzagsmod.common.util.GeneralUtil.isExposedToSunlight;

@Mod.EventBusSubscriber(modid = MODID)
@SuppressWarnings("unused")
public class EventHandler {
//    public static float DIURNAL_MULTIPLIER = 1f, NOCTURNAL_MULTIPLIER = 1f;
//    @SubscribeEvent
//    public static void tick(final TickEvent.LevelTickEvent event) {
//        if (event.level.dimensionType().effectsLocation() == BuiltinDimensionTypes.OVERWORLD_EFFECTS) {
//            long time = event.level.dayTime();
//            DIURNAL_MULTIPLIER = (float) (1 + Config.amethystAxeDamageBonus * Math.exp(-(time - 12000.0) * (time - 12000.0) / 12000.0));
//            NOCTURNAL_MULTIPLIER = (float) (1 + Config.amethystSwordDamageBonus * Math.exp(-((time + 12000.0) % 24000.0 - 12000.0) * ((time + 12000.0) % 24000.0 - 12000.0) / 12000.0));
//        }
//    }

    @SubscribeEvent
    public static void serverStarting(final ServerStartingEvent event) {
        System.out.println(BuiltInRegistries.REGISTRY.keySet());
    }

    @SubscribeEvent
    public static void onTick(final TickEvent.PlayerTickEvent event) {
        var player = event.player;
        if (player.level().isClientSide) for (var effectInstance : player.getActiveEffects()) {
            if (effectInstance == null) return;
            var effect = effectInstance.getEffect();
            if (effect instanceof SightEffect sightEffect)
                Main.CURRENT_PLAYER_CACHE.update(sightEffect, player, effectInstance.getAmplifier());
        }
    }

    @SubscribeEvent
    public static void onHurt(final LivingDamageEvent evt) {
        LivingEntity entity = evt.getEntity();
        Level world = entity.level();
        long time = world.dayTime();
        DamageSource source = evt.getSource();
        float amt = evt.getAmount();
        ItemStack chestplateStack = entity.getItemBySlot(EquipmentSlot.CHEST);
        if (chestplateStack.getItem() instanceof IridiumChestplateItem chestplate && chestplate.getSocket() == Socket.DIAMOND) {
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
                    } else map.put(block, 1);
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
                BlockPos pos = living.blockPosition();

                if (chestplateStack.getItem() instanceof IridiumChestplateItem chestplate && chestplate.getSocket() == Socket.WITHER_SKULL) {
                    int th = EnchantmentHelper.getTagEnchantmentLevel(Enchantments.THORNS, chestplateStack);
                    MobEffectInstance effect = new MobEffectInstance(MobEffects.WITHER, 60 * (3 + th), th / 2);
                    living.addEffect(effect);
                }

                if (mainhandItem instanceof IEffectAttackWeapon effectAttackWeapon) {
                    var map = effectAttackWeapon.effects();
                    for (var entry : map.entries()) {
                        entity.addEffect(entry.getValue());
                    }
                }

                for (var entry : mainhandStack.getAllEnchantments().entrySet())
                    if (entry.getKey() instanceof CustomEnchantment enchantment)
                        for (var effect : enchantment.attackEffects())
                            entity.addEffect(effect);

                if (mainhandItem instanceof IridiumSwordItem sword && sword.getSocket() == Socket.DIAMOND) {
                    int height = attacker.getBlockY();
                    double a = Config.diamondSwordMaxDamageBonus, b = Config.diamondSwordMinDamageBonus,
                            m = (b - a) / 384.0;
                    float damageBonus = (float) (m * (height + 64) + a);
                    evt.setAmount(amt + damageBonus);
                } else if (mainhandItem instanceof IridiumSwordItem sword && sword.getSocket() == Socket.SKULL) {
                    double chance = Config.skullSwordInstakillChance;
                    if (entity.getType().is(Main.BOSS_TAG)) chance = Config.skullSwordInstakillChanceBosses;
                    else if (entity.getType() == EntityType.PLAYER) chance = Config.skullSwordInstakillChancePlayers;
                    if (world.getRandom().nextDouble() <= chance) {
                        evt.setAmount(Float.MAX_VALUE);
                    }
                    //System.out.println("chance " + chance);
                } else if (mainhandItem instanceof IridiumHoeItem hoe && hoe.getSocket() == Socket.SKULL) {
                    if (entity.getMobType() == MobType.UNDEAD) {
                        if (entity.getType().is(Main.BOSS_TAG)) evt.setAmount(25f);
                        else evt.setAmount(Float.MAX_VALUE);
                    }
                } else if (mainhandItem instanceof IridiumAxeItem axe && axe.getSocket() == Socket.AMETHYST) {
                    if (isExposedToSunlight(pos, world)) {
                        double damageBonus = 1 + Config.amethystAxeDamageBonus * Math.exp(-(time - 12000.0) * (time - 12000.0) / 12000.0);
                        evt.setAmount(evt.getAmount() * (float) damageBonus);
                    }
                } else if (mainhandItem instanceof IridiumSwordItem sword && sword.getSocket() == Socket.AMETHYST) {
                    if (isExposedToSunlight(pos, world)) {
                        double damageBonus = 1 + Config.amethystSwordDamageBonus * Math.exp(-((time + 12000.0) % 24000.0 - 12000.0) * ((time + 12000.0) % 24000.0 - 12000.0) / 12000.0);
                        evt.setAmount(evt.getAmount() * (float) damageBonus);
                    }
                } else if (mainhandItem instanceof IridiumAxeItem axe && axe.getSocket() == Socket.EMERALD) {
                    if (living instanceof Player player && !player.getCooldowns().isOnCooldown(axe)) {
                        LightningBolt bolt = new LightningBolt(EntityType.LIGHTNING_BOLT, world);
                        if (living instanceof ServerPlayer sPlayer) bolt.setCause(sPlayer);
                        bolt.moveTo(entity.position());
                        world.addFreshEntity(bolt);
                        axe.addCooldown(player, mainhandStack);
                    }
                }

                if (chestItem instanceof IridiumChestplateItem chestplate && chestplate.getSocket() == Socket.SKULL) {
                    float heal = (float) Math.log(amt) / 4;
                    if (heal >= 0) living.heal(heal);
                }
            } else if (attacker instanceof HomingWitherSkull hws) {
                int i = 0;
                if (world.getDifficulty() == Difficulty.NORMAL) {
                    i = 7;
                } else if (world.getDifficulty() == Difficulty.HARD) {
                    i = 25;
                }

                if (i > 0) {
                    entity.addEffect(new MobEffectInstance(MobEffects.WITHER, 20 * i, 1), hws.getOwner());
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
        if (player instanceof ServerPlayer sPlayer && item instanceof IridiumSwordItem sword && sword.getSocket() == Socket.EMERALD) {
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
                            if (player != null)
                                e = new ItemEntity(level, player.getX(), player.getY(), player.getZ(), i);
                            else e = new ItemEntity(level, blockpos.getX(), blockpos.getY(), blockpos.getZ(), i);
                            world.addFreshEntity(e);
                        }
                    }
                }
                case EMERALD -> event.setFinalState(BlockRegistry.BLESSED_SOIL.get().defaultBlockState());
                case AMETHYST -> event.setFinalState(BlockRegistry.GLOWING_SOIL.get().defaultBlockState());
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
                MobEffectInstance nightVision = player.getEffect(MobEffects.NIGHT_VISION);
                var nightVisionDuration = 10 * 20 + 1;
                if (nightVision == null || nightVision.getDuration() <= nightVisionDuration) {
                    player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, nightVisionDuration, 0, true, false));
                }

                var entities = world.getEntitiesOfClass(LivingEntity.class, player.getBoundingBox().inflate(Config.amethystChestplateRange), (it) -> !it.is(player));
                for (LivingEntity entity : entities) {
                    MobEffectInstance preexisting = entity.getEffect(MobEffects.GLOWING);
                    if (preexisting == null || preexisting.getDuration() <= 1) {
                        MobEffectInstance glowing = new MobEffectInstance(MobEffects.GLOWING, 5, 0, true, true);
                        entity.addEffect(glowing);
                    }
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
                HitResult hitresult = brush.calculateHitResult(player);
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
                                } else if (blockentity instanceof BrushableBlockEntity brushableblockentity) {
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

            }
            living.releaseUsingItem();
        }
    }

    public static boolean brush(BrushableBlockEntity be, Level level, long pStartTick, Player pPlayer, Direction pHitDirection) {
        if (be.getHitDirection() == null) {
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

    @SubscribeEvent
    public static void itemAttrModifier(final ItemAttributeModifierEvent event) {
        ItemStack stack = event.getItemStack();
        for (var entry : stack.getAllEnchantments().entrySet()) if (entry.getKey() instanceof CustomEnchantment customEnchant) {
            int level = entry.getValue();
            var attributeMap = customEnchant.attributes(event.getSlotType(), level);
            for (var attributeModifierEntry : attributeMap.entries()) {
                event.addModifier(attributeModifierEntry.getKey(), attributeModifierEntry.getValue());
                //System.out.println(attributeModifierEntry.getValue().getAmount() + (Minecraft.getInstance().isLocalServer() ? " local server" : " not local server"));
            }
        }

        if (stack.getItem() instanceof ISocketable socketable) {
            var attributeMap = socketable.attributes(event.getSlotType());
            for (var attributeModifierEntry : attributeMap.entries()) event.addModifier(attributeModifierEntry.getKey(), attributeModifierEntry.getValue());
        }
    }

    @SubscribeEvent
    public static void breakSpeed(final PlayerEvent.BreakSpeed event) {
        int multiplier = EnchantmentHelper.getEnchantmentLevel(EnchantmentRegistry.STEADY_ENCHANTMENT.get(), event.getEntity()) + 1;
        if (!event.getEntity().onGround()) event.setNewSpeed(event.getNewSpeed() * multiplier);
    }

    @SubscribeEvent
    public static void jump(final LivingEvent.LivingJumpEvent event) {
        LivingEntity entity = event.getEntity();
        AttributeInstance instance = entity.getAttribute(AttributeRegistry.JUMP_POWER.get());
        if (instance != null) {
            instance.setBaseValue(0.42F * entity.getBlockJumpFactor() + entity.getJumpBoostPower());
            var delta = entity.getDeltaMovement();
            entity.setDeltaMovement(delta.x, instance.getValue(), delta.z);
        }
        if (entity.level().isClientSide) { // TODO: fix this wacky bullshit
            var delta = entity.getDeltaMovement();
            var level = entity.getItemBySlot(EquipmentSlot.LEGS).getAllEnchantments().get(EnchantmentRegistry.SPRINGS_ENCHANTMENT.get());
            if (level != null && level != 0) entity.setDeltaMovement(delta.x, (0.42F * entity.getBlockJumpFactor() + entity.getJumpBoostPower()) * (SpringsEnchantment.JUMP_HEIGHT_MULTIPLIERS[level - 1] + 1), delta.z);
        }
    }

    @SubscribeEvent
    public static void fall(final LivingFallEvent event) {
        event.setDistance((float) (event.getDistance() - 7.14f * event.getEntity().getAttributeValue(AttributeRegistry.JUMP_POWER.get())) + 3);
    }
}
