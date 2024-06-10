package com.glyceryl6.staff.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class StaffModel extends Model {

    private final ModelPart main;

    public StaffModel(ModelPart root) {
        super(RenderType::entitySolid);
        this.main = root.getChild("main");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshDefinition = new MeshDefinition();
        PartDefinition partDefinition = meshDefinition.getRoot();
        partDefinition.addOrReplaceChild("main", CubeListBuilder.create().texOffs(17, 30).addBox(-11.0F, -68.0F, 7.0F, 4.0F, 2.0F, 11.0F, new CubeDeformation(0.0F))
                .texOffs(17, 45).addBox(-11.0F, -52.0F, 7.0F, 4.0F, 2.0F, 11.0F, new CubeDeformation(0.0F))
                .texOffs(17, 2).mirror().addBox(-11.0F, -72.0F, 7.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(17, 12).addBox(-11.0F, -66.0F, 16.0F, 4.0F, 14.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 2).mirror().addBox(-11.0F, -50.0F, 7.0F, 4.0F, 58.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false),
                PartPose.offset(8.0F, 24.0F, -8.0F));
        return LayerDefinition.create(meshDefinition, 64, 64);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.main.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }

}