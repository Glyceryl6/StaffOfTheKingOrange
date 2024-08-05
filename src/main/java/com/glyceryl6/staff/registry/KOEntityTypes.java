package com.glyceryl6.staff.registry;

import com.glyceryl6.staff.Main;
import com.glyceryl6.staff.common.entities.*;
import com.glyceryl6.staff.common.entities.projectile.invisible.*;
import com.glyceryl6.staff.common.entities.projectile.visible.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class KOEntityTypes {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(Registries.ENTITY_TYPE, Main.MOD_ID);
    public static final DeferredHolder<EntityType<?>, EntityType<StaffTnt>> STAFF_TNT = ENTITY_TYPES.register("staff_tnt",
            () -> EntityType.Builder.<StaffTnt>of(StaffTnt::new, MobCategory.MISC).fireImmune().sized(0.98F, 0.98F)
                    .eyeHeight(0.15F).clientTrackingRange((10)).updateInterval((10)).build("tnt_from_stuff"));
    public static final DeferredHolder<EntityType<?>, EntityType<StaffWitherSkull>> STAFF_WITHER_SKULL = ENTITY_TYPES.register("staff_wither_skull",
            () -> EntityType.Builder.<StaffWitherSkull>of(StaffWitherSkull::new, MobCategory.MISC).sized(0.3125F, 0.3125F)
                    .clientTrackingRange((10)).updateInterval((10)).build("staff_wither_skull"));
    public static final DeferredHolder<EntityType<?>, EntityType<StaffFireball>> STAFF_FIREBALL = ENTITY_TYPES.register("staff_fireball",
            () -> EntityType.Builder.<StaffFireball>of(StaffFireball::new, MobCategory.MISC).sized(0.25F, 0.25F)
                    .clientTrackingRange((10)).updateInterval((10)).build("staff_fireball"));
    public static final DeferredHolder<EntityType<?>, EntityType<BoneMeal>> BONE_MEAL = ENTITY_TYPES.register("bone_meal",
            () -> EntityType.Builder.<BoneMeal>of(BoneMeal::new, MobCategory.MISC).sized(0.1F, 0.1F)
                    .clientTrackingRange((4)).updateInterval((10)).build("bone_meal"));
    public static final DeferredHolder<EntityType<?>, EntityType<ThrownBookAndPaper>> THROWN_BOOK_AND_PAPER = ENTITY_TYPES.register("thrown_book_and_paper",
            () -> EntityType.Builder.<ThrownBookAndPaper>of(ThrownBookAndPaper::new, MobCategory.MISC).sized(0.25F, 0.25F)
                    .clientTrackingRange((4)).updateInterval((10)).fireImmune().build("thrown_book_and_paper"));
    public static final DeferredHolder<EntityType<?>, EntityType<Enchant>> ENCHANT = ENTITY_TYPES.register("enchant",
            () -> EntityType.Builder.<Enchant>of(Enchant::new, MobCategory.MISC).sized(0.25F, 0.25F)
                    .clientTrackingRange((4)).updateInterval((10)).build("enchant"));
    public static final DeferredHolder<EntityType<?>, EntityType<IceBomb>> ICE_BOMB = ENTITY_TYPES.register("ice_bomb",
            () -> EntityType.Builder.<IceBomb>of(IceBomb::new, MobCategory.MISC).sized(0.25F, 0.25F)
                    .clientTrackingRange((4)).updateInterval((10)).build("ice_bomb"));
    public static final DeferredHolder<EntityType<?>, EntityType<Signal>> SIGNAL = ENTITY_TYPES.register("signal",
            () -> EntityType.Builder.<Signal>of(Signal::new, MobCategory.MISC).sized(0.25F, 0.25F)
                    .clientTrackingRange((4)).updateInterval((10)).build("signal"));
    public static final DeferredHolder<EntityType<?>, EntityType<Smelting>> SMELTING = ENTITY_TYPES.register("smelting",
            () -> EntityType.Builder.<Smelting>of(Smelting::new, MobCategory.MISC).sized(0.25F, 0.25F)
                    .clientTrackingRange((4)).updateInterval((10)).build("smelting"));
    public static final DeferredHolder<EntityType<?>, EntityType<MusicalNote>> MUSICAL_NOTE = ENTITY_TYPES.register("musical_note",
            () -> EntityType.Builder.<MusicalNote>of(MusicalNote::new, MobCategory.MISC).sized(0.25F, 0.25F)
                    .clientTrackingRange((4)).updateInterval((10)).build("musical_note"));
    public static final DeferredHolder<EntityType<?>, EntityType<FakeBlock>> FAKE_BLOCK = ENTITY_TYPES.register("fake_block",
            () -> EntityType.Builder.<FakeBlock>of(FakeBlock::new, MobCategory.MISC).sized(1.0F, 1.0F)
                    .clientTrackingRange((10)).updateInterval((10)).build("fake_block"));
    public static final DeferredHolder<EntityType<?>, EntityType<Beeper>> BEEPER = ENTITY_TYPES.register("beeper",
            () -> EntityType.Builder.of(Beeper::new, MobCategory.CREATURE).sized(0.7F, 0.6F)
                    .eyeHeight(0.3F).clientTrackingRange((8)).build("beeper"));
    public static final DeferredHolder<EntityType<?>, EntityType<HerobrineHead>> HEROBRINE_HEAD = ENTITY_TYPES.register("herobrine_head",
            () -> EntityType.Builder.<HerobrineHead>of(HerobrineHead::new, MobCategory.MISC).sized(0.3125F, 0.3125F)
                    .clientTrackingRange((4)).updateInterval((10)).build("herobrine_head"));
    public static final DeferredHolder<EntityType<?>, EntityType<Cobweb>> COBWEB = ENTITY_TYPES.register("cobweb",
            () -> EntityType.Builder.<Cobweb>of(Cobweb::new, MobCategory.MISC).sized(1.0F, 1.0F)
                    .clientTrackingRange((4)).updateInterval((10)).build("cobweb"));
    public static final DeferredHolder<EntityType<?>, EntityType<CobwebHook>> COBWEB_HOOK = ENTITY_TYPES.register("cobweb_hook",
            () -> EntityType.Builder.<CobwebHook>of(CobwebHook::new, MobCategory.MISC).sized(0.25F, 0.25F)
                    .clientTrackingRange((4)).updateInterval((5)).noSave().noSummon().build("cobweb_hook"));
    public static final DeferredHolder<EntityType<?>, EntityType<PlacedStaff>> PLACED_STAFF = ENTITY_TYPES.register("placed_staff",
            () -> EntityType.Builder.<PlacedStaff>of(PlacedStaff::new, MobCategory.MISC).sized(0.8F, 3.4F)
                    .clientTrackingRange((10)).updateInterval((10)).build("placed_staff"));
    public static final DeferredHolder<EntityType<?>, EntityType<Stalagmite>> STALAGMITE = ENTITY_TYPES.register("stalagmite",
            () -> EntityType.Builder.<Stalagmite>of(Stalagmite::new, MobCategory.MISC).sized(1.0F, 4.0F)
                    .clientTrackingRange((10)).updateInterval((10)).noSave().noSummon().build("stalagmite"));

}