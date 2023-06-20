package wily.betterfurnaces;


import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import dev.architectury.injectables.annotations.ExpectPlatform;
import me.shedaniel.architectury.platform.Platform;
import net.minecraftforge.common.ForgeConfigSpec;

import java.nio.file.Path;

import static wily.betterfurnaces.Config.*;

public class ForgeConfigCompat {



    public static ForgeConfigSpec COMMON_CONFIG;
    public static ForgeConfigSpec CLIENT_CONFIG;
    public static ForgeConfigSpec SERVER_CONFIG;

    public static ForgeConfigSpec.IntValue cache_capacity;

    public static ForgeConfigSpec.IntValue furnaceXPDropValue;
    public static ForgeConfigSpec.IntValue furnaceXPDropValue2;

    public static ForgeConfigSpec.IntValue copperTierSpeed;
    public static ForgeConfigSpec.IntValue steelTierSpeed;
    public static ForgeConfigSpec.IntValue ironTierSpeed;
    public static ForgeConfigSpec.IntValue amethystTierSpeed;
    public static ForgeConfigSpec.IntValue goldTierSpeed;
    public static ForgeConfigSpec.IntValue diamondTierSpeed;
    public static ForgeConfigSpec.IntValue platinumTierSpeed;
    public static ForgeConfigSpec.IntValue netherhotTierSpeed;
    public static ForgeConfigSpec.IntValue extremeTierSpeed;
    public static ForgeConfigSpec.IntValue ultimateTierSpeed;
    public static ForgeConfigSpec.BooleanValue enableJeiPlugin;
    public static ForgeConfigSpec.BooleanValue enableJeiCatalysts;
    public static ForgeConfigSpec.BooleanValue enableJeiClickArea;

    public static ForgeConfigSpec.IntValue xpFluidType;

    public static ForgeConfigSpec.BooleanValue checkUpdates;

    public static ForgeConfigSpec.BooleanValue showErrors;

    public static ForgeConfigSpec.BooleanValue enableUltimateFurnaces;


    static {
        ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();
        ForgeConfigSpec.Builder CLIENT_BUILDER = new ForgeConfigSpec.Builder();
        ForgeConfigSpec.Builder SERVER_BUILDER = new ForgeConfigSpec.Builder();

        CLIENT_BUILDER.comment("Settings").push(CATEGORY_GENERAL);
        CLIENT_BUILDER.pop();

        CLIENT_BUILDER.comment("Furnace Settings").push(CATEGORY_FURNACE);

        setupFurnacesConfig(COMMON_BUILDER,SERVER_BUILDER);

        CLIENT_BUILDER.pop();

        CLIENT_BUILDER.comment("Additional Content").push(CATEGORY_MODDED_FURNACE);

        enableUltimateFurnaces = COMMON_BUILDER
                .comment("Enable or disable Ultimate Furnaces Addon").define("additional.enable_ultimatefurnaces", true);


        CLIENT_BUILDER.pop();

        CLIENT_BUILDER.comment("JEI Settings").push(CATEGORY_JEI);

        setupJEIConfig(COMMON_BUILDER, CLIENT_BUILDER);

        CLIENT_BUILDER.pop();


        CLIENT_BUILDER.comment("Misc").push(CATEGORY_MISC);

        showErrors = CLIENT_BUILDER
                .comment(" Show furnace settings errors in chat, used for debugging").define("misc.errors", false);


        CLIENT_BUILDER.pop();

        CLIENT_BUILDER.comment("Update Checker Settings").push(CATEGORY_UPDATES);

        checkUpdates = COMMON_BUILDER
                .comment(" true = check for updates, false = don't check for updates.\n Default: true.")
                .define("check_updates.updates", true);

        CLIENT_BUILDER.pop();

        COMMON_CONFIG = COMMON_BUILDER.build();
        CLIENT_CONFIG = CLIENT_BUILDER.build();
        SERVER_CONFIG = SERVER_BUILDER.build();
    }

