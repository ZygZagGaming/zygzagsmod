package com.zygzag.zygzagsmod.common.block.entity;

import com.zygzag.zygzagsmod.common.registry.BlockItemEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;

public class TunedAmethystClusterBlockEntity extends BlockEntity {
    public SoundEvent currentSound = null;

    public TunedAmethystClusterBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public TunedAmethystClusterBlockEntity(BlockPos pos, BlockState state) {
        super(BlockItemEntityRegistry.TUNED_AMETHYST_CLUSTER.getBlockEntityType(), pos, state);
    }

    public TunedAmethystClusterBlockEntity(BlockPos pos, BlockState state, SoundEvent sound) {
        super(BlockItemEntityRegistry.TUNED_AMETHYST_CLUSTER.getBlockEntityType(), pos, state);
        currentSound = sound;
    }

    @Override
    public void saveToItem(ItemStack stack) {
        var tag = stack.getOrCreateTag();
        ForgeRegistries.SOUND_EVENTS.getResourceKey(currentSound).ifPresent((it) -> {
            tag.putString("sound_event", it.location().toString());
        });
        super.saveToItem(stack);
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        ForgeRegistries.SOUND_EVENTS.getResourceKey(currentSound).ifPresent((it) -> {
            tag.putString("sound_event", it.location().toString());
        });
        super.saveAdditional(tag);
    }

    @Override
    public void load(CompoundTag tag) {
        if (tag.contains("sound_event")) {
            var location = new ResourceLocation(tag.getString("sound_event"));
            currentSound = ForgeRegistries.SOUND_EVENTS.getValue(location);
            //System.out.println(ForgeRegistries.SOUND_EVENTS.getValue(location));
        } else currentSound = null;
        super.load(tag);
    }
}
