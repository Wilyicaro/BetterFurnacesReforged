package wily.betterfurnaces.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.platform.ScreenManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fmlclient.gui.widget.Slider;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL46;
import wily.betterfurnaces.BetterFurnacesReforged;
import wily.betterfurnaces.init.Registration;
import wily.betterfurnaces.items.ItemColorUpgrade.ContainerColorUpgrade;

import javax.swing.*;

@OnlyIn(Dist.CLIENT)
public class ItemColorScreen extends ItemUpgradeScreen<ContainerColorUpgrade> {
    public static final ResourceLocation WIDGETS = new ResourceLocation(BetterFurnacesReforged.MOD_ID + ":" + "textures/container/widgets.png");
    private int buttonstate = 0;
    public Slider red;
    public Slider green;
    public Slider blue;
    public Button change;
    private Player player;


    public ItemColorScreen(ContainerColorUpgrade container, Inventory inv, Component name) {
        super(container, inv, name);
        player = inv.player;
    }

    @Override
    protected void init() {
        super.init();
        this.clearWidgets();
        ItemStack itemStack = this.getMenu().itemStackBeingHeld;
        CompoundTag nbt;
        nbt = itemStack.getOrCreateTag();
        red = new Slider( width / 2 - (getXSize() - 8) / 2, getGuiTop() + 24, getXSize() - 8, 20, new TranslatableComponent("gui.betterfurnacesreforged.color.red") , new TextComponent(""), 0, 255, nbt.getInt("red"), false,true, t -> {});
        green = new Slider( width / 2 - (getXSize() - 8) / 2, getGuiTop() + 46, getXSize() - 8, 20, new TranslatableComponent("gui.betterfurnacesreforged.color.green") , new TextComponent(""), 0, 255, nbt.getInt("green"), false,true, t -> {});
        blue = new Slider( width / 2 - (getXSize() - 8) / 2, getGuiTop() + 68, getXSize() - 8, 20, new TranslatableComponent("gui.betterfurnacesreforged.color.blue") , new TextComponent(""), 0, 255, nbt.getInt("blue"), false,true, t -> {});
        this.addRenderableWidget(red);
        this.addRenderableWidget(green);
        this.addRenderableWidget(blue);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        double actualMouseX = mouseX - getGuiLeft();
        double actualMouseY = mouseY - getGuiTop();
        if (actualMouseX>= 8 && actualMouseX <= 22 && actualMouseY >= 6 && actualMouseY <= 20) {
            if (buttonstate == 1) {
                Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 0.3F, 0.3F));
                buttonstate = 0;
            } else {
                if (buttonstate == 0) {
                    Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 0.3F, 0.3F));
                    buttonstate = 1;
                }
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }
    @Override
    protected void renderColorFurnace(PoseStack matrix, float partialTicks, int mouseX, int mouseY) {
        int actualMouseX = mouseX - getGuiLeft();
        int actualMouseY = mouseY - getGuiTop();
        RenderSystem.setShaderTexture(0, WIDGETS);
        if (buttonstate == 0) {
            this.blit(matrix, getGuiLeft() + 8, getGuiTop() + 8, 126, 189, 14, 14);
        }
        if (buttonstate == 1) {
            this.blit(matrix, getGuiLeft() + 8, getGuiTop() + 8, 112, 189, 14, 14);
        }
        if (actualMouseX>= 8 && actualMouseX <= 22 && actualMouseY >= 6 && actualMouseY <= 20){
            if (buttonstate == 0) {
                this.blit(matrix, getGuiLeft() + 8, getGuiTop() + 8, 154, 189, 14, 14);
                this.renderTooltip(matrix, Blocks.FURNACE.getName(), mouseX, mouseY);
            }
            if (buttonstate == 1) {
                this.blit(matrix, getGuiLeft() + 8, getGuiTop() + 8, 140, 189, 14, 14);
                this.renderTooltip(matrix, Registration.EXTREME_FORGE.get().getName(), mouseX, mouseY);
            }
        }
        ItemStack stack = new ItemStack(Registration.EXTREME_FURNACE_ITEM.get());
        ItemStack stack1 = new ItemStack(Registration.EXTREME_FORGE_ITEM.get());
        Lighting.setupFor3DItems();
        CompoundTag nbt = stack.getOrCreateTag();
        nbt.putInt("red", red.getValueInt());
        nbt.putInt("green", green.getValueInt());
        nbt.putInt("blue", blue.getValueInt());
        nbt.putBoolean("colored", true);
        stack.setTag(nbt);
        CompoundTag nbt1 = stack1.getOrCreateTag();
        nbt1.putInt("red", red.getValueInt());
        nbt1.putInt("green", green.getValueInt());
        nbt1.putInt("blue", blue.getValueInt());
        nbt1.putBoolean("colored", true);
        if (buttonstate == 0) {
            this.itemRenderer.renderGuiItem(stack, (width / 2 - 8), (this.getGuiTop() - 48));
        }else
            if (buttonstate == 1)
            this.itemRenderer.renderGuiItem(stack1, (width / 2 - 8), (this.getGuiTop() - 48));
    }

}
