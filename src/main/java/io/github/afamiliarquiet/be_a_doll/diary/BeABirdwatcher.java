package io.github.afamiliarquiet.be_a_doll.diary;

import io.github.afamiliarquiet.be_a_doll.BeADoll;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class BeABirdwatcher {
	public static final SoundEvent RAVEN_CHIRP = registerSound("item.familiar_magic.ribbon.tied");
	public static final SoundEvent RAVEN_CRY = registerSound("item.familiar_magic.ribbon.untied");

	public static void offerTea() {
		// have you heard their songs? listen a little, i've got a small catalogue
	}

	private static SoundEvent registerSound(String thing) {
		Identifier id = BeADoll.id(thing);
		return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
	}
}
