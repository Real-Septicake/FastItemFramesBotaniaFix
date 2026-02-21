package io.github.real_septicake.fastbotaniafix.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import fuzs.fastitemframes.world.level.block.entity.ItemFrameBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vazkii.botania.common.block.block_entity.corporea.BaseCorporeaBlockEntity;
import vazkii.botania.common.block.block_entity.corporea.CorporeaFunnelBlockEntity;
import vazkii.botania.common.helper.FilterHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Mixin(CorporeaFunnelBlockEntity.class)
public abstract class FunnelMixin extends BaseCorporeaBlockEntity {
    @Shadow(remap = false)
    @Final
    private static int[] ROTATION_TO_STACK_SIZE;

    protected FunnelMixin(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Inject(method = "getFilter", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;getEntitiesOfClass(Ljava/lang/Class;Lnet/minecraft/world/phys/AABB;)Ljava/util/List;", shift = At.Shift.AFTER), remap = false)
    private void fix(CallbackInfoReturnable<WeightedRandomList<FilterHelper.WeightedItemStack>> cir, @Local(name = "filter") List<FilterHelper.WeightedItemStack> filter, @Local(name = "dir") Direction dir) {
            Optional<ItemFrameBlockEntity> frame = level.getBlockEntity(worldPosition.relative(dir), fuzs.fastitemframes.init.ModRegistry.ITEM_FRAME_BLOCK_ENTITY.value());
            frame.ifPresent(f -> {
                ItemFrame itemFrame = f.getEntityRepresentation();
                if (itemFrame == null)
                    return;
                if (itemFrame.getDirection() == dir) {
                    List<ItemStack> filterStacks = FilterHelper.getFilterItems(itemFrame);
                    if (!filterStacks.isEmpty()) {
                        int stackSize = ROTATION_TO_STACK_SIZE[itemFrame.getRotation()];
                        filterStacks.stream()
                                .map(s -> FilterHelper.WeightedItemStack.of(s.copyWithCount(stackSize), s.getCount()))
                                .forEach(filter::add);
                    }
                }
            });
    }
}
