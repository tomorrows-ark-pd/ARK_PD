package com.shatteredpixel.shatteredpixeldungeon.items.wands.SP;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.TomorrowRogueNight;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Amok;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barrier;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bleeding;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Charm;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Chill;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Corrosion;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Corruption;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Doom;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Drowsy;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Frost;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hex;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicalSleep;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Ooze;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.PinCushion;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Poison;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Roots;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Slow;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.SoulMark;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vulnerable;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Weakness;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Bee;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.King;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mimic;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Piranha;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Statue;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Swarm;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Wraith;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Yog;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.NPC;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.items.Skill.SK2.AncientKin;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MagesStaff;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.TargetHealthIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

import java.util.HashMap;

public class StaffOfCorrupting extends Wand {
    private static ItemSprite.Glowing COL = new ItemSprite.Glowing(0xA9A9A9);

    {
        image = ItemSpriteSheet.WAND_CORRUPTION;
    }

    @Override
    public ItemSprite.Glowing glowing() {
        return COL;
    }

    //Note that some debuffs here have a 0% chance to be applied.
    // This is because the wand of corruption considers them to be a certain level of harmful
    // for the purposes of reducing resistance, but does not actually apply them itself

    private static final float MINOR_DEBUFF_WEAKEN = 1 / 4f;
    private static final HashMap<Class<? extends Buff>, Float> MINOR_DEBUFFS = new HashMap<>();

    static {
        MINOR_DEBUFFS.put(Weakness.class, 2f);
        MINOR_DEBUFFS.put(Vulnerable.class, 2f);
        MINOR_DEBUFFS.put(Cripple.class, 1f);
        MINOR_DEBUFFS.put(Blindness.class, 1f);
        MINOR_DEBUFFS.put(Terror.class, 1f);

        MINOR_DEBUFFS.put(Chill.class, 0f);
        MINOR_DEBUFFS.put(Ooze.class, 0f);
        MINOR_DEBUFFS.put(Roots.class, 0f);
        MINOR_DEBUFFS.put(Vertigo.class, 0f);
        MINOR_DEBUFFS.put(Drowsy.class, 0f);
        MINOR_DEBUFFS.put(Bleeding.class, 0f);
        MINOR_DEBUFFS.put(Burning.class, 0f);
        MINOR_DEBUFFS.put(Poison.class, 0f);
    }

    private static final float MAJOR_DEBUFF_WEAKEN = 1 / 2f;
    private static final HashMap<Class<? extends Buff>, Float> MAJOR_DEBUFFS = new HashMap<>();

    static {
        MAJOR_DEBUFFS.put(Amok.class, 3f);
        MAJOR_DEBUFFS.put(Slow.class, 2f);
        MAJOR_DEBUFFS.put(Hex.class, 2f);
        MAJOR_DEBUFFS.put(Paralysis.class, 1f);

        MAJOR_DEBUFFS.put(Charm.class, 0f);
        MAJOR_DEBUFFS.put(MagicalSleep.class, 0f);
        MAJOR_DEBUFFS.put(SoulMark.class, 0f);
        MAJOR_DEBUFFS.put(Corrosion.class, 0f);
        MAJOR_DEBUFFS.put(Frost.class, 0f);
        MAJOR_DEBUFFS.put(Doom.class, 0f);
    }

    @Override
    protected void onZap(Ballistica bolt) {
        Char ch = Actor.findChar(bolt.collisionPos);

        if (ch != null) {

            if (!(ch instanceof Mob)) {
                return;
            }

            Mob enemy = (Mob) ch;

            float corruptingPower = 3 + buffedLvl() / 2f;

            //base enemy resistance is usually based on their exp, but in special cases it is based on other criteria
            float enemyResist = 1 + enemy.EXP;
            if (ch instanceof Mimic || ch instanceof Statue) {
                enemyResist = 1 + Dungeon.depth;
            } else if (ch instanceof Piranha || ch instanceof Bee) {
                enemyResist = 1 + Dungeon.depth / 2f;
            } else if (ch instanceof Wraith) {
                //divide by 5 as wraiths are always at full HP and are therefore ~5x harder to corrupt
                enemyResist = (1f + Dungeon.depth / 3f) / 5f;
            } else if (ch instanceof Yog.BurningFist || ch instanceof Yog.RottingFist) {
                enemyResist = 1 + 30;
            } else if (ch instanceof Yog.Larva || ch instanceof King.Undead) {
                enemyResist = 1 + 5;
            } else if (ch instanceof Swarm) {
                //child swarms don't give exp, so we force this here.
                enemyResist = 1 + 3;
            }

            enemyResist *= 0.75f + 4 * Math.pow(enemy.HP / (float) enemy.HT, 2);

            //debuffs placed on the enemy reduce their resistance
            for (Buff buff : enemy.buffs()) {
                if (MAJOR_DEBUFFS.containsKey(buff.getClass()))
                    enemyResist *= (1f - MAJOR_DEBUFF_WEAKEN);
                else if (MINOR_DEBUFFS.containsKey(buff.getClass()))
                    enemyResist *= (1f - MINOR_DEBUFF_WEAKEN);
                else if (buff.type == Buff.buffType.NEGATIVE)
                    enemyResist *= (1f - MINOR_DEBUFF_WEAKEN);
            }

            //cannot re-corrupt or doom an enemy, so give them a major debuff instead
            if (enemy.buff(Corruption.class) != null || enemy.buff(Doom.class) != null) {
                corruptingPower = enemyResist - 0.001f;
            }

            if (corruptingPower > enemyResist) {
                corruptEnemy(enemy);
            } else {
                float debuffChance = corruptingPower / enemyResist;
                if (Random.Float() < debuffChance) {
                    debuffEnemy(enemy, MAJOR_DEBUFFS);
                } else {
                    debuffEnemy(enemy, MINOR_DEBUFFS);
                }
            }

            processSoulMark(ch, chargesPerCast());
            Sample.INSTANCE.play(Assets.Sounds.HIT_MAGIC, 1, 0.8f * Random.Float(0.87f, 1.15f));

        } else {
            Dungeon.level.pressCell(bolt.collisionPos);
        }
    }

