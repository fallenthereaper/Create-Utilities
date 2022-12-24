package com.fallenreaper.createutilities.core.utils;

import com.fallenreaper.createutilities.content.blocks.sprinkler.SprinklerBlock;
import com.fallenreaper.createutilities.content.blocks.sprinkler.SprinklerBlockEntity;
import com.google.common.primitives.Ints;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import com.simibubi.create.foundation.utility.Couple;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.FarmlandWaterManager;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.PlantType;

import java.util.HashSet;
import java.util.Set;

import static com.fallenreaper.createutilities.CreateUtilities.ID;

@SuppressWarnings("ALL")
public class MiscUtil {
    /**
     * https://en.wikipedia.org/wiki/B%C3%A9zier_curve
     */
    Couple<Vec3> starts;
    Couple<Vec3> ends;

    public static Vec3 quadraticBezierCurve(Vec3 p0, Vec3 p1, Vec3 p2, float t) {
        Vec3 lerpValue1 = lerpVector(p0, p1, t);
        Vec3 lerpValue2 = lerpVector(p1, p2, t);
        return lerpVector(lerpValue1, lerpValue2, t);
    }

    public static Vec3 lerpVector(Vec3 from, Vec3 to, float value) {
        Vec3 result = from.add(to.subtract(from).scale(value));
        return result;
    }

    public static int getIndexOf(int[] arr, int t) {


        return Ints.lastIndexOf(arr, t);

    }

    public static double lerp(double start, double target, float increment) {

        return start + ((target - start) * increment);
    }

    public static double getAngleForFacing(Direction facing) {
        return 90 * (facing.equals(Direction.NORTH) ? 4 : facing.equals(Direction.SOUTH) ? 2 : facing.equals(Direction.EAST) ? 3 : 1);
    }

    public static void rotateCenteredInDirection(SuperByteBuffer model, Direction direction, Direction facing) {
        model.rotateCentered(direction, (float) Math.toRadians(getAngleForFacing(facing)));
    }

    /**
     * https://en.wikipedia.org/wiki/B%C3%A9zier_curve
     */
    public static Vec3 cubicBezierCurve(Vec3 p0, Vec3 p1, Vec3 p2, Vec3 p3, float t) {
        //main points lerp
        Vec3 lerpValue1 = lerpVector(p0, p1, t);
        Vec3 lerpValue2 = lerpVector(p1, p2, t);
        Vec3 lerpValue3 = lerpVector(p2, p3, t);
        //inner lines lerp
        Vec3 lerpValue4 = lerpVector(lerpValue1, lerpValue2, t);
        Vec3 lerpValue5 = lerpVector(lerpValue2, lerpValue3, t);
        Vec3 cubicLerp = lerpVector(lerpValue4, lerpValue5, t);
        return cubicLerp;
    }

    public static boolean isInsideCircle(double radius, BlockPos blockPos, BlockPos target) {
        Vec3 centerPos = new Vec3(blockPos.getX(), blockPos.getY(), blockPos.getZ());
        Vec3 targetPos = new Vec3(target.getX(), target.getY(), target.getZ());
        double distance = (int) centerPos.distanceTo(targetPos);

        return distance <= radius;

    }

    public static boolean isInsideCircleHorizontal(double radius, BlockPos blockPos, BlockPos target) {
        Vec3 centerPos = new Vec3(blockPos.getX(), blockPos.getY(), blockPos.getZ());
        Vec3 targetPos = new Vec3(target.getX(), target.getY(), target.getZ());
        double distance = (int) centerPos.distanceTo(targetPos);

        return distance <= radius;

    }

    public static boolean isInsideCircle(double radius, Vec3 center, Vec3 target) {
        double distance = center.distanceTo(target);
        return distance <= radius;
    }

    public static void setPlantFromSeed(ItemStack itemstack, Level pLevel, BlockPos aboveFarmlandPos) {
        if (!(pLevel.getBlockState(aboveFarmlandPos).getBlock() instanceof FarmBlock))
            return;
        if (!(itemstack.getItem() instanceof IPlantable plantable))
            return;
        if (itemstack.isEmpty())
            return;

        if (plantable.getPlantType(pLevel, aboveFarmlandPos) == PlantType.CROP) {
            pLevel.setBlock(aboveFarmlandPos, plantable.getPlant(pLevel, aboveFarmlandPos), 3);
        }
    }

