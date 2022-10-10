package wily.betterfurnaces;

import wily.betterfurnaces.gitup.UpCheck;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import wily.betterfurnaces.compat.TopCompatibility;
import wily.betterfurnaces.handler.GuiHandler;
import wily.betterfurnaces.init.ModBlockColors;
import wily.betterfurnaces.init.ModItemColors;
import wily.betterfurnaces.init.ModObjects;
import wily.betterfurnaces.net.MessageSync;
import wily.betterfurnaces.net.MessageColorSliderSync;
import wily.betterfurnaces.tile.*;
import wily.betterfurnaces.utils.OreProcessingRegistry;
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
	public static final String VERSION = "1.5.3";
	public static final String MC_VERSION = "1.12.2";
	public static final Logger LOGGER = LogManager.getLogger(MODID);

	@Instance
	public static BetterFurnacesReforged INSTANCE;
	public static final SimpleNetworkWrapper NETWORK = NetworkRegistry.INSTANCE.newSimpleChannel("betterfurnaces");

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		NetworkRegistry.INSTANCE.registerGuiHandler(INSTANCE, new GuiHandler());
		NETWORK.registerMessage(MessageSync.Handler.class, MessageSync.class, 0, Side.CLIENT);
		NETWORK.registerMessage(MessageColorSliderSync.Handler.class, MessageColorSliderSync.class, 1, Side.SERVER);
		Configuration cfg = new Configuration(event.getSuggestedConfigurationFile());
		boolean checkUpdates = cfg.getBoolean("checkUpdates", "updates", true,"true = check for updates, false = don't check for updates.", "en-US");
		if (checkUpdates) {
			new UpCheck();
		} else {
			this.LOGGER.warn("You have disabled BetterFurnace's Update Checker, to re-enable: change the value of Update Checker in .minecraft->config->betterfurnacesreforged-client.toml to 'true'.");
		}
		if (cfg.hasChanged()) cfg.save();
		TopCompatibility.MainCompatHandler.registerTOP();
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
		GameRegistry.registerTileEntity(TileEntitySmeltingBase.class, ModObjects.IRON_FURNACE.getRegistryName());
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
