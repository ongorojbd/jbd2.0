package com.shatteredpixel.shatteredpixeldungeon.items.quest;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.JolyneStoneFree;
import com.shatteredpixel.shatteredpixeldungeon.effects.SpellSprite;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;

import java.util.ArrayList;

//동료로 합류한 죠린(JolyneStoneFree)에게 지시를 내리는 아이템
public class StoneFreeString extends Item {

	public static final String AC_ORDER = "ORDER";

	{
		image = ItemSpriteSheet.MAP0;

		stackable = false;

		defaultAction = AC_ORDER;

		unique = true;
	}

	@Override
	public ArrayList<String> actions(Hero hero) {
		ArrayList<String> actions = super.actions(hero);
		actions.add( AC_ORDER );
		return actions;
	}

	@Override
	public void execute(Hero hero, String action) {
		super.execute(hero, action);
		if (action.equals(AC_ORDER)) {
			if (findJolyne() == null) {
				GLog.w(Messages.get(this, "no_jolyne"));
				return;
			}
			GameScene.selectCell(director);
		}
	}

	protected static CellSelector.Listener director = new CellSelector.Listener() {

		@Override
		public void onSelect( Integer target ) {
			if (target == null) return;

			JolyneStoneFree ally = findJolyne();
			if (ally == null) {
				GLog.w(Messages.get(StoneFreeString.class, "no_jolyne"));
				return;
			}

			SpellSprite.show( curUser, SpellSprite.MAP );
			hero.sprite.operate(hero.pos);

			ally.directTocell(target);
		}

		@Override
		public String prompt() {
			return Messages.get(StoneFreeString.class, "prompt");
		}
	};

	private static JolyneStoneFree findJolyne(){
		for (Char ch : Actor.chars()){
			if (ch instanceof JolyneStoneFree){
				return (JolyneStoneFree) ch;
			}
		}
		return null;
	}

	@Override
	public boolean isUpgradable() {
		return false;
	}

	@Override
	public boolean isIdentified() {
		return true;
	}
}
