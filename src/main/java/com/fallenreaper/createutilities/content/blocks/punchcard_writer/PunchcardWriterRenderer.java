package com.fallenreaper.createutilities.content.blocks.punchcard_writer;

import com.fallenreaper.createutilities.index.CUBlockPartials;
import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import com.simibubi.create.foundation.tileEntity.renderer.SmartTileEntityRenderer;
import com.simibubi.create.foundation.utility.AngleHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;

public class PunchcardWriterRenderer extends SmartTileEntityRenderer<PunchcardWriterBlockEntity> {
    public PunchcardWriterRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }
    @Override
    protected void renderSafe(PunchcardWriterBlockEntity tileEntityIn, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        BlockState blockState = tileEntityIn.getBlockState();
        if(!tileEntityIn.inventory.getStackInSlot(0).isEmpty()) {
            SuperByteBuffer blueprint = CachedBufferer.partial(CUBlockPartials.PUNCHCARD, blockState);
            ItemRenderer itemRenderer = Minecraft.getInstance()
                    .getItemRenderer();

            Direction facing = blockState.getValue(PunchcardWriterBlock.HORIZONTAL_FACING);
            ms.pushPose();
            TransformStack.cast(ms)
                    .centre()
                    .rotate(Direction.UP,
                             AngleHelper.rad(180 + AngleHelper.horizontalAngle(facing)))
                    .rotate(Direction.EAST,
                            Mth.PI / 2 )
                    .translate(0, 4.5 / 16f, 0)
                    .scale( .75f );
            itemRenderer.renderStatic(tileEntityIn.inventory.getStackInSlot(0), ItemTransforms.TransformType.FIXED, light, overlay, ms, buffer, 0);

          //  blueprint.renderInto(ms, buffer.getBuffer(RenderType.solid()));
            ms.popPose();
        }
    }
}
