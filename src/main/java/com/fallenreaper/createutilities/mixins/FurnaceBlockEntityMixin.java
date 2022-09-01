package com.fallenreaper.createutilities.mixins;

import com.fallenreaper.createutilities.utils.IFurnaceBurnTimeAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.RecipeHolder;
import net.minecraft.world.inventory.StackedContentsCompatible;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(AbstractFurnaceBlockEntity.class)
public abstract class FurnaceBlockEntityMixin extends BaseContainerBlockEntity implements WorldlyContainer, RecipeHolder, StackedContentsCompatible, IFurnaceBurnTimeAccessor {
    @Final
    @Shadow
    protected ContainerData dataAccess;
    @Shadow
    int litTime;
    @Shadow
    int litDuration;


    protected FurnaceBlockEntityMixin(BlockEntityType<?> p_155076_, BlockPos p_155077_, BlockState p_155078_) {
        super(p_155076_, p_155077_, p_155078_);

    }

    @Override
    public int getBurnTime() {
        return this.litDuration;
    }

    @Override
    public void setBurnTime(int burnTime) {
        this.litTime = burnTime;
    }
}
