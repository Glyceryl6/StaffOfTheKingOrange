package com.glyceryl6.staff.common.items;

import com.glyceryl6.staff.api.IAbstractStaffFunction;
import com.glyceryl6.staff.client.renderer.StaffItemRenderer;
import com.glyceryl6.staff.common.entities.PlacedStaff;
import com.glyceryl6.staff.component.Staffs;
import com.glyceryl6.staff.registry.ModDataComponents;
import com.glyceryl6.staff.registry.ModItems;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.ResolvableProfile;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import static com.glyceryl6.staff.utils.StaffUniversalUtils.*;

public class StaffItem extends Item {

    public StaffItem() {
        super(new Properties().stacksTo(1));
    }

    private boolean isContinuousMode(ItemStack stack) {
        Staffs staffs = stack.get(ModDataComponents.STAFFS.get());
        return staffs != null && staffs.continuousMode();
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return true;
    }

    @Override
    public boolean canAttackBlock(BlockState state, Level level, BlockPos pos, Player player) {
        return !player.isCreative();
    }

    @Override
    public void onUseTick(Level level, LivingEntity livingEntity, ItemStack stack, int remainingUseDuration) {
        if (livingEntity instanceof ServerPlayer player && this.isContinuousMode(stack)) {
            if (player.getTicksUsingItem() > 5) {
                IAbstractStaffFunction function = getStaffFunction(stack);
                if (function.enableUseTick()) {
                    function.useTick(level, player, stack);
                }
            } else {
                BlockState state = getCoreBlockState(stack);
                if (state.getBlock() instanceof NoteBlock) {
                    int note = level.random.nextInt(NoteBlockInstrument.values().length);
                    stack.set(ModDataComponents.STAFFS.get(), new Staffs(Boolean.TRUE, Boolean.TRUE, note));
                }
            }
        }
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity livingEntity, int timeCharged) {
        if (livingEntity instanceof ServerPlayer && this.isContinuousMode(stack)) {
            getStaffFunction(stack).releaseUsing(stack, level, livingEntity, timeCharged);
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack itemInHand = player.getItemInHand(usedHand);
        if (this.isContinuousMode(itemInHand)) {
            player.startUsingItem(usedHand);
            return InteractionResultHolder.consume(itemInHand);
        } else {
            IAbstractStaffFunction function = getStaffFunction(itemInHand);
            if (function.enableUse()) {
                function.use(level, player, itemInHand);
                return InteractionResultHolder.sidedSuccess(itemInHand, level.isClientSide);
            }
        }

        return super.use(level, player, usedHand);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        ItemStack itemInHand = context.getItemInHand();
        Player player = context.getPlayer();
        Level level = context.getLevel();
        if (player != null && player.isShiftKeyDown()) {
            BlockPos pos = context.getClickedPos().above();
            BlockState state = level.getBlockState(pos);
            if (!level.isClientSide && state.canBeReplaced()) {
                PlacedStaff staff = new PlacedStaff(level, pos);
                Direction direction = player.getDirection();
                Direction defaultDirection = player.getDirection();
                Direction.Axis axis = direction.getAxis();
                Direction cw = direction.getClockWise();
                Direction ccw = direction.getCounterClockWise();
                if (axis.isHorizontal()) {
                    defaultDirection = axis == Direction.Axis.X ? cw : ccw;
                }

                staff.setYRot(defaultDirection.toYRot());
                staff.setItem(itemInHand);
                level.addFreshEntity(staff);
                itemInHand.consume(1, player);
            }

            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        IAbstractStaffFunction function = getStaffFunction(itemInHand);
        if (function.enableUse()) {
            if (function.canPlaceBlock(context)) {
                BlockState coreState = getCoreBlockState(itemInHand);
                List<Block> blocks = function.placeableBlocks();
                if (blocks.size() > 1) {
                    RandomSource random = RandomSource.create();
                    Block block = blocks.get(random.nextInt(blocks.size()));
                    coreState = getRandomBlockState(block);
                }

                return this.place(context, coreState);
            }

            if (function.enableUseOnBlock()) {
                function.useOnBlock(context);
                return InteractionResult.sidedSuccess(level.isClientSide);
            }
        }

        return super.useOn(context);
    }

    private InteractionResult place(UseOnContext context, BlockState coreState) {
        BlockPlaceContext placeContext = new BlockPlaceContext(context);
        if (!placeContext.canPlace() || !this.placeBlock(placeContext, coreState)) {
            return InteractionResult.FAIL;
        } else {
            BlockPos clickedPos = placeContext.getClickedPos();
            Level level = placeContext.getLevel();
            Player player = placeContext.getPlayer();
            ItemStack itemStack = placeContext.getItemInHand();
            BlockState state = level.getBlockState(clickedPos);
            if (state.is(coreState.getBlock())) {
                state.getBlock().setPlacedBy(level, clickedPos, state, player, itemStack);
                if (player instanceof ServerPlayer serverPlayer) {
                    CriteriaTriggers.PLACED_BLOCK.trigger(serverPlayer, clickedPos, itemStack);
                }
            }

            SoundType soundType = state.getSoundType(level, clickedPos, player);
            float volume = (soundType.getVolume() + 1.0F) / 2.0F;
            float pitch = soundType.getPitch() * 0.8F;
            level.playSound(player, clickedPos, soundType.getPlaceSound(), SoundSource.BLOCKS, volume, pitch);
            level.gameEvent(GameEvent.BLOCK_PLACE, clickedPos, GameEvent.Context.of(player, state));
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
    }

    private boolean placeBlock(BlockPlaceContext context, BlockState state) {
        return context.getLevel().setBlock(context.getClickedPos(), state, 11);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand usedHand) {
        getStaffFunction(stack).useOnEntity(player, usedHand, target);
        return InteractionResult.sidedSuccess(player.level().isClientSide);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new CustomRenderer());
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return this.isContinuousMode(stack) ? 72000 : 0;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return this.isContinuousMode(stack) ? UseAnim.CUSTOM : UseAnim.NONE;
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        DataComponentType<ItemAttributeModifiers> attributes = DataComponents.ATTRIBUTE_MODIFIERS;
        DataComponentType<CustomData> coreState = ModDataComponents.STAFF_CORE_STATE.get();
        DataComponentType<Staffs> staffs = ModDataComponents.STAFFS.get();
        stack.set(attributes, getStaffFunction(stack).addAttributes(stack));
        if (stack.get(coreState) == null) {
            setNormalBlockForStaff(stack, Blocks.COMMAND_BLOCK.defaultBlockState());
        }

        if (stack.get(staffs) == null) {
            stack.set(staffs, new Staffs(Boolean.TRUE, Boolean.TRUE, 0));
        }
    }

    @Override
    public Component getName(ItemStack stack) {
        String key = "item.staff_of_the_king_orange.custom_staff";
        BlockState state = getCoreBlockState(stack);
        String name = state.getBlock().getName().getString();
        ResolvableProfile profile = stack.get(DataComponents.PROFILE);
        if (state.getBlock() instanceof PlayerHeadBlock && profile != null) {
            name = profile.name().orElse(name);
            if (name.startsWith("MHF_")) {
                name = name.replaceFirst("MHF_", (""));
            }
        }

        return Component.translatable(key, name);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        Staffs staffs = stack.get(ModDataComponents.STAFFS.get());
        Block block = getCoreBlockState(stack).getBlock();
        ResolvableProfile profile = stack.get(DataComponents.PROFILE);
        MutableComponent literal = Component.literal(block.getName().getString());
        if (block instanceof PlayerHeadBlock && profile != null) {
            String key = block.asItem().getDescriptionId() + ".named";
            Optional<String> optional = profile.name();
            if (optional.isPresent()) {
                literal = Component.translatable(key, optional.get());
            }
        }

        Component component = literal.withStyle(ChatFormatting.GOLD);
        tooltipComponents.add(Component.translatable("tooltip.staff.core_block")
                .withStyle(ChatFormatting.GREEN).append(component));

        if (staffs != null) {
            staffs.addToTooltip(context, tooltipComponents::add, tooltipFlag);
        }
    }

    private static class CustomRenderer implements IClientItemExtensions {

        private static final HumanoidModel.ArmPose STAFF_USING =
                HumanoidModel.ArmPose.create("staff_using", true, ((model, entity, arm) -> {
                    float headXRot = model.head.xRot - (float) Math.PI / 2.0F;
                    if (arm == HumanoidArm.RIGHT) {
                        model.rightArm.xRot = headXRot;
                        model.rightArm.yRot = model.head.yRot;
                    } else {
                        model.leftArm.xRot = headXRot;
                        model.leftArm.yRot = model.head.yRot;
                    }
                }));

        @Override
        public BlockEntityWithoutLevelRenderer getCustomRenderer() {
            return new StaffItemRenderer(Minecraft.getInstance().getEntityModels());
        }

        @Override
        public HumanoidModel.@Nullable ArmPose getArmPose(LivingEntity entityLiving, InteractionHand hand, ItemStack itemStack) {
            if (itemStack.is(ModItems.STAFF.get()) && entityLiving.getUsedItemHand() == hand && entityLiving.getUseItemRemainingTicks() > 0) {
                return STAFF_USING;
            }

            return HumanoidModel.ArmPose.EMPTY;
        }

        @Override
        public boolean applyForgeHandTransform(
                PoseStack poseStack, LocalPlayer player, HumanoidArm arm, ItemStack itemInHand,
                float partialTick, float equipProcess, float swingProcess) {
            float i = arm == HumanoidArm.RIGHT ? 1.0F : -1.0F;
            float useDuration = (float) itemInHand.getUseDuration();
            float remainingTicks = (float) player.getUseItemRemainingTicks();
            float f7 = useDuration - (remainingTicks - partialTick + 1.0F);
            float f12 = f7 / 10.0F;
            float limit = i * -0.641864F;
            f12 = (f12 * f12 + f12 * 2.0F) / 3.0F;
            if (f12 > 1.0F) {
                f12 = 1.0F;
            }

            if (player.getUseItem() == itemInHand && player.isUsingItem()) {
                poseStack.translate(i * 0.5F, -limit / 3.0F * i, -0.72F);
                poseStack.translate(i * -0.5F, 0.0F, -0.5F);
                poseStack.mulPose(Axis.XN.rotationDegrees(Math.min(90.0F, 90.0F * 3.0F * f12)));
                poseStack.mulPose(Axis.YN.rotation(0.0F));
                return true;
            }

            return false;
        }

    }

}