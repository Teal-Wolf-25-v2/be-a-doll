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
		if (!world.isClient()) {
			BeAMaid.setDoll(user, !BeAMaid.isDoll(user));
		}
		return super.use(world, user, hand);
	}
}
