package com.shatteredpixel.shatteredpixeldungeon.items.quest;

import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

/**
 * Cargo handed to the hero by NPC_Guard during tutorial quest 1.
 * Delivered to the DeliveryDrone in a floor-6 shop; only exists during that quest.
 */
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
}
