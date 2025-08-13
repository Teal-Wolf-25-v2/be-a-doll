package io.github.afamiliarquiet.be_a_doll.mixin.synthetic_treats;

import io.github.afamiliarquiet.be_a_doll.BeAMaid;
import io.github.afamiliarquiet.be_a_doll.diary.BeAResearcher;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class SyntheticResistancePlayerEntityMixin extends LivingEntity {
	protected SyntheticResistancePlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}

	@Inject(method = "isInvulnerableTo", at = @At("HEAD"), cancellable = true)
	private void dollsAreImmuneToDrowningAndFreezing(ServerWorld world, DamageSource source, CallbackInfoReturnable<Boolean> cir) {
		if (BeAMaid.isDoll((PlayerEntity)(Object)this)) { // full metal alchemist..
			if (source.isIn(BeAResearcher.DOLL_IMMUNE)) {
				cir.setReturnValue(true);
			}
		} // full metal alchemist!
	}
}
