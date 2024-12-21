package net.azurune.lydialib.core.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import net.azurune.lydialib.LydiaLib;
import net.azurune.lydialib.core.registry.LLEffects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public abstract class GuiMixin {
    @Inject(method = "renderHeart", at = @At("HEAD"), cancellable = true)
    private void lydialib_renderHeart(GuiGraphics guiGraphics, Gui.HeartType type, int x, int y, boolean hardcore, boolean blinking, boolean half, CallbackInfo ci) {
        if (type == Gui.HeartType.NORMAL && Minecraft.getInstance().cameraEntity instanceof Player player && (player.hasEffect(LLEffects.CONFUSION))) {
            RenderSystem.enableBlend();
            ResourceLocation texture;
//TODO CHANGE LATER WAS JUST TRYING SMTHING
            ResourceLocation decayFullTexture = LydiaLib.id("hud/heart/decay_full");
            ResourceLocation decayHalfTexture = LydiaLib.id("hud/heart/decay_half");
            ResourceLocation decayHardcoreFullTexture = LydiaLib.id("hud/heart/decay_hardcore_full");
            ResourceLocation decayHardcoreHalfTexture = LydiaLib.id("hud/heart/decay_hardcore_half");
            ResourceLocation decayFullBlinkingTexture = LydiaLib.id("hud/heart/decay_full_blinking");
            ResourceLocation decayHalfBlinkingTexture = LydiaLib.id("hud/heart/decay_half_blinking");
            ResourceLocation decayHardcoreFullBlinkingTexture = LydiaLib.id("hud/heart/decay_hardcore_full_blinking");
            ResourceLocation decayHardcoreHalfBlinkingTexture = LydiaLib.id("hud/heart/decay_hardcore_half_blinking");

            if (!hardcore) {
                if (!half) {
                    texture = blinking ? decayFullBlinkingTexture : decayFullTexture;
                } else {
                    texture = blinking ? decayHalfBlinkingTexture : decayHalfTexture;
                }
            } else if (!half) {
                texture = blinking ? decayHardcoreFullBlinkingTexture : decayHardcoreFullTexture;
            } else {
                texture = blinking ? decayHardcoreHalfBlinkingTexture : decayHardcoreHalfTexture;
            }

            guiGraphics.blitSprite(texture, x, y, 9, 9);
            RenderSystem.disableBlend();
            ci.cancel();
        }
    }
}
