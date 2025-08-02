package io.github.afamiliarquiet.be_a_doll;

import com.google.common.collect.HashMultimap;
import io.github.afamiliarquiet.be_a_doll.diary.BeACollector;
import net.fabricmc.api.ModInitializer;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.DefaultAttributeRegistry;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BeADoll implements ModInitializer {
	public static final String MOD_ID = "be_a_doll";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	// id used for checking on things, map used for removing, attributecontainer used for .. easier persistent adding?
	public static final Identifier DOLLIFIED_MODIFIER_ID = id("dollified");
	public static final HashMultimap<RegistryEntry<EntityAttribute>, EntityAttributeModifier> DOLL_MODIFICATIONS = HashMultimap.create();
//	public static final AttributeContainer DOLLISHNESS = new AttributeContainer(DefaultAttributeRegistry.get(EntityType.PLAYER));
	static {
		DOLL_MODIFICATIONS.put(EntityAttributes.SCALE, new EntityAttributeModifier(DOLLIFIED_MODIFIER_ID, -0.5, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
//		DOLL_MODIFICATIONS.forEach((attribute, modifier) -> {
//			EntityAttributeInstance instance = DOLLISHNESS.getCustomInstance(attribute);
//			if (instance != null) {
//				instance.addPersistentModifier(modifier);
//			}
//		});
	}

	@Override
	public void onInitialize() {
		log("Ready to make some new toys :)");
		BeACollector.initialize();
	}

	public static Identifier id(String thing) {
		return Identifier.of(MOD_ID, thing);
	}

	// probably a good habit to always log my id whenever i'm throwing things in the log, even if it's just a quick test
	public static void log(String message) {
		LOGGER.info("[Be a doll!] {}", message);
	}
}
