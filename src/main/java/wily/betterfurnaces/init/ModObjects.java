package wily.betterfurnaces.init;

import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;
import wily.betterfurnaces.BetterFurnacesReforged;

import java.util.Comparator;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(modid = BetterFurnacesReforged.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModObjects {

    public static final ItemGroup ITEM_GROUP = new ItemGroup(BetterFurnacesReforged.MOD_ID) {

        @Override
        public ItemStack makeIcon() {
            return new ItemStack(Registration.EXTREME_FURNACE.get());
        }
        //@Override
        //@OnlyIn(Dist.CLIENT)
        //public void fillItemList(NonNullList<ItemStack> p_78018_1_) {
        //    for(Item item : ForgeRegistries.ITEMS) {
        //        p_78018_1_.sort((o1, o2) -> {
        //            if (o1.getItem() instanceof BlockItem && !(o2.getItem() instanceof BlockItem)) return  -1;
        //            else return 1;
        //        });
        //        item.fillItemCategory(this, p_78018_1_);
        //    }
        //}
    };

    public static void init(final FMLCommonSetupEvent event) {

    }


}