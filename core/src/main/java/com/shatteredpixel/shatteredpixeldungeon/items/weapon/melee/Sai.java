package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.utils.Random;

public class Sai extends MeleeWeapon {
	{
		image = ItemSpriteSheet.SAI;
		hitSound = Assets.Sounds.HIT_SLASH;
		hitSoundPitch = 1.3f;

		tier = 3;
		DLY = 0.5f; //2x speed
	}

	@Override
	public int max(int lvl) {
		return  Math.round(2.5f*(tier+1)) +     //10 base, down from 20
				lvl*Math.round(0.5f*(tier+1));  //+2 per level, down from +4
	}

	@Override
	public int proc(Char attacker, Char defender, int damage) {
		int thisHP = attacker.HP;
		int thisHT = attacker.HT;
		if (thisHP <= thisHT/4) {
			damage *= 1.5f;
		}
		else if (thisHP <= thisHT / 2){
			damage *= 1.3f;

		}
		else if (thisHP <= thisHT - thisHT/4) {
			damage *= 1.1f;
		}
		return super.proc(attacker, defender, damage);
	}
}

