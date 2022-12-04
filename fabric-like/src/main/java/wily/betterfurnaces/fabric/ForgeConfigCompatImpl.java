package wily.betterfurnaces.fabric;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.minecraftforge.api.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import wily.betterfurnaces.BetterFurnacesReforged;

import static wily.betterfurnaces.ForgeConfigCompat.*;

public class ForgeConfigCompatImpl {
    public static void registerConfig() {
        ModLoadingContext.registerConfig(BetterFurnacesReforged.MOD_ID,ModConfig.Type.CLIENT,CLIENT_CONFIG);
        ModLoadingContext.registerConfig(BetterFurnacesReforged.MOD_ID, ModConfig.Type.SERVER,COMMON_CONFIG);
        ServerWorldEvents.LOAD.register(((server, world) -> loadAllConfigs()));
    }
}
