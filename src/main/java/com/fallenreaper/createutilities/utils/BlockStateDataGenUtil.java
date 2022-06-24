package com.fallenreaper.createutilities.utils;

import com.simibubi.create.content.contraptions.base.DirectionalAxisKineticBlock;
import com.simibubi.create.foundation.data.BlockStateGen;
import com.simibubi.create.repack.registrate.providers.DataGenContext;
import com.simibubi.create.repack.registrate.providers.RegistrateBlockstateProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;

import java.util.function.Function;

public class BlockStateDataGenUtil {
    public class BlockStateUtils {

        public static <T extends DirectionalAxisKineticBlock> void directionalPoweredAxisBlockstate(DataGenContext<Block, T> ctx, RegistrateBlockstateProvider prov) {
            BlockStateGen.directionalAxisBlock(ctx, prov, (blockState, vertical) -> prov.models()
                    .getExistingFile(prov.modLoc("block/" + ctx.getName() + "/" + (vertical ? "vertical" : "horizontal") + (blockState.getValue(BlockStateProperties.POWERED) ? "_powered" : ""))));
        }
        public static <T extends Block> void facingPoweredAxisBlockstate(DataGenContext<Block, T> ctx, RegistrateBlockstateProvider prov) {
            prov.directionalBlock(ctx.getEntry(),
                    blockState -> prov.models().getExistingFile(
                            prov.modLoc("block/"+ ctx.getName() +"/block"+ (blockState.getValue(BlockStateProperties.POWERED) ? "_powered" : ""))
                    )
            );
        }

        public static <T extends Block> void horizontalFacingLitBlockstate(DataGenContext<Block, T> ctx, RegistrateBlockstateProvider prov) {
            prov.horizontalBlock(ctx.get(), blockState -> prov.models()
                    .getExistingFile(prov.modLoc("block/" + ctx.getName() + "/block" + (blockState.getValue(AbstractFurnaceBlock.LIT) ? "_lit" : ""))));
        }

        public static <T extends Block> void torsionSpringBlockstate(DataGenContext<Block, T> ctx, RegistrateBlockstateProvider prov,
                                                                     Function<BlockState, ModelFile> modelFunc, boolean uvLock) {
            prov.getVariantBuilder(ctx.getEntry())
                    .forAllStatesExcept(state -> {
                        Direction.Axis axis = state.getValue(BlockStateProperties.AXIS);
                        return ConfiguredModel.builder()
                                .modelFile(modelFunc.apply(state))
                                .uvLock(uvLock)
                                .rotationX(axis == Direction.Axis.Y ? 90 : 0)
                                .rotationY(axis == Direction.Axis.X ? 90 : 0)
                                .build();
                    }, BlockStateProperties.WATERLOGGED);
        }

        public static <T extends Block> void facingBlockstate(DataGenContext<Block, T> ctx, RegistrateBlockstateProvider prov,String modelPath) {
            prov.directionalBlock(ctx.getEntry(),
                    blockState -> prov.models().getExistingFile(
                            prov.modLoc(modelPath)
                    )
            );
        }
    }

}
