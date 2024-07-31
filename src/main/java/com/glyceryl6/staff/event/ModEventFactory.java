package com.glyceryl6.staff.event;

import com.glyceryl6.staff.api.INormalStaffFunction;
import com.glyceryl6.staff.api.IPlayerHeadStaffFunction;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.NeoForge;

import java.util.Map;

public class ModEventFactory {

    public static void onRegisterNormalStaffFunction(Map<Block, INormalStaffFunction> staffFunctionMap) {
        NeoForge.EVENT_BUS.post(new RegisterNormalStaffFunctionEvent(staffFunctionMap));
    }

    public static void onRegisterPlayerHeadStaffFunction(Map<String, IPlayerHeadStaffFunction> staffFunctionMap) {
        NeoForge.EVENT_BUS.post(new RegisterPlayerHeadStaffFunctionEvent(staffFunctionMap));
    }

    public static boolean onRegisterStaffCoreBlockRender(BlockState state, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        return NeoForge.EVENT_BUS.post(new RegisterStaffCoreBlockRenderEvent(state, poseStack, buffer, packedLight, packedOverlay)).isCanceled();
    }

}