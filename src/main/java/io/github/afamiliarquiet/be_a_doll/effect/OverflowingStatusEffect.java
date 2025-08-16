package io.github.afamiliarquiet.be_a_doll.effect;

import io.github.afamiliarquiet.be_a_doll.BeAMaid;
import io.github.afamiliarquiet.be_a_doll.diary.BeAWitch;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.server.world.ServerWorld;

public class OverflowingStatusEffect extends StatusEffect {
	public OverflowingStatusEffect(StatusEffectCategory category, int color) {
		super(category, color);
	}

	public OverflowingStatusEffect(StatusEffectCategory category, int color, ParticleEffect particleEffect) {
		super(category, color, particleEffect);
	}

	@Override
	public boolean applyUpdateEffect(ServerWorld world, LivingEntity entity, int amplifier) {
		if (entity instanceof PlayerEntity playerEntity) {
			if (BeAMaid.isDoll(playerEntity)) { // dolls get a bit of repairs
				HungerManager hungry = playerEntity.getHungerManager();
				if (hungry.isNotFull()) {
					hungry.add(1, 0f);
				} else {
					hungry.add(1, 1f);
				}
			}

			// everybody gets a little bit of regen, but effectively one amplifier level less up til overflowing 6
			if (entity.getHealth() < entity.getMaxHealth()) {
				entity.heal(0.5f);
			}
		}

		return true;
	}

	@Override
	public boolean canApplyUpdateEffect(int duration, int amplifier) {
		int i = 50 >> amplifier;
		return i == 0 || duration % i == 0;
	}

	@Override
	public void onApplied(LivingEntity entity, int amplifier) {
		BeAWitch.annihilate(entity, entity.getStatusEffect(BeAWitch.OVERFLOWING), entity.getStatusEffect(BeAWitch.FRAGMENTED));
		super.onApplied(entity, amplifier);
	}
}
