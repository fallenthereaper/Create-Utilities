package com.fallenreaper.createutilities.mixins;

import com.simibubi.create.content.contraptions.goggles.IHaveGoggleInformation;
import com.simibubi.create.content.contraptions.processing.burner.BlazeBurnerTileEntity;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.LangBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

import static com.fallenreaper.createutilities.core.utils.MiscUtil.formatTime;

@Mixin(BlazeBurnerTileEntity.class)
public abstract class BlazeBurnerTileEntityMixin implements IHaveGoggleInformation {

    @Shadow protected int remainingBurnTime;

    @Shadow protected BlazeBurnerTileEntity.FuelType activeFuel;

    @Shadow public abstract boolean isCreative();

    @Shadow public abstract int getRemainingBurnTime();

    @Shadow protected abstract boolean tryUpdateFuel(ItemStack itemStack, boolean forceOverflow, boolean simulate);

    @Shadow protected boolean isCreative;

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        int burnTime = this.remainingBurnTime;

        Component indent = Component.literal(" ");
        Component indent1 = Component.literal(spacing + " ");
        Component arrow = Component.literal("->").withStyle(ChatFormatting.DARK_GRAY);
        Component time =   Component.literal(formatTime(burnTime)).withStyle(ChatFormatting.GOLD) ;
      //  String item = ! ? "Empty" : fuelIn.getItem().getName(fuelIn).getString();
       // Component in = Component.literal(item + " " + (fuelIn.isEmpty() ? "" : "x") + (fuelIn.getCount() <= 0 ? "" : fuelIn.getCount())).withStyle(fuelIn.isEmpty() ? ChatFormatting.RED : ChatFormatting.GREEN);
        LangBuilder mb = Lang.translate("generic.unit.millibuckets");

        MutableComponent firstLine = arrow.plainCopy().append(indent);

        Component unlimited;
        unlimited = Component.literal("Unlimited").withStyle(ChatFormatting.LIGHT_PURPLE);

        tooltip.add(indent1.plainCopy()
                .append("Blaze Burner:"));

        tooltip.add(indent1.plainCopy().append("Burn Time:").withStyle(ChatFormatting.GRAY).append(indent).append(this.isCreative ? unlimited : time));


        return true;
    }
}
