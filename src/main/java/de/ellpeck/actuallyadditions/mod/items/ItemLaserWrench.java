/*
 * This file ("ItemLaserWrench.java") is part of the Actually Additions mod for Minecraft.
 * It is created and owned by Ellpeck and distributed
 * under the Actually Additions License to be found at
 * http://ellpeck.de/actaddlicense
 * View the source code at https://github.com/Ellpeck/ActuallyAdditions
 *
 * © 2015-2017 Ellpeck
 */

package de.ellpeck.actuallyadditions.mod.items;

import de.ellpeck.actuallyadditions.api.ActuallyAdditionsAPI;
import de.ellpeck.actuallyadditions.mod.ActuallyAdditions;
import de.ellpeck.actuallyadditions.mod.items.base.ItemBase;
import de.ellpeck.actuallyadditions.mod.tile.TileEntityLaserRelay;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

public class ItemLaserWrench extends ItemBase {

    public ItemLaserWrench() {
        super(ActuallyItems.defaultNonStacking());
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        BlockPos pos = context.getClickedPos();
        Level world = context.getLevel();
        Player player = context.getPlayer();

        ItemStack stack = player.getItemInHand(context.getHand());
        BlockEntity tile = world.getBlockEntity(pos);
        if (tile instanceof TileEntityLaserRelay) {
            TileEntityLaserRelay relay = (TileEntityLaserRelay) tile;
            if (!world.isClientSide) {
                if (ItemPhantomConnector.getStoredPosition(stack) == null) {
                    ItemPhantomConnector.storeConnection(stack, pos.getX(), pos.getY(), pos.getZ(), world);
                    player.displayClientMessage(new TranslatableComponent("tooltip." + ActuallyAdditions.MODID + ".laser.stored.desc"), true);
                } else {
                    BlockPos savedPos = ItemPhantomConnector.getStoredPosition(stack);
                    if (savedPos != null) {
                        BlockEntity savedTile = world.getBlockEntity(savedPos);
                        if (savedTile instanceof TileEntityLaserRelay) {
                            int distanceSq = (int) savedPos.distSqr(pos);
                            TileEntityLaserRelay savedRelay = (TileEntityLaserRelay) savedTile;

                            int lowestRange = Math.min(relay.getMaxRange(), savedRelay.getMaxRange());
                            int range = lowestRange * lowestRange;
                            if (ItemPhantomConnector.getStoredWorld(stack) == world.dimension() && savedRelay.type == relay.type && distanceSq <= range && ActuallyAdditionsAPI.connectionHandler.addConnection(savedPos, pos, relay.type, world, false, true)) {
                                ItemPhantomConnector.clearStorage(stack, "XCoordOfTileStored", "YCoordOfTileStored", "ZCoordOfTileStored", "WorldOfTileStored");

                                ((TileEntityLaserRelay) savedTile).sendUpdate();
                                relay.sendUpdate();

                                player.displayClientMessage(new TranslatableComponent("tooltip." + ActuallyAdditions.MODID + ".laser.connected.desc"), true);

                                return InteractionResult.SUCCESS;
                            }
                        }

                        player.displayClientMessage(new TranslatableComponent("tooltip." + ActuallyAdditions.MODID + ".laser.cantConnect.desc"), false);
                        ItemPhantomConnector.clearStorage(stack, "XCoordOfTileStored", "YCoordOfTileStored", "ZCoordOfTileStored", "WorldOfTileStored");
                    }
                }
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.FAIL;
    }

    //    // TODO: [port] ensure this is correct
    //    @Nullable
    //    @Override
    //    public CompoundNBT getShareTag(ItemStack stack) {
    //        return new CompoundNBT();
    //    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, Level playerIn, List<Component> list, TooltipFlag advanced) {
        BlockPos coords = ItemPhantomConnector.getStoredPosition(stack);
        if (coords != null) {
            list.add(new TranslatableComponent("tooltip." + ActuallyAdditions.MODID + ".boundTo.desc").append(":"));
            list.add(new TextComponent("X: " + coords.getX()));
            list.add(new TextComponent("Y: " + coords.getY()));
            list.add(new TextComponent("Z: " + coords.getZ()));
            list.add(new TranslatableComponent("tooltip." + ActuallyAdditions.MODID + ".clearStorage.desc").withStyle(ChatFormatting.ITALIC));
        }
    }
}
