package io.github.afamiliarquiet.be_a_doll;

import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BeADoll implements ModInitializer {
	public static final String MOD_ID = "be_a_doll";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		//LOGGER.info("[Mod ID] pretty pink princess ponies prancing perpendicular");
	}

	public static Identifier id(String thing) {
		return Identifier.of(MOD_ID, thing);
	}
}
