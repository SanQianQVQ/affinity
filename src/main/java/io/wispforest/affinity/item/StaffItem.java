package io.wispforest.affinity.item;

import com.google.common.collect.Lists;
import io.wispforest.affinity.blockentity.impl.StaffPedestalBlockEntity;
import io.wispforest.affinity.blockentity.template.InquirableOutlineProvider;
import io.wispforest.affinity.client.render.InWorldTooltipProvider;
import io.wispforest.affinity.component.AffinityComponents;
import io.wispforest.affinity.endec.BuiltInEndecs;
import io.wispforest.affinity.misc.util.MathUtil;
import io.wispforest.affinity.network.AffinityNetwork;
import io.wispforest.endec.impl.KeyedEndec;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.item.TooltipData;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ClickType;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class StaffItem extends Item implements SpecialTransformItem {

    public static final KeyedEndec<List<ItemStack>> BUNDLED_STAFFS = BuiltInEndecs.ITEM_STACK.listOf().keyed("bundled_staffs", (List<ItemStack>) null);

    protected StaffItem(Settings settings) {
        super(settings);
    }

    // -------
    // In Hand
    // -------

    protected abstract TypedActionResult<ItemStack> executeSpell(World world, PlayerEntity player, ItemStack stack, int remainingTicks, @Nullable BlockPos clickedBlock);

    protected abstract float getAethumConsumption(ItemStack stack);

    protected boolean isContinuous(ItemStack stack) {
        return false;
    }

    protected @Nullable Text getModeName(ItemStack stack) {
        return null;
    }

    // -----------
    // On Pedestal
    // -----------

    public boolean canBePlacedOnPedestal() {
        return false;
    }

    public void pedestalTickClient(World world, BlockPos pos, StaffPedestalBlockEntity pedestal) {}

    public void pedestalTickServer(ServerWorld world, BlockPos pos, StaffPedestalBlockEntity pedestal) {}

    public void appendTooltipEntries(World world, BlockPos pos, StaffPedestalBlockEntity pedestal, List<InWorldTooltipProvider.Entry> entries) {}

    public ActionResult onPedestalScrolled(World world, BlockPos pos, StaffPedestalBlockEntity pedestal, boolean direction) {
        return ActionResult.PASS;
    }

    public InquirableOutlineProvider.Outline getAreaOfEffect(World world, BlockPos pos, StaffPedestalBlockEntity pedestal) {
        return null;
    }

    // --------------
    // Implementation
    // --------------

    @Override
    public boolean onClicked(ItemStack stack, ItemStack otherStack, Slot slot, ClickType clickType, PlayerEntity player, StackReference cursorStackReference) {
        if (clickType != ClickType.RIGHT) return false;

        if (otherStack.getItem() instanceof StaffItem) {
            var bundle = new ArrayList<ItemStack>();
            if (stack.has(BUNDLED_STAFFS)) bundle.addAll(stack.get(BUNDLED_STAFFS));

            if (otherStack.has(BUNDLED_STAFFS)) {
                var otherBundle = otherStack.get(BUNDLED_STAFFS);
                otherStack.delete(BUNDLED_STAFFS);
                bundle.add(otherStack);
                bundle.addAll(otherBundle);
            } else {
                bundle.add(otherStack);
            }

            stack.put(BUNDLED_STAFFS, bundle);
            cursorStackReference.set(ItemStack.EMPTY);
            return true;
        } else if (otherStack.isEmpty() && stack.has(BUNDLED_STAFFS)) {
            var bundledStaffs = stack.get(BUNDLED_STAFFS);
            if (bundledStaffs.isEmpty()) return false;

            var removed = bundledStaffs.remove(bundledStaffs.size() - 1);
            if (!bundledStaffs.isEmpty()) {
                stack.put(BUNDLED_STAFFS, bundledStaffs);
            } else {
                stack.delete(BUNDLED_STAFFS);
            }

            cursorStackReference.set(removed);
            return true;
        }

        return false;
    }

    @Override
    public boolean onStackClicked(ItemStack stack, Slot slot, ClickType clickType, PlayerEntity player) {
        if (clickType != ClickType.RIGHT || !slot.getStack().isEmpty()) return false;

        var bundledStaffs = stack.get(BUNDLED_STAFFS);
        if (bundledStaffs == null || bundledStaffs.isEmpty()) return false;

        var removed = bundledStaffs.remove(bundledStaffs.size() - 1);
        if (!bundledStaffs.isEmpty()) {
            stack.put(BUNDLED_STAFFS, bundledStaffs);
        } else {
            stack.delete(BUNDLED_STAFFS);
        }

        slot.setStack(removed);
        return true;
    }

    @Override
    public Optional<TooltipData> getTooltipData(ItemStack stack) {
        if (!stack.has(BUNDLED_STAFFS)) return Optional.empty();
        return Optional.of(new BundleTooltipData(Lists.reverse(stack.get(BUNDLED_STAFFS))));
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        return this.handleItemUse(world, user, hand, null);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        return this.handleItemUse(context.getWorld(), context.getPlayer(), context.getHand(), context.getBlockPos()).getResult();
    }

    private TypedActionResult<ItemStack> handleItemUse(World world, PlayerEntity user, Hand hand, @Nullable BlockPos clickedBlock) {
        final var stack = user.getStackInHand(hand);

        final var aethum = user.getComponent(AffinityComponents.PLAYER_AETHUM);
        final var consumption = this.getAethumConsumption(stack);

        if (this.isContinuous(stack)) {
            if (!aethum.hasAethum(consumption * 20)) return TypedActionResult.pass(stack);

            if (this.executeSpell(world, user, stack, this.getMaxUseTime(stack), clickedBlock).getResult().isAccepted()) {
                user.setCurrentHand(hand);
                return TypedActionResult.consume(stack);
            } else {
                return TypedActionResult.pass(stack);
            }
        } else {
            if (!aethum.hasAethum(consumption)) return TypedActionResult.pass(stack);

            var result = this.executeSpell(world, user, stack, -1, clickedBlock);
            if (result.getResult().isAccepted()) aethum.addAethum(-consumption);

            return result;
        }
    }

    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        if (!(user instanceof PlayerEntity player)) return;

        final var aethum = player.getComponent(AffinityComponents.PLAYER_AETHUM);
        if (!aethum.tryConsumeAethum(this.getAethumConsumption(stack))) {
            user.stopUsingItem();
            return;
        }

        if (!this.executeSpell(world, player, stack, remainingUseTicks, null).getResult().isAccepted()) {
            user.stopUsingItem();
        }
    }

    @Override
    public Text getName(ItemStack stack) {
        final var modeName = this.getModeName(stack);
        if (modeName == null) return super.getName(stack);

        return Text.translatable(this.getTranslationKey()).append(Text.translatable(
                "item.affinity.staff.mode_template",
                modeName
        ));
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if (this.isContinuous(stack)) {
            tooltip.add(Text.translatable(
                    "item.affinity.staff.tooltip.consumption_per_second",
                    MathUtil.rounded(this.getAethumConsumption(stack) * 20, 2)
            ));
        } else {
            tooltip.add(Text.translatable(
                    "item.affinity.staff.tooltip.consumption_per_use",
                    MathUtil.rounded(this.getAethumConsumption(stack), 2)
            ));
        }

        if (this.getModeName(stack) != null) {
            tooltip.add(Text.empty());
            tooltip.add(Text.translatable("item.affinity.staff.tooltip.cycle_mode_hint"));
        }
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return this.isContinuous(stack)
                ? 72000
                : 0;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void applyUseActionTransform(ItemStack stack, AbstractClientPlayerEntity player, MatrixStack matrices, float tickDelta, float swingProgress) {
        matrices.translate(-.5, -.5, -.5);
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-45 + (float) Math.sin((player.clientWorld.getTime() + (double) tickDelta) / 20d)));
        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees((float) Math.sin((player.clientWorld.getTime() + (double) tickDelta) / 30)));
        matrices.translate(.5, .75, .5);
    }

    @Override
    public void applyUseActionLeftArmPose(ItemStack stack, AbstractClientPlayerEntity player, PlayerEntityModel<AbstractClientPlayerEntity> model) {
        model.leftArm.yaw = .1f + model.head.yaw;
        model.leftArm.pitch = -(float) Math.PI / 3.5f + model.head.pitch * .5f;
    }

    @Override
    public void applyUseActionRightArmPose(ItemStack stack, AbstractClientPlayerEntity player, PlayerEntityModel<AbstractClientPlayerEntity> model) {
        model.rightArm.yaw = -.1f + model.head.yaw;
        model.rightArm.pitch = -(float) Math.PI / 3.5f + model.head.pitch * .5f;
    }

    public static ItemStack selectStaffFromBundle(ItemStack coreStack, int newCoreIdx) {
        var bundle = coreStack.get(BUNDLED_STAFFS);
        var newCoreStaff = bundle.get(newCoreIdx).copy();

        coreStack = coreStack.copy();
        coreStack.delete(BUNDLED_STAFFS);
        bundle.set(newCoreIdx, coreStack);

        newCoreStaff.put(BUNDLED_STAFFS, bundle);
        return newCoreStaff;
    }

    public static void initNetwork() {
        AffinityNetwork.CHANNEL.registerServerbound(SelectStaffFromBundlePacket.class, (message, access) -> {
            var playerStack = access.player().getStackInHand(message.hand);
            if (!(playerStack.getItem() instanceof StaffItem)) return;

            var bundle = playerStack.get(BUNDLED_STAFFS);
            if (bundle == null || bundle.isEmpty() || message.staffIdx >= bundle.size()) return;

            access.player().setStackInHand(message.hand, selectStaffFromBundle(playerStack, message.staffIdx));
            access.player().playSound(SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, SoundCategory.MASTER, 1f, 1f);
        });
    }

    public record BundleTooltipData(List<ItemStack> bundleStacks) implements TooltipData {}

    public record SelectStaffFromBundlePacket(Hand hand, int staffIdx) {}
}
