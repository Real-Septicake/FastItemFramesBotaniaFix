package io.github.real_septicake.fastbotaniafix.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import fuzs.fastitemframes.world.level.block.entity.ItemFrameBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vazkii.botania.common.block.flower.functional.HopperhockBlockEntity;
import vazkii.botania.common.helper.FilterHelper;

import java.util.List;
import java.util.Optional;

@Mixin(HopperhockBlockEntity.class)
public class HopperhockMixin {
    @Inject(method = "getFilterForInventory", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;getEntitiesOfClass(Ljava/lang/Class;Lnet/minecraft/world/phys/AABB;)Ljava/util/List;", shift = At.Shift.AFTER, remap = true), remap = false)
    private static void fix(Level level, BlockPos pos, boolean recursiveForDoubleChests, CallbackInfoReturnable<List<ItemStack>> cir, @Local(name = "filter") List<ItemStack> filter, @Local(name = "dir") Direction dir) {
        Optional<ItemFrameBlockEntity> frame = level.getBlockEntity(pos.relative(dir), fuzs.fastitemframes.init.ModRegistry.ITEM_FRAME_BLOCK_ENTITY.value());
        frame.ifPresent(f -> {
            ItemFrame itemFrame = f.getEntityRepresentation();
            if (itemFrame == null)
                return;
            if(itemFrame.getDirection() == dir)
                filter.addAll(FilterHelper.getFilterItems(itemFrame));
        });
    }
}
