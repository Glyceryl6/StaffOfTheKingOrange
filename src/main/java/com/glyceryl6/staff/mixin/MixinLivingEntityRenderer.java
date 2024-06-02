package com.glyceryl6.staff.mixin;

import com.glyceryl6.staff.api.IHasEnchantmentGlintEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(LivingEntityRenderer.class)
public abstract class MixinLivingEntityRenderer<T extends LivingEntity, M extends EntityModel<T>> {

    @Shadow protected M model;
    @Shadow protected abstract boolean isBodyVisible(T livingEntity);
    @Shadow protected abstract float getWhiteOverlayProgress(T livingEntity, float partialTicks);
    @Shadow @Nullable protected abstract RenderType getRenderType(T livingEntity, boolean bodyVisible, boolean translucent, boolean glowing);

    @Inject(method = "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
            at = @At(value = "INVOKE", shift = At.Shift.AFTER, target = "Lnet/minecraft/client/model/EntityModel;renderToBuffer(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;IIFFFF)V"))
    public void render(T entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight, CallbackInfo ci) {
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer player = minecraft.player;
        if (entity instanceof IHasEnchantmentGlintEntity glintEntity
                && glintEntity.isGlint() && player != null) {
            boolean flag = this.isBodyVisible(entity);
            boolean flag1 = !flag && !entity.isInvisibleTo(player);
            boolean flag2 = minecraft.shouldEntityAppearGlowing(entity);
            RenderType renderType = this.getRenderType(entity, flag, flag1, flag2);
            if (renderType != null) {
                VertexConsumer consumer = ItemRenderer.getFoilBufferDirect(buffer, renderType, Boolean.TRUE, Boolean.TRUE);
                int i = LivingEntityRenderer.getOverlayCoords(entity, this.getWhiteOverlayProgress(entity, partialTicks));
                this.model.renderToBuffer(poseStack, consumer, packedLight, i, 1.0F, 1.0F, 1.0F, flag1 ? 0.15F : 1.0F);
            }
        }
    }

}