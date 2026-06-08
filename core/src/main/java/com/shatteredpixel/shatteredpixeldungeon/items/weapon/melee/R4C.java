package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.CloserangeShot;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.IsekaiItem;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfSharpshooting;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfBlastWave;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.utils.Random;

public class R4C extends GunWeapon {
    {
        image = ItemSpriteSheet.R4C;
        hitSound = Assets.Sounds.HIT_AR;
        hitSoundPitch = 0.9f;

        FIRE_DELAY_MULT = 0.75f;
        bulletMax = 31;
        bullet = Random.Int(bulletMax / 2, bulletMax + 1);
        MIN_RANGE = 2;
        MAX_RANGE = 6;

        usesTargeting = true;

        defaultAction = AC_ZAP;

        tier = 5;
    }

    @Override
    public int fireMax() {
        return (int) 4
                + tier * 2
                + bulletTier * 3
                + level() * (tier + 1)
                + RingOfSharpshooting.levelDamageBonus(Dungeon.hero) * 2;
    }

    @Override
    public float getFireAcc(int from, int to) {
        int distance = getDistance(from, to);

        // 최대 사거리 10, 최소사거리 2, 유효 사거리 2-6, +50% 보정
        if (isWithinRange(distance)) {
            return 1.5f;
        } else if (distance > getMaxRange()) {
            return Math.max(0f, 1f - 0.2f * (distance - getMaxRange()));
        } else if (distance < getMinRange()) {
            return 0.67f;
        }

        return 1f;
    }

    @Override
    protected void specialFire(Char ch) {
        Ballistica trajectory = new Ballistica(curUser.pos, ch.pos, Ballistica.STOP_TARGET);
        trajectory = new Ballistica(trajectory.collisionPos, trajectory.path.get(trajectory.path.size() - 1), Ballistica.PROJECTILE);
        WandOfBlastWave.throwChar(ch, trajectory, 2); // 넉백 효과
    }

    @Override
    protected void onZap(Ballistica bolt) {
        CloserangeShot closerRange = Dungeon.hero.buff(CloserangeShot.class);
        float oldacc = ACC;
        boolean pala = false;
        boolean anyKill = false;
        try {
            Char ch = Actor.findChar(bolt.collisionPos);
            if (ch != null) {
                if (Dungeon.hero.belongings.getItem(IsekaiItem.class) != null
                        && Dungeon.hero.belongings.getItem(IsekaiItem.class).isEquipped(Dungeon.hero)
                        && ch.buff(Paralysis.class) != null) {
                    pala = true;
                }
                Buff.affect(Dungeon.hero, RangedAttackTracker.class);
                processGunHit(ch, 1f, true);
                if (!ch.isAlive()) anyKill = true;
            } else {
                Dungeon.level.pressCellGunfire(bolt.collisionPos);
            }
            postShotCleanup(closerRange, pala, anyKill);
        } finally {
            ACC = oldacc;
        }
    }

    @Override
    public String desc() {
       String info = Messages.get(this, "desc", bulletTier);
            if (Dungeon.hero.belongings.getItem(IsekaiItem.class) != null) {
                if (Dungeon.hero.belongings.getItem(IsekaiItem.class).isEquipped(Dungeon.hero))
                    info += "\n\n" + Messages.get( R4C.class, "setbouns");}


        return info;
    }
}
