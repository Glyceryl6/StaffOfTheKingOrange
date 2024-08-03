package com.glyceryl6.staff.common.data;

import com.glyceryl6.staff.Main;
import com.glyceryl6.staff.common.data.provider.KOItemModelProvider;
import com.glyceryl6.staff.common.data.provider.KOLanguageProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = Main.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class KODataGenerator {

    @SubscribeEvent
    public static void onGatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        generator.addProvider(event.includeServer(), new KOItemModelProvider(packOutput, existingFileHelper));
        generator.addProvider(event.includeServer(), new KOLanguageProvider(packOutput, "en_us"));
        generator.addProvider(event.includeServer(), new KOLanguageProvider(packOutput, "zh_cn"));
    }

}