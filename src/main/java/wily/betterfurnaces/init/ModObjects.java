package wily.betterfurnaces.init;

import com.google.common.eventbus.Subscribe;
import harmonised.pmmo.api.events.FurnaceBurnEvent;
import harmonised.pmmo.events.impl.FurnaceHandler;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;
import wily.betterfurnaces.BetterFurnacesReforged;
import wily.betterfurnaces.blocks.AbstractSmeltingBlock;

@Mod.EventBusSubscriber(modid = BetterFurnacesReforged.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModObjects {

    public static final CreativeModeTab ITEM_GROUP = new CreativeModeTab(BetterFurnacesReforged.MOD_ID) {

        @Override
        public ItemStack makeIcon() {
            return new ItemStack(Registration.EXTREME_FURNACE.get());
        }
    };


}