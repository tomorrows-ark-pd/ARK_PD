package com.shatteredpixel.shatteredpixeldungeon.journal.quests;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.QuestCat;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.AquaBlast;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.ForceCatalyst;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.PhaseShift;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Recycle;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.Image;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;

/**
 * Phantom's lost cat. Spans two NPCs on two Rhodes floors, so it is standalone rather than
 * nested on either giver.
 *
 * <pre>
 * step 0 - find Miss Christine somewhere in Rhodes      reward: none (the shadow hands out the cat)
 * step 1 - bring her back to Phantom on Rhodes floor 2  reward: 2 items, per rewardVariant
 * </pre>
 *
 * Registrable from either end: Phantom offers it, and stumbling on the cat first starts it too.
 */
public class PhantomCatQuestLine extends QuestLine {

    public static final int STEP_FIND    = 0;
    public static final int STEP_RETURN  = 1;

    //reward pair, rolled at registration; deliberately independent of Dungeon.QuestCatPoint
    private int rewardVariant = Random.Int(2);

    /**
     * true while the cat should still be placed on its floor; drives the level spawn gate.
     */
    public static boolean catStillOut() {
        PhantomCatQuestLine q = Quests.get(PhantomCatQuestLine.class);
        //ABANDONED counts: declining is reversible, so a floor generated meanwhile must still host her
        return q == null || (q.state != State.COMPLETED && q.step == STEP_FIND);
    }

    /**
     * For a pre-port save carrying a cat with no questline: adopt it straight at the turn-in step.
     */
    public static PhantomCatQuestLine adoptOrphanCat() {
        PhantomCatQuestLine q = new PhantomCatQuestLine();
        q.step = STEP_RETURN;
        return q;
    }

    /**
     * For a pre-port save that already finished the quest: its shadow is long dead and the Rhodes
     * floors are already generated, so a re-offer could never be completed.
     */
    public static PhantomCatQuestLine alreadyCompleted() {
        PhantomCatQuestLine q = new PhantomCatQuestLine();
        q.step = q.stepCount();
        q.state = State.COMPLETED;
        return q;
    }

    /**
     * Hand over the cat and complete the turn-in step; false if the hero isn't holding one.
     */
    public boolean tryTurnIn() {
        if (!at(STEP_RETURN) || heldCount(QuestCat.class) < 1) return false;
        consumeAll(QuestCat.class);   //quest-only item: leave nothing behind
        advance();
        return true;
    }

    @Override
    public Image icon() {
        return new ItemSprite(ItemSpriteSheet.CAT);
    }

    @Override
    protected int stepCount() {
        return 2;
    }

    @Override
    public String progressText() {
        if (step != STEP_RETURN) return null;
        return Math.min(heldCount(QuestCat.class), 1) + "/1";
    }

    @Override
    protected ArrayList<Reward> stepRewards(int step) {
        if (step != STEP_RETURN) return items();
        if (rewardVariant == 0) {
            return items(new AquaBlast().quantity(2), new Recycle().quantity(2));
        }
        return items(new ForceCatalyst(), new PhaseShift().quantity(3));
    }

    @Override
    protected void onAbandoned() {
        consumeAll(QuestCat.class);
    }

    @Override
    protected void onReopened() {
        //abandoning took her back, so hand her over again or the turn-in step is uncompletable.
        //silent collect, not doPickUp: only Phantom can reopen this step and he turns it in immediately
        if (step == STEP_RETURN && heldCountIncludingLost(QuestCat.class) < 1) {
            QuestCat cat = new QuestCat();
            if (!cat.collect(Dungeon.hero.belongings.backpack)) {
                Dungeon.level.drop(cat, Dungeon.hero.pos).sprite.drop();
            }
        }
    }

    private static final String REWARD_VARIANT = "reward_variant";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(REWARD_VARIANT, rewardVariant);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        rewardVariant = bundle.getInt(REWARD_VARIANT);
    }
}
