package wily.betterfurnaces.screens;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import wily.betterfurnaces.BetterFurnacesReforged;
import wily.betterfurnaces.init.ItemColorsHandler;
import wily.betterfurnaces.init.Registration;
import wily.betterfurnaces.items.ColorUpgradeItem.ContainerColorUpgrade;
import wily.betterfurnaces.network.Messages;
import wily.betterfurnaces.network.PacketColorSlider;


public class ColorUpgradeScreen extends AbstractUpgradeScreen<ContainerColorUpgrade> {
    public static final ResourceLocation WIDGETS = new ResourceLocation(BetterFurnacesReforged.MOD_ID + ":" + "textures/container/widgets.png");
    private int buttonstate = 0;
    public BetterSlider red;
    public BetterSlider green;
    public BetterSlider blue;
    private Player player;


    public ColorUpgradeScreen(ContainerColorUpgrade container, Inventory inv, Component name) {
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
        red = new BetterSlider( width / 2 - (imageWidth - 8) / 2, relY() + 24, imageWidth - 8, 20, Component.translatable("gui.betterfurnacesreforged.color.red") , Component.empty(), 0, 255, nbt.getInt("red"),  true);
        green = new BetterSlider( width / 2 - (imageWidth - 8) / 2, relY() + 46, imageWidth - 8, 20, Component.translatable("gui.betterfurnacesreforged.color.green") , Component.empty(), 0, 255, nbt.getInt("green"), true);
        blue = new BetterSlider( width / 2 - (imageWidth - 8) / 2, relY() + 68, imageWidth - 8, 20, Component.translatable("gui.betterfurnacesreforged.color.blue") , Component.empty(), 0, 255, nbt.getInt("blue"), true);
        this.addRenderableWidget(red);
        this.addRenderableWidget(green);
        this.addRenderableWidget(blue);

    }
    protected void sliderPacket(BetterSlider slider, int diff){
            Messages.INSTANCE.sendToServer(new PacketColorSlider(slider.getValueInt(), diff));
    }
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        double actualMouseX = mouseX - relX();
        double actualMouseY = mouseY - relY();
        if (actualMouseX>= 8 && actualMouseX <= 22 && actualMouseY >= 6 && actualMouseY <= 20) {
            if (buttonstate == 1) {
                Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK.value(), 0.3F, 0.3F));
                buttonstate = 0;
            } else {
                if (buttonstate == 0) {
                    Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK.value(), 0.3F, 0.3F));
                    buttonstate = 1;
                }
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }
    @Override
    protected void renderColorFurnace(PoseStack matrix, float partialTicks, int mouseX, int mouseY) {
        if (red.isHoveredOrFocused()) sliderPacket(red, 1);
        if (green.isHoveredOrFocused()) sliderPacket(green, 2);
        if (blue.isHoveredOrFocused()) sliderPacket(blue, 3);
        int actualMouseX = mouseX - relX();
            int actualMouseY = mouseY - relY();
            RenderSystem.setShaderTexture(0, WIDGETS);
            if (buttonstate == 0) {
            this.blit(matrix, relX() + 8, relY() + 8, 126, 189, 14, 14);
        }
        if (buttonstate == 1) {
            this.blit(matrix, relX() + 8, relY() + 8, 112, 189, 14, 14);
        }
        if (actualMouseX>= 8 && actualMouseX <= 22 && actualMouseY >= 6 && actualMouseY <= 20){
            if (buttonstate == 0) {
                this.blit(matrix, relX() + 8, relY() + 8, 154, 189, 14, 14);
                this.renderTooltip(matrix, Blocks.FURNACE.getName(), mouseX, mouseY);
            }
            if (buttonstate == 1) {
                this.blit(matrix, relX() + 8, relY() + 8, 140, 189, 14, 14);
                this.renderTooltip(matrix, Registration.EXTREME_FORGE.get().getName(), mouseX, mouseY);
            }
        }
        ItemStack stack = buttonstate == 0 ? new ItemStack(Registration.EXTREME_FURNACE.get().asItem()) : new ItemStack(Registration.EXTREME_FORGE.get().asItem());
        Lighting.setupFor3DItems();
        CompoundTag tag = stack.getOrCreateTag();
        ItemColorsHandler.putColor(tag,red.getValueInt(),green.getValueInt(),blue.getValueInt());
        stack.setTag(tag);
        renderGuiItem(stack, (width / 2 - 8), (this.relY() - 48), 4,4);

    }

}
