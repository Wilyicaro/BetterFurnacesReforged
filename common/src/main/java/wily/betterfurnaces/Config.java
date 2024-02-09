package wily.betterfurnaces;


import dev.architectury.platform.Platform;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

import static wily.betterfurnaces.BetterFurnacesReforged.REGISTRIES;


public class Config {

    public static final String CATEGORY_GENERAL = "general";
    public static final String CATEGORY_FURNACE = "furnaces";
    public static final String CATEGORY_MODDED_FURNACE = "modded_furnaces";
    public static final String CATEGORY_JEI = "jei";
    public static final String CATEGORY_UPDATES = "updates";

    public static final List<String> supportedLiquidXps = new ArrayList<>(List.of("mob_grinding_utils:fluid_xp","industrialforegoing:essence","cyclic:xpjuice","reliquary:xp_juice","kibe:liquid_xp"));

    public static String getLiquidXPType() {
        return supportedLiquidXps.size() > xpFluidType.get() ? supportedLiquidXps.get(xpFluidType.get()) : supportedLiquidXps.get(0);
    }
    public static Fluid getLiquidXP() {
        return REGISTRIES.get().get(Registries.FLUID).get(new ResourceLocation(getLiquidXPType()));
    }

    public static String getLiquidXPMod() {
        String s = getLiquidXPType();
            return s.substring(0, s.indexOf(":"));
    }

    public static void setupPlatformConfig(){
    if (Platform.isModLoaded("forgeconfigapiport") || Platform.isForgeLike()) ForgeConfigCompat.setupPlatformConfig();
    else BetterFurnacesReforged.LOGGER.warn("Currently ForgeConfigApiPort isn't installed, to change BetterFurnaces options, please consider installing it!");
    }



    public static Supplier<Boolean> checkUpdates = ()-> true;
    public static Supplier<Integer> cacheCapacity = ()-> 10;
    public static Supplier<Integer> furnaceXPDropValue =()-> 1;
    public static Supplier<Integer> furnaceXPDropValue2 =()-> 100000;

    public static Supplier<Boolean> checkCommonOresName =()-> false;

    public static Supplier<Boolean> checkRawOresName =()-> false;

    public static Supplier<Boolean> enableUltimateFurnaces =()-> true;
    public static Supplier<Boolean> enableJeiPlugin =()-> true;

    public static Supplier<Boolean> enableJeiCatalysts =()-> true;

    public static Supplier<Boolean> enableJeiClickArea =()-> true;

    public static Supplier<Integer> copperTierSpeed =()-> 175;
    public static Supplier<Integer> ironTierSpeed =()-> 150;
    public static Supplier<Integer> steelTierSpeed =()-> 125;
    public static Supplier<Integer> goldTierSpeed =()-> 100;
    public static Supplier<Integer> amethystTierSpeed =()-> 75;
    public static Supplier<Integer> diamondTierSpeed =()-> 50;
    public static Supplier<Integer> platinumTierSpeed =()-> 25;
    public static Supplier<Integer> netherhotTierSpeed =()-> 8;
    public static Supplier<Integer> extremeTierSpeed =()-> 4;
    public static Supplier<Integer> ultimateTierSpeed =()-> 1;
    public static Supplier<Integer> xpFluidType =()-> 0;
    public static Supplier<List<? extends String>> additionalLiquidFuels = Collections::emptyList;


}
