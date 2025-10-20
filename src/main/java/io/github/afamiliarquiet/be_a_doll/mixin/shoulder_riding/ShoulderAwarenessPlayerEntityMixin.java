package io.github.afamiliarquiet.be_a_doll.mixin.shoulder_riding;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import io.github.afamiliarquiet.be_a_doll.BeADecoration;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Arm;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ServerPlayerEntity.class)
public abstract class ShoulderAwarenessPlayerEntityMixin extends LivingEntity {
	protected ShoulderAwarenessPlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}

	@Definition(id = "getLeftShoulderNbt", method = "Lnet/minecraft/server/network/ServerPlayerEntity;getLeftShoulderNbt()Lnet/minecraft/nbt/NbtCompound;")
	@Definition(id = "isEmpty", method = "Lnet/minecraft/nbt/NbtCompound;isEmpty()Z")
	@Expression("this.getLeftShoulderNbt().isEmpty()")
	@ModifyExpressionValue(method = "mountOntoShoulder", at = @At("MIXINEXTRAS:EXPRESSION"))
	private boolean checkLeftShoulder(boolean original) {
		return BeADecoration.shoulderEntityIsEmpty(this, original, Arm.LEFT);
	}

	@Definition(id = "getRightShoulderNbt", method = "Lnet/minecraft/server/network/ServerPlayerEntity;getRightShoulderNbt()Lnet/minecraft/nbt/NbtCompound;")
	@Definition(id = "isEmpty", method = "Lnet/minecraft/nbt/NbtCompound;isEmpty()Z")
	@Expression("this.getRightShoulderNbt().isEmpty()")
	@ModifyExpressionValue(method = "mountOntoShoulder", at = @At("MIXINEXTRAS:EXPRESSION"))
	private boolean checkRightShoulder(boolean original) {
		return BeADecoration.shoulderEntityIsEmpty(this, original, Arm.RIGHT);
	}
}
