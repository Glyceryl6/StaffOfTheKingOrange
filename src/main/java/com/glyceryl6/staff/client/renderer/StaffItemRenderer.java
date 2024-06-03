package com.glyceryl6.staff.client.renderer;

import com.glyceryl6.staff.Main;
import com.glyceryl6.staff.client.model.StaffModel;
import com.glyceryl6.staff.common.items.StaffItem;
import com.glyceryl6.staff.event.ModEventFactory;
import com.glyceryl6.staff.utils.StaffUniversalUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.SkullModelBase;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BellRenderer;
import net.minecraft.client.renderer.blockentity.SkullBlockRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ResolvableProfile;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.DecoratedPotPatterns;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.RotationSegment;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Matrix4f;

import java.util.Map;
import java.util.Objects;

/** @noinspection deprecation*/
@OnlyIn(Dist.CLIENT)
public class StaffItemRenderer extends BlockEntityWithoutLevelRenderer {

    public static final ResourceLocation TEXTURE = Main.prefix("textures/entity/staff.png");
    private final Material baseMaterial = Objects.requireNonNull(Sheets.getDecoratedPotMaterial(DecoratedPotPatterns.BASE));
    private final BlockRenderDispatcher blockRenderer = Minecraft.getInstance().getBlockRenderer();
    private final Map<SkullBlock.Type, SkullModelBase> skullModelByType;
    private final StaffModel staffModel;
    private final ModelPart bellBody;
    private final ModelPart neck;
    private final ModelPart top;
    private final ModelPart bottom;
    private final ModelPart frontSide;
    private final ModelPart backSide;
    private final ModelPart leftSide;
    private final ModelPart rightSide;

