package wily.betterfurnaces.client.screen;

import com.mojang.blaze3d.platform.Lighting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import wily.betterfurnaces.BetterFurnacesReforged;
import wily.betterfurnaces.client.ItemColorsHandler;
import wily.betterfurnaces.init.Registration;
import wily.betterfurnaces.items.ColorUpgradeItem.ContainerColorUpgrade;
import wily.betterfurnaces.network.Messages;
import wily.betterfurnaces.network.PacketColorSlider;
import wily.factoryapi.base.client.FactoryDrawableButton;
import wily.factoryapi.base.client.FactoryDrawableSlider;

import java.util.List;


public class ColorUpgradeScreen extends AbstractUpgradeScreen<ContainerColorUpgrade> {
    public static final ResourceLocation WIDGETS = new ResourceLocation(BetterFurnacesReforged.MOD_ID + ":" + "textures/container/widgets.png");
    private int buttonstate = 0;
    public FactoryDrawableSlider red;
    public FactoryDrawableSlider green;
    public FactoryDrawableSlider blue;

    public ColorUpgradeScreen(ContainerColorUpgrade container, Inventory inv, Component name) {
        super(container, inv, name);
    }
    @Override
    public List<FactoryDrawableSlider> configSliders() {
        return List.of(red, green, blue);
    }
    @Override
    protected void init() {
        super.init();
        CompoundTag nbt = this.getMenu().itemStackBeingHeld.getOrCreateTag();
        red = new FactoryDrawableSlider(leftPos + 3,relY() + 24,(s,b)-> sliderPacket(s,1),Component.empty(),i->Component.translatable("gui.betterfurnacesreforged.color.red").append(""+i),BetterFurnacesDrawables.VANILLA_BUTTON,BetterFurnacesDrawables.VANILLA_BUTTON_BACKGROUND,6,imageWidth - 6,nbt.getInt("red"),255);
        green = new FactoryDrawableSlider(leftPos + 3,relY() + 46,(s,b)-> sliderPacket(s,2),Component.empty(),i->Component.translatable("gui.betterfurnacesreforged.color.green").append(""+i),BetterFurnacesDrawables.VANILLA_BUTTON,BetterFurnacesDrawables.VANILLA_BUTTON_BACKGROUND,6,imageWidth - 6,nbt.getInt("green"),255);
        blue = new FactoryDrawableSlider(leftPos + 3,relY() + 68,(s,b)-> sliderPacket(s,3),Component.empty(),i->Component.translatable("gui.betterfurnacesreforged.color.blue").append(""+i),BetterFurnacesDrawables.VANILLA_BUTTON,BetterFurnacesDrawables.VANILLA_BUTTON_BACKGROUND,6,imageWidth - 6,nbt.getInt("blue"),255);
    }

    @Override
    public List<FactoryDrawableButton> addButtons(List<FactoryDrawableButton> list) {
        list.add(new FactoryDrawableButton(leftPos + 8,topPos + 8, i-> buttonstate =(buttonstate == 1 ? 0 : 1), (buttonstate == 0 ?Blocks.FURNACE : Registration.EXTREME_FORGE.get()).getName() ,BetterFurnacesDrawables.BUTTON).icon(BetterFurnacesDrawables.getButtonIcon(8 + buttonstate)));
        return list;
    }
    protected void sliderPacket(FactoryDrawableSlider slider, int diff){
        Messages.INSTANCE.sendToServer(new PacketColorSlider(slider.getValue(), diff));
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.render(graphics, mouseX, mouseY, partialTicks);
        renderColorFurnace(graphics);
    }

    protected void renderColorFurnace(GuiGraphics graphics) {
        ItemStack stack =  new ItemStack(buttonstate == 0 ? Registration.EXTREME_FURNACE.get() : Registration.EXTREME_FORGE.get().asItem());
        Lighting.setupFor3DItems();
        CompoundTag tag = stack.getOrCreateTag();
        ItemColorsHandler.putColor(tag,red.getValue(),green.getValue(),blue.getValue());
        stack.setTag(tag);
        graphics.pose().pushPose();
        graphics.pose().translate((width / 2 ), (this.relY() - 40),0);
        graphics.pose().scale(4F,4F,1F);
        graphics.pose().translate(-8, -8,0);
        graphics.renderItem(stack, 0, 0);
        graphics.pose().popPose();
    }

}
