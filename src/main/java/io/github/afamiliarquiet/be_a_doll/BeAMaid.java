package io.github.afamiliarquiet.be_a_doll;

import com.google.common.collect.HashMultimap;
import io.github.afamiliarquiet.be_a_doll.diary.BeALibrarian;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static io.github.afamiliarquiet.be_a_doll.BeADoll.TINKERER;

public class BeAMaid {
	// to be completely honest, most of this is probably better suited to BeADoll in name
	// but the problem is that i like my main initializer classes to be clean and tidy. so, maid here to help!

	// id used for checking on things, map used for removing
	public static final Identifier DOLLIFIED_MODIFIER_ID = BeADoll.id("dollified");
	public static final HashMultimap<RegistryEntry<EntityAttribute>, EntityAttributeModifier> DOLL_MODIFICATIONS = HashMultimap.create();
	static {
		DOLL_MODIFICATIONS.put(EntityAttributes.SCALE, new EntityAttributeModifier(DOLLIFIED_MODIFIER_ID, -0.7, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
	}

	public static void bestowApron() {
		ServerPlayerEvents.COPY_FROM.register(((oldPlayer, newPlayer, alive) ->
				// todo - should i care about alive? in theory maybe but it doesn't really matter
				BeAMaid.setDoll(newPlayer, BeALibrarian.inspectSupposedPlayer(oldPlayer))
		));
	}

	public static boolean isDoll(@Nullable PlayerEntity player) {
		if (player == null) {
			return false;
		}

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

	public static void setDoll(@Nullable PlayerEntity player, BeADoll.Variant variant) {
		// todo - maybe throw in an isClient check so client and server don't try to desync? idk if that'll happen
		if (player == null || player.getWorld().isClient()) {
			return;
		}
		if (variant == BeALibrarian.inspectSupposedPlayer(player)) {
			// nothing to do boss, that doll is doll! or that.. not doll is not doll, i guess.
			return;
		}

		if (variant != BeADoll.Variant.REPRESSED) {
			// add persistent instead of add temporary. because doll is a persistent fact of life
			DOLL_MODIFICATIONS.forEach((attribute, modifier) -> {
				EntityAttributeInstance instance = player.getAttributes().getCustomInstance(attribute);
				if (instance != null) {
					instance.addPersistentModifier(modifier);
				}
			});
			BeALibrarian.reshapeDoll(player, variant);
		} else {
			player.getAttributes().removeModifiers(DOLL_MODIFICATIONS);
			BeALibrarian.repress(player);
		}
	}

	public static @NotNull String syntheticKeysmashing(@NotNull String originalMessage, @Nullable PlayerEntity keySmasher) {
		// if you want to try doll-to-doll communication later, try a mixin at PlayerManager#824 or so
		// this is entirely limited to whatever lowercase can detect, and subject to what My Keyboard looks like.
		// thats just how it is
		if (!originalMessage.isEmpty() && originalMessage.charAt(0) == '\\' || !TINKERER.useKeysmashing) {
			return originalMessage;
		}

		// todo - add more? change how it works? eh.

		String material = !TINKERER.letterPoolOverride.isEmpty() ? TINKERER.letterPoolOverride : "asdfjkl;";
		List<Character> spool = new ArrayList<>(material.length());
		Random random = new Random();
		StringBuilder smashed = new StringBuilder();
		double clarity = TINKERER.startingClarityScore;

		for (int i = 0; i < originalMessage.length(); i++) {
			if (spool.size() < material.length() * TINKERER.restockThreshold) {
				spool.clear();
				for (int j = 0; j < material.length(); j++) {
					spool.add(material.charAt(j));
				}
			}

			char current = originalMessage.charAt(i);
			if (Character.isLowerCase(current)) {
				smashed.append(spool.remove(random.nextInt(spool.size())));
				clarity *= TINKERER.keysmashedMultiplier;
			} else if (Character.isUpperCase(current)) {
				smashed.append(Character.toLowerCase(current));
				clarity += TINKERER.spokenLoudlyClarity;
			} else if (random.nextDouble() < TINKERER.baseClarityChance + (clarity / (1 + smashed.length()))) { // not normal text? good luck
				smashed.append(current);
				clarity += TINKERER.nonletterClarity;
			}
		}

		if (smashed.isEmpty()) { // if it was non-letters and bad luck. a (i don't think this is possible anymore?)
			smashed.append('a');
		}
		return smashed.toString();
	}
}
