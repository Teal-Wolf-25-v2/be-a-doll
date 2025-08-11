package io.github.afamiliarquiet.be_a_doll.mixin.client;

import io.github.afamiliarquiet.be_a_doll.BeASelf;
import io.github.afamiliarquiet.be_a_doll.letters.C2SEssenceAlterationLetter;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
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
	private void clicky(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
		// injecting at head seems fine here. i could've injected at mouseClicked instead here, but.. consistency
		if (BeASelf.isMouseInCreativeSelf(mouseX, mouseY, this.x, this.y)) {
			ItemStack cursorStack = this.handler.getCursorStack();
			ItemStack clickProcessedStack = null;

			if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
				ClientPlayNetworking.send(new C2SEssenceAlterationLetter(true));
				clickProcessedStack = BeASelf.clickSelf(cursorStack, true);
			} else if (button == GLFW.GLFW_MOUSE_BUTTON_RIGHT) {
				ClientPlayNetworking.send(new C2SEssenceAlterationLetter(false));
				clickProcessedStack = BeASelf.clickSelf(cursorStack, false);
			}

			if (clickProcessedStack != null) {
				this.handler.setCursorStack(clickProcessedStack);
				cir.setReturnValue(true);
			}
		}
	}
}
