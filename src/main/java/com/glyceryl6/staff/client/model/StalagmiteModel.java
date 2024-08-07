package com.glyceryl6.staff.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class StalagmiteModel extends Model {

	private final ModelPart main;

	public StalagmiteModel(ModelPart root) {
		super(RenderType::entitySolid);
		this.main = root.getChild("main");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshDefinition = new MeshDefinition();
		PartDefinition partDefinition = meshDefinition.getRoot();
		PartDefinition main = partDefinition.addOrReplaceChild("main", CubeListBuilder.create().texOffs(0, -16)
						.addBox(0.0F, -64.0F, -7.0F, (0.0F), (64.0F), (16.0F), CubeDeformation.NONE),
				PartPose.offset(0.0F, 24.0F, 0.0F));
		main.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, -16)
						.addBox(1.0F, -64.0F, -1.0F, (0.0F), (64.0F), (16.0F), CubeDeformation.NONE),
				PartPose.offsetAndRotation(7.0F, 0.0F, -1.0F, 0.0F, -1.5708F, 0.0F));
		main.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, -16)
						.addBox(1.0F, -64.0F, -1.0F, (0.0F), (64.0F), (16.0F), CubeDeformation.NONE),
				PartPose.offsetAndRotation(-6.0F, 0.0F, -4.0F, 0.0F, 0.7854F, 0.0F));
		main.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(0, -16)
						.addBox(1.0F, -64.0F, -1.0F, (0.0F), (64.0F), (16.0F), CubeDeformation.NONE),
				PartPose.offsetAndRotation(4.0F, 0.0F, -5.0F, 0.0F, -0.7854F, 0.0F));
		return LayerDefinition.create(meshDefinition, 16, 64);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
		this.main.render(poseStack, vertexConsumer, packedLight, packedOverlay);
	}

}