package wily.betterfurnaces;


import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ProjectMMO {
    @ExpectPlatform
    public static void burnEvent(ItemStack itemStack, Level level, BlockPos worldPosition,int i){
        throw new AssertionError();
    }
}
