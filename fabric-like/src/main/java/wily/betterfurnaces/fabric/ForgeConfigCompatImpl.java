package wily.betterfurnaces.fabric;

import fuzs.forgeconfigapiport.api.config.v2.ForgeConfigRegistry;
import fuzs.forgeconfigapiport.api.config.v2.ModConfigEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.minecraftforge.fml.config.ModConfig;
import wily.betterfurnaces.BetterFurnacesReforged;

import static wily.betterfurnaces.ForgeConfigCompat.*;

public class ForgeConfigCompatImpl {
    public static void registerConfig() {
        ForgeConfigRegistry.INSTANCE.register(BetterFurnacesReforged.MOD_ID,ModConfig.Type.CLIENT,CLIENT_CONFIG);
        ForgeConfigRegistry.INSTANCE.register(BetterFurnacesReforged.MOD_ID, ModConfig.Type.COMMON,COMMON_CONFIG);
        ForgeConfigRegistry.INSTANCE.register(BetterFurnacesReforged.MOD_ID, ModConfig.Type.SERVER,SERVER_CONFIG);
        ModConfigEvents.reloading(BetterFurnacesReforged.MOD_ID).register((l)-> {if (l.getSpec().equals(SERVER_CONFIG)) onServerConfigLoad();});
        ServerWorldEvents.LOAD.register((l,r)-> onServerConfigLoad());
    }
}
