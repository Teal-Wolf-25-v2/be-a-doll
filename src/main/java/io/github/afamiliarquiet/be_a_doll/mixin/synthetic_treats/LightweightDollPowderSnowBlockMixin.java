package io.github.afamiliarquiet.be_a_doll.mixin.synthetic_treats;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import io.github.afamiliarquiet.be_a_doll.BeAMaid;
import net.minecraft.block.PowderSnowBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PowderSnowBlock.class)
public abstract class LightweightDollPowderSnowBlockMixin {
	@Definition(id = "isIn", method = "Lnet/minecraft/entity/EntityType;isIn(Lnet/minecraft/registry/tag/TagKey;)Z")
	@Expression("?.isIn(?)")
	@ModifyExpressionValue(method = "canWalkOnPowderSnow", at = @At("MIXINEXTRAS:EXPRESSION"))
	private static boolean dollsCan(boolean original, @Local(argsOnly = true) Entity entity) {
		return original || (entity instanceof PlayerEntity dollRequiredAhead && BeAMaid.isDoll(dollRequiredAhead));
	}
}
