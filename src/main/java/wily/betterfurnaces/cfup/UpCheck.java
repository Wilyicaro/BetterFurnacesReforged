package wily.betterfurnaces.cfup;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import wily.betterfurnaces.BetterFurnacesReforged;


@Mod.EventBusSubscriber(value = Side.CLIENT, modid = BetterFurnacesReforged.MODID)
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

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if(Minecraft.getMinecraft().player != null){
            EntityPlayerSP player = Minecraft.getMinecraft().player;
            int id = 0;
            if(UpCheck.checkFailed){
                player.sendMessage( ITextComponent.Serializer.jsonToComponent(I18n.format("update."+ BetterFurnacesReforged.MODID+".failed")));
            }
            else if(UpCheck.needsUpdateNotify){
                player.sendMessage(ITextComponent.Serializer.jsonToComponent(I18n.format("update."+BetterFurnacesReforged.MODID+".speech")));
                player.sendMessage(ITextComponent.Serializer.jsonToComponent(I18n.format("update."+BetterFurnacesReforged.MODID+".version", BetterFurnacesReforged.MC_VERSION + "-release" + BetterFurnacesReforged.VERSION, UpCheck.updateVersionString)));
                player.sendMessage(ITextComponent.Serializer.jsonToComponent(I18n.format("update."+BetterFurnacesReforged.MODID+".buttons", UpCheck.DOWNLOAD_LINK)));
            }
            if(threadFinished) MinecraftForge.EVENT_BUS.unregister(this);
        }
    }



}
