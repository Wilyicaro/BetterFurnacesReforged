package wily.betterfurnaces.tile;

public class TileEntityGoldFurnace extends TileEntityIronFurnace {

	@Override
	protected int getDefaultCookTime() {
		return 100;
	}

}
