package com.glyceryl6.staff.client.renderer;

import com.glyceryl6.staff.Main;
import com.glyceryl6.staff.client.model.StalagmiteModel;
import com.glyceryl6.staff.client.model.geom.KOModelLayers;
import com.glyceryl6.staff.common.entities.Stalagmite;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class StalagmiteRenderer extends EntityRenderer<Stalagmite> {

    private final StalagmiteModel model;

    public StalagmiteRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new StalagmiteModel(context.bakeLayer(KOModelLayers.STALAGMITE_LAYER));
    }

    @Override
    public void render(Stalagmite entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();
        poseStack.scale(-1.0F, -1.0F, 1.0F);
        poseStack.translate(0.0F, -1.5F, 0.0F);
        VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutout(this.getTextureLocation(entity)));
        this.model.renderToBuffer(poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY);
        poseStack.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(Stalagmite entity) {
        return Main.prefix("textures/entity/stalagmite.png");
    }

}