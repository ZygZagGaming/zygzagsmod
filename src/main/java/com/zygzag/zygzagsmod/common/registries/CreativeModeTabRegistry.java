package com.zygzag.zygzagsmod.common.registries;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.stream.Collectors;

import static com.zygzag.zygzagsmod.common.Main.MODID;

public class CreativeModeTabRegistry extends Registry<CreativeModeTab> {
    public static final CreativeModeTabRegistry INSTANCE = new CreativeModeTabRegistry(DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID));
    public static final RegistryObject<CreativeModeTab> TAB = INSTANCE.register("zygzagsmod_tab", () -> CreativeModeTab.builder()
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> ItemRegistry.IRIDIUM_PLATING.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.acceptAll(ItemRegistry.INSTANCE.register.getEntries().stream().map((it) -> it.get().getDefaultInstance()).collect(Collectors.toList()));
                output.acceptAll(IridiumGearRegistry.INSTANCE.register.getEntries().stream().map((it) -> it.get().getDefaultInstance()).collect(Collectors.toList()));
            })
            .title(Component.translatable("zygzagsmod.tab"))
            .build());

    public CreativeModeTabRegistry(DeferredRegister<CreativeModeTab> register) {
        super(register);
    }
}
