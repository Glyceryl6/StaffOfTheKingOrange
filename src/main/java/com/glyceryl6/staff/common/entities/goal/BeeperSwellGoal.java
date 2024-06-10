package com.glyceryl6.staff.common.entities.goal;

import com.glyceryl6.staff.common.entities.Beeper;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class BeeperSwellGoal extends Goal {

    private final Beeper beeper;
    @Nullable
    private LivingEntity target;

    public BeeperSwellGoal(Beeper beeper) {
        this.beeper = beeper;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        LivingEntity livingentity = this.beeper.getTarget();
        return this.beeper.getSwellDir() > 0 || livingentity != null && this.beeper.distanceToSqr(livingentity) < 9.0;
    }

    @Override
    public void start() {
        this.beeper.getNavigation().stop();
        this.target = this.beeper.getTarget();
    }

    @Override
    public void stop() {
        this.target = null;
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        if (this.target == null) {
            this.beeper.setSwellDir(-1);
        } else if (this.beeper.distanceToSqr(this.target) > 49.0) {
            this.beeper.setSwellDir(-1);
        } else if (!this.beeper.getSensing().hasLineOfSight(this.target)) {
            this.beeper.setSwellDir(-1);
        } else {
            this.beeper.setSwellDir(1);
        }
    }

}