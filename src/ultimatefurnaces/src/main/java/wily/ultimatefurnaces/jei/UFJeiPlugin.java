package wily.ultimatefurnaces.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaRecipeCategoryUid;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
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
                                ITextComponent... message) {
        registry.addIngredientInfo(itemDefinition, VanillaTypes.ITEM, message);
    }
    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        addDescription(registration, new ItemStack(RegistrationUF.ULTIMATE_UPGRADE.get()), new TranslationTextComponent("tooltip." + BetterFurnacesReforged.MOD_ID + ".upgrade.ultore"), new TranslationTextComponent("tooltip." + BetterFurnacesReforged.MOD_ID + ".upgrade.unbreakable"));
        ItemUpgradeTier[] up = {RegistrationUF.COPPER_UPGRADE.get(), RegistrationUF.IRON_UPGRADE.get(),RegistrationUF.STEEL_UPGRADE.get(), RegistrationUF.GOLD_UPGRADE.get(), RegistrationUF.AMETHYST_UPGRADE.get(), RegistrationUF.DIAMOND_UPGRADE.get(),RegistrationUF.PLATINUM_UPGRADE.get(), RegistrationUF.NETHERHOT_UPGRADE.get(),RegistrationUF.ULTIMATE_UPGRADE.get(), RegistrationUF.IRON_UPGRADE.get()};
        for(ItemUpgradeTier i : up)
            addDescription(registration, new ItemStack(i), new StringTextComponent(I18n.get("tooltip." + BetterFurnacesReforged.MOD_ID + ".upgrade.tier", i.from.getName().getString(), i.to.getName().getString())));
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registry) {
        if (Config.enableJeiPlugin.get() && Config.enableJeiCatalysts.get()) {
            Block[] furnace = {RegistrationUF.COPPER_FURNACE.get(),RegistrationUF.STEEL_FURNACE.get(),RegistrationUF.AMETHYST_FURNACE.get(),RegistrationUF.PLATINUM_FURNACE.get(),RegistrationUF.ULTIMATE_FURNACE.get(),RegistrationUF.COPPER_FORGE.get(), RegistrationUF.IRON_FORGE.get(), RegistrationUF.GOLD_FORGE.get(), RegistrationUF.DIAMOND_FORGE.get() ,RegistrationUF.NETHERHOT_FORGE.get() ,RegistrationUF.ULTIMATE_FORGE.get()};
            for (Block b : furnace) {
                ItemStack smelting = new ItemStack(b);
                registry.addRecipeCatalyst(smelting, VanillaRecipeCategoryUid.FURNACE);
                registry.addRecipeCatalyst(smelting, VanillaRecipeCategoryUid.FUEL);

                ItemStack blasting = smelting.copy();
                blasting.getOrCreateTag().putInt("type", 1);
                registry.addRecipeCatalyst(blasting, VanillaRecipeCategoryUid.BLASTING);

                ItemStack smoking = smelting.copy();
                smoking.getOrCreateTag().putInt("type", 2);
                registry.addRecipeCatalyst(smoking, VanillaRecipeCategoryUid.SMOKING);
            }


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
