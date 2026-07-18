package com.shatteredpixel.shatteredpixeldungeon.journal.quests;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Image;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;

/**
 * Base class for journal-tracked quests: a sequence of steps, each with an objective and
 * optional reward. Ongoing quests appear in the journal Notes tab. Stored in the Quests
 * registry and bundled via Bundlable reflection; may live standalone or nested in an NPC.
 */
public abstract class QuestLine implements Bundlable {

    public enum State {ONGOING, COMPLETED, ABANDONED}

    public State state = State.ONGOING;
    protected int step = 0;       // current step index, 0-based
    protected int progress = 0;   // generic counter for counter-style steps
    protected boolean notifiedClaimable = false;  // guards the one-time "claim me" chat message

    public int step() {
        return step;
    }

    public boolean ongoing() {
        return state == State.ONGOING;
    }

    /**
     * true when this quest is ongoing and currently on the given step.
     */
    public boolean at(int step) {
        return state == State.ONGOING && this.step == step;
    }

    // --- journal presentation ---
    public String name() {
        return Messages.get(this, "name");
    }

    public abstract Image icon();                     // grid icon in NotesTab

    public String objectiveDesc() {                   // current objective, journal detail
        return Messages.get(this, "obj_" + step);
    }

    public String progressText() {
        return null;
    }     // e.g. "12/50"; null = no counter

    // --- step machinery ---
    protected abstract int stepCount();

    /**
     * reward for completing the given step; null = none. Dropped at hero pos by advance().
     */
    protected Item stepReward(int step) {
        return null;
    }

    /**
     * Complete the current step: drop its reward and advance; mark COMPLETED after the last.
     */
    public void advance() {
        Item reward = stepReward(step);
        if (reward != null) {
            Dungeon.level.drop(reward, Dungeon.hero.pos).sprite.drop();
        }
        progress = 0;
        if (++step >= stepCount()) {
            state = State.COMPLETED;
            GLog.p(Messages.get(Quests.class, "completed", name()));
        }
    }

    // --- journal-claim support (counter quests) ---
    public boolean claimable() {
        return false;
    }      // true => show enabled Claim button

    public void claim() {
        if (claimable()) advance();
    }

    // --- abandon ---
    public void abandon() {
        state = State.ABANDONED;
        onAbandoned();
    }

    protected void onAbandoned() {
    }                   // cleanup hook (e.g. remove quest items)

    // game event hooks: no-op defaults, override as needed; only dispatched while ONGOING
    public void onMobKilled(Object cause) {
    }

    public void onGoldCollected(int amount) {
    }

    public void onChestOpened() {
    }

    public void onFoodEaten(Item food) {
    }

    public void onNewFloorReached() {
    }

    // --- bundling: STATE, STEP, PROGRESS; subclasses call super and add extras ---
    private static final String STATE = "state";
    private static final String STEP = "step";
    private static final String PROGRESS = "progress";
    private static final String NOTIFIED_CLAIMABLE = "notified_claimable";

    @Override
    public void storeInBundle(Bundle bundle) {
        bundle.put(STATE, state.name());
        bundle.put(STEP, step);
        bundle.put(PROGRESS, progress);
        bundle.put(NOTIFIED_CLAIMABLE, notifiedClaimable);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        try {
            state = State.valueOf(bundle.getString(STATE));
        } catch (Exception e) {
            state = State.ONGOING;
        }
        step = bundle.getInt(STEP);
        progress = bundle.getInt(PROGRESS);
        notifiedClaimable = bundle.getBoolean(NOTIFIED_CLAIMABLE);
    }
}
