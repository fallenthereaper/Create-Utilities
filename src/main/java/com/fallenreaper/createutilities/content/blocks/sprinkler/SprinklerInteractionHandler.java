package com.fallenreaper.createutilities.content.blocks.sprinkler;

import com.fallenreaper.createutilities.utils.InteractionHandler;
import com.jozufozu.flywheel.repack.joml.Vector3d;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.FarmlandWaterManager;


public class SprinklerInteractionHandler extends InteractionHandler {


    public static boolean hasFarmlandBlock(BlockPos pos, Level level) {
        BlockState stateAtPos = level.getBlockState(pos);

        Block getBlock = stateAtPos.getBlock();

        return getBlock instanceof FarmBlock;
    }

    public static void hydrateFarmland(BlockPos blockPos, Level worldIn, BlockState state, AABB aabb) {

        int getMoistureLevel = state.getValue(FarmBlock.MOISTURE);

        if (getMoistureLevel < 7) {

            worldIn.setBlock(blockPos, state.setValue(FarmBlock.MOISTURE, 7), 2);
            FarmlandWaterManager.addAABBTicket(worldIn, aabb);

        }


    }

    public static boolean isInsideCircle(double radius, BlockPos blockPos, BlockPos target) {
        Vector3d centerPos = new Vector3d(blockPos.getX(), blockPos.getY(), blockPos.getZ());
        Vector3d targetPos = new Vector3d(target.getX(), target.getY(), target.getZ());
        double distance = (int) centerPos.distance(targetPos);

        return distance <= radius;

    }
   /* public static void createBoundingBox(float radius, BlockPos startPos, int height, World worldIn, SprinklerTileEntity te) {

        BoundingBox boundingBox = new BoundingBox(startPos.offset(-radius, height, -radius), startPos.offset(radius, height, radius));


        startPos.betweenClosedStream(boundingBox).forEach(iteratedBlockPos -> {
            BlockState state = worldIn.getBlockState(iteratedBlockPos);
            Block foundBlock = state.getBlock();
           if(isInsideCircle(radius, startPos, iteratedBlockPos)) {
            if(checkForPlants(state)) {
                if(!worldIn.isClientSide() && (te.getWorld() instanceof ServerWorld)) {
                    BoneMealItem.addGrowthParticles(worldIn, iteratedBlockPos, 2);
                    if (te.getWorld().getRandom().nextDouble() < CAConfig.FARMLAND_HYDRATE_CHANCE.get()) {
                        foundBlock.randomTick(state, (ServerWorld) te.getWorld(), iteratedBlockPos, te.getWorld().getRandom());
                    }
                }
                worldIn.getBlockTicks().scheduleTick(iteratedBlockPos, foundBlock, 0, TickPriority.HIGH);

            //   randomTicker(worldIn, state, iteratedBlockPos, new Random(), te);

           }


           }


           }
        );
    }
*/


   /* protected ActionResultType doWater(ItemStack stack, World world, PlayerEntity player, BlockPos pos, Direction direction) {

        int range = (this.range - 1) / 2;
        Stream<BlockPos> blocks = BlockPos.betweenClosedStream(pos.offset(-range, -range, -range), pos.offset(range, range, range));
        blocks.forEach(aoePos -> {
            BlockState aoeState = world.getBlockState(aoePos);
            if (aoeState.getBlock() instanceof FarmlandBlock) {
                int moisture = aoeState.getValue(FarmlandBlock.MOISTURE);
                if (moisture < 7) {
                    world.setBlock(aoePos, aoeState.setValue(FarmlandBlock.MOISTURE, 7), 3);
                }
            }
        });

       /* for (int x = -range; x <= range; x++) {
            for (int z = -range; z <= range; z++) {
                double d0 = pos.offset(x, 0, z).getX() + world.getRandom().nextFloat();
                double d1 = pos.offset(x, 0, z).getY() + 1.0D;
                double d2 = pos.offset(x, 0, z).getZ() + world.getRandom().nextFloat();

                BlockState state = world.getBlockState(pos);
                if (state.canOcclude() || state.getBlock() instanceof FarmlandBlock)
                    d1 += 0.3D;

                world.addParticle(ParticleTypes.RAIN, d0, d1, d2, 0.0D, 0.0D, 0.0D);
            }
        }*/

