package io.github.afamiliarquiet.be_a_doll.diary;

import io.github.afamiliarquiet.be_a_doll.BeADoll;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

public class BeAResearcher {
	public static final TagKey<DamageType> DOLL_IMMUNE = TagKey.of(
		RegistryKeys.DAMAGE_TYPE, BeADoll.id("doll_immune")
	);

	public static void grantFunding() {

	}
}
