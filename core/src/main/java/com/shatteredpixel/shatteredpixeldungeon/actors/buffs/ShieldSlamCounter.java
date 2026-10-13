package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.SealOfLight;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfBlastWave;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.ActionIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.AttackIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class ShieldSlamCounter extends CounterBuff implements ActionIndicator.Action {

    public static final float CC_THRESHOLD = 0.5f;

    private boolean lastCharged = false;

    {
        type = buffType.POSITIVE;
    }

    private int getSealLevel() {
        if (!(target instanceof Hero)) return 0;
        Hero hero = (Hero) target;
        SealOfLight seal = hero.belongings.getItem(SealOfLight.class);
        return seal != null ? seal.level() : 0;
    }

    public float getCap() {
        return 50 + getSealLevel() * 5;
    }

    public float getMaxMultiplier() {
        return 1.5f + getSealLevel() * 0.05f;
    }

    public float getMultiplier() {
        float cap = getCap();
        float maxMult = getMaxMultiplier();
        float accumulated = Math.min(count(), cap);
        return Math.min(0.5f + (accumulated / cap) * (maxMult - 0.5f), maxMult);
    }

    public boolean isCharged() {
        return count() >= getCap() * CC_THRESHOLD;
    }

    @Override
    public boolean act() {
        // also displace a stale ShieldSlamCounter left in the indicator (e.g. from before a save reload)
        if (count() > 0 && (ActionIndicator.action == null
                || (ActionIndicator.action instanceof ShieldSlamCounter && ActionIndicator.action != this))) {
            ActionIndicator.setAction(this);
        }
        boolean charged = isCharged();
        if (charged != lastCharged) {
            lastCharged = charged;
            if (ActionIndicator.action == this) {
                ActionIndicator.refresh();
            }
        }
        spend(TICK);
        return true;
    }

    @Override
    public void detach() {
        super.detach();
        ActionIndicator.clearAction(this);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        // replace any stale instance left in ActionIndicator from before a reload (e.g. Amulet scene)
        if (count() > 0) ActionIndicator.setAction(this);
    }

    @Override
    public int icon() {
        return BuffIndicator.ARMOR;
    }

    @Override
    public void tintIcon(Image icon) {
        float fill = Math.min(count() / getCap(), 1f);
        icon.hardlight(fill * 0.6f, 0.6f + fill * 0.4f, 1f);
    }

    @Override
    public String toString() {
        return Messages.get(this, "name");
    }

    @Override
    public String desc() {
        int accumulated = (int) count();
        int cap = (int) getCap();
        int pct = Math.round(getMultiplier() * 100);
        return Messages.get(this, "desc", accumulated, cap, pct);
    }

    // ActionIndicator.Action

    @Override
    public Image getIcon() {
        ItemSprite icon = new ItemSprite(ItemSpriteSheet.ARTIFACT_NEARL, null);
        if (!isCharged()) {
            icon.hardlight(0.5f, 0.5f, 0.5f);
        }
        return icon;
    }

    @Override
    public void doAction() {
        // a stale instance targets a discarded Hero (old pos), so canAttack always fails; redirect to the live buff
        ShieldSlamCounter live = Dungeon.hero.buff(ShieldSlamCounter.class);
        if (live != this) {
            ActionIndicator.clearAction(this);
            if (live != null) {
                ActionIndicator.setAction(live);
                live.doAction();
            }
            return;
        }
        GameScene.selectCell(slamListener);
    }

    private void doShieldSlam(final Char enemy) {
        AttackIndicator.target(enemy);
        Hero hero = (Hero) target;

        if (enemy.defenseSkill(target) >= Char.INFINITE_EVASION) {
            enemy.sprite.showStatus(CharSprite.NEUTRAL, enemy.defenseVerb());
            Sample.INSTANCE.play(Assets.Sounds.MISS);
        } else if (enemy.isInvulnerable(target.getClass())) {
            enemy.sprite.showStatus(CharSprite.POSITIVE, Messages.get(Char.class, "invulnerable"));
            Sample.INSTANCE.play(Assets.Sounds.MISS);
        } else {
            // Damage based on SealOfLight level
            int sealLevel = getSealLevel();
            int dmg = Random.NormalIntRange(1 + sealLevel, 4 + sealLevel * 3);

            // Apply multiplier from accumulated damage
            float multiplier = getMultiplier();
            dmg = Math.round(dmg * multiplier);

            // SHIELD_OF_LIGHT bonus: +10% per point
            if (hero.hasTalent(Talent.SHIELD_OF_LIGHT)) {
                dmg = Math.round(dmg * (1f + hero.pointsInTalent(Talent.SHIELD_OF_LIGHT) * 0.1f));
            }

            dmg = enemy.defenseProc(target, dmg);
            dmg -= enemy.drRoll();
            dmg = Math.max(0, dmg);

            if (enemy.buff(Vulnerable.class) != null) {
                dmg *= 1.33f;
            }

            dmg = target.attackProc(enemy, dmg);
            enemy.damage(dmg, target);

            boolean charged = isCharged();

            // Stun (only when charge is at least 50%)
            if (enemy.isAlive() && charged) {
                Buff.affect(enemy, Paralysis.class, 2f);
                // Blindness during Radiant Knight
                if (hero.buff(RadiantKnight.class) != null) {
                    Buff.affect(enemy, Blindness.class, 2f);
                }
            }

            // Knockback (only when charge is at least 50%)
            if (enemy.isAlive() && charged) {
                Ballistica trajectory = new Ballistica(target.pos, enemy.pos, Ballistica.STOP_TARGET);
                trajectory = new Ballistica(trajectory.collisionPos, trajectory.path.get(trajectory.path.size() - 1), Ballistica.PROJECTILE);
                WandOfBlastWave.throwChar(enemy, trajectory, 1, true, true);
            }

            target.hitSound(Random.Float(0.87f, 1.15f));
            if (enemy.sprite != null) {
                enemy.sprite.bloodBurstA(target.sprite.center(), dmg);
                enemy.sprite.flash();
            }

            if (!enemy.isAlive()) {
                GLog.i(Messages.capitalize(Messages.get(Char.class, "defeat", enemy.name())));
            }
        }

        Invisibility.dispel();

        // Consume stacks
        Buff.detach(hero, ShieldSlamCounter.class);

        hero.spendAndNext(hero.attackDelay());
    }

    private CellSelector.Listener slamListener = new CellSelector.Listener() {
        @Override
        public void onSelect(Integer cell) {
            if (cell == null) return;
            final Char enemy = Actor.findChar(cell);
            if (enemy == null
                    || enemy == target
                    || !Dungeon.level.heroFOV[cell]
                    || target.isCharmedBy(enemy)
                    || enemy.alignment == Char.Alignment.ALLY) {
                GLog.w(Messages.get(ShieldSlamCounter.class, "bad_target"));
            } else if (!((Hero) target).canAttack(enemy)) {
                GLog.w(Messages.get(ShieldSlamCounter.class, "bad_target"));
            } else {
                Dungeon.hero.busy();
                target.sprite.attack(cell, new Callback() {
                    @Override
                    public void call() {
                        doShieldSlam(enemy);
                    }
                });
            }
        }

        @Override
        public String prompt() {
            return Messages.get(ShieldSlamCounter.class, "prompt");
        }
    };
}
