package wily.betterfurnaces.jei;


import com.mojang.blaze3d.matrix.MatrixStack;
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
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import wily.betterfurnaces.BetterFurnacesReforged;
import wily.betterfurnaces.Config;
import wily.betterfurnaces.init.Registration;
import wily.betterfurnaces.items.TierUpgradeItem;
import wily.betterfurnaces.recipes.CobblestoneGeneratorRecipes;
import wily.betterfurnaces.screens.*;
import wily.betterfurnaces.util.FluidRenderUtil;
import wily.betterfurnaces.util.GuiUtil;
import wily.betterfurnaces.util.RecipeUtil;

import java.util.ArrayList;

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
        if (Config.enableJeiPlugin.get() && Config.enableJeiCatalysts.get()) {
            Block[] stack = {Registration.IRON_FURNACE.get(), Registration.GOLD_FURNACE.get(), Registration.DIAMOND_FURNACE.get(), Registration.NETHERHOT_FURNACE.get(), Registration.EXTREME_FURNACE.get(), Registration.EXTREME_FORGE.get()};

            for (Block i : stack) {
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

            registry.addRecipeCatalyst(new ItemStack(Registration.COBBLESTONE_GENERATOR.get()), CobblestoneGeneratorCategory.Uid);

        }
    }

    private void addDescription(IRecipeRegistration registry, ItemStack itemDefinition,
                                ITextComponent... message) {
        registry.addIngredientInfo(itemDefinition, VanillaTypes.ITEM, message);
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new CobblestoneGeneratorCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        World world = Minecraft.getInstance().level;
        RecipeManager recipeManager = world.getRecipeManager();

        registration.addRecipes(RecipeUtil.getRecipes(recipeManager, Registration.COB_GENERATION_RECIPE), CobblestoneGeneratorCategory.Uid);
        TierUpgradeItem[] up = {Registration.IRON_UPGRADE.get(), Registration.GOLD_UPGRADE.get(), Registration.DIAMOND_UPGRADE.get(), Registration.NETHERHOT_UPGRADE.get(), Registration.EXTREME_UPGRADE.get()};
        for (TierUpgradeItem i : up)
            addDescription(registration, new ItemStack(i), new StringTextComponent(I18n.get("tooltip." + BetterFurnacesReforged.MOD_ID + ".upgrade.tier", i.from.getName().getString(), i.to.getName().getString())));
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registry) {
        if (Config.enableJeiPlugin.get() && Config.enableJeiClickArea.get()) {
            registry.addRecipeClickArea(IronFurnaceScreen.class, 79, 35, 24, 17, VanillaRecipeCategoryUid.FUEL, VanillaRecipeCategoryUid.FURNACE);
            registry.addRecipeClickArea(GoldFurnaceScreen.class, 79, 35, 24, 17, VanillaRecipeCategoryUid.FUEL, VanillaRecipeCategoryUid.FURNACE);
            registry.addRecipeClickArea(DiamondFurnaceScreen.class, 79, 35, 24, 17, VanillaRecipeCategoryUid.FUEL, VanillaRecipeCategoryUid.FURNACE);
            registry.addRecipeClickArea(ExtremeForgeScreen.class, 80, 80, 24, 17, VanillaRecipeCategoryUid.FUEL, VanillaRecipeCategoryUid.FURNACE);
            registry.addRecipeClickArea(NetherhotFurnaceScreen.class, 79, 35, 24, 17, VanillaRecipeCategoryUid.FUEL, VanillaRecipeCategoryUid.FURNACE);
            registry.addRecipeClickArea(ExtremeFurnaceScreen.class, 79, 35, 24, 17, VanillaRecipeCategoryUid.FUEL, VanillaRecipeCategoryUid.FURNACE);
            registry.addRecipeClickArea(AbstractCobblestoneGeneratorScreen.class, 58, 44, 17, 12, CobblestoneGeneratorCategory.Uid);
            registry.addRecipeClickArea(AbstractCobblestoneGeneratorScreen.class, 101, 44, 17, 12, CobblestoneGeneratorCategory.Uid);
        }
    }

    public static class CobblestoneGeneratorCategory implements IRecipeCategory<CobblestoneGeneratorRecipes> {
        public static final ResourceLocation GUI = new ResourceLocation(BetterFurnacesReforged.MOD_ID, "textures/container/cobblestone_generator_gui.png");
        private static final int result = 2;
        private static final ResourceLocation Uid = new ResourceLocation(BetterFurnacesReforged.MOD_ID, "jei/rock_generating");
        private final IDrawable background;
        protected IDrawableAnimated lava_anim;
        protected IDrawableAnimated water_anim;
        protected IDrawable lava_overlay;
        protected IDrawable water_overlay;

        protected IGuiHelper guiHelper;
        private final String title;

        public CobblestoneGeneratorCategory(IGuiHelper guiHelper) {
            this.title = Registration.COBBLESTONE_GENERATOR.get().getName().getString();
            this.background = guiHelper.createDrawable(GUI, 46, 21, 85, 52);
            this.guiHelper = guiHelper;
        }

        @Override
        public void draw(CobblestoneGeneratorRecipes recipe, MatrixStack stack, double mouseX, double mouseY) {
            GuiUtil.renderScaled(stack, (float) recipe.duration / 20 + "s", 62, 45, 0.75f, 0x7E7E7E, false);
            FluidRenderUtil.renderTiledFluid(stack, null, 12, 23, 17, 12, new FluidStack(Fluids.LAVA, 1000), false);
            FluidRenderUtil.renderTiledFluid(stack, null, 55, 23, 17, 12, new FluidStack(Fluids.WATER, 1000), true);
            this.lava_anim.draw(stack, 12, 23);
            this.lava_overlay.draw(stack, 12, 23);
            this.water_anim.draw(stack, 55, 23);
            this.water_overlay.draw(stack, 55, 23);
        }

        @Override
        public ResourceLocation getUid() {
            return Uid;
        }

        @Override
        public Class<? extends CobblestoneGeneratorRecipes> getRecipeClass() {
            return CobblestoneGeneratorRecipes.class;
        }

        @Override
        public String getTitle() {
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
        public void setIngredients(CobblestoneGeneratorRecipes recipe, IIngredients ingredients) {
            ArrayList<ItemStack> fluid = new ArrayList<>();
            fluid.add(new ItemStack(Items.LAVA_BUCKET));
            fluid.add(new ItemStack(Items.WATER_BUCKET));
            ingredients.setInputs(VanillaTypes.ITEM, fluid);
            ingredients.setOutput(VanillaTypes.ITEM, recipe.getResultItem());
        }

        @Override
        public void setRecipe(IRecipeLayout iRecipeLayout, CobblestoneGeneratorRecipes recipe, IIngredients iIngredients) {
            IGuiItemStackGroup stacks = iRecipeLayout.getItemStacks();
            stacks.init(result, false, 33, 23);
            stacks.init(0, true, 6, 5);
            stacks.init(1, true, 61, 5);
            // ...
            stacks.set(result, iIngredients.getOutputs(VanillaTypes.ITEM).get(0));
            stacks.set(0, iIngredients.getInputs(VanillaTypes.ITEM).get(0));
            stacks.set(1, iIngredients.getInputs(VanillaTypes.ITEM).get(1));
            // ...
            this.lava_anim = guiHelper.drawableBuilder(GUI, 176, 24, 17, 12).buildAnimated(recipe.duration, IDrawableAnimated.StartDirection.LEFT, false);
            this.lava_overlay = guiHelper.createDrawable(GUI, 176, 0, 17, 12);
            this.water_anim = guiHelper.drawableBuilder(GUI, 176, 36, 17, 12).buildAnimated(recipe.duration, IDrawableAnimated.StartDirection.RIGHT, false);
            this.water_overlay = guiHelper.createDrawable(GUI, 176, 12, 17, 12);
        }
    }

}




