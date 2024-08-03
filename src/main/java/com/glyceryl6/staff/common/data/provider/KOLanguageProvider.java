package com.glyceryl6.staff.common.data.provider;

import com.glyceryl6.staff.Main;
import com.glyceryl6.staff.registry.KOBlocks;
import com.glyceryl6.staff.registry.KOEntityTypes;
import com.glyceryl6.staff.registry.KOItems;
import com.glyceryl6.staff.registry.KOKeyMappings;
import com.google.gson.JsonObject;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.LanguageProvider;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class KOLanguageProvider extends LanguageProvider {

    private final Map<String, String> enData = new TreeMap<>();
    private final Map<String, String> cnData = new TreeMap<>();
    private final PackOutput output;
    private final String locale;

    public KOLanguageProvider(PackOutput output, String locale) {
        super(output, Main.MOD_ID, locale);
        this.output = output;
        this.locale = locale;
    }

    @Override
    protected void addTranslations() {
        this.addBlock(KOBlocks.SIGNAL_BLOCK, "Signal Block", "信号方块");
        this.addItem(KOItems.STAFF, "Staff with %s", "%s权杖");
        this.addItem(KOItems.BEEPER_SPAWN_EGG, "Beeper Spawn Egg", "苦力蜂刷怪蛋");
        this.addEntityType(KOEntityTypes.STAFF_TNT, "Staff TNT", "权杖TNT");
        this.addEntityType(KOEntityTypes.STAFF_WITHER_SKULL, "Staff Wither Skull", "权杖凋灵之首");
        this.addEntityType(KOEntityTypes.STAFF_FIREBALL, "Staff Fireball", "权杖火球");
        this.addEntityType(KOEntityTypes.BONE_MEAL, "Bone Meal", "骨粉");
        this.addEntityType(KOEntityTypes.ICE_BOMB, "Ice Bomb", "冰弹");
        this.addEntityType(KOEntityTypes.MUSICAL_NOTE, "Musical Note", "音符");
        this.addEntityType(KOEntityTypes.BEEPER, "Beeper", "苦力蜂");
        this.add("itemGroup." + Main.MOD_ID, "§6§lStaff of The King Orange", "§6§l橙王的权杖");
        this.add(KOKeyMappings.CATEGORY_STAFF, "§6§lStaff of The King Orange", "§6§l橙王的权杖");
        this.add(KOKeyMappings.ADD_REMOVE_KEYBINDING.getName(), "Add/Remove block", "添加/移除方块");
        this.add(KOKeyMappings.RANDOM_CHANGE_KEYBINDING.getName(), "Random change block", "随机切换方块");
        this.add(KOKeyMappings.CONTINUOUS_MODE_KEYBINDING.getName(), "Switch Continuous Mode", "切换连续模式");
        this.addMessages("normal_block_change", "The core block is change to: %s", "核心方块已更改为：%s");
        this.addMessages("player_head_change", "The core block is change to: %s's head", "核心方块已更改为：%s的头");
        this.addTooltips("continuous_mode", "§bContinuous Mode: ", "§b连续模式：");
        this.addTooltips("continuous_mode.true", "§dON", "§d开");
        this.addTooltips("continuous_mode.false", "§dOFF", "§d关");
        this.addTooltips("core_block", "§aCore Block: §r§6%s", "§a核心方块：§6§r%s");
    }

    @Override
    public @NotNull CompletableFuture<?> run(@NotNull CachedOutput cache) {
        this.addTranslations();
        Path path = this.output.getOutputFolder(PackOutput.Target.RESOURCE_PACK)
                .resolve(Main.MOD_ID).resolve("lang");
        if (this.locale.equals("en_us") && !this.enData.isEmpty()) {
            return this.save(this.enData, cache, path.resolve("en_us.json"));
        }

        if (this.locale.equals("zh_cn") && !this.cnData.isEmpty()) {
            return this.save(this.cnData, cache, path.resolve("zh_cn.json"));
        }

        return CompletableFuture.allOf();
    }

    private CompletableFuture<?> save(Map<String, String> data, CachedOutput cache, Path target) {
        JsonObject json = new JsonObject();
        data.forEach(json::addProperty);
        return DataProvider.saveStable(cache, json, target);
    }

    private void addBlock(Supplier<? extends Block> key, String en, String cn) {
        this.add(key.get().getDescriptionId(), en, cn);
    }

    private void addItem(Supplier<? extends Item> key, String en, String cn) {
        this.add(key.get().getDescriptionId(), en, cn);
    }

    private void addEntityType(Supplier<? extends EntityType<?>> key, String en, String cn) {
        this.add(key.get().getDescriptionId(), en, cn);
    }

    private void addMessages(String key, String en, String cn) {
        this.add("message.staff." + key, en, cn);
    }

    private void addTooltips(Supplier<Item> key, String en, String cn) {
        this.add("tooltip." + key.get().getDescriptionId(), en, cn);
    }

    private void addTooltips(String key, String en, String cn) {
        this.add("tooltip.staff." + key, en, cn);
    }

    private void add(String key, String en, String cn) {
        if (this.locale.equals("en_us") && !this.enData.containsKey(key)) {
            this.enData.put(key, en);
        } else if (this.locale.equals("zh_cn") && !this.cnData.containsKey(key)) {
            this.cnData.put(key, cn);
        }
    }

}