package wily.betterfurnaces.screens;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.glfw.GLFW;
import wily.betterfurnaces.BetterFurnacesReforged;
import wily.betterfurnaces.blocks.AbstractForgeBlock;
import wily.betterfurnaces.init.Registration;
import wily.betterfurnaces.inventory.AbstractSmeltingMenu;
import wily.betterfurnaces.items.FactoryUpgradeItem;
import wily.betterfurnaces.network.Messages;
import wily.betterfurnaces.network.PacketOrientationButton;
import wily.betterfurnaces.network.PacketSettingsButton;
import wily.betterfurnaces.network.PacketShowSettingsButton;
import wily.betterfurnaces.util.FluidRenderUtil;
import wily.betterfurnaces.util.StringHelper;

import java.util.List;

@OnlyIn(Dist.CLIENT)
public abstract class AbstractSmeltingScreen<T extends AbstractSmeltingMenu> extends AbstractContainerScreen<T> {

    public ResourceLocation GUI() {return new ResourceLocation(BetterFurnacesReforged.MOD_ID + ":" + "textures/container/furnace_gui.png");}
    public static final ResourceLocation WIDGETS = new ResourceLocation(BetterFurnacesReforged.MOD_ID + ":" + "textures/container/widgets.png");
    Inventory playerInv;
    Component name;
    // Widgets x and y pos
    protected int FactoryShowButtonY() {return 3;}
    protected int[] FluidTank() {return new int[]{73,49};} // 20x22pixels
    protected int[] EnergyTank() {return new int[]{31,17};} // 16x34pixels
    protected int[] XPTank() { return new int[]{116,57};} // 16x16pixels

    private boolean storedFactoryUpgradeType(int type){
        if (getMenu().te.hasUpgradeType(Registration.FACTORY.get())) {
            FactoryUpgradeItem stack = ((FactoryUpgradeItem)getMenu().te.getUpgradeTypeSlotItem(Registration.FACTORY.get()).getItem());
            if (type == 0) return true;
            else if (type == 1)
                return stack.canInput;
            else if (type == 2)
                return stack.canOutput;
            else if (type == 3)
                return stack.pipeSide;
            else if (type == 4)
                return stack.redstoneSignal;
        }
        return false;
    }

    public boolean add_button;
    public boolean sub_button;

    public AbstractSmeltingScreen(T t, Inventory inv, Component name) {
        super(t, inv, name);
        playerInv = inv;
        this.name = name;
    }

