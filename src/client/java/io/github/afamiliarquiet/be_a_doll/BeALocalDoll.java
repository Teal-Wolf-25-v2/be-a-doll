package io.github.afamiliarquiet.be_a_doll;

import io.github.afamiliarquiet.be_a_doll.personal_diary.BeALocalPenPal;
import net.fabricmc.api.ClientModInitializer;

public class BeALocalDoll implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
		BeALocalPenPal.fillPen();
		BeACurator.payAVisit(); // this isn't necessary but it's cute
	}
}
