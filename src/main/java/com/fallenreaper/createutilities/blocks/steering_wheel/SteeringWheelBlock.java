package com.fallenreaper.createutilities.blocks.steering_wheel;

import com.fallenreaper.createutilities.index.CUBlockEntities;
import com.fallenreaper.createutilities.index.CUBlockPartials;
import com.fallenreaper.createutilities.index.CUBlockShapes;
import com.fallenreaper.createutilities.index.CUBlocks;
import com.jozufozu.flywheel.core.PartialModel;
import com.simibubi.create.content.contraptions.base.DirectionalKineticBlock;
import com.simibubi.create.foundation.block.ITE;
import com.simibubi.create.foundation.config.AllConfigs;
import com.simibubi.create.foundation.utility.Iterate;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;


/*
public class SteeringWheelBlock extends DirectionalKineticBlock  implements ITE<SteeringWheelBlockEntity> {
   int speed;
    boolean shouldRotate;
   public SteeringWheelBlock(Properties properties) {
       super(properties);
   }



    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return CUBlockShapes.STEERING_WHEEL_1.get(pState.getValue(FACING));
    }


    @OnlyIn(Dist.CLIENT)
   public PartialModel getRenderedHandle() {
       return CUBlockPartials.STEERING_WHEEL;
   }

   public float getRotationSpeed() {


       return 16/2;
   }


   @Override
   public RenderShape getRenderShape(BlockState state) {
       return RenderShape.ENTITYBLOCK_ANIMATED;
   }

   public static float convertToDirection(float axisSpeed, Direction d) {
       return d.getAxisDirection() == Direction.AxisDirection.POSITIVE ? axisSpeed : -axisSpeed;
   }

   @Override
   public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
           boolean clockwise = false;
           Vec3 offset = hit.getLocation().subtract(pos.getX(), pos.getY(), pos.getZ());

       withTileEntityDo(worldIn, pos, (te) -> te.turn(getRayTrace(hit, pos, state)));

       //isHoldingACertaintem(player, handIn, item);
       player.causeFoodExhaustion(getRotationSpeed() * AllConfigs.SERVER.kinetics.crankHungerMultiplier.getF());

       return InteractionResult.SUCCESS;
   }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction preferred = getPreferredFacing(context);
        if (preferred == null || (context.getPlayer() != null && context.getPlayer()
                .isShiftKeyDown()))
            return defaultBlockState().setValue(FACING, context.getClickedFace());
        return defaultBlockState().setValue(FACING, preferred.getOpposite());
    }

   @Override
   public boolean canSurvive(BlockState state, LevelReader worldIn, BlockPos pos) {
       Direction facing = state.getValue(FACING)
               .getOpposite();
       BlockPos neighbourPos = pos.relative(facing);
       BlockState neighbour = worldIn.getBlockState(neighbourPos);
       if (CUBlocks.STEERING_WHEEL.has(neighbour))
       {
           return true;
       }
       return !neighbour.getCollisionShape(worldIn, neighbourPos)
               .isEmpty();
   }

   public BlockState modifyBlockState(BlockPlaceContext context) {
       Level worldIn = context.getLevel();
      BlockState blockState = worldIn.getBlockState(context.getClickedPos().below());
       return defaultBlockState();
   }

   @Override
   public void neighborChanged(BlockState state, Level worldIn, BlockPos pos, Block blockIn, BlockPos fromPos,
                               boolean isMoving) {

       if (worldIn.isClientSide)
           return;
       Direction blockFacing = state.getValue(FACING);
       if (fromPos.equals(pos.relative(blockFacing.getOpposite()))) {
           if (!canSurvive(state, worldIn, pos)) {
               worldIn.destroyBlock(pos, !Minecraft.getInstance().player.isCreative());
               return;
           }
       }
   }


   @Override
   public boolean showCapacityWithAnnotation() {
       return super.showCapacityWithAnnotation();
   }

    @Override
    public BlockEntityType<? extends SteeringWheelBlockEntity> getTileEntityType() {
        return CUBlockEntities.STEERING_WHEEL.get();
    }
    @Override
    public BlockEntity newBlockEntity(BlockPos p_153215_, BlockState p_153216_) {
        return ITE.super.newBlockEntity(p_153215_, p_153216_);
    }


    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return super.hasShaftTowards(world, pos, state, face);
    }


   @Override
   public Direction.Axis getRotationAxis(BlockState state) {
       return state.getValue(FACING)
               .getAxis();
   }

   @Override
   public Class<SteeringWheelBlockEntity> getTileEntityClass() {
       return SteeringWheelBlockEntity.class;
   }

    @Override
    public boolean isPathfindable(BlockState pState, BlockGetter pLevel, BlockPos pPos, PathComputationType pType) {
        return super.isPathfindable(pState, pLevel, pPos, pType);
    }

    public boolean shapeIntersection(BlockState state, BlockHitResult ray) {
       System.out.println("Block Intersected!");
       System.out.println( CUBlockShapes.STEERING_WHEEL_1.get(getRotationAxis(state)).bounds().contains(ray.getLocation())
              );
           return CUBlockShapes.STEERING_WHEEL_1.get(getRotationAxis(state))
               .bounds()
               .inflate(0.001)
               .contains((ray.getLocation())
                       .subtract(ray.getLocation()
                               .align(Iterate.axisSet)));
   }


   public boolean getRayTrace(BlockHitResult blockRayTraceResult, BlockPos pos, BlockState state) {

       Vec3 offset = blockRayTraceResult.getLocation().subtract(pos.getX(), pos.getY(), pos.getZ());


       switch (state.getValue(FACING)) {
           case NORTH -> shouldRotate = offset.x <= 0.5F;

           //    System.out.println(shouldRotate);
           case SOUTH -> shouldRotate = offset.x >= 0.5F;

           //  System.out.println(shouldRotate);
           case EAST -> shouldRotate = offset.z <= 0.5F;

           // System.out.println(shouldRotate);
           case WEST -> shouldRotate = offset.z >= 0.5F;

           // System.out.println(shouldRotate);
           case UP -> shouldRotate = offset.y <= 0.5F;

           //  System.out.println(shouldRotate);
           case DOWN -> shouldRotate = offset.y >= 0.5F;

           // System.out.println(shouldRotate);
       }
       return shouldRotate;
   }


}
*/
