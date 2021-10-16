package wily.betterfurnaces;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.constants.VanillaRecipeCategoryUid;
import mezz.jei.api.registration.IAdvancedRegistration;
import wily.betterfurnaces.block.*;
import wily.betterfurnaces.block.IronFurnaceBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

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
			registry.addRecipeCatalyst(new ItemStack(IronFurnaceBlock.block), VanillaRecipeCategoryUid.FURNACE);
			registry.addRecipeCatalyst(new ItemStack(GoldFurnaceBlock.block), VanillaRecipeCategoryUid.FURNACE);
			registry.addRecipeCatalyst(new ItemStack(DiamondFurnaceBlock.block), VanillaRecipeCategoryUid.FURNACE);
			registry.addRecipeCatalyst(new ItemStack(NetherhotFurnaceBlock.block), VanillaRecipeCategoryUid.FURNACE);
			registry.addRecipeCatalyst(new ItemStack(ExtremeFurnaceBlock.block), VanillaRecipeCategoryUid.FURNACE);
			registry.addRecipeCatalyst(new ItemStack(ExtremeForgeBlock.block), VanillaRecipeCategoryUid.FURNACE);

			registry.addRecipeCatalyst(new ItemStack(IronFurnaceBlock.block), VanillaRecipeCategoryUid.FUEL);
			registry.addRecipeCatalyst(new ItemStack(GoldFurnaceBlock.block), VanillaRecipeCategoryUid.FUEL);
			registry.addRecipeCatalyst(new ItemStack(DiamondFurnaceBlock.block), VanillaRecipeCategoryUid.FUEL);
			registry.addRecipeCatalyst(new ItemStack(NetherhotFurnaceBlock.block), VanillaRecipeCategoryUid.FUEL);
			registry.addRecipeCatalyst(new ItemStack(ExtremeFurnaceBlock.block), VanillaRecipeCategoryUid.FUEL);
			registry.addRecipeCatalyst(new ItemStack(ExtremeForgeBlock.block), VanillaRecipeCategoryUid.FUEL);


}
}