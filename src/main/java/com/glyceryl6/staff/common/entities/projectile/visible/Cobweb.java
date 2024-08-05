package com.glyceryl6.staff.common.entities.projectile.visible;

import com.glyceryl6.staff.component.Staffs;
import com.glyceryl6.staff.registry.KODataComponents;
import com.glyceryl6.staff.registry.KOEntityTypes;
import com.glyceryl6.staff.utils.StaffUniversalUtils;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public class Cobweb extends AbstractThrownItem {

    public Cobweb(EntityType<? extends AbstractThrownItem> type, Level level) {
        super(type, level);
    }

    public Cobweb(LivingEntity shooter, Vec3 movement, Level level) {
        super(KOEntityTypes.COBWEB.get(), shooter, movement, level);
    }

    @Override
    protected boolean canHitEntity(Entity entity) {
        return true;
    }

    @Override
    protected boolean shouldBurn() {
        return false;
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        if (result.getEntity() instanceof LivingEntity entity && !this.level().isClientSide) {
            ItemStack itemInHand = entity.getItemInHand(entity.getUsedItemHand());
            Staffs staffs = itemInHand.get(KODataComponents.STAFFS.get());
            entity.hurt(this.damageSources().thrown((this), this.getOwner()), 1.0F);
            this.level().setBlockAndUpdate(entity.blockPosition(), Blocks.COBWEB.defaultBlockState());
            if (staffs != null && StaffUniversalUtils.getCoreBlockState(itemInHand).is(Blocks.COBWEB)) {
                Staffs newStaffs = new Staffs(Boolean.FALSE, staffs.continuousMode(), staffs.note());
                itemInHand.set(KODataComponents.STAFFS.get(), newStaffs);
            }
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        Level level = this.level();
        if (!level.isClientSide) {
            level.setBlockAndUpdate(this.blockPosition(), Blocks.COBWEB.defaultBlockState());
        }
    }

}