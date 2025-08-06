package io.github.afamiliarquiet.be_a_doll.item;

import io.github.afamiliarquiet.be_a_doll.BeAMaid;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class DollcraftItem extends Item {
	public DollcraftItem(Settings settings) {
		super(settings);
	}

	@Override
	public ActionResult use(World world, PlayerEntity user, Hand hand) {
		// much todo here but certainly remember to give it a reasonably long cooldown
		if (!world.isClient()) {
			BeAMaid.setDoll(user, !BeAMaid.isDoll(user)); // probably should force dolls to dismount too
			return ActionResult.SUCCESS;
		} else {
			return ActionResult.SUCCESS_SERVER;
		}
	}
}
