package wily.betterfurnaces.tile;

import java.nio.charset.StandardCharsets;
import java.util.Map.Entry;
import java.util.Random;

import net.minecraft.init.SoundEvents;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidUtil;
import org.apache.commons.lang3.ArrayUtils;
import wily.betterfurnaces.blocks.BlockSmelting;
import wily.betterfurnaces.init.ModObjects;
import wily.betterfurnaces.inventory.SlotFuel;
import wily.betterfurnaces.inventory.SlotInput;
import wily.betterfurnaces.items.ItemUpgrade;
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

public class TileEntitySmeltingBase extends TileEntity implements ITickable {
	//Constants
	public int FUEL() {return 1;}
	public int[] UPGRADES(){ return new int[] {3,4,5};}
	public int HEATER() {return FUEL();}
	public int FINPUT(){ return INPUTS()[0];}
	public int LINPUT(){ return INPUTS()[INPUTS().length - 1];}
	public int FOUTPUT(){ return OUTPUTS()[0];}
	public int LOUTPUT(){ return OUTPUTS()[OUTPUTS().length - 1];}
	public int[] INPUTS(){ return new int[]{0};}
	public int[] OUTPUTS(){ return new int[]{2};}
	public int LiquidCapacity() {return 4000;}
	public int MAX_FE_TRANSFER(){ return 2400;}
	public int MAX_ENERGY_STORED(){ return 16000;}
	public int inventorySize(){ return 6;}

