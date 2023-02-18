package wily.betterfurnaces.forge;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import wily.betterfurnaces.BetterFurnacesReforged;
import wily.betterfurnaces.init.ClientSide;

@Mod.EventBusSubscriber(modid = BetterFurnacesReforged.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class BetterFurnacesForgeClient {
    @SubscribeEvent
    public static void initClient(FMLClientSetupEvent event){event.enqueueWork(ClientSide::init);}
}
