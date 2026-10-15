package com.shatteredpixel.shatteredpixeldungeon.journal.quests;

import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.Image;

/**
 * The run's main goal: retrieve the Doctor (Amulet). Registered at run start, no reward.
 * Completes when the run is cleared via the Amulet or Kaltsit; removed if the hero hands it over and continues.
 */
public class AmuletQuestLine extends QuestLine {

    @Override
    public Image icon() {
        return new ItemSprite(ItemSpriteSheet.AMULET);
    }

    @Override
    protected int stepCount() {
        return 1;
    }

    @Override
    public boolean abandonable() {
        return false;
    }

    public static void complete() {
        AmuletQuestLine q = Quests.get(AmuletQuestLine.class);
        if (q != null && q.ongoing()) q.advance();
    }

    //hero handed the Doctor to Kaltsit and stayed for the bonus floors: the goal no longer applies
    public static void dismiss() {
        AmuletQuestLine q = Quests.get(AmuletQuestLine.class);
        if (q != null) Quests.remove(q);
    }
}
