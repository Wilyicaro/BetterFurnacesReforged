package wily.betterfurnaces;

import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import wily.betterfurnaces.handler.GuiHandler;
import wily.betterfurnaces.init.ModBlockColors;
import wily.betterfurnaces.init.ModItemColors;
import wily.betterfurnaces.init.ModObjects;
import wily.betterfurnaces.net.MessageSync;
import wily.betterfurnaces.net.MessageSyncTF;
import wily.betterfurnaces.tile.*;
import wily.betterfurnaces.utils.OreProcessingRegistry;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid = BetterFurnacesReforged.MODID, name = BetterFurnacesReforged.MODNAME, version = BetterFurnacesReforged.VERSION, useMetadata = true)
public class BetterFurnacesReforged {
	public static final String MODID = "betterfurnacesreforged";
	public static final String MODNAME = "BetterFurnaces Reforged";
	public static final String VERSION = "1.4";
	public static final Logger LOGGER = LogManager.getLogger(MODID);

	public static Object2IntMap<String> FLUID_FUELS = new Object2IntOpenHashMap<>();
	@Instance
	public static BetterFurnacesReforged INSTANCE;
	public static final SimpleNetworkWrapper NETWORK = NetworkRegistry.INSTANCE.newSimpleChannel("betterfurnaces");

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		NetworkRegistry.INSTANCE.registerGuiHandler(INSTANCE, new GuiHandler());
		NETWORK.registerMessage(MessageSync.Handler.class, MessageSync.class, 0, Side.CLIENT);
		NETWORK.registerMessage(MessageSyncTF.Handler.class, MessageSyncTF.class, 1, Side.CLIENT);
		Configuration cfg = new Configuration(event.getSuggestedConfigurationFile());
		String[] fuels = cfg.getStringList("Fluid Fuels", "general", new String[] { "lava@20" }, "A list of fluid fuels, in the format name@time, where time is burn ticks per millibucket.");
		for (String s : fuels) {
			String[] split = s.split("@");
			if (split.length != 2) {
				LOGGER.info("Ignoring invalid fluid fuel config entry {}!", s);
				continue;
			}
			try {
				FLUID_FUELS.put(split[0], Integer.parseInt(split[1]));
			} catch (NumberFormatException e) {
				LOGGER.info("Ignoring invalid fluid fuel config entry {}!", s);
				continue;
			}
		}
		if (cfg.hasChanged()) cfg.save();
	}
	@EventHandler
	public void postInit(FMLLoadCompleteEvent event) {
		OreProcessingRegistry.registerDefaults();
	}

	public static final CreativeTabs BF_TAB = new CreativeTabs(MODID) {

		@Override
		public ItemStack getTabIconItem() {
			return new ItemStack(ModObjects.EXTREME_FURNACE);
		}
	};
	@EventHandler
	public void init(FMLInitializationEvent event) {
		GameRegistry.registerTileEntity(TileEntityIronFurnace.class, ModObjects.IRON_FURNACE.getRegistryName());
		GameRegistry.registerTileEntity(TileEntityGoldFurnace.class, ModObjects.GOLD_FURNACE.getRegistryName());
		GameRegistry.registerTileEntity(TileEntityDiamondFurnace.class, ModObjects.DIAMOND_FURNACE.getRegistryName());
		GameRegistry.registerTileEntity(TileEntityNetherhotFurnace.class, ModObjects.NETHERHOT_FURNACE.getRegistryName());
		GameRegistry.registerTileEntity(TileEntityExtremeFurnace.class, ModObjects.EXTREME_FURNACE.getRegistryName());
		GameRegistry.registerTileEntity(TileEntityForge.class, ModObjects.EXTREME_FORGE.getRegistryName());
		if (event.getSide().isClient()) {
			ModBlockColors.registerBlockColors();
			ModItemColors.registerItemColors();
		}
	}

}
