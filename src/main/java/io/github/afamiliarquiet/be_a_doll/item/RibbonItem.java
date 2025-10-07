package io.github.afamiliarquiet.be_a_doll.item;

import io.github.afamiliarquiet.be_a_doll.BeAMaid;
import io.github.afamiliarquiet.be_a_doll.diary.BeABirdwatcher;
import io.github.afamiliarquiet.be_a_doll.mixin.synthetic_treats.FoxEntityTrustInvoker;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.LazyEntityReference;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Tameable;
import net.minecraft.entity.passive.FoxEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

public class RibbonItem extends Item {
	public RibbonItem(Settings settings) {
		super(settings);
	}

	@Override
	public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
		if (entity instanceof PlayerEntity doll && BeAMaid.isDoll(doll)) {
			if (doll.startRiding(user, false, false)) {
				user.playSound(BeABirdwatcher.RAVEN_CHIRP, 1f, 1f);
				return ActionResult.SUCCESS;
			}
		} else {
			ActionResult tried = useToTryRiding(stack, user, entity, hand);
			if (tried.isAccepted()) {
				return tried;
			}
		}

		return super.useOnEntity(stack, user, entity, hand);
	}

	public ActionResult useToTryRiding(ItemStack stack, PlayerEntity user, Entity entity, Hand hand) {
		if (BeAMaid.isDoll(user)) {
			// ohh.. so the user was the doll!
			boolean shouldRide = false;
			if (entity instanceof Tameable tameable) {
				LazyEntityReference<LivingEntity> ownerRef = tameable.getOwnerReference();
				if (ownerRef != null && ownerRef.uuidEquals(user) && entity.getWidth() > user.getWidth()) {
					shouldRide = true;
				}
			} else if (entity instanceof FoxEntity foxesAreSoCool && ((FoxEntityTrustInvoker)foxesAreSoCool).invokeCanTrust(user)) {
				shouldRide = true;
			}

			if (shouldRide && user.startRiding(entity)) {
				user.playSound(BeABirdwatcher.RAVEN_CHIRP, 1f, 1f);
				return ActionResult.SUCCESS;
			}
		}

		return ActionResult.PASS;
	}

	@Override
	public ActionResult use(World world, PlayerEntity user, Hand hand) {
		// yeah no lol. did you not see the C2SDollDismountLetter i had to make? client's gotta hear about this
		if (!user.getPassengerList().isEmpty() && user.shouldCancelInteraction()) {
			Entity doll = user.getPassengerList().getLast();
			BlockHitResult blockHitResult = raycast(world, user, RaycastContext.FluidHandling.NONE);
			Vec3d pos;
			if (!world.isClient()
				&& blockHitResult.getType() == HitResult.Type.BLOCK
				&& doll instanceof ServerPlayerEntity serverPlayerEntity
				&& (pos = getDollPlacementPos(blockHitResult, doll)) != null
			) {
				serverPlayerEntity.teleportTo(new TeleportTarget(
					serverPlayerEntity.getEntityWorld(),
					pos,
					Vec3d.ZERO,
					user.getYaw() + 180,
					user.getPitch() * -1,
					TeleportTarget.NO_OP
				));
			} else {
				doll.stopRiding();
			}
			user.playSound(BeABirdwatcher.RAVEN_CRY, 1f, 1f);
			return ActionResult.SUCCESS;
		} else {
			return super.use(world, user, hand);
		}
	}

	public static @Nullable Vec3d getDollPlacementPos(BlockHitResult blockHitResult, Entity doll) {
		Vec3d pos = blockHitResult.getPos();
		EntityDimensions dollStanding = doll.getDimensions(EntityPose.STANDING);

		if (blockHitResult.getSide().getAxis() == Direction.Axis.Y) {
			if (blockHitResult.getSide() == Direction.DOWN) {
				pos = pos.add(0, -dollStanding.height(), 0);
			}

			Vec3d firstCheck = checkForCollisionsOnAxis(doll, dollStanding, pos, Direction.Axis.X);
			if (firstCheck != null) {
				pos = firstCheck;
			} else {
				pos = checkForCollisionsOnAxis(doll, dollStanding, pos, Direction.Axis.Z);
			}
		} else {
			Vector3f sideVec = blockHitResult.getSide().getUnitVector();
			pos = pos.add(sideVec.x * dollStanding.width() / 2, 0, sideVec.z * dollStanding.width() / 2);
			pos = checkForCollisionsOnAxis(doll, dollStanding, pos, Direction.Axis.Y);
		}

		if (pos == null || doll.getEntityWorld().getBlockCollisions(doll, dollStanding.getBoxAt(pos)).iterator().hasNext()) { // updated getWorld() → getEntityWorld()
			return null;
		} else {
			return pos;
		}
	}

	private static @Nullable Vec3d checkForCollisionsOnAxis(Entity doll, EntityDimensions dollStanding, @Nullable Vec3d pos, Direction.Axis axis) {
		if (pos == null || !doll.getEntityWorld().getBlockCollisions(doll, dollStanding.getBoxAt(pos)).iterator().hasNext()) { // updated
			return pos;
		} else {
			double xyz = pos.getComponentAlongAxis(axis);
			double positiveEdgeCrumb = (xyz + (axis.isVertical() ? dollStanding.height() : dollStanding.width() / 2)) - Math.ceil(xyz);
			double negativeEdgeCrumb = Math.floor(xyz) - (xyz - (axis.isVertical() ? 0 : dollStanding.width() / 2));

			if (positiveEdgeCrumb < 0.5 && positiveEdgeCrumb > 0
				&& !doll.getEntityWorld().getBlockCollisions(doll, dollStanding.getBoxAt(pos.withAxis(axis, xyz - positiveEdgeCrumb))).iterator().hasNext()) {
				return pos.withAxis(axis, xyz - positiveEdgeCrumb);
			} else if (negativeEdgeCrumb < 0.5 && negativeEdgeCrumb > 0
				&& !doll.getEntityWorld().getBlockCollisions(doll, dollStanding.getBoxAt(pos.withAxis(axis, xyz + negativeEdgeCrumb))).iterator().hasNext()) {
				return pos.withAxis(axis, xyz + negativeEdgeCrumb);
			} else {
				return null;
			}
		}
	}
}
