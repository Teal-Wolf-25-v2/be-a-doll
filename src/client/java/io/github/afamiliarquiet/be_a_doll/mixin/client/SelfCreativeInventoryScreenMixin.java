package io.github.afamiliarquiet.be_a_doll.mixin.client;

import io.github.afamiliarquiet.be_a_doll.BeASelf;
import io.github.afamiliarquiet.be_a_doll.letters.C2SCreativeEssenceAlterationLetter;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CreativeInventoryScreen.class)
public abstract class SelfCreativeInventoryScreenMixin extends HandledScreen<CreativeInventoryScreen.CreativeScreenHandler> {
	public SelfCreativeInventoryScreenMixin(CreativeInventoryScreen.CreativeScreenHandler handler, PlayerInventory inventory, Text title) {
		super(handler, inventory, title);
	}

	// Hey! You, the one reading this code!
	// Are you annoyed/displeased/disgusted/revolted/terrified?
	// Tell me how I can do better! Save me! Please! Please, anyone, help me! Is anyone there?!
	// i don't like screens
	@Inject(method = "mouseReleased", at = @At("HEAD"), cancellable = true)
	private void clicky(Click click, CallbackInfoReturnable<Boolean> cir) {
		// injecting at head seems fine here. i could've injected at mouseClicked instead here, but.. consistency
		if (BeASelf.isMouseInCreativeSelf(click.x(), click.y(), this.x, this.y) && this.client != null && this.client.player != null) {
			ItemStack cursorStack = this.handler.getCursorStack();
			ItemStack clickProcessedStack = null;

			if (click.button() == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
				ClientPlayNetworking.send(new C2SCreativeEssenceAlterationLetter(true, cursorStack));
				clickProcessedStack = BeASelf.clickSelf(cursorStack, this.client.player, true);
			} else if (click.button() == GLFW.GLFW_MOUSE_BUTTON_RIGHT) {
				ClientPlayNetworking.send(new C2SCreativeEssenceAlterationLetter(false, cursorStack));
				clickProcessedStack = BeASelf.clickSelf(cursorStack, this.client.player, false);
			}

			if (clickProcessedStack != null) {
				this.handler.setCursorStack(clickProcessedStack);
				cir.setReturnValue(true);
			}
		}
	}
}
