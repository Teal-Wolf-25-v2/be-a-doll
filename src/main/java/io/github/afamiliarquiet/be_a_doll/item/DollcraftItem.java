package io.github.afamiliarquiet.be_a_doll.item;

import net.minecraft.item.Item;

public class DollcraftItem extends Item {
	public DollcraftItem(Settings settings) {
		super(settings);
	}

	// todo - im hungry lets get something that's REALLY tasty
//	@Override
//	public ActionResult use(World world, PlayerEntity user, Hand hand) {
//		// much todo here but certainly remember to give it a reasonably long cooldown
//		if (!world.isClient()) {
//			BeAMaid.setDoll(user, !BeAMaid.isDoll(user)); // probably should force dolls to dismount too
//			return ActionResult.SUCCESS;
//		} else {
//			return ActionResult.SUCCESS_SERVER;
//		}
//	}
}
