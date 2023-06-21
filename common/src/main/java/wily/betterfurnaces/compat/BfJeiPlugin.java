

package wily.betterfurnaces.compat;


import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.ibm.icu.impl.Pair;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.architectury.fluid.FluidStack;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.registration.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeManager;
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
import wily.ultimatefurnaces.init.RegistrationUF;


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
			registry.addRecipeCatalyst(new ItemStack(Registration.COBBLESTONE_GENERATOR.get()), BFRRecipeTypes.ROCK_GENERATING_JEI);

			Block[] blocks = {Registration.IRON_FURNACE.get(), Registration.GOLD_FURNACE.get(), Registration.DIAMOND_FURNACE.get(), Registration.NETHERHOT_FURNACE.get(), Registration.EXTREME_FURNACE.get(), Registration.EXTREME_FORGE.get()};
			if (Config.enableUltimateFurnaces.get()) blocks = ArrayUtils.addAll(blocks, RegistrationUF.COPPER_FURNACE.get(), RegistrationUF.STEEL_FURNACE.get(),RegistrationUF.AMETHYST_FURNACE.get(),RegistrationUF.PLATINUM_FURNACE.get(), RegistrationUF.ULTIMATE_FURNACE.get(), RegistrationUF.COPPER_FORGE.get(), RegistrationUF.IRON_FORGE.get(), RegistrationUF.GOLD_FORGE.get(), RegistrationUF.DIAMOND_FORGE.get(), RegistrationUF.NETHERHOT_FORGE.get(), RegistrationUF.ULTIMATE_FORGE.get());
			for (Block i : blocks) {
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

	private void addDescription(IRecipeRegistration registry, ItemStack itemDefinition,
								Component... message) {
		registry.addIngredientInfo(itemDefinition, VanillaTypes.ITEM_STACK, message);
	}

	@Override
	public void registerCategories(IRecipeCategoryRegistration registration) {
		registration.addRecipeCategories(new CobblestoneGeneratorCategory(registration.getJeiHelpers().getGuiHelper()));
	}

	@Override
	public void registerRecipes(IRecipeRegistration registration) {
		Level world = Minecraft.getInstance().level;
		RecipeManager recipeManager = world.getRecipeManager();
		registration.addRecipes(BFRRecipeTypes.ROCK_GENERATING_JEI, RecipeUtil.getRecipes(recipeManager, Registration.ROCK_GENERATING_RECIPE.get()));

		Registration.ITEMS.forEach((item)-> {
			if (item.get() instanceof TierUpgradeItem i) addDescription(registration, new ItemStack(i), Component.literal(I18n.get("tooltip." + BetterFurnacesReforged.MOD_ID + ".upgrade.tier", i.from.getName().getString(), i.to.getName().getString())));
		});
		if (Config.enableUltimateFurnaces.get()) Registration.ITEMS.forEach((item)-> {
			if (item.get() instanceof TierUpgradeItem i) addDescription(registration, new ItemStack(i), Component.literal(I18n.get("tooltip." + BetterFurnacesReforged.MOD_ID + ".upgrade.tier", i.from.getName().getString(), i.to.getName().getString())));
		});

	}

	@Override
	public void registerGuiHandlers(IGuiHandlerRegistration registry) {
		if (Config.enableJeiClickArea.get() && Config.enableJeiPlugin.get()) {
			registry.addRecipeClickArea(FurnaceScreen.class, 79, 35, 24, 17, RecipeTypes.SMELTING, RecipeTypes.FUELING);
			registry.addRecipeClickArea(ForgeScreen.class, 80, 80, 24, 17, RecipeTypes.SMELTING, RecipeTypes.FUELING);
			registry.addRecipeClickArea(CobblestoneGeneratorScreen.class, 58, 44, 17, 12, BFRRecipeTypes.ROCK_GENERATING_JEI);
			registry.addRecipeClickArea(CobblestoneGeneratorScreen.class, 101, 44, 17, 12, BFRRecipeTypes.ROCK_GENERATING_JEI);
		}
	}

	public static class CobblestoneGeneratorCategory implements IRecipeCategory<CobblestoneGeneratorRecipes> {
		private final Component title;
		private final IDrawable background;

		private final LoadingCache<Integer, Pair<IDrawableAnimated,IDrawableAnimated>> cachedProgressAnim;

		protected final IGuiHelper guiHelper;
		public static final ResourceLocation GUI = new ResourceLocation(BetterFurnacesReforged.MOD_ID , "textures/container/cobblestone_generator_gui.png");

		public CobblestoneGeneratorCategory(IGuiHelper guiHelper) {
			this.title = Registration.COBBLESTONE_GENERATOR.get().getName();
			this.background = guiHelper.createDrawable(GUI, 46, 21, 85, 52);
			this.guiHelper = guiHelper;
			this.cachedProgressAnim = CacheBuilder.newBuilder()
					.maximumSize(25)
					.build(new CacheLoader<>() {
						@Override
						public Pair<IDrawableAnimated, IDrawableAnimated> load(Integer cookTime) {
							return Pair.of( guiHelper.drawableBuilder(GUI, 176, 24, 17, 12)
									.buildAnimated(cookTime, IDrawableAnimated.StartDirection.LEFT, false), guiHelper.drawableBuilder(GUI, 176, 36, 17, 12)
									.buildAnimated(cookTime, IDrawableAnimated.StartDirection.RIGHT, false));
						}
					});
		}
		@Override
		public void draw(CobblestoneGeneratorRecipes recipe, IRecipeSlotsView recipeSlotsView, PoseStack stack, double mouseX, double mouseY) {
			GuiUtil.renderScaled(stack,  (float) recipe.duration / 20 + "s", 62, 45, 0.75f, 0x7E7E7E, false);
			FluidRenderUtil.renderTiledFluid(stack,12, 23, 17,12, FluidStack.create(Fluids.LAVA, 1000), false);
			FluidRenderUtil.renderTiledFluid(stack,55, 23, 17,12,FluidStack.create(Fluids.WATER, 1000), true);

			Pair<IDrawableAnimated,IDrawableAnimated> cache = cachedProgressAnim.getUnchecked(recipe.duration);
			cache.first.draw(stack, 12,23);
			guiHelper.createDrawable(GUI, 176, 0, 17, 12).draw(stack, 12,23);
			cache.second.draw(stack, 55,23);
			guiHelper.createDrawable(GUI, 176, 12, 17, 12).draw(stack, 55,23);
		}

		@Override
		public RecipeType<CobblestoneGeneratorRecipes> getRecipeType() {
			return BFRRecipeTypes.ROCK_GENERATING_JEI;
		}

		@Override
		public Component getTitle() {
			return title;
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
		public void setRecipe(IRecipeLayoutBuilder builder, CobblestoneGeneratorRecipes recipe, IFocusGroup focuses) {
			builder.addSlot(RecipeIngredientRole.OUTPUT,34, 24).addItemStack(recipe.getResultItem(RegistryAccess.EMPTY));
			builder.addSlot(RecipeIngredientRole.INPUT, 7, 6).addItemStack(new ItemStack(Items.LAVA_BUCKET));
			builder.addSlot(RecipeIngredientRole.INPUT,62, 6).addItemStack(new ItemStack(Items.WATER_BUCKET));

		}
	}
}




