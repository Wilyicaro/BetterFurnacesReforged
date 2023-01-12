package wily.betterfurnaces;

import net.minecraftforge.common.config.Configuration;
import wily.betterfurnaces.gitup.UpCheck;

import java.io.File;

public class Config {
    public final Configuration cfg;

    public static boolean checkUpdates;

    public static int ironTierSpeed;

    public static int goldTierSpeed;

    public static int diamondTierSpeed;

    public static int netherhotTierSpeed;

    public static int extremeTierSpeed;




    public Config(File config){
        cfg = new Configuration(config);
        checkUpdates = cfg.getBoolean("checkUpdates", "updates", true,"true = check for updates, false = don't check for updates.");
        ironTierSpeed = cfg.getInt("ironTierSpeed", "furnaces", 150, 1, 200, "Number of ticks to complete one smelting operation.\n 200 ticks is what a regular furnace takes.");
        goldTierSpeed = cfg.getInt("goldTierSpeed", "furnaces", 100, 1, 200, "Number of ticks to complete one smelting operation.\n 200 ticks is what a regular furnace takes.");
        diamondTierSpeed = cfg.getInt("diamondTierSpeed", "furnaces", 50, 1, 200, "Number of ticks to complete one smelting operation.\n 200 ticks is what a regular furnace takes.");
        netherhotTierSpeed = cfg.getInt("netherhotTierSpeed", "furnaces", 8, 1, 200, "Number of ticks to complete one smelting operation.\n 200 ticks is what a regular furnace takes.");
        extremeTierSpeed = cfg.getInt("extremeTierSpeed", "furnaces", 4, 1, 200, "Number of ticks to complete one smelting operation.\n 200 ticks is what a regular furnace takes.");
        if (cfg.hasChanged()) cfg.save();
    }
}
