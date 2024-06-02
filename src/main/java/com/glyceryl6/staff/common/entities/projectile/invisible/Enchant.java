package com.glyceryl6.staff.common.entities.projectile.invisible;

import com.glyceryl6.staff.registry.ModEntityTypes;
import com.glyceryl6.staff.registry.ModParticleTypes;
import com.glyceryl6.staff.utils.StaffSpecialUtils;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.Nullable;

public class Enchant extends AbstractInvisibleProjectile {

    public Enchant(EntityType<? extends Enchant> type, Level level) {
        super(type, level);
    }

    public Enchant(LivingEntity shooter, double offsetX, double offsetY, double offsetZ, Level level) {
        super(ModEntityTypes.ENCHANT.get(), shooter, offsetX, offsetY, offsetZ, level);
    }

    @Nullable @Override
    protected ParticleOptions getTrailParticle() {
        return ModParticleTypes.HUGE_ENCHANT.get();
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        Level level = this.level();
        if (!level.isClientSide) {
            StaffSpecialUtils.setRandomEnchantment(level, this.blockPosition());
            this.discard();
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        Entity entity = result.getEntity();
        if (entity instanceof LivingEntity livingEntity) {
            RandomSource random = RandomSource.create();
            InteractionHand hand = livingEntity.getUsedItemHand();
            ItemStack itemInHand = livingEntity.getItemInHand(hand);
            StaffSpecialUtils.getRandomEnchantment().forEach(enchantment -> {
                itemInHand.set(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);
                itemInHand.enchant(enchantment, random.nextInt(Byte.MAX_VALUE));
            });
        }
    }

}