    private static void setupFurnacesConfig(ForgeConfigSpec.Builder COMMON_BUILDER, ForgeConfigSpec.Builder SERVER_BUILDER) {

        cache_capacity = SERVER_BUILDER
                .comment(" The capacity of the recipe cache, higher values use more memory.\n Default: 10")
                .defineInRange("recipe_cache", 10, 1, 100);

        copperTierSpeed = SERVER_BUILDER
                .comment(" Number of ticks to complete one smelting operation.\n 200 ticks is what a regular furnace takes.\n Default: 175(Only work with Ultimate Furnaces addon)")
                .defineInRange("copper_tier.speed", 175, 2, 72000);

        ironTierSpeed = SERVER_BUILDER
                .comment(" Number of ticks to complete one smelting operation.\n 200 ticks is what a regular furnace takes.\n Default: 150")
                .defineInRange("iron_tier.speed", 150, 2, 72000);

        steelTierSpeed = SERVER_BUILDER
                .comment(" Number of ticks to complete one smelting operation.\n 200 ticks is what a regular furnace takes.\n Default: 125(Only work with Ultimate Furnaces addon)")
                .defineInRange("steel_tier.speed", 125, 2, 72000);

        goldTierSpeed = SERVER_BUILDER
                .comment(" Number of ticks to complete one smelting operation.\n 200 ticks is what a regular furnace takes.\n Default: 100")
                .defineInRange("gold_tier.speed", 100, 2, 72000);

        amethystTierSpeed = SERVER_BUILDER
                .comment(" Number of ticks to complete one smelting operation.\n 200 ticks is what a regular furnace takes.\n Default: 75(Only work with Ultimate Furnaces addon)")
                .defineInRange("amethyst_tier.speed", 75, 2, 72000);

        diamondTierSpeed = SERVER_BUILDER
                .comment(" Number of ticks to complete one smelting operation.\n 200 ticks is what a regular furnace takes.\n Default: 50")
                .defineInRange("diamond_tier.speed", 50, 2, 72000);

        platinumTierSpeed = SERVER_BUILDER
                .comment(" Number of ticks to complete one smelting operation.\n 200 ticks is what a regular furnace takes.\n Default: 25(Only work with Ultimate Furnaces addon)")
                .defineInRange("platinum_tier.speed", 25, 2, 72000);

        netherhotTierSpeed = SERVER_BUILDER
                .comment(" Number of ticks to complete one smelting operation.\n 200 ticks is what a regular furnace takes.\n Default: 8")
                .defineInRange("netherhot_tier.speed", 8, 2, 72000);

        extremeTierSpeed = SERVER_BUILDER
                .comment(" Number of ticks to complete one smelting operation.\n 200 ticks is what a regular furnace takes.\n Default: 4")
                .defineInRange("extreme_tier.speed", 4, 2, 72000);

        ultimateTierSpeed = SERVER_BUILDER
                .comment(" Number of ticks to complete one smelting operation.\n 200 ticks is what a regular furnace takes.\n Default: 1 (Only work with Ultimate Furnaces addon)")
                .defineInRange("ultimate_tier.speed", 1, 1, 72000);

        furnaceXPDropValue = SERVER_BUILDER
                .comment(" This value indicates when the furnace or forge should 'overload' and spit out the xp stored. \n Default: 10, Recipes")
                .defineInRange("furnace_xp_drop.value", 10, 1, 500);

        furnaceXPDropValue2 = SERVER_BUILDER
                .comment(" This value indicates when the furnace or forge should 'overload' and spit out the xp stored. \n Default: 100000, Single recipe uses")
                .defineInRange("furnace_xp_drop.value_two", 100000, 1, 1000000);

        xpFluidType = COMMON_BUILDER
                .comment(" Value referring to the mod used for the xp fluid generated with the Xp Tank Upgrade. \n 0 = Mob Grinding Utils(Default) \n 1 = Industrial Foregoing \n 2 = Cyclic \n 3 = Reliquary \n 4 = Kibe Utilities")
                .defineInRange("upgrade.xp_fluid_type", getDefaultLiquidXpMod(), 0, supportedLiquidXps.size() - 1);
    }

