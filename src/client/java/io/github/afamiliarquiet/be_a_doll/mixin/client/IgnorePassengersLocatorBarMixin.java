package io.github.afamiliarquiet.be_a_doll.mixin.client;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.bar.LocatorBar;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.UUID;

@Mixin(LocatorBar.class)
public abstract class IgnorePassengersLocatorBarMixin {
	@Definition(id = "equals", method = "Ljava/util/UUID;equals(Ljava/lang/Object;)Z")
	@Expression("?.equals(?)")
	@ModifyExpressionValue(method = "method_70873", at = @At("MIXINEXTRAS:EXPRESSION")) // ah hey that's how you mixin to a lambda
	private static boolean shouldIgnoreMarker(boolean markerIsCameraEntity, @Local(name = "uuid", ordinal = 0, argsOnly = true) UUID uuid) {
		MinecraftClient client = MinecraftClient.getInstance();

		// notodo - locator bar still shows even when this filters out all waypoints. minor issue, maybe fix
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
