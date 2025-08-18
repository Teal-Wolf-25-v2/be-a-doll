package io.github.afamiliarquiet.be_a_doll.diary;

import io.github.afamiliarquiet.be_a_doll.BeADoll;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

public class BeAResearcher {
	public static final TagKey<DamageType> DOLL_IMMUNE = TagKey.of(
		RegistryKeys.DAMAGE_TYPE, BeADoll.id("doll_immune")
	);

	public static final TagKey<DamageType> DOLL_MODIFIES_MESSAGE = TagKey.of(
		RegistryKeys.DAMAGE_TYPE, BeADoll.id("doll_modifies_message")
	);

	public static final TagKey<Item> WOODEN_DOLL_CARE_MATERIALS = TagKey.of(
		RegistryKeys.ITEM, BeADoll.id("wooden_doll_care_materials")
	);

	public static final TagKey<Item> CLAY_DOLL_CARE_MATERIALS = TagKey.of(
		RegistryKeys.ITEM, BeADoll.id("clay_doll_care_materials")
	);

	public static final TagKey<Item> CLOTH_DOLL_CARE_MATERIALS = TagKey.of(
		RegistryKeys.ITEM, BeADoll.id("cloth_doll_care_materials")
	);

	public static void grantFunding() {

	}
}
