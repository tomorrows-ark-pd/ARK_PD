package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

/*
            if (hero.buff(NervousImpairment.class) == null) {
                Buff.affect(hero, NervousImpairment.class);
            }
              hero.buff(NervousImpairment.class).Sum(25);
 */

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.utils.Bundle;

public class NervousImpairment extends Buff {

    float currentDamage = 0;
    float limit = 100;

    public void sum(float nervousDamage) {
        currentDamage = Math.min(100, currentDamage + nervousDamage);
        if (currentDamage <= 0) detach();
        if (currentDamage >= limit) burst();
    }

    void burst() {

        if (Dungeon.extrastage_Sea && Dungeon.depth >= 40) {
            // 이샤믈라 보스전일 경우 명흔 신경손상 데미지 = 최대체력의 33%
            target.damage(target.HT / 3, this);
        } else {
            target.damage(target.HT / 4, this);
        }
        Buff.affect(target, Slow.class, 2f);
        Buff.affect(target, Weakness.class, 2f);

        this.detach();
    }

    @Override
    public int icon() {
        return BuffIndicator.IMPAIRMENT;
    }

    @Override
    public String toString() {
        return Messages.get(this, "name");
    }

    @Override
    public String desc() {
        return Messages.get(this, "desc", currentDamage);
    }

    private static final String POW = "Power";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(POW, currentDamage);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        currentDamage = bundle.getFloat(POW);
    }
}
