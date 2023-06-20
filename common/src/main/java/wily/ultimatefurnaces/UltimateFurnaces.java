package wily.ultimatefurnaces;


import me.shedaniel.architectury.registry.CreativeTabs;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import wily.betterfurnaces.BetterFurnacesReforged;
import wily.ultimatefurnaces.init.RegistrationUF;

// The value here should match an entry in the META-INF/mods.toml file

public class UltimateFurnaces
{


    public static final Logger LOGGER = LogManager.getLogger();
    public static final CreativeModeTab ITEM_GROUP = CreativeTabs.create(new ResourceLocation(BetterFurnacesReforged.MOD_ID,"tab_ultimate"), ()-> RegistrationUF.ULTIMATE_FURNACE.get().asItem().getDefaultInstance());

}
