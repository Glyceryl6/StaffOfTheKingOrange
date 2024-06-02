package com.glyceryl6.staff.functions.utility;

import com.glyceryl6.staff.common.entities.projectile.visible.MusicalNote;
import com.glyceryl6.staff.api.INormalStaffFunction;
import com.glyceryl6.staff.component.Staffs;
import com.glyceryl6.staff.registry.ModDataComponents;
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
        Staffs staffs = stack.get(ModDataComponents.STAFFS.get());
        NoteBlockInstrument[] values = NoteBlockInstrument.values();
        if (staffs != null) {
            int note = level.random.nextInt(24);
            float f = NoteBlock.getPitchFromNote(note);
            SoundEvent soundEvent = values[staffs.note()].getSoundEvent().get();
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

}