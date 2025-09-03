package io.github.afamiliarquiet.be_a_doll.mixin.client;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import io.github.afamiliarquiet.be_a_doll.BeAMaid;
import io.github.afamiliarquiet.be_a_doll.DollishState;
import io.github.afamiliarquiet.be_a_doll.diary.BeALibrarian;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityRenderer.class)
public abstract class NameablePlayerEntityRendererMixin extends LivingEntityRenderer<AbstractClientPlayerEntity, PlayerEntityRenderState, PlayerEntityModel> {
	public NameablePlayerEntityRendererMixin(EntityRendererFactory.Context ctx, PlayerEntityModel model, float shadowRadius) {
		super(ctx, model, shadowRadius);
	}

	@Inject(at = @At("HEAD"), method = "updateRenderState(Lnet/minecraft/client/network/AbstractClientPlayerEntity;Lnet/minecraft/client/render/entity/state/PlayerEntityRenderState;F)V")
	private void alsoCheckDollness(AbstractClientPlayerEntity player, PlayerEntityRenderState state, float f, CallbackInfo ci) {
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

	@WrapMethod(method = "renderLabelIfPresent(Lnet/minecraft/client/render/entity/state/PlayerEntityRenderState;Lnet/minecraft/text/Text;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V")
	private void butDollsAreNoDifferent(PlayerEntityRenderState state, Text text, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, Operation<Void> original) {
		DollishState dollishState = (DollishState) state;
		if (dollishState.be_a_doll$isDoll() && !MinecraftClient.getInstance().getDebugHud().shouldShowDebugHud()) {
			if (dollishState.be_a_doll$isTargeted()) {
				if (dollishState.be_a_doll$getDollName() != null) {
					original.call(state, dollishState.be_a_doll$getDollName(), matrixStack, vertexConsumerProvider, i);
					return;
				} // else { defer to the grand elser }
			} else {
				return; // don't render if doll and not targeted
			}
		} // else { defer to the grand elser }

		// the grand elser
		original.call(state, text, matrixStack, vertexConsumerProvider, i);
	}
}
