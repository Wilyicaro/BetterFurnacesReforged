package wily.betterfurnaces.tile;

import static wily.betterfurnaces.Config.diamondTierSpeed;

public class TileEntityDiamondFurnace extends TileEntitySmeltingBase {

	@Override
	protected int getDefaultCookTime() {
		return diamondTierSpeed;
	}

}