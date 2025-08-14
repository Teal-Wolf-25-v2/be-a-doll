package io.github.afamiliarquiet.be_a_doll;

import io.github.afamiliarquiet.be_a_doll.diary.BeABirdwatcher;
import io.github.afamiliarquiet.be_a_doll.diary.BeACollector;
import io.github.afamiliarquiet.be_a_doll.diary.BeALibrarian;
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
					// todo.. benefits n tfy stuff.
					if (!player.getWorld().isClient()) {
						BeAMaid.setDoll(player, variant);
					}
					player.playSound(BeABirdwatcher.ESSENCE_PLACE, 1f, player.getRandom().nextFloat() * 0.2f + 0.9f);
					return ItemStack.EMPTY;
				}
			}
		} else {
			if (cursorStack.isEmpty()) {
				// todo.. wooziness n sparkly essence stuff
				ItemStack fragment = BeACollector.ESSENCE_FRAGMENT.getDefaultStack();
				fragment.set(BeACollector.DOLL_VARIANT_COMPONENT, BeALibrarian.inspectSupposedPlayer(player));
				player.playSound(BeABirdwatcher.ESSENCE_TAKE, 1f, player.getRandom().nextFloat() * 0.2f + 0.9f);
				return fragment;
			}
		}

		// all else fails..
		return null;
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
