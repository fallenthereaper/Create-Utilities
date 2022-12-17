package com.fallenreaper.createutilities.core.data;

import net.minecraft.world.entity.player.Player;



/**
 * Implementing this will cancel the default sweep particle and use a custom one instead.
 */
public interface ISweepParticleProvider {
    /**
     * @param player player
     * @return True if the default sweep particle should be skipped and use a custom one instead.
     */
    boolean onSweepAttack(Player player);
}