    private void debuffEnemy(Mob enemy, HashMap<Class<? extends Buff>, Float> category) {

        //do not consider buffs which are already assigned, or that the enemy is immune to.
        HashMap<Class<? extends Buff>, Float> debuffs = new HashMap<>(category);
        for (Buff existing : enemy.buffs()) {
            if (debuffs.containsKey(existing.getClass())) {
                debuffs.put(existing.getClass(), 0f);
            }
        }
        for (Class<? extends Buff> toAssign : debuffs.keySet()) {
            if (debuffs.get(toAssign) > 0 && enemy.isImmune(toAssign)) {
                debuffs.put(toAssign, 0f);
            }
        }

        //all buffs with a > 0 chance are flavor buffs
        Class<? extends FlavourBuff> debuffCls = (Class<? extends FlavourBuff>) Random.chances(debuffs);

        if (debuffCls != null) {
            Buff.append(enemy, debuffCls, 6 + buffedLvl() * 3);
        } else {
            //if no debuff can be applied (all are present), then go up one tier
            if (category == MINOR_DEBUFFS) debuffEnemy(enemy, MAJOR_DEBUFFS);
            else if (category == MAJOR_DEBUFFS) corruptEnemy(enemy);
        }
    }

    private void corruptEnemy(Mob enemy) {
        //cannot re-corrupt or doom an enemy, so give them a major debuff instead
        if (enemy instanceof NPC) return;
        if (enemy.buff(Corruption.class) != null || enemy.buff(Doom.class) != null) {
            GLog.w(Messages.get(this, "already_corrupted"));
            return;
        }

        if (!enemy.isImmune(Corruption.class)) {
            enemy.HP = enemy.HT;
            for (Buff buff : enemy.buffs()) {
                if (buff.type == Buff.buffType.NEGATIVE
                        && !(buff instanceof SoulMark)) {
                    buff.detach();
                } else if (buff instanceof PinCushion) {
                    buff.detach();
                }
            }

            AncientKin.Seaborn seaborn = new AncientKin.Seaborn();
            seaborn.pos = enemy.pos;
            seaborn.HT = seaborn.HP = Math.max(5, 5 + enemy.maxLvl);
            int sbuff = Math.max(3, 3 + buffedLvl() * 3);
            Buff.affect(seaborn, Barrier.class).incShield(sbuff);
            Buff.affect(seaborn, Weakness.class, 60f - buffedLvl() * 3);
            Buff.affect(seaborn, SeabornRegen.class).setLevel(buffedLvl());
            try {
                enemy.rollToDropLoot();
                enemy.onConvertKilled(Dungeon.hero);
                enemy.destroy();
                if (enemy.sprite != null) enemy.sprite.killAndErase();
                TargetHealthIndicator.instance.target(null);
                GameScene.add(seaborn);
            } catch (RuntimeException e) {
                TomorrowRogueNight.reportException(
                        new RuntimeException("StaffOfCorrupting Seaborn conversion failed; victim="
                                + enemy.getClass().getSimpleName(), e));
                throw e;
            }


        } else {
            Buff.affect(enemy, Doom.class);
        }
    }

    @Override
    public void onHit(MagesStaff staff, Char attacker, Char defender, int damage) {
        // lvl 0 - 25%
        // lvl 1 - 40%
        // lvl 2 - 50%
        if (Random.Int(buffedLvl() + 4) >= 3) {
            Buff.prolong(defender, Amok.class, 4 + buffedLvl() * 2);
        }
    }

    @Override
    protected void fx(Ballistica bolt, Callback callback) {
        MagicMissile.boltFromChar(curUser.sprite.parent,
                MagicMissile.SHADOW,
                curUser.sprite,
                bolt.collisionPos,
                callback);
        Sample.INSTANCE.play(Assets.Sounds.ZAP);
    }

    public static class SeabornRegen extends Buff {

        private int level = 0;

        private static final float REGEN_DELAY = 10;

        public SeabornRegen setLevel(int lvl) {
            level = lvl;
            return this;
        }

        @Override
        public boolean act() {
            if (target.isAlive()) {
                if (target.HP < target.HT) {
                    int healAmt = 1 + level / 2;
                    target.HP = Math.min(target.HT, target.HP + healAmt);
                }
                spend(REGEN_DELAY);
            } else {
                diactivate();
            }
            return true;
        }

        private static final String LEVEL = "level";

        @Override
        public void storeInBundle(Bundle bundle) {
            super.storeInBundle(bundle);
            bundle.put(LEVEL, level);
        }

        @Override
        public void restoreFromBundle(Bundle bundle) {
            super.restoreFromBundle(bundle);
            level = bundle.getInt(LEVEL);
        }
    }

}
