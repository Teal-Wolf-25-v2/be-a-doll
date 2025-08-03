package io.github.afamiliarquiet.be_a_doll.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.afamiliarquiet.be_a_doll.BeAMaid;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ChatScreen.class)
public abstract class Keysmash extends Screen {
	protected Keysmash(Text title) {
		super(title);
	}

	// mixing in only at the ending sendMessage means we preserve our intended message history
	// allowing the doll to press up and remind itself of what it tried to say
	@WrapOperation(method = "sendMessage", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayNetworkHandler;sendChatMessage(Ljava/lang/String;)V"))
	private void smashKeys(ClientPlayNetworkHandler instance, String content, Operation<Void> original) {
		// ok great glad you sent a message! sorry its keysmashing now if you're a doll
		if (this.client != null && BeAMaid.isDoll(this.client.player)) {
			original.call(instance, BeAMaid.dollishKeysmashing(content, this.client.player));
		} else {
			original.call(instance, content);
		}
	}
}
