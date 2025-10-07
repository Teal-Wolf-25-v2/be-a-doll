package io.github.afamiliarquiet.be_a_doll.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import io.github.afamiliarquiet.be_a_doll.BeADollthing;
import io.github.afamiliarquiet.be_a_doll.BeAMaid;
import io.github.afamiliarquiet.be_a_doll.diary.BeALibrarian;
import io.github.afamiliarquiet.be_a_doll.letters.C2SKeysmashConfigSyncLetter;
import io.github.afamiliarquiet.be_a_doll.letters.IntraLibraryMessageCacheLetter;
import net.minecraft.network.message.MessageType;
import net.minecraft.network.message.SentMessage;
import net.minecraft.network.message.SignedMessage;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Predicate;

@Mixin(PlayerManager.class)
public class DollToDollCommunicationPlayerManagerMixin {
	@Shadow
	@Final
	private MinecraftServer server;

	@Inject(
		at = @At("HEAD"),
		method = "broadcast(Lnet/minecraft/network/message/SignedMessage;Ljava/util/function/Predicate;Lnet/minecraft/server/network/ServerPlayerEntity;Lnet/minecraft/network/message/MessageType$Parameters;)V"
	)
	private void papersPlease(SignedMessage message,
							  Predicate<ServerPlayerEntity> shouldSendFiltered,
							  @Nullable ServerPlayerEntity sender,
							  MessageType.Parameters params,
							  CallbackInfo ci) {
		BeADollthing.prepareMessageSending(message, sender, this.server);
	}

	@ModifyArg(
		index = 0,
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/server/network/ServerPlayerEntity;sendChatMessage(Lnet/minecraft/network/message/SentMessage;ZLnet/minecraft/network/message/MessageType$Parameters;)V"
		),
		method = "broadcast(Lnet/minecraft/network/message/SignedMessage;Ljava/util/function/Predicate;Lnet/minecraft/server/network/ServerPlayerEntity;Lnet/minecraft/network/message/MessageType$Parameters;)V"
	)
	private SentMessage distributeFliers(SentMessage message,
										 @Local(argsOnly = true) SignedMessage signedMessage,
										 @Local(ordinal = 0, argsOnly = true) ServerPlayerEntity sender,
										 @Local(ordinal = 1) ServerPlayerEntity target) {
		IntraLibraryMessageCacheLetter documents = BeALibrarian.checkDocuments(sender);
		C2SKeysmashConfigSyncLetter passwords = BeALibrarian.checkFilesForPasswordManager(target);
		if (documents != null && documents.senderSmashesKeys()) {
			if (target != sender
				&& (BeAMaid.isDoll(target) && passwords.readableOthers()
				|| target.getEntityPos().isInRange(sender.getEntityPos(), 13))
				|| target == sender && documents.senderSeesClearly()) {
				return documents.dolledMessage();
			} else {
				return documents.keysmashedMessage();
			}
		} else {
			return message;
		}
	}

	@Inject(
		at = @At("RETURN"),
		method = "broadcast(Lnet/minecraft/network/message/SignedMessage;Ljava/util/function/Predicate;Lnet/minecraft/server/network/ServerPlayerEntity;Lnet/minecraft/network/message/MessageType$Parameters;)V"
	)
	private void timesUp(SignedMessage message,
						 Predicate<ServerPlayerEntity> shouldSendFiltered,
						 @Nullable ServerPlayerEntity sender,
						 MessageType.Parameters params,
						 CallbackInfo ci) {
		if (sender != null) {
			BeALibrarian.shredDocuments(sender);
		}
	}
}
