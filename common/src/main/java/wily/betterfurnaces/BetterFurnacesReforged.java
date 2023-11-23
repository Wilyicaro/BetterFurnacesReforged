package wily.betterfurnaces;

import com.google.common.base.Suppliers;
import dev.architectury.platform.Platform;
import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import wily.betterfurnaces.gitup.UpCheck;
import wily.betterfurnaces.init.ModObjects;
import wily.betterfurnaces.init.Registration;
import wily.betterfurnaces.network.Messages;
import wily.ultimatefurnaces.UltimateFurnaces;

import java.util.function.Supplier;


public class BetterFurnacesReforged
{

    public static final String MOD_ID = "betterfurnacesreforged";
    public static final Supplier<String> VERSION =  Platform.getMod(MOD_ID)::getVersion;
    public static final String MC_VERSION = "1.18.2-arc";

    public static final Supplier<Registries> REGISTRIES = Suppliers.memoize(() -> Registries.get(MOD_ID));
    public static final Logger LOGGER = LogManager.getLogger();
    public static final CreativeModeTab ITEM_GROUP = CreativeTabRegistry.create(new ResourceLocation(BetterFurnacesReforged.MOD_ID, "tab"), () -> ModObjects.EXTREME_FURNACE.get().asItem().getDefaultInstance());

    public static void init(){

        Messages.registerMessages("betterfurnaces_network");

        Config.setupPlatformConfig();

        Registration.init();

        if (Config.enableUltimateFurnaces.get())
            UltimateFurnaces.init();

        if (Config.checkUpdates.get()) {
            new UpCheck();
        } else {
            LOGGER.warn("You have disabled BetterFurnace's Update Checker, to re-enable: change the value of Update Checker in .minecraft->config->betterfurnacesreforged-client.toml to 'true'.");
        }

    }

}
