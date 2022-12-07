package wily.betterfurnaces;


import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.platform.Platform;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;
import wily.betterfurnaces.init.Registration;

import java.util.ArrayList;
import java.util.List;

import static wily.betterfurnaces.BetterFurnacesReforged.REGISTRIES;


public class Config {

    public static final String CATEGORY_GENERAL = "general";
    public static final String CATEGORY_FURNACE = "furnaces";
    public static final String CATEGORY_MODDED_FURNACE = "modded_furnaces";
    public static final String CATEGORY_JEI = "jei";
    public static final String CATEGORY_UPDATES = "updates";
    public static final String CATEGORY_MISC = "misc";

    public static final List<String> supportedLiquidXps = new ArrayList<>(List.of("mob_grinding_utils:fluid_xp","industrialforegoing:essence","cyclic:xpjuice","reliquary:xp_juice","kibe:liquid_xp"));

    public static String getLiquidXPType() {
        return supportedLiquidXps.size() > xpFluidType ? supportedLiquidXps.get(xpFluidType) : supportedLiquidXps.get(0);
    }
    public static Fluid getLiquidXP() {
        return REGISTRIES.get().get(Registry.FLUID_REGISTRY).get(new ResourceLocation(getLiquidXPType()));
    }

    public static String getLiquidXPMod() {
        String s = getLiquidXPType();
            return s.substring(0, s.indexOf(":"));
    }

    public static void setupPlatformConfig(){
    if (Platform.isModLoaded("forgeconfigapiport") || Platform.isForge()) ForgeConfigCompat.setupPlatformConfig();
    else BetterFurnacesReforged.LOGGER.warn("Currently ForgeConfigApiPort isn't installed, to config  BetterFurnaces options, please consider install it!");
    }



    public static boolean checkUpdates = true;
    public static int cacheCapacity = 10;
    public static int furnaceXPDropValue = 1;
    public static int furnaceXPDropValue2 = 100000;

    public static boolean enableUltimateFurnaces = true;
    public static boolean enableJeiPlugin = true;

    public static boolean enableJeiCatalysts = true;

    public static boolean enableJeiClickArea = true;

    public static int copperTierSpeed = 175;
    public static int  ironTierSpeed = 150;
    public static int steelTierSpeed = 125;
    public static int goldTierSpeed = 100;
    public static int amethystTierSpeed = 75;
    public static int diamondTierSpeed = 50;
    public static int platinumTierSpeed = 25;
    public static int netherhotTierSpeed = 8;
    public static int extremeTierSpeed = 4;
    public static int ultimateTierSpeed = 1;

    public static int xpFluidType = 0;


}
