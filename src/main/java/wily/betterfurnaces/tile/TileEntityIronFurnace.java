package wily.betterfurnaces.tile;

import java.nio.charset.StandardCharsets;
import java.util.Map.Entry;
import java.util.Random;

import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import wily.betterfurnaces.BetterFurnacesReforged;
import wily.betterfurnaces.blocks.BlockIronFurnace;
import wily.betterfurnaces.inventory.SlotFurnaceFuel;
import wily.betterfurnaces.inventory.SlotFurnaceInput;
import wily.betterfurnaces.inventory.SlotUpgrade;
import wily.betterfurnaces.upgrade.Upgrade;
import wily.betterfurnaces.upgrade.Upgrades;
import wily.betterfurnaces.utils.MutableEnergyStorage;
import wily.betterfurnaces.utils.OreProcessingRegistry;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
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
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.common.registry.GameRegistry.ItemStackHolder;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RangedWrapper;
import net.minecraftforge.oredict.OreDictionary;

public class TileEntityIronFurnace extends TileEntity implements ITickable {
	//Constants
	public static final int SLOT_INPUT = 0;
	public static final int SLOT_FUEL = 1;
	public static final int SLOT_OUTPUT = 2;
	public static final int[] SLOT_UPGRADE = { 3, 4, 5 };
	public static final int MAX_FE_TRANSFER = 0;
	public static final int MAX_ENERGY_STORED = 0;

	//Item Handling, RangedWrappers are for sided i/o
	protected final ItemStackHandler inv = new ItemStackHandler(6) {
		public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
			if (!isItemValid(slot, stack)) return stack;
			return super.insertItem(slot, stack, simulate);
		};

