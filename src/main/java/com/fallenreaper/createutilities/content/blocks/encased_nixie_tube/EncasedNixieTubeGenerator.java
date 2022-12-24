package com.fallenreaper.createutilities.content.blocks.encased_nixie_tube;

import com.simibubi.create.content.logistics.block.redstone.DoubleFaceAttachedBlock;
import com.simibubi.create.foundation.data.SpecialBlockStateGen;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.generators.ModelFile;

public class EncasedNixieTubeGenerator extends SpecialBlockStateGen  {


        @Override
        protected int getXRotation(BlockState state) {
            return state.getValue(EncasedNixieTubeBlock.FACE)
                    .xRot();
        }

        @Override
        protected int getYRotation(BlockState state) {
            DoubleFaceAttachedBlock.DoubleAttachFace face = state.getValue(EncasedNixieTubeBlock.FACE);
            return horizontalAngle(state.getValue(EncasedNixieTubeBlock.FACING))
                    + (face == DoubleFaceAttachedBlock.DoubleAttachFace.WALL || face == DoubleFaceAttachedBlock.DoubleAttachFace.WALL_REVERSED ? 180 : 0);
        }

        @Override
        public <T extends Block> ModelFile getModel(DataGenContext<Block, T> ctx, RegistrateBlockstateProvider prov,
                                                    BlockState state) {
            return prov.models()
                    .withExistingParent(ctx.getName(), prov.modLoc("block/encased_nixie_tube/block"));
        }


}
