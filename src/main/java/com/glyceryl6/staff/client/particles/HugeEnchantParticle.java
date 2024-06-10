package com.glyceryl6.staff.client.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.FlameParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.SimpleAnimatedParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.ParametersAreNonnullByDefault;

/** @noinspection deprecation*/
@OnlyIn(Dist.CLIENT)
public class HugeEnchantParticle extends SimpleAnimatedParticle {

    public HugeEnchantParticle(ClientLevel level, double x, double y, double z, SpriteSet sprites) {
        super(level, x, y, z, sprites, 0.0F);
        this.lifetime = (int)(8.0D / (Math.random() * 0.8D + 0.2D));
    }

    @Override
    public void move(double x, double y, double z) {
        this.setBoundingBox(this.getBoundingBox().move(x, y, z));
        this.setLocationFromBoundingbox();
    }

    @Override
    public float getQuadSize(float pScaleFactor) {
        float f = ((float)this.age + pScaleFactor) / (float)this.lifetime;
        return this.quadSize * (1.0F - f * f * 0.5F);
    }

    @Override
    public int getLightColor(float partialTick) {
        BlockPos blockPos = BlockPos.containing(this.x, this.y, this.z);
        return this.level.hasChunkAt(blockPos) ? LevelRenderer.getLightColor(this.level, blockPos) : 0;
    }

    @ParametersAreNonnullByDefault
    @OnlyIn(Dist.CLIENT)
    public static class HugeEnchantProvider extends FlameParticle.Provider {

        public HugeEnchantProvider(SpriteSet sprites) {
            super(sprites);
        }

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            Particle particle = super.createParticle(type, level, x, y, z, xSpeed, ySpeed, zSpeed);
            if (particle != null) {
                particle.scale(2.5F);
            }

            return particle;
        }

    }

}