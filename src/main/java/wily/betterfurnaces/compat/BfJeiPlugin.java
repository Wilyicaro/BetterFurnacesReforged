//? if <1.21.2 {
/*package wily.betterfurnaces.compat;


import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.ibm.icu.impl.Pair;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.handlers.IGuiContainerHandler;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.registration.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import wily.betterfurnaces.BetterFurnacesReforged;
import wily.betterfurnaces.BFRConfig;
import wily.betterfurnaces.client.BFRClientConfig;
import wily.betterfurnaces.client.screen.AbstractBasicScreen;
import wily.betterfurnaces.client.screen.CobblestoneGeneratorScreen;
import wily.betterfurnaces.client.screen.ForgeScreen;
import wily.betterfurnaces.client.screen.FurnaceScreen;
import wily.betterfurnaces.init.ModObjects;
import wily.betterfurnaces.init.Registration;
import wily.betterfurnaces.items.TierUpgradeItem;
import wily.betterfurnaces.recipes.CobblestoneGeneratorRecipe;
import wily.betterfurnaces.util.RecipeUtil;
import wily.factoryapi.base.client.IWindowWidget;
import wily.factoryapi.util.FactoryScreenUtil;
//? if >=1.20.5 {
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.CustomData;
//?}

import java.util.ArrayList;
import java.util.List;


@JeiPlugin
public class BfJeiPlugin implements IModPlugin {
	@Override
	public ResourceLocation getPluginUid() {
		return BetterFurnacesReforged.createModLocation("_plugin");
	}
	@Override
	public void registerAdvanced(IAdvancedRegistration registration) {

	}

	@Override
	public void registerRecipeCatalysts(IRecipeCatalystRegistration registry) {
		if (BFRClientConfig.enableJEICatalysts.get() && BFRClientConfig.enableJEIPlugin.get()) {
			registry.addRecipeCatalyst(new ItemStack(ModObjects.COBBLESTONE_GENERATOR.get()), BFRRecipeTypes.ROCK_GENERATING_JEI);

			Registration.getSmeltingBlocks().forEach(holder->{
				ItemStack smelting = new ItemStack(holder.get());
				registry.addRecipeCatalyst(smelting, RecipeTypes.SMELTING);
				registry.addRecipeCatalyst(smelting, RecipeTypes.FUELING);

				ItemStack blasting = smelting.copy();
				CompoundTag blastingTag = new CompoundTag();
				blastingTag.putInt("type", 1);
				/^? if <1.20.5 {^/
				/^blasting.setTag(blastingTag);
				^///?} else {
				blasting.set(DataComponents.CUSTOM_DATA, CustomData.of(blastingTag));
				 //?}
				registry.addRecipeCatalyst(blasting, RecipeTypes.BLASTING);


				ItemStack smoking = smelting.copy();
				CompoundTag smokingTag = new CompoundTag();
				smokingTag.putInt("type", 2);
				/^? if <1.20.5 {^/
				/^smoking.setTag(smokingTag);
				^///?} else {
				smoking.set(DataComponents.CUSTOM_DATA, CustomData.of(blastingTag));
				 //?}
				registry.addRecipeCatalyst(smoking, RecipeTypes.SMOKING);

			});
		}
	}

	private void addDescription(IRecipeRegistration registry, ItemStack itemDefinition, Component... message) {
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
		registration.addRecipes(BFRRecipeTypes.ROCK_GENERATING_JEI, RecipeUtil.getRecipes(recipeManager, ModObjects.ROCK_GENERATING_RECIPE.get()));

		Registration.getItems().forEach((item)-> {
			if (item.get() instanceof TierUpgradeItem i) addDescription(registration, new ItemStack(i), Component.translatable("tooltip." + BetterFurnacesReforged.MOD_ID + ".upgrade.tier", i.from.getName().getString(), i.to.getName().getString()));
		});

	}

	@Override
	public void registerGuiHandlers(IGuiHandlerRegistration registry) {
		if (BFRClientConfig.enableJEIClickArea.get() && BFRClientConfig.enableJEIPlugin.get()) {
			registry.addGenericGuiContainerHandler(AbstractBasicScreen.class, new IGuiContainerHandler<AbstractBasicScreen<?>>() {
				@Override
				public List<Rect2i> getGuiExtraAreas(AbstractBasicScreen<?> containerScreen) {
					List<Rect2i> list =  new ArrayList<>();
					for (Renderable nested : containerScreen.getNestedRenderables())
						if (nested instanceof IWindowWidget w) list.add(w.getBounds());
					return list;
				}
			});
			registry.addRecipeClickArea(FurnaceScreen.class, 79, 35, 24, 17, RecipeTypes.SMELTING, RecipeTypes.FUELING);
			registry.addRecipeClickArea(ForgeScreen.class, 80, 80, 24, 17, RecipeTypes.SMELTING, RecipeTypes.FUELING);
			registry.addRecipeClickArea(CobblestoneGeneratorScreen.class, 58, 44, 17, 12, BFRRecipeTypes.ROCK_GENERATING_JEI);
			registry.addRecipeClickArea(CobblestoneGeneratorScreen.class, 101, 44, 17, 12, BFRRecipeTypes.ROCK_GENERATING_JEI);
		}
	}

	public static class CobblestoneGeneratorCategory implements IRecipeCategory<CobblestoneGeneratorRecipe> {
		private final Component title;
		private final IDrawable background;

		private final LoadingCache<Integer, Pair<IDrawableAnimated,IDrawableAnimated>> cachedProgressAnim;

		protected final IGuiHelper guiHelper;

		public CobblestoneGeneratorCategory(IGuiHelper guiHelper) {
			this.title = ModObjects.COBBLESTONE_GENERATOR.get().getName();
			this.background = guiHelper.createDrawable(CobblestoneGeneratorScreen.GUI, 46, 21, 85, 52);
			this.guiHelper = guiHelper;
			this.cachedProgressAnim = CacheBuilder.newBuilder().maximumSize(25).build(CacheLoader.from(cookTime->Pair.of(guiHelper.drawableBuilder(CobblestoneGeneratorScreen.GUI, 176, 24, 17, 12).buildAnimated(cookTime, IDrawableAnimated.StartDirection.LEFT, false), guiHelper.drawableBuilder(CobblestoneGeneratorScreen.GUI, 176, 36, 17, 12).buildAnimated(cookTime, IDrawableAnimated.StartDirection.RIGHT, false))));
		}
		@Override
		public void draw(CobblestoneGeneratorRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics graphics, double mouseX, double mouseY) {
			FactoryScreenUtil.renderScaled(graphics.pose(),  (float) recipe.duration() / 20 + "s", 62, 45, 0.75f, 0x7E7E7E, false);
			CobblestoneGeneratorScreen.renderStretchedFluid(graphics, 12, 23, 17,12, CobblestoneGeneratorScreen.lavaInstance, false);
			CobblestoneGeneratorScreen.renderStretchedFluid(graphics, 55, 23, 17,12, CobblestoneGeneratorScreen.waterInstance, true);

			Pair<IDrawableAnimated,IDrawableAnimated> cache = cachedProgressAnim.getUnchecked(recipe.duration());
			cache.first.draw(graphics, 12,23);
			guiHelper.createDrawable(CobblestoneGeneratorScreen.GUI, 176, 0, 17, 12).draw(graphics, 12,23);
			cache.second.draw(graphics, 55,23);
			guiHelper.createDrawable(CobblestoneGeneratorScreen.GUI, 176, 12, 17, 12).draw(graphics, 55,23);
		}

		@Override
		public RecipeType<CobblestoneGeneratorRecipe> getRecipeType() {
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
		public void setRecipe(IRecipeLayoutBuilder builder, CobblestoneGeneratorRecipe recipe, IFocusGroup focuses) {
			builder.addSlot(RecipeIngredientRole.OUTPUT,34, 24).addItemStack(recipe.getResultItem(RegistryAccess.EMPTY));
			builder.addSlot(RecipeIngredientRole.INPUT, 7, 6).addItemStack(new ItemStack(Items.LAVA_BUCKET));
			builder.addSlot(RecipeIngredientRole.INPUT,62, 6).addItemStack(new ItemStack(Items.WATER_BUCKET));

		}
	}
}
*///?}




