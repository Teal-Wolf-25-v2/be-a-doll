package io.github.afamiliarquiet.be_a_doll.mixin.client;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import io.github.afamiliarquiet.be_a_doll.BeAMaid;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntityRenderer.class)
public class DollReadableNameLivingEntityRendererMixin {
	@Definition(id = "getCameraEntity", method = "Lnet/minecraft/client/MinecraftClient;getCameraEntity()Lnet/minecraft/entity/Entity;")
	@Expression("? != ?.getCameraEntity()")
	@ModifyExpressionValue(method = "hasLabel(Lnet/minecraft/entity/LivingEntity;D)Z", at = @At("MIXINEXTRAS:EXPRESSION"))
	private boolean orDoll(boolean original, @Local(name = "clientPlayerEntity") ClientPlayerEntity protagonist) {
		return original || BeAMaid.isDoll(protagonist);
	}
}
