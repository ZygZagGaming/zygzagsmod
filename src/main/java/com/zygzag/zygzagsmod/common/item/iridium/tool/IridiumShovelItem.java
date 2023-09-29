package com.zygzag.zygzagsmod.common.item.iridium.tool;

import com.zygzag.zygzagsmod.common.Config;
import com.zygzag.zygzagsmod.common.Main;
import com.zygzag.zygzagsmod.common.entity.PlayerAlliedSkeleton;
import com.zygzag.zygzagsmod.common.item.iridium.ISocketable;
import com.zygzag.zygzagsmod.common.item.iridium.Socket;
import com.zygzag.zygzagsmod.common.util.GeneralUtil;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BellBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class IridiumShovelItem extends ShovelItem implements ISocketable {
    Socket socket;
    public IridiumShovelItem(Tier tier, float damage, float speed, Properties properties, Socket socket) {
        super(tier, damage, speed, properties);
        this.socket = socket;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> text, TooltipFlag flag) {
        appendHoverText(stack, world, text, flag, "shovel");
    }

    @Override
    public boolean hasCooldown() {
        return socket == Socket.EMERALD || socket == Socket.SKULL || socket == Socket.WITHER_SKULL;
    }

    @Override
    public boolean hasUseAbility() {
        return hasCooldown();
    }

    @Override
    public int getBaseCooldown(ItemStack stack, Level world) {
        switch (socket) {
            case EMERALD -> {
                return Config.emeraldShovelCooldown;
            }
            case SKULL -> {
                return Config.skullShovelCooldown;
            }
            case WITHER_SKULL -> {
                return Config.witherSkullShovelCooldown;
            }
        }
        return 0;
    }

    @Override
    public Socket getSocket() {
        return socket;
    }

    @Override
    public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity user) {
        if (state.is(Main.VEINMINEABLE) && (!(user instanceof Player) || user.isShiftKeyDown() && !((Player) user).getCooldowns().isOnCooldown(this)) && stack.getItem() instanceof IridiumShovelItem shovel && shovel.getSocket() == Socket.DIAMOND) {
            int numDestroyed = 1;
            List<BlockPos> arr = Arrays.stream(getNeighboringBlocks(pos)).collect(Collectors.toList());
            if (level instanceof ServerLevel sLevel) {
                while (numDestroyed <= Config.diamondShovelBlockCount) {
                    ArrayList<BlockPos> tempList = new ArrayList<>();
                    for (BlockPos pos1 : arr) {
                        BlockState state1 = level.getBlockState(pos1);
                        if (state1.is(state.getBlock())) {
                            sLevel.destroyBlock(pos1, true, user);
                            BlockPos[] arr1 = getNeighboringBlocks(pos1);
                            tempList.addAll(Arrays.asList(arr1));
                            numDestroyed++;
                            if (numDestroyed == Config.diamondShovelBlockCount) break;
                        }
                    }
                    if (tempList.size() == 0) break;
                    arr = tempList;
                }
            }
            stack.hurtAndBreak((int) (numDestroyed * Config.diamondShovelDurabilityMultiplier), user, (e) -> { });
        }
        return super.mineBlock(stack, level, state, pos, user);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (stack.getItem() instanceof IridiumShovelItem shovel) {
            switch (shovel.getSocket()) {
                case SKULL -> {
                    var skeleton1 = new PlayerAlliedSkeleton(world, player);
                    var skeleton2 = new PlayerAlliedSkeleton(world, player);
                    var skeleton3 = new PlayerAlliedSkeleton(world, player);
                    skeleton1.setPos(player.position().add(1.0, 0.0, 1.0));
                    skeleton2.setPos(player.position().add(-1.0, 0.0, 1.0));
                    skeleton3.setPos(player.position().add(0.0, 0.0, -1.0));
                    skeleton1.setItemInHand(InteractionHand.MAIN_HAND, Items.WOODEN_SWORD.getDefaultInstance());
                    skeleton2.setItemInHand(InteractionHand.MAIN_HAND, Items.WOODEN_SWORD.getDefaultInstance());
                    skeleton3.setItemInHand(InteractionHand.MAIN_HAND, Items.WOODEN_SWORD.getDefaultInstance());
                    world.addFreshEntity(skeleton1);
                    world.addFreshEntity(skeleton2);
                    world.addFreshEntity(skeleton3);
                    ISocketable.addCooldown(player, stack, Config.skullShovelCooldown);
                }
                case EMERALD -> {
                    if (!world.isClientSide) {
                        BlockPos blockPos = player.blockPosition();
                        world.blockEvent(player.blockPosition(), Blocks.BELL, 1, 1);
                        world.playSound(null, blockPos, SoundEvents.BELL_BLOCK, SoundSource.PLAYERS, 2.0F, 1.0F);
                        BellBlockEntity.makeRaidersGlow(world, blockPos, world.getEntities(player, player.getBoundingBox().inflate(40.0), (e) -> e.getType().is(EntityTypeTags.RAIDERS)).stream().filter((e) -> e instanceof LivingEntity).map((e) -> (LivingEntity) e).collect(Collectors.toList()));
                        List<Villager> villagers = world.getEntitiesOfClass(Villager.class, player.getBoundingBox().inflate(40.0));
                        for (Villager villager : villagers) {
                            villager.getBrain().setActiveActivityIfPossible(Activity.HIDE);
                        }
                        ISocketable.addCooldown(player, stack, Config.emeraldShovelCooldown);
                    }
                    return InteractionResultHolder.consume(stack);
                }
                case WITHER_SKULL -> {
                    BiConsumer<BlockPos, BlockState> setBlock = (pos, state) -> { if (!world.isClientSide) world.setBlockAndUpdate(pos, state); };
                    Supplier<Double> random = () -> world.getRandom().nextDouble();
                    boolean isCrimson = random.get() <= .75;
                    BlockState nylium = isCrimson ? Blocks.CRIMSON_NYLIUM.defaultBlockState() : Blocks.WARPED_NYLIUM.defaultBlockState();
                    for (int j = 0; j < 4; j++) {
                        BlockPos pos = player.blockPosition().below();
                        for (int i = 0; i < 64; i++) {
                            Direction d = Direction.Plane.HORIZONTAL.getRandomDirection(player.getRandom());
                            if (world.getBlockState(pos).is(Blocks.GRASS_BLOCK)) {
                                var abovePos = pos.above();
                                var above = world.getBlockState(abovePos);
                                if (isAirLike(above)) {
                                    setBlock.accept(abovePos, Blocks.AIR.defaultBlockState());
                                    setBlock.accept(pos, nylium);
                                    GeneralUtil.particles(
                                            world,
                                            ParticleTypes.SMOKE,
                                            pos.above(),
                                            5,
                                            0, 0.2, 0
                                    );
                                    if (random.get() <= .45) {
                                        BlockState weeds = isCrimson ^ random.get() <= .05 ? (random.get() <= .1 ? Blocks.CRIMSON_FUNGUS.defaultBlockState() : Blocks.CRIMSON_ROOTS.defaultBlockState()) : (random.get() <= .1 ? Blocks.WARPED_FUNGUS.defaultBlockState() : (random.get() <= .33 ? Blocks.NETHER_SPROUTS.defaultBlockState() : Blocks.WARPED_ROOTS.defaultBlockState()));
                                        setBlock.accept(abovePos, weeds);
                                    }
                                }
                            } else if (world.getBlockState(pos).is(Blocks.SAND))
                                setBlock.accept(pos, Blocks.SOUL_SAND.defaultBlockState());
                            else if (world.getBlockState(pos).is(Blocks.GRAVEL))
                                setBlock.accept(pos, Blocks.SOUL_SOIL.defaultBlockState());
                            pos = pos.relative(d);
                            while (!isAirLike(world.getBlockState(pos))) {
                                pos = pos.above();
                                if (pos.getY() == world.getMaxBuildHeight()) break;
                            }
                            while (isAirLike(world.getBlockState(pos))) {
                                pos = pos.below();
                                if (pos.getY() == world.getMaxBuildHeight()) break;
                            }
                        }
                    }
                    ISocketable.addCooldown(player, stack, Config.witherSkullShovelCooldown);
                    stack.hurtAndBreak(4, player, (e) -> { });

                    return InteractionResultHolder.consume(stack);
                }
            }
        }
        return InteractionResultHolder.pass(stack);
    }

    private static boolean isAirLike(BlockState state) {
        return state.isAir() || state.is(Blocks.GRASS) || state.is(Blocks.TALL_GRASS);
    }

    private BlockPos[] getNeighboringBlocks(BlockPos pos) {
        BlockPos[] arr = new BlockPos[6];
        for (int i = 0; i < 6; i++) {
            arr[i] = pos.relative(Direction.values()[i]);
        }
        return arr;
    }
}