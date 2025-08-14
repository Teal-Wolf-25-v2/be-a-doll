package io.github.afamiliarquiet.be_a_doll.item;

import io.github.afamiliarquiet.be_a_doll.BeADoll;
import io.github.afamiliarquiet.be_a_doll.BeAMaid;
import io.github.afamiliarquiet.be_a_doll.diary.BeALibrarian;
import io.github.afamiliarquiet.be_a_doll.diary.BeAWitch;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.consume.UseAction;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

import java.util.function.Predicate;

public class DollcraftItem extends Item {
	protected BeADoll.Variant variant;

	public DollcraftItem(Settings settings, BeADoll.Variant variant) {
		super(settings);
		this.variant = variant;
	}

	// care for self
	@Override
	public ActionResult use(World world, PlayerEntity user, Hand hand) {
		if (BeAMaid.isDoll(user) && !findCareMaterial(user).isEmpty()) {
			user.setCurrentHand(hand);
			return ActionResult.CONSUME;
		}

		return super.use(world, user, hand);
	}

	@Override
	public UseAction getUseAction(ItemStack stack) {
		return UseAction.BRUSH;
	}

	@Override
	public int getMaxUseTime(ItemStack stack, LivingEntity user) {
		return 62;
	}

	@Override
	public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
		if (user instanceof PlayerEntity doll) {
			performCare(doll, doll, stack);
		}

		return super.finishUsing(stack, world, user);
	}

	// care for other
	@Override
	public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
		// todo - maybe make this more like self-use, take a little bit of time (but less)
		if (entity instanceof PlayerEntity doll) {
			ActionResult careResult = performCare(user, doll, stack);
			if (careResult.isAccepted()) {
				return careResult;
			}
		}

		return super.useOnEntity(stack, user, entity, hand);
	}

	public ActionResult performCare(PlayerEntity user, PlayerEntity doll, ItemStack dollcraftStack) {
		if (BeAMaid.isDoll(doll) && BeALibrarian.inspectDollMaterial(doll) == this.variant) {
			if (user.isInCreativeMode()) {
				caringIsCaring(doll);
			} else {
				ItemStack material = findCareMaterial(user);
				if (!material.isEmpty()) {
					material.split(1);
					// todo - consume durability from tool
					caringIsCaring(doll);
				}
			}
		}
		return ActionResult.PASS;
	}

	private void caringIsCaring(PlayerEntity doll) {
		// dolls get full saturation and some absorption every time because i love them (because they are love)
		// todo - maybe change this absorption to a special doll copy of absorption? but...
		//  if i do the retexture stuff per-doll it's gonna be annoying. i'd have to retexture all the hearts basically,
		//  cause absorption isn't specific to source and then if all absorption is that why not all hearts..
		//  but retexturing all the hearts and food isn't terrible.
		// todo - particles n sound here, maybe a usage tick too
		doll.getHungerManager().add(4, 5);
		doll.addStatusEffect(new StatusEffectInstance(BeAWitch.CARED_FOR, -1, 2, false, false));
	}

	/**
	 * for bows this is where creative mode is checked and can provide a dummy arrow.
	 * however dolls have varied materials that don't matter, please check creative elsewhere
	 */
	public ItemStack findCareMaterial(PlayerEntity user) {
		Predicate<ItemStack> predicate = stack -> stack.isIn(this.variant.getCareMaterialTag());

		// why isn't there an easy .getOpposite like arm has :(
//		ItemStack otherHandStack = user.getStackInHand(usingHand == Hand.MAIN_HAND ? Hand.OFF_HAND : Hand.MAIN_HAND);
//		if (predicate.test(otherHandStack)) {
//			return otherHandStack;
//		} else {
			for (int i = 0; i < user.getInventory().size(); i++) {
				ItemStack current = user.getInventory().getStack(i);
				if (predicate.test(current)) {
					return current;
				}
			}

			return ItemStack.EMPTY;
//		}
	}
}
