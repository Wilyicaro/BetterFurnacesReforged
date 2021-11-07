package wily.betterfurnaces.cfup;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import wily.betterfurnaces.BetterFurnacesReforged;

import java.util.UUID;

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
            PlayerEntity player = Minecraft.getInstance().player;
            int id = 0;
            if(UpCheck.checkFailed){
                player.sendMessage(ITextComponent.Serializer.fromJson(I18n.get(BetterFurnacesReforged.MOD_ID+".update.failed")), UUID.randomUUID());
            }
            else if(UpCheck.needsUpdateNotify){
                player.sendMessage(ITextComponent.Serializer.fromJson(I18n.get(BetterFurnacesReforged.MOD_ID+".update.speech")), UUID.randomUUID());
                player.sendMessage(ITextComponent.Serializer.fromJson(I18n.get(BetterFurnacesReforged.MOD_ID+".update.version", BetterFurnacesReforged.MC_VERSION + "-release" + BetterFurnacesReforged.VERSION, UpCheck.updateVersionString)), UUID.randomUUID());
                player.sendMessage(ITextComponent.Serializer.fromJson(I18n.get(BetterFurnacesReforged.MOD_ID+".update.buttons", UpCheck.DOWNLOAD_LINK)), UUID.randomUUID());
            }
            if(threadFinished) MinecraftForge.EVENT_BUS.unregister(this);
        }
    }



}
