package io.github.afamiliarquiet.be_a_doll.mixin.client;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import io.github.afamiliarquiet.be_a_doll.BeAMaid;
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
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityRenderer.class)
public abstract class NameablePlayerEntityRenderer extends LivingEntityRenderer<AbstractClientPlayerEntity, PlayerEntityRenderState, PlayerEntityModel> {
	@Unique
	private boolean be_a_doll$isDoll = false;

	@Unique
	@Nullable
	private Text be_a_doll$dollDisplayName = null;

	public NameablePlayerEntityRenderer(EntityRendererFactory.Context ctx, PlayerEntityModel model, float shadowRadius) {
		super(ctx, model, shadowRadius);
	}

	@Inject(at = @At("HEAD"), method = "updateRenderState(Lnet/minecraft/client/network/AbstractClientPlayerEntity;Lnet/minecraft/client/render/entity/state/PlayerEntityRenderState;F)V")
	private void alsoCheckDollness(AbstractClientPlayerEntity player, PlayerEntityRenderState state, float f, CallbackInfo ci) {
		be_a_doll$isDoll = BeAMaid.isDoll(player);
		if (be_a_doll$isDoll && state.squaredDistanceToCamera < 4096.0 && player == this.dispatcher.targetedEntity) {
			// todone - add f3 override for moderation + doll name from nametag
			be_a_doll$dollDisplayName = BeALibrarian.inspectDollLabel(player);
			if (be_a_doll$dollDisplayName == null) { // notodo - remove this probably? but... it seems like attachments are broken
				be_a_doll$dollDisplayName = this.getDisplayName(player);
			}
		} else {
			be_a_doll$dollDisplayName = null;
		}
	}

	@WrapMethod(method = "renderLabelIfPresent(Lnet/minecraft/client/render/entity/state/PlayerEntityRenderState;Lnet/minecraft/text/Text;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V")
	private void butDollsAreNoDifferent(PlayerEntityRenderState playerEntityRenderState, Text text, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, Operation<Void> original) {
		// todo - i kinda want to have dolls render their names in first person so they can see their tag?
		//  dolls should probably be able to write their own names too. just 'cause... blank text boxes and multiplayer.
		if (be_a_doll$isDoll && !MinecraftClient.getInstance().getDebugHud().shouldShowDebugHud()) {
			if (be_a_doll$dollDisplayName != null) {
				original.call(playerEntityRenderState, be_a_doll$dollDisplayName, matrixStack, vertexConsumerProvider, i);
			}
		} else {
			original.call(playerEntityRenderState, text, matrixStack, vertexConsumerProvider, i);
		}
	}
}