    public static int getDefaultLiquidXpMod(){
        for (String s : supportedLiquidXps){
            if (Platform.isModLoaded(s.substring(0, s.indexOf(":")))) return supportedLiquidXps.indexOf(s);
        }
        return 0;
    }


    private static void setupJEIConfig(ForgeConfigSpec.Builder COMMON_BUILDER, ForgeConfigSpec.Builder CLIENT_BUILDER) {

        enableJeiPlugin = COMMON_BUILDER
                .comment(" Enable or disable the JeiPlugin of BetterFurnaces.").define("jei.enable_jei", true);

        enableJeiCatalysts = COMMON_BUILDER
                .comment(" Enable or disable the Catalysts in Jei for BetterFurnaces.").define("jei.enable_jei_catalysts", true);

        enableJeiClickArea = CLIENT_BUILDER
                .comment(" Enable or disable the Click Area inside the GUI in all of BetterFurnaces furnaces and forges.").define("jei.enable_jei_click_area", true);

    }



    public static void loadConfig(ForgeConfigSpec spec, Path path) {
        BetterFurnacesReforged.LOGGER.debug("Loading config file {}", path);

        final CommentedFileConfig configData = CommentedFileConfig.builder(path)
                .sync()
                .autosave()
                .writingMode(WritingMode.REPLACE)
                .build();

        BetterFurnacesReforged.LOGGER.debug("Built TOML config for {}", path.toString());
        configData.load();
        BetterFurnacesReforged.LOGGER.debug("Loaded TOML config file {}", path.toString());
        spec.setConfig(configData);
    }



    public static void loadAllSyncConfigs() {
        loadConfig(CLIENT_CONFIG, BetterFurnacesPlatform.getConfigDirectory().resolve(BetterFurnacesReforged.MOD_ID + "-client.toml"));
        loadConfig(COMMON_CONFIG, BetterFurnacesPlatform.getConfigDirectory().resolve(BetterFurnacesReforged.MOD_ID + "-common.toml"));
    }
    public static void onServerConfigLoad(){
        Config.cacheCapacity = cache_capacity::get;
        Config.copperTierSpeed = copperTierSpeed::get;
        Config.ironTierSpeed = ironTierSpeed::get;
        Config.steelTierSpeed = steelTierSpeed::get;
        Config.goldTierSpeed = goldTierSpeed::get;
        Config.amethystTierSpeed = amethystTierSpeed::get;
        Config.diamondTierSpeed = diamondTierSpeed::get;
        Config.platinumTierSpeed = platinumTierSpeed::get;
        Config.netherhotTierSpeed = netherhotTierSpeed::get;
        Config.extremeTierSpeed = extremeTierSpeed::get;
        Config.ultimateTierSpeed = ultimateTierSpeed::get;

        Config.furnaceXPDropValue = furnaceXPDropValue::get;
        Config.furnaceXPDropValue2 = furnaceXPDropValue2::get;
        Config.xpFluidType = xpFluidType::get;
    }

    @ExpectPlatform
    public static void registerConfig() {
        throw new AssertionError();
    }
    public static void setupPlatformConfig(){
        registerConfig();
        loadAllSyncConfigs();
        Config.checkUpdates = checkUpdates::get;
        Config.enableUltimateFurnaces = enableUltimateFurnaces::get;

        Config.enableJeiPlugin = enableJeiPlugin::get;
        Config.enableJeiCatalysts = enableJeiCatalysts::get;
        Config.enableJeiClickArea = enableJeiClickArea::get;
    }

}
