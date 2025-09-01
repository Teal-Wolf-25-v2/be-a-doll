package io.github.afamiliarquiet.be_a_doll.mixin.synthetic_treats;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.FoxEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(FoxEntity.class)
public interface FoxEntityTrustInvoker {
	@Invoker("canTrust")
	public boolean invokeCanTrust(LivingEntity test);
}
