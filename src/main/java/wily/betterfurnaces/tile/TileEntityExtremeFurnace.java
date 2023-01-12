package wily.betterfurnaces.tile;

import static wily.betterfurnaces.Config.extremeTierSpeed;

public class TileEntityExtremeFurnace extends TileEntitySmeltingBase {
	@Override
	protected int getDefaultCookTime() {
		return extremeTierSpeed;
	}

}