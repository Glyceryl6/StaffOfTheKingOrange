package com.glyceryl6.staff.functions.player_head;

import com.glyceryl6.staff.api.IPlayerHeadStaffFunction;
import com.glyceryl6.staff.common.entities.projectile.visible.HerobrineHead;
import com.glyceryl6.staff.utils.StaffConstantUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

public class StaffWithHerobrineHead implements IPlayerHeadStaffFunction {

    @Override
    public boolean enableUseOnBlock() {
        return false;
    }

    @Override
    public void use(Level level, Player player, ItemStack stack) {
        HitResult result = player.pick((32.0D), (0.0F), Boolean.FALSE);
        if (result instanceof BlockHitResult blockHitResult) {
            BlockPos pos = blockHitResult.getBlockPos();
            for (int i = 0; i < 32; i++) {
                double y = player.getY() + level.random.nextDouble() * 2.0D;
                double xs = level.random.nextGaussian();
                double zs = level.random.nextGaussian();
                level.addParticle(ParticleTypes.PORTAL, player.getX(), y, player.getZ(), xs, 0.0D, zs);
            }

            if (!level.isClientSide && player instanceof ServerPlayer) {
                level.playSound(null, player.getX(), player.getY(), player.getZ(),
                        SoundEvents.PLAYER_TELEPORT, SoundSource.PLAYERS);
                player.teleportTo(pos.getX(), pos.getY(), pos.getZ());
                player.resetFallDistance();
            }
        }
    }

    @Override
    public void useTick(Level level, Player player, ItemStack stack) {
        if (!level.isClientSide) {
            String uuid = "9586e5ab-157a-4658-ad80-b07552a9ca63";
            String value = StaffConstantUtils.MHF_HEROBRINE_VALUE;
            String signature = StaffConstantUtils.MHF_HEROBRINE_SIGNATURE;
            HerobrineHead head = new HerobrineHead(level, player, 0.0D, 0.0D, 0.0D);
            head.setPos(player.getRandomX(0.5D), player.getY(0.5D), player.getRandomZ(0.5D));
            head.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.5F, 1.0F);
            head.setProfiles(uuid, "MHF_Herobrine", value, signature);
            level.levelEvent(1024, head.blockPosition(), 0);
            level.addFreshEntity(head);
        }
    }

    @Override
    public void attackEntity(Level level, Player player, Entity target) {
        if (!level.isClientSide && target instanceof LivingEntity entity) {
            entity.kill();
        }
    }

}