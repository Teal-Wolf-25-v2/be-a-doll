package io.github.afamiliarquiet.be_a_doll.mixin;

import com.mojang.authlib.GameProfile;
import io.github.afamiliarquiet.be_a_doll.letters.S2CDollDismountLetter;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(ServerPlayerEntity.class)
public abstract class SpectatorDropsDollsServerPlayerEntityMixin extends PlayerEntity {
	public SpectatorDropsDollsServerPlayerEntityMixin(World world, GameProfile profile) {
		super(world, profile);
	}

	@Inject(method = "changeGameMode", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerEntity;dropShoulderEntities()V"))
	private void untieDolls(GameMode gameMode, CallbackInfoReturnable<Boolean> cir) {
		List<Entity> passengers = this.getPassengerList();
		if (!passengers.isEmpty()) {
			ServerPlayNetworking.send((ServerPlayerEntity) (Object) this, new S2CDollDismountLetter(passengers.stream().map(Entity::getId).toList()));
			this.removeAllPassengers();
		}
	}
}
