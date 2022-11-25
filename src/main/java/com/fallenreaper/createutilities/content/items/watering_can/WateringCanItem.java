package com.fallenreaper.createutilities.content.items.watering_can;

import com.fallenreaper.createutilities.content.blocks.sprinkler.SprinklerInteractionHandler;
import com.fallenreaper.createutilities.content.items.BaseItem;
import com.jozufozu.flywheel.repack.joml.Math;
import com.simibubi.create.content.contraptions.fluids.tank.FluidTankTileEntity;
import com.simibubi.create.content.contraptions.particle.CubeParticleData;
import com.simibubi.create.foundation.utility.Color;
import com.simibubi.create.foundation.utility.VecHelper;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.Random;
//todo: actually work on this, ex: use capabilities also add a specialized tool tip for item tanks, also look at how supplementaries do jars
public class WateringCanItem extends BaseItem {
    public WateringCanFluidTank tank;

    public WateringCanItem(Properties pProperties) {
        super(pProperties);
        tank = new WateringCanFluidTank(250,(fluidStack -> {}));
    }

    @Override
    public @NotNull Optional<TooltipComponent> getTooltipImage(ItemStack pStack) {
        return super.getTooltipImage(pStack);
    }

    @Override
    public void verifyTagAfterLoad(CompoundTag pCompoundTag) {
        super.verifyTagAfterLoad(pCompoundTag);
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        return super.initCapabilities(stack, nbt);
    }
//todo: make it so you can only "hydrate" when theres a crop near the 6 radius between the bplayer
    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
     if(tank != null && !tank.isEmpty()) {
         if(!pLevel.isClientSide) {
             hydrate(pPlayer, pLevel);
         }
        return InteractionResultHolder.consume(pPlayer.getItemInHand(pUsedHand));
     }
        return super.use(pLevel, pPlayer, pUsedHand);
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        BlockPos pos = pContext.getClickedPos();
        Level level = pContext.getLevel();
        Player player = pContext.getPlayer();

        if(player != null) {
            if (player.isCrouching()) {

                if (level.getBlockEntity(pos) instanceof FluidTankTileEntity te)
                    if (tank != null) {
                        if (!(tank.getFluidAmount() >= tank.getCapacity())) {
                            if (te.hasTank()) {
                                if (te.canPlayerUse(player)) {
                                    if (!te.getTank(0).getFluid().isEmpty() && te.getTank(0).getFluid().isFluidEqual(new FluidStack(Fluids.WATER, 5))) {
                                        if (level.isClientSide()) {
                                            var m = new Vec3(pos.getX(), pos.getY(), pos.getZ())
                                                    .scale(.325f);
                                            m = VecHelper.rotate(m, new Random().nextFloat() * 360, Direction.Axis.X);
                                            m = m.add(VecHelper.offsetRandomly(Vec3.ZERO, new Random(), 4f));

                                            CubeParticleData data =
                                                    new CubeParticleData(0, 0.25F, .95f, .65f + (new Random().nextFloat() - .5f) * .25f, 4, false);
                                            level.addParticle(data, pos.getX(), pos.getY(), pos.getZ(), m.x, m.y, m.z);
                                            level.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.GENERIC_SPLASH, SoundSource.PLAYERS, 2, 2);
                                        }
                                        tank.fill(new FluidStack(Fluids.WATER, 250 / 4), IFluidHandler.FluidAction.EXECUTE);

                                        te.getTank(0).drain(250 / 4, IFluidHandler.FluidAction.EXECUTE);
                                        return InteractionResult.CONSUME;
                                    }
                                }
                            }
                        }
                    }
            }
           }
        return InteractionResult.FAIL;
    }

    @Override
    public boolean isBarVisible(ItemStack pStack) {
        return !tank.isEmpty();
    }

    @Override
    public int getBarWidth(ItemStack pStack) {
          return Math.round(Math.min((13.0F * (float) (tank.getFluidAmount() / tank.getCapacity())), 13.0F));
    }

    @Override
    public int getBarColor(ItemStack pStack) {
        return Color.WHITE.getRGB();
    }

    private void hydrate(Player player, Level level) {
        var position = new Vec3(player.getX(), player.getY(), player.getZ());
        int radius = 6;
       var pos = new BlockPos(position.x, position.y, position.z);
       var random = new Random();

        AABB aabb = new AABB(pos).inflate(radius, 0, radius);
        if(tank.isEmpty())
            return;
        // MutableBoundingBox boundingBox = new MutableBoundingBox(worldPosition.offset(-getRadius(), -1, -getRadius()), worldPosition.offset(getRadius(), -1, getRadius()));
        // Farmland hydration & plants growth logic
        for (BlockPos blockPositions : BlockPos.randomBetweenClosed(random, 15, (int) (position.x - radius), (int) position.y, (int) (position.z - radius), (int) position.x + radius, (int) position.y + 1, (int) position.z+ radius)) {
            var blockState = level.getBlockState(blockPositions);

            if (SprinklerInteractionHandler.checkForPlants(blockState.getBlock())) {
                if (SprinklerInteractionHandler.isInsideCircle(radius,pos , blockPositions)) {
                    if (random.nextInt(100) < 50)
                        blockState.getBlock().randomTick(blockState, (ServerLevel) level, blockPositions, random);
                }
            }
        }
        tank.drain( 250/5, IFluidHandler.FluidAction.SIMULATE);
    }
//todo: add arm animations
    @Override
    public <T extends LivingEntity> boolean poseArm(ItemStack itemStack, HumanoidArm arm, HumanoidModel<T> model, T entity, boolean rightHand) {
        if(itemStack.getItem() instanceof WateringCanItem) {

        }
        return super.poseArm(itemStack, arm, model, entity, rightHand);
    }
}
