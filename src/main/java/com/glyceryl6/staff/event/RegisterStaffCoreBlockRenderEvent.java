package com.glyceryl6.staff.event;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

@Cancelable
public class RegisterStaffCoreBlockRenderEvent extends Event {

    private BlockState state;
    private final PoseStack poseStack;
    private final MultiBufferSource buffer;
    private final int packedLight;
    private final int packedOverlay;

    public RegisterStaffCoreBlockRenderEvent(
            BlockState state, PoseStack poseStack,
            MultiBufferSource buffer,
            int packedLight, int packedOverlay) {
        this.state = state;
        this.poseStack = poseStack;
        this.buffer = buffer;
        this.packedLight = packedLight;
        this.packedOverlay = packedOverlay;
    }

    public BlockState getState() {
        return this.state;
    }

    public void setState(BlockState state) {
        this.state = state;
    }

    public PoseStack getPoseStack() {
        return this.poseStack;
    }

    public MultiBufferSource getBuffer() {
        return this.buffer;
    }

    public int getPackedLight() {
        return this.packedLight;
    }

    public int getPackedOverlay() {
        return this.packedOverlay;
    }

}