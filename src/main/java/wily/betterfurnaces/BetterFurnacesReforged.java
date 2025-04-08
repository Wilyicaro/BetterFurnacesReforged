package wily.betterfurnaces;

import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import wily.betterfurnaces.gitup.UpCheck;
import wily.betterfurnaces.init.Registration;
import wily.betterfurnaces.network.*;
import wily.factoryapi.FactoryAPI;
import wily.factoryapi.FactoryAPIPlatform;
import wily.factoryapi.FactoryEvent;
import wily.factoryapi.base.config.FactoryConfig;

import java.util.function.Supplier;


//? if forge {
/*import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.api.distmarker.Dist;
*///?} else if neoforge {
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.api.distmarker.Dist;
//?}

//? if forge || neoforge
@Mod(BetterFurnacesReforged.MOD_ID)
public class BetterFurnacesReforged {

    public static final String MOD_ID = "betterfurnacesreforged";
    public static final Supplier<String> VERSION =  FactoryAPIPlatform.getModInfo(MOD_ID)::getVersion;
    public static final String MC_VERSION = "1.20.4";

    public static final Logger LOGGER = LogManager.getLogger();

    public BetterFurnacesReforged() {
        init();
        //? if forge || neoforge {
        if (FMLEnvironment.dist == Dist.CLIENT)
            BetterFurnacesReforgedClient.init();
        //?}
    }

    public static void init(){

        FactoryEvent.registerPayload(registry-> {
            registry.register(false, FluidSyncPayload.ID);
            registry.register(true, OrientationSyncPayload.ID);
            registry.register(true, ShowSettingsSyncPayload.ID);
            registry.register(true, SettingsSyncPayload.ID);
            registry.register(true, CobblestoneGeneratorSyncPayload.ID);
            registry.register(true, SliderColorSyncPayload.ID);
        });

        FactoryEvent.serverStarted(server -> BFRConfig.COMMON_STORAGE.withServerFile(server,"serverconfig/betterfurnacesreforged.json").load());
        FactoryConfig.registerCommonStorage(createModLocation("common"), BFRConfig.COMMON_STORAGE);

        Registration.init();

        if (BFRConfig.checkUpdates.get()) {
            new UpCheck();
        } else {
            LOGGER.warn("You have disabled BetterFurnace's Update Checker, to re-enable: change the value of Update Checker in .minecraft->config->betterfurnacesreforged-client.toml to 'true'.");
        }

    }

    public static ResourceLocation createModLocation(String path){
        return FactoryAPI.createLocation(MOD_ID, path);
    }

}
