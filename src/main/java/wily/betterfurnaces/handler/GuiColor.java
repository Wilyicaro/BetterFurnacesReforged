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
    private GuiSlider red;
    private GuiSlider green;
    private GuiSlider blue;
    private GuiButtonExt button;

    private static final ResourceLocation TEXTURE = new ResourceLocation(BetterFurnacesReforged.MODID, "textures/gui/container/guiupgrades.png");

    @Override
    public void initGui() {
        ItemStack itemStack = this.mc.player.getHeldItem(EnumHand.MAIN_HAND);
        NBTTagCompound nbt;
        if (itemStack.hasTagCompound()) {
            nbt = itemStack.getTagCompound();
        } else {
            nbt = new NBTTagCompound();
        }
        super.initGui();
        this.guiLeft = (this.width - this.xSize) / 2;
        this.guiTop = (this.height - this.ySize) / 2;
        Keyboard.enableRepeatEvents(true);
        button = new GuiButtonExt(0, width / 2 - (xSize - 8) / 2, guiTop + 98, xSize - 8, 20, I18n.format("gui.betterfurnacesreforged.done"));
        red = new GuiSlider(1, width / 2 - (xSize - 8) / 2, guiTop + 24, xSize - 8, 20, I18n.format("gui.betterfurnacesreforged.color.red") , "", 0, 255, nbt.getInteger("red"), false, true);
        green = new GuiSlider(2, width / 2 - (xSize - 8) / 2, guiTop + 46, xSize - 8, 20, I18n.format("gui.betterfurnacesreforged.color.green") , "", 0, 255, nbt.getInteger("green"), false, true);
        blue = new GuiSlider(3, width / 2 - (xSize - 8) / 2, guiTop + 68, xSize - 8, 20, I18n.format("gui.betterfurnacesreforged.color.blue") , "", 0, 255, nbt.getInteger("blue"), false, true);
        this.buttonList.add(red);
        this.buttonList.add(green);
        this.buttonList.add(blue);
        this.buttonList.add(button);
    }


    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        String s = new ItemStack(ModObjects.COLOR_UPGRADE).getDisplayName();
        this.mc.getTextureManager().bindTexture(TEXTURE);
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
        this.drawTexturedModalRect(this.guiLeft + 154, this.guiTop + 6, 176, 62, 16, 16);


        this.fontRenderer.drawString(s, width / 2 - this.fontRenderer.getStringWidth(s) / 2, this.guiTop + 8, 4210752);
        super.drawScreen(mouseX, mouseY, partialTicks);

        Color myColour = new Color(red.getValueInt(), green.getValueInt(), blue.getValueInt());
        this.drawRect(this.guiLeft + 156, this.guiTop + 8, this.guiLeft + 168, this.guiTop + 20, myColour.getRGB());

        GlStateManager.color(red.getValueInt(), green.getValueInt(), blue.getValueInt(), 1.0F);
        GlStateManager.disableLighting();
        this.mc.getTextureManager().bindTexture(TEXTURE);
        this.drawTexturedModalRect((width / 2 - 32), (this.guiTop - 70), 176, 0, 56, 62);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button == this.button){
            ItemStack itemStack = this.mc.player.getHeldItem(EnumHand.MAIN_HAND);
            NBTTagCompound nbt;
            if (itemStack.hasTagCompound()) {
                nbt = itemStack.getTagCompound();
            } else {
                nbt = new NBTTagCompound();
            }
            nbt.setInteger("red", red.getValueInt());
            nbt.setInteger("green", green.getValueInt());
            nbt.setInteger("blue", blue.getValueInt());
            itemStack.setTagCompound(nbt);
            mc.displayGuiScreen(new GuiInventory(mc.player));
            mc.player.closeScreen();
            mc.displayGuiScreen(this);
        }
    }

}
