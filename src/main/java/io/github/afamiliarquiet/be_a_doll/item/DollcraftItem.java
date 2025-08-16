package io.github.afamiliarquiet.be_a_doll.item;

import io.github.afamiliarquiet.be_a_doll.BeADoll;
import io.github.afamiliarquiet.be_a_doll.BeAMaid;
import io.github.afamiliarquiet.be_a_doll.diary.BeABirdwatcher;
import io.github.afamiliarquiet.be_a_doll.diary.BeALibrarian;
import io.github.afamiliarquiet.be_a_doll.diary.BeAWitch;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.UseCooldownComponent;
import net.minecraft.component.type.WeaponComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.consume.UseAction;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

import java.util.function.Predicate;

public class DollcraftItem extends Item {
	protected BeADoll.Variant variant;

	public DollcraftItem(Settings settings, BeADoll.Variant variant) {
		super(settings.useCooldown(1.3f).component(DataComponentTypes.WEAPON, new WeaponComponent(1)));
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
		// todo - looks wonky in off hand because of transforms. copy brush model/itemstuffs
		return UseAction.BRUSH;
	}

	@Override
	public int getMaxUseTime(ItemStack stack, LivingEntity user) {
		return 62;
	}

	@Override
	public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
		if (user instanceof PlayerEntity praiseTheDoll) {
			if (remainingUseTicks < 6 && remainingUseTicks > -6) { // trying to account for latency lag with the -6
				if (remainingUseTicks % 4 == 3) {
					praiseTheDoll.playSound(BeABirdwatcher.CARE_COMPLETE, 1f, praiseTheDoll.getRandom().nextFloat() * 0.2f + 0.9f);
				}
			} else if (remainingUseTicks % 10 == 7) {
				SoundEvent careSound = switch (BeALibrarian.inspectDollMaterial(praiseTheDoll)) {
					case WOODEN -> BeABirdwatcher.CARE_WOODEN;
					case PORCELAIN -> BeABirdwatcher.CARE_PORCELAIN;
					case CLOTH -> BeABirdwatcher.CARE_CLOTH;
					case REPRESSED -> SoundEvents.BLOCK_ANVIL_FALL;
				};
				praiseTheDoll.playSound(careSound, 1f, praiseTheDoll.getRandom().nextFloat() * 0.2f + 0.9f);
			}
		}
		super.usageTick(world, user, stack, remainingUseTicks);
	}

	@Override
	public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
		if (user instanceof PlayerEntity doll) {
			performCare(doll, doll, stack, user.getActiveHand());
		}

		return super.finishUsing(stack, world, user);
	}

	// care for other
	@Override
	public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
		// todo - maybe make this more like self-use, take a little bit of time (but less)
		if (entity instanceof PlayerEntity doll && !user.getItemCooldownManager().isCoolingDown(stack)) {
			ActionResult careResult = performCare(user, doll, stack, hand);
			if (careResult.isAccepted()) {
				UseCooldownComponent cooldownComponent = stack.get(DataComponentTypes.USE_COOLDOWN);
				if (cooldownComponent != null) {
					cooldownComponent.set(stack, user);
				} // todo - add some of the self-clean effects here too? related to the above

				return careResult;
			}
		}

		return super.useOnEntity(stack, user, entity, hand);
	}

	public ActionResult performCare(PlayerEntity user, PlayerEntity doll, ItemStack dollcraftStack, Hand hand) {
		if (BeAMaid.isDoll(doll) && BeALibrarian.inspectDollMaterial(doll) == this.variant) {
			if (user.isInCreativeMode()) {
				caringIsCaring(doll);
				return ActionResult.SUCCESS;
			} else {
				ItemStack material = findCareMaterial(user);
				if (!material.isEmpty()) {
					material.split(1);
					dollcraftStack.damage(1, user, LivingEntity.getSlotForHand(hand));
					caringIsCaring(doll);
					return ActionResult.SUCCESS;
				}
			}
		}
		return ActionResult.PASS;
	}

	private void caringIsCaring(PlayerEntity doll) {
		// dolls get full saturation and some absorption every time because i love them (because they are love)
		// todo - particles here, maybe a usage tick too
		//  weaving particle is nice for cloth dolls
		doll.playSound(BeABirdwatcher.CARE_COMPLETE, 1f, doll.getRandom().nextFloat() * 0.2f + 0.9f);
		doll.getHungerManager().add(4, 5);
		doll.addStatusEffect(new StatusEffectInstance(BeAWitch.CARED_FOR, -1, 2, false, false));
	}

	/**
	 * for bows this is where creative mode is checked and can provide a dummy arrow.
	 * however dolls have varied materials that don't matter, please check creative elsewhere
	 */
	public ItemStack findCareMaterial(PlayerEntity user) {
		if (this.variant == BeALibrarian.inspectDollMaterial(user)) {
			Predicate<ItemStack> predicate = stack -> stack.isIn(this.variant.getCareMaterialTag());

			for (int i = 0; i < user.getInventory().size(); i++) {
				ItemStack current = user.getInventory().getStack(i);
				if (predicate.test(current)) {
					return current;
				}
			}
		}

		return ItemStack.EMPTY;
	}
}
