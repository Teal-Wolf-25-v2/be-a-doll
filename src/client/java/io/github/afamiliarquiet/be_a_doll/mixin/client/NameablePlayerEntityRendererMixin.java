package io.github.afamiliarquiet.be_a_doll.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import io.github.afamiliarquiet.be_a_doll.BeAMaid;
import io.github.afamiliarquiet.be_a_doll.DollishState;
import io.github.afamiliarquiet.be_a_doll.diary.BeALibrarian;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.PlayerLikeEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityRenderer.class)
public abstract class NameablePlayerEntityRendererMixin extends LivingEntityRenderer<AbstractClientPlayerEntity, PlayerEntityRenderState, PlayerEntityModel> {
	public NameablePlayerEntityRendererMixin(EntityRendererFactory.Context ctx, PlayerEntityModel model, float shadowRadius) {
		super(ctx, model, shadowRadius);
	}

	@Inject(at = @At("HEAD"), method = "updateRenderState(Lnet/minecraft/entity/PlayerLikeEntity;Lnet/minecraft/client/render/entity/state/PlayerEntityRenderState;F)V")
	private void alsoCheckDollness(PlayerLikeEntity playerLike, PlayerEntityRenderState state, float f, CallbackInfo ci) {
		if (!(playerLike instanceof PlayerEntity player))
			return;

		DollishState dollishState = (DollishState)state;
		dollishState.be_a_doll$setDoll(BeAMaid.isDoll(player));
		// no need for now
//		dollishState.be_a_doll$setVariant(BeALibrarian.inspectDollMaterial(player));
		dollishState.be_a_doll$setDollName(BeALibrarian.inspectDollLabel(player));
		dollishState.be_a_doll$setTargeted(player == this.dispatcher.targetedEntity || player == MinecraftClient.getInstance().getCameraEntity());
//		if (dollishState.be_a_doll$isDoll() && state.squaredDistanceToCamera < 4096.0 && player == this.dispatcher.targetedEntity || player == MinecraftClient.getInstance().getCameraEntity()) {
//			// todone - add f3 override for moderation + doll name from nametag
//			dollishState.be_a_doll$setDollName(BeALibrarian.inspectDollLabel(player));
//			if (dollishState.be_a_doll$getDollName() == null) { // notodo - remove this probably? but... it seems like attachments are broken
//				dollishState.be_a_doll$setDollName(this.getDisplayName(player));
//			}
//		} else {
//			dollishState.be_a_doll$setDollName(null);
//		}
	}

	@WrapOperation(method = "renderLabelIfPresent(Lnet/minecraft/client/render/entity/state/PlayerEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;Lnet/minecraft/client/render/state/CameraRenderState;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;submitLabel(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/util/math/Vec3d;ILnet/minecraft/text/Text;ZIDLnet/minecraft/client/render/state/CameraRenderState;)V"))
	private void butDollsAreNoDifferent(OrderedRenderCommandQueue instance, MatrixStack matrixStack, Vec3d nameLabelPos, int y, Text label, boolean notSneaking, int light, double squaredDistanceToCamera, CameraRenderState cameraRenderState, Operation<Void> original, @Local(argsOnly = true) PlayerEntityRenderState state) {
		DollishState dollishState = (DollishState) state;
		if (dollishState.be_a_doll$isDoll() && !MinecraftClient.getInstance().getDebugHud().shouldShowDebugHud()) {
			if (dollishState.be_a_doll$isTargeted()) {
				if (dollishState.be_a_doll$getDollName() != null) {
					original.call(instance, matrixStack, nameLabelPos, y, dollishState.be_a_doll$getDollName(), notSneaking, light, squaredDistanceToCamera, cameraRenderState);
					return;
				} // else { defer to the grand elser }
			} else {
				return; // don't render if doll and not targeted
			}
		} // else { defer to the grand elser }


		// the grand elser
		original.call(instance, matrixStack, nameLabelPos, y, label, notSneaking, light, squaredDistanceToCamera, cameraRenderState);
	}
}
