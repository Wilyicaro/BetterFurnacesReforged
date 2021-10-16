package wily.ultimatefurnaces.init;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import wily.ultimatefurnaces.UltimateFurnaces;

@Mod.EventBusSubscriber(modid = UltimateFurnaces.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModObjects {

    public static final ItemGroup ITEM_GROUP = new ItemGroup(UltimateFurnaces.MOD_ID) {

        @Override
        public ItemStack makeIcon() {
            return new ItemStack(Registration.ULTIMATE_FURNACE.get());
        }
    };

    public static void init(final FMLCommonSetupEvent event) {

    }



}