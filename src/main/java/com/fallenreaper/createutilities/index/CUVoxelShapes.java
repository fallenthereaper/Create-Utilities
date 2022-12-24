package com.fallenreaper.createutilities.index;

import com.simibubi.create.foundation.utility.VoxelShaper;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.function.BiFunction;

import static net.minecraft.core.Direction.SOUTH;

public class CUVoxelShapes {
    public static final VoxelShaper

            SPRINKLER = shape(2, 0, 2, 14, 3, 14)
            .add(1, 3, 1, 15, 13, 15)
            .add(4, 13, 4, 12, 15, 12)
            .forDirectional(Direction.NORTH),
            MECHANICAL_PROPELLER = shape(0, 0, 2, 16, 3, 15)
                    .add(1, 3, 2, 13, 12, 12)
                    .add(0, 13, 2, 16, 16, 15)
                    .forDirectional(),
            SPRINKLER_CEILING = shape(4, 13, 4, 12, 15, 12)
                    .add(1, 3, 1, 15, 13, 15)
                    .add(2, 0, 2, 14, 3, 14)
                    .forDirectional(),
            TYPEWRITER = shape(0, 0, 0, 16, 3, 16)
                    .add(0, 3, 7, 16, 9, 16)
                    .forDirectional(Direction.NORTH),
            BELLOW = shape(0, 0, 0, 16, 2, 16)
                    .add(0, 2, 2, 16, 13, 14)
                    .forDirectional(Direction.NORTH),
            PUNCHCARD_WRITER = shape(0, 0, 0, 16, 4, 16)
                    .add(3, 4, 3, 14, 12, 13)
                    .add(0, 4, 11, 16, 14, 16)
                    .forDirectional(Direction.NORTH),
           STEAM_FURNACE = shape(0, 0, 0, 16, 6, 16)
                    .add(1, 6, 1, 15, 11, 15)
                   .add(0, 11, 0, 16, 16, 16)
                    .forDirectional(Direction.NORTH),
           HAND_DISPLAY = shape(4, 0, 4, 12, 2, 12)
                    .add(6, 2, 6, 10, 5, 10)
                   .add(5, 5, 5, 11, 12, 11)
                    .forDirectional(Direction.NORTH),
            ENCASED_NIXIE_TUBE = shape(0, 0, 0, 16, 4, 16).add(9, 0, 5, 15, 15, 11).add(1, 0, 5, 7, 15, 11)
			.forHorizontalAxis(),
            ENCASED_NIXIE_CEILING = shape(0, 12, 0, 16, 16, 16).add(1, 1, 5, 7, 12, 11).add(9, 1, 5, 15, 12, 11)
			.forHorizontalAxis(),
            ENCASED_NIXIE_WALL = shape(0, 0, 12, 16, 16, 16).add(5, 9, 0, 11, 15, 15).add(5, 1, 0, 11, 7, 15)
			.forHorizontal(SOUTH);



    private static CUVoxelShapes.Builder shape(VoxelShape shape) {
        return new CUVoxelShapes.Builder(shape);
    }

    private static CUVoxelShapes.Builder shape(double x1, double y1, double z1, double x2, double y2, double z2) {
        return shape(cuboid(x1, y1, z1, x2, y2, z2));
    }

    private static VoxelShape cuboid(double x1, double y1, double z1, double x2, double y2, double z2) {
        return Block.box(x1, y1, z1, x2, y2, z2);
    }

    public static class Builder {

        private VoxelShape shape;

        public Builder(VoxelShape shape) {
            this.shape = shape;
        }

        public CUVoxelShapes.Builder add(VoxelShape shape) {
            this.shape = Shapes.or(this.shape, shape);
            return this;
        }

        public CUVoxelShapes.Builder add(double x1, double y1, double z1, double x2, double y2, double z2) {
            return add(cuboid(x1, y1, z1, x2, y2, z2));
        }

        public CUVoxelShapes.Builder erase(double x1, double y1, double z1, double x2, double y2, double z2) {
            this.shape =
                    Shapes.join(shape, cuboid(x1, y1, z1, x2, y2, z2), BooleanOp.ONLY_FIRST);
            return this;
        }

        public VoxelShape build() {
            return shape;
        }

        public VoxelShaper build(BiFunction<VoxelShape, Direction, VoxelShaper> factory, Direction direction) {
            return factory.apply(shape, direction);
        }

        public VoxelShaper build(BiFunction<VoxelShape, Direction.Axis, VoxelShaper> factory, Direction.Axis axis) {
            return factory.apply(shape, axis);
        }

        public VoxelShaper forDirectional(Direction direction) {
            return build(VoxelShaper::forDirectional, direction);
        }

        public VoxelShaper forAxis() {
            return build(VoxelShaper::forAxis, Direction.Axis.Y);
        }

        public VoxelShaper forHorizontalAxis() {
            return build(VoxelShaper::forHorizontalAxis, Direction.Axis.Z);
        }

        public VoxelShaper forHorizontal(Direction direction) {
            return build(VoxelShaper::forHorizontal, direction);
        }

        public VoxelShaper forDirectional() {
            return forDirectional(Direction.UP);
        }
    }
}
