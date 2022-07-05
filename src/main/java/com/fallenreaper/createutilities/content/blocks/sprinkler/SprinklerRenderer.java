package com.fallenreaper.createutilities.content.blocks.sprinkler;

import com.fallenreaper.createutilities.content.blocks.bellow.BellowBlock;
import com.fallenreaper.createutilities.index.CUBlockPartials;
import com.jozufozu.flywheel.backend.Backend;
import com.jozufozu.flywheel.repack.joml.Math;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.content.contraptions.base.KineticTileEntity;
import com.simibubi.create.content.contraptions.base.KineticTileEntityRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class SprinklerRenderer extends KineticTileEntityRenderer {


    public SprinklerRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }


    @Override
    protected void renderSafe(KineticTileEntity te, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {


        BlockState blockState = te.getBlockState();
        Block block = blockState.getBlock();

            float time = AnimationTickHolder.getRenderTime(te.getLevel());
            float speed = te.getSpeed();
            if (speed > 0)
                speed = Math.clamp(speed, 64, 64 * 8);
            if (speed < 0)
                speed = Math.clamp(speed, -64 * 8, -64);

            float angle = (time * speed * 2 / 10f) % 360;

            angle = angle / 180F * (float) Math.PI;

            Direction direction = Direction.UP;
            VertexConsumer vb = buffer.getBuffer(RenderType.cutoutMipped());
            int lightInFront = LevelRenderer.getLightColor(te.getLevel(), te.getBlockPos().relative(direction));
            SuperByteBuffer model =
                    CachedBufferer.partial(CUBlockPartials.SPRINKLER_PROPAGATOR, te.getBlockState());
            KineticTileEntityRenderer.renderRotatingKineticBlock(te, this.getRenderedBlockState(te), ms, vb, light);

            kineticRotationTransform(model, te, direction.getAxis(), angle, lightInFront).renderInto(ms, vb);


    }

    @Override
    public boolean shouldRenderOffScreen(KineticTileEntity pTe) {
        return true;
    }

    @Override
    protected BlockState getRenderedBlockState(KineticTileEntity te) {
        return shaft(getRotationAxisOf(te));
    }

}

