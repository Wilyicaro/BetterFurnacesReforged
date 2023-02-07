package wily.betterfurnaces.gitup;

import dev.architectury.event.events.client.ClientTickEvent;
import dev.architectury.platform.Platform;
import net.fabricmc.api.EnvType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import wily.betterfurnaces.BetterFurnacesReforged;


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
        if (Platform.getEnv().equals(EnvType.CLIENT)) {
            ClientTickEvent.CLIENT_PRE.register(new ClientTickEvent.Client() {
                @Override
                public void tick(Minecraft instance) {
                    if(instance.player != null){
                        Player player = Minecraft.getInstance().player;
                        if(UpCheck.checkFailed){
                            player.displayClientMessage(Component.Serializer.fromJson(I18n.get(BetterFurnacesReforged.MOD_ID+".update.failed")), false);
                        }
                        else if(UpCheck.needsUpdateNotify){
                            player.displayClientMessage(Component.Serializer.fromJson(I18n.get(BetterFurnacesReforged.MOD_ID+".update.speech")), false);
                            player.displayClientMessage(Component.Serializer.fromJson(I18n.get(BetterFurnacesReforged.MOD_ID+".update.version",  BetterFurnacesReforged.MC_VERSION + "-" + BetterFurnacesReforged.VERSION, UpCheck.updateVersionString)), false);
                            player.displayClientMessage(Component.Serializer.fromJson(I18n.get(BetterFurnacesReforged.MOD_ID+".update.buttons", UpCheck.DOWNLOAD_LINK)), false);
                        }

                        if(threadFinished) ClientTickEvent.CLIENT_PRE.unregister(this);
                    }
                }
            });
        }
    }


    }




