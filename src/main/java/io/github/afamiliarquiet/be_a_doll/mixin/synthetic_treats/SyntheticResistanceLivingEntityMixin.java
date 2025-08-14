package io.github.afamiliarquiet.be_a_doll.mixin.synthetic_treats;

import io.github.afamiliarquiet.be_a_doll.BeAMaid;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class SyntheticResistanceLivingEntityMixin {
	@Inject(method = "canFreeze", at = @At("HEAD"), cancellable = true)
	private void ifDollThenNo(CallbackInfoReturnable<Boolean> cir) {
		//noinspection ConstantValue
		if ((Object)this instanceof PlayerEntity couldThisBeDoll && BeAMaid.isDoll(couldThisBeDoll)) {
			cir.setReturnValue(false);
		}
	}

	@Inject(method = "canHaveStatusEffect", at = @At("HEAD"), cancellable = true)
	private void ifDollThenNoHunger(StatusEffectInstance effect, CallbackInfoReturnable<Boolean> cir) {
		//noinspection ConstantValue
		if ((Object)this instanceof PlayerEntity couldThisBeDoll && BeAMaid.isDoll(couldThisBeDoll)) {
			if (effect.equals(StatusEffects.HUNGER)) {
				cir.setReturnValue(false);
			}
		}
	}
}
