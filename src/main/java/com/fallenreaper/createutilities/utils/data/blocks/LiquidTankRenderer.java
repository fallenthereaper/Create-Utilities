package com.fallenreaper.createutilities.utils.data.blocks;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllSpecialTextures;
import com.simibubi.create.CreateClient;
import com.simibubi.create.foundation.fluid.FluidRenderer;
import com.simibubi.create.foundation.tileEntity.behaviour.ValueBox;
import com.simibubi.create.foundation.tileEntity.renderer.SmartTileEntityRenderer;
import com.simibubi.create.foundation.utility.Color;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fluids.FluidStack;

public class LiquidTankRenderer extends SmartTileEntityRenderer<LiquidTankBlockEntity> {
    public LiquidTankRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected void renderSafe(LiquidTankBlockEntity te, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        super.renderSafe(te, partialTicks, ms, buffer, light, overlay);
        BlockPos origin = te.getBlockPos();
      //  float flevel = (float) Math.min(te.fluidTank.tank.getFluid().getAmount(). / te.getTankCapacity(), 1);
        float levell = (float) te.getTank().getFluidAmount() / te.getTankCapacity();
        AABB box = new AABB(origin.getX(), origin.getY(), origin.getZ(), origin.getX() + 1, (origin.getY()) + 1, origin.getZ() + 1).deflate(0.961f);

        Level level = te.getLevel();


        if (level.getBlockEntity(origin.above()) instanceof LiquidTankBlockEntity) {

            AABB newBox = new AABB(origin.above());

        } else if (level.getBlockEntity(origin.below()) instanceof LiquidTankBlockEntity) {
            AABB newBox = new AABB(origin.below());
            // box = newBox.expandTowards(0, 1, 0).move(0, -1, 0);
        }


        AABB bb = new AABB(Vec3.ZERO, Vec3.ZERO).inflate(1/16f)
                .contract(1/16f, 1/16f, 1/16f)
                .move(0.75, 0, 0);
        ValueBox valueBox = new ValueBox.TextValueBox(new TextComponent(""), bb, te.getBlockPos(), new TextComponent(String.valueOf(te.getTank().getFluidAmount())));
        renderFluid(te, partialTicks, ms, buffer, light);
        // CreateClient.OUTLINER.showValueBox(te.getBlockPos(), new ValueBox(new TextComponent(te.toString()),box.contract(10, 10, 10), origin) );
        CreateClient.OUTLINER.showAABB("blockPos" + te.getBlockPos(), box).colored(Color.WHITE).lineWidth((1 / 16F)).withFaceTexture(AllSpecialTextures.THIN_CHECKERED);


//todo: progress log like punchcardwriter
        CreateClient.OUTLINER.showValueBox("box" + te.getBlockPos(),
                valueBox.offsetLabel(bb.getCenter())).colored(Color.WHITE).lineWidth((1 / 16F)/16F).highlightFace(Direction.UP).disableCull();


    }

    protected void renderFluid(LiquidTankBlockEntity te, float partialTicks, PoseStack ms, MultiBufferSource buffer,
                               int light) {

        if (te.fluidTank == null)
            return;


        FluidStack fluidStack = te.getTank().getFluid();
        float level = (float) te.getTank().getFluidAmount() / te.getTankCapacity();

        if (!fluidStack.isEmpty() && level != 0) {


            ms.pushPose();
            ms.translate(0, 0, 0);
            AABB bb = new AABB(Vec3.ZERO.x, Vec3.ZERO.y, Vec3.ZERO.z, 1F, level, 1F);
            FluidRenderer.renderFluidBox(fluidStack, (float)
                            bb.minX, (float)
                            bb.minY, (float)
                            bb.minZ, (float)
                            bb.maxX, (float)
                            bb.maxY, (float)
                            bb.maxZ, buffer, ms, light,
                    true);
            ms.popPose();
        }


    }
}
/*
        assert level != null;
                var shape = tileEntityIn.getLevel().getBlockState(tileEntityIn.getBlockPos())
                .getShape(level, origin);

                AABB box = shape.bounds();
                */
