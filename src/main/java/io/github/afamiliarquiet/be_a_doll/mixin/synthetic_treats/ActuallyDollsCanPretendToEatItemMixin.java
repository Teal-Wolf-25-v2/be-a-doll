package io.github.afamiliarquiet.be_a_doll.mixin.synthetic_treats;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import io.github.afamiliarquiet.be_a_doll.BeAMaid;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Item.class)
public class ActuallyDollsCanPretendToEatItemMixin {
	@Definition(id = "getConsumeTicks", method = "Lnet/minecraft/component/type/ConsumableComponent;getConsumeTicks()I")
	@Expression("?.getConsumeTicks()")
	@ModifyExpressionValue(method = "getMaxUseTime", at = @At("MIXINEXTRAS:EXPRESSION"))
	private int slowlyNibbling(int original, @Local(argsOnly = true, ordinal = 0) ItemStack stack, @Local(argsOnly = true, ordinal = 0) LivingEntity user) {
		if (stack.get(DataComponentTypes.FOOD) != null && user instanceof PlayerEntity player && BeAMaid.isDoll(player)) {
			// note the magic number 3
			return original * 3;
		} else {
			return original;
		}
	}
}
