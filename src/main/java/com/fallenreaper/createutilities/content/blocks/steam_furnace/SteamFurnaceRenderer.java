package com.fallenreaper.createutilities.content.blocks.steam_furnace;

import com.fallenreaper.createutilities.index.CUBlockPartials;
import com.jozufozu.flywheel.util.AnimationTickHolder;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Constants;
import com.simibubi.create.content.contraptions.base.HorizontalKineticBlock;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import com.simibubi.create.foundation.tileEntity.renderer.SmartTileEntityRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import static com.fallenreaper.createutilities.content.blocks.steam_furnace.SteamFurnaceBlockEntity.getIndicatorProgress;
import static com.fallenreaper.createutilities.content.blocks.typewriter.TypewriterRenderer.getAngleForFacing;

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
       //
        Direction direction = te.getBlockState()
                .getValue(BlockStateProperties.HORIZONTAL_FACING);
        float pivot = (float) (3.5/16f);
        float progress = 0;
        float dt = AnimationTickHolder.getPartialTicks();
        if(te.isProducing())
            dt*=-1;
        te.indicatorProgress = Mth.clamp(te.indicatorProgress + dt, -30, 30);


           indicator.rotateCentered(Direction.UP, (float) Math.toRadians(getAngleForFacing(facing)))
                   .translate(8/16F, 11.5/16F, 0)
                   .rotate(Direction.NORTH, 80 * Constants.DEG_TO_RAD * (getIndicatorProgress(te) ))
                    .light(light)
                    .renderInto(ms, buffer.getBuffer(RenderType.solid()));

       // rotateCenteredInDirection(indicator, Direction.UP, facing);
     //  indicator.renderInto(ms, buffer.getBuffer(RenderType.solid()));

    }

}
