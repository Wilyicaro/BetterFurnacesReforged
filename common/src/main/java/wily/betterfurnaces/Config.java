package wily.betterfurnaces;


import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.platform.Platform;


public class Config {

    public static final String CATEGORY_GENERAL = "general";
    public static final String CATEGORY_FURNACE = "furnaces";
    public static final String CATEGORY_MODDED_FURNACE = "modded_furnaces";
    public static final String CATEGORY_JEI = "jei";
    public static final String CATEGORY_UPDATES = "updates";
    public static final String CATEGORY_MISC = "misc";

    public static String getLiquidXPType() {
        if (xpFluidType == 0) {
            return "mob_grinding_utils:fluid_xp";
        } else if (xpFluidType == 1) {
            return "industrialforegoing:essence";
        } else if (xpFluidType == 2) {
            return "cyclic:xpjuice";
        } else if (xpFluidType == 3) {
            return "reliquary:xp_juice";
        }
        return "mob_grinding_utils:fluid_xp";
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
