package com.zygzag.zygzagsmod.common.registry;

import com.zygzag.zygzagsmod.common.registry.base.AkomiRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;
import java.util.stream.Collectors;

import static com.zygzag.zygzagsmod.common.Main.MODID;

public class CreativeModeTabRegistry extends AkomiRegistry<CreativeModeTab> {
    public static final CreativeModeTabRegistry INSTANCE = new CreativeModeTabRegistry(DeferredRegister.create(BuiltInRegistries.CREATIVE_MODE_TAB, MODID));
    public static final Supplier<CreativeModeTab> TAB = INSTANCE.register("zygzagsmod_tab", () -> CreativeModeTab.builder()
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> ItemRegistry.IRIDIUM_PLATING.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.acceptAll(ItemRegistry.INSTANCE.register.getEntries().stream().map((it) -> it.get().getDefaultInstance()).collect(Collectors.toList()));
                output.acceptAll(IridiumGearRegistry.INSTANCE.register.getEntries().stream().map((it) -> it.get().getDefaultInstance()).collect(Collectors.toList()));
                output.acceptAll(BlockWithItemRegistry.INSTANCE.itemRegister.getEntries().stream().map((it) -> it.get().getDefaultInstance()).collect(Collectors.toList()));
                output.acceptAll(BlockItemEntityRegistry.INSTANCE.itemRegister.getEntries().stream().map((it) -> it.get().getDefaultInstance()).collect(Collectors.toList()));
            }) //TODO: organize the tab by item type rather than registry
            .title(Component.translatable("zygzagsmod.tab"))
            .build());

    public CreativeModeTabRegistry(DeferredRegister<CreativeModeTab> register) {
        super(register);
    }
}
