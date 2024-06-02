package com.glyceryl6.staff.common.entities.projectile.visible;

import com.glyceryl6.staff.common.entities.projectile.invisible.AbstractInvisibleProjectile;
import com.glyceryl6.staff.registry.ModEntityTypes;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.NoteBlock;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.Nullable;

public class MusicalNote extends AbstractInvisibleProjectile {

    private static final EntityDataAccessor<Integer> NOTE = SynchedEntityData.defineId(MusicalNote.class, EntityDataSerializers.INT);

    public MusicalNote(EntityType<? extends MusicalNote> type, Level level) {
        super(type, level);
    }

    public MusicalNote(LivingEntity shooter, double offsetX, double offsetY, double offsetZ, Level level) {
        super(ModEntityTypes.MUSICAL_NOTE.get(), shooter, offsetX, offsetY, offsetZ, level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(NOTE, 0);
    }

    public int getNote() {
        return this.entityData.get(NOTE) / 24;
    }

    public void setNote(int note) {
        this.entityData.set(NOTE, Mth.clamp(note, 0, 24));
    }

    @Nullable @Override
    protected ParticleOptions getTrailParticle() {
        return null;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("id", this.getNote());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setNote(compound.getInt("id"));
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        SoundEvent soundEvent = NoteBlockInstrument.values()[this.getNote()].getSoundEvent().get();
        this.playSound(soundEvent, 3.0F, NoteBlock.getPitchFromNote(this.getNote()));
        if (!this.level().isClientSide && result.getEntity() instanceof LivingEntity livingEntity) {
            livingEntity.hurt(this.damageSources().thrown((this), this.getOwner()), 3.0F);
            livingEntity.invulnerableTime = 0;
        }
    }

}