package com.fallenreaper.createutilities.utils.data.blocks;

import com.fallenreaper.createutilities.utils.InteractionHandler;
import com.fallenreaper.createutilities.utils.data.IDevInfo;
import com.simibubi.create.content.contraptions.goggles.IHaveGoggleInformation;
import com.simibubi.create.content.contraptions.processing.EmptyingByBasin;
import com.simibubi.create.foundation.fluid.SmartFluidTank;
import com.simibubi.create.foundation.tileEntity.TileEntityBehaviour;
import com.simibubi.create.foundation.tileEntity.behaviour.BehaviourType;
import com.simibubi.create.foundation.tileEntity.behaviour.fluid.SmartFluidTankBehaviour;
import com.simibubi.create.foundation.utility.Pair;
import com.simibubi.create.foundation.utility.animation.LerpedFloat;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class LiquidTankBlockEntity extends InteractableBlockEntity implements IFluidTransferProvider, IHaveGoggleInformation, IDevInfo {
    public FluidNode node;
    private SmartFluidTankBehaviour tank;
    protected int fluidCapacity = 1024;
    private LerpedFloat fluidLevel;
    private boolean isNodeConnected = false;
    private UUID uuidKey;
    private boolean hasFluidIn;
    public boolean propagateNeighbours = false;
    public LiquidTankSegment fluidTank;


    public LiquidTankBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        fluidTank = new LiquidTankSegment(getTankCapacity(), this);
        registerNode();

    }



    public int getFluidAmount() {
        return getTank().getFluid().getAmount();
    }

    @Override
    public void registerNode() {
        node = new FluidNode(this.level);

    }

    public boolean hasOriginBlock() {
        return node.getSource() != null && !getNode().getSource().isRemoved();
    }

    public boolean hasTargetBlock() {
        return node.getTarget() != null && !getNode().getTarget().isRemoved();
    }

    public LerpedFloat getFluidLevel() {
        return tank.getPrimaryTank().getFluidLevel();
    }

    @Override
    public SmartFluidTank getTank() {
        return fluidTank.getTank();
    }

    public UUID getUuidKey() {
        return uuidKey;
    }

    private FluidStack getCurrentFluidInTank() {
        return fluidTank.getRenderedFluid();
    }
    @Override
    public void initialize() {
        super.initialize();
        if(getLevel() != null) {
            if (getLevel().isClientSide)
                return;
        }


            fluidTank.fluidLevel.forceNextSync();
            fluidTank.onFluidStackChanged();

    }
    @Override
    protected boolean isFluidHandlerCap(Capability<?> cap) {
        return cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY;

    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && side != Direction.DOWN)
            return tank.getCapability()
                    .cast();
        return super.getCapability(cap, side);

    }

    @Override
    public InteractionResult onInteract(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHitResult) {
        ItemStack pItemStack = pPlayer.getItemInHand(pHand);
        boolean present = pItemStack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY)
                .isPresent();
        if (pItemStack.getItem() instanceof BlockItem
                && !present) {
            return InteractionResult.PASS;
        }


            if (present) {
                if(this.getTank() != null) {
                    Pair<FluidStack, ItemStack> emptyItem = EmptyingByBasin.emptyItem(pLevel, pItemStack, true);

                    FluidStack fluidFromItem = emptyItem.getFirst();

                    this.getTank().fill(fluidFromItem, IFluidHandler.FluidAction.EXECUTE);
                    //   transfer.getTank().fill(fluidFromItem, IFluidHandler.FluidAction.EXECUTE);
                    pLevel.playSound(pPlayer, pPos , SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 1.8f, 1f);
                    this.notifyUpdate();

                    if(pPlayer.getItemInHand(pHand).is(Items.BUCKET)) {
                        if(this.getTank().getFluidAmount() > 0) {
                            this.getTank().drain(Math.min(getTankCapacity(), getTank().getFluidAmount()), IFluidHandler.FluidAction.EXECUTE);
                            pLevel.playSound(pPlayer, pPos , SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 1.8f, 1f);
                            if(pLevel.isClientSide) {
                                return InteractionResult.SUCCESS;
                            }
                            return InteractionResult.SUCCESS;
                        }
                    }
                    if(pLevel.isClientSide) {
                        return InteractionResult.SUCCESS;
                    }
                    return InteractionResult.SUCCESS;

                }



        }

        return super.onInteract(pState, pLevel, pPos, pPlayer, pHand, pHitResult);
    }

    @Override
    public boolean hasNode() {
        return isNodeConnected;
    }

    @Override
    public FluidNode getNode() {
        return node;
    }

    @Override
    public BlockPos getBlockPosition() {
        return this.getBlockPos();
    }

    @Override
    public int getTankCapacity() {
        return fluidCapacity;
    }


    @Override
    public void deserializeNBT(CompoundTag nbt) {
        super.deserializeNBT(nbt);
    }

    @Override
    public void addBehaviours(List<TileEntityBehaviour> behaviours) {

        this.tank = SmartFluidTankBehaviour.single(this, getTankCapacity());
        behaviours.add(tank);



    }


    @Override
    public void tick() {
        super.tick();

        this.isNodeConnected = hasOriginBlock() && hasTargetBlock() && !getNode().getTarget().isRemoved() && !getNode().getSource().isRemoved();

        if (hasNode()) {
            node.tick();
            //   node.renderCubes();
        }
        if(fluidTank != null) {
            hasFluidIn = !getCurrentFluidInTank().isEmpty();
            update();
            if (fluidLevel != null)
                fluidLevel.tickChaser();
        }


    }

    @Override
    public void setRemoved() {
       super.setRemoved();
       if(getUuidKey() != null) {
           if (FluidNodeNetwork.hasNode(getUuidKey()))
               FluidNodeNetwork.removeNode(getUuidKey());
       }
    }

    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);

        compound.putBoolean("HasFluid", hasFluidIn);
        compound.putBoolean("HasNode", this.isNodeConnected);
        compound.putBoolean("PropagateNeighbour", this.propagateNeighbours);
        compound.put("Tank", this.fluidTank.writeNBT());
