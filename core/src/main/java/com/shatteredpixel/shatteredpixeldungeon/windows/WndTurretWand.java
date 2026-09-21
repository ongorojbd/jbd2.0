package com.shatteredpixel.shatteredpixeldungeon.windows;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.levels.HospitalLevel;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.ItemButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.noosa.audio.Sample;

import java.util.ArrayList;

/**
 * Picks the wand a freshly installed HospitalLevel turret will fire. Laid out like Spw's reward
 * window: a title bar, a line of prompt text, and one item button per choice, so the wands are
 * shown by their real sprites and can be inspected before being committed to.
 */
public class WndTurretWand extends Window {

	private static final int WIDTH = 120;
	private static final int BTN_SIZE = 32;
	private static final int BTN_GAP = 5;
	private static final int GAP = 2;

	//receives the wand once it is confirmed. Runs on the render thread, so it should only record
	//the choice - anything that touches the level is left for the actor thread to pick up.
	public interface Listener {
		void onChosen(Wand wand);
	}

	private final Listener listener;

	public WndTurretWand(ArrayList<Wand> choices, Listener listener) {
		this.listener = listener;

		IconTitle titlebar = new IconTitle();
		titlebar.icon(new ItemSprite(ItemSpriteSheet.WAND_MAGIC_MISSILE));
		titlebar.label(Messages.get(HospitalLevel.class, "turret_title"));
		titlebar.setRect(0, 0, WIDTH, 0);
		add(titlebar);

		RenderedTextBlock message = PixelScene.renderTextBlock(
				Messages.get(HospitalLevel.class, "turret_prompt"), 6);
		message.maxWidth(WIDTH);
		message.setPos(0, titlebar.bottom() + GAP);
		add(message);

		for (int i = 0; i < choices.size(); i++) {
			final Wand wand = choices.get(i);
			ItemButton btn = new ItemButton() {
				@Override
				protected void onClick() {
					ShatteredPixelDungeon.scene().addToFront(new WndTurretWandInfo(wand));
				}
			};
			btn.item(wand);
			//the mounted wand has no charges and its level is fixed, so the slot shows the
			//sprite alone rather than a "+5" and a charge count that mean nothing here
			btn.slot().textVisible(false);
			btn.setRect(
					(i + 1) * (WIDTH - BTN_GAP) / choices.size() - BTN_SIZE,
					message.top() + message.height() + BTN_GAP,
					BTN_SIZE,
					BTN_SIZE);
			add(btn);
		}

		resize(WIDTH, (int) (message.top() + message.height() + 2 * BTN_GAP + BTN_SIZE));
	}

	//a turret is owed either way, so there is no walking away without picking its wand
	@Override
	public void onBackPressed() {
	}

	private class WndTurretWandInfo extends WndInfoItem {

		WndTurretWandInfo(final Item wand) {
			super(wand);

			RedButton btnConfirm = new RedButton(Messages.get(WndTurretWand.class, "confirm")) {
				@Override
				protected void onClick() {
					WndTurretWandInfo.this.hide();
					WndTurretWand.this.hide();
					listener.onChosen((Wand) wand);
					Sample.INSTANCE.play(Assets.Sounds.TELEPORT);
				}
			};
			btnConfirm.setRect(0, height + 2, width / 2 - 1, 16);
			add(btnConfirm);

			RedButton btnCancel = new RedButton(Messages.get(WndTurretWand.class, "cancel")) {
				@Override
				protected void onClick() {
					hide();
				}
			};
			btnCancel.setRect(btnConfirm.right() + 2, height + 2, btnConfirm.width(), 16);
			add(btnCancel);

			resize(width, (int) btnCancel.bottom());
		}
	}
}
