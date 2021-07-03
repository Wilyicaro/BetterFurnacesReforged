import mezz.jei.api.IModPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import net.mcreator.betterfurnaces.gui.FurnaceguiGui;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.constants.VanillaRecipeCategoryUid;
import mezz.jei.api.registration.IAdvancedRegistration;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import net.mcreator.betterfurnaces.block.NetherhotFurnaceBlock;
import net.mcreator.betterfurnaces.block.IronfurnaceoffBlock;
import net.mcreator.betterfurnaces.block.GoldfurnaceoffBlock;
import net.mcreator.betterfurnaces.block.ExtremeFurnaceBlock;
import net.mcreator.betterfurnaces.block.DiamondfurnaceBlock;
import net.mcreator.betterfurnaces.block.ExtremeForgeBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

@mezz.jei.api.JeiPlugin
public class JeiPlugin implements IModPlugin {
    public static IJeiHelpers jeiHelper;
    @Override
	public ResourceLocation getPluginUid() {
		return new ResourceLocation("betterfurnacesreforged", "default");
	}

	@Override
	public void registerAdvanced(IAdvancedRegistration registration) {

	}

	@Override
	public void registerRecipeCatalysts(IRecipeCatalystRegistration registry) {
			registry.addRecipeCatalyst(new ItemStack(IronfurnaceoffBlock.block), VanillaRecipeCategoryUid.FURNACE);
			registry.addRecipeCatalyst(new ItemStack(GoldfurnaceoffBlock.block), VanillaRecipeCategoryUid.FURNACE);
			registry.addRecipeCatalyst(new ItemStack(DiamondfurnaceBlock.block), VanillaRecipeCategoryUid.FURNACE);
			registry.addRecipeCatalyst(new ItemStack(NetherhotFurnaceBlock.block), VanillaRecipeCategoryUid.FURNACE);
			registry.addRecipeCatalyst(new ItemStack(ExtremeFurnaceBlock.block), VanillaRecipeCategoryUid.FURNACE);
			registry.addRecipeCatalyst(new ItemStack(ExtremeForgeBlock.block), VanillaRecipeCategoryUid.FURNACE);

			registry.addRecipeCatalyst(new ItemStack(IronfurnaceoffBlock.block), VanillaRecipeCategoryUid.FUEL);
			registry.addRecipeCatalyst(new ItemStack(GoldfurnaceoffBlock.block), VanillaRecipeCategoryUid.FUEL);
			registry.addRecipeCatalyst(new ItemStack(DiamondfurnaceBlock.block), VanillaRecipeCategoryUid.FUEL);
			registry.addRecipeCatalyst(new ItemStack(NetherhotFurnaceBlock.block), VanillaRecipeCategoryUid.FUEL);
			registry.addRecipeCatalyst(new ItemStack(ExtremeFurnaceBlock.block), VanillaRecipeCategoryUid.FUEL);
			registry.addRecipeCatalyst(new ItemStack(ExtremeForgeBlock.block), VanillaRecipeCategoryUid.FUEL);


}
}