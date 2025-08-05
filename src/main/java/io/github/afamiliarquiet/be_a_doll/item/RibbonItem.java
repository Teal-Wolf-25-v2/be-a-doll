package io.github.afamiliarquiet.be_a_doll.item;

import io.github.afamiliarquiet.be_a_doll.BeAMaid;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class RibbonItem extends Item {
	public RibbonItem(Settings settings) {
		super(settings);
	}

	@Override
	public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
		if (entity instanceof PlayerEntity doll && BeAMaid.isDoll(doll)) {
			doll.startRiding(user, false);
			return ActionResult.SUCCESS;
		}
		return super.useOnEntity(stack, user, entity, hand);
	}

	@Override
	public ActionResult use(World world, PlayerEntity user, Hand hand) {
		// todone - verify isclient is fine here
		// yeah no lol. did you not see the C2SDollDismountLetter i had to make? client's gotta hear about this
		if (/*!user.getWorld().isClient && */!user.getPassengerList().isEmpty() && user.shouldCancelInteraction()) {
			user.removeAllPassengers();
			return ActionResult.SUCCESS;
		} else {
			return super.use(world, user, hand);
		}
	}
}
