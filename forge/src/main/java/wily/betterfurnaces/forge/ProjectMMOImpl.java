package wily.betterfurnaces.forge;

import harmonised.pmmo.api.events.FurnaceBurnEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;

public class ProjectMMOImpl {
    public static void burnEvent(ItemStack itemStack, Level level, BlockPos worldPosition){
        MinecraftForge.EVENT_BUS.post(new FurnaceBurnEvent(itemStack, level, worldPosition));
    }
}
