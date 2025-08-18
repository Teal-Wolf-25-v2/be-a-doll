package io.github.afamiliarquiet.be_a_doll.mixin.shoulder_riding;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.Entity;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PlayerManager.class)
public class OopsDollDeletedYouPlayerManagerMixin {
	@ModifyExpressionValue(method = "remove", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;hasPlayerRider()Z"))
	private boolean waitWaitStopDontRemoveThatPlayer(boolean original, @Local(argsOnly = true) ServerPlayerEntity disconnected, @Local Entity entity) {
		return original && entity.streamPassengersAndSelf().noneMatch(current -> current != null && current.isPlayer() && current != disconnected);
	}
}
