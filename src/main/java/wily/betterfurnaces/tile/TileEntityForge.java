package wily.betterfurnaces.tile;

import java.nio.charset.StandardCharsets;
import java.util.Map.Entry;
import java.util.Random;

import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fluids.*;
import wily.betterfurnaces.BetterFurnacesReforged;
import wily.betterfurnaces.blocks.BlockForge;
import wily.betterfurnaces.blocks.BlockIronFurnace;
import wily.betterfurnaces.inventory.SlotFurnaceInput;
import wily.betterfurnaces.inventory.SlotUpgrade;
import wily.betterfurnaces.upgrade.Upgrade;
import wily.betterfurnaces.upgrade.Upgrades;
import wily.betterfurnaces.utils.MutableEnergyStorage;
import wily.betterfurnaces.utils.OreProcessingRegistry;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RangedWrapper;
import net.minecraftforge.oredict.OreDictionary;

public class TileEntityForge extends TileEntitySmeltingBase {
	//Constants
	@Override
	protected int getDefaultCookTime() {
		return 4;
	}
	@Override
	public int FUEL() {return 3;}
	@Override
	public int[] UPGRADES(){ return new int[] {7, 8, 9, 10, 11, 12};}
	@Override
	public int UPGRADEORE(){ return 7;}
	@Override
	public int UPGRADEENDER(){ return 8;}
	@Override
	public int UPGRADECOLOR(){ return 12;}
	@Override
	public int FINPUT(){ return INPUTS()[0];}
	@Override
	public int LINPUT(){ return INPUTS()[INPUTS().length - 1];}
	@Override
	public int FOUTPUT(){ return OUTPUTS()[0];}
	@Override
	public int LOUTPUT(){ return OUTPUTS()[OUTPUTS().length - 1];}
	@Override
	public int[] INPUTS(){ return new int[]{0,1,2};}
	@Override
	public int[] OUTPUTS(){ return new int[]{4,5,6};}
	@Override
	public int LiquidCapacity() {return 8000;}
	@Override
	public int MAX_FE_TRANSFER(){ return 6000;}
	@Override
	public int MAX_ENERGY_STORED(){ return 32000;}
	@Override
	public int invsize(){ return 14;}
    @Override
	public boolean canSmelts(){
		return canSmelt(FINPUT(), FOUTPUT()) || canSmelt(FINPUT() + 1, FOUTPUT() + 1) || canSmelt(FINPUT() + 2, FOUTPUT() + 2);
	}
	@Override
	public void trySmelt(){
		if (canSmelt(FINPUT(), FOUTPUT())) smeltItem( FINPUT(), FOUTPUT());
		if (canSmelt(FINPUT() + 1, FOUTPUT() + 1)) smeltItem( FINPUT() + 1, FOUTPUT() + 1);
		if (canSmelt(FINPUT() + 2, FOUTPUT() + 2)) smeltItem( FINPUT() + 2, FOUTPUT() + 2);
	}
}