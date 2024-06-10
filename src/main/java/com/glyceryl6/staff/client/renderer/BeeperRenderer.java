package com.glyceryl6.staff.client.renderer;

import com.glyceryl6.staff.Main;
import com.glyceryl6.staff.common.entities.Beeper;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.BeeModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BeeperRenderer extends MobRenderer<Beeper, BeeModel<Beeper>> {
    
    private static final ResourceLocation ANGRY_BEEPER_TEXTURE = Main.prefix("textures/entity/beeper/beeper_angry.png");
    private static final ResourceLocation ANGRY_NECTAR_BEEPER_TEXTURE = Main.prefix("textures/entity/beeper/beeper_angry_nectar.png");
    private static final ResourceLocation BEEPER_TEXTURE = Main.prefix("textures/entity/beeper/beeper.png");
    private static final ResourceLocation NECTAR_BEEPER_TEXTURE = Main.prefix("textures/entity/beeper/beeper_nectar.png");
    
    public BeeperRenderer(EntityRendererProvider.Context context) {
        super(context, new BeeModel<>(context.bakeLayer(Main.BEEPER_LAYER)), 0.4F);
    }

    @Override
    protected void scale(Beeper entity, PoseStack poseStack, float partialTickTime) {
        float f = entity.getSwelling(partialTickTime);
        float f1 = 1.0F + Mth.sin(f * 100.0F) * f * 0.01F;
        f = Mth.clamp(f, 0.0F, 1.0F);
        f *= f;
        f *= f;
        float f2 = (1.0F + f * 0.4F) * f1;
        float f3 = (1.0F + f * 0.1F) / f1;
        poseStack.scale(f2, f3, f2);
    }

    @Override
    protected float getWhiteOverlayProgress(Beeper entity, float partialTicks) {
        float f = entity.getSwelling(partialTicks);
        return (int)(f * 10.0F) % 2 == 0 ? 0.0F : Mth.clamp(f, 0.5F, 1.0F);
    }

    @Override
    public ResourceLocation getTextureLocation(Beeper entity) {
        if (entity.isAngry()) {
            return entity.hasNectar() ? ANGRY_NECTAR_BEEPER_TEXTURE : ANGRY_BEEPER_TEXTURE;
        } else {
            return entity.hasNectar() ? NECTAR_BEEPER_TEXTURE : BEEPER_TEXTURE;
        }
    }
    
}