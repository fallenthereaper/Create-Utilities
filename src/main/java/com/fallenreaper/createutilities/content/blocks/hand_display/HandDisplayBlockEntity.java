package com.fallenreaper.createutilities.content.blocks.hand_display;

import com.simibubi.create.content.contraptions.goggles.IHaveGoggleInformation;
import com.simibubi.create.foundation.tileEntity.SmartTileEntity;
import com.simibubi.create.foundation.tileEntity.TileEntityBehaviour;
import com.simibubi.create.foundation.utility.Components;
import com.simibubi.create.foundation.utility.NBTHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

import static com.fallenreaper.createutilities.content.blocks.hand_display.HandDisplayBlock.POWERED;

public class HandDisplayBlockEntity extends SmartTileEntity implements IHaveGoggleInformation {
    public ItemStack heldItem;
    public DisplayMode displayMode;
    public boolean powered;

    public HandDisplayBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.heldItem = ItemStack.EMPTY;
        this.displayMode = DisplayMode.STATIC;
    }

    @Override
    public void addBehaviours(List<TileEntityBehaviour> behaviours) {

    }
    public ItemStack getHeldItem() {
        return heldItem;
    }

    public void setHeldItem(ItemStack heldItem) {
        this.heldItem = heldItem;
        notifyUpdate();
    }

    @Override
    public void tick() {
        super.tick();
        boolean shouldPower = this.level.hasNeighborSignal(this.worldPosition);
        boolean poweredState = this.getBlockState().getValue(POWERED);

        if(shouldPower && !poweredState) {
            getLevel().setBlock(getBlockPos(), this.getBlockState().setValue(POWERED, true), 3);
        }
        else if(!shouldPower && poweredState) {
            getLevel().setBlock(getBlockPos(), this.getBlockState().setValue(POWERED, false), 3);
        }
        powered = poweredState;


        if(powered && displayMode != DisplayMode.ANIMATED) {
            setDisplayMode(DisplayMode.ANIMATED);
        }
        else if(!powered && displayMode != DisplayMode.STATIC){
            setDisplayMode(DisplayMode.STATIC);
        }
    }

    @Override
    protected void write(CompoundTag tag, boolean clientPacket) {
        tag.put("Item", heldItem.serializeNBT());
        tag.putBoolean("Powered", powered);
        NBTHelper.writeEnum(tag, "DisplayMode", this.displayMode);

        super.write(tag, clientPacket);
    }

    @Override
    protected void read(CompoundTag tag, boolean clientPacket) {
        heldItem = ItemStack.of(tag.getCompound("Item"));
        this.powered = tag.getBoolean("Powered");
        this.displayMode = NBTHelper.readEnum(tag, "DisplayMode", DisplayMode.class);
        super.read(tag, clientPacket);

    }
    @Override
    public void initialize() {
        super.initialize();
        updateDisplayMode();
    }
    public boolean isStaticDisplay() {
        return this.displayMode == DisplayMode.STATIC;
    }

    public boolean isAnimatedDisplay() {
        return this.displayMode == DisplayMode.ANIMATED;
    }

    public void setDisplayMode(DisplayMode displayMode) {
        this.displayMode = displayMode;
    }

    public void updateDisplayMode() {
        if(level == null)
            return;

        boolean shouldPower = this.level.hasNeighborSignal(this.worldPosition);
        if (shouldPower != this.getLevel().getBlockState(getBlockPos()).getValue(POWERED)) {
            this.powered = shouldPower;
            this.sendData();
            if (powered) {
                setDisplayMode(DisplayMode.ANIMATED);
                level.playSound(null, worldPosition, SoundEvents.LEVER_CLICK, SoundSource.BLOCKS,
                        .125f + level.random.nextFloat() * .125f, .75f - level.random.nextFloat() * .25f);
            }
             else{
                setDisplayMode(DisplayMode.STATIC);
                level.playSound(null, worldPosition, SoundEvents.ARMOR_EQUIP_GENERIC, SoundSource.BLOCKS,
                        .125f + level.random.nextFloat() * .125f, .25f - level.random.nextFloat() * .2f);
            }

        }
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        Component indent1 = Component.literal(spacing + " ");
        tooltip.add(indent1.plainCopy()
                .append("Brass Hand Display:"));

        String name = heldItem.getItem() instanceof BlockItem ? "Block" : "Item";
        if (!heldItem.isEmpty())


            tooltip.add(Component.literal(name + ":").plainCopy().append(" ").append(Components.translatable(heldItem.getDescriptionId()).getString()).append(" ").withStyle(ChatFormatting.GREEN));



        return true;
    }


    public enum DisplayMode {
        ANIMATED,
        STATIC;
    }
}
