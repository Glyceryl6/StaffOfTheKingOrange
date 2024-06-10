package com.glyceryl6.staff.common.entities;

import com.glyceryl6.staff.common.items.StaffItem;
import com.glyceryl6.staff.registry.ModEntityTypes;
import com.glyceryl6.staff.registry.ModItems;
import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.PlayerHeadBlock;
import net.minecraft.world.level.block.state.BlockState;

import static com.glyceryl6.staff.utils.StaffUniversalUtils.getCoreBlockState;
import static com.glyceryl6.staff.utils.StaffUniversalUtils.setNormalBlockForStaff;

public class PlacedStaff extends Entity implements ItemSupplier {

    private static final EntityDataAccessor<ItemStack> STAFF_STACK = SynchedEntityData.defineId(PlacedStaff.class, EntityDataSerializers.ITEM_STACK);
    private static final EntityDataAccessor<Float> Y_ROT = SynchedEntityData.defineId(PlacedStaff.class, EntityDataSerializers.FLOAT);
    private final ItemStack defaultItem = ModItems.STAFF.get().getDefaultInstance();

    public PlacedStaff(EntityType<?> type, Level level) {
        super(type, level);
    }

    public PlacedStaff(Level level, BlockPos pos) {
        this(ModEntityTypes.PLACED_STAFF.get(), level);
        this.setPos(pos.getX(), pos.getY(), pos.getZ());
    }

    public void setItem(ItemStack stack) {
        if (stack.isEmpty()) {
            this.entityData.set(STAFF_STACK, this.defaultItem);
        } else {
            this.entityData.set(STAFF_STACK, stack.copyWithCount(1));
        }
    }

    @Override
    public ItemStack getItem() {
        return this.entityData.get(STAFF_STACK);
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(STAFF_STACK, ModItems.STAFF.get().getDefaultInstance());
        this.entityData.define(Y_ROT, 0.0F);
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        compound.put("Item", this.getItem().save(new CompoundTag()));
        compound.putFloat("YRot", this.getYRot());
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        this.setYRot(compound.getFloat("YRot"));
        this.setItem(ItemStack.of(compound.getCompound("Item")));
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {
        if (!this.level().isClientSide) {
            ItemStack itemInHand = player.getItemInHand(hand);
            if (itemInHand.isEmpty()) {
                player.setItemInHand(hand, this.getItem());
                this.discard();
            } else {
                if (itemInHand.getItem() instanceof BlockItem blockItem) {
                    Block block = blockItem.getBlock();
                    BlockState state = block.defaultBlockState();
                    if (this.getItem().getItem() instanceof StaffItem) {
                        Block coreBlock = getCoreBlockState(this.getItem()).getBlock();
                        GameProfile profile = NbtUtils.readGameProfile(itemInHand.getOrCreateTagElement("SkullOwner"));
                        boolean flag = block instanceof PlayerHeadBlock;
                        if (coreBlock != block || flag) {
                            ItemStack copyOfStack = this.getItem().copy();
                            if (!player.getAbilities().instabuild) {
                                itemInHand.shrink(1);
                            }

                            setNormalBlockForStaff(copyOfStack, state);
                            if (flag && profile != null) {
                                copyOfStack.getOrCreateTag().put("SkullOwner", NbtUtils.writeGameProfile(new CompoundTag(), profile));
                            }

                            this.setItem(copyOfStack);
                        }
                    }
                }
            }
        }

        return InteractionResult.sidedSuccess(this.level().isClientSide);
    }

    @Override
    public void setPos(double x, double y, double z) {
        double x1 = Mth.floor(x) + 0.5D;
        double y1 = Mth.floor((y + 0.5D));
        double z1 = Mth.floor(z) + 0.5D;
        super.setPos(x1, y1, z1);
        this.xo = x1;
        this.yo = y1;
        this.zo = z1;
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    @Override
    public boolean displayFireAnimation() {
        return false;
    }

}