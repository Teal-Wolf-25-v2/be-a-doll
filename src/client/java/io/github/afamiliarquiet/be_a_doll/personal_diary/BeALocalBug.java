package io.github.afamiliarquiet.be_a_doll.personal_diary;

import io.github.afamiliarquiet.be_a_doll.diary.BeABug;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import org.jetbrains.annotations.Nullable;

public class BeALocalBug {
	public static void lookAtBug() {
		ParticleFactoryRegistry bugParty = ParticleFactoryRegistry.getInstance();
		bugParty.register(BeABug.FRAGMENTED, FragmentedParticle.Factory::new);
	}

	public static class FragmentedParticle extends BillboardParticle {
		private static final Random RANDOM = Random.create();
		private final int fromColor;
		private final int toColor;

		protected FragmentedParticle(ClientWorld clientWorld, double x, double y, double z, int fromColor, int toColor, double xr, double zr) {
			// this is silly. 3 layers to avoid being restricted by super() on line 1 and ignore half of it anyway
			this(clientWorld, x, y, z, 0.05 - xr * 0.1, 0.0125 - RANDOM.nextDouble() * 0.025, 0.05 - zr  * 0.1, fromColor, toColor);
		}

		protected FragmentedParticle(ClientWorld clientWorld, double x, double y, double z, double vx, double vy, double vz, int fromColor, int toColor) {
			super(clientWorld, x+vx*2, y+vy*2, z+vz*2, 0, 0, 0);

			this.velocityX = vx;
			this.velocityY = vy;
			this.velocityZ = vz;

			this.scale = (this.random.nextFloat() * 0.01f + 0.02f);
			this.velocityMultiplier = 0.87F;
			this.gravityStrength = 0;

			this.fromColor = fromColor;
			this.toColor = toColor;
			setColorSimpler(this.fromColor);
		}

		@Override
		public void tick() {
			this.lastX = this.x;
			this.lastY = this.y;
			this.lastZ = this.z;
			if (this.age++ >= this.maxAge) {
				this.markDead();
			} else {
				this.move(this.velocityX, this.velocityY, this.velocityZ);

				this.velocityX = this.velocityX * this.velocityMultiplier;
				this.velocityY = this.velocityY * this.velocityMultiplier;
				this.velocityZ = this.velocityZ * this.velocityMultiplier;
				setColorSimpler(ColorHelper.lerp((float)this.age / this.maxAge, this.fromColor, this.toColor));
			}
		}

		private void setColorSimpler(int color) {
			this.setColor(ColorHelper.getRed(color) / 255.0F, ColorHelper.getGreen(color) / 255.0F, ColorHelper.getBlue(color) / 255.0F);
			this.setAlpha(ColorHelper.getAlpha(color) / 255.0F);
		}

		@Override
		protected RenderType getRenderType() {
			return RenderType.PARTICLE_ATLAS_TRANSLUCENT;
		}

		public static class Factory implements ParticleFactory<SimpleParticleType> {
			private final SpriteProvider spriteProvider;

			public Factory(SpriteProvider spriteProvider) {
				this.spriteProvider = spriteProvider;
			}

//			public Particle createParticle(SimpleParticleType simpleParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
//				FragmentedParticle bug = new FragmentedParticle(clientWorld, d, e, f, 0xff95a5e9, RANDOM.nextBoolean() ? 0xfff77490 : 0xfffab598, RANDOM.nextDouble(), RANDOM.nextDouble());
//				bug.scale(MathHelper.nextBetween(clientWorld.getRandom(), 3.0F, 5.0F));
//				bug.setSprite(this.spriteProvider);
//				return bug;
//			}

			@Override
			public @Nullable Particle createParticle(SimpleParticleType parameters, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, Random random) {
				FragmentedParticle bug = new FragmentedParticle(world, x, y, z, 0xff95a5e9, RANDOM.nextBoolean() ? 0xfff77490 : 0xfffab598, RANDOM.nextDouble(), RANDOM.nextDouble());
				bug.scale(MathHelper.nextBetween(world.getRandom(), 3.0F, 5.0F));
				return bug;
			}
		}
	}
}
