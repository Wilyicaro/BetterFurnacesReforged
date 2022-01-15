/**
 Copyright (C) 2017 by jabelar
 This file is part of jabelar's Minecraft Forge modding examples; as such,
 you can redistribute it and/or modify it under the terms of the GNU
 General Public License as published by the Free Software Foundation,
 either version 3 of the License, or (at your option) any later version.
 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 GNU General Public License for more details.
 For a copy of the GNU General Public License see <http://www.gnu.org/licenses/>.
 */
package wily.betterfurnaces.init;

import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import wily.betterfurnaces.tile.TileEntityForge;
import wily.betterfurnaces.tile.TileEntitySmeltingBase;

@SideOnly(Side.CLIENT)
public class ModBlockColors implements IBlockColor
{
    public static final IBlockColor COLOR = new ModBlockColors();
    public static Random rand = new Random();

    /* (non-Javadoc)
     * @see net.minecraft.client.renderer.color.IBlockColor#colorMultiplier(net.minecraft.block.state.IBlockState, net.minecraft.world.IBlockAccess, net.minecraft.util.math.BlockPos, int)
     */
    @SideOnly(Side.CLIENT)
    @Override
    public int colorMultiplier(IBlockState state, IBlockAccess worldIn, BlockPos pos, int tintIndex)
    {
        if (worldIn.getTileEntity(pos) instanceof TileEntitySmeltingBase){
            TileEntitySmeltingBase te = (TileEntitySmeltingBase) worldIn.getTileEntity(pos);
            if (te.hasUpgrade(ModObjects.COLOR_UPGRADE) && te.getUpgradeTypeSlotItem(ModObjects.COLOR_UPGRADE).getTagCompound() != null) {
                return te.hex();
            }
        }

            return 0xFFFFFF;
    }

    /**
     * Register block colors.
     */
    @SideOnly(Side.CLIENT)
    public static void registerBlockColors()
    {
        // DEBUG
        System.out.println("Registering block color handler");

        Minecraft.getMinecraft().getBlockColors().registerBlockColorHandler(COLOR, ModObjects.IRON_FURNACE);
        Minecraft.getMinecraft().getBlockColors().registerBlockColorHandler(COLOR, ModObjects.GOLD_FURNACE);
        Minecraft.getMinecraft().getBlockColors().registerBlockColorHandler(COLOR, ModObjects.DIAMOND_FURNACE);
        Minecraft.getMinecraft().getBlockColors().registerBlockColorHandler(COLOR, ModObjects.NETHERHOT_FURNACE);
        Minecraft.getMinecraft().getBlockColors().registerBlockColorHandler(COLOR, ModObjects.EXTREME_FURNACE);
        Minecraft.getMinecraft().getBlockColors().registerBlockColorHandler(COLOR, ModObjects.EXTREME_FORGE);
    }
}