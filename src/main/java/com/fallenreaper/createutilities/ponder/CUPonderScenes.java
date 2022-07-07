package com.fallenreaper.createutilities.ponder;

import com.simibubi.create.foundation.ponder.SceneBuilder;
import com.simibubi.create.foundation.ponder.SceneBuildingUtil;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;


public class CUPonderScenes {

        public static void sprinkler(SceneBuilder scene, SceneBuildingUtil util) {
            scene.title("sprinkler", "Hydrating Farmland areas using a sprinkler.");
            scene.configureBasePlate(0, 0, 7);

            scene.world.showSection(util.select.layer(0), Direction.UP);

            BlockPos sprinklerPos = util.grid.at(3, 1, 2);

    }
}
