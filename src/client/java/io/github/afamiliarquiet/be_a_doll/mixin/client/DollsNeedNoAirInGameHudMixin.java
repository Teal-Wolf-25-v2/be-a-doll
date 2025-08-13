package io.github.afamiliarquiet.be_a_doll.mixin.client;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import io.github.afamiliarquiet.be_a_doll.BeAMaid;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(InGameHud.class)
public class DollsNeedNoAirInGameHudMixin {
	@WrapWithCondition(method = "renderStatusBars", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;renderAirBubbles(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/entity/player/PlayerEntity;III)V"))
	private boolean letsSupposeThatIAmADollAndYouAreAWaterAndYouWantToKillMe(InGameHud instance, DrawContext context, PlayerEntity player, int heartCount, int top, int left) {
		return !BeAMaid.isDoll(player); // i would simply dodge
	}
}
