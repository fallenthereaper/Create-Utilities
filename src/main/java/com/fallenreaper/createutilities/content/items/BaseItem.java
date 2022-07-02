package com.fallenreaper.createutilities.content.items;

import com.fallenreaper.createutilities.content.blocks.typewriter.TypewriterBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class BaseItem extends Item implements TypewriterProvider {
    public BaseItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        BlockPos pos = pContext.getClickedPos();
        BlockState blockState = pContext.getLevel().getBlockState(pos);
        if(blockState.getBlock() instanceof TypewriterBlock block) {

            return InteractionResult.CONSUME;
        }
        return super.useOn(pContext);
    }

    @Nullable
    @Override
    public CompoundTag getShareTag(ItemStack stack) {
        return super.getShareTag(stack);
    }
}
