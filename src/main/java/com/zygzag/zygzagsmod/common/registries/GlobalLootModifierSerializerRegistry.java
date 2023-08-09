package com.zygzag.zygzagsmod.common.registries;


import com.mojang.serialization.Codec;
import com.zygzag.zygzagsmod.common.loot.AutosmeltModifier;
import com.zygzag.zygzagsmod.common.loot.ExecutionerModifier;
import com.zygzag.zygzagsmod.common.loot.WitherPickNoDropModifier;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.zygzag.zygzagsmod.common.Main.MODID;

@SuppressWarnings("unused")
public class GlobalLootModifierSerializerRegistry extends Registry<Codec<? extends IGlobalLootModifier>> {
    public static final GlobalLootModifierSerializerRegistry INSTANCE = new GlobalLootModifierSerializerRegistry(DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, MODID));

    public GlobalLootModifierSerializerRegistry(DeferredRegister<Codec<? extends IGlobalLootModifier>> register) {
        super(register);
    }

    public static final RegistryObject<Codec<ExecutionerModifier>> EXECUTIONER = INSTANCE.register("executioner", () -> ExecutionerModifier.CODEC);
    public static final RegistryObject<Codec<AutosmeltModifier>> AUTOSMELT = INSTANCE.register("autosmelt", () -> AutosmeltModifier.CODEC);
    public static final RegistryObject<Codec<WitherPickNoDropModifier>> WITHER_PICK_NO_DROP = INSTANCE.register("wither_pickaxe_no_drop", () -> WitherPickNoDropModifier.CODEC);

}