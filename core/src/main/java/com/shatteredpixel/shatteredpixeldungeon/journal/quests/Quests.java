package com.shatteredpixel.shatteredpixeldungeon.journal.quests;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;

import java.util.ArrayList;

/**
 * Static registry + event dispatcher for QuestLines. Bundled with the run, like Notes.
 * Populated only in-run; the dispatcher no-ops safely off-run.
 */
public class Quests {

    private static ArrayList<QuestLine> quests = new ArrayList<>();

    public static void reset() {
        quests = new ArrayList<>();
    }

    public static void add(QuestLine q) {
        quests.add(q);
    }

    /**
     * first quest of the given class in any state; used for once-per-run gating.
     */
    @SuppressWarnings("unchecked")
    public static <T extends QuestLine> T get(Class<T> cls) {
        if (quests == null) return null;
        for (QuestLine q : quests) {
            if (cls.isInstance(q)) return (T) q;
        }
        return null;
    }

    /**
     * ongoing quests, in insert order. Never null; empty off-run.
     */
    public static ArrayList<QuestLine> ongoing() {
        ArrayList<QuestLine> result = new ArrayList<>();
        if (quests == null) return result;
        for (QuestLine q : quests) {
            if (q.state == QuestLine.State.ONGOING) result.add(q);
        }
        return result;
    }

    // --- dispatcher: fan each event out to every ONGOING quest ---
    public static void onMobKilled(Object cause) {
        if (Dungeon.hero == null) return;
        for (QuestLine q : ongoing()) q.onMobKilled(cause);
        notifyClaimable();
    }

    public static void onGoldCollected(int amount) {
        if (Dungeon.hero == null) return;
        for (QuestLine q : ongoing()) q.onGoldCollected(amount);
        notifyClaimable();
    }

    public static void onChestOpened() {
        if (Dungeon.hero == null) return;
        for (QuestLine q : ongoing()) q.onChestOpened();
        notifyClaimable();
    }

    public static void onFoodEaten(Item food) {
        if (Dungeon.hero == null) return;
        for (QuestLine q : ongoing()) q.onFoodEaten(food);
        notifyClaimable();
    }

    public static void onNewFloorReached() {
        if (Dungeon.hero == null) return;
        for (QuestLine q : ongoing()) q.onNewFloorReached();
        notifyClaimable();
    }

    /**
     * One-time chat nudge the moment a counter quest's objective is met.
     */
    private static void notifyClaimable() {
        for (QuestLine q : ongoing()) {
            if (q.claimable() && !q.notifiedClaimable) {
                q.notifiedClaimable = true;
                GLog.p(Messages.get(Quests.class, "claimable", q.name()));
            }
        }
    }

    // --- bundling: single "questlines" collection, like Notes.RECORDS ---
    private static final String QUESTLINES = "questlines";

    public static void storeInBundle(Bundle bundle) {
        bundle.put(QUESTLINES, quests);
    }

    public static void restoreFromBundle(Bundle bundle) {
        quests = new ArrayList<>();
        if (bundle.contains(QUESTLINES)) {
            for (Bundlable q : bundle.getCollection(QUESTLINES)) {
                if (q instanceof QuestLine) quests.add((QuestLine) q);
            }
        }
    }
}
