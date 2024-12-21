package net.azurune.lydialib.core.registry;

import net.azurune.lydialib.core.platform.Services;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.ai.attributes.Attribute;

public class LLAttributes {
    public static final Holder<Attribute> EFFECT_CHANCE_LUCK = register("effect_chance_luck", 0.0, 0, 100.0);

    private static Holder<Attribute> register(String id, double base, double min, double max) {
        return Services.REGISTRY.registerAttribute(id, base, min, max);
    }

    public static void loadAttributes() {
    }
}