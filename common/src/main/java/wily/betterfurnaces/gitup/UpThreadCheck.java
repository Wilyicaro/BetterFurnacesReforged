package wily.betterfurnaces.gitup;

import wily.betterfurnaces.BetterFurnacesReforged;

import java.io.InputStreamReader;
import java.net.URL;
import java.util.Properties;

/**
 * Credits: pizzaatime and Ellpeck, respectively creator of the Actually Additions update checker and creator of Iron Furnaces mod.
 * https://github.com/Qelifern/IronFurnaces
 * Link to the Actually Additions repo: https://github.com/Ellpeck/ActuallyAdditions/
 * Link to the Actually Additions curse page: https://minecraft.curseforge.com/projects/actually-additions
 */
public class UpThreadCheck extends Thread {

    public UpThreadCheck() {
        this.setName("BetterFurnaces Update Checker");
        this.setDaemon(true);
        this.start();
    }

    @Override
    public void run() {
        BetterFurnacesReforged.LOGGER.info("Starting Update Check...");
        try {
            URL newestURL = new URL("https://raw.githubusercontent.com/Wilyicaro/BetterFurnacesReforged/" + BetterFurnacesReforged.MC_VERSION + "-arc/gradle.properties");
            Properties updateProperties = new Properties();
            updateProperties.load(new InputStreamReader(newestURL.openStream()));

            String currentMcVersion = BetterFurnacesReforged.MC_VERSION;
            String newestVersionProp = updateProperties.getProperty("mod_version");

            UpCheck.updateVersionInt =  Integer.parseInt(newestVersionProp.replace(".",""));
            UpCheck.updateVersionString = currentMcVersion + "-" + newestVersionProp;


            int clientVersion = Integer.parseInt(BetterFurnacesReforged.VERSION.replace(".",""));
            if (UpCheck.updateVersionInt > clientVersion) {

                UpCheck.needsUpdateNotify = true;
            }

            BetterFurnacesReforged.LOGGER.info("Update Check done!");
        } catch (Exception e) {
            BetterFurnacesReforged.LOGGER.error("Update Check failed!", e);
            UpCheck.checkFailed = true;
        }

        if (!UpCheck.checkFailed) {
            if (UpCheck.needsUpdateNotify) {
                BetterFurnacesReforged.LOGGER.info("There is an update for the BetterFurnaces mod!");
                BetterFurnacesReforged.LOGGER.info("Current Version: " + BetterFurnacesReforged.MC_VERSION + "-" + BetterFurnacesReforged.VERSION + ", newest Version: " + UpCheck.updateVersionString + "!");
                BetterFurnacesReforged.LOGGER.info("Download at " + UpCheck.DOWNLOAD_LINK);
            } else {
                BetterFurnacesReforged.LOGGER.info("Now BetterFurnaces is updated!");
            }
        }

        UpCheck.threadFinished = true;
    }

}