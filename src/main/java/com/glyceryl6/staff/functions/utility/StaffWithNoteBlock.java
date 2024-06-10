package com.glyceryl6.staff.functions.utility;

import com.glyceryl6.staff.api.INormalStaffFunction;
import com.glyceryl6.staff.common.entities.projectile.visible.MusicalNote;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.NoteBlock;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;

public class StaffWithNoteBlock implements INormalStaffFunction {

    @Override
    public boolean enableUseOnBlock() {
        return false;
    }

    @Override
    public void use(Level level, Player player, ItemStack stack) {

    }

    @Override
    public void useTick(Level level, Player player, ItemStack stack) {
        int noteFromTag = stack.getOrCreateTag().getInt("Note");
        NoteBlockInstrument[] values = NoteBlockInstrument.values();
        int note = level.random.nextInt(24);
        float f = NoteBlock.getPitchFromNote(note);
        SoundEvent soundEvent = values[noteFromTag].getSoundEvent().get();
        level.playSound(null, player.blockPosition(), soundEvent, SoundSource.RECORDS, 3.0F, f);
        if (!level.isClientSide) {
            MusicalNote musicalNote = new MusicalNote(player, 0.0D, 0.0D, 0.0D, level);
            musicalNote.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.2F, 1.0F);
            musicalNote.setPos(player.getRandomX(0.5D), player.getY(0.5D), player.getRandomZ(0.5D));
            musicalNote.setNote(note);
            level.addFreshEntity(musicalNote);
        }
    }

}