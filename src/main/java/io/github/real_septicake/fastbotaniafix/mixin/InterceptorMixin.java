package io.github.real_septicake.fastbotaniafix.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import fuzs.fastitemframes.world.level.block.entity.ItemFrameBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vazkii.botania.common.block.block_entity.corporea.BaseCorporeaBlockEntity;
import vazkii.botania.common.block.block_entity.corporea.CorporeaInterceptorBlockEntity;
import vazkii.botania.common.helper.FilterHelper;

import java.util.List;
import java.util.Optional;

@Mixin(CorporeaInterceptorBlockEntity.class)
public abstract class InterceptorMixin extends BaseCorporeaBlockEntity {
    protected InterceptorMixin(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Inject(method = "getFilter", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;getEntitiesOfClass(Ljava/lang/Class;Lnet/minecraft/world/phys/AABB;)Ljava/util/List;", shift = At.Shift.AFTER, remap = true), remap = false)
    private void fix(CallbackInfoReturnable<List<ItemStack>> cir, @Local(name = "filter") List<ItemStack> filter, @Local(name = "dir") Direction dir) {
        Optional<ItemFrameBlockEntity> frame = level.getBlockEntity(worldPosition.relative(dir), fuzs.fastitemframes.init.ModRegistry.ITEM_FRAME_BLOCK_ENTITY.value());
        frame.ifPresent(f -> {
            ItemFrame itemFrame = f.getEntityRepresentation();
            if(itemFrame == null)
                return;
            if(itemFrame.getDirection() == dir) {
                filter.addAll(FilterHelper.getFilterItems(itemFrame));
            }
        });
    }
}
