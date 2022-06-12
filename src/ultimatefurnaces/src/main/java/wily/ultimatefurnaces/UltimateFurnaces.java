package wily.ultimatefurnaces;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import wily.ultimatefurnaces.init.ClientSide;
import wily.ultimatefurnaces.init.ModObjects;
import wily.ultimatefurnaces.init.RegistrationUF;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(UltimateFurnaces.MOD_ID)
@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
public class UltimateFurnaces
{

    public static final String MOD_ID = "ultimatefurnaces_bfr";
    public static final String VERSION = "111";
    public static final String MC_VERSION = "1.16.5";

    public static final Logger LOGGER = LogManager.getLogger();

    public static IEventBus MOD_EVENT_BUS;

    public UltimateFurnaces() {

        MOD_EVENT_BUS = FMLJavaModLoadingContext.get().getModEventBus();

        FMLJavaModLoadingContext.get().getModEventBus().addListener(ModObjects::init);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(ClientSide::init);

        MOD_EVENT_BUS.register(RegistrationUF.class);
        RegistrationUF.init();

    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event)
    {
        RegistrationUF.registerBlocks(event);
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event)
    {
        RegistrationUF.registerItems(event);
    }
}
