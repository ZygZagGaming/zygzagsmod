package com.zygzag.zygzagsmod.common.registry;

import com.zygzag.zygzagsmod.common.item.iridium.Socket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

import static com.zygzag.zygzagsmod.common.Main.MODID;

public class SocketRegistry extends Registry<Socket> {
    public static final SocketRegistry INSTANCE = new SocketRegistry(DeferredRegister.create(new ResourceLocation(MODID, "socket"), MODID));
    //public static final Supplier<IForgeRegistry<Socket>> SOCKET_REGISTRY = INSTANCE.register.makeRegistry(RegistryBuilder::new);
    public static final RegistryObject<Socket> NONE = makeSocket(Items.AIR, 0xffffff, "none");
    public static final RegistryObject<Socket> DIAMOND = makeSocket(Items.DIAMOND, 0x4aedd9, "diamond");
    public static final RegistryObject<Socket> EMERALD = makeSocket(Items.EMERALD, 0x41f384, "emerald");
    public static final RegistryObject<Socket> SKULL = makeSocket(Items.SKELETON_SKULL, 0xd3d3d3, "skull");
    public static final RegistryObject<Socket> WITHER_SKULL = makeSocket(Items.WITHER_SKELETON_SKULL, 0x515353, "wither_skull");
    public static final RegistryObject<Socket> AMETHYST = makeSocket(Items.AMETHYST_SHARD, 0xcfa0f3, "amethyst");

    private static RegistryObject<Socket> makeSocket(Item item, int color, String id) {
        return INSTANCE.register(id, () -> new Socket(item, color, id));
    }

    public SocketRegistry(DeferredRegister<Socket> register) {
        super(register);
    }
}
