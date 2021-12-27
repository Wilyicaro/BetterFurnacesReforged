package wily.betterfurnaces.handler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import wily.betterfurnaces.inventory.ContainerBF;
import wily.betterfurnaces.inventory.FContainerBF;
import wily.betterfurnaces.tile.TileEntityForge;
import wily.betterfurnaces.tile.TileEntitySmeltingBase;

public class GuiHandler implements IGuiHandler{

	public static final int GUI_FORGE = 0;
	public static final int GUI_FURNACE = 0;

	@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World world,	int x, int y, int z ) {

		TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));
	    if (id == ContainerBF.GUIID)
	        return new ContainerBF(player.inventory, (TileEntitySmeltingBase)tileEntity);
		if (id == FContainerBF.GUIID)
					return new FContainerBF(player.inventory, (TileEntityForge)tileEntity);
		return null;

	}

	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world,	int x, int y, int z) {

		TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));
		if (id == ContainerBF.GUIID)
	        return new GuiBF(player.inventory, (TileEntitySmeltingBase)tileEntity);
		if (id == FContainerBF.GUIID)
					return new GuiForgeBF(player.inventory, (TileEntityForge)tileEntity);
		if (id == GuiColor.GUIID)
			return new GuiColor();
		return null;
	  }

}
