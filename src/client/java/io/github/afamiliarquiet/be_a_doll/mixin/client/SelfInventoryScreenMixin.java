package io.github.afamiliarquiet.be_a_doll.mixin.client;

import io.github.afamiliarquiet.be_a_doll.BeASelf;
import io.github.afamiliarquiet.be_a_doll.letters.C2SEssenceAlterationLetter;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.Click;
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
	@Inject(method = "mouseReleased", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ingame/RecipeBookScreen;mouseReleased(Lnet/minecraft/client/gui/Click;)Z"), cancellable = true)
	private void clicky(Click click, CallbackInfoReturnable<Boolean> cir) {
		// todone i think - if im injecting head i may want to help out with the mouseReleased thing but.. mess
		//  i don't want to let super get called because that does other slot stuff
		if (BeASelf.isMouseInSurvivalSelf(click.x(), click.y(), this.x, this.y) && this.client != null && this.client.player != null) {
			ItemStack cursorStack = this.handler.getCursorStack();
			ItemStack clickProcessedStack = null;

			if (click.button() == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
				ClientPlayNetworking.send(new C2SEssenceAlterationLetter(true));
				clickProcessedStack = BeASelf.clickSelf(cursorStack, this.client.player, true);
			} else if (click.button() == GLFW.GLFW_MOUSE_BUTTON_RIGHT) {
				ClientPlayNetworking.send(new C2SEssenceAlterationLetter(false));
				clickProcessedStack = BeASelf.clickSelf(cursorStack, this.client.player, false);
			}

			if (clickProcessedStack != null) {
				this.handler.setCursorStack(clickProcessedStack);
				cir.setReturnValue(true);
			}
		}
	}
}
