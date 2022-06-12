package wily.ultimatefurnaces.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaRecipeCategoryUid;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import wily.betterfurnaces.BetterFurnacesReforged;
import wily.betterfurnaces.Config;
import wily.betterfurnaces.items.ItemUpgradeTier;
import wily.ultimatefurnaces.gui.*;
import wily.ultimatefurnaces.init.RegistrationUF;

@JeiPlugin
public class UFJeiPlugin implements IModPlugin {

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(BetterFurnacesReforged.MOD_ID, "plugin_" + BetterFurnacesReforged.MOD_ID);
    }
    private void addDescription(IRecipeRegistration registry, ItemStack itemDefinition,
                                Component... message) {
        registry.addIngredientInfo(itemDefinition, VanillaTypes.ITEM, message);
    }
    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        ItemUpgradeTier[] up = {RegistrationUF.COPPER_UPGRADE.get(), RegistrationUF.STEEL_UPGRADE.get(),RegistrationUF.AMETHYST_UPGRADE.get(),RegistrationUF.PLATINUM_UPGRADE.get(),RegistrationUF.ULTIMATE_UPGRADE.get(), RegistrationUF.IRON_UPGRADE.get()};
        for(ItemUpgradeTier i : up)
            addDescription(registration, new ItemStack(i), new TextComponent(I18n.get("tooltip." + BetterFurnacesReforged.MOD_ID + ".upgrade.tier", i.from.getName().getString(), i.to.getName().getString())));
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registry) {
        if (Config.enableJeiPlugin.get() && Config.enableJeiCatalysts.get()) {
            registry.addRecipeCatalyst(new ItemStack(RegistrationUF.COPPER_FURNACE.get()), VanillaRecipeCategoryUid.FURNACE);
            registry.addRecipeCatalyst(new ItemStack(RegistrationUF.ULTIMATE_FURNACE.get()), VanillaRecipeCategoryUid.FURNACE);
            registry.addRecipeCatalyst(new ItemStack(RegistrationUF.COPPER_FORGE.get()), VanillaRecipeCategoryUid.FURNACE);
            registry.addRecipeCatalyst(new ItemStack(RegistrationUF.IRON_FORGE.get()), VanillaRecipeCategoryUid.FURNACE);
            registry.addRecipeCatalyst(new ItemStack(RegistrationUF.GOLD_FORGE.get()), VanillaRecipeCategoryUid.FURNACE);
            registry.addRecipeCatalyst(new ItemStack(RegistrationUF.DIAMOND_FORGE.get()), VanillaRecipeCategoryUid.FURNACE);
            registry.addRecipeCatalyst(new ItemStack(RegistrationUF.NETHERHOT_FORGE.get()), VanillaRecipeCategoryUid.FURNACE);
            registry.addRecipeCatalyst(new ItemStack(RegistrationUF.ULTIMATE_FORGE.get()), VanillaRecipeCategoryUid.FURNACE);

            registry.addRecipeCatalyst(new ItemStack(RegistrationUF.COPPER_FURNACE.get()), VanillaRecipeCategoryUid.FUEL);
            registry.addRecipeCatalyst(new ItemStack(RegistrationUF.ULTIMATE_FURNACE.get()), VanillaRecipeCategoryUid.FUEL);
            registry.addRecipeCatalyst(new ItemStack(RegistrationUF.COPPER_FORGE.get()), VanillaRecipeCategoryUid.FUEL);
            registry.addRecipeCatalyst(new ItemStack(RegistrationUF.IRON_FORGE.get()), VanillaRecipeCategoryUid.FUEL);
            registry.addRecipeCatalyst(new ItemStack(RegistrationUF.GOLD_FORGE.get()), VanillaRecipeCategoryUid.FUEL);
            registry.addRecipeCatalyst(new ItemStack(RegistrationUF.DIAMOND_FORGE.get()), VanillaRecipeCategoryUid.FUEL);
            registry.addRecipeCatalyst(new ItemStack(RegistrationUF.NETHERHOT_FORGE.get()), VanillaRecipeCategoryUid.FUEL);
            registry.addRecipeCatalyst(new ItemStack(RegistrationUF.ULTIMATE_FORGE.get()), VanillaRecipeCategoryUid.FUEL);



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
