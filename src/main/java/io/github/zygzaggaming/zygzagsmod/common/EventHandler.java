package io.github.zygzaggaming.zygzagsmod.common;

import io.github.zygzaggaming.zygzagsmod.common.entity.BlazeSentry;
import io.github.zygzaggaming.zygzagsmod.common.entity.HomingWitherSkull;
import io.github.zygzaggaming.zygzagsmod.common.item.iridium.IEffectAttackWeapon;
import io.github.zygzaggaming.zygzagsmod.common.item.iridium.Socket;
import io.github.zygzaggaming.zygzagsmod.common.item.iridium.armor.IridiumChestplateItem;
import io.github.zygzaggaming.zygzagsmod.common.item.iridium.tool.IridiumAxeItem;
import io.github.zygzaggaming.zygzagsmod.common.item.iridium.tool.IridiumHoeItem;
import io.github.zygzaggaming.zygzagsmod.common.item.iridium.tool.IridiumSwordItem;
import io.github.zygzaggaming.zygzagsmod.common.networking.packet.ServerboundPlayerLeftClickEmptyPacket;
import io.github.zygzaggaming.zygzagsmod.common.registry.AttachmentTypeRegistry;
import io.github.zygzaggaming.zygzagsmod.common.registry.AttributeRegistry;
import io.github.zygzaggaming.zygzagsmod.common.registry.BlockRegistry;
import io.github.zygzaggaming.zygzagsmod.common.util.GeneralUtil;
import io.github.zygzaggaming.zygzagsmod.common.util.ModUtil;
import io.github.zygzaggaming.zygzagsmod.common.util.ProjectileImpactEntityEvent;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.Vec3i;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.AABB;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.MovementInputUpdateEvent;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.event.entity.EntityMobGriefingEvent;
import net.neoforged.neoforge.event.entity.item.ItemExpireEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingExperienceDropEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.player.CriticalHitEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.minecraft.world.entity.projectile.Snowball;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@EventBusSubscriber(modid = Main.MODID)
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
    public static void onTick(final LevelTickEvent.Pre event) {
        if (event.getLevel() instanceof ServerLevel world) {
            List<ServerPlayer> players = world.players();
            HashSet<BlockPos> allCauldrons = new HashSet<>();
            for (ServerPlayer player : players) {
                world.getPoiManager()
                        .findAllWithType(
                                (it) -> it.is(PoiTypes.LEATHERWORKER),
                                (cauldron) -> world.getBlockState(cauldron).is(Blocks.CAULDRON),
                                player.blockPosition(),
                                50,
                                PoiManager.Occupancy.ANY
                        ).forEach((it) -> allCauldrons.add(it.getSecond()));
            }
            for (BlockPos cauldron : allCauldrons) if (isCauldronImmersed(cauldron, world)) {
                List<ItemEntity> itemEntitiesInsideCauldron = world.getEntitiesOfClass(ItemEntity.class, new AABB(cauldron));
                int totalItems = 0;
                for (ItemEntity item : itemEntitiesInsideCauldron) totalItems += item.getItem().getCount();
                double smeltingSpeed = Math.min(0.01 / totalItems, 0.004921875 / totalItems + 0.000078125); // percentage of the smelting that gets done every tick
                for (ItemEntity item : itemEntitiesInsideCauldron) {
                    double progress = item.getData(AttachmentTypeRegistry.ITEM_ENTITY_BULK_SMELTING);
                    progress += smeltingSpeed;
                    if (progress < 1) {
                        item.setData(AttachmentTypeRegistry.ITEM_ENTITY_BULK_SMELTING, progress);
                        continue;
                    }
                    if (progress > 2) continue; // failed smelts will stop incrementing

                    SingleRecipeInput container = new SingleRecipeInput(item.getItem());
                    List<RecipeHolder<SmeltingRecipe>> list = world.getRecipeManager().getRecipesFor(RecipeType.SMELTING, container, world);
                    if (list.isEmpty()) continue;
                    SmeltingRecipe recipe = list.getFirst().value();
                    ItemStack output = recipe.assemble(container, world.registryAccess());
                    item.setItem(output.copyWithCount(output.getCount() * item.getItem().getCount()));
                    item.setData(AttachmentTypeRegistry.ITEM_ENTITY_BULK_SMELTING, 0.0);

                    // TODO: particles
                }
            }
        }
    }

    public static boolean isCauldronImmersed(BlockPos cauldron, Level world) {
        Vec3i[] relativePositions = {
                new Vec3i(1, 0, 0),
                new Vec3i(0, 0, 1),
                new Vec3i(-1, 0, 0),
                new Vec3i(0, 0, -1)
        };
        for (Vec3i relative : relativePositions) if (!world.getFluidState(cauldron.offset(relative)).is(Fluids.LAVA)) return false;
        return true;
    }

    @SubscribeEvent
    public static void itemWouldDespawn(final ItemExpireEvent event) {
        ItemEntity item = event.getEntity();
        Level world = item.level();
        BlockPos blockPos = item.blockPosition();
        BlockState stateAtPos = world.getBlockState(blockPos);
        if (stateAtPos.is(Main.WORLD_CONTAINERS) && item.age < 20 * 60 * 20 /* dies after 20 mins */) {
            event.addExtraLife(20 * 60 * 20);
        }
    }

    @SubscribeEvent
    public static void onHurt(final LivingDamageEvent.Pre evt) {
        Optional<HolderLookup.RegistryLookup<Enchantment>> enchantmentLookup = evt.getEntity().level().registryAccess().lookup(Registries.ENCHANTMENT);

        LivingEntity entity = evt.getEntity();
        Level world = entity.level();
        long time = world.dayTime();
        DamageSource source = evt.getSource();
        float amt = evt.getNewDamage();
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
            evt.setNewDamage(Math.max(evt.getNewDamage() - ((float) extra * 0.25f), 0.05f / (extra + 1)));
        }

        if (source.getEntity() != null) {
            Entity attacker = source.getEntity();

            if (attacker instanceof LivingEntity living) {
                ItemStack mainhandStack = living.getMainHandItem();
                Item mainhandItem = mainhandStack.getItem();
                Item chestItem = living.getItemBySlot(EquipmentSlot.CHEST).getItem();
                BlockPos pos = living.blockPosition();

                if (chestplateStack.getItem() instanceof IridiumChestplateItem chestplate && chestplate.getSocket() == Socket.WITHER_SKULL && enchantmentLookup.isPresent()) {
                    enchantmentLookup.get().get(Enchantments.THORNS).ifPresent(thorns -> {
                        int th = EnchantmentHelper.getTagEnchantmentLevel(thorns, chestplateStack);
                        MobEffectInstance effect = new MobEffectInstance(MobEffects.WITHER, 60 * (3 + th), th / 2);
                        living.addEffect(effect);
                    });
                }

                if (mainhandItem instanceof IEffectAttackWeapon effectAttackWeapon) {
                    var map = effectAttackWeapon.effects();
                    for (var entry : map.entries()) {
                        entity.addEffect(entry.getValue());
                    }
                }

                if (mainhandItem instanceof IridiumSwordItem sword && sword.getSocket() == Socket.DIAMOND) {
                    int height = attacker.getBlockY();
                    double a = Config.diamondSwordMaxDamageBonus, b = Config.diamondSwordMinDamageBonus,
                            m = (b - a) / 384.0;
                    float damageBonus = (float) (m * (height + 64) + a);
                    evt.setNewDamage(amt + damageBonus);
                } else if (mainhandItem instanceof IridiumSwordItem sword && sword.getSocket() == Socket.SKULL) {
                    double chance = Config.skullSwordInstakillChance;
                    if (entity.getType().is(Main.BOSS_TAG)) chance = Config.skullSwordInstakillChanceBosses;
                    else if (entity.getType() == EntityType.PLAYER) chance = Config.skullSwordInstakillChancePlayers;
                    if (world.getRandom().nextDouble() <= chance) {
                        evt.setNewDamage(Float.MAX_VALUE);
                    }
                    //System.out.println("chance " + chance);
                } else if (mainhandItem instanceof IridiumHoeItem hoe && hoe.getSocket() == Socket.SKULL) {
                    if (entity.getType().is(EntityTypeTags.UNDEAD)) {
                        if (entity.getType().is(Main.BOSS_TAG)) evt.setNewDamage(25f);
                        else evt.setNewDamage(Float.MAX_VALUE);
                    }
                } else if (mainhandItem instanceof IridiumAxeItem axe && axe.getSocket() == Socket.AMETHYST) {
                    if (GeneralUtil.isExposedToSunlight(pos, world)) {
                        double damageBonus = 1 + Config.amethystAxeDamageBonus * Math.exp(-(time - 12000.0) * (time - 12000.0) / 12000.0);
                        evt.setNewDamage(evt.getNewDamage() * (float) damageBonus);
                    }
                } else if (mainhandItem instanceof IridiumSwordItem sword && sword.getSocket() == Socket.AMETHYST) {
                    if (GeneralUtil.isExposedToSunlight(pos, world)) {
                        double damageBonus = 1 + Config.amethystSwordDamageBonus * Math.exp(-((time + 12000.0) % 24000.0 - 12000.0) * ((time + 12000.0) % 24000.0 - 12000.0) / 12000.0);
                        evt.setNewDamage(evt.getNewDamage() * (float) damageBonus);
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

                if (attacker instanceof Player attackerPlayer && entity instanceof Player attacked) {
                    var amount = evt.getNewDamage() / 4 * (attackerPlayer.getAttributeValue(AttributeRegistry.ARMOR_DURABILITY_REDUCTION) - 1);
                    if (amount > 0) {
                        if (amount < 1) amount = 1;

                        for (int i = 0; i < 4; i++) {
                            ItemStack stack = attacked.getInventory().armor.get(i);
                            if ((!source.is(DamageTypeTags.IS_FIRE) || !stack.has(DataComponents.FIRE_RESISTANT)) && stack.getItem() instanceof ArmorItem) {
                                stack.hurtAndBreak(
                                        (int) amount,
                                        attacked,
                                        EquipmentSlot.values()[i + 2]
                                );
                            }
                        }
                    }
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
                case EMERALD -> event.setFinalState(BlockRegistry.BLESSED_SOIL.value().defaultBlockState());
                case AMETHYST -> event.setFinalState(BlockRegistry.GLOWING_SOIL.value().defaultBlockState());
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Pre event) {
        Player player = event.getEntity();
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

//    @SubscribeEvent // TODO: maybe come back to this feature?
//    public static void breakSpeed(final PlayerEvent.BreakSpeed event) {
//        int multiplier = EnchantmentHelper.getEnchantmentLevel(EnchantmentRegistry.STEADY_ENCHANTMENT.get(), event.getEntity()) + 1;
//        if (!event.getEntity().onGround()) event.setNewSpeed(event.getNewSpeed() * multiplier);
//    }

    @SubscribeEvent
    public static void crit(final CriticalHitEvent event) {
        AttributeInstance inst = event.getEntity().getAttribute(AttributeRegistry.CRIT_DAMAGE);
        if (event.isVanillaCritical() && inst != null) event.setDamageMultiplier((float) inst.getValue());
    }

    @SubscribeEvent
    public static void sprinting(final MovementInputUpdateEvent event) {
        Player player = event.getEntity();
        double dx = player.getX() - player.xOld, dy = player.getY() - player.yOld, dz = player.getZ() - player.zOld;
        AttributeInstance attributeInstance = player.getAttribute(Attributes.MOVEMENT_SPEED);
        if (attributeInstance == null) return;
        attributeInstance.removeModifier(Main.SPRINT_SPEED_ATTRIBUTE_MOVEMENT_SPEED_MODIFIER_ID);
        if (player.isSprinting()) {
            attributeInstance.addTransientModifier(
                    new AttributeModifier(
                            Main.SPRINT_SPEED_ATTRIBUTE_MOVEMENT_SPEED_MODIFIER_ID,
                            player.getAttributeValue(AttributeRegistry.SPRINT_SPEED) - 1,
                            AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
                    )
            );
            //System.out.println("modifiers " + player.getAttribute(Attributes.MOVEMENT_SPEED).getModifiers() + " on " + GeneralUtil.stringCS(player.level()));
            if (!player.isPassenger() && !player.isSwimming() && !player.isEyeInFluidType(Fluids.WATER.getFluidType()) && !player.isInWater() && !player.onClimbable() && player.onGround()) {
                int cmTraveled = Math.round((float) Math.sqrt(dx * dx + dz * dz) * 100);
                float vanillaExhaustion = (float) cmTraveled * 0.001f; // 0.1 per meter
                float newExhaustion = (float) (vanillaExhaustion * player.getAttributeValue(AttributeRegistry.SPRINT_HUNGER_CONSUMPTION));
                player.causeFoodExhaustion(newExhaustion - vanillaExhaustion);
            }
        }
    }

    @SubscribeEvent
    public static void mobGrief(final EntityMobGriefingEvent event) {
        if (event.getEntity() instanceof BlazeSentry) event.setCanGrief(false);
    }

    @SubscribeEvent
    public static void livingEntityTick(final EntityTickEvent.Pre event) {
        if (event.getEntity() instanceof LivingEntity entity) {
            int overheat = entity.getData(AttachmentTypeRegistry.LIVING_ENTITY_OVERHEAT);
            if (overheat >= 7) {
                overheat -= 5;

                entity.setRemainingFireTicks(3 * 20);
                int fireResistance = GeneralUtil.fireResistance(entity);
                float maxDmg = 3;
                float dmg = maxDmg - maxDmg * GeneralUtil.clamp(fireResistance / 10f, 0, 1);
                if (dmg > 0 && (!(entity instanceof Player player) || (!player.isCreative() && !player.isSpectator())) && !entity.getType().fireImmune())
                    entity.hurt(overheatDamage(entity.level().registryAccess()), dmg);
            } else if (entity.tickCount % 5 == 0 && overheat > 0) {
                overheat--;
            }
            ModUtil.setEntityOverheat(entity, overheat);
        }
    }

    public static DamageSource overheatDamage(RegistryAccess registryAccess) {
        return new DamageSource(registryAccess.registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(OVERHEAT_DAMAGE_TYPE));
    }

    public static final ResourceKey<DamageType> OVERHEAT_DAMAGE_TYPE = ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(Main.MODID,  "overheat"));

    @SubscribeEvent
    public static void onSwing(final PlayerInteractEvent.LeftClickEmpty event) {
        ServerboundPlayerLeftClickEmptyPacket packet = new ServerboundPlayerLeftClickEmptyPacket();
        ((LocalPlayer) event.getEntity()).connection.send(packet);
    }

    @SubscribeEvent
    public static void onProjectileImpact(LivingIncomingDamageEvent event) {
        DamageSource source = event.getSource();
        Entity projectile = source.getDirectEntity();
        if (projectile instanceof Snowball) {
            Entity hitEntity = event.getEntity();
            if (hitEntity instanceof BlazeSentry) event.setAmount(3);
        }
    }
}
