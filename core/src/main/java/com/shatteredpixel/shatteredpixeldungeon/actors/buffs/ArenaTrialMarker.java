package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

/*
 * Marker buff used to indicate Arena trial effects are active this floor.
 * Allows easy cleanup on floor transition/unseal.
 */
public class ArenaTrialMarker extends Buff {

    {
        // no UI, no announce
        announced = false;
    }
}







