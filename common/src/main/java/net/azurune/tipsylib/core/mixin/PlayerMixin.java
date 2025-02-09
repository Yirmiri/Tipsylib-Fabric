package net.azurune.tipsylib.core.mixin;

import net.azurune.tipsylib.core.registry.TLAttributes;
import net.azurune.tipsylib.core.registry.TLEffects;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;
import java.util.Random;

@Mixin(Player.class)
public abstract class PlayerMixin {

    @Unique @Final Player player = (Player) (Object) this;
    private static Random random = new Random();

    private boolean doubleJump = false;

    @Inject(at = @At("TAIL"), method = "attack", cancellable = true)
    public void tipsylib_attack(Entity entity, CallbackInfo ci) {
        double effectLuck = player.getAttributeValue(TLAttributes.EFFECT_CHANCE_LUCK);

        float amount = (float) player.getAttributeValue(Attributes.ATTACK_DAMAGE);
        DamageSource source = player.damageSources().playerAttack(player);

        double lifestealAmount = player.getAttributeValue(TLAttributes.LIFESTEAL_HEAL_AMOUNT);
        double lifestealChance = player.getAttributeValue(TLAttributes.LIFESTEAL_CHANCE);

        if (entity instanceof LivingEntity livingEntity) {
            if (lifestealChance != 0 && random.nextDouble(100.0) < lifestealChance + effectLuck * 10 && player.isAlive()) {
                player.heal((float) lifestealAmount);
                player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.SOUL_ESCAPE, SoundSource.PLAYERS, 1.0F, 1.0F);
            }

            double criticalStrikeChance = player.getAttributeValue(TLAttributes.CRITICAL_STRIKE_CHANCE);
            float criticalStrikeMultiplier = (float) player.getAttributeValue(TLAttributes.CRITICAL_STRIKE_DAMAGE_MULTIPLIER);
            if (criticalStrikeChance != 0 && random.nextDouble(100.0) < criticalStrikeChance + effectLuck * 10 && player.isAlive()) {
                livingEntity.hurt(source, (amount * criticalStrikeMultiplier));
                player.playSound(SoundEvents.ARROW_HIT_PLAYER, 1.0F, 1.0F);
            }

            if (player.hasEffect(TLEffects.ENIGMA)) {
                player.removeEffect(TLEffects.ENIGMA);
            }

//            if (player.hasEffect(TLEffects.SHATTERING_STRIKE) && random.nextDouble(100.0) < 25.0 + effectLuck * 10 && player.isAlive()) {
//                if (!livingEntity.hasEffect(TLEffects.FRACTURING)) {
//                    livingEntity.addEffect(new MobEffectInstance(TLEffects.FRACTURING, 4 + player.getEffect(TLEffects.SHATTERING_STRIKE).getAmplifier(), 0));
//                } else if (livingEntity.hasEffect(TLEffects.FRACTURING) && !livingEntity.getEffect(TLEffects.FRACTURING).isInfiniteDuration()) {
//                    livingEntity.forceAddEffect(new MobEffectInstance(TLEffects.FRACTURING, livingEntity.getEffect(TLEffects.FRACTURING).getDuration() + 1, livingEntity.getEffect(TLEffects.FRACTURING).getAmplifier()), livingEntity);
//                }
//            }
        }
    }
//TODO: use rewritten code from Aurynium to allow infinite jumps
    @Inject(at = @At("HEAD"), method = "tick", cancellable = true)
    public void tipsylib_tickMovement(CallbackInfo ci) {
        if (player.hasEffect(TLEffects.AIR_JUMPER)) {
            if (player.onGround()) {
                doubleJump = true;
            }

            if (player.getDeltaMovement().y < 0) {
                if (Minecraft.getInstance().options.keyJump.isDown() && !player.getAbilities().flying && doubleJump && !player.onClimbable() && !player.onGround()) {
                    player.jumpFromGround();
                    doubleJump = false;
                    player.level().playSound(player, player.blockPosition(), SoundEvents.POWDER_SNOW_BREAK, SoundSource.PLAYERS, 2.0F, 1.0F);
                    player.level().addParticle(ParticleTypes.CLOUD, player.getRandomX(1), player.getRandomY(), player.getRandomZ(1), 0, 0, 0);
                }
            }
        }
    }

    @Inject(at = @At("HEAD"), method = "isHurt", cancellable = true)
    public void tipsylib_isHurt(CallbackInfoReturnable<Float> cir) {
        if (player.hasEffect(TLEffects.INTERNAL_BLEEDING)) {
            cir.cancel();
        }
    }

    @Inject(at = @At("HEAD"), method = "isReducedDebugInfo", cancellable = true)
    public void tipsylib_hasReducedDebugInfo(CallbackInfoReturnable<Boolean> cir) {
        if (player.hasEffect(TLEffects.CONFUSION)) {
            cir.setReturnValue(true);
        }
    }
}