package io.github.afamiliarquiet.be_a_doll.diary;

import io.github.afamiliarquiet.be_a_doll.BeASelf;
import io.github.afamiliarquiet.be_a_doll.letters.C2SEssenceAlterationLetter;
import io.github.afamiliarquiet.be_a_doll.letters.S2CDollDismountLetter;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.PlayerScreenHandler;

public class BeAPenPal {
	public static void fillPen() {
		PayloadTypeRegistry.playS2C().register(S2CDollDismountLetter.ID, S2CDollDismountLetter.PACKET_CODEC);
		PayloadTypeRegistry.playC2S().register(C2SEssenceAlterationLetter.ID, C2SEssenceAlterationLetter.PACKET_CODEC);

		ServerPlayNetworking.registerGlobalReceiver(C2SEssenceAlterationLetter.ID, ((letter, context) -> {
			// todo - well crap. it seems server's screenhandler doesn't like playing with cursorstack
			//  ah actually it kinda works, the problem is creative mode is fucky and doesn't update server much
			PlayerScreenHandler handler = context.player().playerScreenHandler;
			ItemStack clickProcessedStack = BeASelf.clickSelf(handler.getCursorStack(), context.player(), letter.inserting());
			if (clickProcessedStack != null) {
				handler.setCursorStack(clickProcessedStack);
			}
		}));
	}
}
