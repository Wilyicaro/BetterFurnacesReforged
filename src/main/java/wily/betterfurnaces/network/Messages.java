package wily.betterfurnaces.network;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import wily.betterfurnaces.BetterFurnacesReforged;

public class Messages {
    public static SimpleChannel INSTANCE;

    private static int ID = 0;
    private static int nextID() {
        return ID++;
    }

    public static void registerMessages(String channelName) {
        INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(BetterFurnacesReforged.MOD_ID, channelName), () -> "1.0", s -> true, s -> true);

        // Server side

        INSTANCE.registerMessage(nextID(), PacketSettingsButton.class,
                PacketSettingsButton::toBytes,
                PacketSettingsButton::new,
                PacketSettingsButton::handle);

        INSTANCE.registerMessage(nextID(), PacketShowSettingsButton.class,
                PacketShowSettingsButton::toBytes,
                PacketShowSettingsButton::new,
                PacketShowSettingsButton::handle);
        INSTANCE.registerMessage(nextID(), PacketOrientationButton.class,
                PacketOrientationButton::toBytes,
                PacketOrientationButton::new,
                PacketOrientationButton::handle);

        INSTANCE.registerMessage(nextID(), PacketColorSlider.class,
                PacketColorSlider::toBytes,
                PacketColorSlider::new,
                PacketColorSlider::handle);

        INSTANCE.registerMessage(nextID(), PacketCobButton.class,
                PacketCobButton::toBytes,
                PacketCobButton::new,
                PacketCobButton::handle);


        // Client side
    }
}
