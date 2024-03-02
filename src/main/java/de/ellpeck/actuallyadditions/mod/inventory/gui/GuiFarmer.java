/*
 * This file ("GuiFarmer.java") is part of the Actually Additions mod for Minecraft.
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
import de.ellpeck.actuallyadditions.mod.inventory.ContainerFarmer;
import de.ellpeck.actuallyadditions.mod.tile.TileEntityFarmer;
import de.ellpeck.actuallyadditions.mod.util.AssetUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;


@OnlyIn(Dist.CLIENT)
public class GuiFarmer extends AAScreen<ContainerFarmer> {

    private static final ResourceLocation RES_LOC = AssetUtil.getGuiLocation("gui_farmer");
    private final TileEntityFarmer farmer;

    private EnergyDisplay energy;

    public GuiFarmer(ContainerFarmer container, Inventory inventory, Component title) {
        super(container, inventory, title);
        this.farmer = container.farmer;
        this.imageWidth = 176;
        this.imageHeight = 93 + 86;
    }

    @Override
    public void init() {
        super.init();

        this.energy = new EnergyDisplay(this.leftPos + 33, this.topPos + 6, this.farmer.storage);
    }

    @Override
    public void renderBg(PoseStack matrices, float f, int x, int y) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        RenderSystem.setShaderTexture(0, AssetUtil.GUI_INVENTORY_LOCATION);
        this.blit(matrices, this.leftPos, this.topPos + 93, 0, 0, 176, 86);

        RenderSystem.setShaderTexture(0, RES_LOC);
        this.blit(matrices, this.leftPos, this.topPos, 0, 0, 176, 93);

        this.energy.draw(matrices);
    }

    @Override
    public void render(@Nonnull PoseStack matrices, int x, int y, float f) {
        super.render(matrices, x, y, f);
        this.energy.render(matrices, x, y);
    }
}
