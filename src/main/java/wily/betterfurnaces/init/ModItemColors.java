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

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import wily.betterfurnaces.handler.GuiColor;

@SideOnly(Side.CLIENT)
public class ModItemColors implements IItemColor {
    public static final IItemColor COLOR = new ModItemColors();
    public static Random rand = new Random();
    public GuiColor guiColor;
    public Minecraft mc;

    /* (non-Javadoc)
     * @see net.minecraft.client.renderer.color.IBlockColor#colorMultiplier(net.minecraft.block.state.IBlockState, net.minecraft.world.IBlockAccess, net.minecraft.util.math.BlockPos, int)
     */
    @SideOnly(Side.CLIENT)
    @Override
    public int colorMultiplier(ItemStack stack, int tintIndex) {
        NBTTagCompound nbt;
        if (stack.hasTagCompound()) {
            nbt = stack.getTagCompound();
        } else {
            nbt = new NBTTagCompound();
        }
        tintIndex = 0;
        return ((nbt.getInteger("red") & 0x0ff) << 16) | ((nbt.getInteger("green") & 0x0ff) << 8) | (nbt.getInteger("blue") & 0x0ff);
    }
        /**
         * Register block colors.
         */
        @SideOnly(Side.CLIENT)
        public static void registerItemColors()
        {
            // DEBUG
            System.out.println("Registering block color handler");

            Minecraft.getMinecraft().getItemColors().registerItemColorHandler(COLOR, ModObjects.COLOR_FURNACE);
            Minecraft.getMinecraft().getItemColors().registerItemColorHandler(COLOR, ModObjects.COLOR_FORGE);

        }

}