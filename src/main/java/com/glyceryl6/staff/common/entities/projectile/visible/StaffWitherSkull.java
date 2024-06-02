package com.glyceryl6.staff.common.entities.projectile.visible;

import com.glyceryl6.staff.registry.ModEntityTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.WitherSkull;
import net.minecraft.world.level.Level;

public class StaffWitherSkull extends WitherSkull {

    public StaffWitherSkull(EntityType<? extends StaffWitherSkull> type, Level level) {
        super(type, level);
    }

    public StaffWitherSkull(Level level, LivingEntity shooter, double offsetX, double offsetY, double offsetZ) {
        super(level, shooter, offsetX, offsetY, offsetZ);
    }

    @Override
    public EntityType<?> getType() {
        return ModEntityTypes.STAFF_WITHER_SKULL.get();
    }

    @Override
    protected float getInertia() {
        return 1.0F;
    }

    @Override
    protected float getLiquidInertia() {
        return this.getInertia();
    }

}