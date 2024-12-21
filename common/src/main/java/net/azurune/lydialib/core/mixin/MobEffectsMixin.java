package net.azurune.lydialib.core.mixin;

import net.azurune.lydialib.common.effect.NoSpecialEffect;
import net.azurune.lydialib.core.platform.Services;
import net.azurune.lydialib.core.registry.LLAttributes;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MobEffects.class)
public class MobEffectsMixin {

    @Inject(at = @At("HEAD"), method = "register", cancellable = true)
    private static void lydialib_register(String key, MobEffect effect, CallbackInfoReturnable<Holder<MobEffect>> cir) {
        if (key.equals("luck")) {
            cir.setReturnValue(lydialib_registerEffect(key, new NoSpecialEffect(MobEffectCategory.BENEFICIAL, 5882118)
                    .addAttributeModifier(Attributes.LUCK, ResourceLocation.withDefaultNamespace("effect.luck"), 1.0, AttributeModifier.Operation.ADD_VALUE)
                    .addAttributeModifier(LLAttributes.EFFECT_CHANCE_LUCK, ResourceLocation.withDefaultNamespace("effect.effect_chance_luck"), 1.0, AttributeModifier.Operation.ADD_VALUE)));
        }
    }

    @Unique
    private static Holder<MobEffect> lydialib_registerEffect(String id, MobEffect mobEffect) {
        return Services.REGISTRY.registerVanillaEffect(id, mobEffect);
    }
}
