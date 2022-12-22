package wily.betterfurnaces.forge;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.MissingMappingsEvent;
import wily.betterfurnaces.BetterFurnacesReforged;
import wily.betterfurnaces.init.Registration;
import wily.ultimatefurnaces.init.RegistrationUF;

@Mod.EventBusSubscriber(modid = BetterFurnacesReforged.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModEvents {
    @SubscribeEvent
    public static void missingMappingEvent(MissingMappingsEvent event){
        event.getMappings(Registries.BLOCK,"ultimatefurnaces_bfr").forEach((m)-> m.remap(ForgeRegistries.BLOCKS.getValue( new ResourceLocation(BetterFurnacesReforged.MOD_ID, m.getKey().getPath()))));
        event.getMappings(Registries.ITEM,"ultimatefurnaces_bfr").forEach((m)-> {
            m.remap(ForgeRegistries.ITEMS.getValue(new ResourceLocation(BetterFurnacesReforged.MOD_ID, m.getKey().getPath())));
            if (m.getKey().equals(Registration.IRON_UPGRADE.getId())) m.remap(RegistrationUF.IRON_UPGRADE.get());
            if (m.getKey().equals(Registration.GOLD_UPGRADE.getId())) m.remap(RegistrationUF.GOLD_UPGRADE.get());
            if (m.getKey().equals(Registration.DIAMOND_UPGRADE.getId())) m.remap(RegistrationUF.DIAMOND_UPGRADE.get());
            if (m.getKey().equals(Registration.NETHERHOT_UPGRADE.getId())) m.remap(RegistrationUF.NETHERHOT_UPGRADE.get());
        }
        );

    }

}
