package com.fallenreaper.createutilities.core.data.blocks.liquidtank;

import com.simibubi.create.CreateClient;
import com.simibubi.create.foundation.utility.Color;
import com.simibubi.create.foundation.utility.VecHelper;
import com.simibubi.create.foundation.utility.animation.LerpedFloat;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class FluidNode {
    protected IFluidTransferProvider source;
    protected IFluidTransferProvider target;
    protected Level level;

    //  List<Pair<IFluidTransfer, IFluidTransfer>> connectTanks = new ArrayList<>();

    public FluidNode(Level level) {
        this.level = level;
    }

    public static FlowType getFlowType(IFluidTransferProvider teFrom, IFluidTransferProvider teTo) {
        int y0 = teTo.getBlockPosition().getY();
        int y1 = teFrom.getBlockPosition().getY();
         boolean a  = Math.min(y1, y0) == y1;

        return y1 > y0 ? FlowType.DOWNWARDS : y0 == y1 ? FlowType.EQUAL : FlowType.UPWARDS;
    }

    public IFluidTransferProvider getSource() {
        return source;
    }

    public IFluidTransferProvider getTarget() {
        return target;
    }

    public void connectNodes(IFluidTransferProvider origin, IFluidTransferProvider target) {
        this.source = origin;

        this.target = target;

    }

    public Level getLevel() {
        return this.level;
    }
    //todo: add ability to transfer fluid to connected tanks

    public void handleFluidFlow(LiquidTankBlockEntity source, LiquidTankBlockEntity target) {
        Flow flow;
        double distance = nodesDistance();
        boolean isTankFull = target.getTank().getFluid().getAmount() >= source.getTankCapacity();
        boolean isSourceEmpty = source.getTank().getFluid().isEmpty();


        if (!isSourceEmpty && !isTankFull) {
            Vec3 start = getOriginPosition();
            Vec3 end = getTargetPosition();
            Vec3 diff = end.subtract(start);
            Vec3 stt = new Vec3(0, end.y(), 0);

            double pressure = end.distanceTo(new Vec3(0, start.y(), 0))/16*16;
            double height1 = new Vec3(0, end.y(), 0).distanceTo(new Vec3(0, start.y(), 0));


          //  float fluidAmount = source.getTank().getFluidAmount();

           // double pressure = fluidAmount / area;

       //     float P = (float) (fluidAmount*9.81*height1) * 1/16f;
            float fluidAmount = source.getTank().getFluidAmount();
            float consumptionPerTick = (float) (fluidAmount / distance) * fluidAmount / source.getTankCapacity();
            int amount = source.fluidTank.getRenderedFluid().getAmount();
            flow = new Flow(true, source.getTank().getFluid());
            flow.progress.tickChaser();

        //todo: move this to flow(),
            float flowSpeed = (float) (1 / 16f + Mth.clamp((float) pressure / (nodesDistance() * 1.5F), 0, 1));
            flow.progress.setValue(Math.min(flow.progress.getValue() + flowSpeed, 1));

            if (flow.progress.getValue() >= 1) {
                flow.complete = true;
                flow(source, target, flowSpeed);
            }
            FluidStack drained = source.getTank().drain(amount, IFluidHandler.FluidAction.EXECUTE);

            //structure this to be used with Flow.class
            //  flow(source, target, consumptionPerTick)

        }
    }

    protected void flow(LiquidTankBlockEntity source, LiquidTankBlockEntity target, float amount) {
        FluidStack sourceFluidStack = source.getTank().getFluid();

        handleInsertion(target, source.getTank().getFluid(), amount);
        if (!target.getTank().isEmpty()) {
            if (sourceFluidStack.isFluidEqual(target.getTank().getFluid())) {
                handleExtraction(source, source.getTank().getFluid(), amount);
            }
        }
    }

    protected void updateFlow(IFluidTransferProvider source, IFluidTransferProvider target, FlowType flowType) {
        BiConsumer<LiquidTankBlockEntity, LiquidTankBlockEntity> fluidFlow = this::handleFluidFlow;
        switch (flowType.getValue()) {
            case -1 -> {
                //determine the real source after checking
                LiquidTankBlockEntity propagateSource = (LiquidTankBlockEntity) source;
                LiquidTankBlockEntity propagateTarget = (LiquidTankBlockEntity) target;
                handleFluidFlow(propagateSource, propagateTarget);
            }
            case 1 -> {
                LiquidTankBlockEntity propagateSource = (LiquidTankBlockEntity) target;
                LiquidTankBlockEntity propagateTarget = (LiquidTankBlockEntity) source;
                handleFluidFlow(propagateSource, propagateTarget);
            }
            case 0 -> { //determine the real source after checking
                //todo fix this, determine which to move depending on which is full
                boolean isTankFull = source.getTank().getFluid().getAmount() >= source.getTankCapacity();

                if (source.getBlockPosition().distManhattan(target.getBlockPosition()) <= 1) {
                    LiquidTankBlockEntity propagateTarget;
                    LiquidTankBlockEntity propagateSource;
                    if (isTankFull) {
                        propagateTarget = (LiquidTankBlockEntity) source;
                        propagateSource = (LiquidTankBlockEntity) target;

                    } else {
                        propagateTarget = (LiquidTankBlockEntity) target;
                        propagateSource = (LiquidTankBlockEntity) source;

                    }
                    handleFluidFlow(propagateSource, propagateTarget);
                }
            }
        }
        /*
        if (flowType == FlowType.DOWN) { //source -> target

            //determine the real source after checking
            LiquidTankBlockEntity propagateSource = (LiquidTankBlockEntity) source;
            LiquidTankBlockEntity propagateTarget = (LiquidTankBlockEntity) target;
            fluidFlow.accept(propagateSource, propagateTarget);

        } else if (flowType == FlowType.UP) { //target -> source


            LiquidTankBlockEntity propagateSource = (LiquidTankBlockEntity) target;
            LiquidTankBlockEntity propagateTarget = (LiquidTankBlockEntity) source;
            fluidFlow.accept(propagateSource, propagateTarget);


        } else if (flowType == FlowType.EQUAL) { //source <-> target

            //determine the real source after checking
            //todo fix this, determine which to move depending on which is full
            boolean isTankFull = source.getTank().getFluid().getAmount() >= source.getTankCapacity();

            if (source.getBlockPosition().distManhattan(target.getBlockPosition()) <= 1) {
                if (isTankFull) {
                    LiquidTankBlockEntity propagateTarget = (LiquidTankBlockEntity) source;
                    LiquidTankBlockEntity propagateSource = (LiquidTankBlockEntity) target;

                    fluidFlow.accept(propagateSource, propagateTarget);

                } else {
                    LiquidTankBlockEntity propagateTarget = (LiquidTankBlockEntity) target;
                    LiquidTankBlockEntity propagateSource = (LiquidTankBlockEntity) source;

                    fluidFlow.accept(propagateSource, propagateTarget);

                }
            }

         */


    }


    protected void handleExtraction(LiquidTankBlockEntity te, FluidStack pFluidStack, float amount) {
        te.getTank().drain(new FluidStack(pFluidStack, (int) amount), IFluidHandler.FluidAction.EXECUTE);
        te.notifyUpdate();

    }

    protected void handleInsertion(LiquidTankBlockEntity te, FluidStack pFluidStack, float amount) {
        te.getTank().fill(new FluidStack(pFluidStack, (int) amount), IFluidHandler.FluidAction.EXECUTE);
        te.notifyUpdate();
    }

    public boolean isInvalid() {
        return getSource() == null && getTarget() == null && getTarget().isRemoved() && getSource().isRemoved();
    }

    protected void propagateTo(IFluidTransferProvider selectedSource, IFluidTransferProvider selectedTarget) {
        Vec3 start = getOriginPosition();
        Vec3 end = getTargetPosition();
        Vec3 diff = end.subtract(start);


        if (isInvalid())
            return;
        if (selectedSource == null || selectedTarget == null)
            return;

        switch (getFlowType(selectedSource, selectedTarget)) {
            case DOWNWARDS -> {
                //("SOURCE -> TARGET");
                updateFlow(source, target, FlowType.DOWNWARDS);
            }
            case UPWARDS -> {
                //("SOURCE <- TARGET");
                updateFlow(source, target, FlowType.UPWARDS);
            }
            case EQUAL -> {
                if (diff.length() == diff.horizontalDistance()) {
                    //("SOURCE <-> TARGET");
                    updateFlow(source, target, FlowType.EQUAL);
                }
            }
        }
    }

    public void renderNodes() {
        if (getSource() == null || getTarget() == null || getTarget().isRemoved() || getSource().isRemoved())
            return;


        Vec3 start = getOriginPosition();
        Vec3 end = getTargetPosition();
        Vec3 centerOrigin = VecHelper.getCenterOf(getSource().getBlockPosition());
        Vec3 centerTarget = VecHelper.getCenterOf(getTarget().getBlockPosition());
        Vec3 diff = end.subtract(start);
        Vec3 step = diff.normalize();
        Vec3 position = start.add(step.scale(nodesDistance()));

        BlockPos autoPos = new BlockPos(position);
        AABB originAAB = new AABB(getSource().getBlockPosition(), getTarget().getBlockPosition());


        CreateClient.OUTLINER.showLine("line" + diff, centerOrigin, centerTarget).lineWidth(1 / 16F).colored(Color.GREEN).disableCull();
     //   CreateClient.OUTLINER.showAABB("blockPos" +  diff, originAAB).colored(Color.WHITE).lineWidth((1 / 16F)).withFaceTexture(AllSpecialTextures.THIN_CHECKERED);
    }

    public void tick() {
        if (target.isRemoved()) {
            target = null;
            return;
        }

        if (source.isRemoved()) {
            source = null;
            return;
        }

        if (isInvalid())
            return;

        propagateTo(getSource(), getTarget());
        renderNodes();
    }

    public double nodesDistance() {
        if (getSource() == null || getTarget() == null || getTarget().isRemoved() || getSource().isRemoved())
            return 0;


        Vec3 origin = new Vec3(getSource().getBlockPosition().getX(), getSource().getBlockPosition().getY(), getSource().getBlockPosition().getZ());
        Vec3 target = new Vec3(getTarget().getBlockPosition().getX(), getTarget().getBlockPosition().getY(), getTarget().getBlockPosition().getZ());

        return origin.distanceTo(target);
    }

    public Vec3 getOriginPosition() {
        if (source == null)
            return Vec3.ZERO;

        return new Vec3(this.source.getBlockPosition().getX(), this.source.getBlockPosition().getY(), this.source.getBlockPosition().getZ());
    }

    public Vec3 getTargetPosition() {
        if (target == null)
            return Vec3.ZERO;

        return new Vec3(this.target.getBlockPosition().getX(), this.target.getBlockPosition().getY(), this.target.getBlockPosition().getZ());
    }
    /*
    public void tickFlowProgress(Level world, BlockPos pos) {
        if (!hasFlow())
            return;
        PipeConnection.Flow flow = this.flow.get();
        if (flow.fluid.isEmpty())
            return;

        if (world.isClientSide) {
            if (!source.isPresent())
                determineSource(world, pos);

            spawnParticles(world, pos, flow.fluid);
            if (particleSplashNextTick)
                spawnSplashOnRim(world, pos, flow.fluid);
            particleSplashNextTick = false;
        }

        float flowSpeed = 1 / 32f + Mth.clamp(pressure.get(flow.inbound) / 512f, 0, 1) * 31 / 32f;
        flow.progress.setValue(Math.min(flow.progress.getValue() + flowSpeed, 1));
        if (flow.progress.getValue() >= 1)
            flow.complete = true;
    }

     */

    public enum FlowType {
        UPWARDS(1),
        DOWNWARDS(-1),
        EQUAL(0);

        private final int value;
        private final Map<Integer, FlowType> flowTypeMap = new HashMap<>();

        FlowType(int index) {
            this.value = index;
            flowTypeMap.put(index, this);


        }

        public FlowType getFromValue(int i) {
            if (flowTypeMap.containsKey(i)) {
                return flowTypeMap.get(i);
            }
            return null;
        }

        public int getValue() {
            return value;
        }
    }

    public static class Flow {

        public boolean complete;
        public boolean inbound;
        public LerpedFloat progress;
        public FluidStack fluid;

        public Flow(boolean inbound, FluidStack fluid) {
            this.inbound = inbound;
            this.fluid = fluid;
            this.progress = LerpedFloat.linear()
                    .startWithValue(0);
            this.complete = false;
        }

        public boolean isComplete() {
            return complete;
        }

        public boolean isInbound() {
            return inbound;
        }

        public LerpedFloat getProgress() {
            return progress;
        }

        public FluidStack getFluid() {
            return fluid;
        }
    }

}

