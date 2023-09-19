package com.zygzag.zygzagsmod.common.item.iridium.tool;

import com.mojang.blaze3d.MethodsReturnNonnullByDefault;
import com.zygzag.zygzagsmod.common.Config;
import com.zygzag.zygzagsmod.common.item.iridium.ISocketable;
import com.zygzag.zygzagsmod.common.item.iridium.Socket;
import com.zygzag.zygzagsmod.common.recipe.TransmutationRecipe;
import com.zygzag.zygzagsmod.common.registry.EnchantmentRegistry;
import com.zygzag.zygzagsmod.common.registry.MobEffectRegistry;
import com.zygzag.zygzagsmod.common.registry.RecipeTypeRegistry;
import com.zygzag.zygzagsmod.common.registry.SocketRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
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
import java.util.function.Supplier;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class IridiumPickaxeItem extends PickaxeItem implements ISocketable {
    Supplier<Socket> socketSupplier;
    public IridiumPickaxeItem(Tier tier, int damage, float speed, Properties properties, Supplier<Socket> socketSupplier) {
        super(tier, damage, speed, properties);
        this.socketSupplier = socketSupplier;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> text, TooltipFlag flag) {
        Socket s = getSocket();
        Item i = s.item;
        MutableComponent m;
        if (s != SocketRegistry.NONE.get() && world != null) {
            String str = hasUseAbility() ? "use" : "passive";
            MutableComponent t = Component.translatable("socketed.zygzagsmod").withStyle(ChatFormatting.GRAY);
            t.append(Component.literal(": ").withStyle(ChatFormatting.GRAY));
            t.append(((MutableComponent) i.getName(i.getDefaultInstance())).withStyle(ChatFormatting.GOLD));
            text.add(t);

            Socket socket = getSocket();
            text.add(Component.literal(""));
            if (str.equals("passive")) m = Component.translatable(str + ".zygzagsmod").withStyle(ChatFormatting.GRAY);
            else m = Minecraft.getInstance().options.keyUse.getKey().getDisplayName().copy().withStyle(ChatFormatting.GRAY);
            m.append(Component.literal( ": ").withStyle(ChatFormatting.GRAY));
            m.append(Component.translatable( str + "_ability.zygzagsmod.pickaxe." + socket.name.toLowerCase()).withStyle(ChatFormatting.GOLD));
            text.add(m);
            text.add(Component.translatable("description." + str + "_ability.zygzagsmod.pickaxe." + socket.name.toLowerCase()));
            if (hasCooldown()) {
                MutableComponent comp = Component.translatable("zygzagsmod.cooldown").withStyle(ChatFormatting.GRAY);
                comp.append(Component.literal(": ").withStyle(ChatFormatting.GRAY));
                comp.append(Component.literal(Float.toString(getCooldown(stack, world) / 20f) + " ").withStyle(ChatFormatting.GOLD));
                //text.add(Component.literal("\n"));
                text.add(comp);
            }
        }
    }

    @Override
    public boolean hasCooldown() {
        return getSocket() == SocketRegistry.AMETHYST.get();
    }

    @Override
    public int getCooldown(ItemStack stack, Level world) {
        int cooldownLevel = EnchantmentHelper.getTagEnchantmentLevel(EnchantmentRegistry.COOLDOWN_ENCHANTMENT.get(), stack);
        int baseCooldown = getSocket() == SocketRegistry.AMETHYST.get() ? Config.amethystPickaxeCooldown : 0;
        return baseCooldown / (cooldownLevel + 1);
    }

    @Override
    public Socket getSocket() {
        return socketSupplier.get();
    }

    @Override
    public boolean hasUseAbility() {
        return hasCooldown() || getSocket() == SocketRegistry.SKULL.get() || getSocket() == SocketRegistry.EMERALD.get();
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (stack.getItem() instanceof IridiumPickaxeItem item) {
            if (getSocket() == SocketRegistry.EMERALD.get()) {
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
            if (getSocket() == SocketRegistry.SKULL.get()) {
                if (!player.getCooldowns().isOnCooldown(this)) {
                    AABB box = player.getBoundingBox().inflate(5.0);
                    List<ItemEntity> entities = world.getEntitiesOfClass(ItemEntity.class, box);
                    List<TransmutationRecipe> recipes = world.getRecipeManager().getAllRecipesFor(RecipeTypeRegistry.TRANSMUTATION.get());
                    int n = 0;
                    for (ItemEntity itemEntity : entities) {
                        for (TransmutationRecipe recipe : recipes) {
                            if (n >= 10) break;
                            SimpleContainer holder = new SimpleContainer(itemEntity.getItem());
                            if (recipe.matches(holder, world)) {
                                int in = itemEntity.getItem().getCount();
                                ItemEntity newItem = new ItemEntity(world, itemEntity.getX(), itemEntity.getY(), itemEntity.getZ(), recipe.assemble(holder, world.registryAccess()));
                                world.addFreshEntity(newItem);
                                if (!player.getAbilities().instabuild) stack.hurtAndBreak(in, player, (it) -> {});
                                itemEntity.kill();
                                n++;
                            }
                        }
                    }
                    return InteractionResultHolder.success(stack);
                }
            }
            if (getSocket() == SocketRegistry.AMETHYST.get()) {
                if (!player.getCooldowns().isOnCooldown(this)) {
                    player.addEffect(new MobEffectInstance(MobEffectRegistry.SIGHT_EFFECT.get(), 2 * 20, 1));
                    ISocketable.addCooldown(player, stack, Config.amethystPickaxeCooldown);
                }
                return InteractionResultHolder.consume(stack);
            }
        }
        return InteractionResultHolder.fail(stack);
    }
}
