package wily.betterfurnaces.screens;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.gui.widget.Slider;
import org.lwjgl.opengl.GL11;
import wily.betterfurnaces.BetterFurnacesReforged;
import wily.betterfurnaces.init.ItemColorsHandler;
import wily.betterfurnaces.init.Registration;
import wily.betterfurnaces.items.ColorUpgradeItem.ContainerColorUpgrade;
import wily.betterfurnaces.network.Messages;
import wily.betterfurnaces.network.PacketColorSlider;

@OnlyIn(Dist.CLIENT)
public class ColorUpgradeScreen extends AbstractUpgradeScreen<ContainerColorUpgrade> {
    public static final ResourceLocation WIDGETS = new ResourceLocation(BetterFurnacesReforged.MOD_ID + ":" + "textures/container/widgets.png");
    public Slider red;
    public Slider green;
    public Slider blue;
    public Button change;
    private int buttonstate = 0;
    private final PlayerEntity player;


    public ColorUpgradeScreen(ContainerColorUpgrade container, PlayerInventory inv, ITextComponent name) {
        super(container, inv, name);
        player = inv.player;
    }

    protected static void sliderPacket(Slider slider, int diff) {
        Messages.INSTANCE.sendToServer(new PacketColorSlider(slider.getValueInt(), diff));
    }

    @Override
    protected void init() {
        super.init();
        this.buttons.clear();
        ItemStack itemStack = this.getMenu().itemStackBeingHeld;
        CompoundNBT nbt;
        nbt = itemStack.getOrCreateTag();
        red = new Slider(width / 2 - (getXSize() - 8) / 2, getGuiTop() + 24, getXSize() - 8, 20, new TranslationTextComponent("gui.betterfurnacesreforged.color.red"), new StringTextComponent(""), 0, 255, nbt.getInt("red"), false, true, t -> {
        });
        green = new Slider(width / 2 - (getXSize() - 8) / 2, getGuiTop() + 46, getXSize() - 8, 20, new TranslationTextComponent("gui.betterfurnacesreforged.color.green"), new StringTextComponent(""), 0, 255, nbt.getInt("green"), false, true, t -> {
        });
        blue = new Slider(width / 2 - (getXSize() - 8) / 2, getGuiTop() + 68, getXSize() - 8, 20, new TranslationTextComponent("gui.betterfurnacesreforged.color.blue"), new StringTextComponent(""), 0, 255, nbt.getInt("blue"), false, true, t -> {
        });
        this.addButton(red);
        this.addButton(green);
        this.addButton(blue);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        double actualMouseX = mouseX - getGuiLeft();
        double actualMouseY = mouseY - getGuiTop();
        if (actualMouseX >= 8 && actualMouseX <= 22 && actualMouseY >= 6 && actualMouseY <= 20) {
            if (buttonstate == 1) {
                Minecraft.getInstance().getSoundManager().play(SimpleSound.forUI(SoundEvents.UI_BUTTON_CLICK, 0.3F, 0.3F));
                buttonstate = 0;
            } else {
                if (buttonstate == 0) {
                    Minecraft.getInstance().getSoundManager().play(SimpleSound.forUI(SoundEvents.UI_BUTTON_CLICK, 0.3F, 0.3F));
                    buttonstate = 1;
                }
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    protected void renderColorFurnace(MatrixStack matrix, float partialTicks, int mouseX, int mouseY) {
        if (red.isHovered()) sliderPacket(red, 1);
        if (green.isHovered()) sliderPacket(green, 2);
        if (blue.isHovered()) sliderPacket(blue, 3);
        int actualMouseX = mouseX - getGuiLeft();
        int actualMouseY = mouseY - getGuiTop();
        this.minecraft.getTextureManager().bind(WIDGETS);
        if (buttonstate == 0) {
            this.blit(matrix, getGuiLeft() + 8, getGuiTop() + 8, 126, 189, 14, 14);
        }
        if (buttonstate == 1) {
            this.blit(matrix, getGuiLeft() + 8, getGuiTop() + 8, 112, 189, 14, 14);
        }
        if (actualMouseX >= 8 && actualMouseX <= 22 && actualMouseY >= 6 && actualMouseY <= 20) {
            if (buttonstate == 0) {
                this.blit(matrix, getGuiLeft() + 8, getGuiTop() + 8, 154, 189, 14, 14);
                this.renderTooltip(matrix, Blocks.FURNACE.getName(), mouseX, mouseY);
            }
            if (buttonstate == 1) {
                this.blit(matrix, getGuiLeft() + 8, getGuiTop() + 8, 140, 189, 14, 14);
                this.renderTooltip(matrix, Registration.EXTREME_FORGE.get().getName(), mouseX, mouseY);
            }
        }
        GL11.glScalef(4, 4, 4);
        RenderHelper.setupFor3DItems();
        ItemStack stack = new ItemStack((buttonstate == 0 ? Registration.EXTREME_FURNACE.get() :Registration.EXTREME_FORGE.get()).asItem());
        CompoundNBT nbt = stack.getOrCreateTag();
        ItemColorsHandler.putColor(nbt,red.getValueInt(),green.getValueInt(),blue.getValueInt());
        stack.setTag(nbt);
        this.itemRenderer.renderGuiItem(stack, (width / 2 - 32) / 4, (this.getGuiTop() - 70) / 4);

    }

}
