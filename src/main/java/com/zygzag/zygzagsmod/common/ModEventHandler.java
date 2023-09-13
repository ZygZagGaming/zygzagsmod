package com.zygzag.zygzagsmod.common;

import com.zygzag.zygzagsmod.common.item.iridium.ISocketable;
import com.zygzag.zygzagsmod.common.item.iridium.tool.IridiumAxeItem;
import com.zygzag.zygzagsmod.common.registry.EntityRegistry;
import com.zygzag.zygzagsmod.common.registry.IridiumGearRegistry;
import net.minecraft.data.DataProvider;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;

import static com.zygzag.zygzagsmod.common.Main.MODID;

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventHandler {
    @SubscribeEvent
    public static void doAttributes(final EntityAttributeCreationEvent event) {
        event.put(EntityRegistry.PLAYER_ALLIED_SKELETON.get(), AbstractSkeleton.createAttributes().build());
    }

    @SubscribeEvent
    public static void itemColors(final RegisterColorHandlersEvent.Item event) {
        for (RegistryObject<Item> obj : IridiumGearRegistry.SOCKETED_ITEMS) {
            event.register(ISocketable::getColor, obj.get());
        }
    }

    @SubscribeEvent
    public static void gatherData(final GatherDataEvent event) {
        event.getGenerator().addProvider(
                true,
                (DataProvider.Factory<ItemModelProvider>) (output) -> new ItemModelProvider(output, MODID, event.getExistingFileHelper()) {
                    @Override
                    protected void registerModels() {
                        String[] handHeldItemTypes = { "axe", "sword", "pickaxe", "shovel", "hoe", "scepter" };
                        String[] nonHandHeldItemTypes = { "chestplate" };
                        String[] sockets = { "amethyst", "diamond", "emerald", "skull", "wither_skull" };
                        for (String socket : sockets) {
                            for (String type : handHeldItemTypes) {
                                makeSocketedIridiumTool(type, socket);
                            }
                            for (String type : nonHandHeldItemTypes) {
                                makeSocketedIridiumNonTool(type, socket);
                            }
                        }
                    }

                    public void makeSocketedIridiumTool(String type, String socket) {
                        generatedHandheld(getBuilder("zygzagsmod:" + socket + "_socketed_iridium_" + type))
                                .texture("layer0", "zygzagsmod:item/" + type + "/iridium_" + type)
                                .texture("layer1", "zygzagsmod:item/socket/" + type + "_socket");
                    }

                    public void makeSocketedIridiumNonTool(String type, String socket) {
                        getBuilder("zygzagsmod:" + socket + "_socketed_iridium_" + type)
                                .parent(new ModelFile.UncheckedModelFile("item/generated"))
                                .texture("layer0", "zygzagsmod:item/" + type + "/iridium_" + type)
                                .texture("layer1", "zygzagsmod:item/socket/" + type + "_socket");
                    }
                }
        );
    }

    public static ItemModelBuilder generatedHandheld(ItemModelBuilder builder) {
        return builder.parent(new ModelFile.UncheckedModelFile("item/generated")).transforms()
                .transform(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND)
                .rotation(0, -90, 55).translation(0, 4, .5f).scale(.85f).end()
                .transform(ItemDisplayContext.THIRD_PERSON_LEFT_HAND)
                .rotation(0, 90, -55).translation(0, 4, .5f).scale(.85f).end()
                .transform(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND)
                .rotation(0, -90, 25).translation(1.13f, 3.2f, 1.13f).scale(.68f).end()
                .transform(ItemDisplayContext.FIRST_PERSON_LEFT_HAND)
                .rotation(0, 90, -25).translation(1.13f, 3.2f, 1.13f).scale(.68f).end().end();
    }
}
