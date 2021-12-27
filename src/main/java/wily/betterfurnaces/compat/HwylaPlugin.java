package wily.betterfurnaces.compat;

import java.util.List;

import javax.annotation.Nonnull;

import wily.betterfurnaces.blocks.BlockForge;
import wily.betterfurnaces.blocks.BlockIronFurnace;
import wily.betterfurnaces.tile.TileEntityForge;
import wily.betterfurnaces.tile.TileEntitySmeltingBase;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.IWailaRegistrar;
import mcp.mobius.waila.api.SpecialChars;
import mcp.mobius.waila.api.WailaPlugin;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.items.ItemStackHandler;

@WailaPlugin
public class HwylaPlugin implements IWailaPlugin, IWailaDataProvider {

	@Override
	public void register(IWailaRegistrar reg) {
		reg.registerBodyProvider(this, TileEntitySmeltingBase.class);
		reg.registerNBTProvider(this, TileEntitySmeltingBase.class);
	}

	@Nonnull
	@Override
	public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
		if (!config.getConfig("fo.furnacedisplay") || !accessor.getBlockState().getValue(BlockIronFurnace.BURNING)) return currenttip;

		int cookTime = accessor.getNBTData().getShort("current_cook_time");
		int defaultCook = accessor.getNBTData().getShort("default_cook");

