/*
package com.fallenreaper.createutilities.networking;


import com.fallenreaper.createutilities.core.data.punchcard.InstructionManager;
import com.simibubi.create.foundation.networking.SimplePacketBase;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PunchcardAccessPacket extends SimplePacketBase {
    private InstructionManager instructionManager;

    public PunchcardAccessPacket(InstructionManager instructionManager) {
        this.instructionManager = instructionManager;
    }

    public PunchcardAccessPacket(FriendlyByteBuf buffer) {
        instructionManager = InstructionManager.fromTag(buffer.readNbt());
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeNbt(instructionManager.write(BL));
    }

    @Override
    public void handle(Supplier<NetworkEvent.Context> context) {
        context.get()
                .enqueueWork(() -> {

                });
        context.get()
                .setPacketHandled(true);
    }

    }
*/