    public static Set<BlockPos> getAllWithinRadius(Level pLevel, Player plassyer, int radius, BlockPos pBlockPos) {
        Set<BlockPos> allBlocks = new HashSet<>();

        for (BlockPos blockPositions : pBlockPos.betweenClosed(pBlockPos.getX() - radius, pBlockPos.getY(), pBlockPos.getZ() - radius, pBlockPos.getX() + radius, pBlockPos.getY() + 1, pBlockPos.getZ() + radius)) {
            BlockState blockState = pLevel.getBlockState(blockPositions);
            if (blockState.isAir())
                continue;
            if (blockState.is(BlockTags.WITHER_IMMUNE))
                continue;
            if (allBlocks.contains(blockPositions))
                continue;

            // if(!blockState.canHarvestBlock(level, blockPositions, player))
            //     continue;
            BlockPos p;
            p = new BlockPos(blockPositions);


            if (isInsideCircle(radius, pBlockPos, blockPositions)) {
                Block block = blockState.getBlock();
                if (block instanceof CropBlock cropBlock && cropBlock.isMaxAge(blockState)) {
                    allBlocks.add(blockPositions);
                }
            }
        }
        return allBlocks;
    }

    public static String formatTime(int ticks) {
        String base = "";
        int seconds = ticks / 20;
        int minutes = seconds / 60;
        int hours = minutes / 60;
        int days = hours / 24;
        int weeks = days / 7;

        seconds = seconds % 60;
        minutes = minutes % 60;
        hours = hours % 60;

        if (weeks > 0)
            base += weeks + "w ";

        if (days > 0)
            base += days + "d ";

        if (hours > 0)
            base += hours + "h ";

        if (minutes > 0)
            base += minutes + "m ";

        base += seconds + "s";
        return base;

    }

    public static String formatEnumName(Enum<?> target) {
        var stateString = target.name();
        stateString = stateString.substring(0, 1).toUpperCase() + stateString.substring(1).toLowerCase();
        return stateString;
    }

    public static int sortLowest(int[] to) {
        int base;
        int max = to[1];
        for (int i = 0; i < to.length; i++)
            if (to[i] < max)
                max = to[i];
        return max;
    }

    public static boolean isNearWater(LevelReader pLevel, BlockPos pPos) {
        for (BlockPos blockpos : BlockPos.betweenClosed(pPos.offset(-12, 1, -12), pPos.offset(12, 12, 12))) {
            BlockEntity detectedBlock = pLevel.getBlockEntity(blockpos);

            if (detectedBlock instanceof SprinklerBlockEntity be) {
                BlockPos posBe = be.getBlockPos();
                boolean isCeiling = pLevel.getBlockState(posBe).getValue(SprinklerBlock.CEILING);

                if (be.isHydrating() && be.isWater()) {
                    for (BlockPos pos : BlockPos.betweenClosed(posBe.offset(-be.getRadius(), 1, -be.getRadius()), posBe.offset(be.getRadius(), 12, be.getRadius()))) {
                        if (isInsideCircle(be.getRadius(), posBe, pPos)) {
                            return true;
                        }
                    }
                }
            }
        }
        for (BlockPos blockpos : BlockPos.betweenClosed(pPos.offset(-4, 0, -4), pPos.offset(4, 1, 4))) {
            if (pLevel.getFluidState(blockpos).is(FluidTags.WATER)) {
                return true;
            }
        }
        return FarmlandWaterManager.hasBlockWaterTicket(pLevel, pPos);
    }

    public static MutableComponent asItemDescription(Item pItem) {
        return Component.translatable(ID + "." + pItem.getDescriptionId().substring(21) + "." + "description");
    }

    public Vec3 getPositions(double t) {

        return lerpVector(starts.getFirst(), starts.getSecond(), (float) t);
    }
}
