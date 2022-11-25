package com.fallenreaper.createutilities.content.blocks.steam_furnace;

import com.fallenreaper.createutilities.index.CUBlockPartials;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.content.contraptions.base.HorizontalKineticBlock;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import com.simibubi.create.foundation.tileEntity.renderer.SmartTileEntityRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

import static com.fallenreaper.createutilities.content.blocks.typewriter.TypewriterRenderer.rotateCenteredInDirection;

public class SteamFurnaceRenderer extends SmartTileEntityRenderer<SteamFurnaceBlockEntity> {
    public SteamFurnaceRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }


    @Override
    protected void renderSafe(SteamFurnaceBlockEntity te, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        super.renderSafe(te, partialTicks, ms, buffer, light, overlay);
        BlockState blockState = te.getBlockState();
        var facing = blockState.getValue(HorizontalKineticBlock.HORIZONTAL_FACING);
        SuperByteBuffer indicator = CachedBufferer.partial(CUBlockPartials.PRESSURE_INDICATOR, te.getBlockState());
        rotateCenteredInDirection(indicator, Direction.UP, facing);
        indicator.renderInto(ms, buffer.getBuffer(RenderType.solid()));

    }
}
