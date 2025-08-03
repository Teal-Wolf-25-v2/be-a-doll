package io.github.afamiliarquiet.be_a_doll;

import com.google.common.collect.HashMultimap;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

public class BeAMaid {
	// to be completely honest, most of this is probably better suited to BeADoll in name
	// but the problem is that i like my main initializer classes to be clean and tidy. so, maid here to help!

	// id used for checking on things, map used for removing
	public static final Identifier DOLLIFIED_MODIFIER_ID = BeADoll.id("dollified");
	public static final HashMultimap<RegistryEntry<EntityAttribute>, EntityAttributeModifier> DOLL_MODIFICATIONS = HashMultimap.create();
	static {
		DOLL_MODIFICATIONS.put(EntityAttributes.SCALE, new EntityAttributeModifier(DOLLIFIED_MODIFIER_ID, -0.6, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
	}

	public static void bestowApron() {
		ServerPlayerEvents.COPY_FROM.register(((oldPlayer, newPlayer, alive) ->
				// todo - should i care about alive? in theory maybe but it doesn't really matter
				BeAMaid.setDoll(newPlayer, BeAMaid.isDoll(oldPlayer))
		));
	}

	public static boolean isDoll(PlayerEntity player) {
		// if there's any signs of being a doll.... yep, that's a doll
//		int dollPoints = 0;
		for (RegistryEntry<EntityAttribute> attribute : DOLL_MODIFICATIONS.keySet()) {
			EntityAttributeInstance instance = player.getAttributes().getCustomInstance(attribute);
			if (instance != null && instance.hasModifier(DOLLIFIED_MODIFIER_ID)) {
				return true;
//				dollPoints++;
			}
		}
		return false;
//		return dollPoints * 2 >= BeADoll.DOLL_MODIFICATIONS.size(); // at least half doll? yeah that's doll enough
		// practically speaking dollPoints shouldn't really be relevant 'cause you should always have 100% doll or 0%
		// but in spirit that's what i want to do
	}

	public static void setDoll(PlayerEntity player, boolean beADoll) {
		// todo - maybe throw in an isClient check so client and server don't try to desync? idk if that'll happen
		if (beADoll == isDoll(player)) {
			// nothing to do boss, that doll is doll! or that.. not doll is not doll, i guess.
			return;
		}

		if (beADoll) {
			// add persistent instead of add temporary. because doll is a persistent fact of life
			DOLL_MODIFICATIONS.forEach((attribute, modifier) -> {
				EntityAttributeInstance instance = player.getAttributes().getCustomInstance(attribute);
				if (instance != null) {
					instance.addPersistentModifier(modifier);
				}
			});
		} else {
			player.getAttributes().removeModifiers(DOLL_MODIFICATIONS);
		}
	}
}
