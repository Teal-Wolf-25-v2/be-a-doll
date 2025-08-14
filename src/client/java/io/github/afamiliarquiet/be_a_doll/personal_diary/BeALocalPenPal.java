package io.github.afamiliarquiet.be_a_doll.personal_diary;

import io.github.afamiliarquiet.be_a_doll.letters.S2CDollDismountLetter;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.entity.Entity;

public class BeALocalPenPal {
	public static void fillPen() {
		ClientPlayNetworking.registerGlobalReceiver(S2CDollDismountLetter.ID, ((letter, context) -> {
			letter.dismountingDollIds().forEach(id -> {
				Entity ridingDoll = context.player().getWorld().getEntityById(id);
				if (ridingDoll != null) {
					ridingDoll.dismountVehicle();
				}
			});
		}));
	}
}
