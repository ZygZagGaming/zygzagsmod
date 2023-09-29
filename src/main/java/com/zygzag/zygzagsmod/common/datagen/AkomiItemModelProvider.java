package com.zygzag.zygzagsmod.common.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

import static com.zygzag.zygzagsmod.common.Main.MODID;

public class AkomiItemModelProvider extends ItemModelProvider {
    public AkomiItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        makeSocketedIridiumStuff();
        makePartialIridiumStuff();
        makeIridiumNonSocketableArmor("helmet");
        makeIridiumNonSocketableArmor("leggings");
        makeIridiumNonSocketableArmor("boots");
    }

    private void makeSocketedIridiumStuff() {
        String[] handHeldItemTypes = {"axe", "sword", "pickaxe", "shovel", "hoe", "scepter"};
        String[] nonHandHeldItemTypes = {"chestplate"};
        String[] sockets = {"amethyst", "diamond", "emerald", "skull", "wither_skull"};
        for (String socket : sockets) {
            for (String type : handHeldItemTypes) {
                makeSocketedIridiumTool(type, socket);
            }
            for (String type : nonHandHeldItemTypes) {
                makeSocketedIridiumNonTool(type, socket);
            }
        }
        for (String type : handHeldItemTypes) {
            makeNonSocketedIridiumTool(type);
        }
        for (String type : nonHandHeldItemTypes) {
            makeNonSocketedIridiumNonTool(type);
        }
    }

    private void makePartialIridiumStuff() {
        String[] handHeldItemTypes = {"axe", "sword", "pickaxe", "hoe"};
        int[] handHeldItemPlatings = {3, 2, 3, 2};
        String[] nonHandHeldItemTypes = {"helmet", "chestplate", "leggings", "boots"};
        int[] nonHandHeldItemPlatings = {5, 8, 7, 4};

        for (int index = 0; index < handHeldItemTypes.length; index++) {
            for (int platings = 1; platings < handHeldItemPlatings[index]; platings++) {
                makePartialIridiumTool(handHeldItemTypes[index], platings, handHeldItemPlatings[index]);
            }
        }
        for (int index = 0; index < nonHandHeldItemTypes.length; index++) {
            for (int platings = 1; platings < nonHandHeldItemPlatings[index]; platings++) {
                makePartialIridiumNonTool(nonHandHeldItemTypes[index], platings, nonHandHeldItemPlatings[index]);
            }
        }
    }

    private void makeIridiumNonSocketableArmor(String type) {
        getBuilder(MODID + ":iridium_" + type)
                .parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0", MODID + ":item/" + type + "/iridium_" + type);
    }

    private void makePartialIridiumTool(String type, int platings, int maxPlatings) {
        getBuilder(MODID + ":partial_iridium_" + type + "_" + platings)
                .parent(new ModelFile.UncheckedModelFile("item/handheld"))
                .texture("layer0", MODID + ":item/" + type + "/iridium_" + type + "_" + (maxPlatings - platings - 1));
    }

    private void makePartialIridiumNonTool(String type, int platings, int maxPlatings) {
        getBuilder(MODID + ":partial_iridium_" + type + "_" + platings)
                .parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0", MODID + ":item/" + type + "/iridium_" + type + "_" + (maxPlatings - platings - 1));
    }

    private void makeNonSocketedIridiumTool(String type) {
        getBuilder(MODID + ":iridium_" + type)
                .parent(new ModelFile.UncheckedModelFile("item/handheld"))
                .texture("layer0", MODID + ":item/" + type + "/iridium_" + type);
    }

    private void makeNonSocketedIridiumNonTool(String type) {
        getBuilder(MODID + ":iridium_" + type)
                .parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0", MODID + ":item/" + type + "/iridium_" + type);
    }

    private void makeSocketedIridiumTool(String type, String socket) {
        generatedHandheld(getBuilder(MODID + ":" + socket + "_socketed_iridium_" + type))
                .texture("layer0", MODID + ":item/" + type + "/iridium_" + type)
                .texture("layer1", MODID + ":item/socket/" + type + "_socket");
    }

    private void makeSocketedIridiumNonTool(String type, String socket) {
        getBuilder("zygzagsmod:" + socket + "_socketed_iridium_" + type)
                .parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0", "zygzagsmod:item/" + type + "/iridium_" + type)
                .texture("layer1", "zygzagsmod:item/socket/" + type + "_socket");
    }

    private static ItemModelBuilder generatedHandheld(ItemModelBuilder builder) {
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
