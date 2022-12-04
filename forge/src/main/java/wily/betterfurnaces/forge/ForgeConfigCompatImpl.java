package wily.betterfurnaces.forge;

import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;


import static wily.betterfurnaces.ForgeConfigCompat.*;

@Mod.EventBusSubscriber
public class ForgeConfigCompatImpl {
    public static void registerConfig() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT,CLIENT_CONFIG);
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER,COMMON_CONFIG);
    }
    @SubscribeEvent
    public static void onLevelLoad(final LevelEvent.Load event) {
        loadAllConfigs();
    }
}
