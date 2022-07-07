package com.fallenreaper.createutilities.content.blocks.bellow;


import com.fallenreaper.createutilities.utils.IFurnaceBurnTimeAccessor;
import com.simibubi.create.content.contraptions.base.KineticTileEntity;
import com.simibubi.create.content.contraptions.goggles.IHaveGoggleInformation;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.ForgeHooks;

import java.util.List;
import java.util.function.Consumer;

public class BellowBlockEntity extends KineticTileEntity implements IHaveGoggleInformation {

    public BellowBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
        setLazyTickRate(20);
    }

    @Override
    public void tick() {
        super.tick();
        initialize((be)-> {


                    System.out.println(getFuelItemStack(be).getItem().getDescriptionId());
                    be.serializeNBT().getInt("BurnTime");
                    IFurnaceBurnTimeAccessor accessor = (IFurnaceBurnTimeAccessor) be;
         //   SprinklerBlock a = new SprinklerBlock(BlockBehaviour.Properties.copy(getBlockState().getBlock()));

                    accessor.setBurnTime((int) (getFurnaceBurnTime(getFuelItemStack(be))+getSpeed()/256));

                }
        );
    }

    @Override
    public void lazyTick() {



    }

    protected int getFuelBurntime(AbstractFurnaceBlockEntity be){
        return be.serializeNBT().getInt("BurnTime");
    }

    //for testing purposes
  protected void initialize(Consumer<AbstractFurnaceBlockEntity> consumer){

        if(getBlockEntity(getFurnacePos(getBlockPos())) instanceof AbstractFurnaceBlockEntity furnace) {
        if(!getFuelItemStack(furnace).isEmpty() && BellowInteractionHandler.getState(getFurnaceBlockstate(getFurnacePos(getBlockPos()))).isRunning()){
              consumer.accept(furnace);
            }
        }
    }

    private ItemStack getFuelItemStack(AbstractFurnaceBlockEntity be) {
        return be.getItem(1);
    }
    @Override
    public AABB getRenderBoundingBox() {
        return new AABB(this.worldPosition);
    }

    private int getFurnaceBurnTime(ItemStack stack) {
        if(stack.isEmpty())
            return 0;

        return ForgeHooks.getBurnTime(stack, null);
    }

    @Override
    public boolean addToTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        return true;
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        BlockPos pos = getBlockPos().below();
        BlockEntity be = this.level.getBlockEntity(pos);

        Component indent = new TextComponent(spacing + " ");
        tooltip.add(indent.plainCopy()
                .append("Bellow Info:"));
        if(!this.level.isClientSide()) {
            if (getBlockEntity(getFurnacePos(getBlockPos())) instanceof AbstractFurnaceBlockEntity furnace) {
                if (!getFuelItemStack(furnace).isEmpty()) {
                    Component tex = new TextComponent(getFuelItemStack(furnace).getItem().toString());
                    tooltip.add(indent.plainCopy().append(tex));

                }
            }
        }

        return true;
    }
    protected BlockPos getFurnacePos(BlockPos pos) {
        return pos.below();
    }

    protected BlockState getFurnaceBlockstate(BlockPos pos) {
         BlockState state = getLevel().getBlockState(pos);
        return state;
    }

    protected BlockEntity getBlockEntity(BlockPos pos) {
       BlockEntity be = getLevel().getBlockEntity(pos);
        return be;
    }

}
