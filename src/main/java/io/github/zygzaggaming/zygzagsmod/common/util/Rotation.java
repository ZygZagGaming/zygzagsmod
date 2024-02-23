package io.github.zygzaggaming.zygzagsmod.common.util;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.world.phys.Vec3;

import java.util.Arrays;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

public interface Rotation {
    Codec<Rotation> CODEC = Codec.either(
            Codec.either(
                    LimitedRotation.CODEC,
                    LerpedRotation.CODEC
            ),
            SimpleRotation.CODEC
    ).flatXmap(
            (it) -> it
                    .left()
                    .flatMap((left) -> left
                            .left()
                            .map((ll) -> DataResult.success((Rotation) ll))
                            .or(() -> left.right().map(DataResult::success))
                    ).orElse(it
                            .right()
                            .map((r) -> DataResult.success((Rotation) r))
                            .orElse(DataResult.error(() -> "wtf bruh"))
                    ),
            (it) -> it instanceof LimitedRotation cast ? DataResult.success(Either.left(Either.left(cast))) :
                    it instanceof LerpedRotation cast ? DataResult.success(Either.left(Either.right(cast))) :
                    it instanceof SimpleRotation cast ? DataResult.success(Either.right(cast)) :
                    DataResult.error(() -> "wtf")
    );
    Codec<Rotation> LOSSY_CODEC = SimpleRotation.CODEC.flatComapMap(
            (it) -> it,
            (it) -> it instanceof SimpleRotation cast ? DataResult.success(cast) : DataResult.error(() -> "Unrecognized rotation type")
    );
    default void tick() { }
    void setXRot(float value);

    default float getXRot(float partialTick) {
        return getXRot();
    }

    float getXRot();

    void setYRot(float value);

    default float getYRot(float partialTick) {
        return getYRot();
    }

    float getYRot();

    default float[] get() {
        return new float[]{getXRot(), getYRot()};
    }
    default void set(float[] values) {
        set(values[0], values[1]);
        if (Float.isNaN(values[0]) || Float.isNaN(values[1])) throw new IllegalArgumentException("NAN rotation values " + Arrays.toString(values));
    }
    default void set(float xRot, float yRot) {
        setXRot(xRot);
        setYRot(yRot);
    }

    default void setXRotDegrees(float value) {
        setXRot(GeneralUtil.degreesToRadians(value));
    }

    default float getXRotDegrees(float partialTick) {
        return GeneralUtil.radiansToDegrees(getXRot(partialTick));
    }

    default float getXRotDegrees() {
        return GeneralUtil.radiansToDegrees(getXRot());
    }

    default void setYRotDegrees(float value) {
        setYRot(GeneralUtil.degreesToRadians(value));
    }

    default float getYRotDegrees(float partialTick) {
        return GeneralUtil.radiansToDegrees(getYRot(partialTick));
    }

    default float getYRotDegrees() {
        return GeneralUtil.radiansToDegrees(getYRot());
    }
    default float[] direction() {
        return new float[]{(float) (sin(getYRot()) * cos(getXRot())), (float) sin(getXRot()), (float) (cos(getYRot()) * cos(getXRot()))};
    }
    default Vec3 directionVector() {
        return new Vec3(sin(getYRot()) * cos(getXRot()), sin(getXRot()), cos(getYRot()) * cos(getXRot()));
    }

    default float angleDifference(Rotation other) {
        return (float) Math.acos(GeneralUtil.dot(direction(), other.direction()));
    }

    default void setOldXRot(float value) { }
    default void setOldYRot(float value) { }
}
