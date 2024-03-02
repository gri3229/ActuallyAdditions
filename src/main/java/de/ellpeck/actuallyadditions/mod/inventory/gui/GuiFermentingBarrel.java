/*
 * This file ("GuiFermentingBarrel.java") is part of the Actually Additions mod for Minecraft.
 * It is created and owned by Ellpeck and distributed
 * under the Actually Additions License to be found at
 * http://ellpeck.de/actaddlicense
 * View the source code at https://github.com/Ellpeck/ActuallyAdditions
 *
 * © 2015-2017 Ellpeck
 */

package de.ellpeck.actuallyadditions.mod.inventory.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import de.ellpeck.actuallyadditions.mod.inventory.ContainerFermentingBarrel;
import de.ellpeck.actuallyadditions.mod.tile.TileEntityFermentingBarrel;
import de.ellpeck.actuallyadditions.mod.util.AssetUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;


@OnlyIn(Dist.CLIENT)
public class GuiFermentingBarrel extends AAScreen<ContainerFermentingBarrel> {

    private static final ResourceLocation RES_LOC = AssetUtil.getGuiLocation("gui_fermenting_barrel");
    private final TileEntityFermentingBarrel press;
    private FluidDisplay input;
    private FluidDisplay output;

    public GuiFermentingBarrel(ContainerFermentingBarrel container, Inventory inventory, Component title) {
        super(container, inventory, title);
        this.press = container.barrel;
        this.imageWidth = 176;
        this.imageHeight = 93 + 86;
    }

    @Override
    public void render(@Nonnull PoseStack matrices, int x, int y, float f) {
        super.render(matrices, x, y, f);
        this.input.render(matrices, x, y);
        this.output.render(matrices, x, y);
    }

    @Override
    public void init() {
        super.init();
        this.input = new FluidDisplay(this.leftPos + 60, this.topPos + 5, this.press.tanks.inputTank);
        this.output = new FluidDisplay(this.leftPos + 98, this.topPos + 5, this.press.tanks.outputTank);
    }

    @Override
    public void renderBg(PoseStack matrices, float f, int x, int y) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        RenderSystem.setShaderTexture(0, AssetUtil.GUI_INVENTORY_LOCATION);
        this.blit(matrices, this.leftPos, this.topPos + 93, 0, 0, 176, 86);

        RenderSystem.setShaderTexture(0, RES_LOC);
        this.blit(matrices, this.leftPos, this.topPos, 0, 0, 176, 93);

        if (this.press.currentProcessTime > 0) {
            int i = this.press.getProcessScaled(29);
            this.blit(matrices, this.leftPos + 82, this.topPos + 34, 176, 0, 12, i);
        }

        this.input.draw(matrices);
        this.output.draw(matrices);
    }
}
