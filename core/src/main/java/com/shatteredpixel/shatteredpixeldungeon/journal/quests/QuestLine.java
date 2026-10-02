package com.shatteredpixel.shatteredpixeldungeon.journal.quests;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.Bag;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Image;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;

import java.util.ArrayList;

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
    protected boolean objectiveComplete = false;  // latch: once met it stays met; later events can't un-complete it

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
     * Something granted on step completion. Computed on demand in stepRewards(); never bundled.
     */
    public interface Reward {
        void grant();
    }

    public static class ItemReward implements Reward {
        private final Item item;

        public ItemReward(Item item) {
            this.item = item;
        }

        @Override
        public void grant() {
            if (item == null) return;
            Dungeon.level.drop(item, Dungeon.hero.pos).sprite.drop();
        }
    }

    public static class ExpReward implements Reward {
        private final int exp;

        public ExpReward(int exp) {
            this.exp = exp;
        }

        @Override
        public void grant() {
            Dungeon.hero.earnExp(exp, QuestLine.class);
        }
    }

    /**
     * rewards for completing the given step; empty list = none. Null list, empty list and null
     * elements are all no-ops.
     */
    protected ArrayList<Reward> stepRewards(int step) {
        return new ArrayList<>();
    }

    /**
     * convenience for the overwhelmingly common item-only case
     */
    protected static ArrayList<Reward> items(Item... items) {
        ArrayList<Reward> list = new ArrayList<>();
        for (Item i : items) if (i != null) list.add(new ItemReward(i));
        return list;
    }

    /**
     * Complete the current step: grant its rewards and advance; mark COMPLETED after the last.
     */
    public void advance() {
        ArrayList<Reward> rewards = stepRewards(step);
        if (rewards != null) {
            for (Reward r : rewards) {
                if (r != null) r.grant();
            }
        }
        progress = 0;
        if (++step >= stepCount()) {
            state = State.COMPLETED;
            GLog.p(Messages.get(Quests.class, "completed", name()));
        }
    }

    // --- turn-in helpers for fetch quests ---

    /**
     * total quantity of `type` the hero can actually access (respects LostInventory).
     */
    protected static int heldCount(Class<? extends Item> type) {
        if (Dungeon.hero == null) return 0;
        int total = 0;
        for (Item i : Dungeon.hero.belongings.getAllItems(type)) total += i.quantity();
        return total;
    }

    /**
     * Consume exactly `count` of `type`. All-or-nothing: consumes nothing and returns false
     * unless the full amount is present, so a failed turn-in can never eat a partial stack.
     * Only safe for types that cannot be equipped: detachAll never searches the equip slots, so an
     * equipped match would be counted, silently not removed, and still reported consumed.
     */
    protected static boolean consume(Class<? extends Item> type, int count) {
        if (count <= 0) return true;
        if (heldCount(type) < count) return false;

        Bag backpack = Dungeon.hero.belongings.backpack;
        int remaining = count;
        for (Item stack : Dungeon.hero.belongings.getAllItems(type)) {
            //bound take before detaching: detachAll leaves quantity at 1, so a quantity-driven loop never terminates
            int take = Math.min(remaining, stack.quantity());
            for (int i = 0; i < take; i++) stack.detach(backpack);
            remaining -= take;
            if (remaining == 0) break;
        }
        return true;
    }

    /**
     * total quantity of `type` the hero holds, LostInventory included. For cleanup and restore
     * bookkeeping, which must not be fooled into leaving (or duplicating) a quest item by the debuff.
     */
    protected static int heldCountIncludingLost(Class<? extends Item> type) {
        if (Dungeon.hero == null) return 0;
        int total = 0;
        for (Item i : Dungeon.hero.belongings) if (type.isInstance(i)) total += i.quantity();
        return total;
    }

    /**
     * Quest-only items: remove every instance so none linger once the quest ends. Ignores
     * LostInventory for the same reason heldCountIncludingLost does.
     */
    protected static void consumeAll(Class<? extends Item> type) {
        if (Dungeon.hero == null) return;
        Bag backpack = Dungeon.hero.belongings.backpack;
        //collect first: the belongings iterator walks the live bags, so detaching mid-iteration would fail
        ArrayList<Item> matches = new ArrayList<>();
        for (Item i : Dungeon.hero.belongings) if (type.isInstance(i)) matches.add(i);
        for (Item i : matches) i.detachAll(backpack);
    }

    // --- journal-claim support (counter quests) ---

    // Live condition for a counter objective (e.g. progress >= target); override in counter quests. Only latches, never un-latches.
    protected boolean objectiveMet() {
        return false;
    }

    // Latch objectiveComplete the first time the objective is met; called by the Quests dispatcher after every event.
    public void refreshCompletion() {
        if (state == State.ONGOING && objectiveMet()) {
            objectiveComplete = true;
        }
    }

    public boolean claimable() {
        return state == State.ONGOING && objectiveComplete;
    }      // true => show enabled Claim button

    public void claim() {
        if (claimable()) advance();
    }

    // --- abandon ---
    public void abandon() {
        state = State.ABANDONED;
        onAbandoned();
    }

    /**
     * Resume a declined quest on the step it was abandoned at: abandoning turns the offer down, it
     * does not use the quest up. Step and progress are kept, so the giver of the current step picks
     * up where it left off; COMPLETED never reopens, so a run still pays out at most once.
     */
    public boolean reopen() {
        if (state != State.ABANDONED) return false;
        state = State.ONGOING;
        //drop the completion latch and re-derive it, so a counter that regressed while declined isn't claimable
        notifiedClaimable = false;
        objectiveComplete = false;
        onReopened();
        refreshCompletion();
        return true;
    }

    //counterpart of onAbandoned(): restore whatever it cleaned up, so the current step stays doable
    protected void onReopened() {
    }

    /**
     * Reopen only if abandoned on the given step. Call it from the NPC that handed that step out: the
     * step's target must ignore an abandoned quest until its giver takes it back up.
     */
    public boolean resumeAt(int step) {
        return state == State.ABANDONED && this.step == step && reopen();
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
    private static final String OBJECTIVE_COMPLETE = "objective_complete";

    @Override
    public void storeInBundle(Bundle bundle) {
        bundle.put(STATE, state.name());
        bundle.put(STEP, step);
        bundle.put(PROGRESS, progress);
        bundle.put(NOTIFIED_CLAIMABLE, notifiedClaimable);
        bundle.put(OBJECTIVE_COMPLETE, objectiveComplete);
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
        objectiveComplete = bundle.getBoolean(OBJECTIVE_COMPLETE);
    }
}
