package io.github.afamiliarquiet.be_a_doll.mixin.synthetic_treats;

import io.github.afamiliarquiet.be_a_doll.BeAMaid;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class DollsCantEatPlayerEntityMixin {
	@Inject(at = @At("HEAD"), method = "canConsume", cancellable = true)
	private void notIfDoll(boolean ignoreHunger, CallbackInfoReturnable<Boolean> cir) {
		if (BeAMaid.isDoll((PlayerEntity)(Object)this)) {
			cir.setReturnValue(false);
		}
	}
}
