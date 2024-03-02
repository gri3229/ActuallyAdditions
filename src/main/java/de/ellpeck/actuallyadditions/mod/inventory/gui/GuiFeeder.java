/*
 * This file ("GuiFeeder.java") is part of the Actually Additions mod for Minecraft.
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
import de.ellpeck.actuallyadditions.mod.ActuallyAdditions;
import de.ellpeck.actuallyadditions.mod.inventory.ContainerFeeder;
import de.ellpeck.actuallyadditions.mod.tile.TileEntityFeeder;
import de.ellpeck.actuallyadditions.mod.util.AssetUtil;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

@OnlyIn(Dist.CLIENT)
public class GuiFeeder extends AAScreen<ContainerFeeder> {

    private static final ResourceLocation RES_LOC = AssetUtil.getGuiLocation("gui_feeder");
    public final TileEntityFeeder tileFeeder;

    public GuiFeeder(ContainerFeeder container, Inventory inventory, Component title) {
        super(container, inventory, title);
        this.tileFeeder = container.feeder;
        this.imageWidth = 176;
        this.imageHeight = 70 + 86;
    }

    @Override
    public void render(@Nonnull PoseStack matrices, int x, int y, float f) {
        super.render(matrices, x, y, f);
        if (x >= this.leftPos + 69 && y >= this.topPos + 30 && x <= this.leftPos + 69 + 10 && y <= this.topPos + 30 + 10) {
            String[] array = new String[]{this.tileFeeder.currentAnimalAmount + " " + I18n.get("info." + ActuallyAdditions.MODID + ".gui.animals"), this.tileFeeder.currentAnimalAmount >= 2 && this.tileFeeder.currentAnimalAmount < TileEntityFeeder.THRESHOLD
                ? I18n.get("info." + ActuallyAdditions.MODID + ".gui.enoughToBreed")
                : this.tileFeeder.currentAnimalAmount >= TileEntityFeeder.THRESHOLD
                    ? I18n.get("info." + ActuallyAdditions.MODID + ".gui.tooMany")
                    : I18n.get("info." + ActuallyAdditions.MODID + ".gui.notEnough")};
            //this.drawHoveringText(Arrays.asList(array), x, y);
        }
    }

    @Override
    public void renderBg(PoseStack matrices, float f, int x, int y) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        RenderSystem.setShaderTexture(0, AssetUtil.GUI_INVENTORY_LOCATION);
        this.blit(matrices, this.leftPos, this.topPos + 70, 0, 0, 176, 86);
        RenderSystem.setShaderTexture(0, RES_LOC);
        this.blit(matrices, this.leftPos, this.topPos, 0, 0, 176, 70);

        if (this.tileFeeder.currentTimer > 0) {
            int i = this.tileFeeder.getCurrentTimerToScale(20);
            this.blit(matrices, this.leftPos + 85, this.topPos + 42 - i, 181, 19 + 19 - i, 6, 20);
        }

        if (this.tileFeeder.currentAnimalAmount >= 2 && this.tileFeeder.currentAnimalAmount < TileEntityFeeder.THRESHOLD) {
            this.blit(matrices, this.leftPos + 70, this.topPos + 31, 192, 16, 8, 8);
        }

        if (this.tileFeeder.currentAnimalAmount >= TileEntityFeeder.THRESHOLD) {
            this.blit(matrices, this.leftPos + 70, this.topPos + 31, 192, 24, 8, 8);
        }
    }
}
