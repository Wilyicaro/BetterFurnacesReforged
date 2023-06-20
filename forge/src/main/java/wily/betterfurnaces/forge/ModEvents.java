package wily.betterfurnaces.forge;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import wily.betterfurnaces.BetterFurnacesReforged;
import wily.betterfurnaces.init.Registration;
import wily.ultimatefurnaces.init.RegistrationUF;

@Mod.EventBusSubscriber(modid = BetterFurnacesReforged.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModEvents {
    @SubscribeEvent
    public static void missingBlockMappingEvent(RegistryEvent.MissingMappings<Block> event) {
        event.getMappings("ultimatefurnaces_bfr").forEach((m) -> m.remap(ForgeRegistries.BLOCKS.getValue(new ResourceLocation(BetterFurnacesReforged.MOD_ID, m.key.getPath()))));

    }

    @SubscribeEvent
    public static void missingItemMappingEvent(RegistryEvent.MissingMappings<Item> event) {
        event.getMappings( "ultimatefurnaces_bfr").forEach((m) -> {
                    m.remap(ForgeRegistries.ITEMS.getValue(new ResourceLocation(BetterFurnacesReforged.MOD_ID, m.key.getPath())));
                    if (m.key.equals(Registration.IRON_UPGRADE.getId())) m.remap(RegistrationUF.IRON_UPGRADE.get());
                    if (m.key.equals(Registration.GOLD_UPGRADE.getId())) m.remap(RegistrationUF.GOLD_UPGRADE.get());
                    if (m.key.equals(Registration.DIAMOND_UPGRADE.getId())) m.remap(RegistrationUF.DIAMOND_UPGRADE.get());
                    if (m.key.equals(Registration.NETHERHOT_UPGRADE.getId()))
                        m.remap(RegistrationUF.NETHERHOT_UPGRADE.get());
                }
        );
    }
}



