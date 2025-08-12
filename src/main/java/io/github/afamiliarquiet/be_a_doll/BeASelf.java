package io.github.afamiliarquiet.be_a_doll;

import io.github.afamiliarquiet.be_a_doll.diary.BeACollector;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class BeASelf {
	// todone - make more mixins where player is rendered, like creative inv and horse i think
	// nevermind not doing horse it'd be annoying to do it there and there's no need to play w/ essence on a horse
	// todo - something weird is happening w/ an extra essence showing up after closing inv, only in creative?
	public static @Nullable ItemStack clickSelf(ItemStack cursorStack, PlayerEntity player, boolean inserting) {
		if (inserting) {
			BeADoll.log("inserting..");
			BeADoll.log(String.valueOf(cursorStack.isEmpty()));
			if (cursorStack.isOf(BeACollector.ESSENCE_FRAGMENT)) {
				BeADoll.log("inserting.. halfway there");
				BeADoll.Variant variant = cursorStack.get(BeACollector.DOLL_VARIANT_COMPONENT);
				if (variant != null) {
					// todo.. benefits n tfy stuff
					BeADoll.log("Congrats! You're a " + variant.asString() + " doll now!");
					if (!player.getWorld().isClient()) {
						BeAMaid.setDoll(player, variant);
					}
					return ItemStack.EMPTY;
				}
			}
		} else {
			BeADoll.log("extracting..");
			if (cursorStack.isEmpty()) {
				// todo.. wooziness n sparkly essence stuff n also the appropriate doll component
				// todo wait crap. i have to store the doll type somewhere now crap this sucks
				// i think i just have an attachment for that and have the default be player
				// so if it gets desynced somehow.. whatever. fragment breaks a lil, that's fine.
				// actually maybe default is one of the dolls and then i check isDoll for whether to keep that or repress
				// orrrr... i give the different dolls different attributes and then measure their attributes...
				BeADoll.log("Ow..?");
				return BeACollector.ESSENCE_FRAGMENT.getDefaultStack();
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
