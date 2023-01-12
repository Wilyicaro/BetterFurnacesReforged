package wily.betterfurnaces.tile;

import static wily.betterfurnaces.Config.netherhotTierSpeed;

public class TileEntityNetherhotFurnace extends TileEntitySmeltingBase {

	@Override
	protected int getDefaultCookTime() {
		return netherhotTierSpeed;
	}


}