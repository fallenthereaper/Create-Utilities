package com.fallenreaper.createutilities.content.blocks.punchcard_writer;

import com.fallenreaper.createutilities.core.data.punchcard.PunchcardWriter;
import com.fallenreaper.createutilities.core.utils.ContainerBlockEntity;
import com.simibubi.create.foundation.tileEntity.TileEntityBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

//------WIP------//
public class PunchcardWriterBlockEntity extends ContainerBlockEntity<PunchcardWriterItemHandler> implements MenuProvider {

    public boolean hasPunchcard;
    public ContainerLevelAccess levelAccess;
    public PunchcardWriter writer;

    public PunchcardWriterBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {

        super(type, pos, state);
        this.inventory = new PunchcardWriterItemHandler(this);

    }

    @Override
    public void addBehaviours(List<TileEntityBehaviour> behaviours) {

    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap) {

        return super.getCapability(cap);
    }
    public void createWriter(AbstractSmartContainerScreen<?> screen,int x, int y, int width, int height) {
        this.writer = PunchcardWriter.create(screen, x, y, width, height);
    }

    public void setWriter(PunchcardWriter writer) {
        this.writer = writer;
    }

    public PunchcardWriter getWriter() {
        return writer;
    }

    @Override
    public void sendToContainer(FriendlyByteBuf buffer) {
        super.sendToContainer(buffer);
        buffer.writeUUID(UUID.randomUUID());
        addAccess();

    }

    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);
        compound.putBoolean("HasPunchcard", !inventory.getStackInSlot(0).isEmpty());
        if(writer != null)
            writer.write(compound, clientPacket);
    }

    public boolean hasPunchcard() {
        return hasPunchcard;
    }

    public void addAccess() {
        this.levelAccess = ContainerLevelAccess.create(this.level, this.getBlockPos());
    }

    @Override
    public void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
        hasPunchcard = compound.getBoolean("HasPunchcard");
        if(writer != null)
            writer.read(compound, clientPacket);
    }

    @Override
    public Component getDisplayName() {
        return  Component.literal("Punchcard Writer");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {

        return PunchcardWriterContainer.create(pContainerId, pPlayerInventory, this, levelAccess);
    }

}
