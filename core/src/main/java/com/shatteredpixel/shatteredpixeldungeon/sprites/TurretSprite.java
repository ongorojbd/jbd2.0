package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.watabou.noosa.Game;
import com.watabou.noosa.TextureFilm;

//구급차 터렛(AmbulanceTurret)용 스프라이트. 센트리처럼 공중에 떠 있으며,
//발사체 자체는 터렛이 들고 있는 완드에서 나가므로 여기서는 발사 연출만 처리한다
public class TurretSprite extends MobSprite {

	public TurretSprite(){
		super();

		texture( Assets.Sprites.PAISLEY );
		TextureFilm frames = new TextureFilm( texture, 11, 15 );

		idle = new Animation( 2, true );
		idle.frames( frames, 0 );

		run = idle.clone();
		attack = idle.clone();
		die = idle.clone();
		zap = idle.clone();

		play( idle );
	}

	@Override
	public void zap( int pos ) {
		if (ch != null) turnTo( ch.pos, pos );
		idle();
		flash();
		emitter().burst(MagicMissile.WardParticle.UP, 2);
	}

	private float baseY = Float.NaN;

	@Override
	public void place(int cell) {
		super.place(cell);
		baseY = y;
	}

	@Override
	public void update() {
		super.update();

		//공중에 뜬 채 위아래로 흔들린다
		if (!paused){
			if (Float.isNaN(baseY)) baseY = y;
			y = baseY + (float) Math.sin(Game.timeTotal);
			shadowOffset = 0.25f - 0.8f*(float) Math.sin(Game.timeTotal);
		}
	}

	@Override
	public int blood() {
		return 0xFFCC33FF;
	}
}
