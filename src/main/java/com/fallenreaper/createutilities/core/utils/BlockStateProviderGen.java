package com.fallenreaper.createutilities.core.utils;

import com.fallenreaper.createutilities.CreateUtilities;
import com.fallenreaper.createutilities.index.CUBlocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.common.data.ExistingFileHelper;

public class BlockStateProviderGen extends BlockStateProvider {
    public BlockStateProviderGen(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, CreateUtilities.ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
       ceilingHorizontalAxisBlock(CUBlocks.SPRINKLER.get());
    }

    public void ceilingHorizontalAxisBlock(Block block) {
        this.getVariantBuilder(block) // Get variant builder
                .forAllStates(state -> // For all possible states
                        ConfiguredModel.builder() // Creates configured model builder
                                .rotationY(((int) state.getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot() + 180) % 360)
                                .build()
                        // Creates the array of configured models
                );

    }
}