//        compound.putInt("FluidAmount", getFluidAmount());

        if(this.uuidKey != null) {
            compound.putUUID("NodeUUID", this.uuidKey);
        }





    }

    @Override
    public void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);

        if(tank != null) {
            tank.read(compound, clientPacket);
        }
        this.isNodeConnected = compound.getBoolean("HasNode");
        this.hasFluidIn = compound.getBoolean("HasFluid");
        this.fluidTank.readNBT(compound, clientPacket);


        if(uuidKey != null) {
            this.uuidKey = compound.getUUID("NodeUUID");
        }

        //   if (compound.contains("ForceFluidLevel") || fluidLevel == null)
        // fluidLevel = LerpedFloat.linear()
        // .startWithValue(getFillState());

    }

    public void addInteractionHandler(List< InteractionHandler> interactions) {

    }




    public float getFillState() {
        return (float) getTank().getFluidAmount() / getTank().getCapacity();
    }

    public void setUuidKey(UUID key) {
        this.uuidKey = key;
    }
    private LiquidTankBlockEntity getNearbyTank(Direction direction) {
        if (level == null)
            return null;



        BlockPos tankPos = worldPosition;

      //  if (direction.getAxis().isHorizontal()) {
            tankPos = tankPos.relative(direction);
            BlockState pState = level.getBlockState(tankPos);

            BlockEntity be = level.getBlockEntity(tankPos);
            if (be instanceof LiquidTankBlockEntity && !be.isRemoved())
                return (LiquidTankBlockEntity) be;
    //    }
        return null;
    }

    private List<LiquidTankBlockEntity> getNearbyTanks() {
        List<LiquidTankBlockEntity> nearbyTanks = new LinkedList<>();
        for (Direction d : Direction.values()) {
            LiquidTankBlockEntity nearbyTank = getNearbyTank(d);
            if (nearbyTank == null)
                continue;
            nearbyTanks.add(nearbyTank);
        }
        return nearbyTanks;
    }


    //command every neighbour LiquidTank to update
    public void update() {
        if (getNearbyTanks().size() > 0) {
            this.propagateNeighbours = true;
            List<LiquidTankBlockEntity> inputs = getNearbyTanks();
            inputs.forEach(te -> te.propagate(inputs.size()));
        }
        else {
            this.propagateNeighbours = false;
        }
    }
    @Override
    protected AABB createRenderBoundingBox() {
        return new AABB(worldPosition).expandTowards(2, 0, 2);
    }

    //this should be used to make every LiquidTank distribute
    private void propagate(int tanksCount) {
        if(canPropagate()) {
            if(hasFluidIn) {



               // sendData();

            }
        }
    }

    public void onAdded() {
        refreshBlockState();


    }
    boolean canPropagate() {
        return propagateNeighbours;
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        containedFluidTooltip(tooltip, isPlayerSneaking,
                getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY));
        return true;
    }

    @Override
    public <T extends TileEntityBehaviour> T getBehaviour(BehaviourType<T> type) {
        return super.getBehaviour(type);
    }

    @Override
    public String getProvidedInfo() {
        return "HasNode: " + hasNode() + " HasNeighbours: " + canPropagate() + " HasFluidIn: "  + hasFluidIn;
    }
}
