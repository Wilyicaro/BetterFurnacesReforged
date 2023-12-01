package wily.betterfurnaces.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import wily.betterfurnaces.blocks.SmeltingBlock;

import java.util.Optional;

@Mixin(BlockEntity.class)
public class BlockEntityMixin {
    @Inject(method = "loadStatic", at = @At("HEAD"), cancellable = true)
    private static void loadStatic(BlockPos pos, BlockState blockState, CompoundTag compoundTag, CallbackInfoReturnable<BlockEntity> cir){
        ResourceLocation id = new ResourceLocation(compoundTag.getString("id"));
        Optional<BlockEntityType<?>> opt = BuiltInRegistries.BLOCK_ENTITY_TYPE.getOptional(id);
        if (blockState.getBlock() instanceof SmeltingBlock && opt.isEmpty()) {
            BlockEntity entity = ((SmeltingBlock)BuiltInRegistries.BLOCK.get(id)).newBlockEntity(pos,blockState);
            if (entity!= null) entity.load(compoundTag);
            cir.setReturnValue(entity);
        }
    }
}
