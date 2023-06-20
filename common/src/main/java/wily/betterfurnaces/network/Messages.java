package wily.betterfurnaces.network;

import me.shedaniel.architectury.networking.NetworkChannel;
import net.minecraft.resources.ResourceLocation;
import wily.betterfurnaces.BetterFurnacesReforged;

public class Messages {
    public static NetworkChannel INSTANCE;

    private static int ID = 0;
    private static int nextID() {
        return ID++;
    }

    public static void registerMessages(String channelName) {
        INSTANCE = NetworkChannel.create(new ResourceLocation(BetterFurnacesReforged.MOD_ID, channelName));

        // Server side

        INSTANCE.register(PacketSettingsButton.class,
                PacketSettingsButton::toBytes,
                PacketSettingsButton::new,
                PacketSettingsButton::handle);

        INSTANCE.register( PacketShowSettingsButton.class,
                PacketShowSettingsButton::toBytes,
                PacketShowSettingsButton::new,
                PacketShowSettingsButton::handle);

        INSTANCE.register( PacketCobblestoneRecipeUpdate.class,
                PacketCobblestoneRecipeUpdate::toBytes,
                PacketCobblestoneRecipeUpdate::new,
                PacketCobblestoneRecipeUpdate::handle);

        INSTANCE.register( PacketOrientationButton.class,
                PacketOrientationButton::toBytes,
                PacketOrientationButton::new,
                PacketOrientationButton::handle);

        INSTANCE.register( PacketColorSlider.class,
                PacketColorSlider::toBytes,
                PacketColorSlider::new,
                PacketColorSlider::handle);

        // Client side

        INSTANCE.register( PacketSyncFluid.class,
                PacketSyncFluid::encode,
                PacketSyncFluid::new,
                PacketSyncFluid::apply);
        INSTANCE.register( PacketSyncEnergy.class,
                PacketSyncEnergy::encode,
                PacketSyncEnergy::new,
                PacketSyncEnergy::apply);
    }
}
