package io.github.zygzaggaming.zygzagsmod.common.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

public sealed interface PositionAnchor extends Function<Level, Vec3> permits PositionAnchor.Static, PositionAnchor.EntityAnchor {
    Codec<PositionAnchor> CODEC = PositionAnchorType.CODEC.dispatch(PositionAnchor::type, PositionAnchorType::mapCodec);
    enum PositionAnchorType {
        STATIC(Static.MAP_CODEC),
        ENTITY_ANCHOR(EntityAnchor.MAP_CODEC);
        public static final Codec<PositionAnchorType> CODEC = GeneralUtil.enumCodec(PositionAnchorType.class);
        final MapCodec<? extends PositionAnchor> mapCodec;
        PositionAnchorType(MapCodec<? extends PositionAnchor> mapCodec) {
            this.mapCodec = mapCodec;
        }
        MapCodec<? extends PositionAnchor> mapCodec() {
            return mapCodec;
        }
    }

    PositionAnchorType type();

    record Static(Vec3 pos) implements PositionAnchor {
        public static final MapCodec<Static> MAP_CODEC = Vec3.CODEC.xmap(Static::new, Static::pos).fieldOf("pos");
        @Override
        public Vec3 apply(Level world) {
            return pos;
        }

        @Override
        public PositionAnchorType type() {
            return PositionAnchorType.STATIC;
        }
    }

    record EntityAnchor(UUID uuid, Vec3 fallback, AnchorPosition where) implements PositionAnchor {
        public static final MapCodec<EntityAnchor> MAP_CODEC = RecordCodecBuilder.mapCodec(inst ->
                inst.group(
                        GeneralUtil.UUID_CODEC.fieldOf("entity_uuid").forGetter(EntityAnchor::uuid),
                        Vec3.CODEC.fieldOf("fallback").forGetter(EntityAnchor::fallback),
                        AnchorPosition.CODEC.fieldOf("where").forGetter(EntityAnchor::where)
                ).apply(inst, EntityAnchor::new)
        );

        @Override
        public PositionAnchorType type() {
            return PositionAnchorType.ENTITY_ANCHOR;
        }

        public enum AnchorPosition {
            MIN(it -> it.getBoundingBox().getMinPosition()),
            MAX(it -> it.getBoundingBox().getMaxPosition()),
            CENTER(it -> it.getBoundingBox().getCenter()),
            BOTTOM_CENTER(Entity::position),
            EYES(Entity::getEyePosition);

            public static final Codec<AnchorPosition> CODEC = GeneralUtil.enumCodec(AnchorPosition.class);
            final Function<Entity, Vec3> positionGetter;
            AnchorPosition(Function<Entity, Vec3> positionGetter) {
                this.positionGetter = positionGetter;
            }
        }

        @Override
        public Vec3 apply(Level world) {
            return Optional.ofNullable(world.getEntities().get(uuid)).map(where.positionGetter).orElse(fallback);
        }
    }
}
