package com.fallenreaper.createutilities.content.blocks.steam_furnace;

import com.fallenreaper.createutilities.index.CUBlockPartials;
import com.jozufozu.flywheel.util.AnimationTickHolder;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Constants;
import com.mojang.math.Vector3f;
import com.simibubi.create.content.contraptions.base.HorizontalKineticBlock;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import com.simibubi.create.foundation.tileEntity.renderer.SmartTileEntityRenderer;
import com.simibubi.create.foundation.utility.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec2;
import net.minecraftforge.items.ItemStackHandler;

import static com.fallenreaper.createutilities.content.blocks.steam_furnace.SteamFurnaceBlockEntity.getIndicatorProgress;
import static com.fallenreaper.createutilities.content.blocks.typewriter.TypewriterRenderer.getAngleForFacing;
import static net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING;

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
                .getValue(HORIZONTAL_FACING);
        float pivot = (float) (3.5 / 16f);

        float dt = AnimationTickHolder.getPartialTicks();
        float progress = 0;
        if (te.getState().isProducing())
            dt *= -1;
        te.indicatorProgress = Mth.clamp((te.indicatorProgress + dt), -30, 30);
        progress = Mth.clamp(progress * -AnimationTickHolder.getPartialTicks(), 0, 1);

        int color = Color.mixColors(0x2C0300, 0xCD0000, getIndicatorProgress(te));
        indicator.rotateCentered(Direction.UP, (float) Math.toRadians(getAngleForFacing(facing)))
                .translate(8 / 16F, 11.5 / 16F, 0)
                .rotate(Direction.NORTH, 80 * Constants.DEG_TO_RAD * (getIndicatorProgress(te) ))
                .light(light)
                .renderInto(ms, buffer.getBuffer(RenderType.solid()));

        // rotateCenteredInDirection(indicator, Direction.UP, facing);
        //  indicator.renderInto(ms, buffer.getBuffer(RenderType.solid()));
        renderInternalItems(te, partialTicks, ms, buffer, light, overlay);

    }

    public void renderInternalItems(SteamFurnaceBlockEntity te, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        Direction direction = te.getBlockState().getValue(HORIZONTAL_FACING).getOpposite();

        ItemStackHandler inventory = te.getInternalInventory();
        int posLong = (int) te.getBlockPos().asLong();

        for (int i = 0; i < inventory.getSlots(); ++i) {
            ItemStack itemStack = inventory.getStackInSlot(i);
            if (!itemStack.isEmpty()) {
                ms.pushPose();

                // Center item above the stove
                ms.translate(0.5D, 16.2F/16F, 0.5D);

                // Rotate item to face the stove's front side
                float f = -direction.toYRot();
                ms.mulPose(Vector3f.YP.rotationDegrees(f));

                // Rotate item flat on the stove. Use X and Y from now on
                ms.mulPose(Vector3f.XP.rotationDegrees(90.0F));

                // Neatly align items according to their index
                Vec2 itemOffset = te.getItemOffset(i);
                float rotation = te.getRotation(i);
                ms.translate(itemOffset.x - (rotation/360f)/8f, itemOffset.y + (rotation/360f)/8f, 0.0D);
                ms.mulPose(Vector3f.ZN.rotationDegrees(rotation));

                // Resize the items
                float size = 9 / 16F;
                ms.scale(size, size, size);

                if (te.getLevel() != null)
                    Minecraft.getInstance().getItemRenderer().renderStatic(itemStack, ItemTransforms.TransformType.FIXED, LevelRenderer.getLightColor(te.getLevel(), te.getBlockPos().above()), light, ms, buffer, posLong + i);
                ms.popPose();
            }
        }
    }
}

