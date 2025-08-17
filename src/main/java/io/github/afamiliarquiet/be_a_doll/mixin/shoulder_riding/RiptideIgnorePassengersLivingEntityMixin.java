package io.github.afamiliarquiet.be_a_doll.mixin.shoulder_riding;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntity.class)
public abstract class RiptideIgnorePassengersLivingEntityMixin extends Entity {
	public RiptideIgnorePassengersLivingEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	@Definition(id = "LivingEntity", type = LivingEntity.class)
	@Expression("? instanceof LivingEntity")
	@ModifyExpressionValue(method = "tickRiptide", at = @At("MIXINEXTRAS:EXPRESSION"))
	private boolean isTarget(boolean original, @Local(ordinal = 0) Entity possibleTarget) {
		return original && !this.equals(possibleTarget.getVehicle());
	}
}
