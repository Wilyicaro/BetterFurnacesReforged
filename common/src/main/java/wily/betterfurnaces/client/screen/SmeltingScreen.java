package wily.betterfurnaces.client.screen;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import me.shedaniel.architectury.fluid.FluidStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import org.lwjgl.glfw.GLFW;
import wily.betterfurnaces.BetterFurnacesReforged;
import wily.betterfurnaces.init.Registration;
import wily.betterfurnaces.inventory.SmeltingMenu;
import wily.betterfurnaces.items.FactoryUpgradeItem;
import wily.betterfurnaces.items.GeneratorUpgradeItem;
import wily.betterfurnaces.network.Messages;
import wily.betterfurnaces.network.PacketOrientationButton;
import wily.betterfurnaces.network.PacketSettingsButton;
import wily.betterfurnaces.network.PacketShowSettingsButton;
import wily.betterfurnaces.util.StringHelper;
import wily.factoryapi.ItemContainerUtil;
import wily.factoryapi.base.FactoryDrawableType;
import wily.factoryapi.base.Progress;
import wily.factoryapi.util.StorageStringUtil;

import java.util.List;

import static wily.factoryapi.util.StorageStringUtil.getFluidTooltip;

public class SmeltingScreen<T extends SmeltingMenu> extends AbstractBasicScreen<T> {

    public ResourceLocation GUI() {return new ResourceLocation(BetterFurnacesReforged.MOD_ID + ":" + "textures/container/furnace_gui.png");}
    public static final ResourceLocation WIDGETS = new ResourceLocation(BetterFurnacesReforged.MOD_ID + ":" + "textures/container/widgets.png");
    Inventory playerInv;

    // Widgets x and y pos
    protected int factoryShowButtonY() {return 3;}
    protected int[] fluidTankPos() {return new int[]{73,49};} // 20x22pixels
    protected int[] energyCellPos() {
        return getMenu().be.hasUpgrade(Registration.GENERATOR.get()) ? new int[]{116,26} : new int[]{31-(menu.be.hasUpgradeType(Registration.STORAGE.get()) ? 5 : 0),17};
    } // 16x34pixels
    protected int[] xpTankPos() { return new int[]{116,57};} // 16x16pixels

    public static FactoryDrawableType.DrawableProgress MINI_FLUID_TANK = BFProgressType(Progress.Identifier.TANK,new int[]{192,0,16,16},false, FactoryDrawableType.Direction.VERTICAL);

    public static FactoryDrawableType.DrawableProgress FLUID_TANK = BFProgressType(Progress.Identifier.TANK,new int[]{192,16,20,22},false,FactoryDrawableType.Direction.VERTICAL);

    public static FactoryDrawableType.DrawableProgress ENERGY_CELL = BFProgressType(Progress.Identifier.ENERGY_STORAGE,new int[]{240,0,16,34},false, FactoryDrawableType.Direction.VERTICAL);
    public static FactoryDrawableType.DrawableProgress THIN_ENERGY_CELL = BFProgressType(Progress.Identifier.ENERGY_STORAGE,new int[]{248,102,8,34},false, FactoryDrawableType.Direction.VERTICAL);

