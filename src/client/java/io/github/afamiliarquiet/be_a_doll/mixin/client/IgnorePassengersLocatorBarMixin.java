package io.github.afamiliarquiet.be_a_doll.mixin.client;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.bar.LocatorBar;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.util.UUID;

@Mixin(LocatorBar.class)
public abstract class IgnorePassengersLocatorBarMixin {
	@Final
	@Shadow
	private MinecraftClient client;

	@Definition(id = "equals", method = "Ljava/util/UUID;equals(Ljava/lang/Object;)Z")
	@Expression("?.equals(?)")
	@ModifyExpressionValue(method = "method_70873", at = @At("MIXINEXTRAS:EXPRESSION")) // ah hey that's how you mixin to a lambda
	private boolean shouldIgnoreMarker(boolean markerIsCameraEntity, @Local(name="uuid") UUID uuid) {
		if (markerIsCameraEntity) { // fail fast
			return true;
		} else if (client.getCameraEntity() != null) {
			for (Entity passenger : client.getCameraEntity().getPassengersDeep()) {
				if (passenger.getUuid().equals(uuid)) {
					return true;
				}
			}
		}
		return false;
	}
}
