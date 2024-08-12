package com.glyceryl6.staff.client.renderer;

import com.glyceryl6.staff.common.entities.projectile.visible.ThrowableBlock;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;

/** @noinspection deprecation*/
public class ThrowableBlockRenderer extends EntityRenderer<ThrowableBlock> {

    private final BlockRenderDispatcher blockRenderer;

    public ThrowableBlockRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.blockRenderer = context.getBlockRenderDispatcher();
        this.shadowRadius = 0.6F;
    }

    @Override
    public void render(ThrowableBlock entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();
        this.blockRenderer.renderSingleBlock(entity.getBlockState(), poseStack, bufferSource, packedLight, OverlayTexture.NO_OVERLAY);
        poseStack.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(ThrowableBlock entity) {
        return InventoryMenu.BLOCK_ATLAS;
    }
    
}