    public static FactoryDrawableType SLOT = FactoryDrawableType.create(WIDGETS,192,60,18,18);
    public static FactoryDrawableType BIG_SLOT = FactoryDrawableType.create(WIDGETS,210,60,26,26);
    public static FactoryDrawableType INPUT_SLOT_OUTLINE = FactoryDrawableType.create(WIDGETS,0,171,18,18);
    public static FactoryDrawableType FUEL_SLOT_OUTLINE = FactoryDrawableType.create(WIDGETS,18,171,18,18);
    public static FactoryDrawableType OUTPUT_SLOT_OUTLINE = FactoryDrawableType.create(WIDGETS,36,171,18,18);
    public static FactoryDrawableType BIG_OUTPUT_SLOT_OUTLINE = FactoryDrawableType.create(WIDGETS,0,203,26,26);
    public static FactoryDrawableType.DrawableProgress BFProgressType(Progress.Identifier identifier, int[] uvSize, boolean reverse, FactoryDrawableType.Direction plane) {
        return FactoryDrawableType.create(WIDGETS,uvSize[0],uvSize[1],uvSize[2],uvSize[3]).asProgress(identifier, reverse, plane);
    }
    protected FactoryDrawableType.DrawableProgress energyCellDrawable(){
        return menu.be.hasUpgradeType(Registration.STORAGE.get()) ? THIN_ENERGY_CELL : ENERGY_CELL;
    }
    private boolean storedFactoryUpgradeType(int type){
        if (getMenu().be.hasUpgradeType(Registration.FACTORY.get())) {
            FactoryUpgradeItem stack = ((FactoryUpgradeItem)getMenu().be.getUpgradeTypeSlotItem(Registration.FACTORY.get()).getItem());
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

    public SmeltingScreen(T t, Inventory inv, Component name) {
        super(t, inv, name);
        playerInv = inv;
    }

    @Override
    public void render(PoseStack matrix, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrix);
        super.render(matrix, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrix, mouseX, mouseY);
    }

    @Override
    public Component getTitle() {
        return getMenu().be.getName();
    }

    @Override
    protected void renderLabels(PoseStack matrix, int mouseX, int mouseY) {
        int actualMouseX = mouseX - relX();
        int actualMouseY = mouseY - relY();
        int invX = 7;

        int titleX = (this.imageWidth - this.minecraft.font.width(getTitle().getString())) / 2;
        if (getMenu().be.isForge())
            invX = (this.imageWidth - this.minecraft.font.width(inventory.getName().getString())) / 2;
        this.minecraft.font.draw(matrix, inventory.getName(), invX, this.imageHeight - 93, 4210752);
        this.minecraft.font.draw(matrix, getTitle(), titleX, imageHeight - 160, 4210752);
        if (getMenu().be.isLiquid() && FLUID_TANK.inMouseLimit(actualMouseX, actualMouseY, fluidTankPos()[0], fluidTankPos()[1]))
            this.renderTooltip(matrix, getFluidTooltip("tooltip.factory_api.fluid_stored", getMenu().be.fluidTank), actualMouseX, actualMouseY);
        if (getMenu().be.hasUpgrade(Registration.GENERATOR.get()) && MINI_FLUID_TANK.inMouseLimit(actualMouseX, actualMouseY, 54, 18)){
            ItemStack gen = getMenu().be.getUpgradeSlotItem(Registration.GENERATOR.get());
            this.renderTooltip(matrix, getFluidTooltip("tooltip.factory_api.fluid_stored", ((GeneratorUpgradeItem)gen.getItem()).getFluidStorage(gen)), actualMouseX, actualMouseY);
    }if ((getMenu().be.hasUpgrade(Registration.ENERGY.get()) || getMenu().be.hasUpgrade(Registration.GENERATOR.get())) && ENERGY_CELL.inMouseLimit(actualMouseX,actualMouseY, energyCellPos()[0], energyCellPos()[1])){
            this.renderTooltip(matrix, StorageStringUtil.getEnergyTooltip("tooltip.factory_api.energy_stored", getMenu().be.energyStorage), actualMouseX, actualMouseY);
        }if (storedFactoryUpgradeType(0)) {
            this.addFactoryTooltips(matrix, actualMouseX, actualMouseY);
        }
        if (getMenu().be.hasXPTank() && MINI_FLUID_TANK.inMouseLimit(actualMouseX, actualMouseY, xpTankPos()[0], xpTankPos()[1]))
            this.renderTooltip(matrix,getFluidTooltip("tooltip.factory_api.fluid_stored", getMenu().be.xpTank), actualMouseX, actualMouseY);

    }

    private void addFactoryTooltips(PoseStack matrix, int mouseX, int mouseY) {
        if (!getMenu().showInventoryButtons()) {
            if (mouseX >= 7 && mouseX <= 24 && mouseY >= factoryShowButtonY() && mouseY <= factoryShowButtonY() +13) {
                this.renderTooltip(matrix, new TranslatableComponent("tooltip." + BetterFurnacesReforged.MOD_ID + ".gui_open"), mouseX, mouseY);
            }
        } else {
            if (mouseX >= 7 && mouseX <= 24 && mouseY >= factoryShowButtonY() && mouseY <= factoryShowButtonY() +13) {
                this.renderComponentTooltip(matrix, StringHelper.getShiftInfoGui(), mouseX, mouseY);
            }
            if (storedFactoryUpgradeType(3)){
                if (mouseX >= -47 && mouseX <= -34 && mouseY >= 58 && mouseY <= 71) {
                    if (storedFactoryUpgradeType(1)) {
                        List<Component> list = Lists.newArrayList();
                        list.add(new TranslatableComponent("tooltip." + BetterFurnacesReforged.MOD_ID + ".gui_auto_input"));
                        list.add(new TextComponent("" + this.getMenu().getAutoInput()));
                        this.renderComponentTooltip(matrix, list, mouseX, mouseY);
                    }
                } else if (mouseX >= -31 && mouseX <= -18 && mouseY >= 58 && mouseY <= 71) {
                    if (storedFactoryUpgradeType(2)) {
                        List<Component> list = Lists.newArrayList();
                        list.add(new TranslatableComponent("tooltip." + BetterFurnacesReforged.MOD_ID + ".gui_auto_output"));
                        list.add(new TextComponent("" + this.getMenu().getAutoOutput()));
                        this.renderComponentTooltip(matrix, list, mouseX, mouseY);
                    }
                } else if (mouseX >= -31 && mouseX <= -18 && mouseY >= 74 && mouseY <= 87) {
                    List<Component> list = Lists.newArrayList();
                    list.add(new TranslatableComponent("tooltip." + BetterFurnacesReforged.MOD_ID + ".gui_top"));
                    list.add(this.getMenu().getTooltip(1));
                    this.renderComponentTooltip(matrix, list, mouseX, mouseY);
                } else if (mouseX >= -31 && mouseX <= -18 && mouseY >= 102 && mouseY <= 115) {
                    List<Component> list = Lists.newArrayList();
                    list.add(new TranslatableComponent("tooltip." + BetterFurnacesReforged.MOD_ID + ".gui_bottom"));
                    list.add(this.getMenu().getTooltip(0));
                    this.renderComponentTooltip(matrix, list, mouseX, mouseY);
                } else if (mouseX >= -31 && mouseX <= -18 && mouseY >= 88 && mouseY <= 101) {
                    List<Component> list = Lists.newArrayList();
                    if (isShiftKeyDown()) {
                        list.add(new TranslatableComponent("tooltip." + BetterFurnacesReforged.MOD_ID + ".gui_reset"));
                    } else {
                        list.add(new TranslatableComponent("tooltip." + BetterFurnacesReforged.MOD_ID + ".gui_front"));
                        list.add(this.getMenu().getTooltip(this.getMenu().getIndexFront()));
                    }
                    this.renderComponentTooltip(matrix, list, mouseX, mouseY);
                } else if (mouseX >= -45 && mouseX <= -32 && mouseY >= 88 && mouseY <= 101) {
                    List<Component> list = Lists.newArrayList();
                    list.add(new TranslatableComponent("tooltip." + BetterFurnacesReforged.MOD_ID + ".gui_left"));
                    list.add(this.getMenu().getTooltip(this.getMenu().getIndexLeft()));
                    this.renderComponentTooltip(matrix, list, mouseX, mouseY);
                } else if (mouseX >= -17 && mouseX <= -4 && mouseY >= 88 && mouseY <= 101) {
                    List<Component> list = Lists.newArrayList();
                    list.add(new TranslatableComponent("tooltip." + BetterFurnacesReforged.MOD_ID + ".gui_right"));
                    list.add(this.getMenu().getTooltip(this.getMenu().getIndexRight()));
                    this.renderComponentTooltip(matrix, list, mouseX, mouseY);
                } else if (mouseX >= -45 && mouseX <= -32 && mouseY >= 102 && mouseY <= 115) {
                    List<Component> list = Lists.newArrayList();
                    list.add(new TranslatableComponent("tooltip." + BetterFurnacesReforged.MOD_ID + ".gui_back"));
                    list.add(this.getMenu().getTooltip(this.getMenu().getIndexBack()));
                    this.renderComponentTooltip(matrix, list, mouseX, mouseY);
                }else if( mouseX >= -15 && mouseX <= -2 && mouseY >= 58 && mouseY <= 71) {
                    this.renderTooltip(matrix, new TranslatableComponent("tooltip." + BetterFurnacesReforged.MOD_ID + ".gui_show_orientation"), mouseX, mouseY);
                }
            }
            if (storedFactoryUpgradeType(4)) {
                if (this.getMenu().showInventoryButtons() && this.getMenu().getRedstoneMode() == 4) {
                    int comSub = this.getMenu().getComSub();
                    int i = comSub > 9 ? 28 : 31;
                    this.minecraft.font.draw(matrix, new TextComponent("" + comSub), i - 42, 138, 4210752);
                    this.minecraft.font.draw(matrix, new TextComponent("" + comSub), i - 42, 138, ChatFormatting.WHITE.getColor());
                }
                if (mouseX >= -47 && mouseX <= -34 && mouseY >= 118 && mouseY <= 131) {
                    List<Component> list = Lists.newArrayList();
                    list.add(new TranslatableComponent("tooltip." + BetterFurnacesReforged.MOD_ID + ".gui_redstone_ignored"));
                    this.renderComponentTooltip(matrix, list, mouseX, mouseY);
                } else if (mouseX >= -31 && mouseX <= -18 && mouseY >= 118 && mouseY <= 131) {
                    List<Component> list = Lists.newArrayList();
                    if (isShiftKeyDown()) {
                        list.add(new TranslatableComponent("tooltip." + BetterFurnacesReforged.MOD_ID + ".gui_redstone_low"));
                    } else {
                        list.add(new TranslatableComponent("tooltip." + BetterFurnacesReforged.MOD_ID + ".gui_redstone_high"));
                    }
                    this.renderComponentTooltip(matrix, list, mouseX, mouseY);
                } else if (mouseX >= -15 && mouseX <= -2 && mouseY >= 118 && mouseY <= 131) {
                    List<Component> list = Lists.newArrayList();
                    list.add(new TranslatableComponent("tooltip." + BetterFurnacesReforged.MOD_ID + ".gui_redstone_comparator"));
                    this.renderComponentTooltip(matrix, list, mouseX, mouseY);
                } else if (mouseX >= -47 && mouseX <= -34 && mouseY >= 134 && mouseY <= 147) {
                    List<Component> list = Lists.newArrayList();
                    list.add(new TranslatableComponent("tooltip." + BetterFurnacesReforged.MOD_ID + ".gui_redstone_comparator_sub"));
                    this.renderComponentTooltip(matrix, list, mouseX, mouseY);
                }
            }

        }
    }
    protected void blitSmeltingSprites(PoseStack matrix) {
        int i;
        if ((this.getMenu()).BurnTimeGet() > 0) {
            i = (this.getMenu()).getBurnLeftScaled(13);
            blit(matrix, relX() + 55, relY() + 37 + 12 - i, 176, 12 - i, 14, i + 1);
        }
        i = (this.getMenu()).getCookScaled(24);
        blit(matrix, relX() + 79, relY() + 34, 176, 14, i + 1, 16);
        minecraft.getTextureManager().bind( WIDGETS);
        SLOT.draw(matrix, relX() + 53, relY() + 17);
        boolean storage = menu.be.hasUpgradeType(Registration.STORAGE.get());
        if (storage){
            SLOT.draw(matrix, relX() + 35, relY() + 17);
            SLOT.draw(matrix, relX() + 35, relY() + 53);
        }
        if (!getMenu().be.hasUpgrade(Registration.GENERATOR.get())) {
            BIG_SLOT.draw(matrix, relX() + 111, relY() + 30);
            if (storage) SLOT.draw(matrix, relX() + 137, relY() + 34);
        }
    }
    @Override
    protected void renderBg(PoseStack stack, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        minecraft.getTextureManager().bind(GUI());
        blit(stack, relX(), relY(), 0, 0, imageWidth, imageHeight);
        blitSmeltingSprites(stack);
        if (getMenu().be.hasUpgrade(Registration.ENERGY.get()) || getMenu().be.hasUpgrade(Registration.GENERATOR.get())) {
            boolean storage = menu.be.hasUpgradeType(Registration.STORAGE.get());
            blit(stack, relX() + energyCellPos()[0], relY() + energyCellPos()[1], 240 + (storage ? 8 : 0), 34 * (storage ? 2 : 1), 16 - (storage ? 8 : 0), 34);
            energyCellDrawable().drawProgress(stack, relX() + energyCellPos()[0], relY() + energyCellPos()[1], this.getMenu().getEnergyStoredScaled(34));
        }if (getMenu().be.isLiquid()){
            minecraft.getTextureManager().bind(WIDGETS);
            blit(stack, relX() + fluidTankPos()[0], relY() + fluidTankPos()[1], 192, 38, 20, 22);
            FLUID_TANK.drawAsFluidTank(stack,relX() + fluidTankPos()[0], relY() + fluidTankPos()[1], this.getMenu().getFluidStoredScaled(22,false), this.getMenu().getFluidStackStored(false),false);
        }

        if (this.getMenu().be.hasXPTank()) {
            minecraft.getTextureManager().bind( WIDGETS);
            blit(stack, relX() + xpTankPos()[0], relY() + xpTankPos()[1], 208, 0, 16, 16);
            MINI_FLUID_TANK.drawAsFluidTank(stack,relX() + xpTankPos()[0], relY() + xpTankPos()[1], this.getMenu().getFluidStoredScaled(16,true), this.getMenu().getFluidStackStored(true),true);
        }
        if (this.getMenu().be.hasUpgrade(Registration.GENERATOR.get())) {
            ItemStack generatorUp = getMenu().be.getUpgradeSlotItem(Registration.GENERATOR.get());
            MINI_FLUID_TANK.drawAsFluidTank(stack,relX() + 54, relY() + 18, (ItemContainerUtil.getFluid(generatorUp).getAmount().intValue() * 16 / (4 * FluidStack.bucketAmount().intValue())),ItemContainerUtil.getFluid(generatorUp),true);
        }
        if (storedFactoryUpgradeType(0)) {
            minecraft.getTextureManager().bind( WIDGETS);
            int actualMouseX = mouseX - relY();
            int actualMouseY = mouseY - relX();

            this.addInventoryButtons(stack, actualMouseX, actualMouseY);
            if (storedFactoryUpgradeType(4))
                this.addRedstoneButtons(stack, actualMouseX, actualMouseY);
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
                            this.blit(matrix, relX() - 31, relY() + 134, 14, 0, 14, 14);
                        } else {
                            this.blit(matrix, relX() - 31, relY() + 134, 0, 0, 14, 14);
                        }
                    } else {
                        this.sub_button = false;
                        this.blit(matrix, relX() - 31, relY() + 134, 28, 0, 14, 14);
                    }

                } else {
                    if (comSub < 15) {
                        this.add_button = true;
                        if (mouseX >= -31 && mouseX <= -18 && mouseY >= 134 && mouseY <= 147) {
                            this.blit(matrix, relX() - 31, relY() + 134, 14, 14, 14, 14);
                        } else {
                            this.blit(matrix, relX() - 31, relY() + 134, 0, 14, 14, 14);
                        }
                    } else {
                        this.add_button = false;
                        this.blit(matrix, relX() - 31, relY() + 134, 28, 14, 14, 14);

                    }
                }
            }
        }
    }

    private void addInventoryButtons(PoseStack matrix, int mouseX, int mouseY) {
        if (!getMenu().showInventoryButtons()) {
            this.blit(matrix, relX() + 7, relY() + factoryShowButtonY(), 0, 28, 18, 14);
            if (mouseX >= 7 && mouseX <= 24 && mouseY >= factoryShowButtonY() && mouseY <= factoryShowButtonY() + 13)
                this.blit(matrix, relX() + 7, relY() + factoryShowButtonY(), 18, 28, 18, 14);
        } else if (getMenu().showInventoryButtons()) {
            this.blit(matrix, relX() + 7, relY() + factoryShowButtonY(), 18, 28, 18, 14);
            this.blit(matrix, relX() - 53, relY() + 52, 0, 47, 59, 107);
            if (storedFactoryUpgradeType(1)) {
                this.blit(matrix, relX() - 47, relY() + 58, 56, 157, 14, 14);
                if (mouseX >= -47 && mouseX <= -34 && mouseY >= 58 && mouseY <= 71 || this.getMenu().getAutoInput()) {
                    this.blit(matrix, relX() - 47, relY() + 58, 0, 189, 14, 14);
                }
            }
            if (storedFactoryUpgradeType(2)) {
                this.blit(matrix, relX() - 31, relY() + 58, 70, 157, 14, 14);
                if (mouseX >= -31 && mouseX <= -18 && mouseY >= 58 && mouseY <= 71 || this.getMenu().getAutoOutput()) {
                    this.blit(matrix, relX() - 31, relY() + 58, 14, 189, 14, 14);
                }
            }
            if (storedFactoryUpgradeType(3)) {
                    this.blit(matrix, relX() - 15, relY() + 58, 168, 189, 14, 14);
                    if ( (mouseX >= -15 && mouseX <= -2 && mouseY >= 58 && mouseY <= 71 || this.getMenu().be.showOrientation)) {
                        this.blit(matrix, relX() - 15, relY() + 58, 182, 189, 14, 14);
                    }
                this.blitIO(matrix);
            }
        }


    }

    private void blitRedstone(PoseStack matrix) {
        boolean flag = isShiftKeyDown();
        this.blit(matrix, relX() - 47, relY() + 118, 28, 203, 14, 14);
        this.blit(matrix, relX() - 31, relY() + 118, 42, 203, 14, 14);
        this.blit(matrix, relX() - 15, relY() + 118, 56, 203, 14, 14);
        this.blit(matrix, relX() - 47, relY() + 134, 70, 203, 14, 14);
        if (flag) {
            this.blit(matrix, relX() - 31, relY() + 118, 84, 189, 14, 14);
        }
        int setting = this.getMenu().getRedstoneMode();
        if (setting == 0) {
            this.blit(matrix, relX() - 47, relY() + 118, 28, 189, 14, 14);
        } else if (setting == 1 && !flag) {
            this.blit(matrix, relX() - 31, relY() + 118, 42, 189, 14, 14);
        } else if (setting == 2) {
            this.blit(matrix, relX() - 31, relY() + 118, 98, 189, 14, 14);
        } else if (setting == 3) {
            this.blit(matrix, relX() - 15, relY() + 118, 56, 189, 14, 14);
        } else if (setting == 4) {
            this.blit(matrix, relX() - 47, relY() + 134, 70, 189, 14, 14);
        }

    }

    private void blitIO(PoseStack matrix) {
        int[] settings = new int[]{0, 0, 0, 0, 0, 0};
        int setting = this.getMenu().getSettingTop();
        if (setting == 0)
            this.blit(matrix, relX() - 31, relY() + 74, 84, 157, 14, 14);
        else if (setting == 1) {
            this.blit(matrix, relX() - 31, relY() + 74, 0, 157, 14, 14);
        } else if (setting == 2) {
            this.blit(matrix, relX() - 31, relY() + 74, 14, 157, 14, 14);
        } else if (setting == 3) {
            this.blit(matrix, relX() - 31, relY() + 74, 28, 157, 14, 14);
        } else if (setting == 4) {
            this.blit(matrix, relX() - 31, relY() + 74, 42, 157, 14, 14);
        }
        settings[1] = setting;

        setting = this.getMenu().getSettingBottom();
        if (setting == 0)
            this.blit(matrix, relX() - 31, relY() + 102, 84, 157, 14, 14);
        else if (setting == 1) {
            this.blit(matrix, relX() - 31, relY() + 102, 0, 157, 14, 14);
        } else if (setting == 2) {
            this.blit(matrix, relX() - 31, relY() + 102, 14, 157, 14, 14);
        } else if (setting == 3) {
            this.blit(matrix, relX() - 31, relY() + 102, 28, 157, 14, 14);
        } else if (setting == 4) {
            this.blit(matrix, relX() - 31, relY() + 102, 42, 157, 14, 14);
        }
        settings[0] = setting;
        setting = this.getMenu().getSettingFront();
        if (setting == 0)
            this.blit(matrix, relX() - 31, relY() + 88, 84, 157, 14, 14);
        else if (setting == 1) {
            this.blit(matrix, relX() - 31, relY() + 88, 0, 157, 14, 14);
        } else if (setting == 2) {
            this.blit(matrix, relX() - 31, relY() + 88, 14, 157, 14, 14);
        } else if (setting == 3) {
            this.blit(matrix, relX() - 31, relY() + 88, 28, 157, 14, 14);
        } else if (setting == 4) {
            this.blit(matrix, relX() - 31, relY() + 88, 42, 157, 14, 14);
        }
        settings[this.getMenu().getIndexFront()] = setting;
        setting = this.getMenu().getSettingBack();
        if (setting == 0)
            this.blit(matrix, relX() - 45, relY() + 102, 84, 157, 14, 14);
        else if (setting == 1) {
            this.blit(matrix, relX() - 45, relY() + 102, 0, 157, 14, 14);
        } else if (setting == 2) {
            this.blit(matrix, relX() - 45, relY() + 102, 14, 157, 14, 14);
        } else if (setting == 3) {
            this.blit(matrix, relX() - 45, relY() + 102, 28, 157, 14, 14);
        } else if (setting == 4) {
            this.blit(matrix, relX() - 45, relY() + 102, 42, 157, 14, 14);
        }
        settings[this.getMenu().getIndexBack()] = setting;
        setting = this.getMenu().getSettingLeft();
        if (setting == 0)
            this.blit(matrix, relX() - 45, relY() + 88, 84, 157, 14, 14);
        else if (setting == 1) {
            this.blit(matrix, relX() - 45, relY() + 88, 0, 157, 14, 14);
        } else if (setting == 2) {
            this.blit(matrix, relX() - 45, relY() + 88, 14, 157, 14, 14);
        } else if (setting == 3) {
            this.blit(matrix, relX() - 45, relY() + 88, 28, 157, 14, 14);
        } else if (setting == 4) {
            this.blit(matrix, relX() - 45, relY() + 88, 42, 157, 14, 14);
        }
        settings[this.getMenu().getIndexLeft()] = setting;
        setting = this.getMenu().getSettingRight();
        if (setting == 0)
            this.blit(matrix, relX() - 17, relY() + 88, 84, 157, 14, 14);
        else if (setting == 1) {
            this.blit(matrix, relX() - 17, relY() + 88, 0, 157, 14, 14);
        } else if (setting == 2) {
            this.blit(matrix, relX() - 17, relY() + 88, 14, 157, 14, 14);
        } else if (setting == 3) {
            this.blit(matrix, relX() - 17, relY() + 88, 28, 157, 14, 14);
        } else if (setting == 4) {
            this.blit(matrix, relX() - 17, relY() + 88, 42, 157, 14, 14);
        }
        settings[this.getMenu().getIndexRight()] = setting;
        boolean input = false;
        boolean output = false;
        boolean both = false;
        boolean fuel = false;
        for (int set : settings) {
            if (set == 1) input = true;
            else if (set == 2) output = true;
            else if (set == 3) both = true;
            else if (set == 4) fuel = true;
        }
        blitSlotsLayer(matrix, input, both, fuel, output);
    }
    protected void blitSlotsLayer(PoseStack stack, boolean input, boolean both, boolean fuel, boolean output){
        boolean storage = menu.be.hasUpgradeType(Registration.STORAGE.get());
        if (!getMenu().be.hasUpgrade(Registration.GENERATOR.get())) {
            if (input || both) {
                if (storage) INPUT_SLOT_OUTLINE.draw(stack, relX() + 35, relY() + 17);
                INPUT_SLOT_OUTLINE.draw(stack, relX() + 53, relY() + 17);
            }
            if (output || both) {
                if (storage) OUTPUT_SLOT_OUTLINE.draw(stack, relX() + 137, relY() + 34);
                BIG_OUTPUT_SLOT_OUTLINE.draw(stack, relX() + 111, relY() + 30);
            }
        }
        if (fuel) {
            if (storage)  FUEL_SLOT_OUTLINE.draw(stack, relX() + 35, relY() + 53);
            FUEL_SLOT_OUTLINE.draw(stack, relX() + 53, relY() + 53);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        double actualMouseX = mouseX - relX();
        double actualMouseY = mouseY - relY();

        if (storedFactoryUpgradeType(4))
            this.mouseClickedRedstoneButtons(actualMouseX, actualMouseY);
        if (storedFactoryUpgradeType(0))
            this.mouseClickedInventoryButtons(button, actualMouseX, actualMouseY);
        return super.mouseClicked(mouseX, mouseY, button);
    }


    public void mouseClickedInventoryButtons(int button, double mouseX, double mouseY) {
        boolean flag = button == GLFW.GLFW_MOUSE_BUTTON_2;
        if (!getMenu().showInventoryButtons()) {
            if (mouseX >= 7 && mouseX <= 24 && mouseY >= factoryShowButtonY() && mouseY <= factoryShowButtonY() + 13) {
                Messages.INSTANCE.sendToServer(new PacketShowSettingsButton(this.getMenu().getPos(), 1));
                Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1F));
            }
        } else {
            if (mouseX >= 7 && mouseX <= 24 && mouseY >= factoryShowButtonY() && mouseY <= factoryShowButtonY() + 13) {
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
                if ( mouseX >= -15 && mouseX <= -2 && mouseY >= 58 && mouseY <= 71) {
                    Messages.INSTANCE.sendToServer(new PacketOrientationButton(this.getMenu().getPos(), getMenu().be.showOrientation = !this.getMenu().be.showOrientation));
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
