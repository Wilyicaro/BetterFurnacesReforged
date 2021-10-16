package wily.ultimatefurnaces.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaRecipeCategoryUid;
import mezz.jei.api.registration.IAdvancedRegistration;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import wily.betterfurnaces.BetterFurnacesReforged;
import wily.betterfurnaces.Config;
import wily.betterfurnaces.gui.*;
import wily.ultimatefurnaces.gui.*;
import wily.ultimatefurnaces.init.Registration;

@JeiPlugin
public class UFJeiPlugin implements IModPlugin {

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(BetterFurnacesReforged.MOD_ID, "plugin_" + BetterFurnacesReforged.MOD_ID);
    }

    @Override
    public void registerAdvanced(IAdvancedRegistration registration) {

    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registry) {
        if (Config.enableJeiPlugin.get() && Config.enableJeiCatalysts.get()) {
            registry.addRecipeCatalyst(new ItemStack(Registration.COPPER_FURNACE.get()), VanillaRecipeCategoryUid.FURNACE);
            registry.addRecipeCatalyst(new ItemStack(Registration.ULTIMATE_FURNACE.get()), VanillaRecipeCategoryUid.FURNACE);
            registry.addRecipeCatalyst(new ItemStack(Registration.COPPER_FORGE.get()), VanillaRecipeCategoryUid.FURNACE);
            registry.addRecipeCatalyst(new ItemStack(Registration.IRON_FORGE.get()), VanillaRecipeCategoryUid.FURNACE);
            registry.addRecipeCatalyst(new ItemStack(Registration.GOLD_FORGE.get()), VanillaRecipeCategoryUid.FURNACE);
            registry.addRecipeCatalyst(new ItemStack(Registration.DIAMOND_FORGE.get()), VanillaRecipeCategoryUid.FURNACE);
            registry.addRecipeCatalyst(new ItemStack(Registration.NETHERHOT_FORGE.get()), VanillaRecipeCategoryUid.FURNACE);
            registry.addRecipeCatalyst(new ItemStack(Registration.ULTIMATE_FORGE.get()), VanillaRecipeCategoryUid.FURNACE);

            registry.addRecipeCatalyst(new ItemStack(Registration.COPPER_FURNACE.get()), VanillaRecipeCategoryUid.FUEL);
            registry.addRecipeCatalyst(new ItemStack(Registration.ULTIMATE_FURNACE.get()), VanillaRecipeCategoryUid.FUEL);
            registry.addRecipeCatalyst(new ItemStack(Registration.COPPER_FORGE.get()), VanillaRecipeCategoryUid.FUEL);
            registry.addRecipeCatalyst(new ItemStack(Registration.IRON_FORGE.get()), VanillaRecipeCategoryUid.FUEL);
            registry.addRecipeCatalyst(new ItemStack(Registration.GOLD_FORGE.get()), VanillaRecipeCategoryUid.FUEL);
            registry.addRecipeCatalyst(new ItemStack(Registration.DIAMOND_FORGE.get()), VanillaRecipeCategoryUid.FUEL);
            registry.addRecipeCatalyst(new ItemStack(Registration.NETHERHOT_FORGE.get()), VanillaRecipeCategoryUid.FUEL);
            registry.addRecipeCatalyst(new ItemStack(Registration.ULTIMATE_FORGE.get()), VanillaRecipeCategoryUid.FUEL);



        }
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registry) {
        if (Config.enableJeiPlugin.get() && Config.enableJeiClickArea.get()) {
            registry.addRecipeClickArea(BlockCopperFurnaceScreen.class, 79, 35, 24, 17, VanillaRecipeCategoryUid.FUEL, VanillaRecipeCategoryUid.FURNACE);
            registry.addRecipeClickArea(BlockUltimateFurnaceScreen.class, 79, 35, 24, 17, VanillaRecipeCategoryUid.FUEL, VanillaRecipeCategoryUid.FURNACE);
            registry.addRecipeClickArea(BlockCopperForgeScreen.class, 80, 80, 24, 17, VanillaRecipeCategoryUid.FUEL, VanillaRecipeCategoryUid.FURNACE);
            registry.addRecipeClickArea(BlockIronForgeScreen.class, 80, 80, 24, 17, VanillaRecipeCategoryUid.FUEL, VanillaRecipeCategoryUid.FURNACE);
            registry.addRecipeClickArea(BlockGoldForgeScreen.class, 80, 80, 24, 17, VanillaRecipeCategoryUid.FUEL, VanillaRecipeCategoryUid.FURNACE);
            registry.addRecipeClickArea(BlockDiamondForgeScreen.class, 80, 80, 24, 17, VanillaRecipeCategoryUid.FUEL, VanillaRecipeCategoryUid.FURNACE);
            registry.addRecipeClickArea(BlockNetherhotForgeScreen.class, 80, 80, 24, 17, VanillaRecipeCategoryUid.FUEL, VanillaRecipeCategoryUid.FURNACE);
            registry.addRecipeClickArea(BlockUltimateForgeScreen.class, 80, 80, 24, 17, VanillaRecipeCategoryUid.FUEL, VanillaRecipeCategoryUid.FURNACE);
        }
    }
}
