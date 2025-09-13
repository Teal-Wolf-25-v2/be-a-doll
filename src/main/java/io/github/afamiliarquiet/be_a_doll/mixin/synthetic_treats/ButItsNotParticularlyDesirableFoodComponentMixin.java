package io.github.afamiliarquiet.be_a_doll.mixin.synthetic_treats;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import io.github.afamiliarquiet.be_a_doll.BeAMaid;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(FoodComponent.class)
public class ButItsNotParticularlyDesirableFoodComponentMixin {
	@WrapOperation(method = "onConsume", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/HungerManager;eat(Lnet/minecraft/component/type/FoodComponent;)V"))
	private void sorryDollButThatsJustMakingAMessOnTheInside(HungerManager instance, FoodComponent foodComponent, Operation<Void> original, @Local(argsOnly = true, ordinal = 0) LivingEntity user) {
		if (user instanceof PlayerEntity beWaryOfDoll && BeAMaid.isDoll(beWaryOfDoll)) {
			instance.addExhaustion(4f);
		} else {
			original.call(instance, foodComponent);
		}
	}
}