    public StaffItemRenderer(EntityModelSet entityModelSet) {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), entityModelSet);
        ModelPart decoratedPotBase = entityModelSet.bakeLayer(ModelLayers.DECORATED_POT_BASE);
        ModelPart decoratedPotSide = entityModelSet.bakeLayer(ModelLayers.DECORATED_POT_SIDES);
        this.skullModelByType = SkullBlockRenderer.createSkullRenderers(entityModelSet);
        this.staffModel = new StaffModel(entityModelSet.bakeLayer(Main.STAFF_LAYER));
        this.bellBody = entityModelSet.bakeLayer(ModelLayers.BELL);
        this.neck = decoratedPotBase.getChild("neck");
        this.top = decoratedPotBase.getChild("top");
        this.bottom = decoratedPotBase.getChild("bottom");
        this.frontSide = decoratedPotSide.getChild("front");
        this.backSide = decoratedPotSide.getChild("back");
        this.leftSide = decoratedPotSide.getChild("left");
        this.rightSide = decoratedPotSide.getChild("right");
    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext displayContext, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        if (stack.getItem() instanceof StaffItem) {
            poseStack.pushPose();
            poseStack.mulPose(Axis.XP.rotationDegrees(180.0F));
            poseStack.mulPose(Axis.YP.rotationDegrees(-180.0F));
            poseStack.translate(-0.5F, 0.0F, 0.5F);
            poseStack.scale(0.6875F, 0.6875F, 0.6875F);
            VertexConsumer consumer = ItemRenderer.getFoilBufferDirect(buffer, this.staffModel.renderType(TEXTURE), Boolean.FALSE, stack.hasFoil());
            this.staffModel.renderToBuffer(poseStack, consumer, packedLight, packedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);
            poseStack.popPose();
            BlockState state = StaffUniversalUtils.getCoreBlockState(stack);
            if (ModEventFactory.onRegisterStaffCoreBlockRender(state, poseStack, buffer, packedLight, packedOverlay)) {
                return;
            }

            if (player != null) {
                boolean flag = player.getTicksUsingItem() > 5;
                if (state.getBlock() instanceof AbstractFurnaceBlock) {
                    BooleanProperty lit = AbstractFurnaceBlock.LIT;
                    DirectionProperty facing = AbstractFurnaceBlock.FACING;
                    state = state.setValue(lit, flag).setValue(facing, Direction.WEST);
                } else if (state.is(Blocks.BREWING_STAND)) {
                    for (BooleanProperty property : BrewingStandBlock.HAS_BOTTLE) {
                        state = state.setValue(property, flag);
                    }
                } else if (state.is(Blocks.REDSTONE_LAMP)) {
                    state = state.setValue(RedstoneLampBlock.LIT, flag);
                } else if (state.is(Blocks.SCULK_CATALYST)) {
                    state = state.setValue(SculkCatalystBlock.PULSE, flag);
                }
            }

            if (state.getBlock() instanceof AbstractSkullBlock skullBlock) {
                SkullBlock.Type type = skullBlock.getType();
                float f = RotationSegment.convertToDegrees(12);
                SkullModelBase skullModelBase = this.skullModelByType.get(type);
                DataComponentType<ResolvableProfile> profileType = DataComponents.PROFILE;
                ResolvableProfile profile = stack.get(profileType);
                if (profile != null && !profile.isResolved()) {
                    stack.remove(profileType);
                    profile.resolve().thenAcceptAsync(p ->
                            stack.set(profileType, p), mc);
                    profile = null;
                }

                poseStack.pushPose();
                poseStack.scale(1.2F, 1.2F, 1.2F);
                poseStack.translate(-0.04F, 1.0F, -0.04F);
                RenderType renderType = SkullBlockRenderer.getRenderType(type, profile);
                SkullBlockRenderer.renderSkull((null), f, (0.0F), poseStack,
                        buffer, packedLight, skullModelBase, renderType);
                poseStack.popPose();
            } else {
                poseStack.pushPose();
                poseStack.scale(0.6F, 0.6F, 0.6F);
                poseStack.translate(0.4F, 2.0F, 0.4F);
                this.blockRenderer.renderSingleBlock(state, poseStack, buffer, packedLight, packedOverlay);
                if (state.getBlock() instanceof BellBlock) {
                    VertexConsumer vc = BellRenderer.BELL_RESOURCE_LOCATION.buffer(buffer, RenderType::entitySolid);
                    this.bellBody.render(poseStack, vc, packedLight, packedOverlay);
                } else if (state.getBlock() instanceof DecoratedPotBlock) {
                    VertexConsumer vc = this.baseMaterial.buffer(buffer, RenderType::entitySolid);
                    this.neck.render(poseStack, vc, packedLight, packedOverlay);
                    this.top.render(poseStack, vc, packedLight, packedOverlay);
                    this.bottom.render(poseStack, vc, packedLight, packedOverlay);
                    this.renderSide(this.frontSide, poseStack, buffer, packedLight);
                    this.renderSide(this.backSide, poseStack, buffer, packedLight);
                    this.renderSide(this.leftSide, poseStack, buffer, packedLight);
                    this.renderSide(this.rightSide, poseStack, buffer, packedLight);
                } else if (state.is(Blocks.END_GATEWAY)) {
                    Matrix4f pose = poseStack.last().pose();
                    VertexConsumer vc = buffer.getBuffer(RenderType.endGateway());
                    this.renderFace(pose, vc, 0.0F, 1.0F, 0.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
                    this.renderFace(pose, vc, 0.0F, 1.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
                    this.renderFace(pose, vc, 1.0F, 1.0F, 1.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.0F);
                    this.renderFace(pose, vc, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 1.0F, 1.0F, 0.0F);
                    this.renderFace(pose, vc, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 1.0F);
                    this.renderFace(pose, vc, 0.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 0.0F, 0.0F);
                }

                poseStack.popPose();
            }
        }
    }

    private void renderSide(ModelPart modelPart, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        Material material = Sheets.getDecoratedPotMaterial(DecoratedPotPatterns.getResourceKey(Items.BRICK));
        if (material != null) {
            modelPart.render(poseStack, material.buffer(buffer, RenderType::entitySolid), packedLight, OverlayTexture.NO_OVERLAY);
        }
    }
    
    private void renderFace(
            Matrix4f pose, VertexConsumer consumer,
            float x0, float x1, float y0, float y1,
            float z0, float z1, float z2, float z3) {
        consumer.vertex(pose, x0, y0, z0).endVertex();
        consumer.vertex(pose, x1, y0, z1).endVertex();
        consumer.vertex(pose, x1, y1, z2).endVertex();
        consumer.vertex(pose, x0, y1, z3).endVertex();
    }

}