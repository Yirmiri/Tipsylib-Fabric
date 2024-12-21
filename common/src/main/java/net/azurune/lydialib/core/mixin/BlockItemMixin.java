package net.azurune.lydialib.core.mixin;

import net.azurune.lydialib.core.registry.LLDamageTypes;
import net.azurune.lydialib.core.registry.LLEffects;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockItem.class)
public class BlockItemMixin {

    @Inject(at = @At("HEAD"), method = "placeBlock")
    public void lydialib_placeBlock(BlockPlaceContext ctx, BlockState state, CallbackInfoReturnable<Boolean> cir) {
        Player player = ctx.getPlayer();
        if (player != null) {
            if (player.hasEffect(LLEffects.CREATIVE_SHOCK) && !player.getAbilities().instabuild) {
                DamageSource damagesource = new DamageSource(player.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(LLDamageTypes.CREATIVE_SHOCK));
                player.hurt(damagesource, 1.0F + player.getEffect(LLEffects.CREATIVE_SHOCK).getAmplifier() + 1.0F);
            }
        }
    }
}
