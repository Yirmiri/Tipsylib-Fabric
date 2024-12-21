package net.azurune.lydialib.core.registry;

import net.azurune.lydialib.LydiaLib;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffect;

public class LLTags {
    public static class EffectTags {
        public static final TagKey<MobEffect> CHRONOS_BLACKLISTED = tag("chronos_blacklisted");

        private static TagKey<MobEffect> tag(String id) {
            return TagKey.create(Registries.MOB_EFFECT, LydiaLib.id(id));
        }
    }

    public static class DamageTypeTags {
        public static final TagKey<DamageType> BYPASSES_DODGE = tag("bypasses_dodge");

        private static TagKey<DamageType> tag(String id) {
            return TagKey.create(Registries.DAMAGE_TYPE, LydiaLib.id(id));
        }
    }
}
