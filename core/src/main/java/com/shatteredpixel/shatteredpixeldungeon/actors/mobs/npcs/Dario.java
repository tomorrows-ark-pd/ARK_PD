package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.FloatingSeaDrifter;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.SeaCapsule;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.SeaReaper;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.food.SanityPotion;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfStrength;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.NPC_DarioSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndQuest;
import com.watabou.noosa.Game;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Dario extends NPC {
    {
        spriteClass = NPC_DarioSprite.class;
        properties.add(Property.NPC);
        intelligentAlly = true;

        HP = HT = 1000;

        alignment = Alignment.ALLY;
        WANDERING = new Wandering();
        state = WANDERING;

        //before other mobs
        actPriority = MOB_PRIO + 1;
    }

    private int initialPos = -1;

    private boolean seenBefore  = false;
    private boolean encouraged  = false;
    private boolean completed   = false;
    
    @Override
    public int defenseSkill(Char enemy) {
        return Quest.given && Quest.prepared ? 15 : INFINITE_EVASION;
    }

    @Override
    public boolean canAttack( Char enemy ) {
        return Quest.given && Quest.prepared && super.canAttack(enemy);
    }

    @Override
    public int attackSkill(Char target) {
        return 40;
    }

    @Override
    public int attackProc(Char enemy, int damage) {
        if (enemy instanceof Mob) ((Mob)enemy).aggro(this);
        return super.attackProc(enemy, damage);
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange(15, 34);
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(0, 12);
    }

    @Override
    protected boolean act() {
        if (!Quest.given || !Quest.prepared) {
            if (!seenBefore) {
                yell(Messages.get(this, "announce")); // 해당 층에 들어오면 위험대사를 외칩니다
            }
            seenBefore = true;
        } else {
            seenBefore = false;

            if (!encouraged && Quest.killCount >= 5) {
                yell(Messages.get(this, "almost"));
                encouraged = true;
            } else if (!completed && Quest.isQuestComplete()) {
                yell(Messages.get(this, "complete"));
                completed = true;
            }
        }
        return super.act();
    }

    @Override
    public boolean interact(Char c) {
        sprite.turnTo(pos, c.pos);

        if (c != Dungeon.hero){
            return true;
        }

        if (Quest.isQuestComplete()) {
            yell(Messages.get(this, "thank"));
            Quest.dropReward(this);
            GLog.p(Messages.get(this, "success")); // 접촉시 고마워 대사 출력

            destroy(); // 삭제
            sprite.die();

            for (Mob mob : Dungeon.level.mobs) {
                mob.beckon(mob.pos);
            }

        } else if (Quest.given && Quest.prepared) {
            String msg = Messages.get(this, "reminder");
            Quest.pullMobs(this.pos);
            Game.runOnRenderThread(new Callback() {
                @Override
                public void call() {
                    GameScene.show(new WndQuest(Dario.this, msg));
                }
            });
            super.interact(c);
        } else if (Quest.given) {
            yell(Messages.get(this, "start"));

            Quest.prepared = true;
            Quest.startQuest(this.pos);

            state = WANDERING;
            initialPos = pos;
        } else {
            String msg = "";
            switch (Dungeon.hero.heroClass) {
                case WARRIOR:
                    msg += Messages.get(this, "intro_warrior");
                    break;
                case ROGUE:
                    msg += Messages.get(this, "intro_rogue");
                    break;
                case MAGE:
                    msg += Messages.get(this, "intro_mage", Dungeon.hero.heroClass.title());
                    break;
                case HUNTRESS:
                    msg += Messages.get(this, "intro_huntress");
                    break;
                case ROSECAT:
                    msg += Messages.get(this, "intro_rosecat");
                    break;
                case NEARL:
                    msg += Messages.get(this, "intro_nearl");
                    break;
                case CHEN:
                    msg += Messages.get(this, "intro_chen");
                    break;
            }

            msg += Messages.get(this, "intro");
            final String msgFinal = msg;

            Game.runOnRenderThread(new Callback() {
                @Override
                public void call() {
                    GameScene.show(new WndQuest(Dario.this, msgFinal));
                }
            });

            Buff.detach(this, Invisibility.class);
            Quest.given = true;
        }
        return true;
    }

    private static final String INITIAL_POS = "initialPos";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(INITIAL_POS, initialPos);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        initialPos = bundle.getInt(INITIAL_POS);
    }

    @Override
    public void die( Object cause ) {
        yell(Messages.get(this, "die"));
        super.die(cause);
    }

    private class Wandering extends Mob.Wandering {
        @Override
        public boolean act( boolean enemyInFOV, boolean justAlerted) {
            if (enemyInFOV) {
                enemySeen = true;

                notice();

                alerted = true;
                state = HUNTING;
                target = enemy.pos;
            } else {
                enemySeen = false;

                int oldPos = pos;
                target = initialPos;
                if (getCloser( target )) {
                    spend( 1 / speed() );
                    return moveSprite( oldPos, pos );
                } else {
                    spend( TICK );
                }
            }
            return true;
        }
    }

    public static class Quest {
        private static boolean given;
        private static boolean prepared;
        private static int killCount;

        public static void reset() {
            given = false;
            prepared = false;
            killCount = 0;
        }

        private static final String NODE    = "dario";
        private static final String GIVEN   = "given";
        private static final String PREPARED = "prepared";
        private static final String KILL_COUNT = "killCount";

        public static void storeInBundle( Bundle bundle ) {
            Bundle node = new Bundle();

            node.put(GIVEN, given);
            node.put(PREPARED, prepared);
            node.put(KILL_COUNT, killCount);

            bundle.put( NODE, node );
        }

        public static void restoreFromBundle( Bundle bundle ) {
            Bundle node = bundle.getBundle( NODE );

            if (!node.isNull()) {
                given = node.getBoolean( GIVEN );
                prepared = node.getBoolean( PREPARED );
                killCount = node.getInt( KILL_COUNT );
            } else {
                reset();
            }
        }

        public static void startQuest(int pos) {
            HashSet<Mob> questMobs = spawnAdditionalMobs(Dungeon.level);
            for (Mob mob : questMobs) {
                do {
                    // 시야 밖 랜덤 위치에서 스폰
                    mob.pos = Dungeon.level.randomRespawnCell(mob);
                }
                while (Dungeon.level.heroFOV[mob.pos]);
                mob.state = mob.WANDERING;
                GameScene.add(mob, Random.Int(1, 10));
                mob.beckon(pos);
            }
            killCount = 0;
        }

        public static void pullMobs(int pos) {
            for (Mob mob : Dungeon.level.mobs) {
                if (mob.isAlive() && (mob.state != mob.SLEEPING || mob.state != mob.FLEEING)) {
                    mob.beckon(pos);
                }
            }
        }

        public static boolean isQuestComplete() {
            if (!given || !prepared) {
                return false;
            } else {
                return killCount >= 8;
            }
        }

        public static void process() {
            if (given && prepared && killCount < 8) {
                killCount ++;
            }
        }

        public static void spawnDario(Level level, int pos) {
            Dario dario = new Dario();
            dario.pos = pos;
            dario.initialPos = pos;

            dario.seenBefore = false;
            dario.encouraged = false;
            dario.completed = false;

            Buff.append(dario, Invisibility.class, 999f);
            level.mobs.add(dario);
        }

        public static void dropReward(Dario dario) {
            List<Item> rewards = new ArrayList<>();

            rewards.add(new PotionOfStrength().quantity(1));
            rewards.add(new ScrollOfUpgrade().quantity(1));
            rewards.add(new SanityPotion().quantity(5));

            for (Item item : rewards) {
                if (item.doPickUp( Dungeon.hero )) {
                    GLog.i( Messages.get(Dungeon.hero, "you_now_have", item) );
                } else {
                    Dungeon.level.drop( item, dario.pos ).sprite.drop();
                }
            }

        }

        private static HashSet<Mob> spawnAdditionalMobs(Level level) {
            HashSet<Mob> mobs = new HashSet<>();
            mobs.add(new FloatingSeaDrifter());
            mobs.add(new FloatingSeaDrifter());
            mobs.add(new SeaReaper());
            mobs.add(new SeaReaper());
            mobs.add(new SeaReaper());
            mobs.add(new SeaCapsule());
            mobs.add(new SeaCapsule());
            mobs.add(new SeaCapsule());

            return mobs;
        }
    }
}
