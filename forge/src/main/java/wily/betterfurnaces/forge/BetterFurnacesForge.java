package wily.betterfurnaces.forge;

import dev.architectury.platform.forge.EventBuses;
import dev.architectury.registry.level.biome.forge.BiomeModificationsImpl;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.MissingMappingsEvent;
import wily.betterfurnaces.BetterFurnacesReforged;

import java.util.List;

@Mod(BetterFurnacesReforged.MOD_ID)
@Mod.EventBusSubscriber(modid = BetterFurnacesReforged.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class BetterFurnacesForge {
    public BetterFurnacesForge() {
        EventBuses.registerModEventBus(BetterFurnacesReforged.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        BetterFurnacesReforged.init();
    }
}
