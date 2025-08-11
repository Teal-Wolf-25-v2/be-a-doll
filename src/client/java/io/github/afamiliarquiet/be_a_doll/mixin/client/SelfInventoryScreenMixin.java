package io.github.afamiliarquiet.be_a_doll.mixin.client;

import io.github.afamiliarquiet.be_a_doll.BeASelf;
import io.github.afamiliarquiet.be_a_doll.letters.C2SEssenceAlterationLetter;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.screen.ingame.RecipeBookScreen;
import net.minecraft.client.gui.screen.recipebook.RecipeBookWidget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(InventoryScreen.class)
public abstract class SelfInventoryScreenMixin extends RecipeBookScreen<PlayerScreenHandler> {
	public SelfInventoryScreenMixin(PlayerScreenHandler handler, RecipeBookWidget<?> recipeBook, PlayerInventory inventory, Text title) {
		super(handler, recipeBook, inventory, title);
	}

	// Hey! You, the one reading this code!
	// Are you annoyed/displeased/disgusted/revolted/terrified?
	// Tell me how I can do better! Save me! Please! Please, anyone, help me! Is anyone there?!
	// i don't like screens
	@Inject(method = "mouseReleased", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ingame/RecipeBookScreen;mouseReleased(DDI)Z"), cancellable = true)
	private void clicky(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
		// todone i think - if im injecting head i may want to help out with the mouseReleased thing but.. mess
		//  i don't want to let super get called because that does other slot stuff
		if (BeASelf.isMouseInSurvivalSelf(mouseX, mouseY, this.x, this.y)) {
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
