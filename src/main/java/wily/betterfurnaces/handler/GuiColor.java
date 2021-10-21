package wily.betterfurnaces.handler;

import net.minecraft.client.gui.*;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.client.config.GuiButtonExt;
import net.minecraftforge.fml.client.config.GuiSlider;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import wily.betterfurnaces.BetterFurnacesReforged;
import wily.betterfurnaces.init.ModObjects;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.io.IOException;

public class GuiColor extends GuiScreen {
    static int i = 0;
    protected int xSize = 176;
    protected int ySize = 94;
    protected int guiLeft = (this.width - this.xSize) / 2;
    protected int guiTop = (this.height - this.ySize) / 2;
    public static int GUIID = 3;
    public GuiSlider red;
    public GuiSlider green;
    public GuiSlider blue;

    private static final ResourceLocation TEXTURE = new ResourceLocation(BetterFurnacesReforged.MODID, "textures/gui/container/upgrades_gui.png");

    @Override
    public void initGui() {
        super.initGui();
        ItemStack itemStack = this.mc.player.getHeldItem(EnumHand.MAIN_HAND);
        NBTTagCompound nbt;
        if (itemStack.hasTagCompound()) {
            nbt = itemStack.getTagCompound();
        } else {
            nbt = new NBTTagCompound();
        }
        this.guiLeft = (this.width - this.xSize) / 2;
        this.guiTop = (this.height - this.ySize) / 2;
        Keyboard.enableRepeatEvents(true);
        red = new GuiSlider(1, width / 2 - (xSize - 8) / 2, guiTop + 24, xSize - 8, 20, I18n.format("gui.betterfurnacesreforged.color.red") , "", 0, 255, nbt.getInteger("red"), false, true);
        green = new GuiSlider(2, width / 2 - (xSize - 8) / 2, guiTop + 46, xSize - 8, 20, I18n.format("gui.betterfurnacesreforged.color.green") , "", 0, 255, nbt.getInteger("green"), false, true);
        blue = new GuiSlider(3, width / 2 - (xSize - 8) / 2, guiTop + 68, xSize - 8, 20, I18n.format("gui.betterfurnacesreforged.color.blue") , "", 0, 255, nbt.getInteger("blue"), false, true);
        this.buttonList.add(red);
        this.buttonList.add(green);
        this.buttonList.add(blue);
    }


    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        String s = new ItemStack(ModObjects.COLOR_UPGRADE).getDisplayName();
        this.mc.getTextureManager().bindTexture(TEXTURE);
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
        itemRender.renderItemIntoGUI(new ItemStack(ModObjects.COLOR_UPGRADE), this.guiLeft + 154, this.guiTop + 6);

        this.fontRenderer.drawString(s, width / 2 - this.fontRenderer.getStringWidth(s) / 2, this.guiTop + 8, 4210752);
        super.drawScreen(mouseX, mouseY, partialTicks);

        GL11.glScalef(4,4,4);
        RenderHelper.enableGUIStandardItemLighting();
            ItemStack stack = new ItemStack(ModObjects.COLOR_FURNACE);
        NBTTagCompound nbt;
        if (stack.hasTagCompound()) {
            nbt = stack.getTagCompound();
        } else {
            nbt = new NBTTagCompound();
        }
        nbt.setInteger("red", red.getValueInt());
        nbt.setInteger("green", green.getValueInt());
        nbt.setInteger("blue", blue.getValueInt());
        stack.setTagCompound(nbt);
        this.itemRender.renderItemIntoGUI(stack, (width / 2 - 32)/4, (this.guiTop - 70)/4);
    }

    public int hex() {
        return ((red.getValueInt()&0x0ff)<<16)|((green.getValueInt()&0x0ff)<<8)|(blue.getValueInt()&0x0ff);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }


}
