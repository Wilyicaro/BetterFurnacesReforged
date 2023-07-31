

package wily.betterfurnaces.forge.compat;


import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.ibm.icu.impl.Pair;
import com.mojang.blaze3d.vertex.PoseStack;
import me.shedaniel.architectury.fluid.FluidStack;
import me.shedaniel.architectury.utils.Fraction;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaRecipeCategoryUid;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.registration.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluids;
import org.apache.commons.lang3.ArrayUtils;
import wily.betterfurnaces.BetterFurnacesReforged;
import wily.betterfurnaces.Config;
import wily.betterfurnaces.client.screen.CobblestoneGeneratorScreen;
import wily.betterfurnaces.client.screen.ForgeScreen;
import wily.betterfurnaces.client.screen.FurnaceScreen;
import wily.betterfurnaces.init.Registration;
import wily.betterfurnaces.items.TierUpgradeItem;
import wily.betterfurnaces.recipes.CobblestoneGeneratorRecipes;
import wily.betterfurnaces.util.FluidRenderUtil;
import wily.betterfurnaces.util.GuiUtil;
import wily.betterfurnaces.util.RecipeUtil;
import wily.factoryapi.FactoryAPIPlatform;
import wily.ultimatefurnaces.init.RegistrationUF;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Map;


@JeiPlugin
public class BfJeiPlugin implements IModPlugin {
	@Override
	public ResourceLocation getPluginUid() {
		return new ResourceLocation(BetterFurnacesReforged.MOD_ID, "_plugin");
	}
	@Override
	public void registerAdvanced(IAdvancedRegistration registration) {

	}

