package com.glyceryl6.staff.client.renderer;

import com.glyceryl6.staff.common.entities.FakeBlock;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.client.model.lighting.ForgeModelBlockRenderer;

@OnlyIn(Dist.CLIENT)
public class FakeBlockRenderer extends EntityRenderer<FakeBlock> {

    private final BlockColors blockColors;
    private final BlockModelShaper blockModelShaper;
    private final ModelBlockRenderer modelRenderer;

    public FakeBlockRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.blockColors = Minecraft.getInstance().getBlockColors();
        this.blockModelShaper = context.getModelManager().getBlockModelShaper();
        this.modelRenderer = new ForgeModelBlockRenderer(this.blockColors);
    }

    @Override
    public void render(FakeBlock entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();
        poseStack.translate(-0.5F, 0.0F, -0.5F);
        RenderType renderType = RenderType.entityCutoutNoCull(this.getTextureLocation(entity));
        this.renderSingleBlock(entity.getBlockState(), poseStack, buffer, packedLight, renderType);
        poseStack.popPose();
    }

    private void renderSingleBlock(BlockState state, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, RenderType renderType) {
        VertexConsumer consumer = ItemRenderer.getFoilBufferDirect(bufferSource, renderType, Boolean.TRUE, Boolean.TRUE);
        RenderShape renderShape = state.getRenderShape();
        if (renderShape != RenderShape.INVISIBLE) {
            switch (renderShape) {
                case MODEL -> {
                    BakedModel blockModel = this.blockModelShaper.getBlockModel(state);
                    int i = this.blockColors.getColor(state, null, null, 0);
                    float r = (float) (i >> 16 & 0xFF) / 255.0F;
                    float g = (float) (i >> 8 & 0xFF) / 255.0F;
                    float b = (float) (i & 0xFF) / 255.0F;
                    for (var rt : blockModel.getRenderTypes(state, RandomSource.create(42), ModelData.EMPTY)) {
                        this.modelRenderer.renderModel(poseStack.last(), consumer, state, blockModel,
                                r, g, b, packedLight, OverlayTexture.NO_OVERLAY, ModelData.EMPTY, rt);
                    }
                }

                case ENTITYBLOCK_ANIMATED -> {
                    ItemStack stack = new ItemStack(state.getBlock());
                    BlockEntityWithoutLevelRenderer renderer = IClientItemExtensions.of(stack).getCustomRenderer();
                    renderer.renderByItem(stack, ItemDisplayContext.NONE, poseStack, bufferSource, packedLight, OverlayTexture.NO_OVERLAY);
                }
            }
        }
    }

    @Override
    public ResourceLocation getTextureLocation(FakeBlock entity) {
        return InventoryMenu.BLOCK_ATLAS;
    }

}