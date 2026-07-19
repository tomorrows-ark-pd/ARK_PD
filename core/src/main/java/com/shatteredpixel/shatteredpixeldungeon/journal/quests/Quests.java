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
        pendingMessages.clear();
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
        flushMessages();
    }

    public static void onGoldCollected(int amount) {
        if (Dungeon.hero == null) return;
        for (QuestLine q : ongoing()) q.onGoldCollected(amount);
        notifyClaimable();
        flushMessages();
    }

    public static void onChestOpened() {
        if (Dungeon.hero == null) return;
        for (QuestLine q : ongoing()) q.onChestOpened();
        notifyClaimable();
        flushMessages();
    }

    public static void onFoodEaten(Item food) {
        if (Dungeon.hero == null) return;
        for (QuestLine q : ongoing()) q.onFoodEaten(food);
        notifyClaimable();
        flushMessages();
    }

    public static void onNewFloorReached() {
        if (Dungeon.hero == null) return;
        for (QuestLine q : ongoing()) q.onNewFloorReached();
        //no flush here: fires during level transition before GameLog exists; GameScene.create() flushes instead.
        notifyClaimable();
    }

    // Latch completion and queue a one-time nudge when a counter objective is first met; queued so flushMessages() can log it once a GameLog is live.
    private static void notifyClaimable() {
        for (QuestLine q : ongoing()) {
            q.refreshCompletion();
            if (q.claimable() && !q.notifiedClaimable) {
                q.notifiedClaimable = true;
                pendingMessages.add(Messages.get(Quests.class, "claimable", q.name()));
            }
        }
    }

    // Deferred nudges: GLog has no backlog, so completions that latch off-scene queue here and flush once a log is live.
    private static ArrayList<String> pendingMessages = new ArrayList<>();

    public static void flushMessages() {
        if (Dungeon.hero == null) {
            pendingMessages.clear();
            return;
        }
        for (String msg : pendingMessages) GLog.p(msg);
        pendingMessages.clear();
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
