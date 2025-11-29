package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;


import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barrier;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Doom;
import com.shatteredpixel.shatteredpixeldungeon.sprites.Mula_2Sprite;
import com.watabou.utils.Bundle;

//패턴 : 믈라의 몸통은 생존해있다면 주기적으로 머리+꼬리에 보호막을 부여한다
public class IsharmlaSeabornBody extends Mob {
    {
        spriteClass = Mula_2Sprite.class;

        HP = HT = 1000;

        defenseSkill = 25;

        properties.add(Property.SEA);
        properties.add(Property.BOSS);
        properties.add(Property.IMMOVABLE);

        state = HUNTING;
    }
    

    // 모든 믈라 파츠가 파괴되면 사망
    private boolean isDead = false;
    private int cooldown = 8;

    @Override
    public int defenseSkill(Char enemy) {
        if (isDead) return INFINITE_EVASION;
        else return 20;
    }

    // 공격불가
    @Override
    protected boolean canAttack(Char enemy) {
        return false;
    }

    // 몸통은 주기적으로 보호막을 부여한다. 체력이 1이하가 되면 스킬사용 불가
    @Override
    protected boolean act() {

        sprite.turnTo(pos, 999999);
        rooted = true;

        if (isDead) return super.act();

        if (cooldown > 0) cooldown--;
        else {
            for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
                if (mob instanceof IsharmlaSeabornHead || mob instanceof IsharmlaSeabornBody || mob instanceof IsharmlaSeabornTail)
                    Buff.affect(mob, Barrier.class).setShield(80);
            }
            if (Dungeon.isChallenged(Challenges.DECISIVE_BATTLE)) cooldown = 5;
            else cooldown = 8;
        }

        return super.act();
    }

    @Override
    public void damage(int dmg, Object src) {

        if (isDead) return;

        super.damage(dmg, src);

        if (HP < 1) {
            isDead = true;
            Buff.affect(this, Doom.class);
            Dungeon.mulaCount++;
        }
    }


    @Override
    public void die(Object cause) { }

    private static final String IS_DEAD_MID   = "isDeadMid";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put( IS_DEAD_MID, isDead);
    }

    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);

        isDead = bundle.getBoolean(IS_DEAD_MID);
    }
    }





