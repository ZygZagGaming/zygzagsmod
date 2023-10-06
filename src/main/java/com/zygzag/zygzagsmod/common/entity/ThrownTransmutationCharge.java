package com.zygzag.zygzagsmod.common.entity;

import com.zygzag.zygzagsmod.common.recipe.TransmutationRecipe;
import com.zygzag.zygzagsmod.common.registry.EntityTypeRegistry;
import com.zygzag.zygzagsmod.common.registry.ItemRegistry;
import com.zygzag.zygzagsmod.common.registry.RecipeTypeRegistry;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ThrownTransmutationCharge extends ThrowableItemProjectile {
    public ThrownTransmutationCharge(EntityType<? extends ThrownTransmutationCharge> type, Level world) {
        super(type, world);
    }

    public ThrownTransmutationCharge(Level world, double xPos, double yPos, double zPos) {
        super(EntityTypeRegistry.TRANSMUTATION_CHARGE_ENTITY.get(), xPos, yPos, zPos, world);
    }

    public ThrownTransmutationCharge(Level world, LivingEntity thrower) {
        super(EntityTypeRegistry.TRANSMUTATION_CHARGE_ENTITY.get(), thrower, world);
    }

    protected Item getDefaultItem() {
        return ItemRegistry.TRANSMUTATION_CHARGE.get();
    }

    protected float getGravity() {
        return 0.07F;
    }

    protected void onHit(HitResult result) {
        super.onHit(result);
        var level = level();
        if (level instanceof ServerLevel) {
            level.levelEvent(2002, this.blockPosition(), 0x727175);
            AABB box = getBoundingBox().inflate(5.0);
            List<ItemEntity> entities = level.getEntitiesOfClass(ItemEntity.class, box);
            List<TransmutationRecipe> recipes = level.getRecipeManager().getAllRecipesFor(RecipeTypeRegistry.TRANSMUTATION.get());
            for (ItemEntity itemEntity : entities) {
                for (TransmutationRecipe recipe : recipes) {
                    SimpleContainer holder = new SimpleContainer(itemEntity.getItem());
                    if (recipe.matches(holder, level)) {
                        ItemStack output = recipe.assemble(holder, level.registryAccess());
                        int stackSize = output.getItem().getMaxStackSize(output);
                        while (output.getCount() > stackSize) {
                            ItemEntity newItem = new ItemEntity(level, itemEntity.getX(), itemEntity.getY(), itemEntity.getZ(), output.split(stackSize));
                            level.addFreshEntity(newItem);
                        }
                        ItemEntity newItem = new ItemEntity(level, itemEntity.getX(), itemEntity.getY(), itemEntity.getZ(), output);
                        level.addFreshEntity(newItem);
                        itemEntity.kill();
                    }
                }
            }
            this.discard();
        }

    }
}