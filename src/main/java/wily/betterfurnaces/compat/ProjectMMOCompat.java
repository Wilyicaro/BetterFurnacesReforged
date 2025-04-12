//? if (<=1.20.1 && forge) || (1.21.1 && neoforge) {
/*package wily.betterfurnaces.compat;


import harmonised.pmmo.api.events.FurnaceBurnEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import wily.factoryapi.FactoryAPIPlatform;

public class ProjectMMOCompat {
    public static void burnEvent(ItemStack itemStack, Level level, BlockPos blockPos){
        FactoryAPIPlatform.getForgeEventBus().post(new FurnaceBurnEvent(itemStack, level, blockPos));
    }
}
*///?}
