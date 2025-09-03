package io.github.afamiliarquiet.be_a_doll.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import io.github.afamiliarquiet.be_a_doll.BeAMaid;
import io.github.afamiliarquiet.be_a_doll.diary.BeALibrarian;
import io.github.afamiliarquiet.be_a_doll.letters.IntraLibraryMessageCacheLetter;
import net.minecraft.network.message.MessageType;
import net.minecraft.network.message.SentMessage;
import net.minecraft.network.message.SignedMessage;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.PlainTextContent;
import net.minecraft.text.Text;
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

	@Inject(at = @At("HEAD"), method = "broadcast(Lnet/minecraft/network/message/SignedMessage;Ljava/util/function/Predicate;Lnet/minecraft/server/network/ServerPlayerEntity;Lnet/minecraft/network/message/MessageType$Parameters;)V")
	private void papersPlease(SignedMessage message, Predicate<ServerPlayerEntity> shouldSendFiltered, @Nullable ServerPlayerEntity sender, MessageType.Parameters params, CallbackInfo ci) {
		// this is, in my head, a helpful measure with large player counts.
		// (also a late redecoration. i guess i could shave off one decorate with another mixin,
		//  but i don't think i really need to worry that much about optimizing.)
		if (sender != null) {
			BeALibrarian.filePaperwork(sender, new IntraLibraryMessageCacheLetter(
				BeAMaid.isDoll(sender) && BeALibrarian.checkFilesForPasswordManager(sender).useKeysmashing(),
				SentMessage.of(message.withUnsignedContent(
					this.server.getMessageDecorator().decorate(sender, Text.of(
						BeAMaid.syntheticKeysmashing(message.getSignedContent(), sender)
					))
				)),
				SentMessage.of(message.withUnsignedContent(
					this.server.getMessageDecorator().decorate(sender, Text.of(
						MutableText.of(PlainTextContent.of(message.getSignedContent())).styled(
							style -> style.withColor(0xbca1a0).withItalic(true)
						)
					))
				))
			));
		}
	}

	@ModifyArg(index = 0, at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerEntity;sendChatMessage(Lnet/minecraft/network/message/SentMessage;ZLnet/minecraft/network/message/MessageType$Parameters;)V"), method = "broadcast(Lnet/minecraft/network/message/SignedMessage;Ljava/util/function/Predicate;Lnet/minecraft/server/network/ServerPlayerEntity;Lnet/minecraft/network/message/MessageType$Parameters;)V")
	private SentMessage distributeFliers(SentMessage message, @Local(argsOnly = true) SignedMessage signedMessage, @Local(ordinal = 0, argsOnly = true) ServerPlayerEntity sender, @Local(ordinal = 1) ServerPlayerEntity target) {
		IntraLibraryMessageCacheLetter documents = BeALibrarian.checkDocuments(sender);
		if (documents != null && documents.senderSmashesKeys()) {
			if (target != sender && (BeAMaid.isDoll(target) || target.getPos().isInRange(sender.getPos(), 13))) {
				return documents.dolledMessage();
			} else {
				return documents.keysmashedMessage();
			}
		} else {
			return message;
		}
	}

	@Inject(at = @At("RETURN"), method = "broadcast(Lnet/minecraft/network/message/SignedMessage;Ljava/util/function/Predicate;Lnet/minecraft/server/network/ServerPlayerEntity;Lnet/minecraft/network/message/MessageType$Parameters;)V")
	private void timesUp(SignedMessage message, Predicate<ServerPlayerEntity> shouldSendFiltered, @Nullable ServerPlayerEntity sender, MessageType.Parameters params, CallbackInfo ci) {
		if (sender != null) {
			BeALibrarian.shredDocuments(sender);
		}
	}
}
