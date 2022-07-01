package wily.betterfurnaces;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import wily.betterfurnaces.gitup.UpCheck;
import wily.betterfurnaces.init.ClientSide;
import wily.betterfurnaces.init.ModObjects;
import wily.betterfurnaces.init.Registration;
import wily.betterfurnaces.network.Messages;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(BetterFurnacesReforged.MOD_ID)
@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
public class BetterFurnacesReforged
{

    public static final String MOD_ID = "betterfurnacesreforged";
    public static final String VERSION = "104";
    public static final String MC_VERSION = "1.19";

    public static final Logger LOGGER = LogManager.getLogger();

    public static IEventBus MOD_EVENT_BUS;

    public BetterFurnacesReforged() {

        Messages.registerMessages("betterfurnaces_network");

        MOD_EVENT_BUS = FMLJavaModLoadingContext.get().getModEventBus();

        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Config.CLIENT_CONFIG);
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, Config.COMMON_CONFIG);

        FMLJavaModLoadingContext.get().getModEventBus().addListener(ModObjects::init);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(ClientSide::init);

        MOD_EVENT_BUS.register(Registration.class);
        Registration.init();

        Config.loadConfig(Config.CLIENT_CONFIG, FMLPaths.CONFIGDIR.get().resolve(BetterFurnacesReforged.MOD_ID + "-client.toml"));
        Config.loadConfig(Config.COMMON_CONFIG, FMLPaths.CONFIGDIR.get().resolve(BetterFurnacesReforged.MOD_ID + ".toml"));

        if (Config.checkUpdates.get()) {
            new UpCheck();
        } else {
            this.LOGGER.warn("You have disabled BetterFurnace's Update Checker, to re-enable: change the value of Update Checker in .minecraft->config->betterfurnacesreforged-client.toml to 'true'.");
        }
    }
    private static <T extends Recipe<?>> RecipeType<T> recipeRegister(final String key) {
        return Registry.register(Registry.RECIPE_TYPE, new ResourceLocation(MOD_ID, key), new RecipeType<T>() {
            @Override
            public String toString() {
                return key;
            }
        });
    }
}
