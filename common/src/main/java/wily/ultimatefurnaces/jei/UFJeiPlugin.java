package wily.ultimatefurnaces.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import wily.betterfurnaces.BetterFurnacesReforged;
import wily.betterfurnaces.Config;
import wily.betterfurnaces.items.TierUpgradeItem;
import wily.ultimatefurnaces.init.RegistrationUF;
import wily.ultimatefurnaces.screens.*;

@JeiPlugin
public class UFJeiPlugin implements IModPlugin {

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(BetterFurnacesReforged.MOD_ID, "plugin_" + BetterFurnacesReforged.MOD_ID + "_ultimatefurnaces");
    }
    private void addDescription(IRecipeRegistration registry, ItemStack itemDefinition,
                                Component... message) {
        registry.addIngredientInfo(itemDefinition, VanillaTypes.ITEM_STACK, message);
    }
    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        if (Config.enableJeiPlugin && Config.enableUltimateFurnaces) {
            TierUpgradeItem[] up = {RegistrationUF.COPPER_UPGRADE.get(), RegistrationUF.STEEL_UPGRADE.get(), RegistrationUF.AMETHYST_UPGRADE.get(), RegistrationUF.PLATINUM_UPGRADE.get(), RegistrationUF.ULTIMATE_UPGRADE.get(), RegistrationUF.IRON_UPGRADE.get()};
            for (TierUpgradeItem i : up)
                addDescription(registration, new ItemStack(i), Component.literal(I18n.get("tooltip." + BetterFurnacesReforged.MOD_ID + ".upgrade.tier", i.from.getName().getString(), i.to.getName().getString())));
        }
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registry) {
        if (Config.enableJeiPlugin && Config.enableJeiCatalysts && Config.enableUltimateFurnaces) {
            Block[] blocks = new Block[]{RegistrationUF.COPPER_FURNACE.get(), RegistrationUF.AMETHYST_FURNACE.get(),RegistrationUF.AMETHYST_FURNACE.get(),RegistrationUF.PLATINUM_FURNACE.get(), RegistrationUF.ULTIMATE_FURNACE.get(), RegistrationUF.COPPER_FORGE.get(), RegistrationUF.IRON_FORGE.get(), RegistrationUF.GOLD_FORGE.get(), RegistrationUF.DIAMOND_FORGE.get(), RegistrationUF.NETHERHOT_FORGE.get(), RegistrationUF.ULTIMATE_FORGE.get()};
            for(Block i : blocks) {
                ItemStack smelting = new ItemStack(i);
                registry.addRecipeCatalyst(smelting, RecipeTypes.SMELTING);
                registry.addRecipeCatalyst(smelting, RecipeTypes.FUELING);
                ItemStack blasting = smelting.copy();
                blasting.getOrCreateTag().putInt("type", 1);
                registry.addRecipeCatalyst(blasting, RecipeTypes.BLASTING);
                ItemStack smoking = smelting.copy();
                smoking.getOrCreateTag().putInt("type", 2);
                registry.addRecipeCatalyst(smoking, RecipeTypes.SMOKING);
            }

        }
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registry) {
        if (Config.enableJeiPlugin && Config.enableJeiClickArea && Config.enableUltimateFurnaces) {
            registry.addRecipeClickArea(CopperFurnaceScreen.class, 79, 35, 24, 17, RecipeTypes.SMELTING, RecipeTypes.FUELING);
            registry.addRecipeClickArea(SteelFurnaceScreen.class, 79, 35, 24, 17, RecipeTypes.SMELTING, RecipeTypes.FUELING);
            registry.addRecipeClickArea(AmethystFurnaceScreen.class, 79, 35, 24, 17, RecipeTypes.SMELTING, RecipeTypes.FUELING);
            registry.addRecipeClickArea(PlatinumFurnaceScreen.class, 79, 35, 24, 17, RecipeTypes.SMELTING, RecipeTypes.FUELING);
            registry.addRecipeClickArea(UltimateFurnaceScreen.class, 79, 35, 24, 17, RecipeTypes.SMELTING, RecipeTypes.FUELING);
            registry.addRecipeClickArea(CopperForgeScreen.class, 80, 80, 24, 17, RecipeTypes.SMELTING, RecipeTypes.FUELING);
            registry.addRecipeClickArea(IronForgeScreen.class, 80, 80, 24, 17, RecipeTypes.SMELTING, RecipeTypes.FUELING);
            registry.addRecipeClickArea(GoldForgeScreen.class, 80, 80, 24, 17, RecipeTypes.SMELTING, RecipeTypes.FUELING);
            registry.addRecipeClickArea(DiamondForgeScreen.class, 80, 80, 24, 17, RecipeTypes.SMELTING, RecipeTypes.FUELING);
            registry.addRecipeClickArea(NetherhotForgeScreen.class, 80, 80, 24, 17, RecipeTypes.SMELTING, RecipeTypes.FUELING);
            registry.addRecipeClickArea(UltimateForgeScreen.class, 80, 80, 24, 17, RecipeTypes.SMELTING, RecipeTypes.FUELING);
        }
    }
}
