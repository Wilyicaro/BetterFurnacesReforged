

package wily.betterfurnaces.jei;


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
import wily.betterfurnaces.init.Registration;
import wily.betterfurnaces.items.ItemUpgradeTier;

@JeiPlugin
public class BfJeiPlugin implements IModPlugin {

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
			registry.addRecipeCatalyst(new ItemStack(Registration.IRON_FURNACE.get()), VanillaRecipeCategoryUid.FURNACE);
			registry.addRecipeCatalyst(new ItemStack(Registration.GOLD_FURNACE.get()), VanillaRecipeCategoryUid.FURNACE);
			registry.addRecipeCatalyst(new ItemStack(Registration.DIAMOND_FURNACE.get()), VanillaRecipeCategoryUid.FURNACE);
			registry.addRecipeCatalyst(new ItemStack(Registration.NETHERHOT_FURNACE.get()), VanillaRecipeCategoryUid.FURNACE);
			registry.addRecipeCatalyst(new ItemStack(Registration.EXTREME_FURNACE.get()), VanillaRecipeCategoryUid.FURNACE);
			registry.addRecipeCatalyst(new ItemStack(Registration.EXTREME_FORGE.get()), VanillaRecipeCategoryUid.FURNACE);

			registry.addRecipeCatalyst(new ItemStack(Registration.IRON_FURNACE.get()), VanillaRecipeCategoryUid.FUEL);
			registry.addRecipeCatalyst(new ItemStack(Registration.GOLD_FURNACE.get()), VanillaRecipeCategoryUid.FUEL);
			registry.addRecipeCatalyst(new ItemStack(Registration.DIAMOND_FURNACE.get()), VanillaRecipeCategoryUid.FUEL);
			registry.addRecipeCatalyst(new ItemStack(Registration.NETHERHOT_FURNACE.get()), VanillaRecipeCategoryUid.FUEL);
			registry.addRecipeCatalyst(new ItemStack(Registration.EXTREME_FURNACE.get()), VanillaRecipeCategoryUid.FUEL);
			registry.addRecipeCatalyst(new ItemStack(Registration.EXTREME_FORGE.get()), VanillaRecipeCategoryUid.FUEL);



		}
	}
	private void addDescription(IRecipeRegistration registry, ItemStack itemDefinition,
								ITextComponent... message) {
		registry.addIngredientInfo(itemDefinition, VanillaTypes.ITEM, message);
	}
	@Override
	public void registerRecipes(IRecipeRegistration registration) {
		ItemUpgradeTier[] up = {Registration.IRON_UPGRADE.get(), Registration.GOLD_UPGRADE.get(),Registration.DIAMOND_UPGRADE.get(),Registration.NETHERHOT_UPGRADE.get(),Registration.EXTREME_UPGRADE.get()};
		addDescription(registration, new ItemStack(Registration.COBBLESTONE_GENERATOR.get()), new TranslationTextComponent("description." + BetterFurnacesReforged.MOD_ID + ".cobblestone_generator"));
		for(ItemUpgradeTier i : up)
		addDescription(registration, new ItemStack(i), new StringTextComponent(I18n.get("tooltip." + BetterFurnacesReforged.MOD_ID + ".upgrade.tier", i.from.getName().getString(), i.to.getName().getString())));
	}
	@Override
	public void registerGuiHandlers(IGuiHandlerRegistration registry) {
		if (Config.enableJeiPlugin.get() && Config.enableJeiClickArea.get()) {
			registry.addRecipeClickArea(BlockIronFurnaceScreen.class, 79, 35, 24, 17, VanillaRecipeCategoryUid.FUEL, VanillaRecipeCategoryUid.FURNACE);
			registry.addRecipeClickArea(BlockGoldFurnaceScreen.class, 79, 35, 24, 17, VanillaRecipeCategoryUid.FUEL, VanillaRecipeCategoryUid.FURNACE);
			registry.addRecipeClickArea(BlockDiamondFurnaceScreen.class, 79, 35, 24, 17, VanillaRecipeCategoryUid.FUEL, VanillaRecipeCategoryUid.FURNACE);
			registry.addRecipeClickArea(BlockExtremeForgeScreen.class, 80, 80, 24, 17, VanillaRecipeCategoryUid.FUEL, VanillaRecipeCategoryUid.FURNACE);
			registry.addRecipeClickArea(BlockNetherhotFurnaceScreen.class, 79, 35, 24, 17, VanillaRecipeCategoryUid.FUEL, VanillaRecipeCategoryUid.FURNACE);
			registry.addRecipeClickArea(BlockExtremeFurnaceScreen.class, 79, 35, 24, 17, VanillaRecipeCategoryUid.FUEL, VanillaRecipeCategoryUid.FURNACE);

		}
	}

}




