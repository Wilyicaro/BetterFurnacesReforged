package wily.betterfurnaces.forge;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import wily.betterfurnaces.BetterFurnacesReforged;
import wily.betterfurnaces.client.ClientSide;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = BetterFurnacesReforged.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class BetterFurnacesForgeClient {
    public static List<ResourceLocation> REGISTER_MODELS = new ArrayList<>();
    @SubscribeEvent
    public static void initClient(FMLClientSetupEvent event){event.enqueueWork(ClientSide::init);}

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void clientInit(ModelEvent.RegisterAdditional event){
        REGISTER_MODELS.forEach((event::register));
    }
}
