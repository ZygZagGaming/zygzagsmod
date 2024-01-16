package com.zygzag.zygzagsmod.common.capability;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.zygzag.zygzagsmod.common.upgrade.ItemUpgrade;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ItemUpgradeAttachment {
    public static final Codec<ItemUpgradeAttachment> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.unboundedMap(ItemUpgrade.CODEC, Codec.INT).optionalFieldOf("data").forGetter((it) -> Optional.ofNullable(it.getData()))
            ).apply(instance, (data) -> data.map(ItemUpgradeAttachment::new).orElseGet(ItemUpgradeAttachment::new))
    );

    private final Map<ItemUpgrade, Integer> data;
    public ItemUpgradeAttachment() {
        this.data = new HashMap<>();
    }
    public ItemUpgradeAttachment(Map<ItemUpgrade, Integer> data) {
        this.data = new HashMap<>(data);
    }
    public int timesApplied(ItemUpgrade upgrade) { return data.getOrDefault(upgrade, 0);}
    public void setTimesApplied(ItemUpgrade upgrade, int value) {
        data.put(upgrade, value);
    }
    public void incrementApplied(ItemUpgrade upgrade) {
        setTimesApplied(upgrade, timesApplied(upgrade) + 1);
    }
    public void clear(ItemUpgrade upgrade) {
        data.remove(upgrade);
    }
    public Map<ItemUpgrade, Integer> getData() {
        return new HashMap<>(data);
    }
}
