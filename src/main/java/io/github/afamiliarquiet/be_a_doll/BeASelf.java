package io.github.afamiliarquiet.be_a_doll;

import io.github.afamiliarquiet.be_a_doll.diary.BeABirdwatcher;
import io.github.afamiliarquiet.be_a_doll.diary.BeACollector;
import io.github.afamiliarquiet.be_a_doll.diary.BeALibrarian;
import io.github.afamiliarquiet.be_a_doll.diary.BeAWitch;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class BeASelf {
	// todone - make more mixins where player is rendered, like creative inv and horse i think
	// nevermind not doing horse it'd be annoying to do it there and there's no need to play w/ essence on a horse
	public static @Nullable ItemStack clickSelf(ItemStack cursorStack, PlayerEntity player, boolean inserting) {
		if (inserting) {
			if (cursorStack.isOf(BeACollector.ESSENCE_FRAGMENT)) {
				BeADoll.Variant variant = cursorStack.get(BeACollector.DOLL_VARIANT_COMPONENT);
				if (variant != null) {
					doEssencePlaceEffects(player, variant);
					return ItemStack.EMPTY;
				}
			}
		} else {
			if (cursorStack.isEmpty()) {
				doEssenceTakeEffects(player);
				ItemStack fragment = BeACollector.ESSENCE_FRAGMENT.getDefaultStack();
				fragment.set(BeACollector.DOLL_VARIANT_COMPONENT, BeALibrarian.inspectSupposedPlayer(player));
				return fragment;
			}
		}

		// all else fails..
		return null;
	}

	private static void doEssencePlaceEffects(PlayerEntity player, BeADoll.Variant variant) {
		// todo.. maybe a splash of particles here and on take
		player.addStatusEffect(new StatusEffectInstance(BeAWitch.OVERFLOWING, 300, 1));
		player.playSound(BeABirdwatcher.ESSENCE_PLACE, 1f, player.getRandom().nextFloat() * 0.2f + 0.9f);
		if (!player.getWorld().isClient()) {
			BeAMaid.setDoll(player, variant);
		}
	}

	private static void doEssenceTakeEffects(PlayerEntity player) {
		StatusEffectInstance fragmentation = player.getStatusEffect(BeAWitch.FRAGMENTED);
		player.addStatusEffect(new StatusEffectInstance(
			BeAWitch.FRAGMENTED,
			1200 + (fragmentation != null ? fragmentation.getDuration() : 0),
			(fragmentation != null ? fragmentation.getAmplifier() + 1 : 0)
		));
		player.playSound(BeABirdwatcher.ESSENCE_TAKE, 1f, player.getRandom().nextFloat() * 0.2f + 0.9f);
	}

	public static boolean isMouseInSurvivalSelf(double mouseX, double mouseY, int screenX, int screenY) {
		// inset from the black rectangle by 13
		int inset = 13;
		return mouseX > screenX + inset + 26
				&& mouseX < screenX - inset + 75
				&& mouseY > screenY + inset + 8
				&& mouseY < screenY - inset + 78;
	}

	public static boolean isMouseInCreativeSelf(double mouseX, double mouseY, int screenX, int screenY) {
		// inset from the black rectangle by ...
		int inset = 7;
		return mouseX > screenX + inset + 73
				&& mouseX < screenX - inset + 105
				&& mouseY > screenY + inset + 6
				&& mouseY < screenY - inset + 49;
	}
}
