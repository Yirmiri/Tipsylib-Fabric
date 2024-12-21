package net.azurune.lydialib.common.effect;

import net.azurune.lydialib.core.registry.LLEffects;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

public class GravityResistanceEffect extends MobEffect {
    public GravityResistanceEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean applyEffectTick(LivingEntity livingEntity, int amplifier) {
        if (livingEntity.hasEffect(MobEffects.LEVITATION) || livingEntity.hasEffect(MobEffects.SLOW_FALLING) || livingEntity.hasEffect(LLEffects.FAST_FALLING)) {
            livingEntity.removeEffect(MobEffects.LEVITATION);
            livingEntity.removeEffect(MobEffects.SLOW_FALLING);
            livingEntity.removeEffect(LLEffects.FAST_FALLING);
        }
        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }
}
