package com.fallenreaper.createutilities.utils;

import com.simibubi.create.foundation.render.SuperByteBuffer;
import com.simibubi.create.foundation.utility.Couple;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;

public class MathUtil {
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

    public static double lerp(double start, double target, float increment) {

        return start + ((target - start) * increment);
    }

    private static double getAngleForFacing(Direction facing) {
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

    public Vec3 getPositions(double t) {

        return lerpVector(starts.getFirst(), starts.getSecond(), (float) t);
    }
}
