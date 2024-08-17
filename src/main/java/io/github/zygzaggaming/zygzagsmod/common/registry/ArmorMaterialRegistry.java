package io.github.zygzaggaming.zygzagsmod.common.registry;

import io.github.zygzaggaming.zygzagsmod.common.registry.base.AkomiRegistry;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.EnumMap;
import java.util.List;

import static io.github.zygzaggaming.zygzagsmod.common.Main.MODID;

@SuppressWarnings("unused")
public class ArmorMaterialRegistry extends AkomiRegistry<ArmorMaterial> {
    public static final ArmorMaterialRegistry INSTANCE = new ArmorMaterialRegistry(DeferredRegister.create(BuiltInRegistries.ARMOR_MATERIAL, MODID));

    public static final Holder<ArmorMaterial> IRIDIUM = registerIridium("iridium", new int[]{4, 9, 7, 4}, 7, 17);

    public static final Holder<ArmorMaterial> _4_1 = registerIridium("iridium_4_1", new int[]{3, 8, 6, 3}, 7, 15);
    public static final Holder<ArmorMaterial> _4_2 = registerIridium("iridium_4_2", new int[]{4, 9, 7, 4}, 8, 16);
    public static final Holder<ArmorMaterial> _4_3 = registerIridium("iridium_4_3", new int[]{4, 9, 7, 4}, 8, 17);
    public static final Holder<ArmorMaterial> _5_1 = registerIridium("iridium_5_1", new int[]{3, 8, 6, 3}, 7, 15);
    public static final Holder<ArmorMaterial> _5_2 = registerIridium("iridium_5_2", new int[]{3, 8, 6, 3}, 7, 16);
    public static final Holder<ArmorMaterial> _5_3 = registerIridium("iridium_5_3", new int[]{4, 9, 7, 4}, 8, 16);
    public static final Holder<ArmorMaterial> _5_4 = registerIridium("iridium_5_4", new int[]{4, 9, 7, 4}, 8, 17);
    public static final Holder<ArmorMaterial> _7_1 = registerIridium("iridium_7_1", new int[]{3, 8, 6, 3}, 7, 15);
    public static final Holder<ArmorMaterial> _7_2 = registerIridium("iridium_7_2", new int[]{3, 8, 6, 3}, 7, 15);
    public static final Holder<ArmorMaterial> _7_3 = registerIridium("iridium_7_3", new int[]{3, 8, 6, 3}, 7, 16);
    public static final Holder<ArmorMaterial> _7_4 = registerIridium("iridium_7_4", new int[]{4, 9, 7, 4}, 8, 16);
    public static final Holder<ArmorMaterial> _7_5 = registerIridium("iridium_7_5", new int[]{4, 9, 7, 4}, 8, 17);
    public static final Holder<ArmorMaterial> _7_6 = registerIridium("iridium_7_6", new int[]{4, 9, 7, 4}, 8, 17);
    public static final Holder<ArmorMaterial> _8_1 = registerIridium("iridium_8_1", new int[]{3, 8, 6, 3}, 7, 15);
    public static final Holder<ArmorMaterial> _8_2 = registerIridium("iridium_8_2", new int[]{3, 8, 6, 3}, 7, 15);
    public static final Holder<ArmorMaterial> _8_3 = registerIridium("iridium_8_3", new int[]{3, 8, 6, 3}, 7, 15);
    public static final Holder<ArmorMaterial> _8_4 = registerIridium("iridium_8_4", new int[]{3, 8, 6, 3}, 7, 16);
    public static final Holder<ArmorMaterial> _8_5 = registerIridium("iridium_8_5", new int[]{4, 9, 7, 4}, 8, 16);
    public static final Holder<ArmorMaterial> _8_6 = registerIridium("iridium_8_6", new int[]{4, 9, 7, 4}, 8, 17);
    public static final Holder<ArmorMaterial> _8_7 = registerIridium("iridium_8_7", new int[]{4, 9, 7, 4}, 8, 17);

    public static final Holder<ArmorMaterial> DIAMOND_SOCKETED = registerIridium("diamond_iridium", new int[]{9, 9, 9, 9}, 9, 12, 3.5f, 0.4f);
    public static final Holder<ArmorMaterial> EMERALD_SOCKETED = registerIridium("emerald_iridium", new int[]{6, 6, 6, 6}, 6, 19, 3.0f, 0.0f);

    public ArmorMaterialRegistry(DeferredRegister<ArmorMaterial> register) {
        super(register);
    }

    public static Holder<ArmorMaterial> getArmorForPartial(int plating, int outOf) {
        if (outOf == 4) {
            return plating == 1 ? _4_1 : plating == 2 ? _4_2 : _4_3;
        } else if (outOf == 5) {
            return plating == 1 ? _5_1 : plating == 2 ? _5_2 : plating == 3 ? _5_3 : _5_4;
        } else if (outOf == 7) {
            return plating == 1 ? _7_1 : plating == 2 ? _7_2 : plating == 3 ? _7_3 : plating == 4 ? _7_4 : plating == 5 ? _7_5 : _7_6;
        } else {
            return plating == 1 ? _8_1 : plating == 2 ? _8_2 : plating == 3 ? _8_3 : plating == 4 ? _8_4 : plating == 5 ? _8_5 : plating == 6 ? _8_6 : _8_7;
        }
    }

    public static DeferredHolder<ArmorMaterial, ArmorMaterial> registerIridium(String id, int[] mainArmorValues, int bodyArmorValue, int enchantmentValue) {
        return registerIridium(id, mainArmorValues, bodyArmorValue, enchantmentValue, 3f, 0.2f);
    }

    public static DeferredHolder<ArmorMaterial, ArmorMaterial> registerIridium(String id, int[] mainArmorValues, int bodyArmorValue, int enchantmentValue, float toughness, float kbRes) {
        return INSTANCE.register(id, () -> new ArmorMaterial(
                Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
                    map.put(ArmorItem.Type.BOOTS, mainArmorValues[3]);
                    map.put(ArmorItem.Type.LEGGINGS, mainArmorValues[2]);
                    map.put(ArmorItem.Type.CHESTPLATE, mainArmorValues[1]);
                    map.put(ArmorItem.Type.HELMET, mainArmorValues[0]);
                    map.put(ArmorItem.Type.BODY, bodyArmorValue);
                }),
                enchantmentValue,
                SoundEvents.ARMOR_EQUIP_NETHERITE,
                () -> Ingredient.of(ItemRegistry.IRIDIUM_PLATING.get()),
                List.of(new ArmorMaterial.Layer(ResourceLocation.withDefaultNamespace(id))),
                toughness,
                kbRes
        ));
    }
}
