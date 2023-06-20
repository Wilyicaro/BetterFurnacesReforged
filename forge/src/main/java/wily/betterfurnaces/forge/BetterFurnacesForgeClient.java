package wily.betterfurnaces.forge;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.model.ModelLoader;
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
    public static void initClient(FMLClientSetupEvent event){
        event.enqueueWork(()->{
            ClientSide.init();
            REGISTER_MODELS.forEach((ModelLoader::addSpecialModel));
        });
    }
}
