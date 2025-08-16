package io.github.afamiliarquiet.be_a_doll.mixin;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.afamiliarquiet.be_a_doll.diary.BeAWitch;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.registry.entry.RegistryEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntity.class)
public abstract class FragmentedDamageLivingEntityMixin {
	@Definition(id = "hasStatusEffect", method = "Lnet/minecraft/entity/LivingEntity;hasStatusEffect(Lnet/minecraft/registry/entry/RegistryEntry;)Z")
	@Expression("?.hasStatusEffect(?)")
	@ModifyExpressionValue(method = "modifyAppliedDamage", at = @At("MIXINEXTRAS:EXPRESSION"))
	private boolean butWhatIfFragmentedToo(boolean original) {
		return original || hasStatusEffect(BeAWitch.FRAGMENTED);
	}

	@Definition(id = "getAmplifier", method = "Lnet/minecraft/entity/effect/StatusEffectInstance;getAmplifier()I")
	@Expression("?.getAmplifier() + 1")
	@ModifyExpressionValue(method = "modifyAppliedDamage", at = @At("MIXINEXTRAS:EXPRESSION"))
	private int subtractFragmented(int original) {
		if (hasStatusEffect(BeAWitch.FRAGMENTED)) {
			return original - (getStatusEffect(BeAWitch.FRAGMENTED).getAmplifier() + 1);
		} else {
			return original;
		}
	}

	@WrapOperation(method = "modifyAppliedDamage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/effect/StatusEffectInstance;getAmplifier()I"))
	private int waitCrapDontTouchThatNullNOOOOO(StatusEffectInstance instance, Operation<Integer> original) {
		return instance == null ? -1 : original.call(instance);
	}

	@Shadow
	public abstract boolean hasStatusEffect(RegistryEntry<StatusEffect> effect);

	@Shadow
	public abstract StatusEffectInstance getStatusEffect(RegistryEntry<StatusEffect> effect);
}
