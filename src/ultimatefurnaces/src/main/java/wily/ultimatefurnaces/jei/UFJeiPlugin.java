package wily.ultimatefurnaces.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaRecipeCategoryUid;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.IAdvancedRegistration;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import wily.betterfurnaces.BetterFurnacesReforged;
import wily.betterfurnaces.Config;
import wily.betterfurnaces.gui.*;
import wily.betterfurnaces.items.ItemUpgradeTier;
import wily.ultimatefurnaces.gui.*;
import wily.ultimatefurnaces.init.Registration;

@JeiPlugin
public class UFJeiPlugin implements IModPlugin {

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(BetterFurnacesReforged.MOD_ID, "plugin_" + BetterFurnacesReforged.MOD_ID);
    }
    private void addDescription(IRecipeRegistration registry, ItemStack itemDefinition,
                                ITextComponent... message) {
        registry.addIngredientInfo(itemDefinition, VanillaTypes.ITEM, message);
    }
    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        addDescription(registration, new ItemStack(Registration.ULTIMATE_UPGRADE.get()), new TranslationTextComponent("tooltip." + BetterFurnacesReforged.MOD_ID + ".upgrade.ultore"), new TranslationTextComponent("tooltip." + BetterFurnacesReforged.MOD_ID + ".upgrade.unbreakable"));
        ItemUpgradeTier[] up = {Registration.COPPER_UPGRADE.get(), Registration.ULTIMATE_UPGRADE.get(), Registration.IRON_UPGRADE.get()};
        for(ItemUpgradeTier i : up)
            addDescription(registration, new ItemStack(i), new StringTextComponent(I18n.get("tooltip." + BetterFurnacesReforged.MOD_ID + ".upgrade.tier", i.from.getName().getString(), i.to.getName().getString())));
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
