package com.fallenreaper.createutilities.content.blocks.steam_furnace;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.Create;
import com.simibubi.create.content.contraptions.components.steam.SteamEngineBlock;
import com.simibubi.create.content.contraptions.fluids.tank.BoilerData;
import com.simibubi.create.content.contraptions.goggles.IHaveGoggleInformation;
import com.simibubi.create.foundation.block.BlockStressValues;
import com.simibubi.create.foundation.utility.Components;
import com.simibubi.create.foundation.utility.Iterate;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.animation.LerpedFloat;
import joptsimple.internal.Strings;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class SteamFurnaceBoilerData extends BoilerData {
    static final int SAMPLE_RATE = 5;

    private static final int waterSupplyPerLevel = 10;
    private static final float passiveEngineEfficiency = 2 / 8f;
    // Heat score
    public boolean needsHeatLevelUpdate;
    public boolean passiveHeat;
    public int activeHeat;
    public float waterSupply;
    public int attachedEngines;
    public SteamFurnaceBlockEntity te;
    public float steamAmount;
    public float steamLevel;
    public LerpedFloat gauge = LerpedFloat.linear();
    public int boilerWater;
    // Pooled water supply
    int gatheredSupply;
    float[] supplyOverTime = new float[10];
    int ticksUntilNextSample;
    int currentIndex;
    // Display
    private int maxHeatForWater = 0;
    private int maxHeatForSize = 0;
    private int minValue = 0;
    private int maxValue = 0;

    public SteamFurnaceBoilerData(SteamFurnaceBlockEntity te) {
        super();
        this.te = te;
    }

    public void tick() {
        if (!isActive())
            return;
        if (te.getLevel() == null)
            return;
        if (!te.hasSteam())
            return;

        if (te.getLevel().isClientSide) {
            gauge.tickChaser();
            float current = gauge.getValue(1);
            if (current > 1 && te.getLevel().getRandom().nextFloat() < 1 / 2f)
                gauge.setValueNoUpdate(current + Math.min(-(current - 1) * Create.RANDOM.nextFloat(), 0));
            return;
        }
        boilerWater = te.getTank().getFluidAmount();
        if (needsHeatLevelUpdate && updateTemperature())
            te.notifyUpdate();
        ticksUntilNextSample--;
        if (ticksUntilNextSample > 0)
            return;


        ticksUntilNextSample = SAMPLE_RATE;
        supplyOverTime[currentIndex] = gatheredSupply / (float) SAMPLE_RATE;
        waterSupply = Math.max(waterSupply, supplyOverTime[currentIndex]);
        currentIndex = (currentIndex + 1) % supplyOverTime.length;
        gatheredSupply = 0;


        steamAmount = te.getProducedSteam();
        steamLevel = Mth.clamp(te.getProducedSteam() / 128F, 0, 1);

        if (currentIndex == 0) {
            waterSupply = 0;
            for (float i : supplyOverTime)
                waterSupply = Math.max(i, waterSupply);
        }


        if (te.isUnlimitedFuel())
            waterSupply = waterSupplyPerLevel * 20;

        te.notifyUpdate();
    }

    @Override
    public int getTheoreticalHeatLevel() {
        return activeHeat;
    }

    @Override
    public int getMaxHeatLevelForWaterSupply() {
        float level = Mth.clamp(te.getProducedSteam() / 512F, 0, 1);
        return Math.min(18, Mth.ceil(waterSupply) / waterSupplyPerLevel);
    }

    @Override
    public boolean isPassive() {
        return passiveHeat && maxHeatForSize > 0 && maxHeatForWater > 0;
    }

    @Override
    public boolean isPassive(int boilerSize) {
        calcMinMaxForSize();
        return isPassive();
    }


    public float getEngineEfficiency() {

        // if (isPassive())
        //     return passiveEngineEfficiency / attachedEngines;
        /*
        if (activeHeat == 0)
            return 0;

         */
        float val = 0;

        if (!te.hasSteam())
            return 0;
        if (te.hasSteam()) {
            int level = Math.round(steamLevel * 16);

            if (level <= 4)
                val = 0;

            if (level <= 8 && level > 4)
                val = 0.25F;

            if (level <= 12 && level > 8)
                val = 0.75F;

            if (level <= 16 && level > 12)
                val = 1.0F;

        }


        int actualHeat = getActualHeat();
        return val / attachedEngines;
    }

    private int getActualHeat() {

        int forWaterSupply = getMaxHeatLevelForWaterSupply();
        int actualHeat = Math.min(activeHeat, forWaterSupply);
        return actualHeat;
    }

    public int getMaxHeatLevel() {
        return 6;
    }

    public void calcMinMaxForSize() {
        maxHeatForSize = getMaxHeatLevel();
        maxHeatForWater = getMaxHeatLevelForWaterSupply();

        minValue = Math.min(passiveHeat ? 1 : activeHeat, Math.min(maxHeatForWater, maxHeatForSize));
        maxValue = Math.max(passiveHeat ? 1 : activeHeat, Math.max(maxHeatForWater, maxHeatForSize));
    }

    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        if (!isActive())
            return false;

        Component indent = Component.literal(IHaveGoggleInformation.spacing);
        Component indent2 = Component.literal(IHaveGoggleInformation.spacing + " ");


        calcMinMaxForSize();

        tooltip.add(indent.plainCopy()
                .append(
                        Lang.translateDirect("boiler.status", getHeatLevelTextComponent().withStyle(ChatFormatting.GREEN))));

        tooltip.add(indent2.plainCopy()
                .append(getWaterComponent(true, false)));
        tooltip.add(indent2.plainCopy()
                .append(getHeatComponent(true, false)));

        if (attachedEngines == 0)
            return true;

        int boilerLevel = Math.min(activeHeat, maxHeatForWater);
        double totalSU = getEngineEfficiency() * 16 * Math.max(boilerLevel, attachedEngines)
                * BlockStressValues.getCapacity(AllBlocks.STEAM_ENGINE.get());

        tooltip.add(Component.empty());

        Lang.translate("tooltip.capacityProvided")
                .style(ChatFormatting.GRAY)
                .forGoggles(tooltip);

        Lang.number(totalSU)
                .translate("generic.unit.stress")
                .style(ChatFormatting.AQUA)
                .space()
                .add((attachedEngines == 1 ? Lang.translate("boiler.via_one_engine")
                        : Lang.translate("boiler.via_engines", attachedEngines)).style(ChatFormatting.DARK_GRAY))
                .forGoggles(tooltip, 1);

        if (isPlayerSneaking)
            Lang.number(steamAmount)
                    .translate("mb")
                    .style(ChatFormatting.GRAY)
                    .forGoggles(tooltip, 1);

        return true;
    }

    @Override
    @NotNull
    public MutableComponent getHeatLevelTextComponent() {
        int boilerLevel = Math.min(activeHeat, maxHeatForWater);
        int val = 0;
        int level = Math.round(steamLevel * 16);

        if (level <= 4)
            val = 0;

        if (level <= 8 && level > 4)
            val = 1;

        if (level <= 12 && level > 8)
            val = 2;

        if (level <= 16 && level > 12)
            val = 3;

        return isPassive() ? Lang.translateDirect("boiler.passive")
                : (val == 0 ? Lang.translateDirect("boiler.idle")
                : val == 3 ? Lang.translateDirect("boiler.max_lvl")
                : Lang.translateDirect("boiler.lvl", String.valueOf(val)));
    }

    @Override
    public MutableComponent getWaterComponent(boolean forGoggles, boolean useBlocksAsBars, ChatFormatting... styles) {
        return componentHelper("water", maxHeatForWater, forGoggles, useBlocksAsBars, styles);
    }

    @Override
    public MutableComponent getHeatComponent(boolean forGoggles, boolean useBlocksAsBars, ChatFormatting... styles) {
        return componentHelper("heat", passiveHeat ? 1 : activeHeat, forGoggles, useBlocksAsBars, styles);
    }

    private MutableComponent componentHelper(String label, int level, boolean forGoggles, boolean useBlocksAsBars,
                                             ChatFormatting... styles) {
        MutableComponent base = useBlocksAsBars ? blockComponent(level) : barComponent(level);

        if (!forGoggles)
            return base;

        ChatFormatting style1 = styles.length >= 1 ? styles[0] : ChatFormatting.GRAY;
        ChatFormatting style2 = styles.length >= 2 ? styles[1] : ChatFormatting.DARK_GRAY;

        return Lang.translateDirect("boiler." + label)
                .withStyle(style1)
                .append(Lang.translateDirect("boiler." + label + "_dots")
                        .withStyle(style2))
                .append(base);
    }

    private MutableComponent blockComponent(int level) {
        return Component.literal(
                "" + "\u2588".repeat(minValue) + "\u2592".repeat(level - minValue) + "\u2591".repeat(maxValue - level));
    }

    private MutableComponent barComponent(int level) {
        return Components.empty().copy()
                .append(bars(Math.max(0, minValue - 1), ChatFormatting.DARK_GREEN))
                .append(bars(minValue > 0 ? 1 : 0, ChatFormatting.GREEN))
                .append(bars(Math.max(0, level - minValue), ChatFormatting.DARK_GREEN))
                .append(bars(Math.max(0, maxValue - level), ChatFormatting.DARK_RED))
                .append(bars(Math.max(0, Math.min(18 - maxValue, ((maxValue / 5 + 1) * 5) - maxValue)),
                        ChatFormatting.DARK_GRAY));

    }

    private MutableComponent bars(int level, ChatFormatting format) {
        return Component.literal(Strings.repeat('|', level)).withStyle(format);
    }

    public boolean evaluate() {
        BlockPos controllerPos = te.getBlockPos();


        Level level = te.getLevel();
        int prevEngines = attachedEngines;
        attachedEngines = 0;
        attachedWhistles = 0;

        for (Direction direction : Iterate.directions) {

            BlockPos pos = controllerPos.relative(direction);


            BlockState attachedState = level.getBlockState(pos);
            if (AllBlocks.STEAM_ENGINE.has(attachedState) && SteamEngineBlock.getFacing(attachedState) == direction)
                attachedEngines++;
                        /*
                        if (AllBlocks.STEAM_WHISTLE.has(attachedState)
                                && WhistleBlock.getAttachedDirection(attachedState)
                                .getOpposite() == direction)
                            attachedWhistles++;

                         */
        }
        needsHeatLevelUpdate = true;
        return prevEngines != attachedEngines;
    }


    public boolean updateTemperature() {
        BlockPos pos = te.getBlockPos();
        Level level = te.getLevel();
        needsHeatLevelUpdate = false;

        boolean prevPassive = passiveHeat;
        int prevActive = activeHeat;
        passiveHeat = false;
        activeHeat = 0;

        BlockState blockState = level.getBlockState(pos);
        float heat = steamLevel;
        if (heat == 0) {
            passiveHeat = true;
        } else if (heat > 0) {
            activeHeat += heat;
        }

        passiveHeat &= activeHeat == 0;

        return prevActive != activeHeat || prevPassive != passiveHeat;
    }

    @Override
    public boolean isActive() {
        return attachedEngines > 0;
    }

    public void addHeat() {
        activeHeat += te.producedSteam / 16.0F;
    }

    @Override
    public void clear() {
        waterSupply = 0;
        activeHeat = 0;
        passiveHeat = false;
        attachedEngines = 0;
        steamLevel = 0;
        steamAmount = 0;
        Arrays.fill(supplyOverTime, 0);
    }

    @Override
    public CompoundTag write() {
        CompoundTag nbt = new CompoundTag();
        nbt.putFloat("Supply", waterSupply);
        nbt.putInt("ActiveHeat", activeHeat);
        nbt.putBoolean("PassiveHeat", passiveHeat);
        nbt.putInt("Engines", attachedEngines);
        nbt.putBoolean("Update", needsHeatLevelUpdate);
        nbt.putFloat("ActiveSteamAmount", steamAmount);
        nbt.putFloat("SteamLevel", steamLevel);
        nbt.putInt("BoilerWaterLevel", boilerWater);
        return nbt;
    }

    @Override
    public void read(CompoundTag nbt, int boilerSize) {
        waterSupply = nbt.getFloat("Supply");
        activeHeat = nbt.getInt("ActiveHeat");
        passiveHeat = nbt.getBoolean("PassiveHeat");
        attachedEngines = nbt.getInt("Engines");
        needsHeatLevelUpdate = nbt.getBoolean("Update");
        steamAmount = nbt.getFloat("ActiveSteamAmount");
        steamLevel = nbt.getFloat("SteamLevel");
        boilerWater = nbt.getInt("BoilerWaterLevel");
        Arrays.fill(supplyOverTime, (int) waterSupply);

        int forBoilerSize = getMaxHeatLevelForBoilerSize(boilerSize);
        int forWaterSupply = getMaxHeatLevelForWaterSupply();
        int actualHeat = Math.min(activeHeat, Math.min(forWaterSupply, forBoilerSize));
        float target = isPassive(boilerSize) ? 1 / 8f : forBoilerSize == 0 ? 0 : actualHeat / (forBoilerSize * 1f);
        gauge.chase(target, 0.125f, LerpedFloat.Chaser.EXP);
    }
}
