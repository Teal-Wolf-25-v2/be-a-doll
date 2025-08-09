package io.github.afamiliarquiet.be_a_doll.item;

import io.github.afamiliarquiet.be_a_doll.BeAMaid;
import io.github.afamiliarquiet.be_a_doll.diary.BeABirdwatcher;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class RibbonItem extends Item {
	public RibbonItem(Settings settings) {
		super(settings);
	}

	@Override
	public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
		// todo - sounds, like lead?
		if (entity instanceof PlayerEntity doll && BeAMaid.isDoll(doll)) {
			doll.startRiding(user, false);
			// todo - world check on this sound?
			user.getWorld().playSound(null, user.getX(), user.getY(), user.getZ(), BeABirdwatcher.RAVEN_CHIRP, SoundCategory.PLAYERS, 1f, 1f);
			return ActionResult.SUCCESS;
		}
		return super.useOnEntity(stack, user, entity, hand);
	}

	@Override
	public ActionResult use(World world, PlayerEntity user, Hand hand) {
		// todo - maybe consume it while in use like lead? but then need a different force dismount option
		// todone - verify isclient is fine here
		// yeah no lol. did you not see the C2SDollDismountLetter i had to make? client's gotta hear about this
		if (/*!user.getWorld().isClient && */!user.getPassengerList().isEmpty() && user.shouldCancelInteraction()) {
			user.removeAllPassengers();
			user.getWorld().playSound(null, user.getX(), user.getY(), user.getZ(), BeABirdwatcher.RAVEN_CRY, SoundCategory.PLAYERS, 1f, 1f);
			return ActionResult.SUCCESS;
		} else {
			return super.use(world, user, hand);
		}
	}
}