    @Override
    public void render(PoseStack matrix, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrix);
        super.render(matrix, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrix, mouseX, mouseY);
    }

    @Override
    protected void init() {
        super.init();
    }


    @Override
    protected void renderLabels(PoseStack matrix, int mouseX, int mouseY) {
        int actualMouseX = mouseX - ((this.width - this.getXSize()) / 2);
        int actualMouseY = mouseY - ((this.height - this.getYSize()) / 2);
        int imageYdifference = 0;
        int invX = 7;
        int titleX = (this.getXSize() - this.minecraft.font.width(name.getString())) / 2;
        if (getMenu().te.isForge()) {imageYdifference = 20; invX =  (this.getXSize() - this.minecraft.font.width(playerInventoryTitle.getString())) / 2;}
        this.minecraft.font.draw(matrix, playerInventoryTitle, invX, this.getYSize() - 93 - imageYdifference, 4210752);
        this.minecraft.font.draw(matrix, name,  titleX, 6 + imageYdifference, 4210752);
        if (getMenu().te.isLiquid() &&
                (mouseX > getGuiLeft() + FluidTank()[0] && mouseX < getGuiLeft() + FluidTank()[0] + 20 && mouseY > getGuiTop() + FluidTank()[1] && mouseY < getGuiTop() + FluidTank()[1] + 22))
            this.renderTooltip(matrix, Component.literal(this.getMenu().getFluidStackStored(false).getDisplayName().getString() +": " + (this.getMenu()).getFluidStackStored(false).getAmount() + " mB"), actualMouseX, actualMouseY);
        if (getMenu().te.hasUpgrade(Registration.ENERGY.get()) &&
                (mouseX > getGuiLeft() + EnergyTank()[0]&& mouseX < getGuiLeft() + EnergyTank()[0] + 16 && mouseY > getGuiTop() + EnergyTank()[1] && mouseY < getGuiTop() + EnergyTank()[1] + 34))
            this.renderTooltip(matrix, Component.literal((this.getMenu()).getEnergyStored()/1000 + " kFE/" + ( this.getMenu()).getEnergyMaxStored()/1000 + "kFE"), actualMouseX, actualMouseY);
        if (storedFactoryUpgradeType(0)) {
            this.addFactoryTooltips(matrix, actualMouseX, actualMouseY, imageYdifference);
        }
        if (getMenu().te.hasXPTank() &&
                (mouseX > getGuiLeft() + XPTank()[0] && mouseX < getGuiLeft() + XPTank()[0] + 16 && mouseY > getGuiTop() + XPTank()[1] && mouseY < getGuiTop() + XPTank()[1] + 16))
            this.renderTooltip(matrix, Component.literal(this.getMenu().getFluidStackStored(true).getDisplayName().getString() +": " + (this.getMenu()).getFluidStackStored(true).getAmount() + " mB"), actualMouseX, actualMouseY);


    }

    private void addFactoryTooltips(PoseStack matrix, int mouseX, int mouseY, int imageYdifference) {
        if (!getMenu().showInventoryButtons()) {
            if (mouseX >= 7 && mouseX <= 24 && mouseY >= FactoryShowButtonY() && mouseY <= FactoryShowButtonY() +13) {
                this.renderTooltip(matrix, Component.translatable("tooltip." + BetterFurnacesReforged.MOD_ID + ".gui_open"), mouseX, mouseY);
            }
        } else {
            if (mouseX >= 7 && mouseX <= 24 && mouseY >= FactoryShowButtonY() && mouseY <= FactoryShowButtonY() +13) {
                this.renderComponentTooltip(matrix, StringHelper.getShiftInfoGui(), mouseX, mouseY);
            }
            if (storedFactoryUpgradeType(3)){
                if (mouseX >= -47 && mouseX <= -34 && mouseY >= 58 && mouseY <= 71) {
                    if (storedFactoryUpgradeType(1)) {
                        List<Component> list = Lists.newArrayList();
                        list.add(Component.translatable("tooltip." + BetterFurnacesReforged.MOD_ID + ".gui_auto_input"));
                        list.add(Component.literal("" + this.getMenu().getAutoInput()));
                        this.renderComponentTooltip(matrix, list, mouseX, mouseY);
                    }
                } else if (mouseX >= -31 && mouseX <= -18 && mouseY >= 58 && mouseY <= 71) {
                    if (storedFactoryUpgradeType(2)) {
                        List<Component> list = Lists.newArrayList();
                        list.add(Component.translatable("tooltip." + BetterFurnacesReforged.MOD_ID + ".gui_auto_output"));
                        list.add(Component.literal("" + this.getMenu().getAutoOutput()));
                        this.renderComponentTooltip(matrix, list, mouseX, mouseY);
                    }
                } else if (mouseX >= -31 && mouseX <= -18 && mouseY >= 74 && mouseY <= 87) {
                    List<Component> list = Lists.newArrayList();
                    list.add(Component.translatable("tooltip." + BetterFurnacesReforged.MOD_ID + ".gui_top"));
                    list.add(this.getMenu().getTooltip(1));
                    this.renderComponentTooltip(matrix, list, mouseX, mouseY);
                } else if (mouseX >= -31 && mouseX <= -18 && mouseY >= 102 && mouseY <= 115) {
                    List<Component> list = Lists.newArrayList();
                    list.add(Component.translatable("tooltip." + BetterFurnacesReforged.MOD_ID + ".gui_bottom"));
                    list.add(this.getMenu().getTooltip(0));
                    this.renderComponentTooltip(matrix, list, mouseX, mouseY);
                } else if (mouseX >= -31 && mouseX <= -18 && mouseY >= 88 && mouseY <= 101) {
                    List<Component> list = Lists.newArrayList();
                    if (isShiftKeyDown()) {
                        list.add(Component.translatable("tooltip." + BetterFurnacesReforged.MOD_ID + ".gui_reset"));
                    } else {
                        list.add(Component.translatable("tooltip." + BetterFurnacesReforged.MOD_ID + ".gui_front"));
                        list.add(this.getMenu().getTooltip(this.getMenu().getIndexFront()));
                    }
                    this.renderComponentTooltip(matrix, list, mouseX, mouseY);
                } else if (mouseX >= -45 && mouseX <= -32 && mouseY >= 88 && mouseY <= 101) {
                    List<Component> list = Lists.newArrayList();
                    list.add(Component.translatable("tooltip." + BetterFurnacesReforged.MOD_ID + ".gui_left"));
                    list.add(this.getMenu().getTooltip(this.getMenu().getIndexLeft()));
                    this.renderComponentTooltip(matrix, list, mouseX, mouseY);
                } else if (mouseX >= -17 && mouseX <= -4 && mouseY >= 88 && mouseY <= 101) {
                    List<Component> list = Lists.newArrayList();
                    list.add(Component.translatable("tooltip." + BetterFurnacesReforged.MOD_ID + ".gui_right"));
                    list.add(this.getMenu().getTooltip(this.getMenu().getIndexRight()));
                    this.renderComponentTooltip(matrix, list, mouseX, mouseY);
                } else if (mouseX >= -45 && mouseX <= -32 && mouseY >= 102 && mouseY <= 115) {
                    List<Component> list = Lists.newArrayList();
                    list.add(Component.translatable("tooltip." + BetterFurnacesReforged.MOD_ID + ".gui_back"));
                    list.add(this.getMenu().getTooltip(this.getMenu().getIndexBack()));
                    this.renderComponentTooltip(matrix, list, mouseX, mouseY);
                }else if(getMenu().te.isForge() && mouseX >= -15 && mouseX <= -2 && mouseY >= 58 && mouseY <= 71) {
                    this.renderTooltip(matrix, Component.translatable("tooltip." + BetterFurnacesReforged.MOD_ID + ".gui_show_orientation"), mouseX, mouseY);
                }
            }
            if (storedFactoryUpgradeType(4)) {
                if (this.getMenu().showInventoryButtons() && this.getMenu().getRedstoneMode() == 4) {
                    int comSub = this.getMenu().getComSub();
                    int i = comSub > 9 ? 28 : 31;
                    this.minecraft.font.draw(matrix, Component.literal("" + comSub), i - 42, 138 - imageYdifference, 4210752);
                    this.minecraft.font.draw(matrix, Component.literal("" + comSub), i - 42, 138 - imageYdifference, ChatFormatting.WHITE.getColor());
                }
                if (mouseX >= -47 && mouseX <= -34 && mouseY >= 118 && mouseY <= 131) {
                    List<Component> list = Lists.newArrayList();
                    list.add(Component.translatable("tooltip." + BetterFurnacesReforged.MOD_ID + ".gui_redstone_ignored"));
                    this.renderComponentTooltip(matrix, list, mouseX, mouseY);
                } else if (mouseX >= -31 && mouseX <= -18 && mouseY >= 118 && mouseY <= 131) {
                    List<Component> list = Lists.newArrayList();
                    if (isShiftKeyDown()) {
                        list.add(Component.translatable("tooltip." + BetterFurnacesReforged.MOD_ID + ".gui_redstone_low"));
                    } else {
                        list.add(Component.translatable("tooltip." + BetterFurnacesReforged.MOD_ID + ".gui_redstone_high"));
                    }
                    this.renderComponentTooltip(matrix, list, mouseX, mouseY);
                } else if (mouseX >= -15 && mouseX <= -2 && mouseY >= 118 && mouseY <= 131) {
                    List<Component> list = Lists.newArrayList();
                    list.add(Component.translatable("tooltip." + BetterFurnacesReforged.MOD_ID + ".gui_redstone_comparator"));
                    this.renderComponentTooltip(matrix, list, mouseX, mouseY);
                } else if (mouseX >= -47 && mouseX <= -34 && mouseY >= 134 && mouseY <= 147) {
                    List<Component> list = Lists.newArrayList();
                    list.add(Component.translatable("tooltip." + BetterFurnacesReforged.MOD_ID + ".gui_redstone_comparator_sub"));
                    this.renderComponentTooltip(matrix, list, mouseX, mouseY);
                }
            }

        }
    }
    protected void blitSmeltingSprites(PoseStack matrix) {
        int i;
        if ((this.getMenu()).BurnTimeGet() > 0) {
            i = (this.getMenu()).getBurnLeftScaled(13);
            this.blit(matrix, getGuiLeft() + 55, getGuiTop() + 37 + 12 - i, 176, 12 - i, 14, i + 1);
        }
        i = (this.getMenu()).getCookScaled(24);
        this.blit(matrix, getGuiLeft() + 79, getGuiTop() + 34, 176, 14, i + 1, 16);
    }
    @Override
    protected void renderBg(PoseStack matrix, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, GUI());
        int relX = (this.width - this.getXSize()) / 2;
        int relY = (this.height - this.getYSize()) / 2;
        this.blit(matrix, relX, relY, 0, 0, this.getXSize(), this.getYSize());
        blitSmeltingSprites(matrix);
        int i;
        if (getMenu().te.hasUpgrade(Registration.ENERGY.get())){
            RenderSystem.setShaderTexture(0, WIDGETS);
            i = ( this.getMenu()).getEnergyStoredScaled(34);
            this.blit(matrix, getGuiLeft() + EnergyTank()[0], getGuiTop() + EnergyTank()[1], 240, 0, 16, 34);
            this.blit(matrix, getGuiLeft() + EnergyTank()[0], getGuiTop() + EnergyTank()[1], 240, 34, 16, 34-i);
        }
        if (getMenu().te.isLiquid()){
            RenderSystem.setShaderTexture(0, WIDGETS);
            this.blit(matrix, getGuiLeft() + FluidTank()[0], getGuiTop() + FluidTank()[1], 192, 38, 20, 22);
            FluidStack fluid =  this.getMenu().getFluidStackStored(false);
            i = this.getMenu().getFluidStoredScaled(21,false);
            if (i > 0) {
                FluidRenderUtil.renderTiledFluid(matrix, this, FluidTank()[0], FluidTank()[1], 20, 22, fluid, false);
                RenderSystem.setShaderTexture(0, WIDGETS);
                this.blit(matrix, getGuiLeft() + FluidTank()[0], getGuiTop() + FluidTank()[1], 192, 38, 20, 22-i);

            }
            RenderSystem.setShaderTexture(0, WIDGETS);
            this.blit(matrix, getGuiLeft() + FluidTank()[0], getGuiTop() + FluidTank()[1], 192, 16, 20, 22);
        }

        if (this.getMenu().te.hasXPTank()) {
            RenderSystem.setShaderTexture(0, WIDGETS);
            this.blit(matrix, getGuiLeft() + XPTank()[0], getGuiTop() + XPTank()[1], 208, 0, 16, 16);
            FluidStack fluid =  this.getMenu().getFluidStackStored(true);
            i = this.getMenu().getFluidStoredScaled(16,true);
            if (i > 0) {
                FluidRenderUtil.renderTiledFluid(matrix, this, XPTank()[0], XPTank()[1], 16, 16, fluid, false);
                RenderSystem.setShaderTexture(0, WIDGETS);
                this.blit(matrix, getGuiLeft() + XPTank()[0], getGuiTop() + XPTank()[1], 208, 0, 16, 16-i);

            }
            RenderSystem.setShaderTexture(0, WIDGETS);
            this.blit(matrix, getGuiLeft() + XPTank()[0], getGuiTop() + XPTank()[1], 192, 0, 16, 16);
        }
        if (storedFactoryUpgradeType(0)) {
            RenderSystem.setShaderTexture(0, WIDGETS);
            int actualMouseX = mouseX - ((this.width - this.getXSize()) / 2);
            int actualMouseY = mouseY - ((this.height - this.getYSize()) / 2);

            this.addInventoryButtons(matrix, actualMouseX, actualMouseY);
            if (storedFactoryUpgradeType(4))
                this.addRedstoneButtons(matrix, actualMouseX, actualMouseY);
        }
    }


    private void addRedstoneButtons(PoseStack matrix, int mouseX, int mouseY) {
        if (this.getMenu().showInventoryButtons()) {
            this.blitRedstone(matrix);
            if (this.getMenu().getRedstoneMode() == 4) {
                int comSub = getMenu().getComSub();
                boolean flag = isShiftKeyDown();
                if (flag) {
                    if (comSub > 0) {
                        this.sub_button = true;
                        if (mouseX >= -31 && mouseX <= -18 && mouseY >= 86 && mouseY <= 147) {
                            this.blit(matrix, getGuiLeft() - 31, getGuiTop() + 134, 14, 0, 14, 14);
                        } else {
                            this.blit(matrix, getGuiLeft() - 31, getGuiTop() + 134, 0, 0, 14, 14);
                        }
                    } else {
                        this.sub_button = false;
                        this.blit(matrix, getGuiLeft() - 31, getGuiTop() + 134, 28, 0, 14, 14);
                    }

                } else {
                    if (comSub < 15) {
                        this.add_button = true;
                        if (mouseX >= -31 && mouseX <= -18 && mouseY >= 134 && mouseY <= 147) {
                            this.blit(matrix, getGuiLeft() - 31, getGuiTop() + 134, 14, 14, 14, 14);
                        } else {
                            this.blit(matrix, getGuiLeft() - 31, getGuiTop() + 134, 0, 14, 14, 14);
                        }
                    } else {
                        this.add_button = false;
                        this.blit(matrix, getGuiLeft() - 31, getGuiTop() + 134, 28, 14, 14, 14);

                    }
                }
            }
        }
    }

    private void addInventoryButtons(PoseStack matrix, int mouseX, int mouseY) {
        if (!getMenu().showInventoryButtons()) {
            this.blit(matrix, getGuiLeft() + 7, getGuiTop() + FactoryShowButtonY(), 0, 28, 18, 14);
            if (mouseX >= 7 && mouseX <= 24 && mouseY >= FactoryShowButtonY() && mouseY <= FactoryShowButtonY() + 13)
                this.blit(matrix, getGuiLeft() + 7, getGuiTop() + FactoryShowButtonY(), 18, 28, 18, 14);
        } else if (getMenu().showInventoryButtons()) {
            this.blit(matrix, getGuiLeft() + 7, getGuiTop() + FactoryShowButtonY(), 18, 28, 18, 14);
            this.blit(matrix, getGuiLeft() - 53, getGuiTop() + 52, 0, 47, 59, 107);
            if (storedFactoryUpgradeType(1)) {
                this.blit(matrix, getGuiLeft() - 47, getGuiTop() + 58, 56, 157, 14, 14);
                if (mouseX >= -47 && mouseX <= -34 && mouseY >= 58 && mouseY <= 71 || this.getMenu().getAutoInput()) {
                    this.blit(matrix, getGuiLeft() - 47, getGuiTop() + 58, 0, 189, 14, 14);
                }
            }
            if (storedFactoryUpgradeType(2)) {
                this.blit(matrix, getGuiLeft() - 31, getGuiTop() + 58, 70, 157, 14, 14);
                if (mouseX >= -31 && mouseX <= -18 && mouseY >= 58 && mouseY <= 71 || this.getMenu().getAutoOutput()) {
                    this.blit(matrix, getGuiLeft() - 31, getGuiTop() + 58, 14, 189, 14, 14);
                }
            }
            if (storedFactoryUpgradeType(3)) {
                if (getMenu().te.isForge()) {
                    this.blit(matrix, getGuiLeft() - 15, getGuiTop() + 58, 168, 189, 14, 14);
                    if (getMenu().te.isForge() && (mouseX >= -15 && mouseX <= -2 && mouseY >= 58 && mouseY <= 71 || this.getMenu().te.getBlockState().getValue(AbstractForgeBlock.SHOW_ORIENTATION))) {
                        this.blit(matrix, getGuiLeft() - 15, getGuiTop() + 58, 182, 189, 14, 14);
                    }
                }
                this.blitIO(matrix);
            }
        }


    }

    private void blitRedstone(PoseStack matrix) {
        boolean flag = isShiftKeyDown();
        this.blit(matrix, getGuiLeft() - 47, getGuiTop() + 118, 28, 203, 14, 14);
        this.blit(matrix, getGuiLeft() - 31, getGuiTop() + 118, 42, 203, 14, 14);
        this.blit(matrix, getGuiLeft() - 15, getGuiTop() + 118, 56, 203, 14, 14);
        this.blit(matrix, getGuiLeft() - 47, getGuiTop() + 134, 70, 203, 14, 14);
        if (flag) {
            this.blit(matrix, getGuiLeft() - 31, getGuiTop() + 118, 84, 189, 14, 14);
        }
        int setting = this.getMenu().getRedstoneMode();
        if (setting == 0) {
            this.blit(matrix, getGuiLeft() - 47, getGuiTop() + 118, 28, 189, 14, 14);
        } else if (setting == 1 && !flag) {
            this.blit(matrix, getGuiLeft() - 31, getGuiTop() + 118, 42, 189, 14, 14);
        } else if (setting == 2) {
            this.blit(matrix, getGuiLeft() - 31, getGuiTop() + 118, 98, 189, 14, 14);
        } else if (setting == 3) {
            this.blit(matrix, getGuiLeft() - 15, getGuiTop() + 118, 56, 189, 14, 14);
        } else if (setting == 4) {
            this.blit(matrix, getGuiLeft() - 47, getGuiTop() + 134, 70, 189, 14, 14);
        }

    }

    private void blitIO(PoseStack matrix) {
        int[] settings = new int[]{0, 0, 0, 0, 0, 0};
        int setting = this.getMenu().getSettingTop();
        if (setting == 0)
            this.blit(matrix, getGuiLeft() - 31, getGuiTop() + 74, 84, 157, 14, 14);
        else if (setting == 1) {
            this.blit(matrix, getGuiLeft() - 31, getGuiTop() + 74, 0, 157, 14, 14);
        } else if (setting == 2) {
            this.blit(matrix, getGuiLeft() - 31, getGuiTop() + 74, 14, 157, 14, 14);
        } else if (setting == 3) {
            this.blit(matrix, getGuiLeft() - 31, getGuiTop() + 74, 28, 157, 14, 14);
        } else if (setting == 4) {
            this.blit(matrix, getGuiLeft() - 31, getGuiTop() + 74, 42, 157, 14, 14);
        }
        settings[1] = setting;

        setting = this.getMenu().getSettingBottom();
        if (setting == 0)
            this.blit(matrix, getGuiLeft() - 31, getGuiTop() + 102, 84, 157, 14, 14);
        else if (setting == 1) {
            this.blit(matrix, getGuiLeft() - 31, getGuiTop() + 102, 0, 157, 14, 14);
        } else if (setting == 2) {
            this.blit(matrix, getGuiLeft() - 31, getGuiTop() + 102, 14, 157, 14, 14);
        } else if (setting == 3) {
            this.blit(matrix, getGuiLeft() - 31, getGuiTop() + 102, 28, 157, 14, 14);
        } else if (setting == 4) {
            this.blit(matrix, getGuiLeft() - 31, getGuiTop() + 102, 42, 157, 14, 14);
        }
        settings[0] = setting;
        setting = this.getMenu().getSettingFront();
        if (setting == 0)
            this.blit(matrix, getGuiLeft() - 31, getGuiTop() + 88, 84, 157, 14, 14);
        else if (setting == 1) {
            this.blit(matrix, getGuiLeft() - 31, getGuiTop() + 88, 0, 157, 14, 14);
        } else if (setting == 2) {
            this.blit(matrix, getGuiLeft() - 31, getGuiTop() + 88, 14, 157, 14, 14);
        } else if (setting == 3) {
            this.blit(matrix, getGuiLeft() - 31, getGuiTop() + 88, 28, 157, 14, 14);
        } else if (setting == 4) {
            this.blit(matrix, getGuiLeft() - 31, getGuiTop() + 88, 42, 157, 14, 14);
        }
        settings[this.getMenu().getIndexFront()] = setting;
        setting = this.getMenu().getSettingBack();
        if (setting == 0)
            this.blit(matrix, getGuiLeft() - 45, getGuiTop() + 102, 84, 157, 14, 14);
        else if (setting == 1) {
            this.blit(matrix, getGuiLeft() - 45, getGuiTop() + 102, 0, 157, 14, 14);
        } else if (setting == 2) {
            this.blit(matrix, getGuiLeft() - 45, getGuiTop() + 102, 14, 157, 14, 14);
        } else if (setting == 3) {
            this.blit(matrix, getGuiLeft() - 45, getGuiTop() + 102, 28, 157, 14, 14);
        } else if (setting == 4) {
            this.blit(matrix, getGuiLeft() - 45, getGuiTop() + 102, 42, 157, 14, 14);
        }
        settings[this.getMenu().getIndexBack()] = setting;
        setting = this.getMenu().getSettingLeft();
        if (setting == 0)
            this.blit(matrix, getGuiLeft() - 45, getGuiTop() + 88, 84, 157, 14, 14);
        else if (setting == 1) {
            this.blit(matrix, getGuiLeft() - 45, getGuiTop() + 88, 0, 157, 14, 14);
        } else if (setting == 2) {
            this.blit(matrix, getGuiLeft() - 45, getGuiTop() + 88, 14, 157, 14, 14);
        } else if (setting == 3) {
            this.blit(matrix, getGuiLeft() - 45, getGuiTop() + 88, 28, 157, 14, 14);
        } else if (setting == 4) {
            this.blit(matrix, getGuiLeft() - 45, getGuiTop() + 88, 42, 157, 14, 14);
        }
        settings[this.getMenu().getIndexLeft()] = setting;
        setting = this.getMenu().getSettingRight();
        if (setting == 0)
            this.blit(matrix, getGuiLeft() - 17, getGuiTop() + 88, 84, 157, 14, 14);
        else if (setting == 1) {
            this.blit(matrix, getGuiLeft() - 17, getGuiTop() + 88, 0, 157, 14, 14);
        } else if (setting == 2) {
            this.blit(matrix, getGuiLeft() - 17, getGuiTop() + 88, 14, 157, 14, 14);
        } else if (setting == 3) {
            this.blit(matrix, getGuiLeft() - 17, getGuiTop() + 88, 28, 157, 14, 14);
        } else if (setting == 4) {
            this.blit(matrix, getGuiLeft() - 17, getGuiTop() + 88, 42, 157, 14, 14);
        }
        settings[this.getMenu().getIndexRight()] = setting;
        boolean input = false;
        boolean output = false;
        boolean both = false;
        boolean fuel = false;
        for (int set : settings) {
            if (set == 1) {
                input = true;
            } else if (set == 2) {
                output = true;
            } else if (set == 3) {
                both = true;
            } else if (set == 4) {
                fuel = true;
            }
        }
        blitSlotsLayer(matrix, input, both, fuel, output);
    }
    protected void blitSlotsLayer(PoseStack matrix, boolean input, boolean both, boolean fuel, boolean output){
        if (input || both) {
            this.blit(matrix, getGuiLeft() + 53, getGuiTop() + 17, 0, 171, 18, 18);
        }
        if (output || both) {
            this.blit(matrix, getGuiLeft() + 111, getGuiTop() + 30, 0, 203, 26, 26);
        }
        if (fuel) {
            this.blit(matrix, getGuiLeft() + 53, getGuiTop() + 53, 18, 171, 18, 18);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        double actualMouseX = mouseX - ((this.width - this.getXSize()) / 2);
        double actualMouseY = mouseY - ((this.height - this.getYSize()) / 2);

        if (storedFactoryUpgradeType(4))
            this.mouseClickedRedstoneButtons(actualMouseX, actualMouseY);
        if (storedFactoryUpgradeType(0))
            this.mouseClickedInventoryButtons(button, actualMouseX, actualMouseY);
        return super.mouseClicked(mouseX, mouseY, button);
    }


    public void mouseClickedInventoryButtons(int button, double mouseX, double mouseY) {
        boolean flag = button == GLFW.GLFW_MOUSE_BUTTON_2;
        if (!getMenu().showInventoryButtons()) {
            if (mouseX >= 7 && mouseX <= 24 && mouseY >= FactoryShowButtonY() && mouseY <= FactoryShowButtonY() + 13) {
                Messages.INSTANCE.sendToServer(new PacketShowSettingsButton(this.getMenu().getPos(), 1));
                Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1F));
            }
        } else {
            if (mouseX >= 7 && mouseX <= 24 && mouseY >= FactoryShowButtonY() && mouseY <= FactoryShowButtonY() + 13) {
                Messages.INSTANCE.sendToServer(new PacketShowSettingsButton(this.getMenu().getPos(), 0));
                Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1F));
            }
            if (storedFactoryUpgradeType(3)) {
                if (mouseX >= -47 && mouseX <= -34 && mouseY >= 58 && mouseY <= 71) {
                    if (storedFactoryUpgradeType(1)) {
                        if (!this.getMenu().getAutoInput()) {
                            Messages.INSTANCE.sendToServer(new PacketSettingsButton(this.getMenu().getPos(), 6, 1));
                            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 0.6F, 0.3F));
                        } else {
                            Messages.INSTANCE.sendToServer(new PacketSettingsButton(this.getMenu().getPos(), 6, 0));
                            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 0.6F, 0.3F));
                        }
                    }
                } else if (mouseX >= -31 && mouseX <= -18 && mouseY >= 58 && mouseY <= 71) {
                    if (storedFactoryUpgradeType(2)) {
                        if (!this.getMenu().getAutoOutput()) {
                            Messages.INSTANCE.sendToServer(new PacketSettingsButton(this.getMenu().getPos(), 7, 1));
                            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 0.6F, 0.3F));
                        } else {
                            Messages.INSTANCE.sendToServer(new PacketSettingsButton(this.getMenu().getPos(), 7, 0));
                            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 0.6F, 0.3F));
                        }
                    }
                }
                if (getMenu().te.isForge() && mouseX >= -15 && mouseX <= -2 && mouseY >= 58 && mouseY <= 71) {
                    Messages.INSTANCE.sendToServer(new PacketOrientationButton(this.getMenu().getPos(), !this.getMenu().te.getBlockState().getValue(AbstractForgeBlock.SHOW_ORIENTATION)));
                    Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 0.6F, 0.3F));
                }

                if (mouseX >= -31 && mouseX <= -18 && mouseY >= 74 && mouseY <= 87) {
                    if (flag) {
                        sendToServerInverted(this.getMenu().getSettingTop(), 1);
                    } else {
                        sendToServer(this.getMenu().getSettingTop(), 1);
                    }
                } else if (mouseX >= -31 && mouseX <= -18 && mouseY >= 102 && mouseY <= 115) {
                    if (flag) {
                        sendToServerInverted(this.getMenu().getSettingBottom(), 0);
                    } else {
                        sendToServer(this.getMenu().getSettingBottom(), 0);
                    }
                } else if (mouseX >= -31 && mouseX <= -18 && mouseY >= 88 && mouseY <= 101) {
                    if (isShiftKeyDown()) {
                        Messages.INSTANCE.sendToServer(new PacketSettingsButton(this.getMenu().getPos(), 0, 0));
                        Messages.INSTANCE.sendToServer(new PacketSettingsButton(this.getMenu().getPos(), 1, 0));
                        Messages.INSTANCE.sendToServer(new PacketSettingsButton(this.getMenu().getPos(), 2, 0));
                        Messages.INSTANCE.sendToServer(new PacketSettingsButton(this.getMenu().getPos(), 3, 0));
                        Messages.INSTANCE.sendToServer(new PacketSettingsButton(this.getMenu().getPos(), 4, 0));
                        Messages.INSTANCE.sendToServer(new PacketSettingsButton(this.getMenu().getPos(), 5, 0));
                        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 0.8F, 0.3F));
                    } else {
                        if (flag) {
                            sendToServerInverted(this.getMenu().getSettingFront(), this.getMenu().getIndexFront());
                        } else {
                            sendToServer(this.getMenu().getSettingFront(), this.getMenu().getIndexFront());
                        }
                    }
                } else if (mouseX >= -45 && mouseX <= -32 && mouseY >= 102 && mouseY <= 115) {
                    if (flag) {
                        sendToServerInverted(this.getMenu().getSettingBack(), this.getMenu().getIndexBack());
                    } else {
                        sendToServer(this.getMenu().getSettingBack(), this.getMenu().getIndexBack());
                    }
                } else if (mouseX >= -45 && mouseX <= -32 && mouseY >= 88 && mouseY <= 101) {
                    if (flag) {
                        sendToServerInverted(this.getMenu().getSettingLeft(), this.getMenu().getIndexLeft());
                    } else {
                        sendToServer(this.getMenu().getSettingLeft(), this.getMenu().getIndexLeft());
                    }
                } else if (mouseX >= -17 && mouseX <= -4 && mouseY >= 88 && mouseY <= 101) {
                    if (flag) {
                        sendToServerInverted(this.getMenu().getSettingRight(), this.getMenu().getIndexRight());
                    } else {
                        sendToServer(this.getMenu().getSettingRight(), this.getMenu().getIndexRight());
                    }
                }
            }
        }
    }

    private void sendToServer(int setting, int index) {
        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 0.6F, 0.3F));
        if (setting <= 0) {
            Messages.INSTANCE.sendToServer(new PacketSettingsButton(this.getMenu().getPos(), index, 1));
        } else if (setting == 1) {
            Messages.INSTANCE.sendToServer(new PacketSettingsButton(this.getMenu().getPos(), index, 2));
        } else if (setting == 2) {
            Messages.INSTANCE.sendToServer(new PacketSettingsButton(this.getMenu().getPos(), index, 3));
        } else if (setting == 3) {
            Messages.INSTANCE.sendToServer(new PacketSettingsButton(this.getMenu().getPos(), index, 4));
        } else if (setting >= 4) {
            Messages.INSTANCE.sendToServer(new PacketSettingsButton(this.getMenu().getPos(), index, 0));
        }
    }

    private void sendToServerInverted(int setting, int index) {
        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 0.3F, 0.3F));
        if (setting <= 0) {
            Messages.INSTANCE.sendToServer(new PacketSettingsButton(this.getMenu().getPos(), index, 4));
        } else if (setting == 1) {
            Messages.INSTANCE.sendToServer(new PacketSettingsButton(this.getMenu().getPos(), index, 0));
        } else if (setting == 2) {
            Messages.INSTANCE.sendToServer(new PacketSettingsButton(this.getMenu().getPos(), index, 1));
        } else if (setting == 3) {
            Messages.INSTANCE.sendToServer(new PacketSettingsButton(this.getMenu().getPos(), index, 2));
        } else if (setting >= 4) {
            Messages.INSTANCE.sendToServer(new PacketSettingsButton(this.getMenu().getPos(), index, 3));
        }
    }

    public void mouseClickedRedstoneButtons(double mouseX, double mouseY) {
        if (getMenu().showInventoryButtons()) {
            if (mouseX >= -31 && mouseX <= -18 && mouseY >= 132 && mouseY <= 147) {
                if (this.sub_button && isShiftKeyDown()) {
                    Messages.INSTANCE.sendToServer(new PacketSettingsButton(this.getMenu().getPos(), 9, this.getMenu().getComSub() - 1));
                    Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 0.3F, 0.3F));
                }
            }
            if (mouseX >= -31 && mouseX <= -18 && mouseY >= 132 && mouseY <= 147) {
                if (this.add_button && !isShiftKeyDown()) {
                    Messages.INSTANCE.sendToServer(new PacketSettingsButton(this.getMenu().getPos(), 9, this.getMenu().getComSub() + 1));
                    Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 0.6F, 0.3F));
                }
            }
            if (mouseX >= -47 && mouseX <= -34 && mouseY >= 118 && mouseY <= 131) {
                if (this.getMenu().getRedstoneMode() != 0) {
                    Messages.INSTANCE.sendToServer(new PacketSettingsButton(this.getMenu().getPos(), 8, 0));
                    Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 0.6F, 0.3F));
                }
            }

            if (mouseX >= -31 && mouseX <= -18 && mouseY >= 118 && mouseY <= 131) {
                if (this.getMenu().getRedstoneMode() != 1 && !isShiftKeyDown()) {
                    Messages.INSTANCE.sendToServer(new PacketSettingsButton(this.getMenu().getPos(), 8, 1));
                    Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 0.6F, 0.3F));
                }
                if (this.getMenu().getRedstoneMode() != 2 && isShiftKeyDown()) {
                    Messages.INSTANCE.sendToServer(new PacketSettingsButton(this.getMenu().getPos(), 8, 2));
                    Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 0.6F, 0.3F));
                }
            }

            if (mouseX >= -15 && mouseX <= -2 && mouseY >= 118 && mouseY <= 131) {
                if (this.getMenu().getRedstoneMode() != 3) {
                    Messages.INSTANCE.sendToServer(new PacketSettingsButton(this.getMenu().getPos(), 8, 3));
                    Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 0.6F, 0.3F));
                }
            }

            if (mouseX >= -47 && mouseX <= -34 && mouseY >= 134 && mouseY <= 147) {
                if (this.getMenu().getRedstoneMode() != 4) {
                    Messages.INSTANCE.sendToServer(new PacketSettingsButton(this.getMenu().getPos(), 8, 4));
                    Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 0.6F, 0.3F));
                }

            }
        }
    }

    public static boolean isShiftKeyDown() {
        return isKeyDown(GLFW.GLFW_KEY_LEFT_SHIFT) || isKeyDown(GLFW.GLFW_KEY_RIGHT_SHIFT);
    }

    public static boolean isKeyDown(int glfw) {
        InputConstants.Key key = InputConstants.Type.KEYSYM.getOrCreate(glfw);
        int keyCode = key.getValue();
        if (keyCode != InputConstants.UNKNOWN.getValue()) {
            long windowHandle = Minecraft.getInstance().getWindow().getWindow();
            try {
                if (key.getType() == InputConstants.Type.KEYSYM) {
                    return InputConstants.isKeyDown(windowHandle, keyCode);
                } /**else if (key.getType() == InputMappings.Type.MOUSE) {
                 return GLFW.glfwGetMouseButton(windowHandle, keyCode) == GLFW.GLFW_PRESS;
                 }**/
            } catch (Exception ignored) {
            }
        }
        return false;
    }
}
