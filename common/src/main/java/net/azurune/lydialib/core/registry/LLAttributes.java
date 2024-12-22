package net.azurune.lydialib.core.registry;

import net.azurune.lydialib.LydiaLib;
import net.azurune.lydialib.core.platform.Services;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.ai.attributes.Attribute;

public class LLAttributes {
    public static final Holder<Attribute> EFFECT_CHANCE_LUCK = Services.REGISTRY.registerAttribute(LydiaLib.MOD_ID, "effect_chance_luck", 0.0, 0, 100.0);

    public static void loadAttributes() {
    }
}