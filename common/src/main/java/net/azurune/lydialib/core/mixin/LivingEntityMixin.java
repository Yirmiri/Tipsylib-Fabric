package net.azurune.lydialib.core.mixin;

import net.azurune.lydialib.common.effect.FracturingEffect;
import net.azurune.lydialib.common.util.StatusEffectInstance;
import net.azurune.lydialib.core.registry.LLAttributes;
import net.azurune.lydialib.core.registry.LLDamageTypes;
import net.azurune.lydialib.core.registry.LLEffects;
import net.azurune.lydialib.core.registry.LLTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.InstantenousMobEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;
import java.util.Map;
import java.util.Random;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @Shadow @Final private Map<MobEffect, MobEffectInstance> activeEffects;
    @Shadow @Nullable public abstract MobEffectInstance getEffect(Holder<MobEffect> effect);

    @Shadow public abstract Collection<MobEffectInstance> getActiveEffects();

    @Unique @Final LivingEntity living = (LivingEntity) (Object) this;
    @Unique public Level level;
    private static Random random = new Random();

    @Inject(at = @At("TAIL"), method = "createLivingAttributes")
    private static void createLivingAttributes(CallbackInfoReturnable<AttributeSupplier.Builder> cir) {
        cir.getReturnValue()
                .add(Attributes.LUCK)
                .add(LLAttributes.BACKLASH_CHANCE)
                .add(LLAttributes.BACKLASH_DAMAGE_PERCENT)
                .add(LLAttributes.ARROW_DAMAGE_MODIFIER)
                .add(LLAttributes.DODGE_CHANCE)
                .add(LLAttributes.LIFESTEAL_CHANCE)
                .add(LLAttributes.LIFESTEAL_HEAL_AMOUNT)
                .add(LLAttributes.VULNERABILITY_CHANCE)
                .add(LLAttributes.VULNERABILITY_MODIFIER)
                .add(LLAttributes.RETALIATION_CHANCE)
                .add(LLAttributes.RETALIATION_DAMAGE_AMOUNT)
                .add(LLAttributes.BURNING_RETALIATION_LENGTH)
                .add(LLAttributes.BURNING_RETALIATION_CHANCE)
                .add(LLAttributes.CRITICAL_STRIKE_CHANCE)
                .add(LLAttributes.CRITICAL_STRIKE_DAMAGE_MULTIPLIER)
                .add(LLAttributes.OVERHEAL_AMOUNT)
                .add(LLAttributes.OVERHEAL_CHANCE)
                .add(LLAttributes.OVERHEAL_TICK_LENGTH)
                .add(LLAttributes.EFFECT_CHANCE_LUCK)
        ;
    }

    @Inject(at = @At("HEAD"), method = "onEffectRemoved", cancellable = true)
    public void lydialib_onEffectRemoved(MobEffectInstance instance, CallbackInfo ci) {
        if (instance.is(LLEffects.FRACTURING)) {
            DamageSource damagesource = new DamageSource(living.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(LLDamageTypes.FRACTURING));
            if (instance.getAmplifier() == 0 && FracturingEffect.getFracturingTicks() > living.getHealth()) {
                living.hurt(damagesource, living.getHealth() - 1);
            } else {
                living.hurt(damagesource, FracturingEffect.getFracturingTicks());
            }
            living.level().playSound(living, living.blockPosition(), SoundEvents.OMINOUS_BOTTLE_DISPOSE, SoundSource.PLAYERS, 1.0F, 1.0F);
        }
    }

    @Inject(at = @At("HEAD"), method = "tickEffects")
    public void lydialib_tickEffects(CallbackInfo ci) {
        for (MobEffectInstance statusEffect : this.activeEffects.values()) {
            if (!statusEffect.getEffect().is(LLTags.EffectTags.CHRONOS_BLACKLISTED) && !(statusEffect.getEffect() instanceof InstantenousMobEffect)) {
                if (statusEffect instanceof StatusEffectInstance effect) {
                    effect.setEntity((LivingEntity) (Object) this);
                }
            }

            if (statusEffect.getEffect() == LLEffects.CHRONOS) {
                if (this.activeEffects.values().size() > 2) {
                    living.forceAddEffect(new MobEffectInstance(LLEffects.CHRONOS, statusEffect.getDuration() - (this.activeEffects.values().size() - 2), 0), living);
                }
            }

            if (living.hasEffect(LLEffects.TEMPUS)) {
                int tempusAmplifier = this.activeEffects.get(LLEffects.TEMPUS).getAmplifier();
                if (!living.hasEffect(LLEffects.CHRONOS)) {
                    if (statusEffect.getEffect() != LLEffects.TEMPUS) {
                        living.forceAddEffect(new MobEffectInstance(statusEffect.getEffect(), statusEffect.getDuration() - (tempusAmplifier + 1), 0), living);
                    }
                }

                else if (living.hasEffect(LLEffects.CHRONOS)) {
                    if (statusEffect.getEffect() == LLEffects.CHRONOS) {
                        living.forceAddEffect(new MobEffectInstance(statusEffect.getEffect(), statusEffect.getDuration() - (tempusAmplifier + 1), 0), living);
                    }
                }
            }
        }
    }

    @Inject(at = @At("HEAD"), method = "canStandOnFluid", cancellable = true)
    public void lydialib_canStandOnFluid(FluidState state, CallbackInfoReturnable<Boolean> cir) {
        if (!living.isCrouching()) {
            if (state.getType() == Fluids.WATER || state.getType() == Fluids.FLOWING_WATER)
                if (living != null && (this.living.hasEffect(LLEffects.WATER_WALKING))) cir.setReturnValue(true);

            if (state.getType() == Fluids.LAVA || state.getType() == Fluids.FLOWING_LAVA)
                if (living != null && (this.living.hasEffect(LLEffects.LAVA_WALKING))) cir.setReturnValue(true);
        }
    }

    @Inject(at = @At("HEAD"), method = "getWaterSlowDown", cancellable = true)
    public void lydialib_getWaterSlowDown(CallbackInfoReturnable<Float> cir) {
        if (!living.isCrouching() && living.hasEffect(LLEffects.WATER_WALKING)) {
            cir.setReturnValue(1.0F + living.getEffect(LLEffects.WATER_WALKING).getAmplifier());
        }
    }

    @Inject(at = @At("HEAD"), method = "getJumpBoostPower", cancellable = true)
    public void lydialib_getJumpBoostPower(CallbackInfoReturnable<Float> cir) {
        if (living.hasEffect(LLEffects.FAST_FALLING)) {
            cir.setReturnValue(-0.1F * this.getEffect(LLEffects.FAST_FALLING).getAmplifier() -0.1F);
        }
    }

    @Inject(at = @At("HEAD"), method = "canFreeze", cancellable = true)
    public void lydialib_canFreeze(CallbackInfoReturnable<Boolean> cir) {
        if (living.hasEffect(LLEffects.FREEZE_RESISTANCE)) {
            cir.setReturnValue(false);
        }
    }

    @Inject(at = @At("HEAD"), method = "hurt", cancellable = true)
    public void lydialib_hurt(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        double effectLuck = living.getAttributeValue(LLAttributes.EFFECT_CHANCE_LUCK);
        if (living.hasEffect(LLEffects.TOUGH_SKIN) && source.is(net.minecraft.tags.DamageTypeTags.IS_EXPLOSION)) {
            cir.setReturnValue(false);
        }

        if (living.hasEffect(LLEffects.FREEZE_RESISTANCE) && source.is(net.minecraft.tags.DamageTypeTags.IS_FREEZING)) {
            cir.setReturnValue(false);
        }

        if (living.hasEffect(LLEffects.STEEL_FEET) && source.is(net.minecraft.tags.DamageTypeTags.IS_FALL)) {
            cir.setReturnValue(false);
        }

        double dodgeChance = living.getAttributeValue(LLAttributes.DODGE_CHANCE);
        if (!source.is(LLTags.DamageTypeTags.BYPASSES_DODGE) && dodgeChance != 0 && living.isAlive() && random.nextDouble(100.0) < dodgeChance + effectLuck * 10) {
            living.level().playSound(null, living.getX(), living.getY(), living.getZ(), SoundEvents.ARMOR_EQUIP_GENERIC, SoundSource.PLAYERS, 1.0F, 1.0F);
            cir.cancel();
        }
    }

    @Inject(at = @At("HEAD"), method = "createWitherRose", cancellable = true)
    public void lydialib_onKilledBy(LivingEntity adversary, CallbackInfo ci) {
        double effectLuck = living.getAttributeValue(LLAttributes.EFFECT_CHANCE_LUCK);
        if (adversary != null && adversary.isAlive()) {
            double overhealChance = adversary.getAttributeValue(LLAttributes.OVERHEAL_CHANCE);
            int overhealAmount = (int) adversary.getAttributeValue(LLAttributes.OVERHEAL_AMOUNT);
            int overhealLength = (int) adversary.getAttributeValue(LLAttributes.OVERHEAL_TICK_LENGTH);
            if (overhealChance != 0 && random.nextDouble(100.0) < overhealChance + effectLuck * 10) {
                adversary.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, overhealLength, overhealAmount));
            }
        }
    }

    @ModifyVariable(at = @At("HEAD"), method = "hurt", argsOnly = true)
    public float lydialib_shatterSpleen(float amount) {
        double effectLuck = living.getAttributeValue(LLAttributes.EFFECT_CHANCE_LUCK);
        double vulnerabilityChance = living.getAttributeValue(LLAttributes.VULNERABILITY_CHANCE);
        float vulnerabilityModifier = (float) living.getAttributeValue(LLAttributes.VULNERABILITY_MODIFIER);
        if (vulnerabilityChance != 0 && living.isAlive() && random.nextDouble(100.0) < vulnerabilityChance + effectLuck * 10) {
            return amount + amount * vulnerabilityModifier;
        }
        return amount;
    }

    @Inject(at = @At("TAIL"), method = "getDamageAfterMagicAbsorb")
    public void lydialib_modifyAppliedDamage(DamageSource source, float amount, CallbackInfoReturnable<Float> cir) {
        Entity entity = source.getEntity();
        if (entity instanceof LivingEntity attacker && attacker.isAlive()) {
            double effectLuck = living.getAttributeValue(LLAttributes.EFFECT_CHANCE_LUCK);
            double backlashChance = living.getAttributeValue(LLAttributes.BACKLASH_CHANCE);
            double backlashDamagePercent = living.getAttributeValue(LLAttributes.BACKLASH_DAMAGE_PERCENT);
            if (backlashChance != 0 && random.nextDouble(100.0) < backlashChance + effectLuck * 10) {
                DamageSource damagesource = new DamageSource(entity.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(LLDamageTypes.BACKLASH));
                attacker.hurt(damagesource, (amount * (float) backlashDamagePercent));
            }

            double retaliationChance = living.getAttributeValue(LLAttributes.RETALIATION_CHANCE);
            double retaliationDamageAmount = living.getAttributeValue(LLAttributes.RETALIATION_DAMAGE_AMOUNT);
            if (retaliationChance != 0 && random.nextDouble(100.0) < retaliationChance + effectLuck * 10) {
                DamageSource damagesource = new DamageSource(entity.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(LLDamageTypes.RETALIATION));
                attacker.hurt(damagesource, (float) retaliationDamageAmount);
            }

            double burningRetaliationChance = living.getAttributeValue(LLAttributes.BURNING_RETALIATION_CHANCE);
            double burningRetaliationLength = living.getAttributeValue(LLAttributes.BURNING_RETALIATION_LENGTH);
            if (burningRetaliationChance != 0 && random.nextDouble(100.0) < burningRetaliationChance + effectLuck * 10) {
                attacker.igniteForSeconds((float) burningRetaliationLength);
            }
        }
    }

    @Inject(at = @At("HEAD"), method = "heal", cancellable = true)
    public void lydialib_heal(float amount, CallbackInfo ci) {
        if (living.hasEffect(LLEffects.BLEEDING)) {
            ci.cancel();
        }
    }

    @Inject(at = @At("HEAD"), method = "canBeSeenByAnyone", cancellable = true)
    public void lydialib_canBeSeenByAnyone(CallbackInfoReturnable<Boolean> cir) {
        if (living.hasEffect(LLEffects.ENIGMA)) cir.cancel();
    }

    @Inject(at = @At("HEAD"), method = "tick")
    public void lydialib_tick(CallbackInfo ci) {
        BlockPos pos = living.blockPosition();
        if (living.hasEffect(LLEffects.TRAIL_BLAZING) && living.level().getBlockState(pos).isAir()) {
            living.level().setBlockAndUpdate(pos, Blocks.FIRE.defaultBlockState());
        }

        if (living.getBlockStateOn().is(BlockTags.FIRE) && living.hasEffect(LLEffects.PYROMANIAC)) {
            if (living.tickCount % 20 == 0) {
                if (living.getHealth() < living.getMaxHealth()) {
                    living.heal((getEffect(LLEffects.PYROMANIAC).getAmplifier() + 1.0F));
                }
            }
        }
    }
}
