package io.github.zygzaggaming.zygzagsmod.common;

import io.github.zygzaggaming.zygzagsmod.common.datagen.AkomiBlockstateProvider;
import io.github.zygzaggaming.zygzagsmod.common.datagen.AkomiItemModelProvider;
import io.github.zygzaggaming.zygzagsmod.common.datagen.AkomiLootTableProvider;
import io.github.zygzaggaming.zygzagsmod.common.datagen.AkomiRecipeProvider;
import io.github.zygzaggaming.zygzagsmod.common.entity.BlazeSentry;
import io.github.zygzaggaming.zygzagsmod.common.entity.IridiumGolem;
import io.github.zygzaggaming.zygzagsmod.common.entity.SmallRod;
import io.github.zygzaggaming.zygzagsmod.common.item.iridium.ISocketable;
import io.github.zygzaggaming.zygzagsmod.common.networking.handler.*;
import io.github.zygzaggaming.zygzagsmod.common.networking.packet.*;
import io.github.zygzaggaming.zygzagsmod.common.registry.AttributeRegistry;
import io.github.zygzaggaming.zygzagsmod.common.registry.EntityTypeRegistry;
import io.github.zygzaggaming.zygzagsmod.common.registry.IridiumGearRegistry;
import net.minecraft.data.DataProvider;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

import java.util.function.Supplier;

@SuppressWarnings("unused")
@EventBusSubscriber(modid = Main.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ModEventHandler {
    @SubscribeEvent
    public static void doAttributes(final EntityAttributeCreationEvent event) {
        event.put(EntityTypeRegistry.PLAYER_ALLIED_SKELETON.get(), AbstractSkeleton.createAttributes().build());
        event.put(EntityTypeRegistry.IRIDIUM_GOLEM.get(), IridiumGolem.createAttributes().build());
        event.put(EntityTypeRegistry.BLAZE_SENTRY.get(), BlazeSentry.createAttributes().build());
        event.put(EntityTypeRegistry.SMALL_ROD.get(), SmallRod.createAttributes().build());
    }

    @SubscribeEvent
    public static void modifyAttributes(final EntityAttributeModificationEvent event) {
        for (var type : event.getTypes()) {
            if (type == EntityType.PLAYER) {
                event.add(type, AttributeRegistry.CRIT_DAMAGE, 1.5);
                event.add(type, AttributeRegistry.SPRINT_SPEED, 1.0);
                event.add(type, AttributeRegistry.SPRINT_HUNGER_CONSUMPTION, 1.0);
                event.add(type, AttributeRegistry.SPRINT_JUMP_HUNGER_CONSUMPTION, 1.0);
                event.add(type, AttributeRegistry.ARMOR_DURABILITY_REDUCTION, 1.0);
                event.add(type, AttributeRegistry.FLAIL_DAMAGE, 0.0);
            }
        }
    }

      @SubscribeEvent
      public static void entitySpawnAndRestriction(RegisterSpawnPlacementsEvent event) {
          event.register(EntityTypeRegistry.BLAZE_SENTRY.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                BlazeSentry::checkBlazeSentrySpawn, RegisterSpawnPlacementsEvent.Operation.REPLACE);
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
                (DataProvider.Factory<RecipeProvider>) (it) -> new AkomiRecipeProvider(it, event.getLookupProvider())
        );
        event.getGenerator().addProvider(
                event.includeClient(),
                (DataProvider.Factory<BlockStateProvider>) (output) -> new AkomiBlockstateProvider(output, event.getExistingFileHelper())
        );
        event.getGenerator().addProvider(
                event.includeServer(),
                (DataProvider.Factory<? extends DataProvider>) (it) -> new AkomiLootTableProvider(it, event.getLookupProvider())
        );
    }

    @SubscribeEvent
    public static void registerPayloadHandlers(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(Main.MODID);
        registrar.playToClient(ClientboundBlazeSentryRotationPacket.TYPE, ClientboundBlazeSentryRotationPacket.STREAM_CODEC, ClientboundBlazeSentryRotationPacketHandler.getInstance()::handleData);
        registrar.playToClient(ClientboundRotationSetPacket.TYPE, ClientboundRotationSetPacket.STREAM_CODEC, ClientboundRotationSetPacketHandler.getInstance()::handleData);
        registrar.playToClient(ClientboundSmallRodRotationPacket.TYPE, ClientboundSmallRodRotationPacket.STREAM_CODEC, ClientboundSmallRodRotationPacketHandler.getInstance()::handleData);
        registrar.playToClient(ClientboundSelfOverheatUpdatePacket.TYPE, ClientboundSelfOverheatUpdatePacket.STREAM_CODEC, ClientboundSelfOverheatUpdatePacketHandler.getInstance()::handleData);
        registrar.playToClient(ClientboundSocketHitPacket.TYPE, ClientboundSocketHitPacket.STREAM_CODEC, ClientboundSocketHitPacketHandler.getInstance()::handleData);
        registrar.playToServer(ServerboundPlayerLeftClickEmptyPacket.TYPE, ServerboundPlayerLeftClickEmptyPacket.STREAM_CODEC, ServerboundPlayerLeftClickEmptyPacketHandler.getInstance()::handleData);
    }
}
