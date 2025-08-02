package io.github.afamiliarquiet.be_a_doll;

import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.entry.RegistryEntry;

public class BeAMaid {
	public static boolean isDoll(PlayerEntity player) {
		// if there's any signs of being a doll.... yep, that's a doll
//		for (EntityAttributeInstance instance : player.getAttributes().getTracked()) {
//			if (instance.hasModifier(BeADoll.DOLLIFIED_MODIFIER_ID)) {
//				return true;
//			}
//		}
//		int dollPoints = 0;
		for (RegistryEntry<EntityAttribute> attribute : BeADoll.DOLL_MODIFICATIONS.keySet()) {
			EntityAttributeInstance instance = player.getAttributes().getCustomInstance(attribute);
			if (instance != null && instance.hasModifier(BeADoll.DOLLIFIED_MODIFIER_ID)) {
				return true;
//				dollPoints++;
			}
		}
		return false;
//		return dollPoints * 2 >= BeADoll.DOLL_MODIFICATIONS.size(); // at least half doll? yeah that's doll enough
		// practically speaking dollPoints shouldn't really be relevant 'cause you should always have 100% doll or 0%
		// but in spirit that's what i want to do
	}

	public static void setDoll(PlayerEntity player, boolean isDoll) {
		if (isDoll) {
			// add persistent instead of add temporary. because doll is a persistent fact of life
			BeADoll.DOLL_MODIFICATIONS.forEach((attribute, modifier) -> {
				EntityAttributeInstance instance = player.getAttributes().getCustomInstance(attribute);
				if (instance != null) {
					instance.addPersistentModifier(modifier);
				}
			});
		} else {
			player.getAttributes().removeModifiers(BeADoll.DOLL_MODIFICATIONS);
		}
	}
}