	//Item Handling, RangedWrappers are for sided i/o
	protected final ItemStackHandler inv = new ItemStackHandler(inventorySize()) {
		public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
			if (!isItemValid(slot, stack)) return stack;
			return super.insertItem(slot, stack, simulate);
		};
		@Override
		public boolean isItemValid(int slot, ItemStack stack) {
			if (slot >= FINPUT() && slot <= LINPUT()) return SlotInput.isStackValid(stack);
			if (slot == FUEL()) return SlotFuel.isStackValid( TileEntitySmeltingBase.this, stack);
			if (ArrayUtils.contains(UPGRADES(), slot)) {
				if (stack.getItem() instanceof ItemUpgrade && !hasUpgrade(stack.getItem()) && !hasUpgradeType((ItemUpgrade) stack.getItem()))
					if (((ItemUpgrade) stack.getItem()).upgradeType == 1) return (slot == HEATER() || getSlots() <= 6);
					else return true;
			}
		return false;
		};
	};

	private final RangedWrapper TOP = new RangedWrapper(inv, FINPUT(), LINPUT() + 1);
	private final RangedWrapper SIDES = new RangedWrapper(inv, FUEL(), FUEL() + 1);
	private final RangedWrapper BOTTOM = new RangedWrapper(inv, FOUTPUT(), LOUTPUT() + 1);

	//Main TE Fields.
	protected MutableEnergyStorage energy = new MutableEnergyStorage(MAX_ENERGY_STORED(), MAX_FE_TRANSFER(), getEnergyUse());
	protected FluidTank tank = new FluidTank(LiquidCapacity()) {
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
		NBTTagCompound nbt = getUpgradeTypeSlotItem(ModObjects.COLOR_UPGRADE).getTagCompound();

		return ((nbt.getInteger("red")&0x0ff)<<16)|((nbt.getInteger("green")&0x0ff)<<8)|(nbt.getInteger("blue")&0x0ff);
	}

	/**
	 * Main logic method for Iron Furnaces.  Does all the furnace things.
	 */
	@Override
	public final void update() {
		if (world.isRemote) return;
		if (hasUpgrade(ModObjects.COLOR_UPGRADE)){
			world.setBlockState(pos, world.getBlockState(pos).withProperty(BlockSmelting.COLORED, true));
		}else world.setBlockState(pos, world.getBlockState(pos).withProperty(BlockSmelting.COLORED, false));

		ItemStackHandler inv = getInventory();
		ItemStack filler = inv.getStackInSlot(FUEL());
		FluidStack fluid = FluidUtil.getFluidContained(filler);
		if (isFluid() && fluid != null && TileEntityForge.getFluidBurnTime(fluid) > 0) {
			FluidActionResult fill = FluidUtil.tryEmptyContainer(filler, FluidUtil.getFluidHandler(world, pos, null), 1000, null, true);
			if (fill.isSuccess()) {
				world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ITEM_BUCKET_EMPTY_LAVA, SoundCategory.PLAYERS, 0.6F, 0.8F);
				inv.setStackInSlot(FUEL(), fill.result);
			}
		}
		if (inv.getStackInSlot(FUEL()).hasCapability(CapabilityEnergy.ENERGY, null) && energy.getEnergyStored() < energy.getMaxEnergyStored()){
			IEnergyStorage energyFuel = inv.getStackInSlot(FUEL()).getCapability(CapabilityEnergy.ENERGY, null);
			energyFuel.extractEnergy(energy.receiveEnergy(energyFuel.getEnergyStored(), false), false);

		}

		ItemStack fuel = inv.getStackInSlot(FUEL());

		if (!this.isBurning() && (isAltFuel() || !fuel.isEmpty())) {
			if (canSmelts()) burnFuel(fuel, false);
		}

		boolean wasBurning = isBurning();

		if (this.isBurning()) {
			burnTime--;
			if (canSmelts()) {
				currentCookTime++;
				if (this.currentCookTime >= this.getCookTime()) {
					this.trySmelt();
					this.currentCookTime = 0;
				}
			}
			else currentCookTime = 0;
		}

		if (!this.isBurning() && (isAltFuel() || !fuel.isEmpty())) {
			if (canSmelts()) burnFuel(fuel, wasBurning);
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
	public void trySmelt(){
		if (canSmelt(FINPUT(), FOUTPUT())) smeltItem( FINPUT(), FOUTPUT());
	}

	/**
	 * Consumes fuel.
	 * @param fuel The item in the fuel slot.
	 * @param burnedThisTick If we have burned this tick, used to determine if we need to change blockstate.
	 */
	protected void burnFuel(ItemStack fuel, boolean burnedThisTick) {
		boolean Energy = energy.getEnergyStored() >= getEnergyUse();
		boolean Liquid = tank.getFluidAmount() > 1;
		if (isEnergy() && Energy) {
			fuelLength = (burnTime = energy.getEnergyStored() >= getEnergyUse() ? 1 : 0);
			for (int a : INPUTS())
				if (this.isBurning() && !inv.getStackInSlot(a).isEmpty()) energy.extractEnergy(getEnergyUse() * (hasUpgradeType(ModObjects.ORE_PROCESSING_UPGRADE) && isOre(inv.getStackInSlot(a)) ? 2 : 1), false);
		} else if (isFluid() && Liquid) {
			fuelLength = burnTime = getFluidBurnTime(tank.getFluid()) * (hasUpgradeType(ModObjects.FUEL_EFFICIENCY_UPGRADE) ? 2 : 1) * getDefaultCookTime() / 20000;
			if (this.isBurning()) tank.drain(10, true);
		} else if ((isEnergy() && !Energy) || (isFluid() && !Liquid) || (!isEnergy() && !isFluid()) ){

			fuelLength = burnTime = getItemBurnTime(fuel) * getDefaultCookTime() / 200;
			if (this.isBurning()) {
				Item item = fuel.getItem();
				fuel.shrink(1);
				if (fuel.isEmpty()) inv.setStackInSlot(FUEL(), item.getContainerItem(fuel));
			}
		}

		if (hasUpgrade(ModObjects.FUEL_EFFICIENCY_UPGRADE)) {
			ItemStack ender = getUpgradeSlotItem(ModObjects.FUEL_EFFICIENCY_UPGRADE);
			if (ender.attemptDamageItem((int) 1, new Random(), null)) {
				ender.shrink(1);
				ender.setItemDamage(0);
			}
		}

		if (isBurning() && !burnedThisTick) world.setBlockState(pos, getLitState());
		markDirty();
	}

	/**tB
	 * @return If the current item in the input slot can be smelted.
	 */
	protected boolean canSmelt(int i, int o) {
		ItemStack input = inv.getStackInSlot(i);
		ItemStack output = inv.getStackInSlot(o);
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
			if (!matched && (hasUpgradeType(ModObjects.ORE_PROCESSING_UPGRADE))) {
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
		if (hasOreResult && hasUpgradeType(ModObjects.ORE_PROCESSING_UPGRADE)){
			check = check.copy();
			check.grow(check.getCount());
		}

		return !recipeOutput.isEmpty() && (output.isEmpty() || (ItemHandlerHelper.canItemStacksStack(check, output) && (check.getCount() + output.getCount() <= output.getMaxStackSize())));
	}
	public boolean canSmelts(){
		return canSmelt(FINPUT(), FOUTPUT());
	}
	/**
	 * Actually smelts the item in the input slot.  Has special casing for vanilla wet sponge, because w e w vanilla.
	 */
	public void smeltItem(int INPUT, int OUTPUT) {
		ItemStack recipeOutput = getResult(INPUT);
		ItemStack curOutput = inv.getStackInSlot(OUTPUT);
		ItemStack input = inv.getStackInSlot(INPUT);


		if (curOutput.isEmpty()) inv.setStackInSlot(OUTPUT, recipeOutput);
		else if (ItemHandlerHelper.canItemStacksStack(curOutput, recipeOutput)) curOutput.grow(recipeOutput.getCount());

		if (input.isItemEqual(WET_SPONGE) && inv.getStackInSlot(FUEL()).getItem() == Items.BUCKET) inv.setStackInSlot(FUEL(), new ItemStack(Items.WATER_BUCKET));
		if (hasUpgrade(ModObjects.ORE_PROCESSING_UPGRADE)) {
			ItemStack stack = getUpgradeSlotItem(ModObjects.ORE_PROCESSING_UPGRADE);
			if (stack.attemptDamageItem((int) 1, new Random(), null)) {
				stack.shrink(1);
				stack.setItemDamage(0);
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

	public boolean hasUpgrade(Item upg) {
		for (int slot : UPGRADES())
			if (upg == inv.getStackInSlot(slot).getItem()) return true;
		return false;
	}

	public boolean hasUpgradeType(ItemUpgrade upg) {
		for (int slot : UPGRADES()) {
			if (inv.getStackInSlot(slot).getItem() instanceof ItemUpgrade && upg.upgradeType == ((ItemUpgrade)inv.getStackInSlot(slot).getItem()).upgradeType) return true;
		}
		return hasUpgrade(upg);
	}

	public ItemStack getUpgradeTypeSlotItem(ItemUpgrade upg) {
		for (int slot : UPGRADES())
			if (inv.getStackInSlot(slot).getItem() instanceof ItemUpgrade && upg.upgradeType == ((ItemUpgrade) inv.getStackInSlot(slot).getItem()).upgradeType) return inv.getStackInSlot(slot);
		return inv.getStackInSlot(UPGRADES()[0]);
	}

	public ItemStack getUpgradeSlotItem(Item upg) {
		for (int slot : UPGRADES())
			if (upg == inv.getStackInSlot(slot).getItem()) return inv.getStackInSlot(slot);
		return inv.getStackInSlot(UPGRADES()[0]);
	}

	/**
	 * @param stack The item in the fuel slot.
	 * @return The burn time for this fuel, or 0, if this is an electric furnace.
	 */
	public int getItemBurnTime(ItemStack stack) {
		return TileEntityFurnace.getItemBurnTime(stack) * (hasUpgradeType(ModObjects.FUEL_EFFICIENCY_UPGRADE) ? 2 : 1);
	}
	public int getEnergy() {
		return energy.getEnergyStored();
	}

	/**
	 * Says "I have items and energy!"
	 */
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && isFluid() || capability == CapabilityEnergy.ENERGY && isEnergy()) return true;
		return super.hasCapability(capability, facing);
	}

	/**
	 * Returns item/energy caps based on side.  Follows vanilla furnace rules for items.
	 */
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (capability == CapabilityEnergy.ENERGY && isEnergy()) return CapabilityEnergy.ENERGY.cast(this.energy);
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
		return world.getBlockState(pos).withProperty(BlockSmelting.BURNING, false);
	}

	/**
	 * @return The burning state of this TE.
	 */
	public IBlockState getLitState() {
		return world.getBlockState(pos).withProperty(BlockSmelting.BURNING, true);
	}

	/**
	 * @return If this TE has the electric upgrade.
	 */

	/**
	 * @return If this TE has the liquid fuel upgrade.
	 */
	public boolean isFluid() {
		return hasUpgrade(ModObjects.LIQUID_FUEL_UPGRADE);
	}

	public boolean isAltFuel() {
		return isFluid() || isEnergy();
	}

	public ItemStackHandler getInventory() {
		return inv;
	}

	private boolean hasOreResult = false;

	private ItemStack getResult(int i) {
		ItemStack input = inv.getStackInSlot(i);
		if (hasUpgradeType(ModObjects.ORE_PROCESSING_UPGRADE)) {
			ItemStack out = OreProcessingRegistry.getSmeltingResult(input).copy();
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
	public boolean isEnergy() {
		return hasUpgrade(ModObjects.ENERGY_UPGRADE);
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
		return 600;
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
		return stack == null ? 0 : TileEntityFurnace.getItemBurnTime(FluidUtil.getFilledBucket(stack));
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