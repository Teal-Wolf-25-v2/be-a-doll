package io.github.afamiliarquiet.be_a_doll.mixin.client;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import io.github.afamiliarquiet.be_a_doll.BeADoll;
import io.github.afamiliarquiet.be_a_doll.diary.BeACurator;
import io.github.afamiliarquiet.be_a_doll.BeAMaid;
import io.github.afamiliarquiet.be_a_doll.diary.BeALibrarian;
import io.github.afamiliarquiet.be_a_doll.diary.BeAWitch;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class DollRetexturedInGameHudMixin {
	@Shadow
	private int ticks;

	@Inject(method = "renderFood", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/HungerManager;getSaturationLevel()F"))
	private void alterHungerTextures(DrawContext context, PlayerEntity player, int top, int right, CallbackInfo ci,
									 @Local(name = "identifier", ordinal = 0) LocalRef<Identifier> emptyId,
									 @Local(name = "identifier2", ordinal = 1) LocalRef<Identifier> fullId,
									 @Local(name = "identifier3", ordinal = 2) LocalRef<Identifier> halfId
	) {
		// if this has an impact on fps then SUFFER
		if (BeAMaid.isDoll(player)) {
			BeADoll.Variant variant = BeALibrarian.inspectDollMaterial(player);
			emptyId.set(variant.getFoodSpriteEmpty());
			halfId.set(variant.getFoodSpriteHalf());
			fullId.set(variant.getFoodSpritFull());
		}
	}

	@Definition(id = "getTexture", method = "Lnet/minecraft/client/gui/hud/InGameHud$HeartType;getTexture(ZZZ)Lnet/minecraft/util/Identifier;")
	@Expression("?.getTexture(?, ?, ?)")
	@ModifyExpressionValue(method = "drawHeart", at = @At("MIXINEXTRAS:EXPRESSION"))
	private Identifier alterAbsorptitonTexture(Identifier original, @Local(argsOnly = true) InGameHud.HeartType heartType, @Local(argsOnly = true, ordinal = 0) boolean hardcore, @Local(argsOnly = true, ordinal = 2) boolean half) {
		if (heartType == InGameHud.HeartType.ABSORBING && BeAMaid.isDoll(MinecraftClient.getInstance().player)) {
			if (half) {
				if (hardcore) {
					return BeACurator.CARED_HEART_HARDCORE_HALF;
				} else {
					return BeACurator.CARED_HEART_HALF;
				}
			} else {
				if (hardcore) {
					return BeACurator.CARED_HEART_HARDCORE_FULL;
				} else {
					return BeACurator.CARED_HEART_FULL;
				}
			}
		} else {
			return original;
		}
	}

	@Definition(id = "hasStatusEffect", method = "Lnet/minecraft/entity/player/PlayerEntity;hasStatusEffect(Lnet/minecraft/registry/entry/RegistryEntry;)Z")
	@Expression("?.hasStatusEffect(?)")
	@ModifyExpressionValue(method = "renderStatusBars", at = @At("MIXINEXTRAS:EXPRESSION"))
	private boolean orOverflowing(boolean original, @Local(name = "playerEntity", ordinal = 0) PlayerEntity player) {
		return original || player.hasStatusEffect(BeAWitch.OVERFLOWING);
	}

	@Definition(id = "getSaturationLevel", method = "Lnet/minecraft/entity/player/HungerManager;getSaturationLevel()F")
	@Expression("?.getSaturationLevel() <= ?")
	@ModifyExpressionValue(method = "renderFood", at = @At("MIXINEXTRAS:EXPRESSION"))
	private boolean resaturatingWave(boolean original, @Local(argsOnly = true) PlayerEntity player, @Local(name="j", ordinal = 3) int index, @Local(name="k", ordinal = 4) LocalIntRef yPos) {
		if (player.hasStatusEffect(BeAWitch.OVERFLOWING) && BeAMaid.isDoll(player)) {
			// if i was a super optimizer i could put the ticks % 15 outside the for loop.
			if (index == this.ticks % 25) {
				yPos.set(yPos.get() - 2);
			}

			return false; // basically cancels the if statement for random bobs
		} else {
			return original;
		}
	}
}
