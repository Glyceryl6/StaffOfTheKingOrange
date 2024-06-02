package com.glyceryl6.staff.client.renderer;

import com.glyceryl6.staff.common.entities.projectile.visible.MusicalNote;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class MusicalNoteRenderer extends EntityRenderer<MusicalNote> {

    private static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation("textures/particle/note.png");
    private static final RenderType RENDER_TYPE = RenderType.entityCutoutNoCull(TEXTURE_LOCATION);

    public MusicalNoteRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected int getBlockLightLevel(MusicalNote entity, BlockPos pos) {
        return 15;
    }

    @Override
    public void render(MusicalNote entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();
        PoseStack.Pose pose = poseStack.last();
        poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
        VertexConsumer consumer = buffer.getBuffer(RENDER_TYPE);
        vertex(consumer, pose, packedLight, 0.0F, 0.0F, 0.0F, 1.0F);
        vertex(consumer, pose, packedLight, 1.0F, 0.0F, 1.0F, 1.0F);
        vertex(consumer, pose, packedLight, 1.0F, 1.0F, 1.0F, 0.0F);
        vertex(consumer, pose, packedLight, 0.0F, 1.0F, 0.0F, 0.0F);
        poseStack.popPose();
    }

    private static void vertex(VertexConsumer consumer, PoseStack.Pose pose, int packedLight, float x, float y, float u, float v) {
        RandomSource random = RandomSource.create();
        int r = random.nextInt(256);
        int g = random.nextInt(256);
        int b = random.nextInt(256);
        consumer.vertex(pose, x - 0.5F, y - 0.25F, 0.0F).color(r, g, b, 255)
                .uv(u, v).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight)
                .normal(pose, 0.0F, 1.0F, 0.0F).endVertex();
    }

    @Override
    public ResourceLocation getTextureLocation(MusicalNote entity) {
        return TEXTURE_LOCATION;
    }
    
}