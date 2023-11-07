package wily.betterfurnaces.mixin;

import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import wily.betterfurnaces.blocks.SmeltingBlock;

import java.util.Optional;

@Mixin(BlockEntity.class)
public class BlockEntityMixin {

    @Shadow @Final private static Logger LOGGER;

    @Inject(method = "loadStatic", at = @At("HEAD"), cancellable = true)
    private static void loadStatic(BlockState blockState, CompoundTag compoundTag, CallbackInfoReturnable<BlockEntity> cir){
        ResourceLocation id = new ResourceLocation(compoundTag.getString("id"));
        Optional<BlockEntityType<?>> opt = Registry.BLOCK_ENTITY_TYPE.getOptional(id);
        if ((blockState.getBlock() instanceof SmeltingBlock || Registry.BLOCK.containsKey(id)) && !opt.isPresent()) {
            BlockEntity entity = ((SmeltingBlock)Registry.BLOCK.get(id)).newBlockEntity(null);
            if (entity!= null) entity.load(blockState,compoundTag);
            cir.setReturnValue(entity);
        }
    }
}
