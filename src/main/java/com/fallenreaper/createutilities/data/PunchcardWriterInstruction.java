package com.fallenreaper.createutilities.data;

import com.fallenreaper.createutilities.CreateUtilities;
import com.fallenreaper.createutilities.index.GuiTextures;
import com.simibubi.create.foundation.utility.Lang;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TextComponent;

public class PunchcardWriterInstruction {
    protected InstructionType type;
    protected TextComponent description;
    protected GuiTextures texture;
    protected CompoundTag data;

    public PunchcardWriterInstruction(InstructionType type, GuiTextures textures, String name) {
        data = new CompoundTag();
        this.type = type;
        this.texture = textures;
      addDescription(name);
    }
    private void addDescription(String name) {
        data.putString("DescriptionText", "punchcardwriter.instruction." + type.name().toLowerCase() + "." + name.toLowerCase());
        data.putString("InstructionType", type.name());
    }

   protected InstructionType getInstructionType() {
        return type;
    }

    public String getDescription() {
        return Lang.builder(CreateUtilities.ID).translate(data.getString("DescriptionText")).string();
    }

    public GuiTextures getTexture() {
        return texture;
    }
}
