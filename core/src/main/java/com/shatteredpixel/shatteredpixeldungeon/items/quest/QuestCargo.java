package com.shatteredpixel.shatteredpixeldungeon.items.quest;

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.journal.quests.Quests;
import com.shatteredpixel.shatteredpixeldungeon.journal.quests.TutorialQuestLine;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

/**
 * Cargo handed to the hero by NPC_Guard during tutorial quest 1.
 * Delivered to the DeliveryDrone in a floor-6 shop; only exists during that quest.
 */
import java.util.ArrayList;

public class QuestCargo extends Item {
	{
		image = ItemSpriteSheet.CHEST; // placeholder sprite
		unique = true;
		stackable = false;
		cursed = false;
	}

	@Override
	public boolean isIdentified() {
		return true;
	}

	@Override
	public boolean isUpgradable() {
		return false;
	}

	//locked in the pack only while it is being delivered: a dropped parcel let the Guard hand out another every time.
	//outside that step any cargo is a stray (old saves included) and can be thrown away like any junk
	@Override
	public ArrayList<String> actions(Hero hero) {
		ArrayList<String> actions = super.actions(hero);
		TutorialQuestLine q = Quests.get(TutorialQuestLine.class);
		if (q != null && q.at(3)) {
			actions.remove(AC_DROP);
			actions.remove(AC_THROW);
		}
		return actions;
	}
}