		String renderStr = "";
		if (accessor.getBlock() instanceof BlockIronFurnace) {
			ItemStackHandler h = new ItemStackHandler(3);
			h.deserializeNBT(accessor.getNBTData().getCompoundTag("inv"));

			if (!h.getStackInSlot(0).isEmpty()) {
				String name = h.getStackInSlot(0).getItem().getRegistryName().toString();
				renderStr += SpecialChars.getRenderString("waila.stack", "1", name, String.valueOf(h.getStackInSlot(0).getCount()), String.valueOf(h.getStackInSlot(0).getItemDamage()));
			} else renderStr += SpecialChars.getRenderString("waila.stack", "2");

			if (accessor.getNBTData().hasKey("fluid")) {
				FluidStack s = FluidStack.loadFluidStackFromNBT(accessor.getNBTData().getCompoundTag("fluid"));
				h.setStackInSlot(1, FluidUtil.getFilledBucket(s));
				String name = h.getStackInSlot(1).getItem().getRegistryName().toString();
				if (h.getStackInSlot(1).hasTagCompound()) {
					renderStr += SpecialChars.getRenderString("waila.stack", "1", name, String.valueOf(h.getStackInSlot(1).getCount()), String.valueOf(h.getStackInSlot(1).getItemDamage()), h.getStackInSlot(1).getTagCompound().toString());
				} else
					renderStr += SpecialChars.getRenderString("waila.stack", "1", name, String.valueOf(h.getStackInSlot(1).getCount()), String.valueOf(h.getStackInSlot(1).getItemDamage()));
			} else if (!h.getStackInSlot(1).isEmpty()) {
				String name = h.getStackInSlot(1).getItem().getRegistryName().toString();
				renderStr += SpecialChars.getRenderString("waila.stack", "1", name, String.valueOf(h.getStackInSlot(1).getCount()), String.valueOf(h.getStackInSlot(1).getItemDamage()));
			} else renderStr += SpecialChars.getRenderString("waila.stack", "2");

			renderStr += SpecialChars.getRenderString("waila.progress", String.valueOf(cookTime), String.valueOf(defaultCook));

			if (!h.getStackInSlot(2).isEmpty()) {
				String name = h.getStackInSlot(2).getItem().getRegistryName().toString();
				renderStr += SpecialChars.getRenderString("waila.stack", "1", name, String.valueOf(h.getStackInSlot(2).getCount()), String.valueOf(h.getStackInSlot(2).getItemDamage()));
			} else renderStr += SpecialChars.getRenderString("waila.stack", "2");
		}
		if (accessor.getBlock() instanceof BlockForge) {
			ItemStackHandler h = new ItemStackHandler(7);
			h.deserializeNBT(accessor.getNBTData().getCompoundTag("inv"));

			if (accessor.getNBTData().hasKey("fluid")) {
				FluidStack s = FluidStack.loadFluidStackFromNBT(accessor.getNBTData().getCompoundTag("fluid"));
				h.setStackInSlot(3, FluidUtil.getFilledBucket(s));
				String name = h.getStackInSlot(3).getItem().getRegistryName().toString();
				if (h.getStackInSlot(3).hasTagCompound()) {
					renderStr += SpecialChars.getRenderString("waila.stack", "1", name, String.valueOf(h.getStackInSlot(3).getCount()), String.valueOf(h.getStackInSlot(3).getItemDamage()), h.getStackInSlot(3).getTagCompound().toString());
				} else
					renderStr += SpecialChars.getRenderString("waila.stack", "1", name, String.valueOf(h.getStackInSlot(3).getCount()), String.valueOf(h.getStackInSlot(3).getItemDamage()));
			}
			if (!h.getStackInSlot(0).isEmpty()) {
				String name = h.getStackInSlot(0).getItem().getRegistryName().toString();
				renderStr += SpecialChars.getRenderString("waila.stack", "1", name, String.valueOf(h.getStackInSlot(0).getCount()), String.valueOf(h.getStackInSlot(0).getItemDamage()));
			} else renderStr += SpecialChars.getRenderString("waila.stack", "2");

			if (!h.getStackInSlot(1).isEmpty()) {
				String name = h.getStackInSlot(1).getItem().getRegistryName().toString();
				renderStr += SpecialChars.getRenderString("waila.stack", "1", name, String.valueOf(h.getStackInSlot(1).getCount()), String.valueOf(h.getStackInSlot(1).getItemDamage()));
			} else renderStr += SpecialChars.getRenderString("waila.stack", "2");

			if (!h.getStackInSlot(2).isEmpty()) {
				String name = h.getStackInSlot(2).getItem().getRegistryName().toString();
				renderStr += SpecialChars.getRenderString("waila.stack", "1", name, String.valueOf(h.getStackInSlot(2).getCount()), String.valueOf(h.getStackInSlot(2).getItemDamage()));
			} else renderStr += SpecialChars.getRenderString("waila.stack", "2");

			renderStr += SpecialChars.getRenderString("waila.progress", String.valueOf(cookTime), String.valueOf(defaultCook));

			if (!h.getStackInSlot(4).isEmpty()) {
				String name = h.getStackInSlot(4).getItem().getRegistryName().toString();
				renderStr += SpecialChars.getRenderString("waila.stack", "1", name, String.valueOf(h.getStackInSlot(4).getCount()), String.valueOf(h.getStackInSlot(4).getItemDamage()));
			} else renderStr += SpecialChars.getRenderString("waila.stack", "2");

			if (!h.getStackInSlot(5).isEmpty()) {
				String name = h.getStackInSlot(5).getItem().getRegistryName().toString();
				renderStr += SpecialChars.getRenderString("waila.stack", "1", name, String.valueOf(h.getStackInSlot(5).getCount()), String.valueOf(h.getStackInSlot(5).getItemDamage()));
			} else renderStr += SpecialChars.getRenderString("waila.stack", "2");

			if (!h.getStackInSlot(6).isEmpty()) {
				String name = h.getStackInSlot(6).getItem().getRegistryName().toString();
				renderStr += SpecialChars.getRenderString("waila.stack", "1", name, String.valueOf(h.getStackInSlot(6).getCount()), String.valueOf(h.getStackInSlot(6).getItemDamage()));
			} else renderStr += SpecialChars.getRenderString("waila.stack", "2");
		}

		currenttip.add(renderStr);

		return currenttip;
	}

	@Nonnull
	@Override
	public NBTTagCompound getNBTData(EntityPlayerMP player, TileEntity te, NBTTagCompound tag, World world, BlockPos pos) {
		if (te instanceof TileEntitySmeltingBase) {
			return te instanceof TileEntitySmeltingBase ? ((TileEntitySmeltingBase) te).writeHwylaData(tag) : tag;
		} else return null;
	}

}
