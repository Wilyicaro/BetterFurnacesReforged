package wily.betterfurnaces.fabric;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.minecraftforge.api.ModLoadingContext;
import net.minecraftforge.api.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.config.ModConfig;
import wily.betterfurnaces.BetterFurnacesReforged;

import static wily.betterfurnaces.ForgeConfigCompat.*;

public class ForgeConfigCompatImpl {
    public static void registerConfig() {
        ModLoadingContext.registerConfig(BetterFurnacesReforged.MOD_ID,ModConfig.Type.CLIENT,CLIENT_CONFIG);
        ModLoadingContext.registerConfig(BetterFurnacesReforged.MOD_ID, ModConfig.Type.COMMON,COMMON_CONFIG);
        ModLoadingContext.registerConfig(BetterFurnacesReforged.MOD_ID, ModConfig.Type.SERVER,SERVER_CONFIG);
        ModConfigEvent.RELOADING.register((l)-> {if (l.getSpec().equals(SERVER_CONFIG)) onServerConfigLoad();});
        ServerWorldEvents.LOAD.register((l,r)-> onServerConfigLoad());
    }
}
