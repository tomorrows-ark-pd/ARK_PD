package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.QuestScroll.QuestObjective;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.shatteredpixel.shatteredpixeldungeon.journal.quests.QuestLine;
import com.shatteredpixel.shatteredpixeldungeon.journal.quests.Quests;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DobermannSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndQuest;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class Dobermann extends NPC {
    {
        spriteClass = DobermannSprite.class;
        properties.add(Char.Property.IMMOVABLE);
        properties.add(Property.NPC);
    }

    private boolean questGiven = false;

    @Override
    public int defenseSkill(Char enemy) {
        return INFINITE_EVASION;
    }

    @Override
    public void damage(int dmg, Object src) {
    }

    @Override
    public boolean reset() {
        return true;
    }

    @Override
    public boolean interact(Char c) {
        sprite.turnTo(pos, c.pos);

        if (c != Dungeon.hero) {
            return super.interact(c);
        }

        //once-per-run: gate on questGiven, plus a belt-and-suspenders check for old saves
        if (questGiven || Quests.get(Quest.class) != null) {
            sprite.showStatus(CharSprite.NEGATIVE, Messages.get(this, "has_quest"));
            return true;
        }

        //auto-start: roll a quest, register it in the journal, and show the offer text
        final Quest quest = Quest.createRandom();
        Quests.add(quest);
        questGiven = true;

        final String offerText = Messages.get(this, "offer_" + quest.objective.name().toLowerCase(), quest.target);
        Game.runOnRenderThread(new Callback() {
            @Override
            public void call() {
                GameScene.show(new WndQuest(Dobermann.this, offerText));
            }
        });
        return true;
    }

    private static final String QUEST_GIVEN = "quest_given";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(QUEST_GIVEN, questGiven);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        questGiven = bundle.getBoolean(QUEST_GIVEN);
        //old saves may hold a PENDING_QUEST entry here; it is simply never read again
    }

    public static void spawn(Level level, int ppos) {
        Dobermann perro = new Dobermann();
        perro.pos = ppos;
        level.mobs.add(perro);
    }

    //--- the journal quest offered by Dobermann (the Imp.Quest-style nested shape) ---
    public static class Quest extends QuestLine {

        public QuestObjective objective;
        public int target;

        public Quest() {}

        public static Quest createRandom() {
            Quest q = new Quest();
            QuestObjective[] objectives = QuestObjective.values();
            q.objective = objectives[Random.Int(objectives.length)];
            q.target = q.objective.rollTarget();
            q.progress = 0;
            return q;
        }

        @Override
        public Image icon() {
            return new ItemSprite(ItemSpriteSheet.QUEST_SCROLL);
        }

        @Override
        public String objectiveDesc() {
            return Messages.get(this, "obj_" + objective.name().toLowerCase(), target);
        }

        // DESCEND_RATIONLESS trails floor count by one: credit for a floor only counts once left without eating.
        public int currentProgress() {
            if (objective == null) return 0;
            if (objective == QuestObjective.DESCEND_RATIONLESS) {
                return Math.max(0, progress - 1);
            }
            return progress;
        }

        @Override
        public String progressText() {
            //once latched complete, show the full bar even if a later reset knocked the counter down
            int shown = objectiveComplete ? target : Math.min(currentProgress(), target);
            return shown + "/" + target;
        }

        @Override
        protected int stepCount() {
            return 1;
        }

        @Override
        protected Item stepReward(int step) {
            return new Gold(objective.rewardGold(target));
        }

        @Override
        protected boolean objectiveMet() {
            return currentProgress() >= target;
        }

        @Override
        public void onMobKilled(Object cause) {
            if (objective == null) return;
            switch (objective) {
                case SLAY_ENEMIES:
                    if (isHeroKill(cause)) progress++;
                    break;
                case RANGED_KILLS:
                    if (isRangedCause(cause)) progress++;
                    break;
                default:
            }
        }

        @Override
        public void onChestOpened() {
            if (objective == QuestObjective.OPEN_CHESTS) progress++;
        }

        @Override
        public void onFoodEaten(Item food) {
            if (objective == QuestObjective.DESCEND_RATIONLESS) progress = 0;
        }

        @Override
        public void onNewFloorReached() {
            if (objective == QuestObjective.DESCEND_RATIONLESS) progress++;
        }

        @Override
        public void onGoldCollected(int amount) {
            if (objective == QuestObjective.COLLECT_GOLD) progress += amount;
        }

        private static boolean isHeroKill(Object cause) {
            return cause == Dungeon.hero
                    || cause instanceof Wand
                    || cause instanceof MissileWeapon;
        }

        private static boolean isRangedCause(Object cause) {
            if (cause instanceof Wand) return true;
            return cause == Dungeon.hero
                    && Dungeon.hero.belongings.attackingWeapon() instanceof MissileWeapon;
        }

        private static final String OBJECTIVE = "objective";
        private static final String TARGET    = "target";

        @Override
        public void storeInBundle(Bundle bundle) {
            super.storeInBundle(bundle);
            if (objective != null) bundle.put(OBJECTIVE, objective.name());
            bundle.put(TARGET, target);
        }

        @Override
        public void restoreFromBundle(Bundle bundle) {
            super.restoreFromBundle(bundle);
            String objName = bundle.getString(OBJECTIVE);
            if (objName != null && !objName.isEmpty()) {
                try {
                    objective = QuestObjective.valueOf(objName);
                } catch (IllegalArgumentException e) {
                    objective = null;
                }
            }
            target = bundle.getInt(TARGET);
        }
    }
}