	@Override
	public void registerRecipeCatalysts(IRecipeCatalystRegistration registry) {
		if (Config.enableJeiCatalysts.get() && Config.enableJeiPlugin.get()) {
			registry.addRecipeCatalyst(new ItemStack(Registration.COBBLESTONE_GENERATOR.get()), CobblestoneGeneratorRecipes.UID);

			Block[] blocks = {Registration.IRON_FURNACE.get(), Registration.GOLD_FURNACE.get(), Registration.DIAMOND_FURNACE.get(), Registration.NETHERHOT_FURNACE.get(), Registration.EXTREME_FURNACE.get(), Registration.EXTREME_FORGE.get()};
			if (Config.enableUltimateFurnaces.get()) blocks = ArrayUtils.addAll(blocks, RegistrationUF.COPPER_FURNACE.get(), RegistrationUF.STEEL_FURNACE.get(),RegistrationUF.AMETHYST_FURNACE.get(),RegistrationUF.PLATINUM_FURNACE.get(), RegistrationUF.ULTIMATE_FURNACE.get(), RegistrationUF.COPPER_FORGE.get(), RegistrationUF.IRON_FORGE.get(), RegistrationUF.GOLD_FORGE.get(), RegistrationUF.DIAMOND_FORGE.get(), RegistrationUF.NETHERHOT_FORGE.get(), RegistrationUF.ULTIMATE_FORGE.get());
			for (Block i : blocks) {
				ItemStack smelting = new ItemStack(i);
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

	private void addDescription(IRecipeRegistration registry, ItemStack itemDefinition,
								Component... message) {
		registry.addIngredientInfo(itemDefinition, VanillaTypes.ITEM, message);
	}

	@Override
	public void registerCategories(IRecipeCategoryRegistration registration) {
		registration.addRecipeCategories(new CobblestoneGeneratorCategory(registration.getJeiHelpers().getGuiHelper()));
	}

	@Override
	public void registerRecipes(IRecipeRegistration registration) {
		Level world = Minecraft.getInstance().level;
		RecipeManager recipeManager = world.getRecipeManager();
		registration.addRecipes(RecipeUtil.getRecipes(recipeManager, Registration.ROCK_GENERATING_RECIPE.get()),CobblestoneGeneratorRecipes.UID);

		Registration.ITEMS.forEach((item)-> {
			if (item.get() instanceof TierUpgradeItem) {
				addDescription(registration, new ItemStack(item.get()), new TextComponent(I18n.get("tooltip." + BetterFurnacesReforged.MOD_ID + ".upgrade.tier", ((TierUpgradeItem)item.get()).from.getName().getString(),((TierUpgradeItem)item.get()).to.getName().getString())));
			}
		});
		if (Config.enableUltimateFurnaces.get()) Registration.ITEMS.forEach((item)-> {
			if (item.get() instanceof TierUpgradeItem) addDescription(registration, new ItemStack(item.get()), new TextComponent(I18n.get("tooltip." + BetterFurnacesReforged.MOD_ID + ".upgrade.tier", ((TierUpgradeItem)item.get()).from.getName().getString(),((TierUpgradeItem)item.get()).to.getName().getString())));
		});

	}

	@Override
	public void registerGuiHandlers(IGuiHandlerRegistration registry) {
		if (Config.enableJeiClickArea.get() && Config.enableJeiPlugin.get()) {
			registry.addRecipeClickArea(FurnaceScreen.class, 79, 35, 24, 17, VanillaRecipeCategoryUid.FURNACE, VanillaRecipeCategoryUid.FUEL);
			registry.addRecipeClickArea(ForgeScreen.class, 80, 80, 24, 17, VanillaRecipeCategoryUid.FURNACE, VanillaRecipeCategoryUid.FUEL);
			registry.addRecipeClickArea(CobblestoneGeneratorScreen.class, 58, 44, 17, 12, CobblestoneGeneratorRecipes.UID);
			registry.addRecipeClickArea(CobblestoneGeneratorScreen.class, 101, 44, 17, 12, CobblestoneGeneratorRecipes.UID);
		}
	}

	public static class CobblestoneGeneratorCategory implements IRecipeCategory<CobblestoneGeneratorRecipes> {
		private final Component title;
		private final IDrawable background;

		private final LoadingCache<Integer, Map.Entry<IDrawableAnimated,IDrawableAnimated>> cachedProgressAnim;

		protected final IGuiHelper guiHelper;
		public static final ResourceLocation GUI = new ResourceLocation(BetterFurnacesReforged.MOD_ID , "textures/container/cobblestone_generator_gui.png");

		public CobblestoneGeneratorCategory(IGuiHelper guiHelper) {
			this.title = Registration.COBBLESTONE_GENERATOR.get().getName();
			this.background = guiHelper.createDrawable(GUI, 46, 21, 85, 52);
			this.guiHelper = guiHelper;
			this.cachedProgressAnim = CacheBuilder.newBuilder()
					.maximumSize(25)
					.build(new CacheLoader<Integer, Map.Entry<IDrawableAnimated,IDrawableAnimated>>() {
						@Override
						public Map.Entry<IDrawableAnimated, IDrawableAnimated> load(Integer cookTime) {
							return new AbstractMap.SimpleEntry<>( guiHelper.drawableBuilder(GUI, 176, 24, 17, 12)
									.buildAnimated(cookTime, IDrawableAnimated.StartDirection.LEFT, false), guiHelper.drawableBuilder(GUI, 176, 36, 17, 12)
									.buildAnimated(cookTime, IDrawableAnimated.StartDirection.RIGHT, false));
						}
					});
		}
		@Override
		public void draw(CobblestoneGeneratorRecipes recipe,PoseStack stack, double mouseX, double mouseY) {
			GuiUtil.renderScaled(stack,  (float) recipe.duration / 20 + "s", 62, 45, 0.75f, 0x7E7E7E, false);
			FluidRenderUtil.renderTiledFluid(stack, 12, 23, 17,12, FluidStack.create(Fluids.LAVA, Fraction.ofWhole(FactoryAPIPlatform.getBucketAmount())), false);
			FluidRenderUtil.renderTiledFluid(stack, 55, 23, 17,12,FluidStack.create(Fluids.WATER, Fraction.ofWhole(FactoryAPIPlatform.getBucketAmount())), true);

			Map.Entry<IDrawableAnimated,IDrawableAnimated> cache = cachedProgressAnim.getUnchecked(recipe.duration);
			cache.getKey().draw(stack, 12,23);
			guiHelper.createDrawable(GUI, 176, 0, 17, 12).draw(stack, 12,23);
			cache.getValue().draw(stack, 55,23);
			guiHelper.createDrawable(GUI, 176, 12, 17, 12).draw(stack, 55,23);
		}

		@Override
		public ResourceLocation getUid() {
			return CobblestoneGeneratorRecipes.UID;
		}

		@Override
		public Class<? extends CobblestoneGeneratorRecipes> getRecipeClass() {
			return CobblestoneGeneratorRecipes.class;
		}

		@Override
		public String getTitle() {
			return title.getString();
		}

		@Override
		public IDrawable getBackground() {
			return background;
		}

		@Override
		public IDrawable getIcon() {
			return null;
		}

		@Override
		public void setIngredients(CobblestoneGeneratorRecipes recipe, IIngredients ingredients) {
			ArrayList<ItemStack> fluid = new ArrayList<>();
			fluid.add(new ItemStack(Items.LAVA_BUCKET));
			fluid.add(new ItemStack(Items.WATER_BUCKET));
			ingredients.setInputs(VanillaTypes.ITEM, fluid);
			ingredients.setOutput(VanillaTypes.ITEM, recipe.getResultItem());
		}

		public void setRecipe(IRecipeLayout layout, CobblestoneGeneratorRecipes recipe, IIngredients ingredients) {
			IGuiItemStackGroup stacks = layout.getItemStacks();
			stacks.init(0, true, 6, 5);
			stacks.init(1, true, 61, 5);
			stacks.init(2, false, 33, 23);
			// ...
			stacks.set(0, ingredients.getInputs(VanillaTypes.ITEM).get(0));
			stacks.set(1, ingredients.getInputs(VanillaTypes.ITEM).get(1));
			stacks.set(2, ingredients.getOutputs(VanillaTypes.ITEM).get(0));

		}
	}
}




