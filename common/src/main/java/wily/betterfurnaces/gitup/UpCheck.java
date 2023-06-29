package wily.betterfurnaces.gitup;

import me.shedaniel.architectury.utils.Env;
import me.shedaniel.architectury.utils.EnvExecutor;
import wily.betterfurnaces.BetterFurnacesReforged;
import wily.betterfurnaces.client.ClientSide;


public class UpCheck {

    public static final String DOWNLOAD_LINK = "https://www.curseforge.com/minecraft/mc-mods/better-furnaces-reforged/";
    public static boolean checkFailed;
    public static boolean needsUpdateNotify;
    public static int updateVersionInt;
    public static String updateVersionString;
    public static boolean threadFinished = false;

    public UpCheck() {
        BetterFurnacesReforged.LOGGER.info("Initializing Update Checker...");
        new UpThreadCheck();
        EnvExecutor.runInEnv(Env.CLIENT,()-> ClientSide::updateClientTick);
    }


}




