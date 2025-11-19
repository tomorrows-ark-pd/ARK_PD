package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Camouflage;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.sprites.Sea_LeefSprite;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class SeaLeef extends Mob {
    {
        spriteClass = Sea_LeefSprite.class;

        HP = HT = 135;
        EXP = 18;
        maxLvl = 37;

        defenseSkill = 30;

        loot = Gold.class;
        lootChance = 0.28f;

        properties.add(Property.SEA);
    }

    int damageBonus = 0;

    @Override
    public int damageRoll() { return Random.NormalIntRange(16 + (damageBonus / 2), 24 + damageBonus); }

    @Override
    public int attackSkill( Char target ) {
        return 37;
    }

    @Override
    protected float attackDelay() {
        return super.attackDelay() * 0.5f;
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(0, 10);
    }

    @Override
    public int attackProc(Char enemy, int damage) {

        damageBonus = Math.min(damageBonus +3, 60);

        return super.attackProc(enemy, damage);
    }

    private static final String DMG = "dmgbouns";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(DMG, damageBonus);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        damageBonus = bundle.getInt(DMG);
    }

    @Override
    public void activateSeaTerror() {
        if (this.buff(Camouflage.class) == null) {
            Buff.affect(this, Camouflage.class, 1f);
        } else if (this.buff(Camouflage.class) != null) {
            Buff.prolong(this, Camouflage.class, 1f);
        }
    }
}