		public boolean isItemValid(int slot, ItemStack stack) {
			if (slot == SLOT_INPUT) return SlotFurnaceInput.isStackValid(stack);
			if (slot == SLOT_FUEL) return SlotFurnaceFuel.isStackValid(stack);
			return slot > 2 ? SlotUpgrade.isStackValid(stack) : true;
		};
	};

	private final RangedWrapper TOP = new RangedWrapper(inv, SLOT_INPUT, SLOT_INPUT + 1);
	private final RangedWrapper SIDES = new RangedWrapper(inv, SLOT_FUEL, SLOT_FUEL + 1);
	private final RangedWrapper BOTTOM = new RangedWrapper(inv, SLOT_OUTPUT, SLOT_OUTPUT + 1);

	//Main TE Fields.
	protected MutableEnergyStorage energy = new MutableEnergyStorage(MAX_ENERGY_STORED, MAX_FE_TRANSFER, getEnergyUse());
	protected FluidTank tank = new FluidTank(4000) {
		@Override
		public boolean canFillFluidType(FluidStack fluid) {
			return super.canFillFluidType(fluid) && getFluidBurnTime(fluid) > 0;
		}
	};
	protected ItemStack recipeKey = ItemStack.EMPTY;
	protected ItemStack recipeOutput = ItemStack.EMPTY;
	protected ItemStack failedMatch = ItemStack.EMPTY;
	protected int burnTime = 0;
	protected int currentCookTime = 0;
	protected int fuelLength = 0;

	@ItemStackHolder(value = "minecraft:sponge", meta = 1)
	public static final ItemStack WET_SPONGE = ItemStack.EMPTY;


	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		inv.deserializeNBT(tag.getCompoundTag("inv"));
		energy.setEnergy(tag.getInteger("energy"));
		burnTime = tag.getInteger("burn_time");
		fuelLength = tag.getInteger("fuel_length");
		currentCookTime = tag.getInteger("current_cook_time");
		tank.readFromNBT(tag.getCompoundTag("tank"));
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound = super.writeToNBT(compound);
		compound.setTag("inv", inv.serializeNBT());
		compound.setInteger("energy", energy.getEnergyStored());
		compound.setInteger("burn_time", burnTime);
		compound.setInteger("fuel_length", fuelLength);
		compound.setInteger("current_cook_time", currentCookTime);
		compound.setInteger("default_cook", getDefaultCookTime());
		compound.setTag("tank", tank.writeToNBT(new NBTTagCompound()));
		return compound;
	}

	/**
	 * Receives data from the server and sets it to the client TE.
	 * Should only be called on client.
	 */
	public void readContainerSync(int[] fromNet, FluidStack fluid) {
		energy.setEnergy(fromNet[0]);
		burnTime = fromNet[1];
		fuelLength = fromNet[2];
		currentCookTime = fromNet[3];
		tank.setFluid(fluid);
	}

	/**
	 * Writes data that needs to be synced to a byte buffer.  Called from {@link Container#detectAndSendChanges}
	 */
	public void writeContainerSync(ByteBuf buf) {
		buf.writeInt(energy.getEnergyStored());
		buf.writeInt(burnTime);
		buf.writeInt(fuelLength);
		buf.writeInt(currentCookTime);
		FluidStack fluid = tank.getFluid();
		if (fluid == null) {
			buf.writeInt(4);
			buf.writeCharSequence("null", StandardCharsets.UTF_8);
		} else {
			buf.writeInt(fluid.getFluid().getName().length());
			buf.writeCharSequence(fluid.getFluid().getName(), StandardCharsets.UTF_8);
			buf.writeInt(fluid.amount);
		}
	}
	public int hex() {
		NBTTagCompound nbt = getInventory().getStackInSlot(5).getTagCompound();

		return ((nbt.getInteger("red")&0x0ff)<<16)|((nbt.getInteger("green")&0x0ff)<<8)|(nbt.getInteger("blue")&0x0ff);
	}

	/**
	 * Main logic method for Iron Furnaces.  Does all the furnace things.
	 */
	@Override
	public final void update() {
		if (world.isRemote) return;
		if (hasUpgrade(Upgrades.COLOR)){
			world.setBlockState(pos, world.getBlockState(pos).withProperty(BlockIronFurnace.COLORED, true));
		}else world.setBlockState(pos, world.getBlockState(pos).withProperty(BlockIronFurnace.COLORED, false));
		ItemStack fuel = ItemStack.EMPTY;
		boolean canSmelt = canSmelt();
		if (!this.isBurning() && (isAltFuel() || !(fuel = inv.getStackInSlot(SLOT_FUEL)).isEmpty())) {
			if (canSmelt) burnFuel(fuel, false);
		}

		boolean wasBurning = isBurning();

		if (this.isBurning()) {
			burnTime--;
			if (canSmelt) smelt();
			else currentCookTime = 0;
		}

		if (!this.isBurning() && (isAltFuel() || !(fuel = inv.getStackInSlot(SLOT_FUEL)).isEmpty())) {
			if (canSmelt()) burnFuel(fuel, wasBurning);
		}

		if (wasBurning && !isBurning()) world.setBlockState(pos, getDimState());
	}

	/**
	 * @return If this furnace is burning.
	 */
	public boolean isBurning() {
		return getBurnTime() > 0;
	}

	/**
	 * Increments cook time, and tries to smelt the current item.
	 */
	protected void smelt() {
		currentCookTime++;
		if (this.currentCookTime >= this.getCookTime()) {
			this.currentCookTime = 0;
			this.smeltItem();
		}
	}

	/**
	 * Consumes fuel.
	 * @param fuel The item in the fuel slot.
	 * @param burnedThisTick If we have burned this tick, used to determine if we need to change blockstate.
	 */
	protected void burnFuel(ItemStack fuel, boolean burnedThisTick) {
		if (isFluid() && tank.getFluid() != null) {
			fuelLength = burnTime = getFluidBurnTime(tank.getFluid());
			if (this.isBurning()) tank.getFluid().amount--;
		} else {
			fuelLength = burnTime = getItemBurnTime(fuel) * getDefaultCookTime() / 200;
			if (this.isBurning()) {
				Item item = fuel.getItem();
				fuel.shrink(1);
				if (fuel.isEmpty()) inv.setStackInSlot(SLOT_FUEL, item.getContainerItem(fuel));
			}
		}
		if (isBurning() && !burnedThisTick) world.setBlockState(pos, getLitState());
		markDirty();
	}

	/**
	 * @return If the current item in the input slot can be smelted.
	 */
	protected boolean canSmelt() {
		ItemStack input = inv.getStackInSlot(SLOT_INPUT);
		ItemStack output = inv.getStackInSlot(SLOT_OUTPUT);
		if (input.isEmpty() || input == failedMatch) return false;

		if (recipeKey.isEmpty() || !OreDictionary.itemMatches(recipeKey, input, false)) {
			boolean matched = false;
			for (Entry<ItemStack, ItemStack> e : FurnaceRecipes.instance().getSmeltingList().entrySet()) {
				if (OreDictionary.itemMatches(e.getKey(), input, false)) {
					recipeKey = e.getKey();
					recipeOutput = e.getValue();
					matched = true;
					failedMatch = ItemStack.EMPTY;
					break;
				}
			}
			if (!matched && (hasUpgrade(Upgrades.ORE_PROCESSING) || hasUpgrade(Upgrades.ADVORE_PROCESSING))) {
				ItemStack stack = OreProcessingRegistry.getSmeltingResult(input);
				if (stack.isEmpty()) {
					recipeKey = ItemStack.EMPTY;
					recipeOutput = ItemStack.EMPTY;
					failedMatch = input;
					return false;
				} else {
					recipeKey = input;
					recipeOutput = stack;
					matched = true;
					failedMatch = ItemStack.EMPTY;
				}
			} else if(!matched) {
				recipeKey = ItemStack.EMPTY;
				recipeOutput = ItemStack.EMPTY;
				failedMatch = input;
				return false;
			}
		}

		ItemStack check = recipeOutput;
		if (hasOreResult && ((hasUpgrade(Upgrades.ORE_PROCESSING) || hasUpgrade(Upgrades.ADVORE_PROCESSING)))){
			check = check.copy();
			check.grow(check.getCount());
		}

		return !recipeOutput.isEmpty() && (output.isEmpty() || (ItemHandlerHelper.canItemStacksStack(check, output) && (check.getCount() + output.getCount() <= output.getMaxStackSize())));
	}

	/**
	 * Actually smelts the item in the input slot.  Has special casing for vanilla wet sponge, because w e w vanilla.
	 */
	public void smeltItem() {
		ItemStack stack = inv.getStackInSlot(3);
		ItemStack ender = inv.getStackInSlot(4);
		ItemStack input = inv.getStackInSlot(SLOT_INPUT);
		ItemStack recipeOutput = getResult();
		ItemStack curOutput = inv.getStackInSlot(SLOT_OUTPUT);

		if (curOutput.isEmpty()) inv.setStackInSlot(SLOT_OUTPUT, recipeOutput);
		else if (ItemHandlerHelper.canItemStacksStack(curOutput, recipeOutput)) curOutput.grow(recipeOutput.getCount());

		if (input.isItemEqual(WET_SPONGE) && inv.getStackInSlot(SLOT_FUEL).getItem() == Items.BUCKET) inv.setStackInSlot(SLOT_FUEL, new ItemStack(Items.WATER_BUCKET));
		if ((stack != null) || (this.isBurning())) {
			if (stack.attemptDamageItem((int) 1, new Random(), null)) {
				stack.shrink(1);
				stack.setItemDamage(0);
			}
		}
		if (ender != null) {
			if (ender.attemptDamageItem((int) 1, new Random(), null)) {
				ender.shrink(1);
				ender.setItemDamage(0);
			}
		}
		input.shrink(1);
		markDirty();
	}

	/**
	 * Prevents the TE from deleting itself if we change state, but not if the block is removed.
	 */
	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
		return oldState.getBlock() != newState.getBlock();
	}

	/**
	 * @param upg An upgrade.
	 * @return If this TE currently has said upgrade.
	 */
	public boolean hasUpgrade(Upgrade upg) {
		for (int slot : SLOT_UPGRADE)
			if (upg.matches(inv.getStackInSlot(slot))) return true;
		return false;
	}

	/**
	 * @param stack The item in the fuel slot.
	 * @return The burn time for this fuel, or 0, if this is an electric furnace.
	 */
	public int getItemBurnTime(ItemStack stack) {
		if (isAltFuel()) return 0;
		return TileEntityFurnace.getItemBurnTime(stack) * (hasUpgrade(Upgrades.EFFICIENCY) || hasUpgrade(Upgrades.ADVEFFICIENCY) ? 2 : 1);
	}

	/**
	 * Says "I have items and energy!"
	 */
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && isFluid()) return true;
		return super.hasCapability(capability, facing);
	}

	/**
	 * Returns item/energy caps based on side.  Follows vanilla furnace rules for items.
	 */
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			IItemHandler h;
			if (facing == null) h = inv;
			else if (facing == EnumFacing.DOWN) h = BOTTOM;
			else if (facing == EnumFacing.UP) h = TOP;
			else h = SIDES;
			return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(h);
		}
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && isFluid()) return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(tank);
		return super.getCapability(capability, facing);
	}

	/**
	 * @return The unlit state of this TE.
	 */
	public IBlockState getDimState() {
		return world.getBlockState(pos).withProperty(BlockIronFurnace.BURNING, false);
	}

	/**
	 * @return The burning state of this TE.
	 */
	public IBlockState getLitState() {
		return world.getBlockState(pos).withProperty(BlockIronFurnace.BURNING, true);
	}

	/**
	 * @return If this TE has the electric upgrade.
	 */

	/**
	 * @return If this TE has the liquid fuel upgrade.
	 */
	public boolean isFluid() {
		return hasUpgrade(Upgrades.LIQUID_FUEL);
	}

	public boolean isAltFuel() {
		return isFluid();
	}

	public ItemStackHandler getInventory() {
		return inv;
	}

	private boolean hasOreResult = false;

	private ItemStack getResult() {
		if ((hasUpgrade(Upgrades.ORE_PROCESSING)) || hasUpgrade(Upgrades.ADVORE_PROCESSING)) {
			ItemStack out = OreProcessingRegistry.getSmeltingResult(inv.getStackInSlot(SLOT_INPUT)).copy();
			if (out.isEmpty() && isOre(recipeKey)) {
				out = FurnaceRecipes.instance().getSmeltingList().get(recipeKey).copy();
				out.grow(out.getCount());
			}
			if (!out.isEmpty()) {
				hasOreResult = true;
				return out;
			}
        }
		hasOreResult = false;
		return FurnaceRecipes.instance().getSmeltingList().get(recipeKey).copy();
	}

	private static boolean isOre(ItemStack stack) {
		if (!stack.getHasSubtypes() && !stack.isItemStackDamageable()) stack.setItemDamage(0);
		int[] ids = OreDictionary.getOreIDs(stack);
		for (int i : ids) {
			if (OreDictionary.getOreName(i).contains("ore")) return true;
		}
		return false;
	}

	/**
	 * @return The actual cook time of this furnace, taking speed into account.
	 */
	public final int getCookTime() {
		return getDefaultCookTime();
	}

	protected int getDefaultCookTime() {
		return 150;
	}

	public int getEnergyUse() {
		return 0;
	}


	public int getCurrentCookTime() {
		return currentCookTime;
	}

	public int getBurnTime() {
		return burnTime;
	}

	public int getFuelLength() {
		return fuelLength;
	}

	public void clear() {
		for (int i = 0; i < 6; i++) {
			inv.setStackInSlot(i, ItemStack.EMPTY);
		}
	}

	public NBTTagCompound writeHwylaData(NBTTagCompound tag) {
		tag.setTag("inv", inv.serializeNBT());
		tag.setInteger("current_cook_time", currentCookTime);
		tag.setInteger("default_cook", getDefaultCookTime());
		if (isFluid() && tank.getFluidAmount() > 0) tag.setTag("fluid", tank.getFluid().writeToNBT(new NBTTagCompound()));
		return tag;
	}
	@Override
	public NBTTagCompound getUpdateTag() {
		return this.writeToNBT(new NBTTagCompound());
	}
	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		this.readFromNBT(pkt.getNbtCompound());
	}

	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		return new SPacketUpdateTileEntity(this.pos, 0, this.getUpdateTag());
	}

	/**
	 * Returns the burn time for a single mB of a given fluid.
	 */
	public static int getFluidBurnTime(FluidStack stack) {
		return stack == null ? 0 : BetterFurnacesReforged.FLUID_FUELS.getInt(stack.getFluid().getName());
	}

	public FluidTank getTank() {
		return tank;
	}

	public boolean isUsableByPlayer(EntityPlayer player) {
		if (this.world.getTileEntity(this.pos) != this) {
			return false;
		} else {
			return player.getDistanceSq((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D) <= 64.0D;
		}
	}

}