package wily.betterfurnaces;

import mezz.jei.api.*;
import mezz.jei.api.ingredients.IIngredientBlacklist;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import net.minecraft.item.ItemStack;
import wily.betterfurnaces.init.ModObjects;


@JEIPlugin
public class JeiPlugin implements IModPlugin {
    public static IJeiHelpers jeiHelper;



    public void register(IModRegistry registry) {
        registry.addRecipeCatalyst(new ItemStack(ModObjects.IRON_FURNACE), VanillaRecipeCategoryUid.SMELTING);
        registry.addRecipeCatalyst(new ItemStack(ModObjects.GOLD_FURNACE), VanillaRecipeCategoryUid.SMELTING);
        registry.addRecipeCatalyst(new ItemStack(ModObjects.DIAMOND_FURNACE), VanillaRecipeCategoryUid.SMELTING);
        registry.addRecipeCatalyst(new ItemStack(ModObjects.NETHERHOT_FURNACE), VanillaRecipeCategoryUid.SMELTING);
        registry.addRecipeCatalyst(new ItemStack(ModObjects.EXTREME_FURNACE), VanillaRecipeCategoryUid.SMELTING);
        registry.addRecipeCatalyst(new ItemStack(ModObjects.EXTREME_FORGE), VanillaRecipeCategoryUid.SMELTING);

        registry.addRecipeCatalyst(new ItemStack(ModObjects.IRON_FURNACE), VanillaRecipeCategoryUid.FUEL);
        registry.addRecipeCatalyst(new ItemStack(ModObjects.GOLD_FURNACE), VanillaRecipeCategoryUid.FUEL);
        registry.addRecipeCatalyst(new ItemStack(ModObjects.DIAMOND_FURNACE), VanillaRecipeCategoryUid.FUEL);
        registry.addRecipeCatalyst(new ItemStack(ModObjects.NETHERHOT_FURNACE), VanillaRecipeCategoryUid.FUEL);
        registry.addRecipeCatalyst(new ItemStack(ModObjects.EXTREME_FURNACE), VanillaRecipeCategoryUid.FUEL);
        registry.addRecipeCatalyst(new ItemStack(ModObjects.EXTREME_FORGE), VanillaRecipeCategoryUid.FUEL);


    }
}