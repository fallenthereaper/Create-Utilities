package com.fallenreaper.createutilities.content.items;

import com.fallenreaper.createutilities.core.data.blocks.liquidtank.FluidNodeNetwork;
import com.fallenreaper.createutilities.core.data.blocks.liquidtank.IFluidTransferProvider;
import com.fallenreaper.createutilities.core.data.blocks.liquidtank.LiquidTankBlockEntity;
import com.fallenreaper.createutilities.core.data.items.BaseItem;
import com.simibubi.create.AllSoundEvents;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

import java.util.UUID;

public class DevItem extends BaseItem {
    public DevItem(Properties pProperties) {
        super(pProperties);
    }

    public static void createNode(Level world, BlockPos start, BlockPos end, Player player, UUID key) {
                  IFluidTransferProvider origin = (IFluidTransferProvider) world.getBlockEntity(start);
        IFluidTransferProvider target = (IFluidTransferProvider) world.getBlockEntity(end);

        if (origin == null || target == null)
            return;

        if (origin.isRemoved() || target.isRemoved())
            return;


        origin.getNode().connectNodes(origin, target);
        target.getNode().connectNodes(target, origin);


        LiquidTankBlockEntity originR = (LiquidTankBlockEntity) origin;

        FluidNodeNetwork.addNode(origin.getNode(), key);

    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        BlockPos pPos = pContext.getClickedPos();
        Level pLevel = pContext.getLevel();
        ItemStack pItemStack = pContext.getItemInHand();
        Player pPlayer = pContext.getPlayer();
        InteractionHand pHand = pContext.getHand();
        CompoundTag pTag = pItemStack.getOrCreateTag();

        BlockPos savedPos;


        if (pPlayer == null)
            return InteractionResult.FAIL;


        if (pLevel.getBlockEntity(pPos) instanceof IFluidTransferProvider fluidTransfer) {

            // save first position
            if (!pItemStack.hasTag()) {

                //  FluidNode node = transfer.getNode();
                //  node.connectOrigin(transfer);FluidNodeNetwork.addNode(node, pPos);

                CompoundTag blocks = NbtUtils.writeBlockPos(pPos);
                pTag.put("FirstPosition", blocks);
                LiquidTankBlockEntity tankBlockEntity = (LiquidTankBlockEntity) fluidTransfer;

                UUID pValue = tankBlockEntity.getUuidKey() != null ? tankBlockEntity.getUuidKey() : UUID.randomUUID();
                tankBlockEntity.setUuidKey(pValue);
                pTag.putUUID("UUID", pValue);
                pItemStack.setTag(pTag);
                pPlayer.getCooldowns()
                        .addCooldown(pItemStack.getItem(), 5);


                pPlayer.displayClientMessage(Component.literal("First Position Set").withStyle(ChatFormatting.YELLOW), true);
                return InteractionResult.SUCCESS;


            }

            //will be called after having the first position set
            if (pItemStack.hasTag() && pTag.contains("FirstPosition") && pTag.contains("UUID")) {

                savedPos = NbtUtils.readBlockPos(pTag.getCompound("FirstPosition"));
                UUID uuid = pTag.getUUID("UUID");

                if (!pPos.equals(savedPos)) {
                    //    FluidNode savedNode = FluidNodeNetwork.getNode(savedPos);
                    //     savedNode.connectTarget(transfer);
                    createNode(pLevel, pPos, savedPos, pPlayer, uuid);
                    if (pLevel.isClientSide) {
                        pLevel.playSound(pPlayer, pPos, AllSoundEvents.WORLDSHAPER_PLACE.getMainEvent(), SoundSource.BLOCKS, 1.25F, 1F);
                        pPlayer.displayClientMessage(Component.literal("Connected Node").withStyle(ChatFormatting.GREEN), true);
                    }
                    if (pLevel.getBlockEntity(savedPos) instanceof LiquidTankBlockEntity te) {
                        if (te.getUuidKey() == null) {
                            te.setUuidKey(uuid);
                        }
                    }

                    pItemStack.setTag(null);
                           /*
                            for (int i = 1; i < savedNode.nodesDistance() - 1; i++) {
                                Vec3 start = savedNode.getOriginPosition();
                                Vec3 end = savedNode.getTargetPosition();


                                Vec3 diff = end.subtract(start);

                                Vec3 step = diff.normalize();
                                Vec3 position = start.add(step.scale(i));
                                BlockPos autoPos = new BlockPos(position);
                                }
*/

                    //  pLevel.setBlock(autoPos, Blocks.BRICK_SLAB.defaultBlockState(), 2);

                    return InteractionResult.SUCCESS;

                }
            }
        }

        return super.useOn(pContext);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack heldItem = pPlayer.getItemInHand(pUsedHand);

        if (heldItem.getItem() instanceof DevItem && pPlayer.isShiftKeyDown()) {
            if (pPlayer.getItemInHand(pUsedHand).hasTag() && heldItem.getTag().contains("FirstPosition")) {
                heldItem.setTag(null);
                pPlayer.displayClientMessage(Component.literal("Cleared Selection").withStyle(ChatFormatting.RED), true);
                return InteractionResultHolder.success(heldItem);
            }
        }
        return super.use(pLevel, pPlayer, pUsedHand);
    }
}
