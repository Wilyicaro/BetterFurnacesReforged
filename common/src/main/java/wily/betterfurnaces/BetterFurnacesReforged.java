package wily.betterfurnaces;

import dev.architectury.registry.CreativeTabRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import wily.betterfurnaces.gitup.UpCheck;
import wily.betterfurnaces.init.Registration;
import wily.betterfurnaces.network.Messages;

// The value here should match an entry in the META-INF/mods.toml file

public class BetterFurnacesReforged
{

    public static final String MOD_ID = "betterfurnacesreforged";
    public static final String VERSION = "1.0.3";
    public static final String MC_VERSION = "1.19.2";

    public static final Logger LOGGER = LogManager.getLogger();
    public static final CreativeModeTab ITEM_GROUP = CreativeTabRegistry.create(new ResourceLocation(BetterFurnacesReforged.MOD_ID,"tab"), ()-> Registration.EXTREME_FURNACE.get().asItem().getDefaultInstance());

    public static void init(){

        Messages.registerMessages("betterfurnaces_network");

        Config.setupPlatformConfig();

        Registration.init();

        if (Config.checkUpdates) {
            new UpCheck();
        } else {
            LOGGER.warn("You have disabled BetterFurnace's Update Checker, to re-enable: change the value of Update Checker in .minecraft->config->betterfurnacesreforged-client.toml to 'true'.");
        }

    }


}