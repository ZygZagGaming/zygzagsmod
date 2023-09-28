package com.zygzag.zygzagsmod.common.item.iridium.tool;

import com.mojang.blaze3d.MethodsReturnNonnullByDefault;
import com.zygzag.zygzagsmod.common.Config;
import com.zygzag.zygzagsmod.common.item.iridium.ISocketable;
import com.zygzag.zygzagsmod.common.item.iridium.Socket;
import com.zygzag.zygzagsmod.common.recipe.TransmutationRecipe;
import com.zygzag.zygzagsmod.common.registry.MobEffectRegistry;
import com.zygzag.zygzagsmod.common.registry.RecipeTypeRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.Tags;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class IridiumPickaxeItem extends PickaxeItem implements ISocketable {
    Socket socket;
    public IridiumPickaxeItem(Tier tier, int damage, float speed, Properties properties, Socket socket) {
        super(tier, damage, speed, properties);
        this.socket = socket;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> text, TooltipFlag flag) {
        appendHoverText(stack, world, text, flag, "pickaxe");
    }

    @Override
    public boolean hasCooldown() {
        return socket == Socket.AMETHYST;
    }

    @Override
    public int getBaseCooldown(ItemStack stack, Level world) {
        if (socket == Socket.AMETHYST) return Config.amethystPickaxeCooldown;
        return 0;
    }

    @Override
    public Socket getSocket() {
        return socket;
    }

    @Override
    public boolean hasUseAbility() {
        return hasCooldown() || socket == Socket.SKULL || socket == Socket.EMERALD;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (stack.getItem() instanceof IridiumPickaxeItem item) {
            switch (item.getSocket()) {
                case EMERALD -> {
                    if (!player.getCooldowns().isOnCooldown(this)) {
                        int playerX = player.getBlockX();
                        int playerY = player.getBlockY();
                        int playerZ = player.getBlockZ();
                        int n = 0;
                        int range = 6;
                        for (int x = playerX - range; x <= playerX + range; x++) {
                            for (int y = playerY - range; y <= playerY + range; y++) {
                                for (int z = playerZ - range; z <= playerZ + range; z++) {
                                    BlockPos pos = new BlockPos(x, y, z);
                                    BlockState state = world.getBlockState(pos);
                                    if (state.is(Tags.Blocks.ORES)) {
                                        world.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
                                        if (world instanceof ServerLevel sWorld) {
                                            List<ItemStack> stacks = state.getDrops(
                                                    new LootParams.Builder(sWorld)
                                                            .withParameter(LootContextParams.TOOL, stack)
                                                            .withParameter(
                                                                    LootContextParams.ORIGIN,
                                                                    new Vec3(pos.getX(), pos.getY(), pos.getZ())
                                                            )
                                            );
                                            for (ItemStack s : stacks) {
                                                ItemEntity i = new ItemEntity(world, playerX, playerY, playerZ, s);
                                                world.addFreshEntity(i);
                                            }
                                            n++;
                                        }
                                    }
                                }
                            }
                        }
                        stack.hurtAndBreak(n * 4, player, (e) -> { });
                    }
                    return InteractionResultHolder.success(stack);
                }
                case SKULL -> {
                    if (!player.getCooldowns().isOnCooldown(this)) {
                        AABB box = player.getBoundingBox().inflate(5.0);
                        List<ItemEntity> entities = world.getEntitiesOfClass(ItemEntity.class, box);
                        List<TransmutationRecipe> recipes = world.getRecipeManager().getAllRecipesFor(RecipeTypeRegistry.TRANSMUTATION.get());
                        int n = 0;
                        int dura = 0;
                        for (ItemEntity itemEntity : entities) {
                            for (TransmutationRecipe recipe : recipes) {
                                if (n >= 10) break;
                                SimpleContainer holder = new SimpleContainer(itemEntity.getItem());
                                if (recipe.matches(holder, world)) {
                                    int in = itemEntity.getItem().getCount();
                                    dura += in;
                                    ItemEntity newItem = new ItemEntity(world, itemEntity.getX(), itemEntity.getY(), itemEntity.getZ(), recipe.assemble(holder, world.registryAccess()));
                                    world.addFreshEntity(newItem);
                                    if (!player.getAbilities().instabuild) stack.hurtAndBreak(in, player, (it) -> {});
                                    itemEntity.kill();
                                    n++;
                                }
                            }
                        }
                        stack.hurtAndBreak(dura, player, (p) -> {});
                        return InteractionResultHolder.success(stack);
                    }
                }
                case AMETHYST -> {
                    if (!player.getCooldowns().isOnCooldown(this)) {
                        player.addEffect(new MobEffectInstance(MobEffectRegistry.SIGHT_EFFECT.get(), 2 * 20, 1));
                        ISocketable.addCooldown(player, stack, Config.amethystPickaxeCooldown);
                    }
                    return InteractionResultHolder.consume(stack);
                }
            }
        }
        return InteractionResultHolder.fail(stack);
    }
}
