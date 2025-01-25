package net.azurune.lydialib.common.util;

import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public class LydiaLibRegister <T> implements Supplier<T> {
    private final Supplier<T> value;
    private final ResourceLocation id;

    public LydiaLibRegister(String id, Supplier<T> value) {
        this.value = value;
        this.id = LydiaLibModRegistrar.id(id, 3);
    }

    @Override
    public T get() {
        return value.get();
    }
}
