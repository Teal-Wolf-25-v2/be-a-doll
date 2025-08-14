package io.github.afamiliarquiet.be_a_doll.mixin.synthetic_treats;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import io.github.afamiliarquiet.be_a_doll.BeAMaid;
import io.github.afamiliarquiet.be_a_doll.diary.BeAResearcher;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.entry.RegistryEntry;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(DamageSource.class)
public class SyntheticDeathMessagesDamageSourceMixin {
	@Shadow
	@Final
	private RegistryEntry<DamageType> type;

	@Definition(id = "msgId", method = "Lnet/minecraft/entity/damage/DamageType;msgId()Ljava/lang/String;")
	@Expression("?.msgId()")
	@ModifyExpressionValue(method = "getDeathMessage", at = @At("MIXINEXTRAS:EXPRESSION"))
	private String addDollQualifierIfIWant(String original, @Local(argsOnly = true) LivingEntity killed) {
		if (killed instanceof PlayerEntity beWaryOfDoll && BeAMaid.isDoll(beWaryOfDoll)) {
			if (type.isIn(BeAResearcher.DOLL_MODIFIES_MESSAGE)) {
				return "doll." + original;
			}
		}
		return original;
	}
}
