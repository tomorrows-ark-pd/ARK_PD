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

import com.shatteredpixel.shatteredpixeldungeon.journal.quests.QuestLine;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;

/**
 * Journal detail window for a single {@link QuestLine}. Shown when a quest entry is clicked
 * in the Notes tab. All logic lives on the QuestLine; this window is dumb.
 */
public class WndQuestJournal extends Window {

	private static final int WIDTH = 120;
	private static final int GAP   = 2;
	private static final int BTN_H = 16;

	public WndQuestJournal(final QuestLine quest, final Runnable onChange) {
		super();

		IconTitle titlebar = new IconTitle();
		titlebar.icon(quest.icon());
		titlebar.label(quest.name());
		titlebar.setRect(0, 0, WIDTH, 0);
		add(titlebar);

		String bodyText = quest.objectiveDesc();
		if (quest.progressText() != null) {
			bodyText += "\n\n" + Messages.get(this, "progress", quest.progressText());
		}
		RenderedTextBlock body = PixelScene.renderTextBlock(bodyText, 6);
		body.maxWidth(WIDTH);
		body.setPos(0, titlebar.bottom() + GAP);
		add(body);

		float pos = body.bottom() + GAP + 1;

		if (quest.claimable()) {
			RedButton btnClaim = new RedButton(Messages.get(this, "claim")) {
				@Override
				protected void onClick() {
					quest.claim();
					hide();
					if (onChange != null) onChange.run();
				}
			};
			btnClaim.setRect(0, pos, WIDTH, BTN_H);
			add(btnClaim);
			pos = btnClaim.bottom() + GAP;
		}

		RedButton btnAbandon = new RedButton(Messages.get(this, "abandon")) {
			@Override
			protected void onClick() {
				GameScene.show(new WndOptions(
						Messages.get(WndQuestJournal.class, "abandon"),
						Messages.get(WndQuestJournal.class, "confirm_abandon"),
						Messages.get(WndQuestJournal.class, "yes"),
						Messages.get(WndQuestJournal.class, "no")
				) {
					@Override
					protected void onSelect(int index) {
						if (index == 0) {
							quest.abandon();
							WndQuestJournal.this.hide();
							if (onChange != null) onChange.run();
						}
					}
				});
			}
		};
		btnAbandon.setRect(0, pos, WIDTH, BTN_H);
		add(btnAbandon);

		resize(WIDTH, (int) btnAbandon.bottom());
	}
}
