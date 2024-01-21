package com.zygzag.zygzagsmod.common.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.ArrayList;
import java.util.List;

public record RotationArray(Rotation[] backing) {
    public static final Codec<RotationArray> LOSSY_CODEC = Rotation.LOSSY_CODEC.listOf().xmap(
            (list) -> new RotationArray(list.toArray(new Rotation[0])),
            (arr) -> List.of(arr.backing)
    );
    public void tick() {
        for (Rotation rotation : backing) rotation.tick();
    }
    public Rotation get(int index) {
        return backing[index];
    }
}
