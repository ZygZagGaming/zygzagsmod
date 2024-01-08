package com.zygzag.zygzagsmod.common.util;

public class Vectorf {
    float[] values;
    int order;
    public Vectorf(float... values) {
        this.values = values;
        this.order = values.length;
    }

    public float x() {
        return component(0);
    }
    public float y() {
        return component(1);
    }
    public float z() {
        return component(2);
    }

    public float component(int index) {
        return values[index];
    }

    public Vectorf plus(Vectorf other) {
        if (other.order > order) return other.plus(this);
        float[] output = new float[order];
        int i;
        for (i = 0; i < other.order; i++) output[i] = this.component(i) + other.component(i);
        for (; i < this.order; i++) output[i] = this.component(i);
        return new Vectorf(output);
    }
    public Vectorf minus(Vectorf other) {
        return plus(other.times(-1));
    }

    public Vectorf times(float scalar) {
        if (scalar == 1) return this;
        float[] output = new float[order];
        for (int i = 0; i < order; i++) output[i] = this.component(i) * scalar;
        return new Vectorf(output);
    }

    public float dotProduct(Vectorf other) {
        float output = 0;
        for (int i = 0; i < order; i++) output += this.component(i) * other.component(i);
        return output;
    }

    public Vectorf crossProduct(Vectorf other) {
        // Assumes order 3
        return new Vectorf(
                y() * other.z() - z() * other.y(),
                z() * other.x() - x() * other.z(),
                x() * other.y() - y() * other.x()
        );
    }
}
