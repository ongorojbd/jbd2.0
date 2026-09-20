package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;

//WardSprite's own zap() casts its owner to WandOfWarding.Ward, so the ambulance turrets
//(see AmbulanceTurret) need their own. linkVisuals() already falls back to tier 5 for
//non-Ward owners, so only the muzzle flash has to be replaced - the projectile itself is
//fired by the turret, from the wand it carries.
public class TurretSprite extends WardSprite {

	@Override
	public void zap(int pos) {
		idle();
		flash();
		emitter().burst(MagicMissile.WardParticle.UP, 2);
	}
}
