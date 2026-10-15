package com.shatteredpixel.shatteredpixeldungeon.journal.quests;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.Image;

/**
 * Goal for a bonus campaign (floors 31-40). One subclass per campaign, registered when the pilot
 * first flies the hero out, completed when that campaign's final boss falls. No reward.
 */
public abstract class BonusStageQuestLine extends QuestLine {

    @Override
    protected int stepCount() {
        return 1;
    }

    @Override
    public boolean abandonable() {
        return false;
    }

    //the campaign currently selected at Rhodes floor 4, mirroring Dungeon.newLevel()'s choice
    private static BonusStageQuestLine forSelectedStage() {
        if (Dungeon.extrastage_Gavial) return new GavialQuestLine();
        if (Dungeon.extrastage_Sea) return new SeaQuestLine();
        return new SiestaQuestLine();
    }

    //first departure locks in the campaign: floors 31+ are generated once, so later toggles can't change it
    public static void start() {
        if (Quests.get(BonusStageQuestLine.class) == null) Quests.add(forSelectedStage());
    }

    public static void complete(Class<? extends BonusStageQuestLine> cls) {
        BonusStageQuestLine q = Quests.get(cls);
        if (q != null && q.ongoing()) q.advance();
    }

    public static class SiestaQuestLine extends BonusStageQuestLine {
        @Override
        public Image icon() {
            return new ItemSprite(ItemSpriteSheet.SIESTA, null);
        }
    }

    public static class SeaQuestLine extends BonusStageQuestLine {
        @Override
        public Image icon() {
            return new ItemSprite(ItemSpriteSheet.IBERIA, null);
        }
    }

    public static class GavialQuestLine extends BonusStageQuestLine {
        @Override
        public Image icon() {
            return new ItemSprite(ItemSpriteSheet.GAVIAL, null);
        }
    }
}
