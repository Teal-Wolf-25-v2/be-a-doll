package io.github.afamiliarquiet.be_a_doll.personal_diary;

import io.github.afamiliarquiet.be_a_doll.item.DollcraftItem;
import io.github.afamiliarquiet.be_a_doll.letters.S2CDollDismountLetter;
import io.github.afamiliarquiet.be_a_doll.letters.S2CDollRepairedLetter;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;

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

		ClientPlayNetworking.registerGlobalReceiver(S2CDollRepairedLetter.ID, (letter, context) -> {
			Entity repairedEntity = context.player().getWorld().getEntityById(letter.entityId());
			if (repairedEntity instanceof PlayerEntity letThereBeDoll) {
				DollcraftItem.spawnRepairParticles(letThereBeDoll, letter.material(), 16);
			}
		});
	}
}
