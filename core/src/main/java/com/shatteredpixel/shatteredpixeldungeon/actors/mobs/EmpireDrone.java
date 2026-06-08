package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AllyBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Silence;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.TargetedCell;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BlastParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Stylus;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.Imperial_artillerySprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Camera;
import com.watabou.utils.Bundle;
import com.watabou.utils.GameMath;
import com.watabou.utils.Random;

public class EmpireDrone extends Mob {
    {
        spriteClass = Imperial_artillerySprite.class;

        HP = HT = 140;
        defenseSkill = 18;

        EXP = 35;
        maxLvl = 28;

        loot = new Stylus();
        lootChance = 1f;

        flying = true;

        baseSpeed = 0.5f;
        immunities.add(Silence.class);
        immunities.add(AllyBuff.class);
    }

    private int cooldown = 0;
    private int lastTargetPos = -1;
    private Char target;

    @Override
    public int damageRoll() {
        return Random.NormalIntRange(55, 65);
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(0, 20);
    }

    @Override
    protected boolean canAttack(Char enemy) {
        return false;
    }

    @Override
    protected boolean act() {
        getTarget();
        if (target == null) return super.act();
        if (cooldown <= 0) {
            if (lastTargetPos == -1) {
                if (state != HUNTING) return super.act();

                lastTargetPos = target.pos;
                sprite.parent.addToBack(new TargetedCell(lastTargetPos, 0xFF0000));
                sprite.zap(target.pos);

                // 몬스터 어그로
                for (Mob mob : Dungeon.level.mobs) {
                    if (mob.paralysed <= 0
                            && Dungeon.level.distance(pos, mob.pos) <= 7
                            && mob.state != mob.HUNTING) {
                        mob.beckon(target.pos);
                    }
                }

                // 패턴 딜레이 추가
                spend(GameMath.gate(TICK, Dungeon.hero.cooldown(), 2 * TICK));
                Dungeon.hero.interrupt();
                return true;
            } else {
                if (lastTargetPos == target.pos) {
                    int dmg = damageRoll() - target.drRoll();
                    target.damage(dmg, this);
                    target.sprite.burst(CharSprite.NEGATIVE, 10);
                    CellEmitter.center(lastTargetPos).burst(BlastParticle.FACTORY, 10);
                    Camera.main.shake(5, 0.5f);
                    cooldown = 1;
                    lastTargetPos = -1;
                    spend(TICK);

                    if (target == Dungeon.hero && !Dungeon.hero.isAlive()) {
                        Dungeon.fail(getClass());
                        GLog.n(Messages.get(this, "bomb_kill"));
                    }
                    return true;
                } else {
                    CellEmitter.center(lastTargetPos).burst(BlastParticle.FACTORY, 10);
                    Camera.main.shake(5, 0.5f);
                    cooldown = 1;
                    lastTargetPos = -1;
                }
            }

        } else cooldown--;

        return super.act();
    }

    // find new target IF target is null, target is dead, or target is no longer in vision
    // target is always the closest mob, prioritizing hero if multiple in same distance
    private void getTarget() {
        if (target != null && target.isAlive() && this.fieldOfView != null && this.fieldOfView[target.pos]) {
            return;
        }

        target = null;
        int distance = Integer.MAX_VALUE;
        for (Mob mob : Dungeon.level.mobs) {
            if (!mob.isAlive() || mob.alignment != Alignment.ALLY || this.fieldOfView == null || !this.fieldOfView[mob.pos])
                continue;

            int curDistance = mob.distance(this);
            if (curDistance < distance) {
                distance = curDistance;
                target = mob;
            }
        }
        if (Dungeon.hero.isAlive() && this.fieldOfView != null && this.fieldOfView[Dungeon.hero.pos]
                && Dungeon.hero.distance(this) <= distance) {
            target = Dungeon.hero;
        }
    }

    private static final String CD = "CoolDown";
    private static final String SKILLPOS = "LastPos";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(CD, cooldown);
        bundle.put(SKILLPOS, lastTargetPos);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        cooldown = bundle.getInt(CD);
        lastTargetPos = bundle.getInt(SKILLPOS);

    }
}
