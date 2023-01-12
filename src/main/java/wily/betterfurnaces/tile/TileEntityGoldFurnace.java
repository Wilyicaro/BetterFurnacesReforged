package wily.betterfurnaces.tile;

import static wily.betterfurnaces.Config.goldTierSpeed;

public class TileEntityGoldFurnace extends TileEntitySmeltingBase {

	@Override
	protected int getDefaultCookTime() {
		return goldTierSpeed;
	}

}
