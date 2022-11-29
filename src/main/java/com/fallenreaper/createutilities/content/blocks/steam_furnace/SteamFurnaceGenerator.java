package com.fallenreaper.createutilities.content.blocks.steam_furnace;

import com.simibubi.create.foundation.data.SpecialBlockStateGen;
import com.simibubi.create.repack.registrate.providers.DataGenContext;
import com.simibubi.create.repack.registrate.providers.RegistrateBlockstateProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.generators.ModelFile;

import static com.fallenreaper.createutilities.content.blocks.steam_furnace.SteamFurnaceBlock.CREATIVE_LIT;
import static net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING;
import static net.minecraft.world.level.block.state.properties.BlockStateProperties.LIT;

public class SteamFurnaceGenerator extends SpecialBlockStateGen {
    @Override
    protected int getXRotation(BlockState state) {
        return 0;
    }

    @Override
    protected int getYRotation(BlockState state) {
        Direction direction = state.getValue(HORIZONTAL_FACING);
        return horizontalAngle(direction);
    }

    @Override
    public <T extends Block> ModelFile getModel(DataGenContext<Block, T> ctx, RegistrateBlockstateProvider prov, BlockState blockState) {
        String variant = null;
        if(blockState.getValue(LIT))
            variant = "lit";
        if(blockState.getValue(CREATIVE_LIT))
            variant = "creative_lit";

       	return prov.models().getExistingFile(prov.modLoc("block/" + ctx.getName() + "/" + variant));
    }
}
