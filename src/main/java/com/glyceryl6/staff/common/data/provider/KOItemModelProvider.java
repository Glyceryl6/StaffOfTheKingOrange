package com.glyceryl6.staff.common.data.provider;

import com.glyceryl6.staff.Main;
import com.glyceryl6.staff.utils.KOCommonUtils;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class KOItemModelProvider extends ItemModelProvider {

    public KOItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Main.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        for (Item item : KOCommonUtils.getKnownItems()) {
            ResourceLocation key = BuiltInRegistries.ITEM.getKey(item);
            if (item instanceof DeferredSpawnEggItem) {
                this.withExistingParent(key.getPath(), this.mcLoc("item/template_spawn_egg"));
            }
        }
    }

}