package com.zygzag.zygzagsmod.common;

import com.zygzag.zygzagsmod.common.datagen.AkomiBlockstateProvider;
import com.zygzag.zygzagsmod.common.datagen.AkomiItemModelProvider;
import com.zygzag.zygzagsmod.common.datagen.AkomiLootTableProvider;
import com.zygzag.zygzagsmod.common.datagen.AkomiRecipeProvider;
import com.zygzag.zygzagsmod.common.entity.BlazeSentry;
import com.zygzag.zygzagsmod.common.entity.IridiumGolem;
import com.zygzag.zygzagsmod.common.item.iridium.ISocketable;
import com.zygzag.zygzagsmod.common.registry.AttributeRegistry;
import com.zygzag.zygzagsmod.common.registry.EntityTypeRegistry;
import com.zygzag.zygzagsmod.common.registry.IridiumGearRegistry;
import net.minecraft.data.DataProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;

import java.util.function.Supplier;

import static com.zygzag.zygzagsmod.common.Main.MODID;

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventHandler {
    @SubscribeEvent
    public static void doAttributes(final EntityAttributeCreationEvent event) {
        event.put(EntityTypeRegistry.PLAYER_ALLIED_SKELETON.get(), AbstractSkeleton.createAttributes().build());
        event.put(EntityTypeRegistry.IRIDIUM_GOLEM.get(), IridiumGolem.createAttributes().build());
        event.put(EntityTypeRegistry.BLAZE_SENTRY.get(), BlazeSentry.createAttributes().build());
    }

    @SubscribeEvent
    public static void modifyAttributes(final EntityAttributeModificationEvent event) {
        for (var type : event.getTypes()) {
            event.add(type, AttributeRegistry.JUMP_POWER.get(), 1);
        }
    }

    @SubscribeEvent
    public static void itemColors(final RegisterColorHandlersEvent.Item event) {
        for (Supplier<Item> obj : IridiumGearRegistry.SOCKETED_ITEMS) {
            event.register(ISocketable::getColor, obj.get());
        }
    }

    @SubscribeEvent
    public static void gatherData(final GatherDataEvent event) {
        event.getGenerator().addProvider(
                event.includeClient(),
                (DataProvider.Factory<ItemModelProvider>) (output) -> new AkomiItemModelProvider(output, event.getExistingFileHelper())
        );
        event.getGenerator().addProvider(
                event.includeClient(),
                (DataProvider.Factory<RecipeProvider>) (output) -> new AkomiRecipeProvider(output, event.getLookupProvider())
        );
        event.getGenerator().addProvider(
                event.includeClient(),
                (DataProvider.Factory<BlockStateProvider>) (output) -> new AkomiBlockstateProvider(output, event.getExistingFileHelper())
        );
        event.getGenerator().addProvider(
                event.includeServer(),
                (DataProvider.Factory<LootTableProvider>) AkomiLootTableProvider::new
        );
    }
}
