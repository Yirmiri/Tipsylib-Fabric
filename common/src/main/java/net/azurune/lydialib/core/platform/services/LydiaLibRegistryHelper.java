package net.azurune.lydialib.core.platform.services;

import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.ai.attributes.Attribute;

public interface LydiaLibRegistryHelper {


    Holder<MobEffect> registerEffect(String id, MobEffect mobEffect);

    Holder<MobEffect> registerVanillaEffect(String id, MobEffect mobEffect);

    Holder<Attribute> registerAttribute(String id, double base, double min, double max);
}
