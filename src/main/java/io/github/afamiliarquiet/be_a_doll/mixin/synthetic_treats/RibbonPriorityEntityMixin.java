package io.github.afamiliarquiet.be_a_doll.mixin.synthetic_treats;

import io.github.afamiliarquiet.be_a_doll.diary.BeACollector;
import io.github.afamiliarquiet.be_a_doll.item.RibbonItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class RibbonPriorityEntityMixin {
	@Inject(method = "interact", at = @At(value = "HEAD"), cancellable = true)
	private void orRibbon(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
		// i wanted to try injecting after the method already got the stack in hand for me but..
		ItemStack handStack = player.getStackInHand(hand);
		if (handStack.isOf(BeACollector.DOLL_RIBBON)) {
			cir.setReturnValue(((RibbonItem)handStack.getItem()).useToTryRiding(handStack, player, (Entity)(Object)this, hand));
		}
	}
}
