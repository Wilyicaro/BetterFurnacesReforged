package wily.betterfurnaces.forge;

import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import wily.betterfurnaces.BetterFurnacesReforged;

@Mod(BetterFurnacesReforged.MOD_ID)
@Mod.EventBusSubscriber(modid = BetterFurnacesReforged.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class BetterFurnacesForge {
    public BetterFurnacesForge() {
        EventBuses.registerModEventBus(BetterFurnacesReforged.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        BetterFurnacesReforged.init();
    }
}
