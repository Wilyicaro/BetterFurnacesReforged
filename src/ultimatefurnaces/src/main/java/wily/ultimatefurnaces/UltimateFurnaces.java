package wily.ultimatefurnaces;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import wily.betterfurnaces.Config;
import wily.betterfurnaces.init.Registration;
import wily.ultimatefurnaces.init.ClientSide;
import wily.ultimatefurnaces.init.ModObjects;
import wily.ultimatefurnaces.init.Registration;
import wily.betterfurnaces.network.Messages;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(UltimateFurnaces.MOD_ID)
@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
public class UltimateFurnaces
{

    public static final String MOD_ID = "ultimatefurnaces_bfr";
    public static final String VERSION = "1.1";
    public static final String MC_VERSION = "1.16.5";

    public static final Logger LOGGER = LogManager.getLogger();

    public static IEventBus MOD_EVENT_BUS;

    public UltimateFurnaces() {

        MOD_EVENT_BUS = FMLJavaModLoadingContext.get().getModEventBus();

        FMLJavaModLoadingContext.get().getModEventBus().addListener(ModObjects::init);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(ClientSide::init);

        MOD_EVENT_BUS.register(Registration.class);
        Registration.init();

    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event)
    {
        Registration.registerBlocks(event);
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event)
    {
        Registration.registerItems(event);
    }
}
