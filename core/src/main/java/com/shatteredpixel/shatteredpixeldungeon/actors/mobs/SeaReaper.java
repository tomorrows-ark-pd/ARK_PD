package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Corruption;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.NervousImpairment;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Dario;
import com.shatteredpixel.shatteredpixeldungeon.items.food.SanityPotion;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfWealth;
import com.shatteredpixel.shatteredpixeldungeon.sprites.Sea_ReaperSprite;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class SeaReaper extends Mob {
    {
        spriteClass = Sea_ReaperSprite.class;

        HP = HT = 140;
        defenseSkill = 20;

        EXP = 15;
        maxLvl = 31;

        loot = new SanityPotion();
        lootChance = 0.1f;

        properties.add(Property.SEA);
    }

    boolean awake = false;
    boolean firstHit = false;

    @Override
    public int damageRoll() {
        return Random.NormalIntRange(26, 40);
    }

    @Override
    public int attackSkill(Char target) {
        return 35;
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(0, 16);
    }

    @Override
    public float speed() {
        if (awake) return super.speed() * 2f;
        return super.speed();
    }

    @Override
    public int defenseProc(Char enemy, int damage) {
        if (!awake) {
            awake = true;
            ((Sea_ReaperSprite) sprite).updateChargeState(awake);
        }

        return super.defenseProc(enemy, damage);
    }

    @Override
    protected boolean act() {
        if (!firstHit) {
            ((Sea_ReaperSprite) sprite).updateChargeState(awake);
            firstHit = true;
        }

        boolean isCorrupted = buff(Corruption.class) != null;
        if (awake && !isCorrupted) {
            for (int i = 0; i < PathFinder.NEIGHBOURS8.length; i++) {
                Char ch = findChar(pos + PathFinder.NEIGHBOURS8[i]);
                if (ch != null && ch.isAlive() && ch.alignment == Alignment.ALLY) {
                    Buff.affect(ch, NervousImpairment.class).sum(16);
                }
            }
        }

        return super.act();
    }

    @Override
    public void rollToDropLoot() {
        float healChance = 0.17f * RingOfWealth.dropChanceMultiplier(Dungeon.hero);
        if (Dungeon.hero.lvl <= maxLvl + 2 && Random.Float() < healChance) {
            Dungeon.level.drop(new PotionOfHealing(), pos).sprite.drop();
        }
        super.rollToDropLoot();
    }

    private static final String AWAKE = "awake";
    private static final String FIRST_HIT = "firstHit";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(AWAKE, awake);
        bundle.put(FIRST_HIT, firstHit);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        awake = bundle.getBoolean(AWAKE);
        firstHit = bundle.getBoolean(FIRST_HIT);
    }

    @Override
    public void die(Object cause) {
        super.die(cause);
        Dario.Quest.process();
    }

    @Override
    public void onConvertKilled(Object cause) {
        super.onConvertKilled(cause);
        Dario.Quest.process();
    }
}
