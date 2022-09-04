package wily.betterfurnaces.gitup;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import wily.betterfurnaces.BetterFurnacesReforged;

@Mod.EventBusSubscriber
public class UpCheck {

    public static final String DOWNLOAD_LINK = "https://www.curseforge.com/minecraft/mc-mods/better-furnaces-reforged/";
    public static boolean checkFailed;
    public static boolean needsUpdateNotify;
    public static int updateVersionInt;
    public static String updateVersionString;
    public static boolean threadFinished = false;

    public UpCheck(){
        BetterFurnacesReforged.LOGGER.info("Initializing Update Checker...");
        new UpThreadCheck();
        MinecraftForge.EVENT_BUS.register(this);
    }


    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent(receiveCanceled = true)
    public void onTick(TickEvent.ClientTickEvent event) {

        if(Minecraft.getInstance().player != null){
            Player player = Minecraft.getInstance().player;
            int id = 0;
            if(UpCheck.checkFailed){
                player.displayClientMessage(Component.Serializer.fromJson(I18n.get(BetterFurnacesReforged.MOD_ID+".update.failed")), false);
            }
            else if(UpCheck.needsUpdateNotify){
                player.displayClientMessage(Component.Serializer.fromJson(I18n.get(BetterFurnacesReforged.MOD_ID+".update.speech")), false);
                player.displayClientMessage(Component.Serializer.fromJson(I18n.get(BetterFurnacesReforged.MOD_ID+".update.version",  BetterFurnacesReforged.MC_VERSION + "-" + BetterFurnacesReforged.VERSION, UpCheck.updateVersionString)), false);
                player.displayClientMessage(Component.Serializer.fromJson(I18n.get(BetterFurnacesReforged.MOD_ID+".update.buttons", UpCheck.DOWNLOAD_LINK)), false);
            }
            if(threadFinished) MinecraftForge.EVENT_BUS.unregister(this);
        }
    }



}
