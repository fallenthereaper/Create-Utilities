package com.fallenreaper.createutilities.content.blocks.mechanical_propeller;

import com.fallenreaper.createutilities.index.CUBlockPartials;
import com.jozufozu.flywheel.backend.Backend;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import com.simibubi.create.foundation.tileEntity.renderer.SmartTileEntityRenderer;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;

import static com.simibubi.create.content.contraptions.base.KineticTileEntityRenderer.kineticRotationTransform;
import static net.minecraft.world.level.block.state.properties.BlockStateProperties.FACING;

public class MechanicalPropellerRenderer extends SmartTileEntityRenderer<MechanicalPropellerBlockEntity> {
    public MechanicalPropellerRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected void renderSafe(MechanicalPropellerBlockEntity te, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        if (Backend.canUseInstancing(te.getLevel())) return;

        Direction direction = te.getBlockState()
                .getValue(FACING);
        VertexConsumer vb = buffer.getBuffer(RenderType.cutoutMipped());

        int lightBehind = LevelRenderer.getLightColor(te.getLevel(), te.getBlockPos().relative(direction.getOpposite()));
        int lightInFront = LevelRenderer.getLightColor(te.getLevel(), te.getBlockPos().relative(direction));


        SuperByteBuffer fanInner =
                CachedBufferer.partialFacing(CUBlockPartials.PROPELLER, te.getBlockState(), direction.getOpposite());

        float time = AnimationTickHolder.getRenderTime(te.getLevel());
        float speed = te.getSpeed()  ;
        if (speed > 0)
            speed = Mth.clamp(speed, 80, 64 * 12);
        if (speed < 0)
            speed = Mth.clamp(speed, -64 * 12, -80);
        float angle = (time * speed * 3 / 10f) % 360;
        angle = angle / 180f * (float) Math.PI;

      //  standardKineticRotationTransform(shaftHalf, te, lightBehind).renderInto(ms, vb);
        kineticRotationTransform(fanInner.translate(0.03D, 0, 0), te, direction.getAxis(), angle, lightBehind).renderInto(ms, vb);
    }
}
