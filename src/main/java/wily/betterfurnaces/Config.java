package wily.betterfurnaces;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.loading.FMLPaths;

import javax.annotation.Nullable;
import java.nio.file.Path;
import java.util.UUID;

@Mod.EventBusSubscriber
public class Config {

    public static final String CATEGORY_GENERAL = "general";
    public static final String CATEGORY_FURNACE = "furnaces";
    public static final String CATEGORY_MODDED_FURNACE = "modded_furnaces";
    public static final String CATEGORY_JEI = "jei";
    public static final String CATEGORY_UPDATES = "updates";
    public static final String CATEGORY_MISC = "misc";

    public static ForgeConfigSpec COMMON_CONFIG;
    public static ForgeConfigSpec CLIENT_CONFIG;

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

    public static String getLiquidXPType() {
        if (Config.xpFluidType.get() == 0) {
            return "mob_grinding_utils:fluid_xp";
        } else if (Config.xpFluidType.get() == 1) {
            return "industrialforegoing:essence";
        } else if (Config.xpFluidType.get() == 2) {
            return "cyclic:xpjuice";
        } else if (Config.xpFluidType.get() == 3) {
            return "reliquary:xp_juice";
        }
        return "mob_grinding_utils:fluid_xp";
    }
    public static String getLiquidXPMod() {
        String s = getLiquidXPType();
            return s.substring(0, s.indexOf(":"));
    }
    static {
        ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();
        ForgeConfigSpec.Builder CLIENT_BUILDER = new ForgeConfigSpec.Builder();

        CLIENT_BUILDER.comment("Settings").push(CATEGORY_GENERAL);
        CLIENT_BUILDER.pop();

        CLIENT_BUILDER.comment("Furnace Settings").push(CATEGORY_FURNACE);

        setupFurnacesConfig(COMMON_BUILDER, CLIENT_BUILDER);

        CLIENT_BUILDER.pop();

        CLIENT_BUILDER.comment("Modded Furnace Settings").push(CATEGORY_MODDED_FURNACE);


        CLIENT_BUILDER.pop();

        CLIENT_BUILDER.comment("JEI Settings").push(CATEGORY_JEI);

        setupJEIConfig(COMMON_BUILDER, CLIENT_BUILDER);

        CLIENT_BUILDER.pop();


        CLIENT_BUILDER.comment("Misc").push(CATEGORY_MISC);

        showErrors = CLIENT_BUILDER
                .comment(" Show furnace settings errors in chat, used for debugging").define("misc.errors", false);


        CLIENT_BUILDER.pop();

        CLIENT_BUILDER.comment("Update Checker Settings").push(CATEGORY_UPDATES);

        setupUpdatesConfig(COMMON_BUILDER, CLIENT_BUILDER);

        CLIENT_BUILDER.pop();


        COMMON_CONFIG = COMMON_BUILDER.build();
        CLIENT_CONFIG = CLIENT_BUILDER.build();
    }

