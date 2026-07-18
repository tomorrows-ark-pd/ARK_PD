package com.shatteredpixel.shatteredpixeldungeon.journal.quests;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.QuestCargo;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.Image;

/**
 * The Rhodes onboarding questline. Fixed order, repeatable per run, run-level only.
 *
 * <pre>
 * step 0 - talk to Closure in the shop area          reward: 25 gold
 * step 1 - talk to Purestream (upper-right)          reward: 25 gold
 * step 2 - talk to the reserve Guard operator        reward: none (hands out cargo)
 * step 3 - deliver the cargo to the floor-6 drone    reward: 50 gold
 * </pre>
 *
 * All steps advance from the relevant NPC's {@code interact()}; no event-hook overrides needed.
 */
public class TutorialQuestLine extends QuestLine {

	@Override
	public Image icon() {
		return new ItemSprite(ItemSpriteSheet.QUEST_SCROLL);
	}

	@Override
	protected int stepCount() {
		return 4;
	}

	@Override
	protected Item stepReward(int step) {
		switch (step) {
			case 0: return new Gold(25);
			case 1: return new Gold(25);
			case 3: return new Gold(50);
			default: return null;
		}
	}

	@Override
	protected void onAbandoned() {
		//remove the delivery cargo; one left on a floor heap is an inert dud (see its desc)
		if (Dungeon.hero != null) {
			QuestCargo cargo = Dungeon.hero.belongings.getItem(QuestCargo.class);
			if (cargo != null) {
				cargo.detachAll(Dungeon.hero.belongings.backpack);
			}
		}
	}
}