    /* if (!world.isClientSide()) {
         if (Math.random() <= this.chance) {
             blocks = BlockPos.betweenClosedStream(pos.offset(-range, -range, -range), pos.offset(range, range, range));
             blocks.forEach(aoePos -> {
                 BlockState state = world.getBlockState(aoePos);
                 Block plantBlock = state.getBlock();
                 if (plantBlock instanceof IGrowable || plantBlock instanceof IPlantable || plantBlock == Blocks.MYCELIUM || plantBlock == Blocks.CHORUS_FLOWER) {
                     state.randomTick((ServerWorld) world, aoePos, random);
                 }
             });

             return ActionResultType.PASS;
         }
     }

     return ActionResultType.PASS;
 }*/
 /*   public boolean isMaxAge(BlockState pState) {

        return pState.getValue(getAgeProperty(pState)) >= this.getMaxAgeIn(pState);
    }
*/
    public static boolean checkForPlants(BlockState blockState) {
        Block foundBlock = blockState.getBlock();

        return foundBlock instanceof BonemealableBlock;
    }

    public static double randomWithRange(double min, double max) {

        double range = (max - min) + 1;
        return (Math.random() * range) + min;
    }
/*
    public static void randomTick(BlockState pState, World pLevel, BlockPos pPos, Random pRandom, SprinklerTileEntity te) {
   float speed = te.getSpeed();
            int age = getAge(pState);
            if (age < getMaxAgeIn(pState)) {
                float f = getGrowthSpeed(pState.getBlock(), pLevel, pPos);
                if (pRandom.nextInt((int) (50.0F / f) + 1) <= 6){
                    pLevel.setBlock(pPos, getStateForAgeIn(age + 1, pState), 2);
                    BoneMealItem.addGrowthParticles(pLevel, pPos, 2);
                    ForgeHooks.onCropsGrowPost(pLevel, pPos, pState);
                }
            }
        }

    protected static float getGrowthSpeed(Block pBlock, IBlockReader pLevel, BlockPos pPos) {
        float f = 1.0F;
        BlockPos blockpos = pPos.below();

        for (int i = -1; i <= 1; ++i) {
            for (int j = -1; j <= 1; ++j) {
                float f1 = 0.0F;
                BlockState blockstate = pLevel.getBlockState(blockpos.offset(i, 0, j));

                if (i != 0 || j != 0) {
                    f1 /= 50.0F;
                }

                f += f1;
            }
        }
        return  f;

    }

  public void growCrops(World pLevel, BlockPos pPos, BlockState pState) {
        int i = getAge(pState) + getBonemealAgeIncrease(pLevel);
        int j = getMaxAgeIn(pState);
        if (i > j) {
            i = j;
        }
        int min = 1;
        int max = 100;
        int randomInt = (int)Math.floor(Math.random()*(max-min+1)+min);

            pLevel.setBlock(pPos, getStateForAgeIn(i, pState), 2);
            BoneMealItem.addGrowthParticles(pLevel, pPos, 2);

    }

    public static BlockState getStateForAgeIn(int pAge, BlockState blockState) {
        return blockState.getBlock().defaultBlockState().setValue(getAgeProperty(blockState), Integer.valueOf(pAge));
    }

    public static int getMaxAgeIn(BlockState state) {
     if(state.getBlock() instanceof BeetrootBlock) {
           //return ((CropsBlock) state.getBlock()).getMaxAge();
           return 3;

        }
       return 7;


    }

    protected static int getAge(BlockState pState) {
        return pState.getValue(getAgeProperty(pState));
    }

    public static IntegerProperty getAgeProperty(BlockState blockState) {

      if(blockState.getBlock() instanceof BeetrootBlock) {
            return BeetrootBlock.AGE;
        }
        else if(blockState.getBlock() instanceof CarrotBlock) {
            return CarrotBlock.AGE;
        }
        else if(blockState.getBlock() instanceof PotatoBlock) {
            return PotatoBlock.AGE;

        }
        else if(blockState.getBlock() instanceof StemBlock) {
            return StemBlock.AGE;

        }

        return CropsBlock.AGE;
      //  return ((CropsBlock) blockState.getBlock()).AGE;
        //return CropsBlock.AGE;
    }





    protected int getBonemealAgeIncrease(World pLevel) {
        return MathHelper.nextInt(pLevel.random, 3, 45);
    }

    public static void growPlant(BlockState blockState, World worldIn, Random random, BlockPos blockPos) {
  if(!worldIn.isClientSide()){

      blockState.randomTick((ServerWorld) worldIn, blockPos, random);
      BoneMealItem.addGrowthParticles(worldIn, blockPos, 2);
  }
    }
*/
}



