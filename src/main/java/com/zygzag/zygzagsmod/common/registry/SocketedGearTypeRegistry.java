package com.zygzag.zygzagsmod.common.registry;

import com.zygzag.zygzagsmod.common.item.iridium.SocketedGearType;
import com.zygzag.zygzagsmod.common.item.iridium.SocketedItemFactory;
import com.zygzag.zygzagsmod.common.item.iridium.armor.IridiumChestplateItem;
import com.zygzag.zygzagsmod.common.item.iridium.tool.*;
import com.zygzag.zygzagsmod.common.tier.IridiumArmorMaterial;
import com.zygzag.zygzagsmod.common.tier.IridiumToolTier;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import static com.zygzag.zygzagsmod.common.Main.MODID;

public class SocketedGearTypeRegistry extends Registry<SocketedGearType> {
    public static final SocketedGearTypeRegistry INSTANCE = new SocketedGearTypeRegistry(DeferredRegister.create(new ResourceLocation(MODID, "socketed_gear_type"), MODID));

    public static final RegistryObject<SocketedGearType> SWORD = makeSGT("sword", (properties, socket) -> new IridiumSwordItem(
            socket == SocketRegistry.DIAMOND ? IridiumToolTier.DIAMOND_SOCKETED :
                    socket == SocketRegistry.EMERALD ? IridiumToolTier.EMERALD_SOCKETED :
                            IridiumToolTier.FULL,
            3,
            -2.4f,
            properties,
            socket
    ));
    public static final RegistryObject<SocketedGearType> PICKAXE = makeSGT("pickaxe", (properties, socket) -> new IridiumPickaxeItem(
            socket == SocketRegistry.DIAMOND ? IridiumToolTier.DIAMOND_SOCKETED_PICK :
                    socket == SocketRegistry.EMERALD ? IridiumToolTier.EMERALD_SOCKETED :
                            socket == SocketRegistry.WITHER_SKULL ? IridiumToolTier.WITHER_SOCKETED_PICK :
                                    IridiumToolTier.FULL,
            1,
            -2.8f,
            properties,
            socket
    ));
    public static final RegistryObject<SocketedGearType> AXE = makeSGT("axe", (properties, socket) -> new IridiumAxeItem(
            socket == SocketRegistry.DIAMOND ? IridiumToolTier.DIAMOND_SOCKETED :
                    socket == SocketRegistry.EMERALD ? IridiumToolTier.EMERALD_SOCKETED :
                            IridiumToolTier.FULL,
            5,
            -3,
            properties,
            socket
    ));
    public static final RegistryObject<SocketedGearType> SHOVEL = makeSGT("shovel", (properties, socket) -> new IridiumShovelItem(
            socket == SocketRegistry.DIAMOND ? IridiumToolTier.DIAMOND_SOCKETED :
                    socket == SocketRegistry.EMERALD ? IridiumToolTier.EMERALD_SOCKETED :
                            IridiumToolTier.FULL,
            1.5f,
            -3,
            properties,
            socket
    ));
    public static final RegistryObject<SocketedGearType> HOE = makeSGT("hoe", (properties, socket) -> new IridiumHoeItem(
            socket == SocketRegistry.DIAMOND ? IridiumToolTier.DIAMOND_SOCKETED :
                    socket == SocketRegistry.EMERALD ? IridiumToolTier.EMERALD_SOCKETED :
                            IridiumToolTier.FULL,
            socket == SocketRegistry.WITHER_SKULL ? 3 : -1,
            socket == SocketRegistry.WITHER_SKULL ? -2.4f : 0,
            properties,
            socket
    ));
    public static final RegistryObject<SocketedGearType> CHESTPLATE = makeSGT("chestplate", (properties, socket) -> new IridiumChestplateItem(
            socket == SocketRegistry.DIAMOND ? IridiumArmorMaterial.DIAMOND_SOCKETED :
                    socket == SocketRegistry.EMERALD ? IridiumArmorMaterial.EMERALD_SOCKETED :
                            IridiumArmorMaterial.IRIDIUM,
            properties,
            socket
    ));
    public static final RegistryObject<SocketedGearType> SCEPTER = makeSGT("scepter", IridiumScepterItem::new);


    public static RegistryObject<SocketedGearType> makeSGT(String id, SocketedItemFactory factory) {
        return INSTANCE.register(id, () -> new SocketedGearType(factory));
    }
    public SocketedGearTypeRegistry(DeferredRegister<SocketedGearType> register) {
        super(register);
    }
}
