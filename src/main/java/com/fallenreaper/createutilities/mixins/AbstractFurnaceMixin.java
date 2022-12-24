package com.fallenreaper.createutilities.mixins;

import com.simibubi.create.content.contraptions.goggles.IHaveGoggleInformation;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(AbstractFurnaceBlockEntity.class)
public abstract class AbstractFurnaceMixin implements IHaveGoggleInformation {

    @Shadow
    int litTime;

    @Shadow
    int litDuration;

//todo: remove this and just use the bellow to display info
    @Deprecated
    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        /*
        int burnTime = this.litTime;
        AbstractFurnaceBlockEntity te = (AbstractFurnaceBlockEntity) (Object) this;
        Component indent = Component.literal(" ");
        Component indent1 = Component.literal(spacing + " ");
        Component arrow = Component.literal("->").withStyle(ChatFormatting.DARK_GRAY);
        Component time =   Component.literal(formatTime(burnTime)).withStyle(ChatFormatting.GOLD) ;

        ItemStack fuelIn = te.getItem(1);
        String item = fuelIn.isEmpty() ? "Empty" : fuelIn.getItem().getName(fuelIn).getString();
        Component in = Component.literal(item + " " + (fuelIn.isEmpty() ? "" : "x") + (fuelIn.getCount() <= 0 ? "" : fuelIn.getCount())).withStyle(fuelIn.isEmpty() ? ChatFormatting.RED : ChatFormatting.GREEN);
        MutableComponent firstLine = arrow.plainCopy().append(indent);

        Component unlimited;
        unlimited = Component.literal("Unlimited").withStyle(ChatFormatting.LIGHT_PURPLE);
        String name = te.getDisplayName().getString();
        tooltip.add(indent1.plainCopy()
                .append(name + ":"));
        tooltip.add(firstLine.plainCopy().append(" Fuel:").withStyle(ChatFormatting.GRAY).append(indent).append(in));
        tooltip.add(indent1.plainCopy().append("Burn Time:").withStyle(ChatFormatting.GRAY).append(indent).append(this.litDuration >= 200000 ? unlimited : time));


        return true;

         */
        return false;
    }
}
