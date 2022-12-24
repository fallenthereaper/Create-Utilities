package com.fallenreaper.createutilities.mixins;

import com.fallenreaper.createutilities.content.blocks.encased_nixie_tube.EncasedNixieTubeBlock;
import com.fallenreaper.createutilities.content.blocks.encased_nixie_tube.EncasedNixieTubeBlockEntity;
import com.fallenreaper.createutilities.content.blocks.encased_nixie_tube.INixieTube;
import com.fallenreaper.createutilities.index.CUBlocks;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.contraptions.base.KineticTileEntity;
import com.simibubi.create.content.logistics.block.redstone.NixieTubeBlock;
import com.simibubi.create.content.logistics.block.redstone.NixieTubeTileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.simibubi.create.content.logistics.block.redstone.DoubleFaceAttachedBlock.FACE;
import static net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING;
import static net.minecraft.world.level.block.state.properties.BlockStateProperties.WATERLOGGED;

@Mixin(NixieTubeBlock.class)
public abstract class NixieTubeMixin implements INixieTube {

    @Shadow
    public abstract DyeColor getColor();

    //todo: make it copy the text
    @Inject(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;getItemInHand(Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/item/ItemStack;"), cancellable = true, remap = false)
    void createutilities_applyBrassCasing(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult ray, CallbackInfoReturnable<InteractionResult> cir) {
        ItemStack heldItem = player.getItemInHand(hand);
        if (AllBlocks.BRASS_CASING.isIn(heldItem)) {
            MutableComponent text = null;
            NixieTubeTileEntity te = (NixieTubeTileEntity) world.getBlockEntity(pos);
            if (te != null) {
                text = te.getFullText();
            }

            if (world.isClientSide) {
                world.playSound(player, pos, SoundEvents.BAMBOO_BREAK, SoundSource.BLOCKS, 0.75F, 8 / 16F);
                cir.setReturnValue(InteractionResult.SUCCESS);
            }

            KineticTileEntity.switchToBlockState(world, pos, CUBlocks.ENCASED_NIXIE_TUBES.get(this.getColor()).getDefaultState().setValue(FACE, state.getValue(FACE))
                    .setValue(WATERLOGGED, state.getValue(WATERLOGGED)).setValue(HORIZONTAL_FACING, state.getValue(HORIZONTAL_FACING)));
            if(text != null && !te.reactsToRedstone()) {
                if (world.getBlockState(pos).getBlock() instanceof EncasedNixieTubeBlock) {
                    Component finalText = text;
                    EncasedNixieTubeBlock.walkNixies(world, pos, (currentPos, rowPosition) -> {
                        BlockEntity blockEntity = world
                                .getBlockEntity(currentPos);
                        if (blockEntity instanceof EncasedNixieTubeBlockEntity nixie) {
                            nixie.displayCustomText(finalText.getString(), rowPosition);
                        }
                    });
                }
            }

            cir.setReturnValue(InteractionResult.SUCCESS);
        }
    }
/*
    @Inject(method = "colorOf",at = @At(value ="RETURN", target = "Lnet/minecraft/world/entity/player/Player;getItemInHand(Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/item/ItemStack;"), cancellable = true, remap = false )
    static void applyColor(BlockState blockState, CallbackInfoReturnable<DyeColor> cir) {
        DyeColor toReturn ;
        if(blockState.getBlock() instanceof EncasedNixieTubeBlock) {
            toReturn = ((EncasedNixieTubeBlock) blockState.getBlock()).color;
        }
         else {
             toReturn = DyeColor.ORANGE;
        }


         cir.setReturnValue(toReturn);
    }

 */
}
