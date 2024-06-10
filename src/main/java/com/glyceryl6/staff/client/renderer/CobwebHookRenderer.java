package com.glyceryl6.staff.client.renderer;

import com.glyceryl6.staff.common.entities.CobwebHook;
import com.glyceryl6.staff.registry.ModItems;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Matrix4f;

@OnlyIn(Dist.CLIENT)
public class CobwebHookRenderer extends EntityRenderer<CobwebHook> {

    private static final ResourceLocation GUARDIAN_BEAM_LOCATION = new ResourceLocation("textures/entity/guardian_beam.png");

    public CobwebHookRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(CobwebHook entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        Player player = entity.getPlayer();
        if (player != null) {
            poseStack.pushPose();
            double x = Mth.lerp(partialTick, entity.xo, entity.getX());
            double y = Mth.lerp(partialTick, entity.yo, entity.getY()) + (double)entity.getEyeHeight();
            double z = Mth.lerp(partialTick, entity.zo, entity.getZ());
            Vec3 vec3d = this.getPlayerHandPos(player, entityYaw, partialTick, ModItems.STAFF.get());
            Vec3 vec3d2 = new Vec3(x, y, z);
            float h = (float)entity.tickCount + partialTick;
            float j = h * 0.15f % 1.0f;
            Vec3 vec3d3 = vec3d.subtract(vec3d2);
            float k = (float)(vec3d3.length() + 0.1f);
            vec3d3 = vec3d3.normalize();
            float l = (float)Math.acos(vec3d3.y);
            float m = (float)Math.atan2(vec3d3.z, vec3d3.x);
            poseStack.mulPose(Axis.YP.rotationDegrees((1.5707964f - m) * 57.295776f));
            poseStack.mulPose(Axis.XP.rotationDegrees(l * 57.295776f));
            float n = h * 0.05f * -1.5f;
            float p = Mth.cos(n + (float)Math.PI) * 0.2f;
            float q = Mth.sin(n + (float)Math.PI) * 0.2f;
            float r = Mth.cos(n + 0.0f) * 0.2f;
            float s = Mth.sin(n + 0.0f) * 0.2f;
            float t = Mth.cos(n + 1.5707964f) * 0.2f;
            float u = Mth.sin(n + 1.5707964f) * 0.2f;
            float v = Mth.cos(n + 4.712389f) * 0.2f;
            float w = Mth.sin(n + 4.712389f) * 0.2f;
            float aa = -1.0f + j;
            float ab = k * 2.5f + aa;
            RenderType renderType = RenderType.entityCutoutNoCull(GUARDIAN_BEAM_LOCATION);
            VertexConsumer vertexConsumer = buffer.getBuffer(renderType);
            Matrix4f matrix4f = poseStack.last().pose();
            vertex(vertexConsumer, matrix4f, p, k, q, 0.4999f, ab);
            vertex(vertexConsumer, matrix4f, p, 0.0f, q, 0.4999f, aa);
            vertex(vertexConsumer, matrix4f, r, 0.0f, s, 0.0f, aa);
            vertex(vertexConsumer, matrix4f, r, k, s, 0.0f, ab);
            vertex(vertexConsumer, matrix4f, t, k, u, 0.4999f, ab);
            vertex(vertexConsumer, matrix4f, t, 0.0f, u, 0.4999f, aa);
            vertex(vertexConsumer, matrix4f, v, 0.0f, w, 0.0f, aa);
            vertex(vertexConsumer, matrix4f, v, k, w, 0.0f, ab);
            poseStack.popPose();
        }
    }

    private static void vertex(VertexConsumer vertexConsumer, Matrix4f pose, float x, float y, float z, float u, float v) {
        vertexConsumer.vertex(pose, x, y, z).color(255, 255, 255, 255).uv(u, v)
                .overlayCoords(OverlayTexture.NO_OVERLAY).uv2((15728880)).normal(0.0F, 1.0F, 0.0F).endVertex();
    }

    public Vec3 getPlayerHandPos(Player player, float entityYaw, float partialTick, Item item) {
        Options options = this.entityRenderDispatcher.options;
        int i = player.getMainArm() == HumanoidArm.RIGHT ? 1 : -1;
        if (!player.getMainHandItem().is(item)) {
            i = -i;
        }

        if (options.getCameraType().isFirstPerson() && player == Minecraft.getInstance().player) {
            double d4 = 960.0 / (double) options.fov().get();
            Vec3 vec3 = this.entityRenderDispatcher.camera.getNearPlane()
                    .getPointOnPlane(((float)i * 0.525F), -0.1F).scale(d4)
                    .yRot(entityYaw * 0.5F).xRot(-entityYaw * 0.7F);
            return player.getEyePosition(partialTick).add(vec3);
        } else {
            float f = Mth.lerp(partialTick, player.yBodyRotO, player.yBodyRot) * (float) (Math.PI / 180.0D);
            double d0 = Mth.sin(f);
            double d1 = Mth.cos(f);
            double f1 = player.getScale();
            double d2 = (double)i * 0.35D * f1;
            double d3 = 0.8 * f1;
            double f2 = player.isCrouching() ? -0.1875D : 0.0D;
            return player.getEyePosition(partialTick).add(-d1 * d2 - d0 * d3, f2 - 0.45D * f1, -d0 * d2 + d1 * d3);
        }
    }

    @Override
    public ResourceLocation getTextureLocation(CobwebHook entity) {
        return InventoryMenu.BLOCK_ATLAS;
    }

}