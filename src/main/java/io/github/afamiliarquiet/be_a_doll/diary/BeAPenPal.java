package io.github.afamiliarquiet.be_a_doll.diary;

import io.github.afamiliarquiet.be_a_doll.letters.C2SCreativeEssenceAlterationLetter;
import io.github.afamiliarquiet.be_a_doll.letters.C2SEssenceAlterationLetter;
import io.github.afamiliarquiet.be_a_doll.letters.S2CDollDismountLetter;
import io.github.afamiliarquiet.be_a_doll.letters.S2CDollRepairedLetter;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public class BeAPenPal {
	public static void fillPen() {
		PayloadTypeRegistry.playS2C().register(S2CDollDismountLetter.ID, S2CDollDismountLetter.PACKET_CODEC);

		PayloadTypeRegistry.playC2S().register(C2SEssenceAlterationLetter.ID, C2SEssenceAlterationLetter.PACKET_CODEC);
		PayloadTypeRegistry.playC2S().register(C2SCreativeEssenceAlterationLetter.ID, C2SCreativeEssenceAlterationLetter.PACKET_CODEC);
		ServerPlayNetworking.registerGlobalReceiver(C2SEssenceAlterationLetter.ID, C2SEssenceAlterationLetter::receive);
		ServerPlayNetworking.registerGlobalReceiver(C2SCreativeEssenceAlterationLetter.ID, C2SCreativeEssenceAlterationLetter::receive);

		PayloadTypeRegistry.playS2C().register(S2CDollRepairedLetter.ID, S2CDollRepairedLetter.PACKET_CODEC);
	}
}
