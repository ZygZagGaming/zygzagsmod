package io.github.zygzaggaming.zygzagsmod.common.registry;


import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import io.github.zygzaggaming.zygzagsmod.common.loot.AutosmeltModifier;
import io.github.zygzaggaming.zygzagsmod.common.loot.ExecutionerModifier;
import io.github.zygzaggaming.zygzagsmod.common.loot.WitherPickNoDropModifier;
import io.github.zygzaggaming.zygzagsmod.common.registry.base.AkomiRegistry;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

import static io.github.zygzaggaming.zygzagsmod.common.Main.MODID;

@SuppressWarnings("unused")
public class GlobalLootModifierSerializerRegistry extends AkomiRegistry<MapCodec<? extends IGlobalLootModifier>> {
    public static final GlobalLootModifierSerializerRegistry INSTANCE = new GlobalLootModifierSerializerRegistry(DeferredRegister.create(NeoForgeRegistries.GLOBAL_LOOT_MODIFIER_SERIALIZERS, MODID));
    public static final Supplier<MapCodec<ExecutionerModifier>> EXECUTIONER = INSTANCE.register("executioner", () -> ExecutionerModifier.CODEC);
    public static final Supplier<MapCodec<AutosmeltModifier>> AUTOSMELT = INSTANCE.register("autosmelt", () -> AutosmeltModifier.CODEC);
    public static final Supplier<MapCodec<WitherPickNoDropModifier>> WITHER_PICK_NO_DROP = INSTANCE.register("wither_pickaxe_no_drop", () -> WitherPickNoDropModifier.CODEC);

    public GlobalLootModifierSerializerRegistry(DeferredRegister<MapCodec<? extends IGlobalLootModifier>> register) {
        super(register);
    }
}