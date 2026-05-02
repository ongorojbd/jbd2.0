/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2026 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.shatteredpixel.shatteredpixeldungeon.windows;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.InvokerEnergy;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;

/**
 * 원소술사 조합 스킬 목록을 보여주는 정보 창.
 * 클릭으로 스킬을 발동하지 않으며, 조합 목록을 읽는 용도로만 사용.
 */
public class WndInvokerSpells extends Window {

    private static final int WIDTH_P = 130;
    private static final int WIDTH_L = 190;
    private static final int MARGIN  = 3;

    public WndInvokerSpells(InvokerEnergy buff) {
        super();

        int width = PixelScene.landscape() ? WIDTH_L : WIDTH_P;
        float pos = MARGIN;

        // ── 제목
        RenderedTextBlock title = PixelScene.renderTextBlock("조합 목록", 9);
        title.hardlight(TITLE_COLOR);
        title.maxWidth(width - MARGIN * 2);
        title.setPos((width - title.width()) / 2f, pos);
        add(title);
        pos = title.bottom() + MARGIN * 2;

        // ── 현재 상태 (에너지 / 충전)
        String stateText =
                "_에너지:_ " + (int) buff.energy + " / " + buff.energyCap() +
                "    _파츠:_ Q:" + buff.quasCharges +
                " W:" + buff.wexCharges +
                " E:" + buff.exortCharges;

        RenderedTextBlock stateBlock = PixelScene.renderTextBlock(stateText, 7);
        stateBlock.maxWidth(width - MARGIN * 2);
        stateBlock.setPos(MARGIN, pos);
        add(stateBlock);
        pos = stateBlock.bottom() + MARGIN * 2;

        // ── 구분선 대용 (얇은 텍스트 줄)
//        RenderedTextBlock sep = PixelScene.renderTextBlock("─────────────────────", 6);
//        sep.maxWidth(width - MARGIN * 2);
//        sep.setPos(MARGIN, pos);
//        add(sep);
//        pos = sep.bottom() + MARGIN;

        // ── 스킬 목록
        for (InvokerEnergy.InvokerSpell spell : InvokerEnergy.SPELLS) {
            boolean canAfford = buff.energy >= spell.cost;
            boolean isBlocked = buff.isSpellBlocked(spell);
            boolean available = canAfford && !isBlocked;

            String costOrBlock = isBlocked
                    ? "_(재사용 대기: " + buff.spellBlockRemaining(spell) + "번)_"
                    : "";
            String header = "_[" + spell.comboLabel() + "]_  " + spell.name + "  " + costOrBlock;

            RenderedTextBlock headerBlock = PixelScene.renderTextBlock(header, 7);
            if (!available) headerBlock.hardlight(0x888888);
            headerBlock.maxWidth(width - MARGIN * 2);
            headerBlock.setPos(MARGIN, pos);
            add(headerBlock);
            pos = headerBlock.bottom() + 1;

            RenderedTextBlock descBlock = PixelScene.renderTextBlock("  " + spell.descFor(Dungeon.hero), 6);
            if (!available) descBlock.hardlight(0x666666);
            descBlock.maxWidth(width - MARGIN * 2);
            descBlock.setPos(MARGIN, pos);
            add(descBlock);
            pos = descBlock.bottom() + MARGIN;
        }

        resize(width, (int) pos + MARGIN);
    }
}
