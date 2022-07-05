package com.fallenreaper.createutilities.content.blocks.bellow;


import com.simibubi.create.content.contraptions.base.KineticTileEntity;
import com.simibubi.create.content.contraptions.goggles.IHaveGoggleInformation;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.ForgeHooks;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;


public class BellowBlockEntity extends KineticTileEntity implements IHaveGoggleInformation {

    public BellowBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    }

    @Override
    public void tick() {
        super.tick();
        setFuel(()-> {
            if(getBlockEntity(getFurnacePos(getBlockPos())) instanceof AbstractFurnaceBlockEntity furnace) {
                if(!getFuelItemStack(furnace).isEmpty()) {
                    System.out.println(getFuelItemStack(furnace).getItem());
                    int diff = Math.abs(getFurnaceBurnTime(getFuelItemStack(furnace)) - furnace.serializeNBT().getInt("BurnTime"));
                    //furnace.serializeNBT().remove("BurnTime");



                    // System.out.println(getFuelBurntime(furnace));
                }
            }
        });
    }

    protected int getFuelBurntime(AbstractFurnaceBlockEntity be){
        return be.serializeNBT().getInt("BurnTime");
    }


    //for testing purposes

    void setFuel(Runnable run){
run.run();

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
