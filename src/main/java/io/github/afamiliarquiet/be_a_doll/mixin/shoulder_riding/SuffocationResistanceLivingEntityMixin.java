package io.github.afamiliarquiet.be_a_doll.mixin.shoulder_riding;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntity.class)
public abstract class SuffocationResistanceLivingEntityMixin extends Entity {
	public SuffocationResistanceLivingEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	@ModifyReturnValue(method = "isInsideWall", at = @At("RETURN"))
	private boolean notIfMyRideIsOkay(boolean original) {
		if (this.getVehicle() instanceof PlayerEntity myRide) {
			return original && myRide.isInsideWall();
		} else {
			return original;
		}
	}
}
