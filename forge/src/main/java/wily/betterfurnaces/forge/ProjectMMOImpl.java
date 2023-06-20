package wily.betterfurnaces.forge;


import harmonised.pmmo.events.FurnaceHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ProjectMMOImpl {
    public static void burnEvent(ItemStack itemStack, Level level, BlockPos worldPosition,int i){
        FurnaceHandler.handleSmelted(itemStack,level,worldPosition,i);
    }
}
