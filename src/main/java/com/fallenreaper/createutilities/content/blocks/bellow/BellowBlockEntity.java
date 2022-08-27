package com.fallenreaper.createutilities.content.blocks.bellow;


import com.fallenreaper.createutilities.utils.IFurnaceBurnTimeAccessor;
import com.simibubi.create.content.contraptions.base.KineticTileEntity;
import com.simibubi.create.content.contraptions.goggles.IHaveGoggleInformation;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
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
    public int timer;
    public boolean isValid;
    public int remainingTime;
    private ItemStack itemIn;
    public int maxTime;

    public BellowBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
        setLazyTickRate(20);
    }

    public ItemStack getItemIn() {
        return itemIn;
    }

    @Override
    public void tick() {
        super.tick();

        if (timer <= 0)
            timer = 0;

        if (isValid && getSpeed() != 0) {
            timer++;
        }

        if (!isValid || getSpeed() == 0)
            timer = 0;

        // System.out.println("Counter:"+ " " + timer);
        initialize(be -> {
                    if (!(be.getItem(1).isEmpty()))
                        this.itemIn = getFuelItemStack(be);

                    // System.out.println(getFuelItemStack(be).getItem().getDescriptionId());
                    if (!getFuelItemStack(be).isEmpty()) {
                        this.remainingTime = be.serializeNBT().getInt("BurnTime");
                    }
                    IFurnaceBurnTimeAccessor accessor = (IFurnaceBurnTimeAccessor) be;
                    accessor.getBurnTime();
                    //   SprinklerBlock a = new SprinklerBlock(BlockBehaviour.Properties.copy(getBlockState().getBlock()));
                }
        );
    }

    @Override
    public void lazyTick() {
    }

    protected int getFuelBurntime(AbstractFurnaceBlockEntity be) {
        return be.serializeNBT().getInt("BurnTime");
    }

    //for testing purposes
    protected void initialize(Consumer<AbstractFurnaceBlockEntity> consumer) {

        if (getBlockEntity(getFurnacePos(getBlockPos())) instanceof AbstractFurnaceBlockEntity furnace) {
            if (!getFuelItemStack(furnace).isEmpty() && BellowInteractionHandler.getState(getFurnaceBlockstate(getFurnacePos(getBlockPos()))).isRunning()) {
                isValid = true;
                sendData();
                consumer.accept(furnace);
            } else
                isValid = false;
            sendData();
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
        if (stack.isEmpty())
            return 0;

        return ForgeHooks.getBurnTime(stack, null);
    }

    @Override
    public boolean addToTooltip(List<Component> tooltip, boolean isPlayerSneaking) {

        return true;
    }

    @Override
    protected void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);
        if (this.itemIn != null) {
            compound.put("ItemIn", this.itemIn.serializeNBT());
            compound.putInt("RemainingTime", this.remainingTime);
            compound.putInt("TotalTime", getMaxBurnTime(itemIn));
        }
        compound.putInt("Timer", this.timer);
        compound.putBoolean("IsValid", isValid);
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
        timer = compound.getInt("Timer");
        isValid = compound.getBoolean("IsValid");
        maxTime = compound.getInt("TotalTime");
        remainingTime = compound.getInt("RemainingTime");

        this.itemIn = ItemStack.of(compound.getCompound("ItemIn"));

    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {

        Component indent = new TextComponent(" ");
        Component indent1 = new TextComponent(spacing + " ");
        Component arrow = new TextComponent("->").withStyle(ChatFormatting.DARK_GRAY);
        Component time = new TextComponent(getTotalTime(getMaxBurnTime(itemIn))).withStyle(ChatFormatting.GOLD);
        String item = itemIn.isEmpty() ? "None" : itemIn.getItem().getName(itemIn).getString();
        Component in = new TextComponent(item + " " + (itemIn.isEmpty() ? "" : "x") + (itemIn.getCount() <= 0 ? "" : itemIn.getCount())).withStyle(itemIn.isEmpty() ? ChatFormatting.RED : ChatFormatting.GREEN).withStyle(ChatFormatting.UNDERLINE);
        Component status = new TextComponent( isValid ? "RUNNING" : "PAUSED").withStyle(ChatFormatting.AQUA);
        tooltip.add(indent1.plainCopy()
                .append("Bellow Info:"));
        tooltip.add(arrow.plainCopy()
                .append(indent)
                .append("Time Elapsed:").withStyle(ChatFormatting.GRAY).append(indent).append(time));

        tooltip.add(arrow.plainCopy().append(indent).append("Fuel:").withStyle(ChatFormatting.GRAY).append(indent).append(in));
        tooltip.add(arrow.plainCopy().append(indent).append("Status:").withStyle(ChatFormatting.GRAY).append(indent).append(status));


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

    //Credits: Create Aeronautics, I had no idea I could use "+=" on strings
    protected String getTotalTime(int ticks) {
        String base = "";
        int seconds = ticks / 20;
        int minutes = seconds / 60;
        int hours = minutes / 60;
        int days = hours / 24;
        int weeks = days / 7;

        seconds = seconds % 60;
        minutes = minutes % 60;
        hours = hours % 60;

        if (weeks > 0)
            base += weeks + "w ";

        if (days > 0)
            base += days + "d ";

        if (hours > 0)
            base += hours + "h ";

        if (minutes > 0)
            base += minutes + "m ";

        base += seconds + "s ";
        return base;

    }

    protected int getMaxBurnTime(ItemStack itemStack) {
        if (itemStack.isEmpty())
            return 0;

        int seconds = remainingTime;

        //convert to seconds
        int modifier = getFurnaceBurnTime(itemStack);


        return seconds + (modifier * itemStack.getCount());
    }

}
