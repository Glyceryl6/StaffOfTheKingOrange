package com.glyceryl6.staff.client.renderer.layers;

import com.glyceryl6.staff.registry.KODataComponents;
import com.glyceryl6.staff.registry.KOItems;
import com.glyceryl6.staff.utils.StaffUniversalUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.mojang.math.Constants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.BundleContents;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/** @noinspection deprecation*/
@OnlyIn(Dist.CLIENT)
public class SurroundingBlockLayer<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {

    private final BlockRenderDispatcher blockRenderer = Minecraft.getInstance().getBlockRenderer();

    public SurroundingBlockLayer(RenderLayerParent<T, M> renderer) {
        super(renderer);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, T livingEntity, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        if (livingEntity instanceof Player player) {
            ItemStack itemInHand = player.getItemInHand(player.getUsedItemHand());
            BlockState coreBlock = StaffUniversalUtils.getCoreBlockState(itemInHand);
            BundleContents bundleContents = itemInHand.get(DataComponents.BUNDLE_CONTENTS);
            CustomData customData = itemInHand.get(KODataComponents.STAFF_SURROUNDING_BLOCK);
            boolean flag1 = bundleContents != null && !bundleContents.isEmpty();
            boolean flag2 = customData != null && customData.copyTag().getBoolean("Show");
            if (itemInHand.is(KOItems.STAFF) && coreBlock.is(Blocks.LAPIS_BLOCK) && flag1 && flag2) {
                float value = (player.tickCount + partialTick) / -5.0F * Constants.RAD_TO_DEG;
                BundleContents.Mutable mutable = new BundleContents.Mutable(bundleContents);
                int count = mutable.items.size();
                for (int i = 0; i < count; i++) {
                    poseStack.pushPose();
                    float degree = value + (i * (360.0F / count));
                    poseStack.mulPose(Axis.YP.rotationDegrees(degree));
                    poseStack.translate(-0.5F, 0.0F, -0.5F);
                    poseStack.translate(0.0F, 0.0F, -0.6F);
                    poseStack.scale(0.5F, 0.5F, 0.5F);
                    if (mutable.items.get(i).getItem() instanceof BlockItem blockItem) {
                        BlockState state = blockItem.getBlock().defaultBlockState();
                        this.blockRenderer.renderSingleBlock(state, poseStack, bufferSource, packedLight, OverlayTexture.NO_OVERLAY);
                    }

                    poseStack.popPose();
                }
            }
        }
    }

}