package com.zygzag.zygzagsmod.common.registry;


import com.mojang.serialization.Codec;
import com.zygzag.zygzagsmod.common.loot.AutosmeltModifier;
import com.zygzag.zygzagsmod.common.loot.ExecutionerModifier;
import com.zygzag.zygzagsmod.common.loot.WitherPickNoDropModifier;
import com.zygzag.zygzagsmod.common.registry.base.AkomiRegistry;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

import static com.zygzag.zygzagsmod.common.Main.MODID;

@SuppressWarnings("unused")
public class GlobalLootModifierSerializerRegistry extends AkomiRegistry<Codec<? extends IGlobalLootModifier>> {
    public static final GlobalLootModifierSerializerRegistry INSTANCE = new GlobalLootModifierSerializerRegistry(DeferredRegister.create(NeoForgeRegistries.GLOBAL_LOOT_MODIFIER_SERIALIZERS, MODID));
    public static final Supplier<Codec<ExecutionerModifier>> EXECUTIONER = INSTANCE.register("executioner", () -> ExecutionerModifier.CODEC);
    public static final Supplier<Codec<AutosmeltModifier>> AUTOSMELT = INSTANCE.register("autosmelt", () -> AutosmeltModifier.CODEC);
    public static final Supplier<Codec<WitherPickNoDropModifier>> WITHER_PICK_NO_DROP = INSTANCE.register("wither_pickaxe_no_drop", () -> WitherPickNoDropModifier.CODEC);
    public GlobalLootModifierSerializerRegistry(DeferredRegister<Codec<? extends IGlobalLootModifier>> register) {
        super(register);
    }

}