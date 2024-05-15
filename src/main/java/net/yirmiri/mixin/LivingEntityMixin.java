package net.yirmiri.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.math.BlockPos;
import net.yirmiri.mixininteraces.IStatusEffectInstanceMixin;
import net.yirmiri.register.TLDamageTypes;
import net.yirmiri.register.TLStatusEffects;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.registry.tag.DamageTypeTags;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @Shadow @Final private Map<StatusEffect, StatusEffectInstance> activeStatusEffects;
    @Shadow @Nullable public abstract StatusEffectInstance getStatusEffect(StatusEffect effect);
    @Unique @Final LivingEntity living = (LivingEntity) (Object) this;
    @Unique public Entity entity;

    public LivingEntityMixin(Entity entity) {
        this.entity = entity;
    }

    //TODO: Hyper Elasticity, Fast Falling & True Invisibility

    @Inject(at = @At("HEAD"), method = "tickStatusEffects", cancellable = true)
    public void tickStatusEffects(CallbackInfo ci) {
        for (StatusEffectInstance statusEffect : this.activeStatusEffects.values()) {
            if (statusEffect instanceof IStatusEffectInstanceMixin effect) {
                effect.setEntity((LivingEntity) (Object) this);
            }
        }
    }

    @Inject(at = @At("HEAD"), method = "canWalkOnFluid", cancellable = true)
    public void canWalkOnFluid(FluidState state, CallbackInfoReturnable<Boolean> cir) {
        if (state.getFluid() == Fluids.WATER || state.getFluid() == Fluids.FLOWING_WATER)
            if (living instanceof LivingEntity && (this.living.hasStatusEffect(TLStatusEffects.WATER_WALKING))) cir.setReturnValue(true);

        if (state.getFluid() == Fluids.LAVA || state.getFluid() == Fluids.FLOWING_LAVA)
            if (living instanceof LivingEntity && (this.living.hasStatusEffect(TLStatusEffects.LAVA_WALKING))) cir.setReturnValue(true);
    }

    @Inject(at = @At("HEAD"), method = "damage", cancellable = true)
    public void damage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (source.isIn(DamageTypeTags.IS_FIRE) && living.hasStatusEffect(TLStatusEffects.PYROMANIAC)) {
            if (living.age % 20 == 0) {
                if (living.getHealth() < living.getMaxHealth()) {
                    living.heal(2.0F * (getStatusEffect(TLStatusEffects.PYROMANIAC).getAmplifier() + 1.0F));
                }
            }
            cir.setReturnValue(false);
        }

        if (living.hasStatusEffect(TLStatusEffects.TRAIL_BLAZING) || living.hasStatusEffect(TLStatusEffects.LAVA_WALKING) && source.isIn(DamageTypeTags.IS_FIRE))
            cir.setReturnValue(false);

        if (living.hasStatusEffect(TLStatusEffects.BURNING_THORNS)) {
            Entity entity = source.getAttacker();
            if (entity != null) entity.setOnFireFor(5 * (getStatusEffect(TLStatusEffects.BURNING_THORNS).getAmplifier() + 1));
        }

        if (living.hasStatusEffect(TLStatusEffects.RETALIATION)) {
            Entity entity = source.getAttacker();
            if (entity != null) entity.damage(TLDamageTypes.of(entity.getWorld(), TLDamageTypes.RETALIATION), 1.0F + (getStatusEffect(TLStatusEffects.RETALIATION).getAmplifier() + 1));
        }

        if (living.hasStatusEffect(TLStatusEffects.TOUGH_SKIN) && source.isIn(DamageTypeTags.IS_EXPLOSION))
            cir.setReturnValue(false);
    }

    @Inject(at = @At("HEAD"), method = "heal", cancellable = true)
    public void heal(float amount, CallbackInfo ci) {
        if (living.hasStatusEffect(TLStatusEffects.BLEEDING)) ci.cancel();
    }

    @Inject(at = @At("HEAD"), method = "tick")
    public void tick(CallbackInfo ci) {
        BlockPos pos = living.getBlockPos();
        if (living.hasStatusEffect(TLStatusEffects.TRAIL_BLAZING) && living.getWorld().getBlockState(pos).isAir()) {
            living.getWorld().setBlockState(pos, Blocks.FIRE.getDefaultState());
        }
    }

    @Inject(at = @At("HEAD"), method = "fall", cancellable = true)
    public void fall(double heightDifference, boolean onGround, BlockState state, BlockPos landedPosition, CallbackInfo ci) {
        if (living.hasStatusEffect(TLStatusEffects.STEEL_FEET)) ci.cancel();
    }
}