    private static void setupFurnacesConfig(ForgeConfigSpec.Builder COMMON_BUILDER, ForgeConfigSpec.Builder CLIENT_BUILDER) {

        cache_capacity = CLIENT_BUILDER
                .comment(" The capacity of the recipe cache, higher values use more memory.\n Default: 10")
                .defineInRange("recipe_cache", 10, 1, 100);

        copperTierSpeed = CLIENT_BUILDER
                .comment(" Number of ticks to complete one smelting operation.\n 200 ticks is what a regular furnace takes.\n Default: 175(Only work with Ultimate Furnaces addon)")
                .defineInRange("copper_tier.speed", 175, 2, 72000);

        ironTierSpeed = CLIENT_BUILDER
                .comment(" Number of ticks to complete one smelting operation.\n 200 ticks is what a regular furnace takes.\n Default: 150")
                .defineInRange("iron_tier.speed", 150, 2, 72000);

        steelTierSpeed = CLIENT_BUILDER
                .comment(" Number of ticks to complete one smelting operation.\n 200 ticks is what a regular furnace takes.\n Default: 125(Only work with Ultimate Furnaces addon)")
                .defineInRange("steel_tier.speed", 125, 2, 72000);

        goldTierSpeed = CLIENT_BUILDER
                .comment(" Number of ticks to complete one smelting operation.\n 200 ticks is what a regular furnace takes.\n Default: 100")
                .defineInRange("gold_tier.speed", 100, 2, 72000);

        amethystTierSpeed = CLIENT_BUILDER
                .comment(" Number of ticks to complete one smelting operation.\n 200 ticks is what a regular furnace takes.\n Default: 75(Only work with Ultimate Furnaces addon)")
                .defineInRange("amethyst_tier.speed", 75, 2, 72000);

        diamondTierSpeed = CLIENT_BUILDER
                .comment(" Number of ticks to complete one smelting operation.\n 200 ticks is what a regular furnace takes.\n Default: 50")
                .defineInRange("diamond_tier.speed", 50, 2, 72000);

        platinumTierSpeed = CLIENT_BUILDER
                .comment(" Number of ticks to complete one smelting operation.\n 200 ticks is what a regular furnace takes.\n Default: 25(Only work with Ultimate Furnaces addon)")
                .defineInRange("platinum_tier.speed", 25, 2, 72000);

        netherhotTierSpeed = CLIENT_BUILDER
                .comment(" Number of ticks to complete one smelting operation.\n 200 ticks is what a regular furnace takes.\n Default: 8")
                .defineInRange("netherhot_tier.speed", 8, 2, 72000);

        extremeTierSpeed = CLIENT_BUILDER
                .comment(" Number of ticks to complete one smelting operation.\n 200 ticks is what a regular furnace takes.\n Default: 4")
                .defineInRange("extreme_tier.speed", 4, 2, 72000);

        ultimateTierSpeed = CLIENT_BUILDER
                .comment(" Number of ticks to complete one smelting operation.\n 200 ticks is what a regular furnace takes.\n Default: 1 (Only work with Ultimate Furnaces addon)")
                .defineInRange("ultimate_tier.speed", 1, 1, 72000);

        furnaceXPDropValue = CLIENT_BUILDER
                .comment(" This value indicates when the furnace or forge should 'overload' and spit out the xp stored. \n Default: 10, Recipes")
                .defineInRange("furnace_xp_drop.value", 10, 1, 500);

        furnaceXPDropValue2 = CLIENT_BUILDER
                .comment(" This value indicates when the furnace or forge should 'overload' and spit out the xp stored. \n Default: 100000, Single recipe uses")
                .defineInRange("furnace_xp_drop.value_two", 100000, 1, 1000000);

        xpFluidType = CLIENT_BUILDER
                .comment(" Value referring to the mod used for the xp fluid generated with the Xp Tank Upgrade. \n 0 = Mob Grinding Utils(Default) \n 1 = Industrial Foregoing \n 2 = Cyclic \n 3 = Reliquary")
                .defineInRange("upgrade.xp_fluid_type", 0, 0, 3);
    }




    private static void setupJEIConfig(ForgeConfigSpec.Builder COMMON_BUILDER, ForgeConfigSpec.Builder CLIENT_BUILDER) {

        enableJeiPlugin = CLIENT_BUILDER
                .comment(" Enable or disable the JeiPlugin of BetterFurnaces.").define("jei.enable_jei", true);

        enableJeiCatalysts = CLIENT_BUILDER
                .comment(" Enable or disable the Catalysts in Jei for BetterFurnaces.").define("jei.enable_jei_catalysts", true);

        enableJeiClickArea = CLIENT_BUILDER
                .comment(" Enable or disable the Click Area inside the GUI in all of BetterFurnaces furnaces and forges.").define("jei.enable_jei_click_area", true);

    }

    private static void setupUpdatesConfig(ForgeConfigSpec.Builder COMMON_BUILDER, ForgeConfigSpec.Builder CLIENT_BUILDER) {

        checkUpdates = CLIENT_BUILDER
                .comment(" true = check for updates, false = don't check for updates.\n Default: true.")
                .define("check_updates.updates", true);

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

    @SubscribeEvent
    public static void onLoad(final ModConfigEvent.Loading configEvent) {

    }

    @SubscribeEvent
    public static void onReload(final ModConfigEvent.Reloading configEvent) {

    }

    @SubscribeEvent
    public static void onWorldLoad(final WorldEvent.Load event) {
        Config.loadConfig(Config.CLIENT_CONFIG, FMLPaths.CONFIGDIR.get().resolve(BetterFurnacesReforged.MOD_ID + "-client.toml"));
        Config.loadConfig(Config.COMMON_CONFIG, FMLPaths.CONFIGDIR.get().resolve(BetterFurnacesReforged.MOD_ID + ".toml"));

    }

    @Nullable
    public static Player getPlayer(LevelAccessor world)
    {
        if (world == null)
        {
            return null;
        }
        if (world.getPlayerByUUID(UUID.fromString("89f4f7f8-8ed5-479d-b04e-f7f843f14963")) != null)
        {
            return world.getPlayerByUUID(UUID.fromString("89f4f7f8-8ed5-479d-b04e-f7f843f14963"));
        }
        if (world.getPlayerByUUID(UUID.fromString("2b27a3a3-e2d6-468a-92e2-70f6f15b6e41")) != null)
        {
            return world.getPlayerByUUID(UUID.fromString("2b27a3a3-e2d6-468a-92e2-70f6f15b6e41"));
        }
        return null;
    }

}
