package io.github.afamiliarquiet.be_a_doll.item;

import io.github.afamiliarquiet.be_a_doll.BeADoll;
import io.github.afamiliarquiet.be_a_doll.BeAMaid;
import io.github.afamiliarquiet.be_a_doll.diary.BeABirdwatcher;
import io.github.afamiliarquiet.be_a_doll.diary.BeALibrarian;
import io.github.afamiliarquiet.be_a_doll.diary.BeAWitch;
import io.github.afamiliarquiet.be_a_doll.letters.S2CDollRepairedLetter;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.UseCooldownComponent;
import net.minecraft.component.type.WeaponComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.consume.UseAction;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
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
		return UseAction.BRUSH;
	}

	@Override
	public int getMaxUseTime(ItemStack stack, LivingEntity user) {
		return 62;
	}

	@Override
	public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
		if (user instanceof PlayerEntity praiseTheDoll) {
			ItemStack material = findCareMaterial(praiseTheDoll);
			if (material.isEmpty()) {
				user.stopUsingItem();
			} else {
				if (remainingUseTicks < 6 && remainingUseTicks > -6) { // trying to account for latency lag with the -6
					if (remainingUseTicks % 4 == 3) {
						praiseTheDoll.playSound(BeABirdwatcher.CARE_COMPLETE, 1f, praiseTheDoll.getRandom().nextFloat() * 0.2f + 0.9f);
					}
				} else if (remainingUseTicks % 10 == 7) {
					spawnRepairParticles(praiseTheDoll, material, 5);
					SoundEvent careSound = switch (BeALibrarian.inspectDollMaterial(praiseTheDoll)) {
						case WOODEN -> BeABirdwatcher.CARE_WOODEN;
						case PORCELAIN -> BeABirdwatcher.CARE_PORCELAIN;
						case CLOTH -> BeABirdwatcher.CARE_CLOTH;
						case REPRESSED -> SoundEvents.BLOCK_ANVIL_FALL;
					};
					praiseTheDoll.playSound(careSound, 1f, praiseTheDoll.getRandom().nextFloat() * 0.2f + 0.9f);
				}
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
		if (entity instanceof PlayerEntity doll && !user.getItemCooldownManager().isCoolingDown(stack)) {
			ActionResult careResult = performCare(user, doll, stack, hand);
			if (careResult.isAccepted()) {
				if (!user.getWorld().isClient()) {
					S2CDollRepairedLetter letter = new S2CDollRepairedLetter(doll.getId(), stack);
					PlayerLookup.tracking(doll).forEach(player -> ServerPlayNetworking.send(player, letter));
					ServerPlayNetworking.send((ServerPlayerEntity) doll, letter);
				}
				spawnRepairParticles(doll, findCareMaterial(user), 16);
				UseCooldownComponent cooldownComponent = stack.get(DataComponentTypes.USE_COOLDOWN);
				if (cooldownComponent != null) {
					cooldownComponent.set(stack, user);
				}

				return careResult;
			}
		}

		return super.useOnEntity(stack, user, entity, hand);
	}

	public ActionResult performCare(PlayerEntity user, PlayerEntity doll, ItemStack dollcraftStack, Hand hand) {
		if (BeAMaid.isDoll(doll) && BeALibrarian.inspectDollMaterial(doll) == this.variant) {
			ItemStack material = findCareMaterial(user);
			if (!material.isEmpty()) {
				caringIsCaring(doll);
				material.split(1);
				dollcraftStack.damage(1, user, LivingEntity.getSlotForHand(hand));
				return ActionResult.SUCCESS;
			}
		}
		return ActionResult.PASS;
	}

	private void caringIsCaring(PlayerEntity doll) {
		// dolls get full saturation and some absorption every time because i love them (because they are love)
		doll.playSound(BeABirdwatcher.CARE_COMPLETE, 1f, doll.getRandom().nextFloat() * 0.2f + 0.9f);
		doll.getHungerManager().add(4, 5);
		doll.addStatusEffect(new StatusEffectInstance(BeAWitch.CARED_FOR, -1, 2, false, false));
	}

	public ItemStack findCareMaterial(PlayerEntity user) {
		if (user.isInCreativeMode() || user.getWorld().isClient() && !user.isMainPlayer()) { // otherclientplayers have no inv, so cheat for particles
			return switch(this.variant) {
				case WOODEN -> Items.STICK.getDefaultStack();
				case PORCELAIN -> Items.CLAY_BALL.getDefaultStack();
				case CLOTH -> Items.STRING.getDefaultStack();
				default -> ItemStack.EMPTY;
			};
		} else {
			Predicate<ItemStack> predicate = stack -> stack.isIn(this.variant.getCareMaterialTag());

			for (int i = 0; i < user.getInventory().size(); i++) {
				ItemStack current = user.getInventory().getStack(i);
				if (predicate.test(current)) {
					return current;
				}
			}

			return ItemStack.EMPTY;
		}
	}

	public static void spawnRepairParticles(PlayerEntity doll, ItemStack material, int count) {
		for (int i = 0; i < count; i++) {
			Vec3d vel = new Vec3d(
				(doll.getRandom().nextFloat() - 0.5) * 0.1,
				Math.random() * 0.1 + 0.1,
				(doll.getRandom().nextFloat() - 0.5) * 0.1);

			Box dollHouse = doll.getBoundingBox();
			Vec3d pos = new Vec3d(
				doll.getRandom().nextDouble() * dollHouse.getLengthX(),
				doll.getRandom().nextDouble() * dollHouse.getLengthY(),
				doll.getRandom().nextDouble() * dollHouse.getLengthZ()
			);
			pos = pos.add(dollHouse.getMinPos());

			doll.getWorld().addParticleClient(new ItemStackParticleEffect(ParticleTypes.ITEM, material), pos.x, pos.y, pos.z, vel.x, vel.y + 0.05, vel.z);
		}
	}
}
