package com.glyceryl6.staff.client.renderer;

import com.glyceryl6.staff.Main;
import com.glyceryl6.staff.common.entities.projectile.visible.AbstractPlayerHead;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.SkullModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.SkullBlockRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SkullRenderer extends EntityRenderer<AbstractPlayerHead> {

    private final SkullModel model;

    public SkullRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new SkullModel(context.bakeLayer(Main.PLAYER_HEAD_LAYER));
    }

    @Override
    public void render(AbstractPlayerHead entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();
        poseStack.scale(-1.0F, -1.0F, 1.0F);
        float f = Mth.rotLerp(partialTicks, entity.yRotO, entity.getYRot());
        float f1 = Mth.lerp(partialTicks, entity.xRotO, entity.getXRot());
        RenderType renderType = SkullBlockRenderer.getRenderType(SkullBlock.Types.PLAYER, entity.getProfile());
        VertexConsumer consumer = buffer.getBuffer(renderType);
        this.model.setupAnim((0.0F), f, f1);
        this.model.renderToBuffer(
                poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY,
                1.0F, 1.0F, 1.0F, 1.0F);
        poseStack.popPose();
    }

    @Override
    protected int getBlockLightLevel(AbstractPlayerHead entity, BlockPos pos) {
        return 15;
    }

    @Override
    public ResourceLocation getTextureLocation(AbstractPlayerHead entity) {
        return DefaultPlayerSkin.getDefaultSkin();
    }

}