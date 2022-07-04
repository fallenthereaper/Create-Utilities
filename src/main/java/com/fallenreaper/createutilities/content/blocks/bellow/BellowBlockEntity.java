package com.fallenreaper.createutilities.content.blocks.bellow;


import com.simibubi.create.content.contraptions.base.KineticTileEntity;
import com.simibubi.create.content.contraptions.goggles.IHaveGoggleInformation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class BellowBlockEntity extends KineticTileEntity implements IHaveGoggleInformation {
    private AbstractFurnaceBlockEntity furnace;

    public BellowBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    }

    @Override
    public void tick() {
        super.tick();
        setFuel();
    }
    //for testing purposes
    void setFuel(){
        BlockPos pos = getBlockPos().below();
        BlockEntity be = this.level.getBlockEntity(pos);
        if(be instanceof AbstractFurnaceBlockEntity furnace) {
            if(!furnace.getItem(1).isEmpty()) {
             ItemStack item =   furnace.getItem(1);
             item.grow(1);
            }
            if(furnace.getContainerSize() != 0)
            System.out.println(furnace.getContainerSize());

        }
    }
}
