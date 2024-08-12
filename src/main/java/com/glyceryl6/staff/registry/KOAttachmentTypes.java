package com.glyceryl6.staff.registry;

import com.glyceryl6.staff.Main;
import com.mojang.serialization.Codec;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class KOAttachmentTypes {

    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, Main.MOD_ID);
    public static final DeferredHolder<AttachmentType<?>, AttachmentType<Boolean>> ENCHANTMENT_GLINT_OVERLAY =
            ATTACHMENT_TYPES.register("enchantment_glint_overlay", () -> AttachmentType.builder(() -> false).serialize(Codec.BOOL).build());

}