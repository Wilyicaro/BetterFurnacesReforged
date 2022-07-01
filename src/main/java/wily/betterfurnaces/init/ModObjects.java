package wily.betterfurnaces.init;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import wily.betterfurnaces.BetterFurnacesReforged;

@Mod.EventBusSubscriber(modid = BetterFurnacesReforged.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModObjects {

    public static final ItemGroup ITEM_GROUP = new ItemGroup(BetterFurnacesReforged.MOD_ID) {

        @Override
        public ItemStack makeIcon() {
            return new ItemStack(Registration.EXTREME_FURNACE.get());
        }
    };

    public static void init(final FMLCommonSetupEvent event) {

    }


}