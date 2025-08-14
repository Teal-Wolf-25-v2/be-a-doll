package io.github.afamiliarquiet.be_a_doll.diary;

import io.github.afamiliarquiet.be_a_doll.BeADoll;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.AbsorptionStatusEffect;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;

public class BeAWitch {
	public static final RegistryEntry<StatusEffect> CARED_FOR = Registry.registerReference(
		Registries.STATUS_EFFECT,
		BeADoll.id("cared_for"),
		new AbsorptionStatusEffect(StatusEffectCategory.BENEFICIAL, 0xb0b8dd)
			.addAttributeModifier(EntityAttributes.MAX_ABSORPTION, BeADoll.id("effect.cared_for"), 14.0, EntityAttributeModifier.Operation.ADD_VALUE)
	);

	public static void putOnHat() {

	}
}
