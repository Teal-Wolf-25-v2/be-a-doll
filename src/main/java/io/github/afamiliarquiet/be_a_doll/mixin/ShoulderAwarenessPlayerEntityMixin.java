package io.github.afamiliarquiet.be_a_doll.mixin;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import io.github.afamiliarquiet.be_a_doll.BeADecoration;
import io.github.afamiliarquiet.be_a_doll.diary.BeAPenPal;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Arm;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(PlayerEntity.class)
public abstract class ShoulderAwarenessPlayerEntityMixin extends LivingEntity {
	@Shadow
	public abstract Arm getMainArm();

	protected ShoulderAwarenessPlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}

	@Inject(method = "tickRiding", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;stopRiding()V"))
	private void sendDismountNotice(CallbackInfo ci) {
		// for some reason the client has a hard time recognizing when a riding player wants to dismount.
		// hopefully this  helps. wasn't able to track down the problem
		if (this.getVehicle() instanceof ServerPlayerEntity serverPlayer) {
			ServerPlayNetworking.send(serverPlayer, new BeAPenPal.C2SDollDismountLetter(List.of(this.getId())));
		}
	}

	@Definition(id = "getShoulderEntityLeft", method = "Lnet/minecraft/entity/player/PlayerEntity;getShoulderEntityLeft()Lnet/minecraft/nbt/NbtCompound;")
	@Definition(id = "isEmpty", method = "Lnet/minecraft/nbt/NbtCompound;isEmpty()Z")
	@Expression("this.getShoulderEntityLeft().isEmpty()")
	@ModifyExpressionValue(method = "addShoulderEntity", at = @At("MIXINEXTRAS:EXPRESSION"))
	private boolean checkLeftShoulder(boolean original) {
		return BeADecoration.shoulderEntityIsEmpty(this, original, Arm.LEFT);
	}

	@Definition(id = "getShoulderEntityRight", method = "Lnet/minecraft/entity/player/PlayerEntity;getShoulderEntityRight()Lnet/minecraft/nbt/NbtCompound;")
	@Definition(id = "isEmpty", method = "Lnet/minecraft/nbt/NbtCompound;isEmpty()Z")
	@Expression("this.getShoulderEntityRight().isEmpty()")
	@ModifyExpressionValue(method = "addShoulderEntity", at = @At("MIXINEXTRAS:EXPRESSION"))
	private boolean checkRightShoulder(boolean original) {
		return BeADecoration.shoulderEntityIsEmpty(this, original, Arm.RIGHT);
	}
}
